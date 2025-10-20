package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {
    
    int board_idx;
    int member_idx;
    int festival_idx;
    int festival_category_idx;
    String board_title;
    String board_content;
    String board_regDate;
    int board_views;
    String board_img_path;
    String board_category;

}
