package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.InquiryDAO;
import com.soldesk.festival.dto.InquiryDTO;
import com.soldesk.festival.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {
    
    private final InquiryDAO inquiryDAO;

    public List<InquiryDTO> selectAllInquiry(){
        return inquiryDAO.selectAllInquiry();
    }

    public List<InquiryDTO> selectInquiry(int member_idx){
        return inquiryDAO.selectInquiry(member_idx);
    }

    public void insertInquiry(InquiryDTO inquiry, MemberDTO loginMember){
        if(loginMember != null){
            inquiry.setMember_idx(loginMember.getMember_idx());
        }

        inquiryDAO.insertInquiry(inquiry);
    }

    public void updateInquiry(InquiryDTO inquiry){
        if(inquiry != null){
            inquiryDAO.updateInquiry(inquiry);
        }
    }

    public int countInquiryByNUll(){
        return inquiryDAO.countInquiryByNUll();
    }

    public int countInquiryByMember(int idx){
        return inquiryDAO.countInquiryByMember(idx);
    }

    public int countInquiry(){
        return inquiryDAO.countInquiry();
    }

    public List<InquiryDTO> getInquiryListPaged(int offset, int limit) {
        return inquiryDAO.selectInquiryPaged(offset, limit);
	}

}
