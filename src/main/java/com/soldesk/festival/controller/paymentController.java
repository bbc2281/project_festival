package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.fundingFestivalDTO;
import com.soldesk.festival.service.FundingService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
public class paymentController {
    
    private final FundingService fundingService;

    @GetMapping("/payment/main")
    public String paymentMain(@RequestParam("festivalIdx")int festivalIdx,@RequestParam("orderId")String orderId ,Model model){
        fundingFestivalDTO festivalDTO = fundingService.selectFundingFestival(festivalIdx);
        model.addAttribute("festivalDTO", festivalDTO);
        return "/payment/main";
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(){
        return "/payment/success";
    }

    @GetMapping("/payment/fail")
    public String paymentFail(){
        return "payment/fail";
    }
}
