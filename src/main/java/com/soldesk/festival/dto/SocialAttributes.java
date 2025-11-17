package com.soldesk.festival.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class SocialAttributes {

    private String name;
    private String email;
    private String id;
    private String provider;

    private SocialAttributes(Map<String, Object> attributes){
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.id = (String) attributes.get("sub");
    }

    public static SocialAttributes of(String registrationId, Map<String, Object> attributes){
        if("google".equals(registrationId)){
            return new SocialAttributes(attributes);
        }

        throw new IllegalArgumentException();
    }

}
