package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.BoardPageDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/main")
    public String main(){
        return "/admin/admin";
    }
    @GetMapping("/event")
    public String event(@RequestParam(name = "page" , defaultValue = "1") int page, Model model){

        boardService.selectAllBoard(page);
        if (page < 1) page = 1;
        List<BoardDTO> boardList = boardService.selectAllBoard(page);
        BoardPageDTO pageDTO = boardService.getPageDTO(page);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageDTO", pageDTO);

        return "/admin/admin-event";
    }
    @GetMapping("/festival")
    public String festival(Model model){

        List<FestivalDTO> festivals = festivalService.AllFestivals();

        model.addAttribute("festivals", festivals);
        
        return "/admin/admin-festival";
    }
    @GetMapping("/inquiry")
    public String inquiry(){
        return "/admin/admin-inquiry";
    }
    @GetMapping("/member")
    public String member(Model model){

        List<MemberDTO> members = memberService.getMemberList();

        model.addAttribute("members", members);

        return "/admin/admin-member";
    }
    @GetMapping("/proposal")
    public String proposal(){
        return "/admin/admin-proposal";
    }
    
}
