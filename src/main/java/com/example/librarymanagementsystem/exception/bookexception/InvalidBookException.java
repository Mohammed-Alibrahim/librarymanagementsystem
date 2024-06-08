package com.example.librarymanagementsystem.exception.bookexception;

public class InvalidBookException extends RuntimeException {
    public InvalidBookException(String message) {
        super(message);
    }
}