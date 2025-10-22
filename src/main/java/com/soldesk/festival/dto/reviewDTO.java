package com.soldesk.festival.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class reviewDTO {
    
    private int review_idx;
    private int festival_idx;
    private int member_idx;
    private String review_title;
    private String review_content;
    private String review_reg_date;
    private String review_img_path;
    private int review_like;
    

}
