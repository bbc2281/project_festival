package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.FestivalDAO;
import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.RegionDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FestivalService {
    
    private final FestivalDAO festivalDAO;

    public List<FestivalDTO> AllFestivals(){
        
        return festivalDAO.AllFestivals();
    } //AllFestivals

    public FestivalDTO getFestival(int id){
        return festivalDAO.getFestival(id);
    } //getFestival

    public List<FestivalCategoryDTO> getCategory(){
        return festivalDAO.getCategory();
    } //getCategory

    public List<RegionDTO> getRegion(){
        return festivalDAO.getRegion();
    }

    public void insertFestival(FestivalDTO festival){
        festival.setSeg_festival_idx((Integer) null);
        
        festivalDAO.insertFestival(festival);
    }

    public void deleteFestival(int festival_idx){
        festivalDAO.deleteFestival(festival_idx);
    }
    
    public int countFestival(String keyword){
        if(keyword != null && !keyword.trim().isEmpty()){
			keyword = keyword.trim();
            return festivalDAO.countFestivalByKeyword(keyword);
        }else{
            return festivalDAO.countFestival();
        }
    }

    public List<FestivalDTO> getFestivalListPaged(String keyword, int offset, int limit) {
        if(keyword != null && !keyword.trim().isEmpty()){
			keyword = keyword.trim();
            return festivalDAO.selectFestivalPagedByKeyword(keyword, offset, limit);
        }else{
            return festivalDAO.selectFestivalPaged(offset, limit);}
	}
}
