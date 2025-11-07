package com.soldesk.festival.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
		
		return memberMapper.findMemberById(userId);
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
	
	/* 
	@Transactional
	public Optional<MemberDetailDTO> modifyMember(MemberUpdateDTO updateMember){
		
		MemberDTO existingMember = findUserbyId(updateMember.getMember_id()).orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다."));
		
		if(updateMember.getMember_pass() != null && !updateMember.getMember_pass().isBlank()) {
			updateMember.setMember_pass(authUtil.encodedPassword(updateMember.getMember_pass()));
		}else {
			updateMember.setMember_pass(existingMember.getMember_pass());
		}
		
		memberMapper.updateMember(updateMember);

		Optional<MemberDTO> finalMemberDTO = memberMapper.findMemberById(updateMember.getMember_id());
        
		return finalMemberDTO.map(dto -> MemberDetailDTO.builder()
		                                  .member_id(dto.getMember_id())
										  .member_name(dto.getMember_name())
										  .member_email(dto.getMember_email())
										  .role(dto.getRole())
										  .member_nickname(dto.getMember_nickname())
										  .build()
										  );
					  
		} */

    @Transactional(rollbackFor= com.soldesk.festival.exception.UserException.class)
	public void modifyMember(MemberUpdateDTO updateUser){

		MemberDTO existingMember = findUserbyId(updateUser.getMember_id()).orElseThrow(()-> new UserException("아이디나 비밀번호가 일치하지 않습니다."));
        if(updateUser.getMember_pass() != null && !updateUser.getMember_pass().isBlank()){
			updateUser.setMember_pass(authUtil.encodedPassword(updateUser.getMember_pass()));
		}else {
			updateUser.setMember_pass(existingMember.getMember_pass());
		}

		memberMapper.updateMember(updateUser);
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
			if(opMember.isPresent()){
				  MemberDTO member = opMember.get();
                  return MemberDetailDTO.builder()
				                       .member_idx(member.getMember_idx())
									   .member_id(member.getMember_id())
									   .member_name(member.getMember_name())
									   .member_email(member.getMember_email())
									   .member_nickname(member.getMember_nickname())
									   .role(member.getRole())
									   .build();

				}
                
			    throw new UsernameNotFoundException("해당하는 회원정보를 찾을 수 없습니다");
			
		}
		
		public List<MemberDTO> getMemberList(){
			
			return memberMapper.getMemberList();
		}


	public int countMember(){
		return memberMapper.countMember();
	}

	public void adminDeleteMember(int member_idx){
		memberMapper.adminDeleteMember(member_idx);
	}
}
