package com.soldesk.festival.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.soldesk.festival.dto.MemberDTO;
import com.soldesk.festival.dto.SocialAttributes;
import com.soldesk.festival.mapper.CompanyMapper;
import com.soldesk.festival.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2Service extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;
    private final CompanyMapper companyMapper;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration()
                                     .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oauth2User.getAttributes();

        SocialAttributes socialAttributes = SocialAttributes.of(registrationId,attributes);
        
        String memberId = registrationId + "_" + socialAttributes.getId();
        
        Optional<MemberDTO> opMember = memberService.getAllDetails(memberId);
        
        

        return super.loadUser(userRequest);
    }


     

}
