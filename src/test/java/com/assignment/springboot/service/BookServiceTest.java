package com.assignment.springboot.service;

import com.assignment.springboot.entity.Author;
import com.assignment.springboot.entity.Book;
import com.assignment.springboot.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Author author;

    @BeforeEach
    public void setup() {
        author = new Author("Author 1");
        author.setId(1L);
        book = new Book("Book 1", "ISBN-1", author);
        book.setId(1L);
    }

    @Test
    public void testFindAllBooksWithAuthors() {
        when(bookRepository.findAllBooksWithAuthors()).thenReturn(Arrays.asList(book));

        List<Book> books = bookService.findAllBooksWithAuthors();

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAllBooksWithAuthors();
    }

    @Test
    public void testFindById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.findById(1L);

        assertNotNull(foundBook);
        assertEquals(1L, foundBook.getId());
        assertEquals("Book 1", foundBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        bookService.save(book);
        verify(bookRepository, times(1)).save(book);
    }
}
