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
import com.example.librarymanagementsystem.service.BorrowingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.librarymanagementsystem.mapper.BorrowingRecordMapper.convertToDto;

@Slf4j
@AllArgsConstructor
@Service
public class BorrowingServiceImpl implements BorrowingService {
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Override
    @Transactional
    public BorrowingRecordDto borrowBook(long bookId, long patronId) {
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<Patron> patron = patronRepository.findById(patronId);

        if (book.isEmpty() && patron.isEmpty()) {
            throw new CannotBorrowBookException("Book and patron not found.");

        } else if (book.isEmpty()) {
            throw new CannotBorrowBookException("Book not found.");

        } else if (patron.isEmpty()) {
            throw new CannotBorrowBookException("Patron not found.");

        } else {
            Optional<BorrowingRecord> existingRecord = borrowingRecordRepository
                    .findByBookIdAndPatronId(bookId, patronId);

            if (existingRecord.isPresent() && existingRecord.get().getReturnDate() == null) {
                throw new CannotBorrowBookException("This book is already borrowed by the same patron.");
            }

            BorrowingRecord borrowingRecord = existingRecord.orElseGet(BorrowingRecord::new);

            borrowingRecord.setBook(book.get());
            borrowingRecord.setPatron(patron.get());
            borrowingRecord.setBorrowingDate(LocalDate.now());
            borrowingRecord.setReturnDate(null); // Ensure the return date is null when borrowed

            return convertToDto(borrowingRecordRepository.save(borrowingRecord));
        }
    }

    @Override
    @Transactional
    public BorrowingRecordDto returnBook(long bookId, long patronId) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId);

        if (borrowingRecord.isPresent()) {
            BorrowingRecord existingBorrowingRecord = borrowingRecord.get();

            if (existingBorrowingRecord.getReturnDate() != null) {
                throw new CannotReturnBookException("Book has already been returned.");
            }

            existingBorrowingRecord.setReturnDate(LocalDate.now());

            return convertToDto(borrowingRecordRepository.save(existingBorrowingRecord));

        } else {
            throw new CannotReturnBookException("Cannot return the book as it was not borrowed by this patron.");
        }
    }
}
