package com.smoothstack.ordermicroservice.exceptions;

public class UserMismatchException extends Exception {
    
    public UserMismatchException() {}

    public UserMismatchException(String message) {
        super(message);
    }
}
