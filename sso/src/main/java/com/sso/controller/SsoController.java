package com.sso.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sso.domain.AuthenticationRequest;
import com.sso.domain.AuthenticationToken;
import com.sso.dto.User;
import com.sso.service.UserService;

import kong.unirest.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class SsoController {
    
     @Autowired AuthenticationManager authenticationManager;
     @Autowired UserService userService;
     
     @PostMapping(value="/login")
     public ResponseEntity login(
    		@RequestHeader(value="username") String username,
 			@RequestHeader(value="password") String password) {
    	 
    	 System.out.println("!!!!!!");
         
          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
          
          log.info("[API] Authenticate UsernamePassword Auth Token!");
          Authentication authentication = authenticationManager.authenticate(token);
          
//          SecurityContextHolder.getContext().setAuthentication(authentication);
//          
//          session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
//                    SecurityContextHolder.getContext());
         
          if(authentication.isAuthenticated()) return new ResponseEntity<>("login success", HttpStatus.OK); 
          else {
        	 
        	  return new ResponseEntity<>("auth failed", HttpStatus.UNAUTHORIZED);
          }
     }
    
     @RequestMapping(value="/login/code", method=RequestMethod.POST)
     public AuthenticationToken loginWithCode(
               @RequestBody AuthenticationRequest authenticationRequest,
               HttpSession session
               ) {
          String username = authenticationRequest.getUsername();
          String password = authenticationRequest.getPassword();
          
          log.info("{}/{}", username, password);
         
          log.info("[API] get UsernamePassword Auth Token!");
          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
          
          log.info("[API] Authenticate UsernamePassword Auth Token!");
          Authentication authentication = authenticationManager.authenticate(token);
          
          SecurityContextHolder.getContext().setAuthentication(authentication);
          
          session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
         
          log.info("[API] Read user!");
          User user = userService.readUser(username);
          return new AuthenticationToken(user.getName(), user.getAuthorities(), session.getId());
     }
}