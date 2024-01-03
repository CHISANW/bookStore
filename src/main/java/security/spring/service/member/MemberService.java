package security.spring.service.member;

import security.spring.dto.MemberDto;
import security.spring.entity.member.Member;

import java.util.List;

public interface MemberService {

    Member createMember(MemberDto memberDto);
    Member findByLoginId(String loginId);

    Member findById(Long memberId);

    Member findByUsername(String username);
    public void save(Member member);

    String findByGrade(String username);

    public List<Member> findAll();

    Member findByPhoneNumber(String phoneNumber);

}
