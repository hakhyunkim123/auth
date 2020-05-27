package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.test.utils.RelayManager;

@SpringBootApplication
public class RelayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelayApplication.class, args);
		
//		RelayManager.updateVerifyMap();
	}
}
