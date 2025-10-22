package com.soldesk.festival.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;

@Data
public class SecurityAllUsersDTO implements UserDetails, OAuth2User{
	
	private MemberDTO member;
	private Map<String, Object> attributes;
	private CompanyDTO company;
	
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
		
		//1.MemberRole 권한 추가
		String memberRole = member.getRole().getMemberRole();
		authority.add(new SimpleGrantedAuthority(memberRole));
		
		//2.CompanyRole 권한 추가(Company객체가 있을경우만 해당)
		if(this.company != null && this.company.getRole() != null){
			String companyRole = company.getRole().getCompanyRole();
			authority.add(new SimpleGrantedAuthority(companyRole));
		}
		return authority;
		
	}//사용자의 권한



	@Override
	public String getPassword() {
		return member.getMember_pass();
	} //사용자의 비밀번호

	@Override
	public String getUsername() {
		return member.getMember_id();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return member.getMember_id();
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

}
