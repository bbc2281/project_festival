package com.soldesk.festival.dto;

import com.soldesk.festival.config.MemberRole;

import lombok.Getter;

@Getter
public class MemberDTO { 
    //DB 연결용 MemberDTO
	//일반회원 &  관리자
	
	private Integer member_idx; // MySQL에서 멤버추가할때 auto_increment(?) 통해 데이터 저장시 자동 순서 부여(수동숫자부여 X) 
	
	private String member_id;

	private String member_name;

	private String member_pass;
	
	//private String member_pass2;
	
	private String member_nickname;
	
	private String member_email;
	
	private String member_phone;
	

	private String member_address;
	
	private String member_gender;
	private String member_job;

	private String member_birth;
	
	private int member_point;
	private MemberRole role;
	
	//private boolean deleted;
	//private LocalDateTime deletedAt;




}
