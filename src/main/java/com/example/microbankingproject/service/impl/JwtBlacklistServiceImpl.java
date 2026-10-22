package com.example.microbankingproject.service.impl;

import com.example.investhome.service.auth.JwtBlacklistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addBlacklist(String accessToken, Long TTL) {
        String key = DigestUtils.md5DigestAsHex(accessToken.getBytes()) + " Old Access Token:";
        redisTemplate.opsForValue().
                set(key, accessToken, TTL, TimeUnit.MINUTES);
    }

    @Override
    public boolean isBlacklisted(String oldAccessToken) {
        String hash = DigestUtils.md5DigestAsHex(oldAccessToken.getBytes());
        String value = (String) redisTemplate.opsForValue().get(hash + " Old Access Token:");
        return value != null && value.equals(oldAccessToken);
    }
}
