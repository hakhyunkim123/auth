package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimbusds.jwt.SignedJWT;
import com.test.domain.ReturnTokens;
import com.test.dto.VertifyDto;
import com.test.mapper.RelayMapper;
import com.test.utils.RelayConstants;
import com.test.utils.RelayUtils;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
@RequestMapping("api")
@RestController
public class RelayController {
	@Autowired
	RelayMapper relayMapper;
	
	String message = "";
	
	@PostMapping("/relay/login")
	public ResponseEntity  relayLoginUsingSSO(
			@RequestHeader(value="username") String username,
			@RequestHeader(value="password") String password){

		HttpResponse loginResponse = RelayUtils.callSSOApi(username, password);
		
		if(loginResponse.getStatus() == 200) {
			//  CALL TOKEN API
			// implement!
			
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
		
		else if(loginResponse.getStatus() == 401){
			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
		}
	
		return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@PostMapping("/itsm/relay")
	public ResponseEntity  goSvcFromITSM(
			@RequestHeader(value="code") String code,
			@RequestHeader(value="ip") String ip){
		
		List<String> serviceMapping = relayMapper.readIpListByServiceCode(code);
		
		for(String iptest : serviceMapping) {
			log.info("[IP] {}" , iptest);
		}
		
		// check code
		if(serviceMapping.size() == 0) {
			String errorMessage = "[CODE] " + code + " is not found.";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
		
		// check ip
		if(!serviceMapping.contains(ip.trim())) {
			String errorMessage = "[IP] " + ip + " is not matched." + serviceMapping.contains(ip);
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
		
		// check tokenList
		if(!RelayConstants.accessTokenList.containsKey(code)) {
			
			// make new token
			HttpResponse<ReturnTokens> genJwtResponse =
					RelayUtils.callGenJWT(RelayConstants.genJWTUrl, code, "ITSM", code, ip);
	
			log.info("generate Token finished [{}]", genJwtResponse.getStatus());
			
//			String accessTokenSerialized = genJwtResponse.getBody().getObject().get("accessTokenSerialized").toString();
//			String refreshTokenSerialized = genJwtResponse.getBody().getObject().get("refreshTokenSerialized").toString();
			
			String accessTokenSerialized = genJwtResponse.getBody().getAccessTokenSerialized();
			String refreshTokenSerialized = genJwtResponse.getBody().getRefreshTokenSerialized();
			
			log.info("access : {} / refresh : {}", accessTokenSerialized, refreshTokenSerialized);
			
			SignedJWT accessToken = RelayUtils.parseToken(accessTokenSerialized);
			SignedJWT refreshToken = RelayUtils.parseToken(refreshTokenSerialized);
			
			// add Token in TokenList
			RelayConstants.accessTokenList.put(code, accessToken);
			RelayConstants.refreshTokenList.put(code, refreshToken);
	
			// call SVC API
			HttpResponse<String> response = RelayUtils.callSvcApi(accessTokenSerialized, code);
			
			if(response.getStatus() != 200) {
				String errorMessage = "[relay] call " + code + " API Error!\n"
						+ "code : " + Integer.toString(response.getStatus()) + "\n"
						+ "message : " + response.getBody(); 
				return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
			}
			else {
				message = response.getBody();
			}
		}
		
		// Token is existed
		else {
			SignedJWT token = RelayConstants.accessTokenList.get(code);
			
			// existed token is expired..
			if(RelayUtils.isExpired(token)) {
				// remove existed token
				RelayConstants.accessTokenList.remove(code);
				
				SignedJWT refreshToken = RelayConstants.refreshTokenList.get(code);
				
				// call make new token
				HttpResponse<String> genJwtResponse =
						RelayUtils.callRefreshTokenApi(refreshToken.serialize());
				
				String serializedNewToken = genJwtResponse.getBody();
				// update token
				token = RelayUtils.parseToken(serializedNewToken);
				
				// add new token
				RelayConstants.accessTokenList.put(code, token);
				
			}
			
			String serializedToken = token.serialize();
			
			// call token
			HttpResponse<String> response = RelayUtils.callSvcApi(serializedToken, code);
				
			if(response.getStatus() != 200) {
				String errorMessage = "[relay] call " + code + "API Error!\n"
						+ "code : " + Integer.toString(response.getStatus()) + "\n"
						+ "message : " + response.getBody(); 
				return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
			}
			else {
				message = response.getBody();
			}
		}
	
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@GetMapping("/web/login")
	public ResponseEntity  goSvcFromWeb(
			@RequestHeader(value="username") String username,
			@RequestHeader(value="password") String password){
		
		HttpResponse loginResponse = RelayUtils.callSSOApi(username, password);
		
		if(loginResponse.getStatus() == 200) {
			return new ResponseEntity<>(message, HttpStatus.OK);
		}
		else if(loginResponse.getStatus() == 401) {
			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
		}
		else {
			return new ResponseEntity<>(message, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}
	
	@GetMapping("/dbrefresh")
	public String serviceMapRefresh() {
		RelayConstants.verifyMap.clear();
		List<VertifyDto> serviceMapList = relayMapper.readAllServiceMapping();
		for(VertifyDto vertify : serviceMapList) {
			RelayConstants.verifyMap.put(vertify.getCode(), vertify);
			
			log.info("[{}] - [{}]", vertify.getCode().trim(), vertify.getIp().trim());
		}
		
		return "success";
	}
}
