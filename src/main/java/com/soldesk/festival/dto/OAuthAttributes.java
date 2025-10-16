package com.soldesk.festival.dto;

import java.util.Map;

import lombok.Getter;

@Getter
public class OAuthAttributes {
	
	private String nameAttributeKey; //공급자 식별키(pk)
	private Map<String, Object> attributes; //OAuth 서버에서 가져온 사용자
	private String name;
	private String email;
	
	private String provider; // OAuth2 공급자 구분용 필드(예: "Google", "Naver", "KaKao")
	
	public OAuthAttributes(String nameAttributeKey, Map<String, Object>attributes, String name, String email, String provider) {
		this.nameAttributeKey = nameAttributeKey;
		this.attributes = attributes;
		this.name = name;
		this.email = email;
		this.provider = provider;
		
		System.out.println("nameAttributeKey : " + nameAttributeKey);
	}
	
	public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object>attributes) {
		Map<String, Object> response = (Map<String, Object>)attributes.get("response");
		return new OAuthAttributes(userNameAttributeName, attributes, (String)response.get("home"), response, userNameAttributeName);
	}

}
