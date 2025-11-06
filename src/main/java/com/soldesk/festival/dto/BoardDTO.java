package com.soldesk.festival.dto;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    
    private int board_idx;
    private Integer member_idx;
    private int festival_idx;
    private int festival_category_idx;
    private String board_title;
    private String board_content;
    private Date board_regDate;
    private int board_views;
    private String board_img_path;
    private String board_category;
    private String member_nickname;
    



}
