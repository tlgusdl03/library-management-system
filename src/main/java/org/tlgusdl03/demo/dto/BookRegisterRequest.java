package org.tlgusdl03.demo.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.tlgusdl03.demo.entities.BookStatus;
import org.tlgusdl03.demo.entities.Books;

@Builder
@Data
public class BookRegisterRequest {
    private String isbn;

    private String title;

    private String author;

    private BookStatus status;
}
