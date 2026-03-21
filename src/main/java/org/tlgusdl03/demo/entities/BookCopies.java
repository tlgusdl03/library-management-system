package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "book_copies")
public class BookCopies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    BookStatus status;

    @Column
    Long bookId;
}
