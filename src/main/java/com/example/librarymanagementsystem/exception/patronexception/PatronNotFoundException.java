package com.example.librarymanagementsystem.exception.patronexception;

public class PatronNotFoundException extends RuntimeException {
    public PatronNotFoundException(String message) {
        super(message);
    }
}