package com.example.librarymanagementsystem.exception.bookexception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}