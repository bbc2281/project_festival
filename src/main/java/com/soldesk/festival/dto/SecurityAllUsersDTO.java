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
		
	}//사용자의 권한



	@Override
	public String getPassword() {
		
		if(this.company != null){
			return company.getCompany_pass();
		}else if(this.member != null){
			return member.getMember_pass();
		}
		return null;
		
	} //사용자의 비밀번호

	@Override
	public String getUsername() {

		if(this.company != null){
			return company.getCompany_id();
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

}
