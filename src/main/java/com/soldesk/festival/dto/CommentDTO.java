package com.soldesk.festival.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    
    private int comment_idx;
    private int member_idx;
    private int review_idx;
    private String member_nickname;
    private String comment_content;
    private String comment_regDate;
    private String member_nickname;
}
