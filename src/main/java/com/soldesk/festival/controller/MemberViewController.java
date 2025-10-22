package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    
    //로그인품
    @GetMapping("/Allusers/login")
    public String loginForm(){
        return "Allusers/login";
    }

    //회원가입:일반회원
    @GetMapping("/member/join")
    public String joinForm(){
        return "member/join";   
    }
    
    //기업회원가입
    @GetMapping("/company/join")
    public String companyJoinForm(){
        return "company/join";
    }
    
    //공용마이페이지
    @GetMapping("/Allusers/mypage")
    public String mypageForm(){
        return "Allusers/mypage";
    }
    
    
    //일반회원 정보수정
    @GetMapping("/member/modify")
    public String mypageModifyForMember(){
        return "member/modify";
    }
    //기업회원 정보수정
    @GetMapping("/company/modify")
    public String mypageModifyForCompany(){
        return "company/modify";
    }
    
    //공용회원탈퇴페이지
    @GetMapping("/Allusers/delete")
    public String accountWithdrawal(){
        return "Allsusers/delete";
    }


       
    //관리자 전용 페이지
    @GetMapping("/Allusers/admin")
    public String onlyAdminPage(){
        return "Allusers/admin";
    }
    

}
