package com.soldesk.festival.dto;
 
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberUpdateDTO {

	private Integer member_idx;
    
	@Setter
	private String member_id;
	

    @NotBlank(message="이름을 입력해주세요")
	@Setter
	private String member_name;
    
    @NotBlank
	@Setter
	@Size(min=8, max=30, message="비밀번호는 영문, 숫자,특수문자를 모두 포함하여 8자 이상 입력해야 합니다 ")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$")
	private String member_pass;
	
	@Setter
	private String member_nickname;
	
    @Email(message="이메일 형식이 올바르지 않습니다")
	@Setter
	private String member_email;
	
 
	@Setter
	@NotBlank(message="핸드폰 번호를 입력해주세요")
	private String member_phone;
	
    @NotBlank(message="주소를 입력해주세요")
	@Setter
	private String member_address;
	
	@Setter
	private String member_gender;
    
	@Setter
	private String member_job;
  
    //@NotBlank
	@Setter
	private String member_birth;


}
