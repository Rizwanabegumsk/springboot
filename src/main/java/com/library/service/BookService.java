package com.library.service;

import com.library.entity.Book;
import com.library.entity.BookWithAuthorDTO;
import com.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookWithAuthorDTO> getAllBooksWithAuthorDetails() {
        return bookRepository.fetchBooksWithWriterInfo();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
