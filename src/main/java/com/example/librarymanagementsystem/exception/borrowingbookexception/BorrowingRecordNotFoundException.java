package com.example.librarymanagementsystem.exception.borrowingbookexception;

public class BorrowingRecordNotFoundException extends RuntimeException {
    public BorrowingRecordNotFoundException(String message) {
        super(message);
    }
}