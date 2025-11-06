package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.MemberDetailDTO;
import com.soldesk.festival.dto.PageDTO;
import com.soldesk.festival.mapper.MemberMapper;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.MemberService;
import com.soldesk.festival.service.SegFestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    //관리자 전용 페이지
    @GetMapping("/admin/main")
    public String onlyAdminPage(){
        return "admin/main";
        

    }

    private final FestivalService festivalService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final SegFestivalService segFestivalService;
    private final MemberMapper memberMapper;

    @GetMapping("/main")
    public String main(Model model){

        int countFestival = festivalService.countFestival();
        model.addAttribute("countFestival", countFestival);

        int countBoard = boardService.countBoard();
        model.addAttribute("countBoard", countBoard);

        return "/admin/main";
    }
    @GetMapping("/event")
    public String event(@RequestParam(name = "page" , defaultValue = "1") int page, Model model){

        boardService.selectAllBoard(page);
        if (page < 1) page = 1;
        List<BoardDTO> boardList = boardService.selectAllBoard(page);
        PageDTO pageDTO = boardService.getPageDTO(page);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageDTO", pageDTO);

        return "/admin/event";
    }
    @GetMapping("/festival")
    public String festival(Model model){

        List<FestivalDTO> festivals = festivalService.AllFestivals();

        model.addAttribute("festivals", festivals);
        
        return "/admin/festival";
    }
    @GetMapping("/inquiry")
    public String inquiry(){
        return "/admin/inquiry";
    }
    @GetMapping("/member")
    public String member(Model model){

        List<MemberDetailDTO> list = memberService.getMemberListforAdmin();
        model.addAttribute("members", list);

        return "/admin/member";
    }
    @GetMapping("/proposal")
    public String proposal(Model model){

        List<FestivalDTO> proposals = segFestivalService.selectAllFestivals();

        model.addAttribute("proposals", proposals);

        return "/admin/proposal";
    }
    
}
