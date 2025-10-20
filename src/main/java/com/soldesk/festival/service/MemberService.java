package com.soldesk.festival.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.AuthUtil;
import com.soldesk.festival.config.MemberRole;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.MemberResponseDTO;
import com.soldesk.festival.dto.MemberUpdateDTO;
import com.soldesk.festival.dto.SecurityMemberDTO;
import com.soldesk.festival.exception.MemberException;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper memberMapper;
	private AuthUtil authUtil;


	public MemberService(MemberMapper memberMapper, AuthUtil authUtil) {
		this.memberMapper = memberMapper;
		this.authUtil = authUtil;
		
	}
	
	public UserDetails login(String userId, String userPass) {
		/*
		Optional<Member> optionalMember = findUserbyId(userId);
		Member loginMember = optionalMember.filter(member-> authUtil.checkPassword(userPass,member.getMember_pass()))
				.orElseThrow(()-> new BadCredentialsException("아이디나 비밀번호가 일치하지 않습니다"));
		
		return new SecurityMemberDTO(loginMember); */
		return findUserbyId(userId).filter(member-> authUtil.checkPassword(userPass, member.getMember_pass()))
				.map(SecurityMemberDTO::new)
				.orElseThrow(()-> new MemberException("아이디나 비밀번호가 일치하지 않습니다"));	
	}
	
	public Optional<MemberDTO> findUserbyId(String userId){
		
		return memberMapper.findUserById(userId);
	}
    
	@Transactional
	public void join(MemberJoinDTO joinMember) {
		
        if(checkMemberIdExists(joinMember.getMember_id())) {
        	return;
        }
        
        joinMember.setMember_pass(authUtil.encodedPassword(joinMember.getMember_pass()));
		joinMember.setRole(MemberRole.USER);
		
		memberMapper.insertMember(joinMember);
		
	}
	
	public boolean checkMemberIdExists(String userId) {
		
		if(userId != null && findUserbyId(userId).isPresent()) {
			return true;
		}else {
			return false;
		}
	}
	
	@Transactional
	public Optional<MemberResponseDTO> modifyMember(MemberUpdateDTO updateMember){
		
		MemberDTO existingMember = findUserbyId(updateMember.getMember_id()).orElseThrow(()-> new MemberException("아이디나 비밀번호가 일치하지 않습니다."));
		
		if(updateMember.getMember_pass() != null && !updateMember.getMember_pass().isBlank()) {
			updateMember.setMember_pass(authUtil.encodedPassword(updateMember.getMember_pass()));
		}else {
			updateMember.setMember_pass(existingMember.getMember_pass());
		}
		
		memberMapper.updateMember(updateMember);

		Optional<MemberDTO> finalMemberDTO = memberMapper.findUserById(updateMember.getMember_id());

		return finalMemberDTO.map(dto -> MemberResponseDTO.builder()
		                                  .member_id(dto.getMember_id())
										  .member_name(dto.getMember_name())
										  .member_email(dto.getMember_email())
										  .role(dto.getRole())
										  .member_nickname(dto.getMember_nickname())
										  .build()
										  );
		}
		
	@Transactional
	public void deleteMember(String userId, String password) {
		
		MemberDTO currentMember = findUserbyId(userId).filter(member -> authUtil.checkPassword(password, member.getMember_pass()))
				.orElseThrow(()-> new MemberException("아이디나 비밀번호가 일치하지 않습니다"));
		
		//currentMember.setDeleted(true);
		//currentMember.setDeletedAt(LocalDateTime.now());
		
		memberMapper.deleteMember(currentMember);
		
	}

}
