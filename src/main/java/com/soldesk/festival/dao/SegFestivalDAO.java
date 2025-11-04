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
    }//제안축제 정보 추가

    public FestivalDTO selectFestival(int idx){
        return segFestivalMapper.selectFestival(idx);
    }//제안축제중 특정축제 선택

    public List<FestivalDTO> selectAllFestivals(){
        return segFestivalMapper.selectAllFestivals();
    }//제안축제 모든정보 선택

    public void deleteFestival(int idx){
        segFestivalMapper.deleteFestival(idx);
    }//제안축제 삭제
}
