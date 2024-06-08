package com.example.librarymanagementsystem.mapper;

import com.example.librarymanagementsystem.dto.BookDto;
import com.example.librarymanagementsystem.entity.Book;

public class BookMapper {
    private BookMapper() {
    }

    public static Book convertToEntity(BookDto bookDto) {
        Book book = new Book();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setPublicationYear(bookDto.getPublicationYear());

        return book;
    }

    public static BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPublicationYear(book.getPublicationYear());

        return bookDto;
    }
}
