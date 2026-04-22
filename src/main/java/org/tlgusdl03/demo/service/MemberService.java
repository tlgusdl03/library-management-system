package org.tlgusdl03.demo.service;

import org.tlgusdl03.demo.dto.MemberJoinRequest;
import org.tlgusdl03.demo.dto.MemberUpdateRequest;

public interface MemberService {
    Long join(MemberJoinRequest dto);
    void update(Long id, MemberUpdateRequest dto);
}
