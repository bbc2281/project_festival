package com.soldesk.festival.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
public class SecurityMemberDTO implements UserDetails, OAuth2User{
	
	private MemberDTO member;
	private Map<String, Object> attributes;
	
	public SecurityMemberDTO(MemberDTO member) {
		this.member = member;
	}
	
	public SecurityMemberDTO(MemberDTO member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authority = new ArrayList<>();
		
		String roleString = member.getRole().getMemberRole();
		authority.add(new SimpleGrantedAuthority(roleString));
		
		return authority;
		
	}//사용자의 권한

	@Override
	public String getPassword() {
		return member.getMember_pass();
	} //사용자의 비밀번호

	@Override
	public String getUsername() {
		return member.getMember_id();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return member.getMember_id();
	} 

}
