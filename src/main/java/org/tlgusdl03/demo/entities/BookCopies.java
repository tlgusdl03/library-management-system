package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "book_copies")
public class BookCopies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    @Enumerated(EnumType.STRING)
    BookStatus status;

    @Column
    Long bookId;

    public void changeStatus(BookStatus status) {
        this.status = status;
    }
}
