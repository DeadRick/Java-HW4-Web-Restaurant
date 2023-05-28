package com.example.restaurantnew.controller;

import com.example.restaurantnew.dto.OrderDTO;
import com.example.restaurantnew.model.Order;
import com.example.restaurantnew.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderService) {
        this.orderRepository = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderRequest) {
        Order order = new Order();
        Long userId = orderRequest.getUserId();

        // Проверка на наличие клиента, сделавшего заказ
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(order);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this ID doesn't exist");
        }
        order.setUserId(userId);

        // Установка статуса заказа
        String status = orderRequest.getStatus();
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(order);

//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect order status");
        }
        order.setStatus(status);

        // Время создания заказа
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Устанавливаем специальный текст от пользователя
        order.setSpecialRequests(orderRequest.getSpecialRequests());

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
//        try {
//            Order order = orderRepository.getOrderById(orderId);
//            return ResponseEntity.ok(order);
//        } catch (OrderNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Other endpoints for updating or managing orders

}
