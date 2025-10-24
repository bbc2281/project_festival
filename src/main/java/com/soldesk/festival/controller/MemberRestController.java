package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.MemberDetailDTO;
import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.MemberLoginDTO;
import com.soldesk.festival.dto.UserResponse;
import com.soldesk.festival.service.AuthService;
import com.soldesk.festival.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberRestController {
	
	private final MemberService memberService;
	private final AuthService authService;
	private final AuthenticationManager authenticationManager;

	@GetMapping("/checkId")
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id")String member_id){
	
		boolean exists = memberService.checkMemberIdExists(member_id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
    
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@Valid @RequestBody MemberLoginDTO memberLogin, HttpSession session){
        
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberLogin.getMember_id(), memberLogin.getMember_pass()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
   
		UserDetails authUser = (UserDetails)authentication.getPrincipal();
        
		//forUsingSessionAttribute
		/* 
		Optional<MemberDTO> opMember = memberService.findUserbyId(authUser.getUsername());
		if(opMember.isPresent()){
			MemberDTO loginMember = opMember.get();
			session.setAttribute("loginMember", loginMember);
		}else {
            System.out.println("세션에 저장된 회원의 정보가 없습니다");
		}
			*/
		
		MemberDetailDTO details = memberService.getMemberDetails(authUser.getUsername());
		UserResponse response = UserResponse.success("로그인 성공", details);
	    

		return ResponseEntity.ok(response);
	}

    @PostMapping("/join")
	public ResponseEntity<UserResponse> join(@Valid @RequestBody MemberJoinDTO memberJoin){
        
		
		memberService.join(memberJoin);
        
		UserResponse response = UserResponse.successMessage("회원가입 성공");

		return ResponseEntity.status(201).body(response);
	}


	
}  
