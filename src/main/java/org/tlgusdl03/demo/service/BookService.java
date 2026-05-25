package org.tlgusdl03.demo.service;

import org.tlgusdl03.demo.dto.BookRegisterRequest;
import org.tlgusdl03.demo.entities.Books;

public interface BookService {
    Long registerBook(BookRegisterRequest request);
    Books searchById(Long bookId);
    Books searchByIsbn(String isbn);
    Books searchByTitle(String title);
    Books searchByAuthor(String author);
}
