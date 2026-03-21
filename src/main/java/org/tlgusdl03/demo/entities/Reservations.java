package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    Long turn;

    @Column
    Long bookId;

    @Column
    Long memberId;
}
