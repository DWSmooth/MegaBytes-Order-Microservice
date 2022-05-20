package com.smoothstack.ordermicroservice.exceptions;

public class OrderNotUpdateableException extends Exception {

    public OrderNotUpdateableException() {}

    public OrderNotUpdateableException(String message) {
        super(message);
    }
    
}