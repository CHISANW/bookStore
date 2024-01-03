package security.spring.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.spring.entity.member.Grade;
import security.spring.entity.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUsername(String username);
    Member findByLoginId(String loginId);
    Grade findByGrade(String username);
    Member findByPhoneNumber(String phoneNumber);

}
