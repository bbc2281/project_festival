package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.MemberLoginDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.dto.UserResponse;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.service.AuthService;
import com.soldesk.festival.service.MemberService;

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
	    Map<String, Object> response = new HashMap<>();

		if(member_id == null || member_id.trim().isBlank()){
			response.put("error", "아이디를 입력해주세요");
			return ResponseEntity.badRequest().body(response);
		}
		
		boolean exists = memberService.checkMemberIdExists(member_id);
		
		
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
    
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@Valid @RequestBody MemberLoginDTO memberLogin){

		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberLogin.getMember_id(), memberLogin.getMember_pass()));

             SecurityContextHolder.getContext().setAuthentication(authentication);
			 
			 SecurityAllUsersDTO user = (SecurityAllUsersDTO)authentication.getPrincipal();

		    UserResponse response = UserResponse.success("로그인 성공", user);

		    return ResponseEntity.ok(response);

		} catch (AuthenticationException e) {

			String errorMessage = "아이디 혹은 비밀번호가 올바르지 않습니다";
			UserResponse response = UserResponse.error(errorMessage);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);


		}
        
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

		
	}

    @PostMapping("/join")
	public ResponseEntity<UserResponse> join(@Valid @RequestBody MemberJoinDTO memberJoin){
        
		try {
			memberService.join(memberJoin);
			UserResponse response = UserResponse.successMessage("회원가입 성공");
			return ResponseEntity.status(201).body(response);
			
		} catch (UserException e) {
			UserResponse response = UserResponse.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		
		} catch (Exception e){
			e.printStackTrace();
			UserResponse response = UserResponse.error("회원가입 중 오류가 발생하였습니다");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


	
}  
