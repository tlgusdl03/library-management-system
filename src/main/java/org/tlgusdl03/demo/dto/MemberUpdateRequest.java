package org.tlgusdl03.demo.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String name;
    private byte[] residentNumber;
    private String phone;
    String userName;
    String password;
}
