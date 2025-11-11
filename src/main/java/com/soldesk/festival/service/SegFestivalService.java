package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.SegFestivalDAO;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.FestivalDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SegFestivalService {
    
    private final SegFestivalDAO segFestivalDAO;

    public void insertSegFestival(FestivalDTO festivalDTO, CompanyDTO company){
        if(company != null){
            festivalDTO.setCompany_idx(company.getCompany_idx());
            festivalDTO.setFestival_host(company.getCompany_name());
            segFestivalDAO.insertSegFestival(festivalDTO);
        }
    }

    public FestivalDTO selectFestival(int idx){
        return segFestivalDAO.selectFestival(idx);
    }

    public List<FestivalDTO> selectFestivalByCompany(int idx){
        return segFestivalDAO.selectFestivalByCompany(idx);
    }

    public List<FestivalDTO> selectAllFestivals(){
        return segFestivalDAO.selectAllFestivals();
    }

    public void deleteFestival(int idx){
        segFestivalDAO.deleteFestival(idx);
    }

    public void updateSetLog(int idx){
        segFestivalDAO.updateSetLog(idx);
    }

    public void updateDelLog(int idx){
        segFestivalDAO.updateDelLog(idx);
    }
}
