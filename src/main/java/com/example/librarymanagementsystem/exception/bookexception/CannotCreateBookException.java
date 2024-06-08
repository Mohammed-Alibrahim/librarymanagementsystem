package com.example.librarymanagementsystem.exception.bookexception;

public class CannotCreateBookException extends RuntimeException {
    public CannotCreateBookException(String message) {
        super(message);
    }
}