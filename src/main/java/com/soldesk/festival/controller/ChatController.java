package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soldesk.festival.dto.ChatDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    
    @SendTo("/topic/chat/{roomId}")//구독한 클라이언트 들에게 메시지를 발송
    @MessageMapping("/chat/{roomId}")
    public Map<String, Object> sendMessage(@DestinationVariable("roomId")int roomId, ChatDTO message){

        MemberDTO sender = chatService.getMember(message.getChat_sender());//채팅을 보낸 사용자 조회
        String senderEmail = sender.getMember_email();

        Map<String, Object> response = new HashMap<>();
        response.put("chat_type", message.getChat_type());
        response.put("chat_room", message.getChat_room());
        response.put("chat_sender", message.getChat_sender());
        response.put("chat_message", message.getChat_message());
        response.put("sender_email", senderEmail);
        return response;

    }
}