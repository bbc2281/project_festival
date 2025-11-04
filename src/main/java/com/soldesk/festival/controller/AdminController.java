package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    //관리자 전용 페이지
    @GetMapping("/auth/admin")
    public String onlyAdminPage(){
        return "auth/admin";
    }

}
