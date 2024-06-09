package com.example.librarymanagementsystem.service.impl;

import com.example.librarymanagementsystem.dto.PatronDto;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.exception.patronexception.CannotDeletePatronException;
import com.example.librarymanagementsystem.exception.patronexception.InvalidPatronException;
import com.example.librarymanagementsystem.exception.patronexception.PatronNotFoundException;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
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

import static com.example.librarymanagementsystem.mapper.PatronMapper.convertToEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceImplTest {
    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    @Test
    void getAllPatrons_ShouldReturnAllPatrons() {
        // Given
        Patron patron1 = new Patron();
        patron1.setId(1L);
        patron1.setName("Patron 1");
        patron1.setMobile("0987654321");

        Patron patron2 = new Patron();
        patron2.setId(2L);
        patron2.setName("Patron 2");
        patron2.setMobile("0912345678");

        when(patronRepository.findAll()).thenReturn(List.of(patron1, patron2));

        // When
        List<PatronDto> allPatrons = patronService.getAllPatrons();

        // Then
        assertNotNull(allPatrons);
        assertEquals(2, allPatrons.size());
        verify(patronRepository).findAll();
    }

    @Test
    void getPatron_ShouldReturnPatron_WhenPatronExists() {
        // Given
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("Patron");
        patron.setMobile("0987654321");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        // When
        PatronDto patronDto = patronService.getPatron(1L);

        // Then
        assertNotNull(patronDto);
        assertEquals("Patron", patronDto.getName());
        assertEquals("0987654321", patronDto.getMobile());
        verify(patronRepository).findById(1L);
    }

    @Test
    void getPatron_ShouldThrowException_WhenPatronDoesNotExist() {
        // Given
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        PatronNotFoundException ex = assertThrows(PatronNotFoundException.class,
                () -> patronService.getPatron(1L));
        assertEquals("Patron not found with id 1", ex.getMessage());
        verify(patronRepository).findById(1L);
    }

    @Test
    void createPatron_ShouldCreateAndReturnPatron_WhenDataIsValid() {
        // Given
        PatronDto patronDto = new PatronDto();
        patronDto.setName("New Patron");
        patronDto.setMobile("0987654321");

        when(patronRepository.save(any(Patron.class))).thenReturn(convertToEntity(patronDto));

        // When
        PatronDto createdPatronDto = patronService.createPatron(patronDto);

        // Then
        assertNotNull(createdPatronDto);
        assertEquals("New Patron", createdPatronDto.getName());
        assertEquals("0987654321", createdPatronDto.getMobile());
        verify(patronRepository).save(any(Patron.class));
    }

    @NullSource
    @ValueSource(strings = "")
    @ParameterizedTest
    void createPatron_ShouldThrowException_WhenNameIsNullOrEmpty(String name) {
        // Given
        PatronDto patronDto = new PatronDto();
        patronDto.setName(name);
        patronDto.setMobile("0987654321");

        // When / Then
        InvalidPatronException ex = assertThrows(InvalidPatronException.class,
                () -> patronService.createPatron(patronDto));
        assertEquals("Patron name cannot be null or empty", ex.getMessage());
    }

    @NullSource
    @ValueSource(strings = "")
    @ParameterizedTest
    void createPatron_ShouldThrowException_WhenMobileIsNullOrEmpty(String mobile) {
        // Given
        PatronDto patronDto = new PatronDto();
        patronDto.setName("New Patron");
        patronDto.setMobile(mobile);

        // When / Then
        InvalidPatronException ex = assertThrows(InvalidPatronException.class,
                () -> patronService.createPatron(patronDto));
        assertEquals("Patron mobile cannot be null or empty", ex.getMessage());
    }

    @Test
    void updatePatron_ShouldUpdateAndReturnPatron_WhenPatronIsExistAndDataIsValid() {
        // Given
        Patron existingPatron = new Patron();
        existingPatron.setId(1L);
        existingPatron.setName("Existing Patron");
        existingPatron.setMobile("0987654321");

        PatronDto patronDto = new PatronDto();
        patronDto.setName("Updated Patron");
        patronDto.setMobile("0912345678");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(existingPatron);

        // When
        PatronDto updatedPatronDto = patronService.updatePatron(1L, patronDto);

        // Then
        assertNotNull(updatedPatronDto);
        assertEquals("Updated Patron", updatedPatronDto.getName());
        assertEquals("0912345678", updatedPatronDto.getMobile());
        verify(patronRepository).save(any(Patron.class));
    }

    @Test
    void updatePatron_ShouldThrowException_WhenPatronDoesNotExist() {
        // Given
        PatronDto patronDto = new PatronDto();
        patronDto.setName("Updated Patron");
        patronDto.setMobile("0987654321");

        // When / Then
        PatronNotFoundException ex = assertThrows(PatronNotFoundException.class,
                () -> patronService.updatePatron(1L, patronDto));
        assertEquals("Patron not found with id 1", ex.getMessage());
    }

    @Test
    void deletePatron_ShouldDeletePatron_WhenNoBorrowedBooks() {
        // Given
        when(patronRepository.existsById(1L)).thenReturn(true);
        when(borrowingRecordRepository.existsByPatronIdAndReturnDateIsNull(1L)).thenReturn(false);

        // When
        patronService.deletePatron(1L);

        // Then
        verify(patronRepository).deleteById(1L);
    }

    @Test
    void deletePatron_ShouldThrowException_WhenPatronHasBorrowedBooks() {
        // Given
        when(patronRepository.existsById(1L)).thenReturn(true);
        when(borrowingRecordRepository.existsByPatronIdAndReturnDateIsNull(1L)).thenReturn(true);

        // When / Then
        assertThrows(CannotDeletePatronException.class, () -> patronService.deletePatron(1L));
    }

    @Test
    void deletePatron_ShouldThrowException_WhenPatronDoesNotExist() {
        // Given
        when(patronRepository.existsById(1L)).thenReturn(false);

        // When / Then
        assertThrows(PatronNotFoundException.class, () -> patronService.deletePatron(1L));
    }
}