package com.token.service;

import java.security.PrivateKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.token.domain.ReturnTokens;
import com.token.dto.JWKDto;
import com.token.dto.RefreshToken;
import com.token.mapper.PrivateKeyMapper;
import com.token.mapper.TokenMapper;
import com.utils.CommConstants;
import com.utils.CommUtils;
import com.utils.TokenManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {
	@Autowired TokenMapper tokenMapper;
	@Autowired PrivateKeyMapper privateKeyMapper;
	
	public JWK getJWK(String kid) {
		JWKDto jwkDto = privateKeyMapper.readPrivateKey(kid);
		
		String decodedJWK = null;
		try {
			decodedJWK = CommUtils.decryptAES256(jwkDto.getEncryptedKey(), jwkDto.getKid());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		log.info("[DECODED] {}", decodedJWK);
		
		JWK jwk = null;
		
		try {
			jwk = JWK.parse(decodedJWK);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("[DECODED] {}", jwk.getKeyID());
		log.info("[DECODED] {}", jwk.toJSONString());
		
		return jwk;
	}
	
	public void refreshJWKSet() throws Exception {
		List<JWKDto> newJWKList = privateKeyMapper.readAllKeys();
		
		CommConstants.jwkList.clear();
		
		for(JWKDto jwkDto : newJWKList) {
			
			String decodedJWK = CommUtils.decryptAES256(jwkDto.getEncryptedKey(), jwkDto.getKid());
			
			JWK jwk = JWK.parse(decodedJWK);
			
			CommConstants.jwkList.add(jwk);
		}
	}
	
	public ReturnTokens genToken(String issuer, String serviceCode, 
			String username, String clientType) throws JOSEException {
		
		JWK jwk = getJWK(issuer);
		
		if(jwk == null) return null ;
		
		String accessToken = genAccessToken(jwk, issuer, serviceCode, username, clientType);
		String refreshToken = genRefreshToken(jwk, issuer, serviceCode, username, clientType);
		
		saveRefershToken(username, serviceCode, refreshToken);
		
		return new ReturnTokens(accessToken, refreshToken);
	}
	
	public String genAccessToken(JWK jwk, String issuer, String serviceCode, 
			String username, String clientType) throws JOSEException {
		
		// JWK To PrivateKey To signature
		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
		
		// Create RSA Signer
		JWSSigner signer = null;
		signer = new RSASSASigner(privateKey);

		Date expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60);
		// make access token with claim set
		SignedJWT accessToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
				TokenManager.makeClaimSet(issuer, serviceCode, clientType, username, expiredTime));

		// Apply the RSA-256 protection
		accessToken.sign(signer);
		
		// Serialize to compact form, produces something like
		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
		String accessTokenSerialized = accessToken.serialize();
			
		return accessTokenSerialized;
	}
	
	public String genRefreshToken(JWK jwk, String issuer, String serviceCode, 
		String username, String clientType) throws JOSEException {
		// JWK To PrivateKey To signature
		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
		
		// Create RSA Signer
		JWSSigner signer = null;
		signer = new RSASSASigner(privateKey);

		Date expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60);
		// make access token with claim set
		SignedJWT refreshToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
				TokenManager.makeClaimSet(issuer, serviceCode, clientType, username, expiredTime));

		// Apply the RSA-256 protection
		refreshToken.sign(signer);
		
		// Serialize to compact form, produces something like
		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
		String refreshTokenSerialized = refreshToken.serialize();
		
		return refreshTokenSerialized;
	}
	
	public String updateToken(String refreshToken) throws ParseException, JOSEException {
		JWT token = JWTParser.parse(refreshToken);
		
		String issuer = token.getJWTClaimsSet().getIssuer();
		String serviceCode = token.getJWTClaimsSet().getSubject();
		String username = token.getJWTClaimsSet().getClaim("username").toString();
		String clientType = "WEB";
		if(username.equals("ITSM")) clientType = "ITSM"; 
		
		log.info("{} / {} / {} / {}", issuer, serviceCode, username, clientType);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("serviceCode", serviceCode);
		
		RefreshToken refreshTokenInDB = tokenMapper.readRefreshToken(params);
		
		JWT tokenInDB = JWTParser.parse(refreshTokenInDB.getToken());
		
		String issuer2 = tokenInDB.getJWTClaimsSet().getIssuer();
		String serviceCode2 = tokenInDB.getJWTClaimsSet().getSubject();
		String username2 = tokenInDB.getJWTClaimsSet().getClaim("username").toString();
		String clientType2 = "WEB";
		if(username2.equals("ITSM")) clientType2 = "ITSM";
		
		log.info("{} / {} / {} / {}", issuer, serviceCode, username, clientType);
		
		if(refreshTokenInDB == null) return "refresh token doesn't existed.";
		
		if(refreshToken.trim().equals(refreshTokenInDB.getToken().trim()))  {
			
			String newAccessToken = genAccessToken(getJWK(issuer), token.getJWTClaimsSet().getIssuer(), serviceCode, username, clientType);
			
			return newAccessToken;
		}
		else {
			return "Refresh Token not matched";
		}
		
	}
	
	public void saveRefershToken(String username, String serviceCode, String refreshToken) {
//		String encryedToken = CommUtils.encryptAES256(refreshToken, username);
		
		RefreshToken token = new RefreshToken();
		
		token.setUsername(username);
		token.setServiceCode(serviceCode);
		token.setToken(refreshToken);
		
		tokenMapper.createRefreshToken(token);
	}
	
	public void deleteRefershToken(String username, String serviceCode) {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("serviceCode", serviceCode);
		
		int isExisted = tokenMapper.countRefreshToken(params);
		
		log.info("[COUNT] {}", isExisted);
		
		if(isExisted > 0) {
			tokenMapper.deleteRefreshToken(params);
		}
		else {
			log.info("nonono");
			return;
		}
		
	}
}
