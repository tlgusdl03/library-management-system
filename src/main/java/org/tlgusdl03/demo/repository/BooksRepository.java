package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Books;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);
    Optional<Books> findByTitle(String title);
    List<Books> findByAuthor(String author);
}
