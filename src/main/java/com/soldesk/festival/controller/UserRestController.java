package com.soldesk.festival.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public ResponseEntity<Map<String, Object>> checkId(@RequestParam("member_id") String member_id) {
		Map<String, Object> response = new HashMap<>();

		if (member_id == null || member_id.trim().isBlank()) {
			response.put("error", "아이디를 입력해주세요");
			return ResponseEntity.badRequest().body(response);
		}

		boolean exists = authService.isIdExists(member_id);

		response.put("exists", exists);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginDTO userLogin, HttpServletRequest request,
			HttpServletResponse res) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userLogin.getMember_id(), userLogin.getMember_pass()));

			// SecurityContextHolder.getContext().setAuthentication(authentication);
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);

			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

			memberService.findUserbyId(userLogin.getMember_id())
					.ifPresentOrElse(
							member -> {
								session.setAttribute("loginMember", member);
								System.out.println("loginMember 세션 저장: " + member);
							},
							() -> System.out.println("loginMember 없음"));

			companyService.findCompanyUserById(userLogin.getMember_id())
					.ifPresentOrElse(
							company -> {
								session.setAttribute("companyMember", company);
								System.out.println("companyMember 세션 저장: " + company);
							},
							() -> System.out.println("companyMember 없음"));
			session.setMaxInactiveInterval(30 * 60);

			SecurityAllUsersDTO user = (SecurityAllUsersDTO) authentication.getPrincipal();
			
			SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, res);
			String redirectUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";

			UserResponse response = UserResponse.success("로그인 성공", user);
			response.setRedirectUrl(redirectUrl);
			return ResponseEntity.ok(response);

		} catch (AuthenticationException e) {

			String errorMessage = "아이디 혹은 비밀번호가 올바르지 않습니다";
			UserResponse response = UserResponse.error(errorMessage);

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			// 로그인 페이지(login.html)에 아이디/ 비번찾기 기능 추가해야함

		}

	}

	@PostMapping("/logout")
	public ResponseEntity<UserResponse> logout(HttpServletRequest request, HttpServletResponse res,
			HttpSession session) {

		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, res, SecurityContextHolder.getContext().getAuthentication());
		UserResponse response = UserResponse.successMessage("로그아웃 성공");

		return ResponseEntity.ok(response);

	}

	/*
	 * @PostMapping("/login")
	 * public ResponseEntity<UserResponse> login(
	 * 
	 * @Valid @RequestBody LoginDTO userLogin,
	 * HttpSession session, // 👈 HttpSession 객체 추가
	 * 
	 * @Autowired MemberService memberService) { // 👈 MemberService는 주입 필요 (만약 멤버
	 * 정보를 DB에서 가져와야 한다면)
	 * 
	 * try {
	 * Authentication authentication = authenticationManager.authenticate(
	 * new UsernamePasswordAuthenticationToken(userLogin.getMember_id(),
	 * userLogin.getMember_pass()));
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication);
	 * 
	 * SecurityAllUsersDTO user =
	 * (SecurityAllUsersDTO)authentication.getPrincipal();-
	 * 
	 * String memberId = user.getUsername();
	 * 
	 * Optional<MemberDTO> opMember = memberService.findUserbyId(memberId);
	 * 
	 * if (opMember.isPresent()) {
	 * MemberDTO loginMember = opMember.get();
	 * session.setAttribute("loginMember", loginMember);
	 * 
	 * System.out.println("세션에 저장된 회원 ID: " + loginMember.getMember_id());
	 * 
	 * } else {
	 * System.out.println("세션에 저장할 회원의 DB 정보(MemberDTO)를 찾을 수 없습니다.");
	 * 
	 * }
	 * UserResponse response = UserResponse.success("로그인 성공", user);
	 * 
	 * return ResponseEntity.ok(response);
	 * 
	 * } catch (AuthenticationException e) {
	 * String errorMessage = "아이디 혹은 비밀번호가 올바르지 않습니다";
	 * UserResponse response = UserResponse.error(errorMessage);
	 * 
	 * return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	 * }
	 * }
	 * 
	 * 
	 */

	@PostMapping("/join")
	public ResponseEntity<UserResponse> join(@Valid @RequestBody MemberJoinDTO memberJoin) {

		try {
			memberService.join(memberJoin);
			UserResponse response = UserResponse.successMessage("회원가입 성공");
			return ResponseEntity.status(201).body(response);

		} catch (UserException e) {
			UserResponse response = UserResponse.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			UserResponse response = UserResponse.error("회원가입 중 오류가 발생하였습니다");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/joincompany")
	public ResponseEntity<UserResponse> companyJoinProcess(@Valid @RequestBody CompanyJoinDTO companyJoin) {

		try {
			companyService.join(companyJoin);
			UserResponse response = UserResponse.successMessage("회원가입 성공");
			return ResponseEntity.status(201).body(response);
		} catch (UserException e) {
			UserResponse response = UserResponse.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			e.printStackTrace();
			UserResponse response = UserResponse.error("회원가입 중 오류가 발생하였습니다");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

    @PostMapping("/modify")
    public ResponseEntity<UserResponse> modifyProcess(@RequestBody MemberDTO userInfo, HttpSession session) {
        try {
            memberService.modifyMember(userInfo);
            MemberDTO modifiedMember = memberService.findUserbyIdx(userInfo.getMember_idx());
            session.setAttribute("loginMember", modifiedMember);
            return ResponseEntity.ok(UserResponse.successMessage("회원정보 수정 성공"));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UserResponse.error("회원정보 수정 중 오류가 발생했습니다."));
        }
    }

	@PostMapping("/delete")
	public void deleteProcess(@SessionAttribute("loginMember") MemberDTO loginMember, HttpSession session,
			HttpServletResponse response) throws Exception {
		memberService.deleteMember(loginMember.getMember_idx());
		session.invalidate();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('회원탈퇴 성공');");
		out.println("window.location.href='/';");
		out.println("</script>");
		out.close();
	}

	@GetMapping("/login/naver")
	public ResponseEntity<UserResponse> naverCallback(
			@RequestParam String code,
			@RequestParam String state,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		// 1) 인증 코드로 액세스 토큰 요청
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "6ODyCh148nT7pPRiBkqK");
		params.add("client_secret", "_iRJIB6qT1");
		params.add("code", code);
		params.add("state", state);
		params.add("redirect_uri", "http://localhost:8080/oauth2/authorization/naver/callback");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

		ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
				"https://nid.naver.com/oauth2.0/token", tokenRequest, String.class);

		if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(UserResponse.error("네이버 인증 토큰 발급 실패"));
		}

		// 2) access_token 파싱
		ObjectMapper mapper = new ObjectMapper();
		JsonNode tokenJson = mapper.readTree(tokenResponse.getBody());
		String accessToken = tokenJson.get("access_token").asText();

		// 3) access_token으로 사용자 프로필 조회
		HttpHeaders profileHeaders = new HttpHeaders();
		profileHeaders.setBearerAuth(accessToken);
		HttpEntity<Void> profileRequest = new HttpEntity<>(profileHeaders);

		ResponseEntity<String> profileResponse = restTemplate.exchange(
				"https://openapi.naver.com/v1/nid/me",
				HttpMethod.GET,
				profileRequest,
				String.class);

		if (!profileResponse.getStatusCode().is2xxSuccessful()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(UserResponse.error("네이버 사용자 정보 조회 실패"));
		}

		// 4) 프로필 정보 파싱
		JsonNode profileJson = mapper.readTree(profileResponse.getBody()).get("response");
		String naverId = profileJson.get("id").asText();
		String email = profileJson.has("email") ? profileJson.get("email").asText() : "";
		String nickname = profileJson.has("nickname") ? profileJson.get("nickname").asText() : "";
		String name = profileJson.has("name") ? profileJson.get("name").asText() : "";
		String gender = profileJson.has("gender") ? profileJson.get("gender").asText() : "";
		String mobile = profileJson.has("mobile") ? profileJson.get("mobile").asText() : "";
		String birthday = profileJson.has("birthday") ? profileJson.get("birthday").asText() : "";
		String birthyear = profileJson.has("birthyear") ? profileJson.get("birthyear").asText() : "";
		String birth = birthyear + "-" + birthday;

		// 5) 회원 조회 및 가입 처리
		Optional<MemberDTO> optionalMember = memberService.findUserbyId(naverId);
		MemberDTO member;
		if (optionalMember.isEmpty()) {
			MemberJoinDTO joinDTO = new MemberJoinDTO();
			joinDTO.setMember_id(naverId);
			joinDTO.setMember_email(email);
			joinDTO.setMember_nickname(nickname);
			joinDTO.setMember_name(name);
			joinDTO.setMember_gender(gender);
			joinDTO.setMember_phone(mobile);
			joinDTO.setMember_birth(birth);
			joinDTO.setMember_pass("SOCIAL_LOGIN");
			joinDTO.setMember_address("기타");
			joinDTO.setMember_job("기타");
			joinDTO.setIs_social(1);//소셜회원 = 1 일반회원 = 0

			memberService.join(joinDTO);
			optionalMember = memberService.findUserbyId(naverId);
			member = optionalMember.get();
		} else {
			member = optionalMember.get();
		}

		// 6) Spring Security Authentication 생성 및 등록
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(member.getMember_id(), "SOCIAL_LOGIN", authorities));
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		// 세션 저장
		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
		session.setAttribute("loginMember", member);
		session.setMaxInactiveInterval(30 * 60);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("alert('로그인 성공');");
		out.println("if(window.opener && !window.opener.closed) {");
		out.println("   window.opener.location.href='/';");
		out.println("   window.close();");
		out.println("} else {");
		out.println("   window.location.href='/';");
		out.println("}");
		out.println("</script>");
		out.close();
		return null;
	}

}
