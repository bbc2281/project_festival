package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    
    @GetMapping("/festivalInfo")
    public String info(@RequestParam("id") int id, Model model){
        
        FestivalDTO festival = festivalService.getFestival(id);
        model.addAttribute("festival", festival);
        return "festival/festival";
    }

    @GetMapping("/festivalReg")
    public String register(Model model){
        List<FestivalCategoryDTO> category = festivalService.getCategory();

        model.addAttribute("category", category);

        return "festival/project_plan";
    }
}
