package com.soldesk.festival.service;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.FundingDAO;
import com.soldesk.festival.dto.fundingFestivalDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingService {
    
    private final FundingDAO fundingDAO;
    
    public fundingFestivalDTO selectFundingFestival(int funding_festival_idx){

        return fundingDAO.selectFundingFestival(funding_festival_idx);
    }
}
