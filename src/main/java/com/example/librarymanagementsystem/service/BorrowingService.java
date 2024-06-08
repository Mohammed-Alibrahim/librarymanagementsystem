package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BorrowingRecordDto;

import java.util.List;

public interface BorrowingService {
    List<BorrowingRecordDto> getAllBorrowingRecords();

    BorrowingRecordDto borrowBook(long bookId, long patronId);

    BorrowingRecordDto returnBook(long bookId, long patronId);
}
