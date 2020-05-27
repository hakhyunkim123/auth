package com.token.dto;

import lombok.Data;

@Data
public class RSAKeyPair {
	private String kid;
	private String publicKey;
	private String privateKey;
}
