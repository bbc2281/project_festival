package com.soldesk.festival.dto;

import java.util.Date;

import com.soldesk.festival.config.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO { 
    //DB 연결용 MemberDTO
	//일반회원 &  관리자
	
	private Integer member_idx;
	
	private String member_id;

	private String member_name;

	private String member_pass;
	
	private String member_pass2;
	
	private String member_nickname;
	
	private String member_email;
	
	private String member_phone;
	

	private String member_address;
	
	private String member_gender;
	private String member_job;

	private Date member_birth;
	
	private Integer member_point;
	private MemberRole role;
	
	//private boolean deleted;
	//private LocalDateTime deletedAt;




}
