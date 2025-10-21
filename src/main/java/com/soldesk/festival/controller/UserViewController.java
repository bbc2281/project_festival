package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    
    //로그인품
    @GetMapping("/user/login")
    public String loginForm(){
        return "user/login";
    }

    //회원가입:일반/기업 메뉴
    @GetMapping("/user/join")
    public String joinForm(){
        return "user/join";   
    }
    
    //일반회원가입 
    @GetMapping("/user/memberJoin")
    public String memberJoinForm(){
        return "user/memberJoin";
    }
    
    //기업회원가입
    @GetMapping("/user/companyJoin")
    public String companyJoinForm(){
        return "user/companyJoin";
    }
    
    
    //공용마이페이지
    @GetMapping("/user/mypage")
    public String mypageForm(){
        return "user/mypage";
    }
    
    //관리자 전용 페이지
    @GetMapping("/user/adminPage")
    public String onlyAdminPage(){
        return "user/adminPage";
    }
    

    //회원정보 수장 : 일반 / 기업 Form 나뉨
    
    //일반회원정보수정
    @GetMapping("/user/modifyMember")
    public String mypageModifyForMember(){
        return "user/modifyMember";
    }
    //기업회원정보수정
    @GetMapping("/user/modifyCompany")
    public String mypageModifyForCompany(){
        return "user/modifyCompany";
    }
    
    //공용멤버탈퇴페이지
    @GetMapping("user/userDelete")
    public String userdeleteForm(){
        return "user/userDelete";
    }

}
