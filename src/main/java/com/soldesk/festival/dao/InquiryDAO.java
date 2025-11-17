package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.InquiryDTO;
import com.soldesk.festival.mapper.InquiryMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InquiryDAO {
    
    private final InquiryMapper inquiryMapper;

    public List<InquiryDTO> selectAllInquiry(){
        return inquiryMapper.selectAllInquiry();
    }

    public List<InquiryDTO> selectInquiry(int member_idx){
        return inquiryMapper.selectInquiry(member_idx);
    }

    public void insertInquiry(InquiryDTO inquiry){
        inquiryMapper.insertInquiry(inquiry);
    }

    public void updateInquiry(InquiryDTO inquiry){
        inquiryMapper.updateInquiry(inquiry);
    }

    public int countInquiryByNUll(){
        return inquiryMapper.countInquiryByNUll();
    }

    public int countInquiry(){
        return inquiryMapper.countInquiry();
    }

    public int countInquiryByMember(int idx){
        return inquiryMapper.countInquiryByMember(idx);
    }

    public List<InquiryDTO> selectInquiryPaged(int offset, int limit){
        return inquiryMapper.selectInquiryPaged(offset, limit);
    }
}
