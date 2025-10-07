package com.example.libra.service;

import com.example.libra.dto.BookRequest;
import com.example.libra.dto.BookResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.libra.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.libra.repository.BookRepo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;

    @Transactional
    public BookResponse createBook(BookRequest request) {
        // 1) Normalize & validate
        String title  = request.getTitle()  == null ? "" : request.getTitle().trim();
        String author = request.getAuthor() == null ? "" : request.getAuthor().trim();

        if (title.isBlank() || author.isBlank()) {
            throw new IllegalArgumentException("Title and author must not be blank");
        }

        Integer year = request.getPublishedYear(); // prefer Integer so null is possible

        // 2) Uniqueness rule (choose one strategy)
        // If you want to guard by (title + author + year) when year is provided:
        if (year != null && bookRepo
                .existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublishedYear(title, author, year)) {
            throw new IllegalArgumentException("A book with the same title, author, and year already exists");
        }

        // 3) Map request -> entity
        Book entity = new Book();
        entity.setTitle(title);
        entity.setAuthor(author);
        entity.setCategory(request.getCategory());
        entity.setPublishedYear(year);
        entity.setAvailable(true);

        // 4) Save once
        Book saved = bookRepo.save(entity);

        // 5) Map entity -> response
        return toResponse(saved);
    }
    //mapper
    private BookResponse toResponse(Book b) {
        BookResponse res = new BookResponse();
        res.setId(b.getId());
        res.setTitle(b.getTitle());
        res.setAuthor(b.getAuthor());
        res.setCategory(b.getCategory());
        res.setPublishedYear(b.getPublishedYear());
        res.setAvailable(b.isAvailable());
        return res;
    }
    public BookResponse getBookById(Long id) {
        if (id == null) throw new IllegalArgumentException("id must not be null");
        Book book = bookRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        return toResponse(book);
    }

    public List<BookResponse> listBooks() {
        return bookRepo.findAll().stream().map(this::toResponse).toList();
    }

    public Page<BookResponse> findByTitle(String title, Pageable pageable) {
        if (title == null) throw new IllegalArgumentException("title must not be null");
        Page<Book> page = bookRepo.findByTitle(title, pageable); //.orElseThrow(() -> new EntityNotFoundException("Book with title " + title + " not found"));
        return page.map(this::toResponse);
    }

    public Page<BookResponse> findByAuthor(String author, Pageable pageable) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("author must not be null or blank");
        }
        Page<Book> page = bookRepo.findByAuthor(author, pageable);
        return page.map(this::toResponse);
    }

    // Filtereleri yönetecek service
    public Page<BookResponse> findBySpec(String title, String author, Pageable pageable) {
        if (title != null && !title.isBlank() && author != null && !author.isBlank()) {
            Page<Book> page = bookRepo.findByTitleAndAuthor(title, author, pageable); //.orElseThrow(() -> new EntityNotFoundException("Book with title " + title + " not found"));
            return page.map(this::toResponse);
        }

        else if(title != null && !title.isBlank()) {
            Page<Book> page = bookRepo.findByTitle(title, pageable); //.orElseThrow(() -> new EntityNotFoundException("Book with title " + title + " not found"));
            return page.map(this::toResponse);
        }
        else if(author != null && !author.isBlank()) {
            Page<Book> page = bookRepo.findByAuthor(author, pageable);
            return page.map(this::toResponse);
        }
        else {
            return bookRepo.findAll(pageable).map(this::toResponse);
        }
    }


    // to retrieve all available books
    public List<BookResponse> findByAvailable(Boolean available) {
        return bookRepo.findByAvailable(true).stream().map(this::toResponse).collect(Collectors.toList());

    }
}
