package org.tlgusdl03.demo.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.tlgusdl03.demo.entities.Books;

@Data
public class BookRegisterRequest {
    String isbn;

    String title;

    String author;

    public Books toEntity() {
        return Books.builder()
                .author(this.author)
                .isbn(this.isbn)
                .title(this.title)
                .build();
    }
}
