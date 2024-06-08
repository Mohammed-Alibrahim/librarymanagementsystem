package com.example.librarymanagementsystem.mapper;

import com.example.librarymanagementsystem.dto.BorrowingRecordDto;
import com.example.librarymanagementsystem.entity.BorrowingRecord;

public class BorrowingRecordMapper {
    private BorrowingRecordMapper() {
    }

    public static BorrowingRecord convertToEntity(BorrowingRecordDto borrowingRecordDto) {
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        borrowingRecord.setBook(BookMapper.convertToEntity(borrowingRecordDto.getBook()));
        borrowingRecord.setPatron(PatronMapper.convertToEntity(borrowingRecordDto.getPatron()));
        borrowingRecord.setBorrowingDate(borrowingRecordDto.getBorrowingDate());
        borrowingRecord.setReturnDate(borrowingRecordDto.getReturnDate());

        return borrowingRecord;
    }

    public static BorrowingRecordDto convertToDto(BorrowingRecord borrowingRecord) {
        BorrowingRecordDto borrowingRecordDto = new BorrowingRecordDto();

        borrowingRecordDto.setId(borrowingRecord.getId());
        borrowingRecordDto.setBook(BookMapper.convertToDto(borrowingRecord.getBook()));
        borrowingRecordDto.setPatron(PatronMapper.convertToDto(borrowingRecord.getPatron()));
        borrowingRecordDto.setBorrowingDate(borrowingRecord.getBorrowingDate());
        borrowingRecordDto.setReturnDate(borrowingRecord.getReturnDate());

        return borrowingRecordDto;
    }
}
