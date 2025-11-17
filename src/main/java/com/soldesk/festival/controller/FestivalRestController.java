package com.soldesk.festival.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FestivalRestController {

    private final FestivalService festivalService;

    @GetMapping("/api/data")
    public FestivalDTO getData(@RequestParam("festival_idx")int festival_idx) {
        return festivalService.getFestival(festival_idx);
    }
    
}
