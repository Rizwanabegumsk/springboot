package com.assignment.springboot.repository;

import com.assignment.springboot.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Custom query method that performs an inner join between both entities
    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    List<Book> findAllBooksWithAuthors();
}
