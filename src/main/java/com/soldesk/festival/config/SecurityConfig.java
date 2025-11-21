package com.soldesk.festival.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.firebase.auth.UserInfo;
import com.soldesk.festival.service.CustomOAuth2Service;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomOAuth2Service customOAuth2Service;
    
    @Autowired
    public void setCustomOAuth2Service(CustomOAuth2Service customOAuth2Service){
        this.customOAuth2Service = customOAuth2Service;
    }


    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
    }
    
     @Bean
     public WebSecurityCustomizer webSecurityCustomizer(){
         return (web-> web.ignoring().requestMatchers("/css/**", "/js/**", "/image/**"));
     }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

      
        
        RequestMatcher apiMatcher = AntPathRequestMatcher.antMatcher("/api/**");
        
                                    

        http
            .csrf((csrf)-> csrf
                               .ignoringRequestMatchers(apiMatcher)
                               
                           
            )
            .authorizeHttpRequests((auth)-> auth
            
                 //로그인 , 회원가입 , 정적리로스
                                        .requestMatchers("/auth/loginPage", "/auth/join","/auth/memberjoin" , "/auth/companyjoin",
                                                            "/api/v1/auth/login", "/api/v1/auth/join", "/api/v1/auth/checkId","/api/v1/auth/memberjoin",
                                                            "/oauth2/**" , "/api/v1/auth/joincompany").permitAll()
                                        .requestMatchers("/", "/css/**", "/js/**", "/image/**").permitAll() 
                                         .requestMatchers("/admin/**").hasRole(MemberRole.ADMIN.name())
                                         .requestMatchers("/member/**").hasAnyRole(MemberRole.USER.name())
                                         .requestMatchers("/company/**").hasAnyRole(MemberRole.COMPANY.name())                                       
                                       // .requestMatchers("/**").permitAll() //일단 모든 요청 접근가능하게함 나중에 정리해야댐
                                         .requestMatchers("/api/v1/auth/admin/**").hasRole(MemberRole.ADMIN.name())
                                                                                  
                                        .anyRequest().authenticated()
                 
                );
        http
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .oauth2Login(oauth2 -> oauth2
                          .userInfoEndpoint(UserInfo -> UserInfo
                                             .userService(customOAuth2Service))
                           .defaultSuccessUrl("/")                  
            );
  
        return http.build();        
               

    }


    

}
