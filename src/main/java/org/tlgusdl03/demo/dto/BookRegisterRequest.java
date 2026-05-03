package org.tlgusdl03.demo.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class BookRegisterRequest {
    String isbn;

    String title;

    String author;
}
