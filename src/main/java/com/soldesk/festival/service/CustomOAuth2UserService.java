package com.soldesk.festival.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
/* 
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	@Autowired
	private final MemberMapper memberMapper;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oauth2User = delegate.loadUser(userRequest);
		
		//google에서 받아온 사용자 정보
		Map<String, Object> attributes = oauth2User.getAttributes();
		System.out.println("OAuth2User attributes : " + attributes);
		
		String userNameAttributeName = userRequest.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();
		
		System.out.println("OAuth2 공급자 : " + userNameAttributeName);
		
		
		
		//MemberDTO existingMember = memberMapper.selectUserByEmail();		
		
		 //new SecurityMemberDTO(existingMember);
	}

}
*/