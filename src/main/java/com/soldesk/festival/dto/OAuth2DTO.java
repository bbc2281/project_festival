package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2DTO {

    private String name;
    private String email;
    private String id;
    private String provider;

}
