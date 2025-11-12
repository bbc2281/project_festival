package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import lombok.Getter;

@Getter
public class CompanyDTO {


	private Integer company_idx;
	
	private String company_name;
	
	private String member_id;
	
	private String member_pass;
	
	//private String company_pass2;

	private String member_email;

	private String company_phone;

	private Integer company_reg_num;
	

	private String company_owner;
	

	private String company_open_date;

	private String company_address;
	
	private String company_account;
	
	private MemberRole role;

}
