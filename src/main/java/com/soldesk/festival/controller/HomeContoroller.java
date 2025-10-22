package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soldesk.festival.dto.BoardDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.service.BoardService;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeContoroller {

    private final FestivalService festivalService;
    private final BoardService boardService;
    
    @GetMapping("/")
    public String home(){
        
        return "index";
    }

    @GetMapping("/api/festivals")
    @ResponseBody
    public List<FestivalDTO> getFestivals() {
        return festivalService.AllFestivals();  // JSON 형태로 반환
    }
    
    // @GetMapping("/index/board")
    // @ResponseBody
    // public List<BoardDTO> getBoards() {
    //     return boardService.AllBoards();
    // }
    
}
