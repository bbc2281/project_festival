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

        boolean exists = favoriteService.existsFavorite(dto.getMember_idx(), dto.getFestival_idx());
        if (exists) {
            favoriteService.deleteFavorite(dto);
            result.put("status", "removed");
        } else {
            favoriteService.insertFavorite(dto);
            result.put("status", "added");
        }
    return result;
    }

}
