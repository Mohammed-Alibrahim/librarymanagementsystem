package com.example.librarymanagementsystem.service.impl;

import com.example.librarymanagementsystem.dto.BorrowingRecordDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.BorrowingRecord;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.exception.borrowingbookexception.CannotBorrowBookException;
import com.example.librarymanagementsystem.exception.borrowingbookexception.CannotReturnBookException;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Test
    void borrowBook_ShouldThrowException_WhenBookAndPatronNotFound() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        CannotBorrowBookException ex = assertThrows(CannotBorrowBookException.class,
                () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Book and patron not found.", ex.getMessage());
    }

    @Test
    void borrowBook_ShouldThrowException_WhenBookNotFound() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(patronRepository.findById(1L)).thenReturn(Optional.of(new Patron()));

        // When / Then
        CannotBorrowBookException ex = assertThrows(CannotBorrowBookException.class,
                () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Book not found.", ex.getMessage());
    }

    @Test
    void borrowBook_ShouldThrowException_WhenPatronNotFound() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        CannotBorrowBookException ex = assertThrows(CannotBorrowBookException.class,
                () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Patron not found.", ex.getMessage());
    }

    @Test
    void borrowBook_ShouldThrowException_WhenBookNotBorrowed() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        CannotBorrowBookException ex = assertThrows(CannotBorrowBookException.class,
                () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("Patron not found.", ex.getMessage());
    }

    @Test
    void borrowBook_ShouldThrowException_WhenBookAlreadyBorrowedBySamePatron() {
        // Given
        Book book = new Book();
        Patron patron = new Patron();

        BorrowingRecord existingRecord = new BorrowingRecord();
        existingRecord.setBook(book);
        existingRecord.setPatron(patron);
        existingRecord.setReturnDate(null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L))
                .thenReturn(Optional.of(existingRecord));

        // When / Then
        CannotBorrowBookException ex = assertThrows(CannotBorrowBookException.class,
                () -> borrowingService.borrowBook(1L, 1L));
        assertEquals("This book is already borrowed by the same patron.", ex.getMessage());
    }

    @Test
    void borrowBook_ShouldCreateBorrowingRecord_WhenBookAndPatronAreValid() {
        // Given
        Book book = new Book();
        Patron patron = new Patron();

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setReturnDate(null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.empty());
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        // When
        BorrowingRecordDto borrowingRecordDto = borrowingService.borrowBook(1L, 1L);

        // Then
        assertNotNull(borrowingRecordDto);
        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
    }

    @Test
    void returnBook_ShouldThrowException_WhenBorrowingRecordNotFound() {
        // Given
        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.empty());

        // When / Then
        CannotReturnBookException ex = assertThrows(CannotReturnBookException.class,
                () -> borrowingService.returnBook(1L, 1L));
        assertEquals("Cannot return the book as it was not borrowed by this patron.", ex.getMessage());
    }

    @Test
    void returnBook_ShouldThrowException_WhenBookAlreadyReturned() {
        // Given
        Book book = new Book();
        Patron patron = new Patron();
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setReturnDate(LocalDate.now());

        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));

        // When / Then
        CannotReturnBookException ex = assertThrows(CannotReturnBookException.class,
                () -> borrowingService.returnBook(1L, 1L));
        assertEquals("Book has already been returned.", ex.getMessage());
    }

    @Test
    void returnBook_ShouldUpdateReturnDate_WhenBorrowingRecordIsValid() {
        // Given
        Book book = new Book();
        Patron patron = new Patron();
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setReturnDate(null);

        when(borrowingRecordRepository.findByBookIdAndPatronId(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        // When
        BorrowingRecordDto borrowingRecordDto = borrowingService.returnBook(1L, 1L);

        // Then
        assertNotNull(borrowingRecordDto);
        assertEquals(LocalDate.now(), borrowingRecord.getReturnDate());
        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
    }
}