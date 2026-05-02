package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BookWithAuthorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.library.entity.BookWithAuthorDTO("
            + "b.id, b.title, b.genre, b.publishedYear, b.isbn,"
            + "a.id, a.name, a.nationality)"
            + " FROM Book b INNER JOIN b.author a")
    List<BookWithAuthorDTO> fetchBooksWithWriterInfo();
}
