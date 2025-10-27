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
                                        .requestMatchers("/auth/login", "/auth/join","/auth/memberjoin" , "/auth/companyjoin",
                                                            "/api/v1/auth/login", "/api/v1/auth/join", "/api/v1/auth/checkId","/api/v1/auth/memberjoin",
                                                            "/api/v1/auth/companyjoin").permitAll()
                                        .requestMatchers("/", "/css/**", "/js/**", "/image/**").permitAll() 
                                        .requestMatchers("/api/v1/auth/admin/**").hasRole(MemberRole.ADMIN.name())
                                        .requestMatchers("/api/v1/auth/member/**").hasAnyRole(MemberRole.USER.name(), MemberRole.ADMIN.name(),
                                                                                        CompanyRole.COMPANY.name(), CompanyRole.FESTIVAL_PLANNER.name())
                                                                                        
                                        .anyRequest().authenticated()
                 
                );
        http
            .formLogin((form)-> form
                                    .loginPage("/auth/login")
                                    .loginProcessingUrl("/auth/login")
                                    .usernameParameter("member_id")
                                    .passwordParameter("member_pass")
                                    .defaultSuccessUrl("/")
                                    .failureUrl("/auth/login?error")
                                    .permitAll()
                                    
                      )
            .logout((logout)-> logout
                                     .logoutUrl("/auth/logout")
                                     .logoutSuccessUrl("/")
                                     .invalidateHttpSession(true)
                                     .deleteCookies("JSESSIONID")
                                     .permitAll()
                   );          
     
             
        return http.build();        
               

    }


    

}
