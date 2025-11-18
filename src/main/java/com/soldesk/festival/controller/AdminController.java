package com.soldesk.festival.controller;

import java.util.List;

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
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.InquiryDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.CompanyService;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.InquiryService;
import com.soldesk.festival.service.MemberService;
import com.soldesk.festival.service.ReviewService;
import com.soldesk.festival.service.SegFestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final SegFestivalService segFestivalService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final CompanyService companyService;

    @GetMapping("/main")
    public String main(Model model) {

        int countFestival = festivalService.countFestival(null);
        model.addAttribute("countFestival", countFestival);

        int countBoard = boardService.countBoard();
        model.addAttribute("countBoard", countBoard);

        List<BoardDTO> boardList = boardService.selectAllBoard(1);
        model.addAttribute("boardList", boardList.stream().limit(5).toList());

        List<MemberDTO> memberList = memberService.getMemberList();
        int countMember = memberService.countMember(null);
        model.addAttribute("memberList", memberList.stream().limit(5).toList());
        model.addAttribute("countMember", countMember);

        int countInquiry = inquiryService.countInquiryByNUll();
        model.addAttribute("countInquiry", countInquiry);
        return "/admin/main";
    }

    @GetMapping("/event")
    public String event(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        int totalCount = boardService.countBoard();
        List<BoardDTO> boardList = boardService.getBoardListPaged((page - 1) * pageSize, pageSize);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("boardList", boardList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "/admin/event";
    }

    @GetMapping("/festival")
    public String festival(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword, Model model) {
        int pageSize = 10;
        int totalCount = festivalService.countFestival(keyword);
        List<FestivalDTO> festivals = festivalService.getFestivalListPaged(keyword, (page - 1) * pageSize, pageSize);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        int blockSize = 10; // 페이지 블록 크기
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        int prevBlockPage = Math.max(startPage - blockSize, 1);
        int nextBlockPage = Math.min(startPage + blockSize, totalPages);

        model.addAttribute("festivals", festivals);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevBlockPage", prevBlockPage);
        model.addAttribute("nextBlockPage", nextBlockPage);
        model.addAttribute("keyword", keyword);

        return "/admin/festival";
    }

    @GetMapping("/inquiry")
    public String inquiry(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        int totalCount = inquiryService.countInquiry();
        List<InquiryDTO> list = inquiryService.getInquiryListPaged((page - 1) * pageSize, pageSize);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("list", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "/admin/inquiry";
    }

    @PostMapping("/answerInquiry")
    public String answerInquiry(@RequestBody InquiryDTO inquiry) {

        inquiryService.updateInquiry(inquiry);

        return "redirect:/admin/inquiry";
    }

    @GetMapping("/member")
    public String member(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword, Model model) {
        int pageSize = 10;
        int totalCount = memberService.countMember(keyword);
        List<MemberDTO> members = memberService.getMemberListPaged(keyword, (page - 1) * pageSize, pageSize);
        
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("members", members);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        return "/admin/member";
    }

    @GetMapping("/company")
    public String company(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String keyword, Model model) {
        int pageSize = 10;
        int totalCount = companyService.countCompany(keyword);
        List<CompanyDTO> companyMember = companyService.getCompanyListPaged(keyword, (page - 1) * pageSize, pageSize);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("companyMember", companyMember);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);
        return "/admin/company";
    }

    @GetMapping("/proposal")
    public String proposal(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        int totalCount = segFestivalService.countProposal();
        List<FestivalDTO> proposals = segFestivalService.getProposalListPaged((page - 1) * pageSize, pageSize);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("proposals", proposals);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "/admin/proposal";
    }

    @GetMapping("/analytics")
    @ResponseBody
    public CountDTO analysics(@RequestParam("date") String date) {

        CountDTO countDTO = new CountDTO();
        int boardCount = boardService.countBoardNow(date);
        int reviewCount = reviewService.countReviewNow(date);

        countDTO.setBoardCount(boardCount);
        countDTO.setReviewCount(reviewCount);

        return countDTO;
    }

    @GetMapping("/delete")
    public String deleteMember(@RequestParam("member_idx") Integer member_idx) {

        if (member_idx != null) {
            memberService.deleteMember(member_idx);
        }
        return "redirect:/admin/member";
    }

    @GetMapping("/company/delete")
    public String deleteCompany(@RequestParam("company_idx") Integer company_idx) {

        if (company_idx != null) {
            companyService.deleteCompanyByAdmin(company_idx);
        }
        return "redirect:/admin/member";
    }

    @GetMapping("/board/delete")
    public String deleteBoard(@RequestParam("id") int board_idx) {

        boardService.deleteProecess(board_idx);

        return "redirect:/admin/event";
    }

}
