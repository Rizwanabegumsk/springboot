package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @Column(unique = true)
    @NotBlank
    private String isbn;

    private int publishedYear;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
