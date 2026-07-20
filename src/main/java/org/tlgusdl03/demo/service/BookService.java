package org.tlgusdl03.demo.service;

import org.tlgusdl03.demo.dto.BookRegisterRequest;
import org.tlgusdl03.demo.dto.BookResponse;

import java.util.List;

/*
    Books 테이블을 조회 한 후 그와 관련된 데이터를 BooksCopies에서 찾음
    관련된 하나의 dto를 만드는 것이 좋을 것 같음
 */
public interface BookService {
    void registerBook(BookRegisterRequest request);
    BookResponse searchById(Long bookId);
    BookResponse searchByIsbn(String isbn);
    BookResponse searchByTitle(String title);
    List<BookResponse> searchByAuthor(String author);
}
