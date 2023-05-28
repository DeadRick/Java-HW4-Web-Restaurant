package com.example.restaurantnew.repository;

import com.example.restaurantnew.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    // Дополнительный метод для поиска пользователя по имени пользователя
    User findByUsername(String username);
}
