package com.soldesk.festival.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberJoinDTO;

@Mapper
public interface MemberMapper {
	
	@Select("select * from member where member_id=#{member_id}")
	MemberDTO findUserById(@Param("member_id")String userId);
	
	@Select("select * from member where member_email=#{member_email")
	MemberDTO selectUserByEmail(@Param("member_email")String userEmail);
	
	@Select("select * from member")
	List<MemberDTO> getMemberList();
	
	@Insert("insert into member(member_id, member_name, member_pass, member_nickname, member_email, member_phone, member_address,"
			+ "member_gender, member_job, member_age, role) values(#{member_id). #{member_name}, #{member_pass}, #{member_nickname}, #{member_email},"
			+ "#{member_email}, #{member_phone}, #{member_address}, #{member_gender}, #{member_job}, #{member_age}, #{role})")
	void insertMember(MemberJoinDTO joinMember);
	
	@Update("update ")
	MemberDTO updateMember(MemberDTO updateMember); //아이디 제외
	
	@Update("update ")
	MemberDTO updateMemberDeletion(MemberDTO deleteMember);
	
	
	
	


}
