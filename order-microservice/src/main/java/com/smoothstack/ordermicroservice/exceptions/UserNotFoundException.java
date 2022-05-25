package com.smoothstack.ordermicroservice.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {}

    public UserNotFoundException(String message) {
        super(message);
    }
    
}
