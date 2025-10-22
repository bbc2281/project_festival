package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.MemberLoginDTO;
import com.soldesk.festival.service.AuthService;
import com.soldesk.festival.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberRestController {
	
	private final MemberService memberService;
	private final AuthService authService;

	@GetMapping("/checkId")
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id")String member_id){
	
		boolean exists = memberService.checkMemberIdExists(member_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
    
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDTO memberLogin){
      
		UserDetails authUser = authService.login(memberLogin.getMember_id(), memberLogin.getMember_pass());

		Map<String,Object> response = new HashMap<>();
		response.put("message", "로그인 성동");
		response.put("member_id", authUser.getUsername());

		return ResponseEntity.ok(response);
	}

    @PostMapping("/join")
	public ResponseEntity<?> join(@Valid @RequestBody MemberJoinDTO memberJoin){
        
		checkId(memberJoin.getMember_id());
		memberService.join(memberJoin);

		Map<String,Object> response = new HashMap<>();
		response.put("message", "회원가입 상공");

		return ResponseEntity.status(201).body(response);
	}

	
}  
