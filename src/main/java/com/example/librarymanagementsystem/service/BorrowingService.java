package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BorrowingRecordDto;

public interface BorrowingService {
    BorrowingRecordDto borrowBook(long bookId, long patronId);

    BorrowingRecordDto returnBook(long bookId, long patronId);
}
