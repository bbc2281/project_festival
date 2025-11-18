package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CompanyUpdateDTO {

    
	private Integer company_idx;
	
	@Setter
	private String company_name;
	
	@Setter
	private String member_id;
	
	//@NotBlank(message="비밀번호를 입력해주세요")
    @Setter
	private String member_pass;
	
	//@NotBlank(message="비밀번호확인입력을 해주세요")
	//private String company_pass2;
	
	@Email(message="올바른 이메일 형식이 아닙니다")
    @Setter
	private String member_email;
	
	@NotBlank(message="회사 연락처를 입력해주세요")
    @Setter
	private String company_phone;
	
	@NotBlank(message="사업자 번호를 입력해주시길 바랍니다")
	@Setter
	private String company_reg_num;
	
	//@NotBlank(message="사업주 이름을 입력해주세요")
	//private String company_owner;
	
	//private String company_open_date;
	
	@NotBlank(message="회사 소재지 주소를 입력해주세요")
    @Setter
	private String company_address;
	
	@NotBlank(message="계좌 번호를 입력해주세요")
    @Setter
	private String company_account;
	
	//private boolean deleted;
	//private LocalDateTime deletedAt;

   

}
