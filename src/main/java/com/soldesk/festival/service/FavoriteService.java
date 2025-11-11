package com.soldesk.festival.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.FavoriteDTO;
import com.soldesk.festival.dto.FestivalDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.mapper.FavoriteMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    
    private final FavoriteMapper favoriteMapper;

    public boolean existsFavoriteByMember(int member_idx, int festival_idx){
        
        int countMember = favoriteMapper.existsFavoriteByMember(member_idx, festival_idx);
        
        boolean exist = false;
        if(countMember > 0){
            exist = true;
        }
        return exist;
    }
    public boolean existsFavoriteByCompany(int festival_idx, int company_idx){
        
        
        int countCompany = favoriteMapper.existsFavoriteByCompany(company_idx, festival_idx);
        boolean exist = false;
        if(countCompany > 0){
            exist = true;
        }
        return exist;
    }

    public void insertFavoriteByMember(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteByMember(favorite);
    }
    public void insertFavoriteByCompany(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteByCompany(favorite);
    }

    public void deleteFavoriteByMember(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteByMember(favorite);
    }
    public void deleteFavoriteByCompany(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteByCompany(favorite);
    }

    public int countFavoriteByFestival(int festival_idx){
        return favoriteMapper.countFavoriteByFestival(festival_idx);
    }

    public List<FestivalDTO> selectAllFavoriteByUser(MemberDTO loginMember){
        if(loginMember != null){
            int member_idx = loginMember.getMember_idx();
            return favoriteMapper.selectAllFavoriteByUser(member_idx);
        }
        return null;
    }

    public List<FestivalDTO> selectAllFavoriteByCompany(CompanyDTO loginCompany){
        if(loginCompany != null){
            int company_idx = loginCompany.getCompany_idx();
            return favoriteMapper.selectAllFavoriteByCompany(company_idx);
        }
        return null;
    }
}
