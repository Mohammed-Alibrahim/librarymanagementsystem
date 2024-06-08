package com.example.librarymanagementsystem.aspect;

import com.example.librarymanagementsystem.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.librarymanagementsystem.repository.BookRepository.save(..)) ||" +
            " execution(* com.example.librarymanagementsystem.repository.BookRepository.saveAndFlush(..))")
    public void bookUpdatePointcut() {
    }

    @AfterReturning(pointcut = "bookUpdatePointcut()", returning = "updatedBook")
    public void logBookUpdate(JoinPoint joinPoint, Book updatedBook) {
        if (updatedBook != null) {
            Long bookId = updatedBook.getId();
            log.info("Book with ID {} updated by method {}", bookId, joinPoint.getSignature().getName());
            logUpdatedFields(updatedBook);
        }
    }

    private void logUpdatedFields(Book book) {
        log.info("Updated book details:");
        log.info("Book ID: {}", book.getId());
        log.info("Title: {}", book.getTitle());
        log.info("Author: {}", book.getAuthor());
        log.info("ISBN: {}", book.getIsbn());
        log.info("Publication Year: {}", book.getPublicationYear());
    }
}