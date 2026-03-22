package org.tlgusdl03.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tlgusdl03.demo.dto.MemberJoinRequest;
import org.tlgusdl03.demo.entities.Members;
import org.tlgusdl03.demo.repository.MembersRepository;

@Service
@RequiredArgsConstructor

public class MemberServiceImpl implements MemberService{
    private final MembersRepository membersRepository;
    @Override
    public Long join(MemberJoinRequest dto) {
        Members members = dto.toEntity();
        membersRepository.save(members);
        return members.getId();
    }
}
