package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryDTO {
    
    private int inquiry_idx;
    private int member_idx;
    private String inquiry_title;
    private String inquiry_content;
    private String inquiry_regDate;

    private String member_name;
    private String member_email;
}
