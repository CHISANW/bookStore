package security.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import security.spring.dto.MemberDto;
import security.spring.entity.member.Member;
import security.spring.event.MemberRegistrationEvent;
import security.spring.service.member.MemberServiceImpl;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("member", new MemberDto());
        return "join";
    }

    @PostMapping("/join")
    public String joinPost(@Valid @ModelAttribute("member") MemberDto memberDto, BindingResult result){
        if (result.hasErrors()){
            return "join";
        }

        Member member = memberService.createMember(memberDto);
        eventPublisher.publishEvent(new MemberRegistrationEvent(member));

        return "redirect:/?validate";
    }


    @PostMapping("/checkDuplicateId")
    @ResponseBody
    public int checkDuplicateId(@RequestParam("loginId") String loginId){
        int id = memberService.validateMemberId(loginId);
        return id;
    }

    @PostMapping("/checkDuplicatePwd")
    @ResponseBody
    public int checkDuplicatePwd(@RequestParam("password1") String password1,@RequestParam("password2")String password2){
        int passwordMatch = memberService.passwordMatch(password1, password2);
        return passwordMatch;
    }

}
