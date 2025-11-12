package com.soldesk.festival.dao;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.fundingFestivalDTO;
import com.soldesk.festival.mapper.FundingMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FundingDAO {
    
    private final FundingMapper fundingMapper;

    public fundingFestivalDTO selectFundingFestival(int funding_festival_idx){

        return fundingMapper.selectFundingFestival(funding_festival_idx);
    }

}
