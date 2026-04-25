package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tlgusdl03.demo.dto.MemberUpdateRequest;

@Entity
@Table(name = "members")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    String name;

    @Column
    byte[] residentNumber;

    @Column
    String phone;

    @Column
    @Enumerated(EnumType.STRING)
    @Builder.Default
    MemberStatus status = MemberStatus.normal;

    @Column
    @Builder.Default
    Long overdueDate = 0L;

    @Column
    String userName;

    @Column
    String password;

    public void updateProfile(String name, byte[] residentNumber, String phone, String userName, String password) {
        this.name = name;
        this.residentNumber = residentNumber;
        this.phone = phone;
        this.userName = userName;
        this.password = password;
    }

    public void withdraw(){
        this.status = MemberStatus.withdrawn;
    }
}
