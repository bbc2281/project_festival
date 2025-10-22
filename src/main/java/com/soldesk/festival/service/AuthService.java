package com.soldesk.festival.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.mapper.CompanyMapper;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
	
	private final MemberMapper memberMapper;
	private final CompanyMapper companyMapper;
	private final AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		CompanyDTO company = companyMapper.findCompanyUserById(username).orElse(null);
		
		if(company != null){
			return new SecurityAllUsersDTO(null,company);
		}


		MemberDTO member = memberMapper.findUserById(username)
						    .orElseThrow(()-> new UsernameNotFoundException("사용자의 이름을 찾을 수 없습니다" + username));
        		
		return new SecurityAllUsersDTO(member, null);
	}

	  
    public UserDetails login(String id, String pass){
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id,pass));
        return (UserDetails)auth.getPrincipal();
    }

}
