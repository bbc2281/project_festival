package com.soldesk.festival.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.mapper.SegFestivalMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SegFestivalDAO {
    
    private final SegFestivalMapper segFestivalMapper;

    public void insertSegFestival(FestivalDTO festivalDTO){
        segFestivalMapper.insertSegFestival(festivalDTO);
    }

    public FestivalDTO selectFestival(int idx){
        return segFestivalMapper.selectFestival(idx);
    }

    public List<FestivalDTO> selectAllFestivals(){
        return segFestivalMapper.selectAllFestivals();
    }

    public void deleteFestival(int idx){
        segFestivalMapper.deleteFestival(idx);
    }
}
