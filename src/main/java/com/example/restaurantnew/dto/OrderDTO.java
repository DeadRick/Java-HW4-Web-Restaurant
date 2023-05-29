package com.example.restaurantnew.dto;


import java.util.Map;
import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private String token;
    private Long userId;
    private Map<Long, Long> dishes;
    private String status;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDTO() {

    }

    public Long getId() {
        return id;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<Long, Long> getDishes() {
        return dishes;
    }

    public void setDishes(Map<Long, Long> dishes) {
        this.dishes = dishes;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
