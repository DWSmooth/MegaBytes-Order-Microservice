package com.smoothstack.ordermicroservice.exceptions;

public class OrderNotCancelableException extends Exception {

    public OrderNotCancelableException() {}

    public OrderNotCancelableException(String message) {
        super(message);
    }
    
}