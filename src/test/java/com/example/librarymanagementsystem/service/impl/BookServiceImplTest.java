package com.example.librarymanagementsystem.service.impl;

import com.example.librarymanagementsystem.dto.BookDto;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.exception.bookexception.*;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.librarymanagementsystem.mapper.BookMapper.convertToEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_ShouldReturnListOfBookDtos() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setPublicationYear(2000);
        book1.setIsbn("ISBN 978-0-596-52068-7");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book Two");
        book1.setAuthor("Author Two");
        book1.setPublicationYear(2000);
        book1.setIsbn("ISBN 978-0-596-52068-8");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // When
        List<BookDto> bookDtos = bookService.getAllBooks();

        // Then
        verify(bookRepository).findAll();
        assertEquals(2, bookDtos.size());
    }

    @Test
    void getBook_ShouldReturnBookDto_WhenBookExists() {
        // Given
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setPublicationYear(2000);
        book.setIsbn("ISBN 978-0-596-52068-7");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // When
        BookDto bookDto = bookService.getBook(1L);

        // Then
        verify(bookRepository).findById(1L);
        assertNotNull(bookDto);
        assertEquals(1L, bookDto.getId());
    }

    @Test
    void getBook_ShouldThrowException_WhenBookDoesNotExist() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When // Then
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.getBook(1L));
        assertEquals("Book not found with id 1", ex.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void createBook_ShouldSaveBook_WhenBookDataIsValid() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");

        when(bookRepository.save(any(Book.class))).thenReturn(convertToEntity(bookDto));
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);

        // When
        BookDto savedBook = bookService.createBook(bookDto);

        // Then
        verify(bookRepository).save(any(Book.class));
        assertNotNull(savedBook);
        assertEquals("Title", savedBook.getTitle());
        assertEquals("Author", savedBook.getAuthor());
        assertEquals(2000, savedBook.getPublicationYear());
        assertEquals("ISBN 978-0-596-52068-7", savedBook.getIsbn());
    }

    @Test
    void createBook_ShouldThrowException_WhenISBNIsAlreadyUsed() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");

        when(bookRepository.existsByIsbn(bookDto.getIsbn())).thenReturn(true);

        // When / Then
        BookAlreadyExistsException ex = assertThrows(BookAlreadyExistsException.class,
                () -> bookService.createBook(bookDto));
        assertEquals("Book with ISBN '" + bookDto.getIsbn() + "' is already exists", ex.getMessage());
    }

    @Test
    void createBook_ShouldThrowException_WhenISBNIsInvalid() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn("invalid_isbn");

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class, () -> bookService.createBook(bookDto));
        assertEquals("Book ISBN is invalid", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void createBook_ShouldThrowException_WhenTitleIsNullOrEmpty(String title) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class, () -> bookService.createBook(bookDto));
        assertEquals("Book title cannot be null or empty", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void createBook_ShouldThrowException_WhenAuthorIsNullOrEmpty(String author) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor(author);
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class, () -> bookService.createBook(bookDto));
        assertEquals("Book author cannot be null or empty", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void createBook_ShouldThrowException_WhenISBNIsNullOrEmpty(String isbn) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn(isbn);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class, () -> bookService.createBook(bookDto));
        assertEquals("Book ISBN cannot be null or empty", ex.getMessage());
    }

    @Test
    void createBook_ShouldThrowException_WhenPublicationYearIsNull() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(null);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class, () -> bookService.createBook(bookDto));
        assertEquals("Book publication year cannot be null", ex.getMessage());
    }

    @Test
    void updateBook_ShouldUpdateBookAndReturnBook_WhenBookExistsAndDataIsValid() {
        // Given
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Title");
        existingBook.setAuthor("Author");
        existingBook.setIsbn("ISBN 978-0-596-52068-7");
        existingBook.setPublicationYear(2020);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Title");
        bookDto.setAuthor("Updated Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-8");
        bookDto.setPublicationYear(2021);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbn(bookDto.getIsbn())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // When
        BookDto updatedBook = bookService.updateBook(1L, bookDto);

        // Then
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldUpdateBook_WhenBookExistsAndDataIsValidAndNotUpdatingIsbn() {
        // Given
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Title");
        existingBook.setAuthor("Author");
        existingBook.setIsbn("ISBN 978-0-596-52068-7");
        existingBook.setPublicationYear(2020);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Title");
        bookDto.setAuthor("Updated Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-7");
        bookDto.setPublicationYear(2021);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // When
        BookDto updatedBook = bookService.updateBook(1L, bookDto);

        // Then
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void updateBook_ShouldThrowException_WhenBookTitleIsNullOrEmpty(String title) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle(title);
        bookDto.setAuthor("Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-7");
        bookDto.setPublicationYear(2020);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book title cannot be null or empty", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void updateBook_ShouldThrowException_WhenBookAuthorIsNullOrEmpty(String author) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Book");
        bookDto.setAuthor(author);
        bookDto.setIsbn("ISBN 978-0-596-52068-7");
        bookDto.setPublicationYear(2020);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book author cannot be null or empty", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = {""})
    @ParameterizedTest
    void updateBook_ShouldThrowException_WhenISBNIsNullOrEmpty(String isbn) {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setPublicationYear(2000);
        bookDto.setIsbn(isbn);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book ISBN cannot be null or empty", ex.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookPublicationYearIsNull() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Book");
        bookDto.setAuthor("Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-7");
        bookDto.setPublicationYear(null);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book publication year cannot be null", ex.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookIsbnIsInvalid() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setIsbn("invalid_isbn");
        bookDto.setPublicationYear(2020);

        // When / Then
        InvalidBookException ex = assertThrows(InvalidBookException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book ISBN is invalid", ex.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookDoesNotExist() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthor("Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-7");
        bookDto.setPublicationYear(2020);

        // When / Then
        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book not found with id 1", ex.getMessage());
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookWithSameIsbnAlreadyExists() {
        // Given
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Title");
        existingBook.setAuthor("Author");
        existingBook.setIsbn("ISBN 978-0-596-52068-7");
        existingBook.setPublicationYear(2000);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Title");
        bookDto.setAuthor("Updated Author");
        bookDto.setIsbn("ISBN 978-0-596-52068-8"); // here we added the same ISBN for an existing book
        bookDto.setPublicationYear(2020);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.existsByIsbn("ISBN 978-0-596-52068-8")).thenReturn(true);

        // When / Then
        BookAlreadyExistsException ex = assertThrows(BookAlreadyExistsException.class,
                () -> bookService.updateBook(1L, bookDto));
        assertEquals("Book with ISBN '" + bookDto.getIsbn() + "' is already exists", ex.getMessage());
    }

    @Test
    void deleteBook_ShouldThrowException_WhenBookIsNotExist() {
        // Given
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
        assertEquals("Book not found with id 1", ex.getMessage());
    }

    @Test
    void deleteBook_ShouldThrowException_WhenBookIsBorrowed() {
        // Given
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(anyLong())).thenReturn(true);

        // When & Then
        CannotDeleteBookException ex = assertThrows(CannotDeleteBookException.class,
                () -> bookService.deleteBook(1L));
        assertEquals("Cannot delete book because it is currently borrowed", ex.getMessage());
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenBookIsExistAndNotBorrowed() {
        // Given
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(anyLong())).thenReturn(false);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository).deleteById(1L);
    }
}