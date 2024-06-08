package com.example.librarymanagementsystem.exception.bookexception;

public class CannotDeleteBookException extends RuntimeException {
    public CannotDeleteBookException(String message) {
        super(message);
    }
}