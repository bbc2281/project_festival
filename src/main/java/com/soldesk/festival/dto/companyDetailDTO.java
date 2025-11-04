package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class companyDetailDTO {

    private Integer company_idx;
	
	private String company_name;
	
	private String member_id;

	private String member_email;

	private String company_owner;
	
	private String company_open_date;

	private String company_address;
	
	
	private MemberRole role;

}
