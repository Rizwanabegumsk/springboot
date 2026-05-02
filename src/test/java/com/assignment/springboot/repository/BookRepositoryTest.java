package com.assignment.springboot.repository;

import com.assignment.springboot.entity.Author;
import com.assignment.springboot.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testFindAllBooksWithAuthors() {
        Author author = new Author("Test Author");
        authorRepository.save(author);

        Book book1 = new Book("Test Book 1", "ISBN-TEST-1", author);
        Book book2 = new Book("Test Book 2", "ISBN-TEST-2", author);
        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAllBooksWithAuthors();

        assertNotNull(books);
        // It might be 12 if data initializer runs, but DataJpaTest is transactional and rolls back, 
        // however, we should check if our inserted books have the author fetched.
        // Actually DataJpaTest doesn't run CommandLineRunner by default.
        assertEquals(2, books.size());
        assertEquals("Test Author", books.get(0).getAuthor().getName());
    }
}
