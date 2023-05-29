package com.example.restaurantnew.controller;

import com.example.restaurantnew.dto.UserDTO;
import com.example.restaurantnew.model.Session;
import com.example.restaurantnew.model.User;
import com.example.restaurantnew.repository.SessionRepository;
import com.example.restaurantnew.repository.UserRepository;
import com.example.restaurantnew.util.JwtUtils;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public AuthController(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        String email = userDTO.getEmail();
        String username = userDTO.getUsername();

        // Проверка корректности Email
        if (!email.contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect email address!");
        }

        // Проверка существования пользователя с заданной электронной почтой
        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with the same email already exists");
        }

        // Проверка существования пользователя с заданным никнеймом
        if (userRepository.findByUsername(username) != null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with the same nickname already exists");
        }


        // Создание и сохранение нового пользователя
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(userDTO.getPassword());
        newUser.setRole(userDTO.getRole());

        // Задаём время создания
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        userRepository.save(newUser);

        return ResponseEntity.ok("User " + username + " registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        // Проверка существования пользователя по никнейму
        String username = userDTO.getUsername();
        User userByUsername = userRepository.findByUsername(username);
        if (userByUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        userByUsername.setUpdatedAt(LocalDateTime.now());

        // Проверка существования пользователя по email
        String email = userDTO.getEmail();
        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Проверка соответствия пароля
        String password = userDTO.getPassword();
        if (!userByUsername.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Создание токена сессии (JWT)
        String sessionToken = JwtUtils.generateSessionToken(email, JwtUtils.generateSecretKey());
        Session session = new Session();
        session.setUser(userByUsername);
        session.setSessionToken(sessionToken);
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        sessionRepository.save(session);

        // Обновление данных в userRepository (updated at)
        userRepository.save(userByUsername);

        return ResponseEntity.ok(sessionToken);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserByToken(@RequestHeader("Authorization") String token) {

        // Поиск токена в БД
        Session sessionUser = sessionRepository.findBySessionToken(token);
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Поиск пользователя по токену
        User userByToken = sessionUser.getUser();

        // Создание DTO для передачи информации о пользователе
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userByToken.getUsername());
        userDTO.setEmail(userByToken.getEmail());
        userDTO.setPassword("<password was hidden>");
        userDTO.setRole(userByToken.getRole());

        return ResponseEntity.ok(userDTO);
    }

}
