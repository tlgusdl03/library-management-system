package org.tlgusdl03.demo.service;

import org.tlgusdl03.demo.dto.MemberJoinRequest;

public interface MemberService {
    Long join(MemberJoinRequest dto);
}
