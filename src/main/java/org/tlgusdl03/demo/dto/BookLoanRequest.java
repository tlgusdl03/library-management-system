package org.tlgusdl03.demo.dto;

import lombok.Data;

@Data
public class BookLoanRequest {
    Long memberId;
    Long bookId;
}
