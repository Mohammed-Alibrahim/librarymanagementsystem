package com.example.librarymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BorrowingRecordDto {
    private Long id;
    private BookDto book;
    private PatronDto patron;
    private LocalDate borrowingDate;
    private LocalDate returnDate;
}
