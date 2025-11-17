package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.dto.fundingFestivalDTO;
import com.soldesk.festival.service.FundingService;
import com.soldesk.festival.service.paymentService;

import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class paymentController {
    
    private final FundingService fundingService;
    private final paymentService paymentService;

    @GetMapping("/main")
    public String paymentMain(@RequestParam("festivalIdx")int festivalIdx,@RequestParam("orderId")String orderId ,Model model){
        fundingFestivalDTO festivalDTO = fundingService.selectFundingFestival(festivalIdx);
        PaymentDTO paymentDTO = paymentService.infoProcess(orderId);
        model.addAttribute("festivalDTO", festivalDTO);
        model.addAttribute("payment", paymentDTO);
        return "/payment/main";
    }

    @GetMapping("/success")
    public String paymentSuccess(){
        return "/payment/success";
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam("message")String Message , Model model){
        model.addAttribute("messageparam", Message);
        return "payment/fail";
    }
}
