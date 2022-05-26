package com.smoothstack.ordermicroservice.exceptions;

public class NoAvailableDriversException extends Exception {
    public NoAvailableDriversException() {}

    public NoAvailableDriversException(String message) {
        super(message);
    }
}
