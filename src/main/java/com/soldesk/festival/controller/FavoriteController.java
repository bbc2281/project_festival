package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soldesk.festival.dto.FavoriteDTO;
import com.soldesk.festival.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {
    
    private final FavoriteService favoriteService;

    @PostMapping("/festival/toggleFavorite")
    @ResponseBody
    public Map<String, Object> favorite(@RequestBody FavoriteDTO dto){
        Map<String, Object> result = new HashMap<>();
        System.out.println("축제id"+dto.getFavorite_idx());
        System.out.println("펀딩id"+dto.getFunding_festival_idx());
        System.out.println("일반회원id"+dto.getMember_idx());
        System.out.println("기업회원id"+dto.getCompany_idx());
        if (dto.getMember_idx() != 0 && dto.getFestival_idx() != 0) {
            boolean exists_f = favoriteService.existsFavoriteByMember(dto.getMember_idx(), dto.getFestival_idx());
                if (exists_f) {
                    favoriteService.deleteFavoriteByMember(dto);
                    result.put("status", "removed");
                } else {
                    favoriteService.insertFavoriteByMember(dto);
                    result.put("status", "added");
                }
        } else if(dto.getMember_idx() != 0 && dto.getFunding_festival_idx() != 0 ){
            boolean exists_funding = favoriteService.existsFavoriteFundingByMember(dto.getMember_idx(), dto.getFunding_festival_idx());
                if (exists_funding) {
                    favoriteService.deleteFavoriteFundingByMember(dto);
                    result.put("status", "removed");
                } else {
                    favoriteService.insertFavoriteFundingByMember(dto);
                    result.put("status", "added");
                }
        } else if (dto.getCompany_idx() != 0 && dto.getFestival_idx() != 0) {
            boolean exists_f = favoriteService.existsFavoriteByCompany(dto.getCompany_idx(), dto.getFestival_idx());
                if (exists_f) {
                    favoriteService.deleteFavoriteByCompany(dto);
                    result.put("status", "removed");
                } else {
                    favoriteService.insertFavoriteByCompany(dto);
                    result.put("status", "added");
                }
        } else if (dto.getCompany_idx() != 0 && dto.getFunding_festival_idx() !=0){
            boolean exists_funding = favoriteService.existsFavoriteFundingByCompany(dto.getCompany_idx(), dto.getFunding_festival_idx());
                if (exists_funding) {
                    favoriteService.deleteFavoriteFundingByCompany(dto);
                    result.put("status", "removed");
                } else {
                    favoriteService.insertFavoriteFundingByCompany(dto);
                    result.put("status", "added");
                }
        }

        return result;
    }

}
