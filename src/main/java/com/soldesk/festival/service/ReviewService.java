package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.ReviewDAO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.PageDTO;
import com.soldesk.festival.dto.ReviewDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDAO reviewDAO;

    public void writeProcess(ReviewDTO reviewDTO , MemberDTO memberDTO){

        reviewDTO.setMember_idx(memberDTO.getMember_idx());
        reviewDAO.writeProcess(reviewDTO);
    }

    public List<ReviewDTO> selectAllReviews(int festival_idx){
        return reviewDAO.selectAllReviews(festival_idx);
    }

    public PageDTO getPageDTO(int festival_idx,int currentPage){
        int reviewCount = reviewDAO.countReview(festival_idx);
        PageDTO pageDTO = new PageDTO(reviewCount, currentPage);
        return pageDTO;
    }

    public ReviewDTO infoProcess(int review_idx){
        return reviewDAO.infoProcess(review_idx);
    }

    public void modifyProcess(ReviewDTO reviewDTO){
        reviewDAO.modifyProcess(reviewDTO);
    }

    public void deleteReview(int review_idx){
        reviewDAO.deleteReview(review_idx);
    }
    
    public int countReviewNow(String date){
        return reviewDAO.countReviewNow(date);
    }
}
