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
    @GetMapping("/member/mypage")
    public String mypageForm(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }

       MemberDetailDTO userInfo = memberService.getMemberDetails(userdetails.getUsername());

       model.addAttribute("userInfo", userInfo);
       model.addAttribute("displayName", userdetails.getUserDisplayName());

       return "member/mypage";
    }

    

    @GetMapping("/company/mypage")
    public String companyPageForm(){
        return "company/mypage";
    }
    
    
    //일반회원 정보수정
    @GetMapping("/mypage/edit")
    public String mypageModifyForMember(){
        return "member/edit";
    }
    
    //기업회원 정보수정
    @GetMapping("/company/edit")
    public String mypageModifyForCompany(){
        return "company/edit";
    }
    
    //일반회원탈퇴페이지
    @GetMapping("/member/delete")
    public String accountWithdrawal(){
        return "member/delete";
    }


       
    

}
