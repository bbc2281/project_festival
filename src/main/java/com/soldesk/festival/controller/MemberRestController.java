package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.MemberLoginDTO;
import com.soldesk.festival.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberRestController {
	
	private MemberService memberService;

	public MemberRestController(MemberService memberService){
		this.memberService = memberService;
	}
	
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id")String member_id){
	
		boolean exists = memberService.checkMemberIdExists(member_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
    
	@PostMapping
	public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDTO memberLogin){
       return null;
	}


	
	

}
