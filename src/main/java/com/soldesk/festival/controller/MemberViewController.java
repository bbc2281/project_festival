package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    
    @GetMapping("/member/login")
    public String loginForm(){
        return "member/login";
    }
    
    @GetMapping("/member/join")
    public String joinForm(){
        return "member/join";
    }
    
    @GetMapping("/member/mypage")
    public String mypageForm(){
        return "member/mypage";
    }

}
