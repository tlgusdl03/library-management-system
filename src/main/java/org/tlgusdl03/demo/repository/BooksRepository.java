package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Books;

public interface BooksRepository extends JpaRepository<Books, Long> {
}
