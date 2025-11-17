package com.soldesk.festival.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.fundingFestivalDTO;

@Mapper
public interface FundingMapper {
    
    @Select("select * from festival_funding where funding_festival_idx = #{funding_festival_idx}")
    fundingFestivalDTO selectFundingFestival(int funding_festival_idx);

}
