package com.example.librarymanagementsystem.exception.bookexception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}