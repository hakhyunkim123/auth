package com.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.dto.VertifyDto;
import com.test.mapper.RelayMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RelayManager {
	
//	@Autowired static RelayMapper relayMapper;
//	
//	public static void updateVerifyMap() {
//		RelayConstants.verifyMap.clear();
//		List<VertifyDto> serviceMapList = relayMapper.readAllServiceMapping();
//		for(VertifyDto vertify : serviceMapList) {
//			RelayConstants.verifyMap.put(vertify.getCode(), vertify);
//			
//			log.info("[{}] - [{}]", vertify.getCode().trim(), vertify.getIp().trim());
//		}
//	}
}
