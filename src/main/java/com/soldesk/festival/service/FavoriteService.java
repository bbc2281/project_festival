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
        boolean exist = false;

        int countMember = favoriteMapper.existsFavoriteByMember(member_idx, festival_idx);
        if(countMember > 0){
            exist = true;
        }
        return exist;
    }

    public boolean existsFavoriteFundingByMember(int member_idx, int funding_idx){
        boolean exist = false;

        int count = favoriteMapper.existsFavoriteFundingByMember(member_idx, funding_idx);
        if(count > 0){
            exist = true;
        }

        return exist;
    }

    public boolean existsFavoriteByCompany(int company_idx, int festival_idx){
        
        
        int countCompany = favoriteMapper.existsFavoriteByCompany(company_idx, festival_idx);
        boolean exist = false;
        if(countCompany > 0){
            exist = true;
        }
        return exist;
    }

    public boolean existsFavoriteFundingByCompany(int company_idx, int funding_idx){
        boolean exist = false;

        int count = favoriteMapper.existsFavoriteFundingByCompany(company_idx, funding_idx);
        if(count > 0){
            exist = true;
        }

        return exist;
    }
    //insert
    public void insertFavoriteByMember(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteByMember(favorite);
    }
    public void insertFavoriteByCompany(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteByCompany(favorite);
    }
    public void insertFavoriteFundingByMember(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteFundingByMember(favorite);
    }
    public void insertFavoriteFundingByCompany(FavoriteDTO favorite){
        favoriteMapper.insertFavoriteFundingByCompany(favorite);
    }
    //delete
    public void deleteFavoriteByMember(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteByMember(favorite);
    }
    public void deleteFavoriteByCompany(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteByCompany(favorite);
    }
    public void deleteFavoriteFundingByMember(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteFundingByMember(favorite);
    }
    public void deleteFavoriteFundingByCompany(FavoriteDTO favorite){
        favoriteMapper.deleteFavoriteFundingByCompany(favorite);
    }
    //count
    public int countFavoriteFundingByFestival(int funding_festival_idx){
        return favoriteMapper.countFavoriteFundingByFestival(funding_festival_idx);
    }
    public int countFavoriteByFestival(int festival_idx){
        return favoriteMapper.countFavoriteByFestival(festival_idx);
    }
    public int countFavoriteByMember(int member_idx){
        return favoriteMapper.countFavoriteByMember(member_idx);
    }
    public int countFavoriteByCompany(int company_idx){
        return favoriteMapper.countFavoriteByCompany(company_idx);
    }

    // select List
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
