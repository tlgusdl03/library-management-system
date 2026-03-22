package org.tlgusdl03.demo.dto;

import org.tlgusdl03.demo.entities.Members;

public class MemberJoinRequest {
    private String name;
    private byte[] residentNumber;
    private String phone;
    String userName;
    String password;

    public Members toEntity() {
        return Members.builder()
                .name(this.name)
                .residentNumber(this.residentNumber)
                .phone(this.phone)
                .userName(this.userName)
                .password(this.password)
                .build();
    }
}
