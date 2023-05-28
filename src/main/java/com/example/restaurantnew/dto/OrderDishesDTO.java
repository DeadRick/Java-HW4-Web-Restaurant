package com.example.restaurantnew.dto;

public class OrderDishesDTO {
    private Integer orderId;
    private Integer dishId;
    private Integer quantity;
    private Double price;

    public OrderDishesDTO() {
    }

    public OrderDishesDTO(Integer orderId, Integer dishId, Integer quantity, Double price) {
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
