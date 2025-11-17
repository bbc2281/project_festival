package com.soldesk.festival.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CompanyDetailDTO;
import com.soldesk.festival.dto.MemberDTO;
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
        String userId = userdetails.getUsername();
        System.out.println("인증된 사용자 : " + userId);

        Optional<MemberDetailDTO> opDetail = memberService.getMemberDetails(userId);

        if(opDetail.isEmpty()){

            return "redirect:/auth/loginPage?error=dataError";
        }

        MemberDetailDTO userInfo = opDetail.get();
      

       model.addAttribute("userInfo", userInfo);
       model.addAttribute("displayName", userdetails.getUserDisplayName());
        // model.addAttribute("userIDX", userdetails.getUserIdx()); 이렇게해서도 idx가져올수있음

       return "member/mypage";
    }

    
    //기업회원 마이페이지
    @GetMapping("/company/mypage")
    public String companyPageForm(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){
        
        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }
        String userId = userdetails.getUsername();
        System.out.println("인증된 기업회원" + userId);
        Optional<CompanyDetailDTO> opDetail = companyService.getDetails(userId);

        if(opDetail.isEmpty()){

            return "redirect:/auth/loginPage?error=dataError";
        }

        CompanyDetailDTO userInfo = opDetail.get();

        model.addAttribute("userInfo", userInfo);

        return "company/mypage";
    }
    


    //일반회원 정보보기
    
    @GetMapping("/member/info")
    public String myinfo(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }
        String userId = userdetails.getUsername();
        System.out.println("인증된 사용자 : " + userId);

        Optional<MemberDTO> opDetail = memberService.getAllDetails(userId);

        if(opDetail.isEmpty()){

            return "redirect:/auth/loginPage?error=dataError";
        }

        MemberDTO userInfo = opDetail.get();
      
       model.addAttribute("userInfo", userInfo);
        // model.addAttribute("userIDX", userdetails.getUserIdx()); 이렇게해서도 idx가져올수있음
        return "member/info";
    }
    
    //일반회원 정보수정
    @GetMapping("/member/edit")
    public String mypageModifyForMember(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }
        String userId = userdetails.getUsername();
        System.out.println("인증된 사용자가 회원정보 수정 페이지 진입");

        Optional<MemberDTO> opDetail = memberService.getAllDetails(userId);

        if(opDetail.isEmpty()){
            return "redirect:/auth/loginPage?error=dataError";
        }
        MemberDTO userInfo = opDetail.get();

        model.addAttribute("userInfo", userInfo);
        
        return "member/edit";
    }

    @GetMapping("/company/info")
    public String companyinfoPage(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }
        String userId = userdetails.getUsername();
        System.out.println("인증된 사용자 아이디" + userId);

        Optional<CompanyDetailDTO> opDetail = companyService.getDetails(userId);

        if(opDetail.isEmpty()){
            return "redirect:/auth/loginPage?error=dataError";
        }
        CompanyDetailDTO userInfo = opDetail.get();
        model.addAttribute("userInfo", userInfo);
        return "company/info";


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
