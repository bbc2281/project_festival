package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.ReviewDTO;
import com.soldesk.festival.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewDAO {

    private final ReviewMapper reviewMapper;

    public void writeProcess(ReviewDTO reviewDTO){
        reviewMapper.writeProcess(reviewDTO);
    }

    public List<ReviewDTO> selectAllReviews(int festival_idx,int start, int limit){
        return reviewMapper.selectAllReviews(festival_idx,start,limit);
    }

    public int countReview(int festival_idx){
        return reviewMapper.countReview(festival_idx);
    }

    public ReviewDTO infoProcess(int review_idx){
        return reviewMapper.infoProcess(review_idx);
    }

    public void modifyProcess(ReviewDTO reviewDTO){
        reviewMapper.modifyProcess(reviewDTO);
    }
    
}
