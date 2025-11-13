package com.soldesk.festival.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.AuthUtil;
import com.soldesk.festival.config.MemberRole;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CompanyJoinDTO;
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

    @Transactional(rollbackFor= com.soldesk.festival.exception.UserException.class)
	public void join(CompanyJoinDTO joinCompany){

		joinCompany.setMember_pass(authUtil.encodedPassword(joinCompany.getMember_pass()));
		if(joinCompany.getRole().name().isBlank() || !joinCompany.getRole().isCompany()){
				joinCompany.setRole(MemberRole.COMPANY);
		}
		joinCompany.setRole(joinCompany.getRole());
		companyMapper.insertCompany(joinCompany);
		
	}

	public void deleteCompany(String companyId, String password) {
		
		CompanyDTO thisCompany = findCompanyUserById(companyId).filter(company -> authUtil.checkPassword(password, company.getMember_pass()))
				.orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다"));
		
		
	}

	public List<CompanyDTO> getAllCompanys(){
		return companyMapper.getAllCompanys();
	}

	public void deleteCompanyByAdmin(int id){
		companyMapper.deleteCompanyByAdmin(id);
	}

	public CompanyDTO selectCompanyByIdx(int idx){
		return companyMapper.selectCompanyByIdx(idx);
	}
}
