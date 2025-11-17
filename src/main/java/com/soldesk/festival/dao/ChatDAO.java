package com.soldesk.festival.dao;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.mapper.ChatMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatDAO {
    private final ChatMapper chatMapper;

    public ChatRoomDTO getChatRoomById(int roomId){
        return chatMapper.selectChatRoomById(roomId);
    }//특정 채팅방 조회

    //임시
    public MemberDTO getMember(int i){
        return chatMapper.selectMember(i);
    }//특정 멤버 조회
}
