package org.tlgusdl03.demo.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.tlgusdl03.demo.entities.BookStatus;

@Data
@AllArgsConstructor
public class BookResponse {
    String isbn;
    String title;
    String author;
    boolean isBorrowable;
}
