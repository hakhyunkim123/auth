package com.token;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.token.dto.JWKDto;
import com.token.service.TokenService;
import com.utils.TokenManager;

@SpringBootApplication
public class TokenApplication {
	public static void main(String[] args) {
		SpringApplication.run(TokenApplication.class, args);
	}

}
