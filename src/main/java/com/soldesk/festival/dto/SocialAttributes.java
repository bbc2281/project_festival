package com.soldesk.festival.dto;

import java.util.Map;

import com.soldesk.festival.config.MemberRole;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SocialAttributes {

    private String name;
    private String email;
    private String id;
    private String provider;
    private MemberRole role;

    private SocialAttributes(Map<String, Object> attributes){
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.id = (String) attributes.get("sub");
    }

    public static SocialAttributes of(String registrationId, Map<String, Object> attributes){
        if("google".equals(registrationId)){
            return new SocialAttributes(attributes);
        }else if("naver".equals(registrationId)){
            return new SocialAttributes(attributes);
        }

        throw new IllegalArgumentException();
    }

}
