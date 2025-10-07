package com.example.libra.controller;

import com.example.libra.dto.BookRequest;
import com.example.libra.dto.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.example.libra.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable long id) {
        BookResponse book = bookService.getBookById(id);
        return book;
    }

    @GetMapping()
    public Page<BookResponse> listBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @org.springframework.data.web.PageableDefault(size = 20) Pageable pageable) {
        return bookService.findBySpec(title, author, pageable);
    }

    @PostMapping("/create")
    public BookResponse CreateBook(@RequestBody BookRequest book) {
        BookResponse request = bookService.createBook(book);
        return request;
    }

    @GetMapping("/available/{available}")
    public List<BookResponse> availableBooks(@PathVariable boolean available) {
        return bookService.findByAvailable(available);
    }
}
