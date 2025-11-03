package com.soldesk.festival.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.ChatRoomDTO;
import com.soldesk.festival.dto.MemberDTO;

@Mapper
public interface ChatMapper {

    @Select("select * from chat_room where chat_room_idx = #{roomId}")
    ChatRoomDTO selectChatRoomById(@Param("roomId") int roomId);

    @Select("select * from member where member_idx = #{i}")
    MemberDTO selectMember(@Param("i") int i);
}
