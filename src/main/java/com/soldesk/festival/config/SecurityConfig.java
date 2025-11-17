package com.soldesk.festival.config;


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
                                                            "/api/v1/auth/joincompany").permitAll()
                                        .requestMatchers("/", "/css/**", "/js/**", "/image/**").permitAll() 
                                         .requestMatchers("/admin/**").hasRole(MemberRole.ADMIN.name())
                                         .requestMatchers("/member/**").hasAnyRole(MemberRole.USER.name())
                                         .requestMatchers("/company/**").hasAnyRole(MemberRole.COMPANY.name())                                       
                                       // .requestMatchers("/**").permitAll() //일단 모든 요청 접근가능하게함 나중에 정리해야댐
                                         .requestMatchers("/api/v1/auth/admin/**").hasRole(MemberRole.ADMIN.name())
                                        // .requestMatchers("/api/v1/auth/member/**").hasAnyRole(MemberRole.USER.name(), MemberRole.ADMIN.name(),
                                        //                                                 CompanyRole.COMPANY.name(), CompanyRole.FESTIVAL_PLANNER.name())
                                                                                  
                                        .anyRequest().authenticated()
                 
                );
        http
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);
            //.formLogin(form->form.disable())
            //.logout(logout->logout.disable());
              /* 
            .formLogin((form)-> form
                                    .loginPage("/auth/loginPage")
                                    .loginProcessingUrl("/auth/loginPage")
                                    .usernameParameter("member_id")
                                    .passwordParameter("member_pass")
                                    .defaultSuccessUrl("/")
                                    .failureUrl("/auth/loginPage?error")
                                    .permitAll()
                                    
                      )*/
          /*  .logout((logout)-> logout
                                     .logoutUrl("/api/v1/auth/logout")
                                     //.logoutSuccessUrl("/")
                                     .logoutSuccessHandler((req, res, auth)-> {
                                        res.setStatus(HttpServletResponse.SC_OK);
                                        res.setContentType("application/json;charset=UTF-8");
                                        res.getWriter().write("{\"success\":true, \"message\":\"로그아웃 성공\"}");
                                     })
                                     .invalidateHttpSession(true)
                                     .deleteCookies("JSESSIONID")
                                     .permitAll()
                   );          
        */
             
        return http.build();        
               

    }


    

}
