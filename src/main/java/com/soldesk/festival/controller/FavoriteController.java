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

        if (dto.getMember_idx() != 0) {
            boolean exists = favoriteService.existsFavoriteByMember(dto.getMember_idx(), dto.getFestival_idx());
            if (exists) {
                favoriteService.deleteFavoriteByMember(dto);
                result.put("status", "removed");
            } else {
                favoriteService.insertFavoriteByMember(dto);
                result.put("status", "added");
            }
        } else if (dto.getCompany_idx() != 0) {
            boolean exists = favoriteService.existsFavoriteByCompany(dto.getFestival_idx(), dto.getCompany_idx());
            if (exists) {
                favoriteService.deleteFavoriteByCompany(dto);
                result.put("status", "removed");
            } else {
                favoriteService.insertFavoriteByCompany(dto);
                result.put("status", "added");
            }
        }

        return result;
    }

}
