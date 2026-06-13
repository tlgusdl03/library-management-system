package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Table(name = "loans")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    Instant loanDate;

    @Column
    Instant returnDate;

    @Column
    @Enumerated(EnumType.STRING)
    LoanStatus loanStatus;

    @Column
    Long overdueDate;

    @Column
    Long expansion;

    @Column
    Long bookId;

    @Column
    Long bookCopyId;

    @Column
    Long memberId;

    public void changeLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public void extendLoan() {
        this.returnDate.plus(7, ChronoUnit.DAYS);
        this.expansion++;
    }
}
