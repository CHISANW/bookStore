package security.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import security.spring.entity.member.Grade;
import security.spring.entity.item.Item;
import security.spring.entity.member.Member;
import security.spring.service.item.ItemService;
import security.spring.service.member.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/")
    public String home(Model model , @PageableDefault(size = 8)Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = "";
        String email="";
        String grade="";

        //구글 로그인
        if(authentication.getPrincipal() instanceof OAuth2User){
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            email = oAuth2User.getAttribute("email");
            userName = oAuth2User.getAttribute("name");
        }

        //일반 사용자 로그인
        if(authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            Member byLoginId = memberService.findByLoginId(user.getUsername());
            model.addAttribute("userName",byLoginId);
        }

        List<Item> items = itemService.findItems();


        Page<Item> itemPage = itemService.findAllPage(pageable);


        model.addAttribute("email",email);
        model.addAttribute("grade",grade);
        model.addAttribute("items",items);
        return "index";
    }
}
