package com.token.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.token.dto.JWKDto;


@Mapper
public interface PrivateKeyMapper {
	public JWKDto readPrivateKey(String kid);
	public List<JWKDto> readAllKeys();
    public void createPrivateKey(JWKDto key);
    public void deletePrivateKey(String kid);
    public void updatePrivateKey(JWKDto key);
}