package com.example.librarymanagementsystem.aspect;

import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.entity.Patron;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.librarymanagementsystem.repository.BookRepository.save(..)) ||" +
            " execution(* com.example.librarymanagementsystem.repository.BookRepository.saveAndFlush(..))")
    public void bookSavePointcut() {
    }

    @AfterReturning(pointcut = "bookSavePointcut()", returning = "book")
    public void logBookSave(JoinPoint joinPoint, Book book) {
        if (book != null) {
            log.info("Book with ID {} saved by method {}", book.getId(), joinPoint.getSignature().getName());

            logNewBookFields(book);
        }
    }

    private void logNewBookFields(Book book) {
        log.info("New book details:");
        log.info("Book ID: {}", book.getId());
        log.info("Title: {}", book.getTitle());
        log.info("Author: {}", book.getAuthor());
        log.info("ISBN: {}", book.getIsbn());
        log.info("Publication Year: {}", book.getPublicationYear());
    }

    @Pointcut("execution(* com.example.librarymanagementsystem.repository.PatronRepository.save(..)) ||" +
            " execution(* com.example.librarymanagementsystem.repository.PatronRepository.saveAndFlush(..))")
    public void patronSavePointcut() {
    }

    @AfterReturning(pointcut = "patronSavePointcut()", returning = "patron")
    public void logPatronSave(JoinPoint joinPoint, Patron patron) {
        if (patron != null) {
            log.info("Patron with ID {} saved by method {}", patron.getId(), joinPoint.getSignature().getName());

            logNewPatronFields(patron);
        }
    }

    private void logNewPatronFields(Patron patron) {
        log.info("New patron details:");
        log.info("Patron ID: {}", patron.getId());
        log.info("Name: {}", patron.getName());
        log.info("Mobile: {}", patron.getMobile());
    }

    @Before("execution(* com.example.librarymanagementsystem.service..*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        log.info("Entering {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @Around("execution(* com.example.librarymanagementsystem.service..*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;

            log.error("Exception in {}.{}() with cause = \"{}\" and duration = {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getMessage() != null ? ex.getMessage() : "NULL",
                    duration);

            throw ex;
        }

        long duration = System.currentTimeMillis() - startTime;

        log.info("Execution of {}.{}() took {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                duration);

        return result;
    }
}