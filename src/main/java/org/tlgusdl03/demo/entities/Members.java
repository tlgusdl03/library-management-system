package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
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
    MemberStatus status;

    @Column
    Long overdueDate;

    @Column
    String userName;

    @Column
    String password;
}
