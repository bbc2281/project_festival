package com.soldesk.festival.service;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.MemberDAO;
import com.soldesk.festival.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberDAO memberDAO;

  public MemberDTO selectMember(String member_id){
    return memberDAO.selectMember(member_id);
  }
}
