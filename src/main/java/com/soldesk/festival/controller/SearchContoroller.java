package com.soldesk.festival.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchContoroller {
    
    

    @GetMapping("/search")
    public String search(Model model){

        return "search/search";
    }
}
