package com.soldesk.festival.dto;
 
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDTO {

    @NotBlank
	private String member_name;
    
    @NotBlank
	private String member_pass;
	
    @NotBlank
	private String member_pass2;
	
	private String member_nickname;
	
    @Email
	private String member_email;
	
    @NotBlank
	private String member_phone;
	
    @NotBlank
	private String member_address;
	
	private String member_gender;

	private String member_job;

    @NotBlank
	private Date member_birth;


}
