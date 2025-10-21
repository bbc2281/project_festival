package com.soldesk.festival.dto;
 
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberUpdateDTO {

	private Integer member_idx;

	private String member_id;
	

    @NotBlank
	@Setter
	private String member_name;
    
    @NotBlank
	@Setter
	private String member_pass;
	
    @NotBlank
	@Setter
	private String member_pass2;
	
	@Setter
	private String member_nickname;
	
    @Email
	@Setter
	private String member_email;
	
    @NotBlank
	@Setter
	private String member_phone;
	
    @NotBlank
	@Setter
	private String member_address;
	
	@Setter
	private String member_gender;
    
	@Setter
	private String member_job;
  
    @NotBlank
	@Setter
	private Date member_birth;


}
