//package com.token.controller;
//
//import java.security.KeyPair;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.token.domain.ReturnTokens;
//import com.token.dto.JWKDto;
//import com.token.dto.RSAKeyPair;
//import com.token.dto.RefreshToken;
//import com.token.mapper.PrivateKeyMapper;
//import com.token.mapper.TokenMapper;
//import com.utils.CommConstants;
//import com.utils.CommUtils;
//import com.utils.TokenManager;
//
//import lombok.extern.slf4j.Slf4j;
//import net.minidev.json.JSONObject;
//
//@Slf4j
//@RestController
//@RequestMapping("/api")
//public class TokenController2 {
//	
//	@Autowired TokenMapper tokenMapper;
//	@Autowired PrivateKeyMapper privateKeyMapper;
////	@Autowired BCryptPasswordEncoder passwordEncoder;
//	
//	@GetMapping("/jwks.json")
//	public JSONObject  getJwks(){
//		JWKSet jwks = TokenManager.getJwks();
//		return jwks.toJSONObject(true);
//	}
//	
//	@GetMapping("/genJwt")
//	public ReturnTokens genJwtFromITSM(
//			@RequestHeader(value="issuer") String issuer,
//			@RequestHeader(value="clientType") String clientType,
//			@RequestHeader(value="username") String username,
//			@RequestHeader(value="subject") String subject) throws JOSEException {
//		
//		log.info("Generate JWT : " + issuer + ", " + subject);
//		
//		return TokenManager.genJWT(issuer, subject, clientType, username);
//	}
//	
//	@GetMapping("/savePk")
//	public String savePrivateKey() {
//		
////		KeyPair keyPair = TokenManager.genRSAKeyPair();
////		
////		RSAKeyPair rsaKeyPair = new RSAKeyPair();
////		
////		rsaKeyPair.setKid(CommConstants.TEST_JWK_KID);
////		rsaKeyPair.setPrivateKey(keyPair.getPrivate().toString());
////		rsaKeyPair.setPublicKey(keyPair.getPublic().toString());
//		
////		privateKeyMapper.createPrivateKey(pkDto);
//		
////		log.info("[PRIVATE KEY : {}] {}", rsaKeyPair.getPrivateKey().toString().length(), rsaKeyPair.getPrivateKey().toString());
////		log.info("[PUBLIC KEY : {}] {}", rsaKeyPair.getPublicKey().toString().length(), rsaKeyPair.getPublicKey().toString());
//		
//		JWK jwk = TokenManager.genJWK(CommConstants.TEST_JWK_KID);
//		
//		JWKDto pkDto = new JWKDto();
//		
//		pkDto.setKid(jwk.getKeyID());
//		try {
//			pkDto.setKey(CommUtils.encryptAES256(jwk.toJSONString(), pkDto.getKid()));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		log.info("[PUBLIC KEY : {}] {}", pkDto.getKey().length(), pkDto.getKey());
//		
////		privateKeyMapper.createPrivateKey(pkDto);
//		
//		String keyJsonString = null;
//		try {
//			keyJsonString = CommUtils.decryptAES256(pkDto.getKey(), pkDto.getKid());
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		JWK keyFromJSONString = null;
//		
//		log.info("[DECODED] {}", keyJsonString);
//		try {
//			keyFromJSONString = JWK.parse(keyJsonString);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		log.info("[DECODED] {}", keyFromJSONString.getKeyID());
//		log.info("[DECODED] {}", keyFromJSONString.toJSONString());
//		
//		
//		return "success";
//	}
//	
//	@GetMapping("dbTest")
//	public String dbTest() {
//		RefreshToken token = new RefreshToken();
//		
//		String testUserName = "test";
//		String testServiceCode = "SLC";
//		String testToken = "testResfreshToken";
//		
//		token.setUsername(testUserName);
//		token.setServiceCode(testServiceCode);
//		token.setToken(testToken);
//		tokenMapper.createRefreshToken(token);
//		
//		Map<String, String> params = new HashMap<String, String>();
//		
//		params.put("username", testUserName);
//		params.put("service_code", testServiceCode);
//		
//		RefreshToken readToken = tokenMapper.readRefreshToken(params);
//		
//		log.info("{}/{} TOKEN - [{}]", testUserName, testServiceCode, readToken.getToken());
//		
//		token.setToken("updateToken");
//		
//		tokenMapper.updateRefreshToken(token);
//		
//		readToken = tokenMapper.readRefreshToken(params);
//		
//		log.info("[UPDATE] {}/{} TOKEN - [{}]", testUserName, testServiceCode, readToken.getToken());
//		
//		tokenMapper.deleteRefreshToken(params);
//		
//		log.info("[DELETE COMPLETE]");
//		
//		return "success";
//	}
//}