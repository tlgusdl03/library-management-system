package org.tlgusdl03.demo.service.loan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.tlgusdl03.demo.dto.BookLoanRequest;
import org.tlgusdl03.demo.dto.BookRegisterRequest;
import org.tlgusdl03.demo.dto.BookResponse;
import org.tlgusdl03.demo.dto.MemberJoinRequest;
import org.tlgusdl03.demo.entities.BookCopies;
import org.tlgusdl03.demo.entities.BookStatus;
import org.tlgusdl03.demo.entities.Books;
import org.tlgusdl03.demo.entities.Loans;
import org.tlgusdl03.demo.repository.BookCopiesRepository;
import org.tlgusdl03.demo.repository.BooksRepository;
import org.tlgusdl03.demo.repository.LoansRepository;
import org.tlgusdl03.demo.service.BookService;
import org.tlgusdl03.demo.service.LoanService;
import org.tlgusdl03.demo.service.MemberService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@Transactional
public class LoanServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    BookService bookService;
    @Autowired
    LoanService loanService;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    BookCopiesRepository bookCopiesRepository;
    @Autowired
    LoansRepository loansRepository;

    @DisplayName("도서 대출 기능을 테스트 합니다.")
    @Test
        // 1. 카피가 한 권인 책은 도서 대출시 상태가 대출 상태로 변경되어야 함
        // 2. 카피가 여러권인 책은 책 여러권 모두가 대출 상태로 변환되는지 확인해야 함
        // 3. 대출중인 도서를 대출 시도시 불가능해야 함
    void loanBookTest() {

        // 멤버 1명 추가
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("member01")
                .phone("010-1234-5678")
                .password("123456")
                .residentNumber("123456-1234567".getBytes())
                .userName("member01")
                .build();

        Long memberId = memberService.join(memberJoinRequest);

        // 책 정보 1개와 책 보유 정보 3개 추가
        BookRegisterRequest dto = BookRegisterRequest.builder()
                .isbn("isbn01")
                .title("title01")
                .author("author01")
                .status(BookStatus.available)
                .build();

        bookService.registerBook(dto);
        bookService.registerBook(dto);
        bookService.registerBook(dto);

        Books book = booksRepository.findByIsbn("isbn01").orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Long bookId = book.getId();

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder()
                .bookId(bookId)
                .memberId(memberId)
                .build();
        // 1~3번째 대출 시도 성공해야 함
        loanService.loanBook(bookLoanRequest);
        loanService.loanBook(bookLoanRequest);
        loanService.loanBook(bookLoanRequest);

        List<BookCopies> copies = bookCopiesRepository.findAllByIsbn("isbn01");

        Assertions.assertThat(copies)
                .hasSize(3)
                .extracting(BookCopies::getStatus)
                .containsOnly(BookStatus.loaned);

        Assertions.assertThatThrownBy(() -> loanService.loanBook(bookLoanRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book Not Available");
    }

    @Test
    @DisplayName("도서 반납 기능을 테스트 합니다.")
    void returnBookTest() {
        // 멤버 1명 추가
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("member01")
                .phone("010-1234-5678")
                .password("123456")
                .residentNumber("123456-1234567".getBytes())
                .userName("member01")
                .build();

        Long memberId = memberService.join(memberJoinRequest);

        // 책 정보 1개와 책 보유 정보 1개 추가
        BookRegisterRequest dto = BookRegisterRequest.builder()
                .isbn("isbn01")
                .title("title01")
                .author("author01")
                .status(BookStatus.available)
                .build();

        bookService.registerBook(dto);

        Books book = booksRepository.findByIsbn("isbn01").orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Long bookId = book.getId();

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder()
                .bookId(bookId)
                .memberId(memberId)
                .build();
        // 1번째 대출 시도 성공해야 함
        Long loanId = loanService.loanBook(bookLoanRequest);

        // 2번째 대출 시도 실패해야 함
        Assertions.assertThatThrownBy(() -> loanService.loanBook(bookLoanRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book Not Available");


        loanService.returnBook(loanId);

        List<BookCopies> copies = bookCopiesRepository.findAllByIsbn("isbn01");
        Assertions.assertThat(copies.getFirst().getStatus())
                .isEqualTo(BookStatus.available);

        // 반납 후 세번째 대출 시도 성공해야 함
        Long newLoanId = loanService.loanBook(bookLoanRequest);

        Assertions.assertThat(newLoanId).isNotNull();
        Assertions.assertThat(newLoanId).isNotEqualTo(loanId);

        Assertions.assertThat(copies.getFirst().getStatus())
                .isEqualTo(BookStatus.loaned);

    }

    @Test
    @DisplayName("도서 대출 연장 기능을 테스트 합니다.")
    void expansionLoanDateTest(){
        // 멤버 1명 추가
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .name("member01")
                .phone("010-1234-5678")
                .password("123456")
                .residentNumber("123456-1234567".getBytes())
                .userName("member01")
                .build();

        Long memberId = memberService.join(memberJoinRequest);

        // 책 정보 1개와 책 보유 정보 1개 추가
        BookRegisterRequest dto = BookRegisterRequest.builder()
                .isbn("isbn01")
                .title("title01")
                .author("author01")
                .status(BookStatus.available)
                .build();

        bookService.registerBook(dto);

        Books book = booksRepository.findByIsbn("isbn01").orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Long bookId = book.getId();

        BookLoanRequest bookLoanRequest = BookLoanRequest.builder()
                .bookId(bookId)
                .memberId(memberId)
                .build();

        Long loanId = loanService.loanBook(bookLoanRequest);

        loanService.extendLoan(loanId);

        Loans loans = loansRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        Assertions.assertThat(loans.getExpansion())
                .isEqualTo(1);
        Assertions.assertThat(loans.getReturnDate())
                .isCloseTo(Instant.now().plus(14, ChronoUnit.DAYS), Assertions.within(2, ChronoUnit.SECONDS));
    }
//
//    @DisplayName("")
}
