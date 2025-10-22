package com.soldesk.festival.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import org.springframework.util.AntPathMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception{
        return authConfiguration.getAuthenticationManager();
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
                                        .requestMatchers("/api/v1/member/login", "/api/v1/member/join", "/api/v1/member/checkId","/api/v1/company/join").permitAll()
                                        .requestMatchers("/", "/css/**", "/js/**", "/image/**").permitAll() 
                                        .requestMatchers("/api/v1/admin/**").hasRole(MemberRole.ADMIN.name())
                                        .requestMatchers("/api/v1/company/join").hasAnyRole(CompanyRole.COMPANY.name(), CompanyRole.FESTIVAL_PLANNER.name())
                                        .requestMatchers("/api/v1/member/**").hasAnyRole(MemberRole.USER.name(), MemberRole.ADMIN.name(),
                                                                                        CompanyRole.COMPANY.name(), CompanyRole.FESTIVAL_PLANNER.name())
                                                                                        
                                        .anyRequest().authenticated()
                 
                );
        http
            .formLogin((form)-> form
                                    .loginPage("/member/login")
                                    .loginProcessingUrl("/member/login")
                                    .defaultSuccessUrl("/")
                                    .failureUrl("member/login?error")
                                    .permitAll()
                                    
                      )
            .logout((logout)-> logout
                                     .logoutUrl("/member/logout")
                                     .logoutSuccessUrl("/")
                                     .invalidateHttpSession(true)
                                     .deleteCookies("JSESSIONID")
                                     .permitAll()
                   );          
                                    
        
        
             
        return http.build();        
               

    }


    

}
