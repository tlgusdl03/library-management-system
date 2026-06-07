package org.tlgusdl03.demo.service.book;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tlgusdl03.demo.dto.BookRegisterRequest;
import org.tlgusdl03.demo.dto.BookResponse;
import org.tlgusdl03.demo.repository.BookCopiesRepository;
import org.tlgusdl03.demo.repository.BooksRepository;
import org.tlgusdl03.demo.service.BookService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookServiceTest {
    @Autowired
    BookService bookService;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    BookCopiesRepository bookCopiesRepository;

    @Test
    @DisplayName("책 등록 기능을 테스트 합니다.")
    void registerBookTest() {
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest();

        bookRegisterRequest.setIsbn("test01");
        bookRegisterRequest.setTitle("제목1");
        bookRegisterRequest.setAuthor("작가1");

        Long savedId = bookService.registerBook(bookRegisterRequest);
        BookResponse bookResponse = bookService.searchById(savedId);

        assertThat(bookResponse).isNotNull();
        assertThat(bookResponse.getIsbn()).isEqualTo("test01");
        assertThat(bookResponse.getTitle()).isEqualTo("제목1");
        assertThat(bookResponse.getAuthor()).isEqualTo("작가1");
    }
}
