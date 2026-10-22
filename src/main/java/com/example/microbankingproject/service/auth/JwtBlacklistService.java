package com.example.microbankingproject.service.auth;

public interface JwtBlacklistService {

    void addBlacklist(String accessToken, Long TTL);

    boolean isBlacklisted(String oldAccessToken);
}