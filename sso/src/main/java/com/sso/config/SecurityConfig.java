package com.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.sso.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired UserService userService;
	@Autowired RestAuthenticationEntryPoint authenticationEntryPoint;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    		.httpBasic().disable() 	// rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
    		.csrf().disable() 		// rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .exceptionHandling()
            	.authenticationEntryPoint(authenticationEntryPoint)
            	.and()
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.NEVER)
            	.and()
            .authorizeRequests() // 리퀘스트에 대한 사용권한 체크
//            	.antMatchers(HttpMethod.POST, "/user/login").permitAll() // 로그인 주소는 누구나 접근가능
            	.antMatchers(HttpMethod.POST, "/api/login").permitAll() // 로그인 주소는 누구나 접근가능
              	// CORS로 인한 OPTIONS에서 거부 당하는 방법 해결책..
            	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/user").hasAuthority("USER")
//                .antMatchers("/admin").hasAuthority("ADMIN")
                .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근
                .and()
              .formLogin().disable()
              .logout()
              	.invalidateHttpSession(true) // 세션 제거
              	.deleteCookies("JSESSIONID") // 쿠키 제거
              ;   
    	
    	;
    }
   
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userService)
              .passwordEncoder(userService.passwordEncoder());
    }
   
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
//    	log.info("[authenticationManagerBean]");
         return super.authenticationManagerBean();
    }
   
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
              return new HeaderHttpSessionStrategy();
    }
}