package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookIdAndPatronId(long bookId, long patronId);

    boolean existsByBookIdAndReturnDateIsNull(long bookId);

    boolean existsByPatronIdAndReturnDateIsNull(long patronId);

    boolean existsByBookIdAndReturnDateIsNotNull(long bookId);

    void deleteByBookId(long bookId);

    boolean existsByPatronIdAndReturnDateIsNotNull(long patronId);

    void deleteByPatronId(long patronId);
}
