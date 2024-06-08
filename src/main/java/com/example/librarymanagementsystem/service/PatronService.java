package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.PatronDto;

import java.util.List;

public interface PatronService {
    List<PatronDto> getAllPatrons();

    PatronDto getPatron(long bookId);

    PatronDto createPatron(PatronDto bookDto);

    PatronDto updatePatron(long bookId, PatronDto bookDto);

    void deletePatron(long bookId);
}
