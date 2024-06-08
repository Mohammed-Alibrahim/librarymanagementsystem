package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();

    BookDto getBook(long bookId);

    BookDto createBook(BookDto bookDto);

    BookDto updateBook(long bookId, BookDto bookDto);

    void deleteBook(long bookId);
}
