package com.soldesk.festival.dao;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberDAO {
  
  private final MemberMapper memberMapper;

  public MemberDTO selectMember(String member_id){
    return memberMapper.selectMember(member_id);
  }
}
