package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController{
  
  private final MemberService memberService;

  @PostMapping("/login")
  public String Login(@RequestParam("member_id") String member_id, HttpSession session){

    System.out.println(member_id);
    MemberDTO member = memberService.selectMember(member_id);
    System.out.println(member.getMember_idx());

    session.setAttribute("loginMember", member);
    session.setMaxInactiveInterval(60*30);
    return "redirect:/";
  }

  @GetMapping("/logout")
  public String Logout(HttpSession session){

    session.removeAttribute("loginMember");
    return "redirect:/";
  }

}
