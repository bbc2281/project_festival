package com.soldesk.festival.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.SecurityMemberDTO;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
	
	private final MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberDTO member = memberMapper.findUserById(username);
		if(member != null) {
			return new SecurityMemberDTO(member);
		}
		throw new UsernameNotFoundException("사용자를 찾을 수 없습니다" + username);
		
	}

}
