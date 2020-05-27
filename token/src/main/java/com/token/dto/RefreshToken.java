package com.token.dto;

import lombok.Data;

@Data
public class RefreshToken {
	private String username;
	private String serviceCode;
	private String token;
}
