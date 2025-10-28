package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.FestivalCategoryDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.RegionDTO;
import com.soldesk.festival.mapper.FestivalMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FestivalDAO {
    
    private final FestivalMapper festivalMapper;

    public List<FestivalDTO> AllFestivals(){
        return festivalMapper.selectAllFestival();
    }//AllFestivals

    public FestivalDTO getFestival(int id){
        return festivalMapper.selectFestival(id);
    }//getFestival

    public List<FestivalCategoryDTO> getCategory(){
        return festivalMapper.selectAllCategory();
    }//getCategory

    public List<RegionDTO> getRegion(){
        return festivalMapper.selectAllRegion();
    }
}
