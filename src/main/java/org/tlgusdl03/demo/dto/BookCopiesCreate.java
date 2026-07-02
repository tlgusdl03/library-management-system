package org.tlgusdl03.demo.dto;

import lombok.Builder;
import lombok.Data;
import org.tlgusdl03.demo.entities.BookStatus;

@Builder
@Data
// 책 소장 정보 생성용 dto
public class BookCopiesCreate {
    private String isbn;

    private BookStatus status;
}
