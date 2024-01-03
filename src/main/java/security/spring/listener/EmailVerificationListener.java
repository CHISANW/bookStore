package security.spring.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import security.spring.entity.member.Member;
import security.spring.event.MemberRegistrationEvent;
import security.spring.service.member.EmailService;

import java.util.Base64;


@Service
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<MemberRegistrationEvent> {

    private final JavaMailSender mailSender;
    private final EmailService emailService;



    @Override
    public void onApplicationEvent(MemberRegistrationEvent event) {
        Member member = event.getUser();
        String loginId = member.getLoginId();
        String verificationId = emailService.generateVerification(loginId);
        String email = event.getUser().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Course Tracker Account Verification");
        message.setText(getText(member, verificationId));
        message.setFrom("wlwhsrjaeka@naver.com");
        message.setTo(email);
        mailSender.send(message);
    }

    private String getText(Member member, String verificationId) {
        String encodedId = new String(Base64.getEncoder().encode(verificationId.getBytes()));
        StringBuffer buffer = new StringBuffer();
        buffer.append("Dear ").append(member.getUsername()).append(" ").append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("Your account has been successfully created in the Course Tracker application. ");

        buffer.append("Activate your account by clicking the following link: ");
        buffer.append("http://localhost:8080/verify/email?id=").append(encodedId);
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("Regards,").append(System.lineSeparator()).append("Course Tracker Team");
        return buffer.toString();
    }
}
