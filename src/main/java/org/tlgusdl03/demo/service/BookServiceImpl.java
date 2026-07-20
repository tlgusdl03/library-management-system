package org.tlgusdl03.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tlgusdl03.demo.dto.BookCopiesCreate;
import org.tlgusdl03.demo.dto.BookInfoCreate;
import org.tlgusdl03.demo.dto.BookRegisterRequest;
import org.tlgusdl03.demo.dto.BookResponse;
import org.tlgusdl03.demo.entities.BookCopies;
import org.tlgusdl03.demo.entities.BookStatus;
import org.tlgusdl03.demo.entities.Books;
import org.tlgusdl03.demo.repository.BookCopiesRepository;
import org.tlgusdl03.demo.repository.BooksRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    private final BooksRepository booksRepository;
    private final BookCopiesRepository bookCopiesRepository;

    @Override
    // 등록하려는 도서 정보가 이미 있는지 확인
    // 있다면 도서보유 테이블에만 추가
    // 없다면 도서 정보 및 도서보유 정보 추가
    public void registerBook(BookRegisterRequest dto) {
        boolean isBookExists = booksRepository.existsByIsbn(dto.getIsbn());

        if(!isBookExists){
            registerBookInfo(dto);
        }

        registerBookCopies(dto);
    }

    private void registerBookInfo(BookRegisterRequest dto){
        Books book = Books.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .build();

        booksRepository.save(book);
    }

    private void registerBookCopies(BookRegisterRequest dto){
        BookCopies bookCopies = BookCopies.builder()
                .isbn(dto.getIsbn())
                .status(dto.getStatus())
                .build();

        bookCopiesRepository.save(bookCopies);
    }

    @Override
    // 리팩토링 필요
    public BookResponse searchById(Long id) {
        Books books = booksRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Books not found"));
        // id -> bookId로 하여 book_copies에 모든 책 복사본들을 검색
        List<BookCopies> bookCopies = bookCopiesRepository.findAllByIsbn(books.getIsbn());

        boolean isAvailable = bookCopies.stream().anyMatch((copy -> copy.getStatus().equals(BookStatus.available)));

        return new BookResponse(
                books.getIsbn(),
                books.getTitle(),
                books.getAuthor(),
                isAvailable
        );
    }

    @Override
    public BookResponse searchByIsbn(String isbn) {
        Books books = booksRepository.findByIsbn(isbn).orElseThrow(() -> new IllegalArgumentException("Books not found"));

        List<BookCopies> bookCopies = bookCopiesRepository.findAllByIsbn(isbn);

        boolean isAvailable = bookCopies.stream().anyMatch(copy -> copy.getStatus().equals(BookStatus.available));

        return new BookResponse(
                books.getIsbn(),
                books.getTitle(),
                books.getAuthor(),
                isAvailable
        );
    }

    @Override
    public BookResponse searchByTitle(String title) {
        Books books = booksRepository.findByTitle(title).orElseThrow(() -> new IllegalArgumentException("Books not found"));

        List<BookCopies> bookCopies = bookCopiesRepository.findAllByIsbn(books.getIsbn());

        boolean isAvailable = bookCopies.stream().anyMatch(copy -> copy.getStatus().equals(BookStatus.available));

        return new BookResponse(
                books.getIsbn(),
                books.getTitle(),
                books.getAuthor(),
                isAvailable
        );
    }

    @Override
    public List<BookResponse> searchByAuthor(String author) {
        List<Books> books = booksRepository.findByAuthor(author);

        if (books.isEmpty()) {
            throw new IllegalArgumentException("Books not found");
        }

        return books.stream().map(book -> {
                    List<BookCopies> bookCopies = bookCopiesRepository.findAllByIsbn(book.getIsbn());

                    boolean isAvailable = bookCopies.stream().anyMatch(copy -> copy.getStatus().equals(BookStatus.available));

                    return new BookResponse(
                            book.getIsbn(),
                            book.getTitle(),
                            book.getAuthor(),
                            isAvailable
                    );
                })
                .toList();
    }
}
