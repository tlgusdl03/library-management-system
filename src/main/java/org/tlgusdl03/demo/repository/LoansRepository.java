package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.LoanStatus;
import org.tlgusdl03.demo.entities.Loans;

import java.time.Instant;
import java.util.List;

public interface LoansRepository extends JpaRepository<Loans, Long> {
    public List<Loans> findAllByLoanStatusAndReturnDateBefore(LoanStatus status, Instant date);
}
