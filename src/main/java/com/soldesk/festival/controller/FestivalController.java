package com.soldesk.festival.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.ChatService;
import com.soldesk.festival.service.FestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final ChatService chatService;
    
    @GetMapping("/festivalInfo")
    public String info(@RequestParam("id") int id, Model model){
        
        FestivalDTO festival = festivalService.getFestival(id);
        model.addAttribute("festival", festival);

        ChatRoomDTO chatRoom = chatService.getChatRoomById(id);
        model.addAttribute("chatRoom", chatRoom);

        MemberDTO memberDTO = chatService.getMember(3);

        model.addAttribute("loginMember", memberDTO);

        return "festival/festival";
    }
}
