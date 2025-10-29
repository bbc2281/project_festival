package com.soldesk.festival.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.AuthUtil;
import com.soldesk.festival.config.MemberRole;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.MemberDetailDTO;
import com.soldesk.festival.dto.MemberJoinDTO;
import com.soldesk.festival.dto.MemberUpdateDTO;
import com.soldesk.festival.exception.UserException;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper memberMapper;
	private final AuthUtil authUtil;


	public Optional<MemberDTO> findUserbyId(String userId){
		
		return memberMapper.findUserById(userId);
	}

    
	@Transactional(rollbackFor= com.soldesk.festival.exception.UserException.class)
	public void join(MemberJoinDTO joinMember) {
		
		/* 
        if(checkMemberIdExists(joinMember.getMember_id())) {
           throw new UserException("이미 사용중인 아이디 입니다");
        }
		   */
        
        joinMember.setMember_pass(authUtil.encodedPassword(joinMember.getMember_pass()));
		joinMember.setRole(MemberRole.USER);
		joinMember.setMember_point(0); 
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
	public Optional<MemberDetailDTO> modifyMember(MemberUpdateDTO updateMember){
		
		MemberDTO existingMember = findUserbyId(updateMember.getMember_id()).orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다."));
		
		if(updateMember.getMember_pass() != null && !updateMember.getMember_pass().isBlank()) {
			updateMember.setMember_pass(authUtil.encodedPassword(updateMember.getMember_pass()));
		}else {
			updateMember.setMember_pass(existingMember.getMember_pass());
		}
		
		memberMapper.updateMember(updateMember);

		Optional<MemberDTO> finalMemberDTO = memberMapper.findUserById(updateMember.getMember_id());
        
		return finalMemberDTO.map(dto -> MemberDetailDTO.builder()
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
				.orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다"));
		
		//currentMember.setDeleted(true);
		//currentMember.setDeletedAt(LocalDateTime.now());
		
		memberMapper.deleteMember(currentMember);
		
	}


    
		public MemberDetailDTO getMemberDetails(String userId){
            
				Optional<MemberDTO> opMember = memberMapper.findUserByIdforUser(userId);
				MemberDTO member = opMember.get();
                
				return MemberDetailDTO.builder()
				                       .member_idx(member.getMember_idx())
									   .member_pass(member.getMember_pass())
									   .member_id(member.getMember_id())
									   .member_name(member.getMember_name())
									   .member_email(member.getMember_email())
									   .member_nickname(member.getMember_nickname())
									   .role(member.getRole())
									   .build();
			
		}
		


}
