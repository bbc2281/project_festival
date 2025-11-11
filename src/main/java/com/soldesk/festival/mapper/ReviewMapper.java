package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.ReviewDTO;

@Mapper
public interface ReviewMapper {

        @Insert("insert into review (review_content, review_reg_date, review_img_path, festival_idx, member_idx ) values (#{review_content}, now(), #{review_img_path}, #{festival_idx}, #{member_idx})")
        void writeProcess(ReviewDTO reviewDTO);

        @Select("select r.review_idx, r.review_content, r.review_reg_date, r.festival_idx, r.review_img_path, r.review_like, m.member_nickname "
                + " from review r LEFT join member m on r.member_idx = m.member_idx"
                + " where festival_idx = #{festival_idx}")
        List<ReviewDTO> selectAllReviews(int festival_idx);
        
        @Select("select count(*) from review where festival_idx = #{festival_idx}")
        int countReview(int festival_idx);
        
        @Select("select * from review where review_idx = #{review_idx} ")
        ReviewDTO infoProcess(int review_idx);

        @Update("update review set review_content=#{review_content}, review_img_path=#{review_img_path} where review_idx = #{review_idx}")
        void modifyProcess(ReviewDTO reviewDTO);

        @Delete("delete from review where review_idx = #{review_idx}")
        void deleteReview(int review_idx);

        @Select("select count(*) from review where review_reg_date = #{date}")
        int countReviewNow(@Param("date") String date);

        @Select("select * from review where member_idx = #{member_idx}")
        List<ReviewDTO> infoReviewByMember(@Param("member_idx") int idx);
    }