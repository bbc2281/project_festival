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
    }//AllFestivals 모든축제정보

    public FestivalDTO getFestival(int id){
        return festivalMapper.selectFestival(id);
    }//getFestival 특정축제

    public List<FestivalCategoryDTO> getCategory(){
        return festivalMapper.selectAllCategory();
    }//getCategory 축제 카테고리리스트

    public List<RegionDTO> getRegion(){
        return festivalMapper.selectAllRegion();
    }//getRegion 서울 지역구 리스트

    public void insertFestival(FestivalDTO festival){
        festivalMapper.insertFestival(festival);
    }//insertFestival 축제 정보추가

    public void deleteFestival(int festival_idx){
        festivalMapper.deleteFestival(festival_idx);
    }//deleteFestival 축제정보 삭제

    public int countFestival(){
        return festivalMapper.countFestival();
    }
    public int countFestivalByKeyword(String keyword){
        return festivalMapper.countFestivalByKeyword(keyword);
    }

    public List<FestivalDTO> selectFestivalPaged(int offset, int limit){
        return festivalMapper.selectFestivalPaged(offset, limit);
    }
    public List<FestivalDTO> selectFestivalPagedByKeyword(String keyword, int offset, int limit){
        return festivalMapper.selectFestivalPagedByKeyword(keyword, offset, limit);
    }
}
