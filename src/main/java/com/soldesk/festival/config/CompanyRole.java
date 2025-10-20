package com.soldesk.festival.config;

import lombok.Getter;

@Getter
public enum CompanyRole {
	
	COMPANY,
	FESTIVAL_PLANNER;
	
	public String getCompanyRole() {
		return "ROLE_" + this.name();
	}
	
}
