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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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

    @Test
    @DisplayName("Isbn을 통한 책 검색 기능을 테스트 합니다.")
    void searchBookByIsbnTest() {
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest();
        
        bookRegisterRequest.setIsbn("test01");
        bookRegisterRequest.setTitle("제목1");
        bookRegisterRequest.setAuthor("작가1");

        Long savedId =  bookService.registerBook(bookRegisterRequest);
        BookResponse bookResponse1 = bookService.searchById(savedId);
        BookResponse bookResponse2 = bookService.searchByIsbn("test01");

        assertThat(bookResponse1.getIsbn()).isEqualTo("test01");
        assertThat(bookResponse2.getIsbn()).isEqualTo("test01");
        assertThat(bookResponse1.getTitle()).isEqualTo("제목1");
        assertThat(bookResponse2.getTitle()).isEqualTo("제목1");
        assertThat(bookResponse1.getAuthor()).isEqualTo("작가1");
        assertThat(bookResponse2.getAuthor()).isEqualTo("작가1");

    }

    @Test
    @DisplayName("Title을 통한 책 검색 기능을 테스트 합니다.")
    void searchBookByTitleTest() {
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest();

        bookRegisterRequest.setIsbn("test01");
        bookRegisterRequest.setTitle("제목1");
        bookRegisterRequest.setAuthor("작가1");

        Long savedId =  bookService.registerBook(bookRegisterRequest);
        BookResponse bookResponse1 = bookService.searchById(savedId);
        BookResponse bookResponse2 = bookService.searchByTitle("제목1");

        assertThat(bookResponse1.getIsbn()).isEqualTo("test01");
        assertThat(bookResponse2.getIsbn()).isEqualTo("test01");
        assertThat(bookResponse1.getTitle()).isEqualTo("제목1");
        assertThat(bookResponse2.getTitle()).isEqualTo("제목1");
        assertThat(bookResponse1.getAuthor()).isEqualTo("작가1");
        assertThat(bookResponse2.getAuthor()).isEqualTo("작가1");

    }

    @Test
    @DisplayName("Author를 통한 책 검색 기능을 테스트 합니다.")
    void searchBookByAuthorTest() {
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest();
        bookRegisterRequest.setIsbn("test01");
        bookRegisterRequest.setTitle("제목1");
        bookRegisterRequest.setAuthor("작가1");
        bookService.registerBook(bookRegisterRequest);

        BookRegisterRequest bookRegisterRequest2 = new BookRegisterRequest();
        bookRegisterRequest2.setIsbn("test02");
        bookRegisterRequest2.setTitle("제목2");
        bookRegisterRequest2.setAuthor("작가1");
        bookService.registerBook(bookRegisterRequest2);

        List<BookResponse> bookResponseList = bookService.searchByAuthor("작가1");

        assertThat(bookResponseList).hasSize(2);

        assertThat(bookResponseList)
                .extracting(BookResponse::getIsbn, BookResponse::getTitle, BookResponse::getAuthor)
                .containsExactly(
                        tuple("test01","제목1","작가1"),
                        tuple("test02","제목2","작가1")
                );

    }
}
