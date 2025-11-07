package com.soldesk.festival.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordVerifyDTO {
    
    @NotBlank(message="비밀번호는 필수로 입력해야 합니다")
    private String current_pass;

}
