package com.soldesk.festival.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.CompanyDTO;
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
	
	@Select("select * from company where company_reg_num=#{company_reg_num}")
	CompanyDTO findCompanyUserByregNum(@Param("company_reg_num")Integer regNum); //사용자용

}
