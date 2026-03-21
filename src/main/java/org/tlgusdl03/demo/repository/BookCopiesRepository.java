package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.BookCopies;

public interface BookCopiesRepository extends JpaRepository<BookCopies, Long> {
}
