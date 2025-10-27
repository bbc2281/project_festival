package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    
    private int member_idx;
    private String member_id;
    private String member_name;
    private String member_pass;
    private String member_nickname;
    private String member_email;
    private String member_phone;
    private String member_address;
    private String member_gender;
    private String member_job;
    private String member_birth;
    private String role;
    private int member_point;
}
