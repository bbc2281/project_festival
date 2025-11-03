package com.soldesk.festival.config;

import lombok.Getter;

@Getter
public enum CompanyRole {
	
	GUEST,
	FESTIVAL_PLANNER,
	COMPANY;
	
	public String getCompanyRole() {
		return "ROLE_" + this.name();
	}
	
	public static CompanyRole fromString(String rolename){
		if(rolename == null){
			return GUEST;
		}
		try {
			return CompanyRole.valueOf(rolename.toUpperCase());
		} catch (IllegalStateException e) {
			e.getMessage();
			return GUEST;
		}
	}

	public boolean isCompany(){
		return this == COMPANY;
	}

	public boolean hasHigherLevelThan(CompanyRole otherRole){
		return this.ordinal() >= otherRole.ordinal();
	}   
}
