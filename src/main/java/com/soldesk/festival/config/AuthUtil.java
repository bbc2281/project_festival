package com.soldesk.festival.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtil {
	
	
	private final PasswordEncoder passEncoder;
	
	public boolean checkPassword(String raw, String encoded) {
		
		 return raw != null && encoded != null && passEncoder.matches(raw, encoded);
	}
	
	public String encodedPassword(String rawPassword){
		if(rawPassword == null) {
			throw new IllegalArgumentException("비밀번호가 비어있을 수 없습니다");
		}
		return passEncoder.encode(rawPassword);
	}
	
	public List<GrantedAuthority> toAuthorities(Enum<?>... roles){
		return Arrays.stream(roles)
				.map(role -> new SimpleGrantedAuthority(role.name()))
				.collect(Collectors.toList());
		
	}
}
