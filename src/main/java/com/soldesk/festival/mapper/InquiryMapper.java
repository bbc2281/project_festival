package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.InquiryDTO;

@Mapper
public interface InquiryMapper {
    
    @Select("select * from inquiry i join member m on i.member_idx = m.member_idx")
    List<InquiryDTO> selectAllInquiry(); 

    @Insert("insert into inquiry (member_idx, inquiry_title, inquiry_content, inquiry_regDate) values (#{member_idx}, #{inquiry_title}, #{inquiry_content}, now())")
    void insertInquiry(InquiryDTO inquiry);
}
