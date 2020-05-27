package com.token.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnTokens {
	private String accessTokenSerialized;
	private String refreshTokenSerialized;
	
	public ReturnTokens(String accessTokenSerialized, String refreshTokenSerialized) {
		this.accessTokenSerialized = accessTokenSerialized;
		this.refreshTokenSerialized = refreshTokenSerialized;
	}
	
}
