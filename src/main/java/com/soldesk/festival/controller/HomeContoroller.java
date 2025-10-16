package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeContoroller {

    private final FestivalService festivalService;
    
    @GetMapping("/")
    public String home(Model model){

        List<FestivalDTO> festivals = festivalService.AllFestivals();
        model.addAttribute("festivals", festivals);
        
        return "index";
    }

    @GetMapping("/api/festivals")
    @ResponseBody
    public List<FestivalDTO> getFestivals() {
        return festivalService.AllFestivals();  // JSON 형태로 반환
    }
}
