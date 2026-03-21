package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "loans")
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
    LoanStatus loanStatus;

    @Column
    Long overdueDate;

    @Column
    Long expansion;

    @Column
    Long bookId;

    @Column
    Long memberId;
}
