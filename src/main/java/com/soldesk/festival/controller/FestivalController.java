package com.soldesk.festival.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.PageDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.ReviewDTO;
import com.soldesk.festival.service.FestivalService;
import com.soldesk.festival.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final ReviewService reviewService;
    
    @GetMapping("/festivalInfo")
    public String info(@RequestParam("id") int id, Model model){
        
        FestivalDTO festival = festivalService.getFestival(id);
        model.addAttribute("festival", festival);

        //리뷰 추가 
        int festivalIdx = festival.getFestival_idx();
        List<ReviewDTO> reviewList = reviewService.selectAllReviews(festivalIdx );
        model.addAttribute("reviews", reviewList);


        
        return "festival/festival";
    }
}
