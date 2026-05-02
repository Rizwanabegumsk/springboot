package com.assignment.springboot.service;

import com.assignment.springboot.entity.Book;
import com.assignment.springboot.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAllBooksWithAuthors() {
        return bookRepository.findAllBooksWithAuthors();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
    }

    public void save(Book book) {
        bookRepository.save(book);
    }
}
