package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Notifications;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
}
