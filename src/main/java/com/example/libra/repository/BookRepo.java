package com.example.libra.repository;

import com.example.libra.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

//    List<Book> findByAvailable();

    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndPublishedYear(String title, String author, Integer year);

    Page<Book> findByTitle(String title, Pageable pageable);

    Page<Book> findByAuthor(String author, Pageable pageable);

    Page<Book> findByTitleAndAuthor(String title, String author, Pageable pageable);

    Page<Book> findAll(Pageable pageable);

    List<Book> findByAvailable(boolean available);
}
    
