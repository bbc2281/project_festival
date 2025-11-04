package com.soldesk.festival.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.soldesk.festival.config.MemberRole;

import lombok.Data;

@Data
public class SecurityAllUsersDTO implements UserDetails, OAuth2User{
	
	private final MemberDTO member;
	private Map<String, Object> attributes;
	private final CompanyDTO company;
	
	//일반 회원가입한 회원 생성자
	public SecurityAllUsersDTO(MemberDTO member, CompanyDTO company) {
		this.member = member;
		this.company = company;
		this.attributes = null;
	}

	//OAuth2,소셜 로그인용 생성자
	public SecurityAllUsersDTO(MemberDTO member, CompanyDTO company, Map<String, Object> attributes){
		this.member = member;
		this.company = company;
		this.attributes = attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		
		Collection<GrantedAuthority> authority = new ArrayList<>();
		
	
		//2.CompanyRole 권한 추가(Company객체가 있을경우만 해당)
		if(this.company != null && this.company.getRole() != null){
			String companyRole = company.getRole().getMemberRole();
			authority.add(new SimpleGrantedAuthority(companyRole));
		}else if (this.member != null && this.member.getRole() != null) {
		    String memberRole = member.getRole().getMemberRole();
		    authority.add(new SimpleGrantedAuthority(memberRole));
			
		} 
		return authority;
		
		//return Collections.singletonList(new SimpleGrantedAuthority(this.member.getRole().getMemberRole()));
		
	} //사용자의 권한



	@Override
	public String getPassword() {
		
		if(this.company != null){
			return company.getMember_pass();
		}else if(this.member != null){
			return member.getMember_pass();
		}
		return null;
	}

	@Override
	public String getUsername() {


		if(this.company != null){
			return company.getMember_id();
		}else if(this.member != null){
			return member.getMember_id();
		}
		return null;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return getUsername();
	} 

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    //유저의 idx값을 가져오는 메서드ㄴ
	public long getUserIdx(){
		if(this.member != null){
			return this.member.getMember_idx();
		}else if(this.company != null){
			return this.company.getCompany_idx();
		}
		throw new IllegalStateException("로그인된 사용자의 고유 식별자를 찾을 수 없습니다");
	
	}

	// 닉네임 설정되어있으면 닉네임 가져오고 없으면 이름 가져오기
	public String getUserDisplayName(){   
		if(this.member != null){
			String nickname = this.member.getMember_nickname();

			if(nickname != null && !nickname.trim().isBlank()){
				return nickname;
			}
			return this.member.getMember_name();
		}

		else if(this.company != null){
			return this.company.getCompany_name();
		}

		return getUsername();
	}

	public MemberRole getUserRole(){
		if(this.member != null){
			return this.member.getRole();
		}else if(this.company != null){
			return this.company.getRole();
		}
		return null;
	}

}
