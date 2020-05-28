package com.test.utils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.nimbusds.jwt.SignedJWT;
import com.test.domain.ReturnTokens;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class RelayUtils {
//	public static HttpResponse<String> callGenJWTApi(String url, String issuer, String code, String ip) {
//		
//		return Unirest.get(url)
//				.header("issuer", issuer)
//				.header("subject", code)
//				.header("ip", ip)
//				.asString();
//	}
	
	public static HttpResponse<ReturnTokens> callGenJWT(String url, 
		String issuer, String client, String code, String ip) {
		
		return Unirest.post(url)
				.header("issuer", issuer)
				.header("clientType", client)
				.header("username", ip)
				.header("subject", code)
				.asObject(ReturnTokens.class)
				;
	}
	
	public static HttpResponse<String> callRefreshTokenApi(String refreshTokenSerialized) {
		
		return Unirest.post(RelayConstants.refreshTokenUrl)
				.header("refreshToken", refreshTokenSerialized)
				.asString();
	}
	
	public static HttpResponse<String> callSvcApi(String serializedToken, String code) {
		code = code.toLowerCase();
		String svcUrl = "http://simple-"+ code + ".test-mtls.svc.cluster.local:8000/" + code;
		
		System.out.println("url: " + svcUrl );
		
		return Unirest.get(svcUrl)
				.header("test-auth", serializedToken)
				.asString();
		
	}
	
	@SuppressWarnings("rawtypes")
	public static HttpResponse callSSOApi(String username, String password) {
		
		return Unirest.post(RelayConstants.ssoLoginUrl)
				.header("username", username)
				.header("password", password)
				.asEmpty()
		;
	}
	
	public static String tokenSerialized(SignedJWT token) {
		return token.serialize();
	}
	
	public static SignedJWT parseToken(String serializedToken) {
		SignedJWT token = null;
		try {
			token = SignedJWT.parse(serializedToken);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}
	
	public static boolean isExpired(SignedJWT token) {
		Date expiredTime = null;
		try {
			expiredTime = token.getJWTClaimsSet().getExpirationTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocalDateTime expTime = LocalDateTime.ofInstant(expiredTime.toInstant(), ZoneId.of("Asia/Seoul"));
		LocalDateTime curTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		
		System.out.println("expired time: " + expTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		System.out.println("current time: " + expTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		
		return curTime.isAfter(expTime.minusMinutes(3));
	}
}
