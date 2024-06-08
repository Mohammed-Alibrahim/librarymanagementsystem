package com.example.librarymanagementsystem.service.impl;

import com.example.librarymanagementsystem.dto.PatronDto;
import com.example.librarymanagementsystem.entity.Patron;
import com.example.librarymanagementsystem.exception.bookexception.CannotDeleteBookException;
import com.example.librarymanagementsystem.exception.patronexception.CannotDeletePatronException;
import com.example.librarymanagementsystem.exception.patronexception.PatronNotFoundException;
import com.example.librarymanagementsystem.exception.patronexception.InvalidPatronException;
import com.example.librarymanagementsystem.mapper.PatronMapper;
import com.example.librarymanagementsystem.repository.BorrowingRecordRepository;
import com.example.librarymanagementsystem.repository.PatronRepository;
import com.example.librarymanagementsystem.service.PatronService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.librarymanagementsystem.mapper.PatronMapper.convertToDto;
import static com.example.librarymanagementsystem.mapper.PatronMapper.convertToEntity;

@Slf4j
@AllArgsConstructor
@Service
public class PatronServiceImpl implements PatronService {
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PatronDto> getAllPatrons() {
        return patronRepository.findAll().stream()
                .map(PatronMapper::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PatronDto getPatron(long patronId) {
        return patronRepository.findById(patronId)
                .map(PatronMapper::convertToDto)
                .orElseThrow(() -> new PatronNotFoundException("Patron not found with id " + patronId));
    }

    @Override
    @Transactional
    public PatronDto createPatron(PatronDto patronDto) {
        Patron patron = patronRepository.save(convertToEntity(patronDto));

        patronDto.setId(patron.getId());

        return patronDto;
    }

    @Override
    @Transactional
    public PatronDto updatePatron(long patronId, PatronDto patronDto) {
        if (patronRepository.existsById(patronId)) {
            if (patronDto.getName() == null || patronDto.getName().isEmpty()) {
                throw new InvalidPatronException("Patron name cannot be null or empty");
            }

            if (patronDto.getMobile() == null || patronDto.getMobile().isEmpty()) {
                throw new InvalidPatronException("Patron mobile cannot be null or empty");
            }

            Patron existingPatron = patronRepository.findById(patronId)
                    .orElseThrow(() -> new PatronNotFoundException("Patron not found with id " + patronId));

            // update the existing patron with the provided data
            existingPatron.setName(patronDto.getName());
            existingPatron.setMobile(patronDto.getMobile());

            return convertToDto(patronRepository.save(existingPatron));

        } else {
            throw new PatronNotFoundException("Patron not found with id " + patronId);
        }
    }

    @Override
    @Transactional
    public void deletePatron(long patronId) {
        if (patronRepository.existsById(patronId)) {
            // check if this patron has borrowed books
            if (borrowingRecordRepository.existsByPatronIdAndReturnDateIsNull(patronId)) {
                throw new CannotDeletePatronException("Cannot delete patron because it currently has borrowed books");
            }

            patronRepository.deleteById(patronId);

        } else {
            throw new PatronNotFoundException("Patron not found with id " + patronId);
        }
    }
}
