package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberRestController {
	
	private MemberService memberService;
	
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id")String member_id){
	
		boolean exists = memberService.checkMemberIdExists(member_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
	
	

}
