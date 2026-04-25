package org.tlgusdl03.demo.service.member;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.tlgusdl03.demo.dto.MemberJoinRequest;
import org.tlgusdl03.demo.dto.MemberResponse;
import org.tlgusdl03.demo.dto.MemberUpdateRequest;
import org.tlgusdl03.demo.entities.MemberStatus;
import org.tlgusdl03.demo.entities.Members;
import org.tlgusdl03.demo.repository.MembersRepository;
import org.tlgusdl03.demo.service.MemberService;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MembersRepository membersRepository;

    @Test
    @DisplayName("회원 가입을 테스트합니다.")
    void memberJoinTest() {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest();

        memberJoinRequest.setName("테스터1");
        memberJoinRequest.setResidentNumber("123456-1234567".getBytes());
        memberJoinRequest.setPhone("010-1234-1234");
        memberJoinRequest.setPassword("123456");
        memberJoinRequest.setUserName("테스터01");
        memberJoinRequest.setPassword("123456");

        Long saveId = memberService.join(memberJoinRequest);

        Members foundMember = membersRepository.findById(saveId).orElseThrow();
        Assertions.assertEquals(memberJoinRequest.getName(), foundMember.getName());
        Assertions.assertEquals(memberJoinRequest.getResidentNumber(), foundMember.getResidentNumber());
        Assertions.assertEquals(memberJoinRequest.getPhone(), foundMember.getPhone());
        Assertions.assertEquals(memberJoinRequest.getPassword(), foundMember.getPassword());
        Assertions.assertEquals(memberJoinRequest.getUserName(), foundMember.getUserName());
        Assertions.assertEquals(foundMember.getStatus(), MemberStatus.normal);
        Assertions.assertEquals(foundMember.getOverdueDate(), 0L);


    }

    @Test
    @DisplayName("회원 업데이트를 테스트 합니다.")
    void memberUpdateTest() {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest();

        memberJoinRequest.setName("테스터1");
        memberJoinRequest.setResidentNumber("123456-1234567".getBytes());
        memberJoinRequest.setPhone("010-1234-1234");
        memberJoinRequest.setPassword("123456");
        memberJoinRequest.setUserName("테스터01");
        memberJoinRequest.setPassword("123456");

        Long saveId = memberService.join(memberJoinRequest);

        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();

        memberUpdateRequest.setName("테스터2");
        memberUpdateRequest.setResidentNumber("654321-7654321".getBytes());
        memberUpdateRequest.setPhone("010-4321-4321");
        memberUpdateRequest.setUserName("테스터02");
        memberUpdateRequest.setPassword("654321");

        memberService.update(saveId, memberUpdateRequest);

        Members foundMember = membersRepository.findById(saveId).orElseThrow();
        Assertions.assertEquals(memberUpdateRequest.getName(), foundMember.getName());
        Assertions.assertEquals(memberUpdateRequest.getResidentNumber(), foundMember.getResidentNumber());
        Assertions.assertEquals(memberUpdateRequest.getPhone(), foundMember.getPhone());
        Assertions.assertEquals(memberUpdateRequest.getUserName(), foundMember.getUserName());
        Assertions.assertEquals(memberUpdateRequest.getPassword(), foundMember.getPassword());
    }

    @Test
    @DisplayName("회원 조회를 테스트 합니다.")
    void memberGetTest() {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest();

        memberJoinRequest.setName("테스터1");
        memberJoinRequest.setResidentNumber("123456-1234567".getBytes());
        memberJoinRequest.setPhone("010-1234-1234");
        memberJoinRequest.setPassword("123456");
        memberJoinRequest.setUserName("테스터01");
        memberJoinRequest.setPassword("123456");

        Long saveId = memberService.join(memberJoinRequest);

        MemberResponse foundMember = memberService.getMember(saveId);

        Assertions.assertEquals(foundMember.getName(), memberJoinRequest.getName());
        Assertions.assertEquals(foundMember.getPhone(), memberJoinRequest.getPhone());
        Assertions.assertEquals(foundMember.getStatus(), MemberStatus.normal);
        Assertions.assertEquals(foundMember.getOverdueDate(), 0L);
        Assertions.assertEquals(foundMember.getUserName(), memberJoinRequest.getUserName());
    }

    @Test
    @DisplayName("회원 삭제를 테스트 합니다.")
    void memberDeleteTest() {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest();

        memberJoinRequest.setName("테스터1");
        memberJoinRequest.setResidentNumber("123456-1234567".getBytes());
        memberJoinRequest.setPhone("010-1234-1234");
        memberJoinRequest.setPassword("123456");
        memberJoinRequest.setUserName("테스터01");
        memberJoinRequest.setPassword("123456");

        Long saveId = memberService.join(memberJoinRequest);
        memberService.deleteMember(saveId);

        Members foundMember = membersRepository.findById(saveId).orElseThrow();
        Assertions.assertEquals(foundMember.getName(), memberJoinRequest.getName());
        Assertions.assertEquals(foundMember.getPhone(), memberJoinRequest.getPhone());
        Assertions.assertEquals(foundMember.getStatus(), MemberStatus.withdrawn);
        Assertions.assertEquals(foundMember.getOverdueDate(), 0L);
        Assertions.assertEquals(foundMember.getUserName(), memberJoinRequest.getUserName());

    }
}
