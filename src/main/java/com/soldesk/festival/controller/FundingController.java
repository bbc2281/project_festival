package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/funding")
public class FundingController {
    
    @GetMapping("/main")
    public String main(){
        return "funding/main";
    }

    @GetMapping("/info")
    public String info(){
        return "funding/info";
    }
}
