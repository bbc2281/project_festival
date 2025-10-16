package com.soldesk.festival.dto;
import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
	
	// 축제펀딩투자하는 기업 & 축제기획 등록하는 그룹
	
	private Integer company_idx;
	
	@NotBlank(message = "회사이름을 입력해주세요")
	private String company_name;
	
	@NotBlank(message = "아이디를 입력해주세요")
	private String company_id;
	
	@NotBlank(message = "비밀번호를 입력해주세요")
	private String company_pass;
	
	@NotBlank(message = "비밀번호확인입력을 해주세요")
	private String company_pass2;
	
	@Email(message = "올바른 이메일 형식이 아닙니다")
	private String company_email;
	
	@NotBlank(message = "회사 연락처를 입력해주세요")
	private String company_phone;
	
	@NotBlank(message = "사업자 번호를 입력해주시길 바랍니다")
	private Integer company_reg_num;
	
	@NotBlank(message = "사업주 이름을 입력해주세요")
	private String company_owner;
	
	@NotBlank(message = "회사 창립날짜를 입력해주세요")
	private Date company_open_date;
	
	@NotBlank(message = "회사 소재지 주소를 입력해주세요")
	private String company_address;
	
	private Integer company_account;
	
	private CompanyRole role;
	
	private boolean deleted;
	private LocalDateTime deletedAt;

}
