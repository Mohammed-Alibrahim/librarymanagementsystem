package com.example.librarymanagementsystem.exception.borrowingbookexception;

public class CannotBorrowBookException extends RuntimeException {
    public CannotBorrowBookException(String message) {
        super(message);
    }
}