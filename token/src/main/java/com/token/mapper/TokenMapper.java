package com.token.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.token.dto.RefreshToken;

@Mapper
public interface TokenMapper {
	public RefreshToken readRefreshToken(Map<String, String> params);
	public int countRefreshToken(Map<String, String> params);
    public void createRefreshToken(RefreshToken token);
    public void deleteRefreshToken(Map<String, String> params);
    public void updateRefreshToken(RefreshToken token);
}
