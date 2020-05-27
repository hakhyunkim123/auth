package com.utils;

import java.util.ArrayList;
import java.util.List;

import com.nimbusds.jose.jwk.JWK;

public class CommConstants {
	public static final String TEST_JWK_KID = "TESTKEYID";
	public static final String TEST_ISSUER = "relaytest.com";
	public static final String TEST_SUBJECT = "testsubject";
	
	public static List<JWK> jwkList = new ArrayList<JWK>();
}
