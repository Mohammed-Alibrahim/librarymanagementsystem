package com.example.librarymanagementsystem.exception.borrowingbookexception;

public class BorrowingRecordAlreadyExistsException extends RuntimeException {
    public BorrowingRecordAlreadyExistsException(String message) {
        super(message);
    }
}