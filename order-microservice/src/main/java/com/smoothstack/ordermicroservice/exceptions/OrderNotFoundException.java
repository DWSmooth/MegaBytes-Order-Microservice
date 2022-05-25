package com.smoothstack.ordermicroservice.exceptions;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException() {}

    public OrderNotFoundException(String message) {
        super(message);
    }
    
}
