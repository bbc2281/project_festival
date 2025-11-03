package com.soldesk.festival.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataApiService {
    
    @Value("${api.gonggong.key}")
    private String apiKey;

    public boolean verifyBusinessNumber(String companyName, String ownerName, String companyAddress){

    }

}
