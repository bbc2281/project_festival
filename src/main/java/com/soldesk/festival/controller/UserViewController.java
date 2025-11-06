package com.soldesk.festival.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.soldesk.festival.dto.InquiryDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberDetailDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.service.CompanyService;
import com.soldesk.festival.service.InquiryService;
import com.soldesk.festival.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final MemberService memberService;
    private final CompanyService companyService;
    private final InquiryService inquiryService;
    
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

    // -----일반회원--------------

    //회원가입:일반회원
    @GetMapping("/auth/memberjoin")
    public String joinForm(){
        return "auth/memberJoin";   
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

    
    //일반회원 정보수정
    @GetMapping("/mypage/edit")
    public String mypageModifyForMember(){
        return "member/edit";
    }
    
    //일반회원 찜한페이지
    @GetMapping("/mypage/bookmark")
    public String mypageBookmark(){
        return "member/bookmark";
    }

    //일반회원 1:1문의
    @GetMapping("/mypage/inquiry")
    public String mypageInquiry(@ModelAttribute("inquiry") InquiryDTO inquiry){
        return "member/inquiry";
    }

    @PostMapping("/mypage/regInquiry")
    public String mypageInsertInquiry(@ModelAttribute("inquiry") InquiryDTO inquiry, @SessionAttribute("loginMember") MemberDTO loginMember){
       
        inquiryService.insertInquiry(inquiry, loginMember);

        return "redirect:/mypage/inquiry";
    }

    //일반회원 리뷰작성정보
    @GetMapping("/mypage/review")
    public String mypageReview(){
        return "member/review";
    }

    //일반회원탈퇴페이지
    @GetMapping("/member/delete")
    public String accountWithdrawal(){
        return "member/delete";
    }

    // -------기업회원------------

    //기업회원 가입
    @GetMapping("/auth/companyjoin")
    public String companyJoinForm(){
        return "auth/companyjoin";
    }

    //기업회원 마이페이지
    @GetMapping("/company/mypage")
    public String companyPageForm(){
        return "company/mypage";
    }

    //기업회원 정보수정
    @GetMapping("/company/edit")
    public String mypageModifyForCompany(){
        return "company/edit";
    }
    //기업회원 찜하기
    @GetMapping("/company/favorite")
    public String mypageFavoriteForCompany(){
        return "company/favorite";
    }
    //기업회원 등록축제정보
    @GetMapping("/company/festival")
    public String mypageFestivalForCompany(){
        return "company/festival";
    }
    //기업회원 후원정보
    @GetMapping("/company/sponsor")
    public String mypageSponsorForCompany(){
        return "company/sponsor";
    }
    //기업회원 회원탈퇴
    @GetMapping("/company/delete")
    public String mypageDeleteForCompany(){
        return "company/delete";
    }
    
    


       
    

}
