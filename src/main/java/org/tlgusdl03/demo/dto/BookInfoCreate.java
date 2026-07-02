package org.tlgusdl03.demo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
// 책 정보 생성 전용 dto
// 현재는 기능이 많지 않아 상위 dto랑 구조가 같지만 기능이 늘어날 것을 대비하여 별도의 dto를 생성함
public class BookInfoCreate {
    String isbn;

    String title;

    String author;
}
