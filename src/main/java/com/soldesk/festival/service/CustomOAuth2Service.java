package com.soldesk.festival.service;

import java.lang.reflect.Member;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soldesk.festival.config.MemberRole;
import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.SecurityAllUsersDTO;
import com.soldesk.festival.mapper.CompanyMapper;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        OAuth2User oauth2 = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oauth2.getAttributes();

        String member_id;
        String member_name;
        String member_email;
      

       // String userNameAttribute = userRequest.getClientRegistration()
        //                             .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //String key = MemberRole.fromString(userNameAttribute).getMemberRole();                          

        if("naver".equals(registrationId)){
            Map<String,Object> response = (Map<String,Object>)attributes.get("response");
            member_id = registrationId + "_" + response.get("id");
            member_email = (String)response.get("email");
            member_name = (String)response.get("name");
        }else if("kakao".equals(registrationId)){
            member_id = registrationId + "_" + attributes.get("id");
            Map<String, Object> kakaoAccount = (Map<String,Object>)attributes.get("kakao_account");

            Map<String, Object> profile = (Map<String,Object>)kakaoAccount.get("profile");

            member_email = (String)kakaoAccount.get("email");
            member_name = (String)kakaoAccount.get("name");
        }else{
            member_id = registrationId + "_" + attributes.get("sub");
            member_email = (String)attributes.get("email");
            member_name = (String)attributes.get("name");
        }

        Optional<MemberDTO> opMember = memberMapper.findMemberById(member_id);
        MemberDTO member;

        if(opMember.isPresent()){
            member = opMember.get();
        }else {
            member = saveNewOauth2User(member_id, member_email, member_name);
        }
        
         return new SecurityAllUsersDTO(member, null, attributes);
        
    }

    private MemberDTO saveNewOauth2User(String member_id, String member_email, String member_name){
        log.info("새로운 소셜 로그인 사용자 등록: {}", member_id);

        MemberDTO newMember = MemberDTO.builder()
                              .member_id(member_id)
                              .member_email(member_email)
                              .member_name(member_name)
                              .role(MemberRole.USER)
                              .member_pass(null)
                              .member_nickname("Social_Login")
                              .member_address("Social_Login")
                              .member_gender("O")
                              .member_job("Social_Login")
                              .member_phone("000-0000-0000")
                              .member_birth("0000-00-00")
                              .member_point(0)
                              .build();
           memberMapper.saveOAuth2(newMember);
           
           return newMember;

    }

}
