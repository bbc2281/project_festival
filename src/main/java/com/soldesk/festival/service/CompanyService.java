package com.soldesk.festival.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.soldesk.festival.config.AuthUtil;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.SecurityCompanyDTO;
import com.soldesk.festival.exception.CompanyException;
import com.soldesk.festival.mapper.CompanyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
	
	private final CompanyMapper companyMapper;
	private AuthUtil authUtil;
	
	public CompanyService(CompanyMapper companyMapper, AuthUtil authUtil) {
		this.companyMapper = companyMapper;
		this.authUtil = authUtil;
	}
    
	public UserDetails loginCompany(String companyId, String pass) {
		
		return findCompanyUserById(companyId).filter(company -> authUtil.checkPassword(pass, company.getCompany_pass()))
				.map(SecurityCompanyDTO::new)
				.orElseThrow(()-> new CompanyException("아이디나 비밀번호가 일치하지 않습니다"));
	}
	
	public Optional<CompanyDTO> findCompanyUserById(String companyId){
		
		return Optional.ofNullable(companyMapper.findCompanyUserById(companyId));
	} // 시스템 조회용
	
	public Optional<CompanyDTO> findCompanyUserByregNum(int regNum){
		
		return Optional.ofNullable(companyMapper.findCompanyUserByregNum(regNum));
	}
	
	public void deleteCompany(String companyId, String password) {
		
		CompanyDTO thisCompany = findCompanyUserById(companyId).filter(company -> authUtil.checkPassword(password, company.getCompany_pass()))
				.orElseThrow(()-> new CompanyException("아이디나 비밀번호가 일치하지 않습니다"));
		
		thisCompany.setDeleted(true);
		thisCompany.setDeletedAt(LocalDateTime.now());
		
	}
}
