package com.library.controller;

import com.library.entity.Book;
import com.library.service.AuthorService;
import com.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public String showBooks(Model model) {
        model.addAttribute("catalogue", bookService.getAllBooksWithAuthorDetails());
        return "books/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book, @RequestParam("authorId") Long authorId, RedirectAttributes ra) {
        try {
            book.setAuthor(authorService.getAuthorById(authorId));
            bookService.saveBook(book);
            ra.addFlashAttribute("successMessage", "New book saved!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book, @RequestParam("authorId") Long authorId, RedirectAttributes ra) {
        try {
            book.setId(id);
            book.setAuthor(authorService.getAuthorById(authorId));
            bookService.saveBook(book);
            ra.addFlashAttribute("successMessage", "Book updated!");
        } catch (Exception ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/books";
    }
}
