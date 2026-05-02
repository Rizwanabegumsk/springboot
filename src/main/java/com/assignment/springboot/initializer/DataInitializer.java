package com.assignment.springboot.initializer;

import com.assignment.springboot.entity.Author;
import com.assignment.springboot.entity.Book;
import com.assignment.springboot.repository.AuthorRepository;
import com.assignment.springboot.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public DataInitializer(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (authorRepository.count() == 0) {
            List<Author> authors = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                authors.add(new Author("Author " + i));
            }
            authorRepository.saveAll(authors);

            List<Book> books = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                books.add(new Book("Book Title " + i, "ISBN-" + i + "00" + i, authors.get(i - 1)));
            }
            bookRepository.saveAll(books);
            
            System.out.println("Initialized database with 10 authors and 10 books.");
        }
    }
}
