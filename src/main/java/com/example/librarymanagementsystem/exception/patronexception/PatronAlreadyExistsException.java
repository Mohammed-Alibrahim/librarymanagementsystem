package com.example.librarymanagementsystem.exception.patronexception;

public class PatronAlreadyExistsException extends RuntimeException {
    public PatronAlreadyExistsException(String message) {
        super(message);
    }
}