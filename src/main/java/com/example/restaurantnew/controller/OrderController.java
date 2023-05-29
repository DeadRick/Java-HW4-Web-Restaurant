package com.example.restaurantnew.controller;

import com.example.restaurantnew.dto.DishDTO;
import com.example.restaurantnew.dto.OrderDTO;
import com.example.restaurantnew.exception.OrderException;
import com.example.restaurantnew.model.Dish;
import com.example.restaurantnew.model.Order;
import com.example.restaurantnew.repository.DishRepository;
import com.example.restaurantnew.repository.OrderDishRepository;
import com.example.restaurantnew.repository.OrderRepository;
import com.example.restaurantnew.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderRepository orderRepository;
    private OrderDishRepository orderDishRepository;
    private DishRepository dishRepository;

    private OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderDishRepository orderDishRepository, DishRepository dishRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderDishRepository = orderDishRepository;
        this.dishRepository = dishRepository;
        this.orderService = orderService;
    }

    // Создание заказа
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderRequest) throws OrderException {

        String condition = orderService.makeOrder(orderRequest);
        if (condition == "ok") {
            return ResponseEntity.ok("Order was created!");
        } else {
            return ResponseEntity.ok(condition);
        }
    }

    // Создание или обновление блюда в меню
    @PostMapping("/createmenu")
    public ResponseEntity<String> createMenu(@RequestBody DishDTO dishDTO) {
        Dish dish = dishRepository.findByName(dishDTO.getName());
        if (dish != null) {
            dish.setQuantity(dishDTO.getQuantity() + dish.getQuantity());
            dish.setUpdatedAt(LocalDateTime.now());
        } else {
            dish = new Dish();
            dish.setName(dishDTO.getName());
            dish.setDescription(dishDTO.getDescription());
            dish.setPrice(dishDTO.getPrice());
            dish.setQuantity(dishDTO.getQuantity());
            dish.setAvailable(dishDTO.getAvailable());
        }
        dishRepository.save(dish);
        return ResponseEntity.ok("Dish was added to the menu");
    }

    // Получение меню со списком блюд и цен
    @PostMapping("/getmenu")
    public ResponseEntity<Map<String, Double>> getMenu() {
        Map<String, Double> dishMenu = orderService.getMenu();
        return ResponseEntity.ok(dishMenu);
    }

    // Получение информации о заказе по его идентификатору
    @GetMapping("/getorder/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("No such order"));
            return ResponseEntity.ok(order);
        } catch (OrderException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

}

