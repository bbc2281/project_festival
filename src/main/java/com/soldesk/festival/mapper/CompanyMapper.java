package com.soldesk.festival.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.soldesk.festival.dto.CompanyDTO;
import com.soldesk.festival.dto.CompanyJoinDTO;
import com.soldesk.festival.handler.MemberRoleTypeHandler;

@Mapper
public interface CompanyMapper {

	
	@Results({
		@Result(property="member_id", column="member_id"),
		@Result(property="company_name", column="company_name"),
		@Result(property="role", column= "role", typeHandler=MemberRoleTypeHandler.class)
	})
	@Select("select * from company where member_id=#{member_id}")
	Optional<CompanyDTO> findCompanyUserById(@Param("member_id")String member_id); //시스템 내부에서 조회용
    
	@Select("select count(*) from company where member_id=#{member_id}")
	int checkIdExist(@Param("member_id")String member_id);
   
	@Results({
		@Result(property="company_idx", column="company_idx"),
		@Result(property="company_name", column="company_name"),
		@Result(property="member_id", column="member_id"),
		@Result(property="member_pass", column="member_pass"),
		@Result(property="member_email", column="member_email"),
		@Result(property="company_phone", column="company_phone"),
		@Result(property="company_reg_num", column="company_reg_num"),
		@Result(property="company_owner", column="company_owner"),
		@Result(property="company_open_date", column="company_open_date"),
		@Result(property="company_address", column="company_address"),
		@Result(property="company_account", column="company_account"),
		@Result(property="role", column= "role", typeHandler=MemberRoleTypeHandler.class)
	})
	@Select("select * from company where member_id=#{member_id}")
	Optional<CompanyDTO> findCompanyDetailAllById(@Param("member_id")String id);


	@Select("select * from company where company_reg_num=#{company_reg_num}")
	CompanyDTO findCompanyUserByregNum(@Param("company_reg_num")Integer regNum); //사용자용
    
	@Select("select * from company where company_idx = #{company_idx}")
	CompanyDTO selectCompanyByIdx(@Param("company_idx") int id);

	@Options(useGeneratedKeys=true, keyProperty="company_idx")
	@Insert("insert into company(company_name, member_id, member_pass, member_email, company_phone, company_reg_num, company_owner, company_open_date, role, company_address, company_account) "
	+"values(#{company_name}, #{member_id}, #{member_pass}, #{member_email}, #{company_phone}, #{company_reg_num}, #{company_owner}, #{company_open_date}, #{role}, #{company_address}, #{company_account})")
	void insertCompany(CompanyJoinDTO joinCompany);

	@Select("select * from company")
	List<CompanyDTO> getAllCompanys();

	@Delete("delete from company where company_idx = #{company_idx}")
	void deleteCompanyByAdmin(@Param("company_idx") int id);

}
