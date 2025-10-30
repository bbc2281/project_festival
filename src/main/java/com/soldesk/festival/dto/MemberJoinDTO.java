package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDTO {
	
	//일반회원 &  관리자
	
	private Integer member_idx;
	
	@NotBlank(message="아이디를 입력해주세요")
	@Size(min=4, max=20, message="아이디는 영어 대소문자와 숫자만 사용가능하며 4~20글자여야합니다")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,20}$")
	private String member_id;
	
	@NotBlank(message="이름을 입력해주세요")
	private String member_name;
	
	@NotBlank(message="비밀번호를 입력해주세요")
	@Size(min=8, max=30, message="비밀번호는 영문, 숫자,특수문자를 모두 포함하여 8자 이상 입력해야 합니다 ")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$")
	private String member_pass;
	
	//@NotBlank(message="비밀번호 확인입력을 해주세요")
	//private String member_pass2;
	
	private String member_nickname;
	
	@Email(message="이메일 형식이 올바르지 않습니다")
	private String member_email;
	
	@NotBlank(message="핸드폰 번호를 입력해주세요")
	private String member_phone;
	
	@NotBlank(message="주소를 입력해주세요")
	private String member_address;
	
	private String member_gender;
	private String member_job;
	
	@Pattern(regexp="\\d{4}-\\d{2}-\\d{2}", message="YYYY-MM-DD형식으로 입력해주세요")
	@NotNull(message="생년월일을 선택해주세요")
	private String member_birth;
	
	private int member_point;
	private MemberRole role;
	
	//private boolean deleted;
	//private LocalDateTime deletedAt;

}
