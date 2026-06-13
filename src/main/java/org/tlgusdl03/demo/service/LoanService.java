package org.tlgusdl03.demo.service;

import org.tlgusdl03.demo.dto.BookLoanRequest;

public interface LoanService {
    // 대출
    Long loanBook(BookLoanRequest request);
    // 반납
    void returnBook(Long loanId);
    // 연장
    void extendLoan(Long loanId);
    // 제재
    void applyPenalty();
}
