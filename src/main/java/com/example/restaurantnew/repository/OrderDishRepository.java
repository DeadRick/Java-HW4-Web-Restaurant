package com.example.restaurantnew.repository;

import com.example.restaurantnew.model.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {
    Optional<OrderDish> findById(Long id);
}