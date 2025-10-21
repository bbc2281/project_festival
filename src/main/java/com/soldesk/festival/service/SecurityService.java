package com.soldesk.festival.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.SecurityMemberDTO;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
	
	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return memberMapper.findUserById(username)
		                   .map(member-> new SecurityMemberDTO(member))
						   .orElseThrow(()-> new UsernameNotFoundException("사용자의 이름을 찾을 수 없습니다" + username));
		
	}

}
