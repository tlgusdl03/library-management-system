package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.BookCopies;

import java.util.List;

public interface BookCopiesRepository extends JpaRepository<BookCopies, Long> {
    List<BookCopies> findAllByBookId(Long bookId);
}
