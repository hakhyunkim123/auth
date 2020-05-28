package com.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@SuppressWarnings("rawtypes")
@RestController
public class SimpleController {
	
	@GetMapping("/slc")
	public ResponseEntity text1(@RequestHeader(value = "test-auth", required = false) String token){
		
		if(token == null) token = "faded...;;;";
		String resultText = "This is SLC SERVICE" + "\n" 
		+ "Token: " + token + "\n";
		
		return new ResponseEntity<>(resultText, HttpStatus.OK);
	}
	
	@GetMapping("/report")
	public ResponseEntity text2(@RequestHeader(value = "test-auth", required = false) String token){
		
		if(token == null) token = "faded...;;;";
		String resultText = "This is REPORT SERVICE" + "\n" 
				+ "Token: " + token + "\n";
		
		return new ResponseEntity<>(resultText, HttpStatus.OK);
	}

	@GetMapping("/svc3")
	public ResponseEntity text3(@RequestHeader(value = "test-auth", required = false) String token){
		
		if(token == null) token = "faded...;;;";
		String resultText = "This is Service 3" + "\n" 
				+ "Token: " + token + "\n";
		
		return new ResponseEntity<>(resultText, HttpStatus.OK);
	}
	
//	@GetMapping("/svc1")
//	public ResponseEntity text1(){
//		String url = "http://simple-svc2.test-mtls.svc.cluster.local:8000/svc2"; 
//		HttpResponse<String> response = Unirest.get(url)
//				.asString();
//		
//		if(response.getStatus() != 200) {
//			String errorMessage = "[svc1] call svc2 API Error!\n"
//					+ "code : " + Integer.toString(response.getStatus()) + "\n"
//					+ "message : " + response.getBody();
//			return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
//		}
//		
//		String result = "[Service 1] call svc2 API Result:" + "\n";
//		result += response.getBody().toString() + " " + "And This is Service 1";
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
//	
//	@GetMapping("/svc3")
//	public ResponseEntity text3(){
//		String url = "http://simple-svc2.test-mtls.svc.cluster.local:8000/svc2"; 
//		HttpResponse<String> response = Unirest.get(url)
//				.asString();
//		
//		if(response.getStatus() != 200) {
//			String errorMessage = "[svc3] call svc2 API Error!\n"
//					+ "code : " + Integer.toString(response.getStatus()) + "\n"
//					+ "message : " + response.getBody();
//			return new ResponseEntity<>(errorMessage, HttpStatus.SERVICE_UNAVAILABLE);
//		}
//		
//		String result = "[Service 1] call svc2 API Result:" + "\n";
//		result += response.getBody().toString() + " " + "And This is Service 3";
//		
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
}
