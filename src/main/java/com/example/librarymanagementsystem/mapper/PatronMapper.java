package com.example.librarymanagementsystem.mapper;

import com.example.librarymanagementsystem.dto.PatronDto;
import com.example.librarymanagementsystem.entity.Patron;

public class PatronMapper {
    private PatronMapper() {
    }

    public static Patron convertToEntity(PatronDto patronDto) {
        Patron patron = new Patron();

        patron.setName(patronDto.getName());
        patron.setMobile(patronDto.getMobile());

        return patron;
    }

    public static PatronDto convertToDto(Patron patron) {
        PatronDto patronDto = new PatronDto();

        patronDto.setId(patron.getId());
        patronDto.setName(patron.getName());
        patronDto.setMobile(patron.getMobile());

        return patronDto;
    }
}
