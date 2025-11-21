package com.soldesk.festival.config;

import lombok.Getter;


@Getter
public enum MemberRole {
	
	GUEST,
	USER,
	COMPANY,
	ADMIN, getMemberRole;
	

	public String getMemberRole() {
		return "ROLE_"+ this.name();
	}

	public static MemberRole fromString(String roleName){
		if(roleName == null) {
			return GUEST;
		}
		try {
			return MemberRole.valueOf(roleName.toUpperCase()); //들어온 문자열 ->  대문자
		} catch (IllegalArgumentException e) {
			e.getMessage();
			return GUEST; // 일치하는 enum값 존재하지 않으면 기본값 반환
		}
	}
	

    public boolean isAdmin(){
		return this == ADMIN;
	}


	public boolean isCompany(){
		return this == COMPANY;
	}

	public boolean isGuest(){
		return this == GUEST;
	}

	public boolean hasHigherLevelThan(MemberRole otherRole){
		return this.ordinal() >= otherRole.ordinal();
	}

	/* 💡
	if (userRole.hasHigherLevelThan(MemberRole.USER)) { 
		ADMIN 또는 USER만 접근 가능
	}*/
	
}
