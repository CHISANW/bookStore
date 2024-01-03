package security.spring.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.spring.entity.member.EmailVerification;
import security.spring.repository.member.EmailRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final EmailRepository emailRepository;

    public String generateVerification(String username){
        if(!emailRepository.existsByUsername(username)){
            EmailVerification emailVerification = new EmailVerification(username);
            emailVerification = emailRepository.save(emailVerification);
            return emailVerification.getVerificationId();
        }
        return getVerificationIdByUsername(username);
    }

    public String getVerificationIdByUsername(String username){
        EmailVerification byUsername = emailRepository.findByUsername(username);
        if(byUsername!=null){
            return byUsername.getVerificationId();
        }
        return null;
    }

    public String getUsernameForVerificationId(String username) {
        Optional<EmailVerification> verification = emailRepository.findById(username);
        if(verification.isPresent()){
            return verification.get().getUsername();
        }
        return null;
    }
}
