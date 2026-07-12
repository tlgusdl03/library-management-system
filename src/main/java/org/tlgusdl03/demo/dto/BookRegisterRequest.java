package org.tlgusdl03.demo.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.tlgusdl03.demo.entities.BookStatus;
import org.tlgusdl03.demo.entities.Books;

@Builder
@Data
public class BookRegisterRequest {
    String isbn;

    String title;

    String author;

    BookStatus status;
}
