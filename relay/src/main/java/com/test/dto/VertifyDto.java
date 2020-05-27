package com.test.dto;

public class VertifyDto {
	private String ip ;
	private String code;
	
//	public VertifyDto(List<String> ipList, String code) {
//		for(String ip :  ipList)
//			this.ip.add(ip);
//		this.code = code;
//	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
