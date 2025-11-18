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
import org.apache.ibatis.annotations.Update;

import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.handler.MemberRoleTypeHandler;

@Mapper
public interface MemberMapper {
	
	@Select("select * from member where member_id=#{member_id}")
	@Results({
		@Result(property="member_idx", column="member_idx"),
		@Result(property="member_id", column="member_id"),
		@Result(property="member_name", column="member_name"),
		@Result(property="member_pass", column="member_pass"),
		@Result(property="member_nickname", column="member_nickname"),
		@Result(property="member_email", column="member_email"),
		@Result(property="member_phone", column="member_phone"),
		@Result(property="member_address", column="member_address"),
		@Result(property="member_gender", column="member_gender"),
		@Result(property="member_job", column="member_job"),
		@Result(property="member_birth", column="member_birth"),
		@Result(property="member_point", column="member_point"),
		@Result(property="role", column="role", typeHandler=MemberRoleTypeHandler.class),
		@Result(property="is_social", column="is_social")
	})
	Optional<MemberDTO> findUserDetailAllById(@Param("member_id")String userId);


	
	@Select("select * from member where member_id=#{member_id}")
	@Results({
		@Result(property="member_idx", column="member_idx"),
		@Result(property="member_id", column="member_id"),
		@Result(property="member_name", column="member_name"),
		@Result(property="role", column="role", typeHandler=MemberRoleTypeHandler.class),
		@Result(property="is_social", column="is_social")
	})
	Optional<MemberDTO> findMemberById(@Param("member_id")String userId);

    @Select("select count(*) from member where member_id=#{member_id}")
    int checkIdExist(@Param("member_id")String member_id);

	@Select("select * from member where member_id=#{member_id}")
	@Results({
		@Result(property="member_idx", column="member_idx"),
		@Result(property="member_id", column="member_id"),
		@Result(property="member_name", column="member_name"),
		@Result(property="member_pass", column="member_pass"),
		@Result(property="member_nickname", column="member_nickname"),
		@Result(property="member_email", column="member_email"),
		@Result(property="member_phone", column="member_phone"),
		@Result(property="member_address", column="member_address"),
		@Result(property="member_gender", column="member_gender"),
		@Result(property="member_job", column="member_job"),
		@Result(property="member_birth", column="member_birth"),
		@Result(property="member_point", column="member_point"),
		@Result(property="role", column="role", typeHandler=MemberRoleTypeHandler.class),
		@Result(property="is_social", column="is_social")
	})
	Optional<MemberDTO> findUserByIdforUser(@Param("member_id")String userId);
	

	@Select("select * from member where member_email=#{member_email}")
	@Results({
		@Result(property="member_email", column="member_email"),
		@Result(property="role", column="role", typeHandler=MemberRoleTypeHandler.class)
	})
	Optional<MemberDTO> selectUserByEmail(@Param("member_email")String userEmail);
	
    
	@Select("select * from member where role = 'USER' order by member_idx desc")
	List<MemberDTO> getMemberList();

	@Options(useGeneratedKeys=true, keyProperty="member_idx")
	@Insert("insert into member(member_id, member_name, member_pass, member_nickname, member_email, member_phone, member_address,"
            + "member_gender, member_job, member_birth, role, member_point, is_social) values(#{member_id}, #{member_name}, #{member_pass}, #{member_nickname}, #{member_email},"
            + " #{member_phone}, #{member_address}, #{member_gender}, #{member_job}, #{member_birth}, #{role}, #{member_point}, #{is_social})")		
	void insertMember(MemberJoinDTO joinMember);
	
	@Update("update member set member_name=#{member_name}, member_pass=#{member_pass}, member_nickname=#{member_nickname}, member_email=#{member_email}, member_phone=#{member_phone}, member_address=#{member_address}, member_gender=#{member_gender}, member_job=#{member_job} where member_id=#{member_id}")
	void updateMember(MemberDTO updateMember); //아이디 제외

	@Update("update member set member_name=#{member_name}, member_nickname=#{member_nickname}, member_email=#{member_email}, member_phone=#{member_phone}, member_address=#{member_address}, member_gender=#{member_gender}, member_job=#{member_job} where member_id=#{member_id}")
	void updateMemberNotPass(MemberDTO updateMember);
	
	@Delete("delete from member where member_idx=#{member_idx}")
	void deleteMember(@Param("member_idx") int member_idx);
	
	@Select("select count(member_idx) from member")
	int countMember();

	@Select("select count(*) from member where member_id like concat('%', #{keyword}, '%') or member_name like concat('%', #{keyword}, '%') or member_email like concat('%', #{keyword}, '%')")
	int countMemberByKeyword(@Param("keyword") String keyword);

	@Select("select * from member where member_idx=#{member_idx}")
	MemberDTO findUserbyIdx(@Param("member_idx") int member_idx);

	@Select("select * from member order by member_idx desc limit #{limit} offset #{offset}")
	List<MemberDTO> selectMemberPaged(@Param("offset") int offset, @Param("limit") int limit);

	@Select("select * from member where member_id like concat('%', #{keyword}, '%') or member_name like concat('%', #{keyword}, '%') or member_email like concat('%', #{keyword}, '%') order by member_idx desc limit #{limit} offset #{offset}")
	List<MemberDTO> selectMemberPagedByKewWord(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
}
