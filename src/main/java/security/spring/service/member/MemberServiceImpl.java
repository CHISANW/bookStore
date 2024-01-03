package security.spring.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.spring.dto.MemberDto;
import security.spring.entity.member.Address;
import security.spring.entity.member.Grade;
import security.spring.entity.member.Member;
import security.spring.repository.item.AddressRepository;
import security.spring.repository.member.MemberRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;


    public Member createMember(MemberDto memberDto){

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .year(memberDto.getYear())
                .month(memberDto.getMonth())
                .day(memberDto.getDay())
                .email(memberDto.getEmail())
                .phoneNumber(memberDto.getPhoneNumber())
                .grade(Grade.BRONZE)
                .verified(false)
                .locked(false)
                .count(1)
                .accountCredentialsExpired(false).build();
        Address address = Address.builder()
                .zipcode(memberDto.getZipcode())
                .address(memberDto.getAddress())
                .detailAddr(memberDto.getDetailAddr())
                .subAddr(memberDto.getSubAddr())
                .member(member)
                .build();

        addressRepository.save(address);
        return memberRepository.save(member);

    }

    public Member findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    public Member findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public void save(Member member){
        memberRepository.save(member);
    }

    @Override
    public String findByGrade(String username) {
       return  null;
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member findByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }


    public int validateMemberId(String id) {
        Member findId = memberRepository.findByLoginId(id);
        if(findId !=null){
            return 1;  //아이디 중복시 1;
        }
        return 0;      //아이디 중복x 0;

    }

    public int passwordMatch(String password1, String password2){
        if(password1.equals(password2)){
            return 1;  //일치하면 1
        }
        return 0; //일치하지않다면 0
    }

}
