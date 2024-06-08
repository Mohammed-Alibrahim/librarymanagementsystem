package com.example.librarymanagementsystem.exception;

import com.example.librarymanagementsystem.exception.bookexception.BookAlreadyExistsException;
import com.example.librarymanagementsystem.exception.bookexception.BookNotFoundException;
import com.example.librarymanagementsystem.exception.bookexception.CannotCreateBookException;
import com.example.librarymanagementsystem.exception.bookexception.InvalidBookException;
import com.example.librarymanagementsystem.exception.borrowingbookexception.BorrowingRecordNotFoundException;
import com.example.librarymanagementsystem.exception.borrowingbookexception.CannotBorrowBookException;
import com.example.librarymanagementsystem.exception.patronexception.CannotCreatePatronException;
import com.example.librarymanagementsystem.exception.patronexception.InvalidPatronException;
import com.example.librarymanagementsystem.exception.patronexception.PatronAlreadyExistsException;
import com.example.librarymanagementsystem.exception.patronexception.PatronNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // General exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Book exception handlers
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotCreateBookException.class)
    public ResponseEntity<String> handleCannotCreateBookException(CannotCreateBookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> handleBookAlreadyExistsException(BookAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<String> handleInvalidBookException(InvalidBookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Patron exception handlers
    @ExceptionHandler(PatronNotFoundException.class)
    public ResponseEntity<String> handlePatronNotFoundException(PatronNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotCreatePatronException.class)
    public ResponseEntity<String> handleCannotCreatePatronException(CannotCreatePatronException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PatronAlreadyExistsException.class)
    public ResponseEntity<String> handlePatronAlreadyExistsException(PatronAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPatronException.class)
    public ResponseEntity<String> handleInvalidPatronException(InvalidPatronException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // BorrowingRecord exception handlers
    @ExceptionHandler(BorrowingRecordNotFoundException.class)
    public ResponseEntity<String> handleBorrowingRecordNotFoundException(BorrowingRecordNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotBorrowBookException.class)
    public ResponseEntity<String> handleCannotCreateBorrowingRecordException(CannotBorrowBookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
