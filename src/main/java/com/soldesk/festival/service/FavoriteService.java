package com.soldesk.festival.service;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.FavoriteDTO;
import com.soldesk.festival.mapper.FavoriteMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    
    private final FavoriteMapper favoriteMapper;

    public boolean existsFavorite(int member_idx, int festival_idx){
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setFestival_idx(festival_idx);
        favoriteDTO.setMember_idx(member_idx);
        int count = favoriteMapper.existsFavorite(favoriteDTO);
        boolean exist = false;
        if(count > 0){
            exist = true;
        }
        return exist;
    }

    public void insertFavorite(FavoriteDTO favorite){
        favoriteMapper.insertFavorite(favorite);
    }

    public void deleteFavorite(FavoriteDTO favorite){
        favoriteMapper.deleteFavorite(favorite);
    }

    public int countFavoriteByFestival(int festival_idx){
        return favoriteMapper.countFavoriteByFestival(festival_idx);
    }

}
