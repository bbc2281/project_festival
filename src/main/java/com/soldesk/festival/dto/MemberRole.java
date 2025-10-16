package com.soldesk.festival.dto;

import lombok.Getter;


@Getter
public enum MemberRole {
	
	USER,
	ADMIN;
	
	public String getMemberRole() {
		return "ROLE_"+ this.name();
	}

}
