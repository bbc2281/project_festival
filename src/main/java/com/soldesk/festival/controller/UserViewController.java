package com.soldesk.festival.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.soldesk.festival.dto.MemberDetailDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.service.CompanyService;
import com.soldesk.festival.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    public final MemberService memberService;
    public final CompanyService companyService;
    
    //로그인품
    @GetMapping("/auth/loginPage")
    public String loginForm(){
        return "auth/loginPage";
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
    
    //일반회원 마이페이지(일반)
    @GetMapping("/mypage/mypageuser")
    public String mypageForm(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }

       MemberDetailDTO userInfo = memberService.getMemberDetails(userdetails.getUsername());

       model.addAttribute("userInfo", userInfo);
       model.addAttribute("displayName", userdetails.getUserDisplayName());

       return "mypage/mypageuser";
    }

    

    @GetMapping("/mypage/mypagecompany")
    public String companyPageForm(){
        return "mypage/mypagecompany";
    }
    
    
    //일반회원 정보수정
    @GetMapping("/mypage/mypageedit")
    public String mypageModifyForMember(){
        return "mypage/mypageedit";
    }
    
    //기업회원 정보수정
    @GetMapping("/mypage/mypage")
    public String mypageModifyForCompany(){
        return "auth/company/modify";
    }
    
    //공용회원탈퇴페이지
    @GetMapping("/auth/delete")
    public String accountWithdrawal(){
        return "auth/delete";
    }


       
    

}
