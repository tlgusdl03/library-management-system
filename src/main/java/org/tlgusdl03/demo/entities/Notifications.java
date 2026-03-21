package org.tlgusdl03.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    NotificationsType type;

    @Column
    Long targetId;

    @Column
    String content;

    @Column
    Long memberId;
}
