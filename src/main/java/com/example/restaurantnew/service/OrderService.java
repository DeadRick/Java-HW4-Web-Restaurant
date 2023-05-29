package com.example.restaurantnew.service;

import com.example.restaurantnew.dto.OrderDTO;
import com.example.restaurantnew.exception.OrderException;
import com.example.restaurantnew.model.*;
import com.example.restaurantnew.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final OrderRepository orderRepository;
    private final OrderDishRepository orderDishRepository;
    private final DishRepository dishRepository;


    public OrderService(UserRepository userRepository, SessionRepository sessionRepository, OrderRepository orderRepository, OrderDishRepository orderDishRepository, DishRepository dishRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.orderRepository = orderRepository;
        this.orderDishRepository = orderDishRepository;
        this.dishRepository = dishRepository;
    }

    public Map<String, Double> getMenu() {
        Map<String, Double> menu = new HashMap<>();
        for (Dish dish : dishRepository.findAll()) {
            if (dish.getAvailable()) {
                menu.put(dish.getName(), dish.getPrice());
            }
        }
        return menu;
    }


    public String makeOrder(OrderDTO orderDTO) throws OrderException {
        String token = orderDTO.getToken();

        if (sessionRepository.findBySessionToken(token) == null) {
            throw new OrderException("no such seesion");
        }
        Session session = sessionRepository.findBySessionToken(token);

        User user = userRepository.findById(session.getUserId()).orElseThrow(() -> new OrderException("No such user"));


        Map<Long, Long> dishesMap = orderDTO.getDishes();
        for (Map.Entry<Long, Long> dishElement : dishesMap.entrySet()) {
            Dish dish = dishRepository.findById(dishElement.getKey()).orElseThrow(() -> new OrderException("No such a dish"));
            if (dishElement.getValue() > dish.getQuantity()) {
                return "Not enough";
            }
        }

        Order order = new Order();
        order.setSpecialRequests(orderDTO.getSpecialRequests());

        order.setUserId(user.getUserId());
        order.setStatus("В ожидании");

        // Имитация приготовления блюда
        final Order orderTimer = order;
        orderRepository.save(order);
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                orderTimer.setStatus("Выполнен");
                orderRepository.save(order);
            }
        }, 10000);

        for (Map.Entry<Long, Long> dishElement : dishesMap.entrySet()) {
            OrderDish orderDish = new OrderDish();
            System.out.println(orderTimer.getId());
            orderDish.setOrderId(orderTimer.getId());
            orderDish.setDishId(dishElement.getKey());

            // Проверяем на существоание блюда
            Dish dish = dishRepository.findById(dishElement.getKey()).orElseThrow(() -> new OrderException("No such a dish"));
            if (dish == null) {
                return "No such a dish";
            }
            // Меняем кол-во доступных блюд
            dish.setQuantity(dish.getQuantity() - dishElement.getValue());
            if (dish.getQuantity() == 0) {
                dish.setAvailable(false);
            }
            orderDish.setPrice(dish.getPrice() * dishElement.getValue());
            orderDish.setQuantity(dishElement.getValue());
            dishRepository.save(dish);
            orderDishRepository.save(orderDish);
        }
        return "ok";
    }
}
