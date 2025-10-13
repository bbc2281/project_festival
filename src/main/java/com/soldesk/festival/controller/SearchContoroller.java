package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchContoroller {
    
    @GetMapping("/search")
    public String search(){
        return "search/search";
    }
}
