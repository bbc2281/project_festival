package com.soldesk.festival.dto;

public enum CompanyRole {
	
	COMPANY,
	FESTIVAL_PLANNER;
	
	public String getCompanyRole() {
		return "ROLE_" + this.name();
	}
	
}
