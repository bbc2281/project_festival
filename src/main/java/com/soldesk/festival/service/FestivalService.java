package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dao.FestivalDAO;
import com.soldesk.festival.dto.FestivalDTO;

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

    public List<String> getCategory(){
        return festivalDAO.getCategory();
    } //getCategory

    public List<String> getRegion(){
        return festivalDAO.getRegion();
    }

}
