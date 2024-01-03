package security.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import security.spring.entity.member.Member;
import security.spring.service.member.EmailService;
import security.spring.service.member.MemberServiceImpl;

import java.util.Base64;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationController {

    private final EmailService emailService;
    private final MemberServiceImpl memberService;

    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id){
        byte[] actualId = Base64.getDecoder().decode(id.getBytes());
        log.info("actualId={}",actualId);
        String username = emailService.getUsernameForVerificationId(new String(actualId));
        log.info("username={}",username);
        if(username!=null){
            Member user = memberService.findByLoginId(username);
            user.setVerified(true);
            memberService.save(user);
            return "redirect:/login-emailVerified";
        }
        return "redirect:/login-error";
    }

}
