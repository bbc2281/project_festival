package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/funding")
public class FundingController {
    
    private final FestivalService festivalService;

    @GetMapping("/main")
    public String main(Model model){

        List<FestivalCategoryDTO> category = festivalService.getCategory();

        model.addAttribute("category", category);

        return "funding/main";
    }

    @GetMapping("/info")
    public String info(){
        return "funding/info";
    }
}
