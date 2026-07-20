package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.BookCopies;
import org.tlgusdl03.demo.entities.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookCopiesRepository extends JpaRepository<BookCopies, Long> {
    List<BookCopies> findAllByIsbn(String isbn);

    Optional<BookCopies> findFirstByIsbnAndStatus(String isbn, BookStatus status);
}
