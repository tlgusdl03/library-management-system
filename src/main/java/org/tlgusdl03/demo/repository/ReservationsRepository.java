package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Reservations;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {
}
