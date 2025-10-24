package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {
    
    private String chat_type;// 채팅 유형(입장, 채팅, 퇴장)
    private int chat_room;// 채팅방 번호
    private int chat_sender;// 채팅 보낸 사람
    private String chat_message;// 채팅 내용

    private String sender_name; // 보낸 사람 이름
    private String sender_id; // 보낸 사람 아이디
    private String sender_nickname; // 보낸 사람 닉네임
}
