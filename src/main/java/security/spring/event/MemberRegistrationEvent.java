package security.spring.event;

import org.springframework.context.ApplicationEvent;
import security.spring.entity.member.Member;

public class MemberRegistrationEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1l;

    private Member member;


    public MemberRegistrationEvent(Member member) {
        super(member);
        this.member = member;
    }

    public Member getUser(){
        return member;
    }
}
