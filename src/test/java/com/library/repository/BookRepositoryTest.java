package com.library.repository;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookWithAuthorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testFetchBooksWithWriterInfo() {
        Author author = new Author();
        author.setName("Test Author");
        author.setNationality("Testland");
        authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setGenre("Test Genre");
        book.setIsbn("123-456");
        book.setPublishedYear(2024);
        book.setAuthor(author);
        bookRepository.save(book);

        List<BookWithAuthorDTO> result = bookRepository.fetchBooksWithWriterInfo();

        assertFalse(result.isEmpty());
        assertEquals("Test Author", result.get(0).getAuthorName());
    }
}
