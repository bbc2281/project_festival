package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberDetailDTO {
    private Integer member_idx;
    private String member_id;
    private String member_name;
    private String member_email;
    private String member_nickname;
    private MemberRole role;


}
