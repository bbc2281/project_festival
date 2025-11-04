package com.soldesk.festival.service;

import java.util.Optional;

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
	private final MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String member_id) throws UsernameNotFoundException {
        
		/* 
		Optional<MemberDTO> opMember = memberMapper.findUserDetailAllById(member_id);
		
        MemberDTO member = opMember.orElseThrow(() ->
            new UsernameNotFoundException("회원 정보를 찾을 수 없습니다")
        );
		*/
		Optional<MemberDTO> opMember = memberMapper.findUserDetailAllById(member_id);
        MemberDTO member = opMember.orElseThrow(() ->
            new UsernameNotFoundException("회원 정보를 찾을 수 없습니다")
        );
        
		Optional<CompanyDTO> opCompany = companyMapper.findCompanyUserById(member_id);
		CompanyDTO company = opCompany.orElse(null);
    
        

		return new SecurityAllUsersDTO(member, company);
	
	}
}
