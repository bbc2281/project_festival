package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    
    //로그인품
    @GetMapping("/auth/login")
    public String loginForm(){
        return "auth/login";
    }

    //회원가입 유형 선택
    @GetMapping("/auth/join")
    public String joinSelect(){
        return "auth/join";
    }

    //회원가입:일반회원
    @GetMapping("/auth/memberjoin")
    public String joinForm(){
        return "auth/memberjoin";   
    }
    
    //기업회원가입
    @GetMapping("/auth/companyjoin")
    public String companyJoinForm(){
        return "auth/companyjoin";
    }
    
    //공용마이페이지(일반/회사-미정)
    @GetMapping("/auth/mypage")
    public String mypageForm(){
        return "auth/mypage";
    }
    
    
    //일반회원 정보수정
    @GetMapping("/auth/modify")
    public String mypageModifyForMember(){
        return "auth/modify";
    }
    
    //기업회원 정보수정
    @GetMapping("/company/modify")
    public String mypageModifyForCompany(){
        return "auth/company/modify";
    }
    
    //공용회원탈퇴페이지
    @GetMapping("/auth/delete")
    public String accountWithdrawal(){
        return "auth/delete";
    }


       
    //관리자 전용 페이지
    @GetMapping("/auth/admin")
    public String onlyAdminPage(){
        return "auth/admin";
    }
    

}
