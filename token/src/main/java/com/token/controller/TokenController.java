package com.token.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.token.domain.ReturnTokens;
import com.token.dto.JWKDto;
import com.token.dto.RefreshToken;
import com.token.mapper.PrivateKeyMapper;
import com.token.mapper.TokenMapper;
import com.token.service.TokenService;
import com.utils.TokenManager;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@RestController
@RequestMapping("/api")
public class TokenController {
	
	@Autowired TokenMapper tokenMapper;
	@Autowired PrivateKeyMapper privateKeyMapper;
	@Autowired TokenService tokenService;
//	@Autowired BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/jwks.json")
	public JSONObject  getJwks(){
		return TokenManager.exposePublicKeySet();
	}
	
	@PostMapping("/genJwt")
	public ReturnTokens genJwtFromITSM(
			@RequestHeader(value="issuer") String issuer,
			@RequestHeader(value="clientType") String clientType,
			@RequestHeader(value="username") String username,
			@RequestHeader(value="subject") String subject) throws JOSEException {
		
		log.info("[REQ] gen token start!");
		
		tokenService.deleteRefershToken(username, subject);

		return tokenService.genToken(issuer, subject, username, clientType);
	}
	
	@PostMapping("/web/genJwt")
	public ReturnTokens genJwtFromWeb(
			@RequestHeader(value="issuer") String issuer,
			@RequestHeader(value="clientType") String clientType,
			@RequestHeader(value="username") String username,
			@RequestHeader(value="password") String password,
			@RequestHeader(value="subject") String subject) throws JOSEException {
		
		HttpResponse loginRes = Unirest.post("http://52.231.52.58:31011/api/relay/login")
			.header("username", username)
			.header("password", password)
			.asEmpty()
		;
		
		if(loginRes.getStatus() == 200) {

			tokenService.deleteRefershToken(username, subject);
			
			return tokenService.genToken(issuer, subject, username, clientType);
		}
		
		else {
			return null;
		}
		
	}
	
	@PostMapping("/makeJwk")
	public String savePrivateKey(@RequestHeader(value="kid") String kid) throws Exception {
		JWKDto jwk = TokenManager.makeJWK(kid);
		privateKeyMapper.createPrivateKey(jwk);
		
		return "success";
	}
	
	@PostMapping("/updateToken")
	public String refreshToken(@RequestHeader(value="refreshToken") String refreshToken) throws Exception {
		return tokenService.updateToken(refreshToken);
	}
	
	@GetMapping("/delete/refreshToken")
	public String deleteRefresToken(
			@RequestHeader(value="username") String username,
			@RequestHeader(value="serviceCode") String serviceCode){
		
		tokenService.deleteRefershToken(username, serviceCode);
		
		return "success";
	}
	
	@GetMapping("/dbTest")
	public String dbTest() {
		RefreshToken token = new RefreshToken();
		
		String testUserName = "test";
		String testServiceCode = "SLC";
		String testToken = "testResfreshToken";
		
		token.setUsername(testUserName);
		token.setServiceCode(testServiceCode);
		token.setToken(testToken);
		tokenMapper.createRefreshToken(token);
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("username", testUserName);
		params.put("service_code", testServiceCode);
		
		RefreshToken readToken = tokenMapper.readRefreshToken(params);
		
		log.info("{}/{} TOKEN - [{}]", testUserName, testServiceCode, readToken.getToken());
		
		token.setToken("updateToken");
		
		tokenMapper.updateRefreshToken(token);
		
		readToken = tokenMapper.readRefreshToken(params);
		
		log.info("[UPDATE] {}/{} TOKEN - [{}]", testUserName, testServiceCode, readToken.getToken());
		
		tokenMapper.deleteRefreshToken(params);
		
		log.info("[DELETE COMPLETE]");
		
		return "success";
	}
}