package com.example.librarymanagementsystem.exception.patronexception;

public class CannotDeletePatronException extends RuntimeException {
    public CannotDeletePatronException(String message) {
        super(message);
    }
}