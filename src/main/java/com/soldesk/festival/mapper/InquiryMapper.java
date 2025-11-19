package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.InquiryDTO;

@Mapper
public interface InquiryMapper {
    
    @Select("select * from inquiry i join member m on i.member_idx = m.member_idx")
    List<InquiryDTO> selectAllInquiry();

    @Select("select * from inquiry i join member m on i.member_idx = m.member_idx where m.member_idx = #{member_idx}")
    List<InquiryDTO> selectInquiry(@Param("member_idx") int member_idx);

    @Insert("insert into inquiry (member_idx, inquiry_title, inquiry_content, inquiry_regDate) values (#{member_idx}, #{inquiry_title}, #{inquiry_content}, now())")
    void insertInquiry(InquiryDTO inquiry);

    @Update("update inquiry set inquiry_answer = #{inquiry_answer} where inquiry_idx = #{inquiry_idx}")
    void updateInquiry(InquiryDTO inquiry);

    @Select("select count(*) from inquiry where inquiry_answer is null")
    int countInquiryByNUll();

    @Select("select count(*) from inquiry where member_idx= #{member_idx}")
    int countInquiryByMember(@Param("member_idx") int idx);

    @Select("select count(*) from inquiry")
    int countInquiry();

    @Select("select * from inquiry i join member m on i.member_idx = m.member_idx order by i.inquiry_idx desc limit #{limit} offset #{offset}")
    List<InquiryDTO> selectInquiryPaged(@Param("offset")int offset, @Param("limit")int limit);
}
