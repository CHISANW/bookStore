package security.spring.service.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import security.spring.entity.member.Member;
import security.spring.repository.member.MemberRepository;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Member loginId = memberRepository.findByLoginId(username);
        if(loginId ==null){
            throw new UsernameNotFoundException(username + " 을 찾을수가 없습니다.");
        }

        log.info("member={}",loginId.getLoginId());
        return User.builder()
                .username(loginId.getLoginId())
                .password(loginId.getPassword())
                .disabled(!loginId.isVerified())
                .accountExpired(loginId.isAccountCredentialsExpired())
                .accountLocked(loginId.isLocked())
                .authorities("ROLE_USER")
                .build();
    }
}
