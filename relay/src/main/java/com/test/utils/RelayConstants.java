package com.test.utils;

import java.util.HashMap;

import com.nimbusds.jwt.SignedJWT;
import com.test.dto.VertifyDto;

public class RelayConstants {
	
	public static final String genJWTUrl = "http://52.231.52.58:31011/api/genJwt";
	public static final String refreshTokenUrl = "http://52.231.52.58:31011/api/refreshToken";
	public static final String ssoLoginUrl = "http://52.231.52.58:31011/api/login";
	
//	public static final String genJWTUrl = "http://localhost:8001/api/genJwt";
//	public static final String refreshTokenUrl = "http://localhost:8001/api/refreshToken";
//	public static final String ssoLoginUrl = "http://localhost:8002/api/login";
	
	public static HashMap<String, VertifyDto> verifyMap = new HashMap<String, VertifyDto>();
	public static HashMap<String, SignedJWT> accessTokenList = new HashMap<String, SignedJWT>();
	public static HashMap<String, SignedJWT> refreshTokenList = new HashMap<String, SignedJWT>();
	
} 
