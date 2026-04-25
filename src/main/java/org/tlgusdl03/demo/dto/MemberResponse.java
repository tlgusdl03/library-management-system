package org.tlgusdl03.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.tlgusdl03.demo.entities.MemberStatus;

@Data
@AllArgsConstructor
public class MemberResponse {
    String name;
    String phone;
    MemberStatus status;
    Long overdueDate;
    String userName;
}
