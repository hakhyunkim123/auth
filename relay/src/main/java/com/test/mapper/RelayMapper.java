package com.test.mapper;

import java.util.List;

import com.test.dto.VertifyDto;

public interface RelayMapper {
	public List<String> readIpListByServiceCode(String serviceCode);
	public List<VertifyDto> readAllServiceMapping();
}
