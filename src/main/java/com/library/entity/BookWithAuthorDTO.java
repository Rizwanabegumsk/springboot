package com.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithAuthorDTO {
    private Long id;
    private String title;
    private String genre;
    private int publishedYear;
    private String isbn;
    private Long authorId;
    private String authorName;
    private String authorNationality; // Not in our Entity yet, but friend's repo has it. I'll stick to Author attributes I have.
}
