package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soldesk.festival.dto.companyDTO;
import com.soldesk.festival.dto.fundingFestivalDTO;
import com.soldesk.festival.service.FundingService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/funding")
@RequiredArgsConstructor
public class FundingController {
    
    private final FundingService fundingService;


    @GetMapping("/main")
    public String mainForm(){
        return "/funding/main";
    }
    
    @GetMapping("/info")
    public String infoForm(Model model , HttpSession session){
        fundingFestivalDTO fundingFestivalDTO = fundingService.selectFundingFestival(1);
        
        //해당 객체를 모델에 넣어 보내야만 결제 시스템이 정상 작동 됩니다.
        model.addAttribute("fundingFestival", fundingFestivalDTO);

        //임시용 으로 세션에 객체를 넣었습니다.
        companyDTO companyDTO = new companyDTO();
        session.setAttribute("companyMember", companyDTO);

        return "/funding/info";
    }

}
