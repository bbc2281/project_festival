package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {

	//private Integer member_idx;
    
	//@Size(min=4, max=20, message="아이디는 영어 대소문자와 숫자만 사용가능하며 4~20글자여야합니다")
    @NotBlank(message="아이디를 입력해주세요")
	private String member_id;
    
	//@Size(min=8, max=30, message="비밀번호는 영문, 숫자,특수문자를 모두 포함하여 8자 이상 입력해야 합니다")
	@NotBlank(message="비밀번호를 입력해주세요")
	private String member_pass;

    private MemberRole role;
	

}
