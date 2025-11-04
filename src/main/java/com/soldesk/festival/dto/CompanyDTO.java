package com.soldesk.festival.dto;

import com.soldesk.festival.config.CompanyRole;

import lombok.Getter;

@Getter
public class CompanyDTO {


	private Integer company_idx;
	
	private String company_name;
	
	private String company_id;
	
	private String company_pass;
	
	private String company_pass2;

	private String company_email;

	private String company_phone;

	private Integer company_reg_num;
	

	private String company_owner;
	

	private String company_open_date;

	private String company_address;
	
	private String company_account;
	
	private CompanyRole role;

}
