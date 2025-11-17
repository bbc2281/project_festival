package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.soldesk.festival.dto.*;
import com.soldesk.festival.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserViewController {

    private final SegFestivalService segFestivalService;

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final CompanyService companyService;
    private final InquiryService inquiryService;
    private final FavoriteService favoriteService;
    private final CommentService commentService;
    private final FundingFestivalService fundingFestivalService;


    // 로그인품
    @GetMapping("/auth/loginPage")
    public String loginForm() {
        return "auth/loginPage";
    }

    // 회원가입 유형 선택
    @GetMapping("/auth/join")
    public String joinSelect() {
        return "auth/join";
    }

    // 네이버로그인테스트
    @GetMapping("/oauth2/authorization/naver")
    public String naverLogin() {
        return "auth/naverTest";
    }

    // -----일반회원--------------

    // 회원가입:일반회원
    @GetMapping("/auth/memberjoin")
    public String joinForm() {
        return "auth/memberJoin";
    }

    // 일반회원 마이페이지(일반)
    @GetMapping("/member/mypage")
    public String mypageForm(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model, @SessionAttribute("loginMember")MemberDTO loginMember) {
        if(userdetails == null) {
            return "redirect:/auth/loginPage";
        }
        MemberDetailDTO userInfo = memberService.getMemberDetails(userdetails.getUsername());
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("displayName", userdetails.getUserDisplayName());

        List<FestivalDTO> festivalList = favoriteService.selectAllFavoriteByUser(loginMember);
        int fevoriteCount = festivalList.size();
        model.addAttribute("fevoriteCount", fevoriteCount);

        List<InquiryDTO> inquiryList = inquiryService.selectInquiry(loginMember.getMember_idx());
        int inquiryCount = inquiryList.size();
        model.addAttribute("inquiryCount", inquiryCount);

        List<ReviewDTO> reviewList = reviewService.infoReviewByMember(loginMember);
        int reviewCount = reviewList.size();
        model.addAttribute("reviewCount", reviewCount);

        int commentCount = commentService.countCommentByMember(loginMember.getMember_idx());
        model.addAttribute("commentCount", commentCount);
        return "member/mypage";
    }

    @GetMapping("/mypage/edit")
    public String myinfo(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model) {
        if(userdetails == null) {
            return "redirect:/auth/loginPage";
        }
        MemberDetailDTO userInfo = memberService.getMemberDetails(userdetails.getUsername());
        model.addAttribute("userInfo", userInfo);
        return "member/edit";
    }

    // 일반회원 찜한페이지
    @GetMapping("/mypage/bookmark")
    public String mypageBookmark(Model model, HttpSession session) {

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        List<FestivalDTO> festivalList = favoriteService.selectAllFavoriteByUser(loginMember);

        model.addAttribute("festivalList", festivalList);

        return "member/bookmark";
    }

    // 일반회원 1:1문의
    @GetMapping("/mypage/inquiry")
    public String mypageInquiry(@ModelAttribute("inquiry") InquiryDTO inquiry, Model model,
            @SessionAttribute("loginMember") MemberDTO loginMember) {

        List<InquiryDTO> inquiryList = inquiryService.selectInquiry(loginMember.getMember_idx());
        model.addAttribute("inquiryList", inquiryList);

        return "member/inquiry";
    }

    @PostMapping("/mypage/regInquiry")
    public String mypageInsertInquiry(@ModelAttribute("inquiry") InquiryDTO inquiry,
            @SessionAttribute("loginMember") MemberDTO loginMember) {

        inquiryService.insertInquiry(inquiry, loginMember);

        return "redirect:/mypage/inquiry";
    }

    // 일반회원 리뷰작성정보
    @GetMapping("/mypage/review")
    public String mypageReview(Model model, HttpSession session){

        MemberDTO loginMember = (MemberDTO) session.getAttribute("loginMember");

        List<ReviewDTO> reviewList = reviewService.infoReviewByMember(loginMember);

        model.addAttribute("reviewList", reviewList);

        return "member/review";
    }

    // 일반회원탈퇴페이지
    @GetMapping("/mypage/delete")
    public String accountWithdrawal() {
        return "member/delete";
    }

    // -------기업회원------------

    // 기업회원 가입
    @GetMapping("/auth/companyjoin")
    public String companyJoinForm() {
        return "auth/companyjoin";
    }

    // 기업회원 마이페이지
    @GetMapping("/company/mypage")
    public String companyPageForm(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model){

        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }

        int idx = (int) userdetails.getUserIdx();
        CompanyDTO login = companyService.selectCompanyByIdx(idx);
        model.addAttribute("login", login);

        int segFestival = segFestivalService.countFestivalByCompany(idx);
        int favorite = favoriteService.countFavoriteByCompany(idx);
        int festival_funding = fundingFestivalService.countFundingByCompany(idx);
        int payment = 0;

        model.addAttribute("segFestival", segFestival);
        model.addAttribute("favorite", favorite);
        model.addAttribute("festival_funding", festival_funding);
        model.addAttribute("payment", payment);

        List<FestivalDTO> commitFestivalList = segFestivalService.selectCommitFestival(idx);

        model.addAttribute("commitFestivalList", commitFestivalList);

        return "company/mypage";
    }

    // 기업회원 정보수정
    @GetMapping("/company/edit")
    public String mypageModifyForCompany(@AuthenticationPrincipal SecurityAllUsersDTO userdetails, Model model) {
        if(userdetails == null){
            return "redirect:/auth/loginPage";
        }

        int idx = (int) userdetails.getUserIdx();
        CompanyDTO loginMember = companyService.selectCompanyByIdx(idx);

        model.addAttribute("loginMember", loginMember);
        return "company/edit";
    }

    // 기업회원 찜하기
    @GetMapping("/company/favorite")
    public String mypageFavoriteForCompany(Model model, HttpSession session) {

        CompanyDTO company = (CompanyDTO) session.getAttribute("companyMember");

        List<FestivalDTO> festivalList = favoriteService.selectAllFavoriteByCompany(company);

        model.addAttribute("festivalList", festivalList);
        return "company/favorite";
    }

    // 기업회원 등록축제정보
    @GetMapping("/company/festival")
    public String mypageFestivalForCompany(Model model, HttpSession session){

        CompanyDTO company = (CompanyDTO) session.getAttribute("companyMember");
        int id = company.getCompany_idx();
        List<FestivalDTO> festivalList = segFestivalService.selectFestivalByCompany(id);

        model.addAttribute("festivalList", festivalList);

        return "company/festival";
    }

    // 기업회원 후원정보
    @GetMapping("/company/sponsor")
    public String mypageSponsorForCompany() {
        return "company/sponsor";
    }

    // 기업회원 회원탈퇴
    @GetMapping("/company/delete")
    public String mypageDeleteForCompany() {
        return "company/delete";
    }
    

}
