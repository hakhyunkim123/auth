package com.token.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.token.dto.RSAKeyPair;

@Mapper
public interface RSAKeyPairMapper {
	public RSAKeyPair readPrivateKey(String kid);
    public void createPrivateKey(RSAKeyPair key);
    public void deletePrivateKey(String kid);
    public void updatePrivateKey(RSAKeyPair key);
}