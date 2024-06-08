package com.example.librarymanagementsystem.exception.borrowingbookexception;

public class InvalidBorrowingRecordException extends RuntimeException {
    public InvalidBorrowingRecordException(String message) {
        super(message);
    }
}