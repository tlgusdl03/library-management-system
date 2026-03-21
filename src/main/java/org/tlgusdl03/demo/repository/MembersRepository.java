package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Members;

public interface MembersRepository extends JpaRepository<Members, Long> {
}
