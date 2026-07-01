package org.tlgusdl03.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tlgusdl03.demo.dto.BookLoanRequest;
import org.tlgusdl03.demo.entities.BookCopies;
import org.tlgusdl03.demo.entities.BookStatus;
import org.tlgusdl03.demo.entities.LoanStatus;
import org.tlgusdl03.demo.entities.Loans;
import org.tlgusdl03.demo.repository.BookCopiesRepository;
import org.tlgusdl03.demo.repository.BooksRepository;
import org.tlgusdl03.demo.repository.LoansRepository;
import org.tlgusdl03.demo.repository.MembersRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService{
    private final BookCopiesRepository bookCopiesRepository;
    private final LoansRepository loansRepository;

    // BooksId를 받고
    // Copies 중 하나를 선택해서 빌려줌
    // 그러려면 Loans에 copiesId도 저장해야 함
    @Override
    public Long loanBook(BookLoanRequest bookLoanRequest) {
        Long bookId = bookLoanRequest.getBookId();
        Long memberId = bookLoanRequest.getMemberId();

        BookCopies availableCopy = bookCopiesRepository.findFirstByBookIdAndStatus(bookId, BookStatus.available).orElseThrow(() -> new RuntimeException("Book Not Available"));
        availableCopy.changeStatus(BookStatus.loaned);

        Loans loan = Loans.builder()
                .bookId(bookId)
                .bookCopyId(availableCopy.getId())
                .memberId(memberId)
                .loanDate(Instant.now())
                .returnDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .loanStatus(LoanStatus.loaned)
                .expansion(0L)
                .overdueDate(0L)
                .build();

        loansRepository.save(loan);
        return loan.getId();
    }

    @Override
    public void returnBook(Long loanId) {
        Loans loan = loansRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.changeLoanStatus(LoanStatus.returned);

        BookCopies copy = bookCopiesRepository.findById(loan.getBookCopyId()).orElseThrow(() -> new IllegalArgumentException("Book Copy not found"));
        copy.changeStatus(BookStatus.available);
    }

    @Override
    public void extendLoan(Long loanId) {
        Loans loan = loansRepository.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.extendLoan();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void applyPenalty() {
        Instant now = Instant.now();

        List<Loans> overdueLoans = loansRepository.findAllByLoanStatusAndReturnDateBefore(LoanStatus.loaned, now);

        if (overdueLoans.isEmpty()) {
            return;
        }

        for (Loans loan : overdueLoans) {
            loan.changeLoanStatus(LoanStatus.overdue);
        }
    }
}
