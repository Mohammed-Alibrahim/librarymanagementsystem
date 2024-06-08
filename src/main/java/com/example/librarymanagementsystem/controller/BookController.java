package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.BookDto;
import com.example.librarymanagementsystem.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable("id") long bookId) {
        return bookService.getBook(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createClient(@RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable("id") long bookId, @RequestBody BookDto bookDto) {
        return bookService.updateBook(bookId, bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteBook(bookId);
    }
}
