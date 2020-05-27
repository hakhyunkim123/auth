package com.utils;
//package com.utils;
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSSigner;
//import com.nimbusds.jose.crypto.RSASSASigner;
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.KeyUse;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import com.token.domain.ReturnTokens;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class TokenManager {
////private static List<JWK> jwkList = new ArrayList<JWK>();
//	
//	// Make Json Web Token Key
//	public static String makeJWKS(String kid) {
//		KeyPairGenerator gen = null;
//		try {
//			gen = KeyPairGenerator.getInstance("RSA");
//		} catch (NoSuchAlgorithmException e3) {
//			// TODO Auto-generated catch block
//			e3.printStackTrace();
//		}
//		
//		if(gen == null) return "KeyPairGenerator Error";
//		
//		gen.initialize(2048);
//		KeyPair keyPair = gen.generateKeyPair();
//
//		// Convert to JWK format
//		JWK jwk = new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
//		    .privateKey((RSAPrivateKey)keyPair.getPrivate())
//		    .keyUse(KeyUse.SIGNATURE)
//		    .keyID(kid)
//		    .build();
//		
//		CommConstants.jwkList.add(jwk);
//		
//		return "success";
//	}
//	
//	public static JWK genJWK(String kid) {
//		
//		// Generate RSA Key
//		KeyPairGenerator gen = null;
//		try {
//			gen = KeyPairGenerator.getInstance("RSA");
//		} catch (NoSuchAlgorithmException e3) {
//			// TODO Auto-generated catch block
//			e3.printStackTrace();
//		}
//		
//		gen.initialize(2048);
//		KeyPair keyPair = gen.generateKeyPair();
//
//		// Convert to JWK format
//		JWK jwk = new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
//		    .privateKey((RSAPrivateKey)keyPair.getPrivate())
//		    .keyUse(KeyUse.SIGNATURE)
//		    .keyID(kid)
//		    .build();
//				
//		return jwk;
//	}
//	
//	public static JWKSet getJwks() {
//		return new JWKSet(CommConstants.jwkList);
//	}
//	
//	//AccessToken, RefreshToken 반환
////	public static ReturnTokens genJWT(String issuer, String subject, String clientType, String username) throws JOSEException {
////		if(new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject) == null) {
////			System.out.println("No Key of " + subject);
////			makeJWKS(subject);
////		}
////		
////		JWK jwk = new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject);
////		// get PK in jwk
////		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
////		
////		// Create RSA Signer
////		JWSSigner signer = null;
////		signer = new RSASSASigner(privateKey);
////
////		// Prepare JWT with claims set
////		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
////			.claim("username", username)
////			.issuer(issuer)
////			.audience(ip)
////		    .subject(subject)
//////		    .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60))
////		    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
////		    .build();
////
////		SignedJWT accessToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
////
////		// Apply the RSA-256 protection
////		accessToken.sign(signer);
////		
////		claimsSet = new JWTClaimsSet.Builder()
////				.issuer(issuer)
////				.audience(ip)
////			    .subject(subject)
////			    .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60 * 24 * 14))
////			    .build();
////		
////		SignedJWT refreshToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
////		
////		refreshToken.sign(signer);
////
////		// Serialize to compact form, produces something like
////		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
////		String accessTokenSerialized = accessToken.serialize();
////		String refreshTokenSerialized = refreshToken.serialize();
////		
////		ReturnTokens returnToken = new ReturnTokens(accessTokenSerialized, refreshTokenSerialized);
////			
////		return returnToken;
////	}
//	
//	public static ReturnTokens genJWT(String issuer, String subject, String clientType, String username) throws JOSEException {
//		if(new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject) == null) {
//			log.info("No Key of " + subject);
//			return null;
//		}
//		
//		JWK jwk = new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject);
//		// get PK in jwk
//		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
//		
//		// Create RSA Signer
//		JWSSigner signer = null;
//		signer = new RSASSASigner(privateKey);
//
//		// make access token with claim set
//		SignedJWT accessToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
//				makeClaimSet(issuer, subject, clientType, username, false));
//
//		// Apply the RSA-256 protection
//		accessToken.sign(signer);
//		
//		// make refresh token with claim set
//		SignedJWT refreshToken = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), 
//				makeClaimSet(issuer, subject, clientType, username, true));
//		
//		// Apply the RSA-256 protection
//		refreshToken.sign(signer);
//
//		// Serialize to compact form, produces something like
//		// eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
//		String accessTokenSerialized = accessToken.serialize();
//		String refreshTokenSerialized = refreshToken.serialize();
//		
//		ReturnTokens returnToken = new ReturnTokens(accessTokenSerialized, refreshTokenSerialized);
//			
//		return returnToken;
//	}
//	
//	public static String updateToken(String refreshToken) {
//		
//	}
//	
//	public static SignedJWT genSignedJWT(String issuer, String subject, String ip) throws JOSEException {
//		if(new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject) == null) {
//			System.out.println("No Key of " + subject);
//			makeJWKS(subject);
//		}
//		
//		JWK jwk = new JWKSet(CommConstants.jwkList).getKeyByKeyId(subject);
//		// get PK in jwk
//		PrivateKey privateKey = jwk.toRSAKey().toPrivateKey();
//		
//		// Create RSA Signer
//		JWSSigner signer = null;
//		signer = new RSASSASigner(privateKey);
//
//		// Prepare JWT with claims set
//		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//			.issuer(issuer)
//			.audience(ip)
//		    .subject(subject)
//		    .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60))
//		    .build();
//
//		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
//
//		// Apply the RSA-256 protection
//		signedJWT.sign(signer);
//		
//		return signedJWT;
//	}
//	
////	public static void selfTest() throws JOSEException, ParseException, InterruptedException {
////		String token = genJWT("test", "111.111.111.111", "test");
////		
////		SignedJWT jwt = SignedJWT.parse(token);
////		
////		while(true) {
////			System.out.println(jwt.getJWTClaimsSet().getExpirationTime());
////			Thread.sleep(1000);
////		}
////	}
//	
//	public static JWTClaimsSet makeClaimSet(String issuer, String subject, 
//			String clientType, String username, boolean isRefreshToken) {
//		Date expiredTime = null;
//		if(isRefreshToken) {
//			expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60);
//		}
//		else {
//			expiredTime = new Date(new Date().getTime() + 60 * 1000 * 60 * 24 * 14);
//		}
//		
////		JWTClaimsSet claimsSet = null;
//		
//		if(clientType.equals("ITSM")) {
//			return new JWTClaimsSet.Builder()
//				.claim("username", username)
//				.issuer(issuer)
//				.audience(username)
//				.subject(subject)
//				.expirationTime(expiredTime)
//				.build();
//		}
//		else {
//			return new JWTClaimsSet.Builder()
//				.claim("username", username)
//				.issuer(issuer)
//				.subject(subject)
//				.expirationTime(expiredTime)
//				.build();
//		}
//	}
//	
//}
