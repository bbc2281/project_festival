package com.soldesk.festival.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.MemberDTO;

@Mapper
public interface MemberMapper {
  
  @Select("select * from member where member_id = #{member_id}")
  MemberDTO selectMember(String member_id);
}
