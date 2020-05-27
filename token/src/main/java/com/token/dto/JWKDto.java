package com.token.dto;

import lombok.Data;

@Data
public class JWKDto {
	private String kid;
	private String encryptedKey;
}
