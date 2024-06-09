package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.BorrowingRecordDto;
import com.example.librarymanagementsystem.service.BorrowingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class BorrowingController {
    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowingRecordDto borrowBook(@PathVariable("bookId") long bookId,
                                         @PathVariable("patronId") long patronId) {

        return borrowingService.borrowBook(bookId, patronId);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public BorrowingRecordDto updatePatron(@PathVariable("bookId") long bookId,
                                           @PathVariable("patronId") long patronId) {

        return borrowingService.returnBook(bookId, patronId);
    }
}
