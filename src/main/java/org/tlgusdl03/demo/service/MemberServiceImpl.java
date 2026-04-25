package org.tlgusdl03.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tlgusdl03.demo.dto.MemberJoinRequest;
import org.tlgusdl03.demo.dto.MemberResponse;
import org.tlgusdl03.demo.dto.MemberUpdateRequest;
import org.tlgusdl03.demo.entities.Members;
import org.tlgusdl03.demo.repository.MembersRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MembersRepository membersRepository;

    @Override
    @Transactional(readOnly=true)
    public Long join(MemberJoinRequest dto) {
        Members members = dto.toEntity();
        membersRepository.save(members);
        return members.getId();
    }

    @Override
    @Transactional
    public void update(Long id, MemberUpdateRequest dto) {
        Members member = membersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.updateProfile(dto.getName(), dto.getResidentNumber(), dto.getPhone(), dto.getUserName(),  dto.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMember(Long id) {
        Members member = membersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return new MemberResponse(
                member.getName(),
                member.getPhone(),
                member.getStatus(),
                member.getOverdueDate(),
                member.getUserName()
        );

    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        Members member = membersRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.withdraw();
    }
}
