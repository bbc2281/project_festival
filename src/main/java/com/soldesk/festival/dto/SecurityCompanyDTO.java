package com.soldesk.festival.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class SecurityCompanyDTO implements UserDetails{
	
	private CompanyDTO company;
	
	public SecurityCompanyDTO(CompanyDTO company) {
		this.company = company;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		String roleString = company.getRole().getCompanyRole();
		authorities.add(new SimpleGrantedAuthority(roleString));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return company.getCompany_pass();
	}

	@Override
	public String getUsername() {
		return company.getCompany_id();
	}

}
