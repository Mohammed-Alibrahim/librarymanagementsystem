package com.example.librarymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patrons")
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "patron_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @OneToMany(mappedBy = "patron", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords;
}
