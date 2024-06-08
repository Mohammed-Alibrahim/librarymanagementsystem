package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.PatronDto;
import com.example.librarymanagementsystem.service.PatronService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/patrons")
public class PatronController {
    private final PatronService patronService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PatronDto> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public PatronDto getPatron(@PathVariable("id") long patronId) {
        return patronService.getPatron(patronId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatronDto createClient(@RequestBody PatronDto patronDto) {
        return patronService.createPatron(patronDto);
    }

    @PutMapping("/{id}")
    public PatronDto updatePatron(@PathVariable("id") long patronId, @RequestBody PatronDto patronDto) {
        return patronService.updatePatron(patronId, patronDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatron(@PathVariable("id") long patronId) {
        patronService.deletePatron(patronId);
    }
}
