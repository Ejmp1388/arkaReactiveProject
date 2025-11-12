package com.msvc.orders.exception;


public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
