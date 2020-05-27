package com.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.token.domain.ReturnTokens;

import com.token.dto.JWKDto;
import com.token.dto.RefreshToken;
import com.token.mapper.PrivateKeyMapper;
import com.token.mapper.TokenMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;


@Slf4j
public class TokenManager {
	
	@Autowired 
	static TokenMapper tokenMapper;
	
//	public static ReturnTokens genToken(String issuer, String serviceCode, 
//			String username, String clientType) throws JOSEException {
//		
//		JWK jwk = getJWK(serviceCode);
//		
//		if(jwk == null) return null ;
//		
//		String accessToken = genAccessToken(jwk, issuer, serviceCode, username, clientType);
//		String refreshToken = genRefreshToken(jwk, issuer, serviceCode, username, clientType);
//		
//		saveRefershToken(username, serviceCode, refreshToken);
//		
//		return new ReturnTokens(accessToken, refreshToken);
//	}
	
//	public static String genAccessToken(JWK jwk, String issuer, String serviceCode, 
//			String username, String clientType) throws JOSEException {
//		
//		// JWK To PrivateKey To signature
//		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
//		
//		// Create RSA Signer
//		JWSSigner signer = null;
//		signer = new RSASSASigner(privateKey);
//
//		Date expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60);
//		// make access token with claim set
//		SignedJWT accessToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
//				makeClaimSet(issuer, serviceCode, clientType, username, expiredTime));
//
//		// Apply the RSA-256 protection
//		accessToken.sign(signer);
//		
//		// Serialize to compact form, produces something like
//		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
//		String accessTokenSerialized = accessToken.serialize();
//			
//		return accessTokenSerialized;
//	}
//	
//	public static String genRefreshToken(JWK jwk, String issuer, String serviceCode, 
//		String username, String clientType) throws JOSEException {
//		// JWK To PrivateKey To signature
//		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
//		
//		// Create RSA Signer
//		JWSSigner signer = null;
//		signer = new RSASSASigner(privateKey);
//
//		Date expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60);
//		// make access token with claim set
//		SignedJWT refreshToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
//				makeClaimSet(issuer, serviceCode, clientType, username, expiredTime));
//
//		// Apply the RSA-256 protection
//		refreshToken.sign(signer);
//		
//		// Serialize to compact form, produces something like
//		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
//		String refreshTokenSerialized = refreshToken.serialize();
//		
//		return refreshTokenSerialized;
//	}
	
//	public static String updateToken(String refreshToken) throws ParseException {
//		JWT token = JWTParser.parse(refreshToken);
//		
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("username", token.getJWTClaimsSet().getClaim("username").toString());
//		params.put("serviceCode", token.getJWTClaimsSet().getSubject());
//		
//		RefreshToken refreshTokenInDB = tokenMapper.readRefreshToken(params);
//		
//		if(refreshTokenInDB == null) return "refresh token doesn't existed.";
//
//		String result
//		= refreshToken.equals(refreshTokenInDB.getToken()) ? "Success" : "Refresh Token not matched";
//		
//		return result;
//		
//	}
	 
	public static JSONObject exposePublicKeySet() {
		JWKSet jwks = new JWKSet(CommConstants.jwkList);
		return jwks.toJSONObject(true);
	}
	 
	public static JWKDto makeJWK(String kid) throws Exception {
		
		KeyPairGenerator gen = null;
		try {
			gen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		gen.initialize(2048);
		KeyPair keyPair = gen.generateKeyPair();

		// Convert to JWK format
		JWK jwk = new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
		    .privateKey((RSAPrivateKey)keyPair.getPrivate())
		    .keyUse(KeyUse.SIGNATURE)
		    .keyID(kid)
		    .build();
		
		JWKDto pkDto = new JWKDto();
		
		pkDto.setKid(jwk.getKeyID());
//		pkDto.setKey("asdasd");
		pkDto.setEncryptedKey(CommUtils.encryptAES256(jwk.toJSONString(), pkDto.getKid()));
		
		log.info("[PUBLIC KEY : {}] {}", pkDto.getEncryptedKey().length(), pkDto.getEncryptedKey());
		
		return pkDto;
	}
	
//	public static JWK getJWK(String kid) {
//		JWKDto jwkDto = privateKeyMapper.readPrivateKey(kid);
//		
//		String decodedJWK = null;
//		try {
//			decodedJWK = CommUtils.decryptAES256(jwkDto.getKey(), jwkDto.getKid());
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		log.info("[DECODED] {}", decodedJWK);
//		
//		JWK jwk = null;
//		
//		try {
//			jwk = JWK.parse(decodedJWK);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		log.info("[DECODED] {}", jwk.getKeyID());
//		log.info("[DECODED] {}", jwk.toJSONString());
//		
//		return jwk;
//	}
	
	public static boolean checkJWKExisted(String kid) {
		boolean isExisted = false;
		
		for(JWK jwk : CommConstants.jwkList) {
			if(jwk.getKeyID().trim().equals(kid.trim())) return true;
		}
		
//		return getJWK(kid) != null;
//		return new JWKSet(CommConstants.jwkList).getKeyByKeyId(kid) != null;
		
		return isExisted;
		
	}
	
//	public void refreshJWKSet() throws Exception {
//		List<JWKDto> newJWKList = privateKeyMapper.readAllKeys();
//		
//		CommConstants.jwkList.clear();
//		
//		for(JWKDto jwkDto : newJWKList) {
//			
//			String decodedJWK = CommUtils.decryptAES256(jwkDto.getKey(), jwkDto.getKid());
//			
//			JWK jwk = JWK.parse(decodedJWK);
//			
//			CommConstants.jwkList.add(jwk);
//		}
//	}
	
	public static JWTClaimsSet makeClaimSet(String issuer, String subject, 
		String clientType, String username, Date expiredTime) {
		
		if(clientType.equals("ITSM")) {
			return new JWTClaimsSet.Builder()
				.claim("username", username)
				.issuer(issuer)
				.audience(username)
				.subject(subject)
				.expirationTime(expiredTime)
				.build();
		}
		else {
			return new JWTClaimsSet.Builder()
				.claim("username", username)
				.issuer(issuer)
				.subject(subject)
				.expirationTime(expiredTime)
				.build();
		}
	}
	
//	public static void saveRefershToken(String username, String serviceCode, String refreshToken) {
////		String encryedToken = CommUtils.encryptAES256(refreshToken, username);
//		
//		RefreshToken token = new RefreshToken();
//		
//		token.setUsername(username);
//		token.setServiceCode(serviceCode);
//		token.setToken(refreshToken);
//		
//		tokenMapper.createRefreshToken(token);
//	}
//	
//	public static void deleteRefershToken(String username, String serviceName) {
//		
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("username", username);
//		params.put("serviceName", serviceName);
//		tokenMapper.deleteRefreshToken(params);
//		
//	}
}
