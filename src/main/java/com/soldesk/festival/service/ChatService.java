package com.soldesk.festival.service;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.ChatDAO;
import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDAO chatDAO;
    
    public ChatRoomDTO getChatRoomById(int roomId){
        return chatDAO.getChatRoomById(roomId);
    }

    //임시
    public MemberDTO getMember(int i){
        return chatDAO.getMember(i);
    }
}
