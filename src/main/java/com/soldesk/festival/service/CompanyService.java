package com.soldesk.festival.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.MemberRole;
import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CompanyDetailDTO;
import com.soldesk.festival.dto.CompanyJoinDTO;
import com.soldesk.festival.dto.CompanyUpdateDTO;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.mapper.CompanyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
	
	private final CompanyMapper companyMapper;
	private final PasswordEncoder passwordEncoder;

		
	/*public boolean checkPassword(String raw, String encoded) {
		
		 return raw != null && encoded != null && passwordEncoder.matches(raw, encoded);
	}*/
	
	public String encodedPassword(String rawPassword){
		if(rawPassword == null) {
			throw new IllegalArgumentException("비밀번호가 비어있을 수 없습니다");
		}
		return passwordEncoder.encode(rawPassword);
	}
	

    //내부 시스템 조회용 
	public Optional<CompanyDTO> findCompanyUserById(String companyId){
		
		return companyMapper.findCompanyUserById(companyId);
	} 

	public Optional<CompanyDTO> findCompanyDetailAllById(String comId){
		return companyMapper.findCompanyDetailAllById(comId);
	}
	
	//기업회원용 사업자번호로 기업회원찾기
	@Transactional(readOnly=true)
	public Optional<CompanyDTO> findCompanyUserByregNum(int regNum){
		
		return Optional.ofNullable(companyMapper.findCompanyUserByregNum(regNum));
	}



    @Transactional(rollbackFor= com.soldesk.festival.exception.UserException.class)
	public void join(CompanyJoinDTO joinCompany){

		joinCompany.setMember_pass(encodedPassword(joinCompany.getMember_pass()));
		if(joinCompany.getRole().name().isBlank() || !joinCompany.getRole().isCompany()){
				joinCompany.setRole(MemberRole.COMPANY);
		}
		joinCompany.setRole(joinCompany.getRole());
		companyMapper.insertCompany(joinCompany);

	}

    
	public Optional<CompanyDetailDTO> getDetails(String userId){

		Optional<CompanyDTO> opCom = companyMapper.findUserIdforUser(userId);
		
		if(opCom.isPresent()){
			CompanyDTO company = opCom.get();
			return Optional.of(
				CompanyDetailDTO.builder()
				.company_idx(company.getCompany_idx())
				.company_name(company.getCompany_name())
				.member_id(company.getMember_id())
				.member_email(company.getMember_email())
				.company_owner(company.getCompany_owner())
				.company_open_date(company.getCompany_open_date())
				.company_address(company.getCompany_address())
				.role(company.getRole())
				.build()
			);
		}
		return Optional.empty();

	}


	
	public void deleteCompany(String companyId, String password) {
		
		CompanyDTO thisCompany = findCompanyUserById(companyId).filter(company -> checkPassword(password, company.getMember_pass()))
				.orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다"));
		
		
	}

   
	public List<CompanyDetailDTO> getCompanyList(String userId){

		Optional<CompanyDTO> opCompany = companyMapper.findCompanyDetailAllById(userId);

		if(opCompany.isPresent()){
		   CompanyDTO admin = opCompany.get();

		   if(admin.getRole().isAdmin()){
			  
			  List<CompanyDetailDTO> list = companyMapper.getCompanyList();
			  
			  return list.stream()
			         .map(company -> CompanyDetailDTO.builder()
					 .company_idx(company.getCompany_idx())
					 .company_name(company.getCompany_name())
					 .member_id(company.getMember_id())
					 .member_email(company.getMember_email())
					 .company_open_date(company.getCompany_open_date())
					 .company_owner(company.getCompany_owner())
					 .company_address(company.getCompany_address())
					 .build()
					 )
					 .collect(Collectors.toList());
					 

		   }
		}
		return Collections.emptyList();
	}


   
	public boolean checkPassword(String userId, String pass){

		Optional<CompanyDTO> opCom = findCompanyUserById(userId);

		if(opCom.isEmpty()){
			return false;
		}

		CompanyDTO company = opCom.get();
		String password = company.getMember_pass();

		return checkPassword(pass, password);

	}

    @Transactional(rollbackFor = com.soldesk.festival.exception.UserException.class)
	public void updateCompany(CompanyUpdateDTO updateUser){

		CompanyDTO existingMember = findCompanyUserById(updateUser.getMember_id()).orElseThrow(() -> new UserException("아이디나 비밀번호가 일치하지 않습니다"));

	    if(updateUser.getMember_pass() == null || updateUser.getMember_pass().isBlank()){
			updateUser.setMember_pass(existingMember.getMember_pass());
		}else{
		    		
			updateUser.setMember_pass(encodedPassword(updateUser.getMember_pass()));
		}
		companyMapper.updateCompany(updateUser);

	}
    

	
}
