package com.example.restaurantnew.exception;

public class OrderException extends Exception {
    public OrderException(String not_such_user) {
        super(not_such_user);
    }
}
