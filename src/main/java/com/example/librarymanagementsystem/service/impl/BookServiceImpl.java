package com.example.librarymanagementsystem.service.impl;

import com.example.librarymanagementsystem.dto.BookDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.exception.bookexception.*;
import com.example.librarymanagementsystem.mapper.BookMapper;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.librarymanagementsystem.mapper.BookMapper.convertToDto;
import static com.example.librarymanagementsystem.mapper.BookMapper.convertToEntity;

@Slf4j
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getBook(long bookId) {
        log.info("my book {}", bookRepository.findById(bookId).orElse(null));

        return bookRepository.findById(bookId)
                .map(BookMapper::convertToDto)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));
    }

    @Override
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new BookAlreadyExistsException("Book with ISBN '" + bookDto.getIsbn() + "' is already exists");
        }

        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            throw new InvalidBookException("Book title cannot be null or empty");
        }

        if (bookDto.getAuthor() == null || bookDto.getAuthor().isEmpty()) {
            throw new InvalidBookException("Book author cannot be null or empty");
        }

        if (bookDto.getIsbn() == null || bookDto.getIsbn().isEmpty()) {
            throw new InvalidBookException("Book ISBN cannot be null or empty");
        }

        if (bookDto.getPublicationYear() == null) {
            throw new InvalidBookException("Book publication year cannot be null");
        }

        // check if using a valid ISBN
        if (isValidIsbn(bookDto.getIsbn())) {
            Book book = bookRepository.save(convertToEntity(bookDto));

            bookDto.setId(book.getId());
        } else {
            throw new CannotCreateBookException("Cannot create the book because it has an invalid ISBN");
        }

        return bookDto;
    }

    @Override
    @Transactional
    public BookDto updateBook(long bookId, BookDto bookDto) {
        if (bookRepository.existsById(bookId)) {
            if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
                throw new InvalidBookException("Book title cannot be null or empty");
            }

            if (bookDto.getAuthor() == null || bookDto.getAuthor().isEmpty()) {
                throw new InvalidBookException("Book author cannot be null or empty");
            }

            if (bookDto.getIsbn() == null || bookDto.getIsbn().isEmpty()) {
                throw new InvalidBookException("Book ISBN cannot be null or empty");
            }

            if (bookDto.getPublicationYear() == null) {
                throw new InvalidBookException("Book publication year cannot be null");
            }

            Book existingBook = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));

            if (!existingBook.getIsbn().equals(bookDto.getIsbn()) && bookRepository.existsByIsbn(bookDto.getIsbn())) {
                throw new BookAlreadyExistsException("Book with ISBN " + bookDto.getIsbn() + " is already exists");
            }

            // update the existing book with the provided data
            existingBook.setTitle(bookDto.getTitle());
            existingBook.setAuthor(bookDto.getAuthor());
            existingBook.setIsbn(bookDto.getIsbn());
            existingBook.setPublicationYear(bookDto.getPublicationYear());

            return convertToDto(bookRepository.save(existingBook));

        } else {
            throw new BookNotFoundException("Book not found with id " + bookId);
        }
    }

    @Override
    @Transactional
    public void deleteBook(long bookId) {
        if (bookRepository.existsById(bookId)) {
            if (borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(bookId)) {
                throw new CannotDeleteBookException("Cannot delete book because it is currently borrowed");
            }

            bookRepository.deleteById(bookId);

        } else {
            throw new BookNotFoundException("Book not found with id " + bookId);
        }
    }

    private boolean isValidIsbn(String isbn) {
        String isbnRegex = "^(?:ISBN(?:-13)?:? )?(?=\\d{13}$|(?=(?:\\d+[- ]){4})[- 0-9]{17}$)97[89][- ]" +
                "?\\d{1,5}[- ]?\\d+[- ]?\\d+[- ]?\\d$";

        Pattern pattern = Pattern.compile(isbnRegex);

        Matcher matcher = pattern.matcher(isbn);

        return matcher.matches();
    }
}
