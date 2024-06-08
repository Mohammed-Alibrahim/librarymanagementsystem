package com.example.librarymanagementsystem.exception.borrowingbookexception;

public class CannotReturnBookException extends RuntimeException{
    public CannotReturnBookException(String message) {
        super(message);
    }
}
