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

    public void insertInquiry(InquiryDTO inquiry){
        inquiryMapper.insertInquiry(inquiry);
    }

    public void updateInquiry(InquiryDTO inquiry){
        inquiryMapper.updateInquiry(inquiry);
    }

    public int countInquiryByNUll(){
        return inquiryMapper.countInquiryByNUll();
    }

    public int countInquiryByMember(int idx){
        return inquiryMapper.countInquiryByMember(idx);
    }
}
