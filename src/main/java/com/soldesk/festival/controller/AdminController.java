package com.soldesk.festival.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CountDTO;
import com.soldesk.festival.mapper.BoardMapper;
import com.soldesk.festival.mapper.ReviewMapper;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.InquiryDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.CompanyService;
import com.soldesk.festival.service.FavoriteService;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.InquiryService;
import com.soldesk.festival.service.MemberService;
import com.soldesk.festival.service.ReviewService;
import com.soldesk.festival.service.SegFestivalService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private final SegFestivalService segFestivalService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final CompanyService companyService;

    @GetMapping("/main")
    public String main(Model model){

        int countFestival = festivalService.countFestival();
        model.addAttribute("countFestival", countFestival);

        int countBoard = boardService.countBoard();
        model.addAttribute("countBoard", countBoard);

        List<BoardDTO> boardList = boardService.selectAllBoard(1);
        model.addAttribute("boardList", boardList.stream().limit(5).toList());

        List<MemberDTO> memberList = memberService.getMemberList();
        int countMember = memberService.countMember();
        model.addAttribute("memberList", memberList.stream().limit(5).toList());
        model.addAttribute("countMember", countMember);

        int countInquiry = inquiryService.countInquiryByNUll();
        model.addAttribute("countInquiry", countInquiry);
        return "/admin/main";
    }
    @GetMapping("/event")
    public String event(Model model){

        List<BoardDTO> boardList = boardMapper.selectAllBoardNoLimits();

        model.addAttribute("boardList", boardList);
        

        return "/admin/event";
    }
    @GetMapping("/festival")
    public String festival(Model model){

        List<FestivalDTO> festivals = festivalService.AllFestivals();

        model.addAttribute("festivals", festivals);
        
        return "/admin/festival";
    }
    @GetMapping("/inquiry")
    public String inquiry(Model model){

        List<InquiryDTO> list = inquiryService.selectAllInquiry();

        model.addAttribute("list", list);

        return "/admin/inquiry";
    }
    @PostMapping("/answerInquiry")
    public String answerInquiry(@RequestBody InquiryDTO inquiry){
        
        inquiryService.updateInquiry(inquiry);

        return "redirect:/admin/inquiry";
    }

    @GetMapping("/member")
    public String member(Model model){

        List<MemberDTO> members = memberService.getMemberList();
        
        model.addAttribute("members", members);
        

        return "/admin/member";
    }

    @GetMapping("/company")
    public String company(Model model){

        List<CompanyDTO> companyMember = companyService.getAllCompanys();
        
        model.addAttribute("companyMember", companyMember);

        return "/admin/company";
    }
    @GetMapping("/proposal")
    public String proposal(Model model, HttpSession session){

        List<FestivalDTO> proposals = segFestivalService.selectAllFestivals();
        
        model.addAttribute("proposals", proposals);

        return "/admin/proposal";
    }
    
    @GetMapping("/analytics")
    @ResponseBody
    public CountDTO analysics(@RequestParam("date") String date){
        
        CountDTO countDTO = new CountDTO();
        int boardCount = boardService.countBoardNow(date);
        int reviewCount= reviewService.countReviewNow(date);

        countDTO.setBoardCount(boardCount);
        countDTO.setReviewCount(reviewCount);
        
        return countDTO;
    }

    @GetMapping("/delete")
    public String deleteMember(@RequestParam("member_idx") Integer member_idx){
        
        if(member_idx != null){
            memberService.adminDeleteMember(member_idx);
        }
        return "redirect:/admin/member";
    }

    @GetMapping("/company/delete")
    public String deleteCompany(@RequestParam("company_idx") Integer company_idx){
        
        if(company_idx != null){
            companyService.deleteCompanyByAdmin(company_idx);
        }
        return "redirect:/admin/member";
    }

    @GetMapping("/board/delete")
    public String deleteBoard(@RequestParam("id") int board_idx){

        boardService.deleteProecess(board_idx);

        return "redirect:/admin/event";
    }

}
