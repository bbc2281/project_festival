package com.soldesk.festival.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


@Component
public class AuthUtil {
	
	
	public List<GrantedAuthority> toAuthorities(Enum<?>... roles){
		return Arrays.stream(roles)
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
		
	}
}
