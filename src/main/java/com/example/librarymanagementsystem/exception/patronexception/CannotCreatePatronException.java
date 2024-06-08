package com.example.librarymanagementsystem.exception.patronexception;

public class CannotCreatePatronException extends RuntimeException {
    public CannotCreatePatronException(String message) {
        super(message);
    }
}