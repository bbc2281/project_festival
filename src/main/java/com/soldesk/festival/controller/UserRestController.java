package com.soldesk.festival.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soldesk.festival.dto.CompanyJoinDTO;
import com.soldesk.festival.dto.LoginDTO;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.dto.UserResponse;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.service.AuthService;
import com.soldesk.festival.service.CompanyService;
import com.soldesk.festival.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserRestController {
	
	private final MemberService memberService;
	private final AuthService authService;
	private final CompanyService companyService;
	private final AuthenticationManager authenticationManager;

	@GetMapping("/checkId")
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id")String member_id){
	    Map<String, Object> response = new HashMap<>();

		if(member_id == null || member_id.trim().isBlank()){
			response.put("error", "아이디를 입력해주세요");
			return ResponseEntity.badRequest().body(response);
		}
		
		boolean exists = authService.isIdExists(member_id);
		
		response.put("exists", exists);
		
		return ResponseEntity.ok(response);
	}
    
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginDTO userLogin, HttpServletRequest request, HttpServletResponse res){

		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getMember_id(), userLogin.getMember_pass()));

             //SecurityContextHolder.getContext().setAuthentication(authentication);
	         SecurityContext securityContext = SecurityContextHolder.getContext();
			 securityContext.setAuthentication(authentication);		 

			 HttpSession session = request.getSession(true);
			 session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
			 
			 memberService.findUserbyId(userLogin.getMember_id())
    			.ifPresent(member -> session.setAttribute("loginMember", member));
				session.setMaxInactiveInterval(30*60);

			 SecurityAllUsersDTO user = (SecurityAllUsersDTO)authentication.getPrincipal();

		    UserResponse response = UserResponse.success("로그인 성공", user);

		    return ResponseEntity.ok(response);

		} catch (AuthenticationException e) {

			String errorMessage = "아이디 혹은 비밀번호가 올바르지 않습니다";
			UserResponse response = UserResponse.error(errorMessage);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
           //로그인 페이지(login.html)에 아이디/ 비번찾기 기능 추가해야함

		}
			
	}

    @PostMapping("/logout")
	public ResponseEntity<UserResponse> logout(HttpServletRequest request, HttpServletResponse res){
		 
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		 logoutHandler.logout(request, res, SecurityContextHolder.getContext().getAuthentication());
		 UserResponse response = UserResponse.successMessage("로그아웃 성공");

		 return ResponseEntity.ok(response);

	}


	/* 
	 @PostMapping("/login")
	 public ResponseEntity<UserResponse> login(
			@Valid @RequestBody LoginDTO userLogin, 
			HttpSession session, // 👈 HttpSession 객체 추가
			@Autowired MemberService memberService) { // 👈 MemberService는 주입 필요 (만약 멤버 정보를 DB에서 가져와야 한다면)

		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLogin.getMember_id(), userLogin.getMember_pass()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			SecurityAllUsersDTO user = (SecurityAllUsersDTO)authentication.getPrincipal();-
		
			String memberId = user.getUsername();
			
			Optional<MemberDTO> opMember = memberService.findUserbyId(memberId);
			
			if (opMember.isPresent()) {
				MemberDTO loginMember = opMember.get();
				session.setAttribute("loginMember", loginMember);
				
				System.out.println("세션에 저장된 회원 ID: " + loginMember.getMember_id());
				
			} else {
				System.out.println("세션에 저장할 회원의 DB 정보(MemberDTO)를 찾을 수 없습니다.");
				
			}
			UserResponse response = UserResponse.success("로그인 성공", user);

			return ResponseEntity.ok(response);

		} catch (AuthenticationException e) {
			String errorMessage = "아이디 혹은 비밀번호가 올바르지 않습니다";
			UserResponse response = UserResponse.error(errorMessage);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
   }
	  
	  
	 */

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
    
	@PostMapping("/joincompany")
	public ResponseEntity<UserResponse> companyJoinProcess(@Valid @RequestBody CompanyJoinDTO companyJoin){

		try {
			companyService.join(companyJoin);
			UserResponse response = UserResponse.successMessage("회원가입 성공");
			return ResponseEntity.status(201).body(response);
		} catch (UserException e) {
			UserResponse response = UserResponse.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}catch (Exception e){
			e.printStackTrace();
			UserResponse response = UserResponse.error("회원가입 중 오류가 발생하였습니다");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	
}  
