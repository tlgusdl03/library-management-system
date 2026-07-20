package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "book_copies")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCopies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    @Enumerated(EnumType.STRING)
    BookStatus status;

    // 참조 변경
    @Column
    String isbn;

    public void changeStatus(BookStatus status) {
        this.status = status;
    }
}
