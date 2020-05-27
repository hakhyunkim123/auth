package com.sso.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// 인증되지 않은 Request에 대한 문제 해결
@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override 
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) 
			throws IOException, ServletException { 
		
//		log.info("[ERROR] : {}", request.authenticate(response));
//		log.info("[ERROR] : {}", request.);
		log.info("[ERROR] : {}", authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()); 

//		UUID dd = UUID.randomUUID();
	}

}