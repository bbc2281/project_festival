package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.FundingFestivalDTO;
import com.soldesk.festival.dto.PaymentDTO;
import com.soldesk.festival.mapper.FundingFestivalMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingFestivalService {
    
    private final FundingFestivalMapper fundingFestivalMapper;

    public List<FundingFestivalDTO> selectAllFunding(){
        return fundingFestivalMapper.selectAllFunding();
    }

    public FundingFestivalDTO selectFunding(int idx){
        return fundingFestivalMapper.selectFunding(idx);
    }

    public void insertFunding(FundingFestivalDTO funding, CompanyDTO login){

        if(login != null){
            funding.setCompany_idx(login.getCompany_idx());
        }else{
            funding.setCompany_idx(1);
        }

        fundingFestivalMapper.insertFunding(funding);
    }

    public int countFundingByCompany(int company_idx){
        return fundingFestivalMapper.countFundingByCompany(company_idx);
    }

    public void insertFundingAmount(PaymentDTO amount){
        fundingFestivalMapper.insertFundingAmount(amount);
    }

    public int countFunding(){
        return fundingFestivalMapper.countFunding();
    }

    public List<FundingFestivalDTO> getFestivalListPaged(int offset, int limit) {
        return fundingFestivalMapper.getFestivalListPaged(offset, limit);
	}

    public void deleteFunding(int idx){
        fundingFestivalMapper.deleteFunding(idx);
    }

    public List<FundingFestivalDTO> selectFundingByCompany(int idx){
        return fundingFestivalMapper.selectFundingByCompany(idx);
    }
}
