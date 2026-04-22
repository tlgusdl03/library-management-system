package org.tlgusdl03.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tlgusdl03.demo.entities.Members;

public interface MembersRepository extends JpaRepository<Members, Long> {
    public Members findByResidentNumber(byte[] residentNumber);
    public Members findByUserName(String userName);
}
