package com.example.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "borrowing_record_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @Column(name = "borrowing_date")
    private LocalDate borrowingDate;
    @Column(name = "return_date")
    private LocalDate returnDate;

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "id=" + id +
                ", bookId=" + book.getId() +
                ", patronId=" + patron.getId() +
                ", borrowingDate=" + borrowingDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
