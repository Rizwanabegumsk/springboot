package com.assignment.springboot.controller;

import com.assignment.springboot.entity.Book;
import com.assignment.springboot.service.AuthorService;
import com.assignment.springboot.service.BookService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAllBooksWithAuthors());
        return "books";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        return "book-form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book, Model model) {
        try {
            bookService.save(book);
            return "redirect:/books";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Integrity violation: Could not save the book.");
            model.addAttribute("authors", authorService.findAll());
            return "book-form";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving.");
            model.addAttribute("authors", authorService.findAll());
            return "book-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        return "book-form";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book book, Model model) {
        try {
            book.setId(id);
            bookService.save(book);
            return "redirect:/books";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Integrity violation: Could not update the book.");
            model.addAttribute("authors", authorService.findAll());
            return "book-form";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving.");
            model.addAttribute("authors", authorService.findAll());
            return "book-form";
        }
    }
}
