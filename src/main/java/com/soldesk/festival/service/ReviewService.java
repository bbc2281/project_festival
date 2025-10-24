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

    public List<ReviewDTO> selectAllReviews(int festival_idx,int page){
        int start = (page - 1 ) * 10 ;
        int limit = 10;
        return reviewDAO.selectAllReviews(festival_idx,start,limit);
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
    
}
