package com.soldesk.festival.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.CompanyDTO;

@Mapper
public interface CompanyMapper {
	
	@Select("select * from company where company_id=#{company_id}")
	CompanyDTO findCompanyUserById(@Param("company_id")String id); //시스템 내부에서 조회용
	
	@Select("select * from company where company_reg_num=#{company_reg_num}")
	CompanyDTO findCompanyUserByregNum(@Param("company_reg_num")Integer regNum); //사용자용

}
