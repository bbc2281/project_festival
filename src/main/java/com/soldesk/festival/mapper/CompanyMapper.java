package com.soldesk.festival.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CompanyJoinDTO;
import com.soldesk.festival.handler.CompanyRoleTypeHandler;

@Mapper
public interface CompanyMapper {

	
	@Results({
		@Result(property="company_id", column="company_id"),
		@Result(property="company_name", column="company_name"),
		@Result(property="role", column= "role", typeHandler=CompanyRoleTypeHandler.class)
	})
	@Select("select * from company where company_id=#{company_id}")
	Optional<CompanyDTO> findCompanyUserById(@Param("company_id")String id); //시스템 내부에서 조회용
   
	@Results({
		@Result(property="company_idx", column="company_idx"),
		@Result(property="company_name", column="company_name"),
		@Result(property="company_id", column="company_id"),
		@Result(property="company_pass", column="company_pass"),
		@Result(property="company_email", column="company_email"),
		@Result(property="company_phone", column="company_phone"),
		@Result(property="company_reg_num", column="company_reg_num"),
		@Result(property="company_owner", column="company_owner"),
		@Result(property="company_open_date", column="company_open_date"),
		@Result(property="company_address", column="company_address"),
		@Result(property="company_account", column="company_account"),
		@Result(property="role", column= "role", typeHandler=CompanyRoleTypeHandler.class)
	})
	@Select("select * from company where company_id=#{company}")
	Optional<CompanyDTO> findCompanyDetailById(@Param("company_id")String id);
    
     
	@Select("select * from company where company_reg_num=#{company_reg_num}")
	CompanyDTO findCompanyUserByregNum(@Param("company_reg_num")Integer regNum); //사용자용
    

	@Options(useGeneratedKeys=true, keyProperty="company_idx")
	@Insert("insert into company(company_name, company_id, company_pass, company_email, company_phone, company_reg_num, company_owner, company_open_date, role, company_address, company_account) "
	+"values(company_name=#{company_name}, company_id=#{company_id}, company_pass, company_email, company_phone, company_reg_num, company_owner, company_open_date, role, company_address, company_account)")
	void insertCompany(CompanyJoinDTO joinCompany);
}
