package com.library.service;

import com.library.entity.Book;
import com.library.entity.BookWithAuthorDTO;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testGetAllBooksWithAuthorDetails() {
        BookWithAuthorDTO dto = new BookWithAuthorDTO();
        dto.setAuthorName("Mock Author");
        when(bookRepository.fetchBooksWithWriterInfo()).thenReturn(Collections.singletonList(dto));

        List<BookWithAuthorDTO> result = bookService.getAllBooksWithAuthorDetails();

        assertEquals(1, result.size());
        assertEquals("Mock Author", result.get(0).getAuthorName());
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertEquals(1L, result.getId());
    }
}
