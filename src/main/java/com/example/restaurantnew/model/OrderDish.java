package com.example.restaurantnew.model;


import jakarta.persistence.*;

@Entity
@Table(name = "order_dish")
public class OrderDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "dish_id")
    private Long dishId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Double price;

    // Constructors, getters, and setters

    public OrderDish() {
    }

    public OrderDish(Long id, Long orderId, Long dishId, Long quantity, Double price) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
