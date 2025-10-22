package com.soldesk.festival.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.AuthUtil;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.mapper.CompanyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
	
	private final CompanyMapper companyMapper;
	private final AuthUtil authUtil;

    //내부 시스템 조회용 
	public Optional<CompanyDTO> findCompanyUserById(String companyId){
		
		return companyMapper.findCompanyUserById(companyId);
	} 
	
	//기업회원용 사업자번호로 기업회원찾기
	@Transactional(readOnly=true)
	public Optional<CompanyDTO> findCompanyUserByregNum(int regNum){
		
		return Optional.ofNullable(companyMapper.findCompanyUserByregNum(regNum));
	}
	
	public void deleteCompany(String companyId, String password) {
		
		CompanyDTO thisCompany = findCompanyUserById(companyId).filter(company -> authUtil.checkPassword(password, company.getCompany_pass()))
				.orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다"));
		
		//thisCompany.setDeleted(true);
		//thisCompany.setDeletedAt(LocalDateTime.now());
		
	}
}
