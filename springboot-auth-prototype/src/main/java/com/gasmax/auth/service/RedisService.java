package com.gasmax.auth.service;

import com.gasmax.auth.model.AppUser;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * 기존 RedisUtil.java 기반 → Spring Bean으로 전환
 * 세션 토큰을 Redis에 저장/조회
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private static final String SESSION_PREFIX = "SESSION:";
    private static final long SESSION_TTL_SECONDS = 3600; // 1시간 (기존과 동일)

    private final StringRedisTemplate redisTemplate;
    private final Gson gson = new Gson();

    /**
     * 새 세션 토큰 발급 후 Redis에 저장
     */
    public String saveSession(AppUser appUser) {
        String sessionToken = UUID.randomUUID().toString();
        String userJson = gson.toJson(appUser);
        redisTemplate.opsForValue().set(
                SESSION_PREFIX + sessionToken,
                userJson,
                Duration.ofSeconds(SESSION_TTL_SECONDS)
        );
        return sessionToken;
    }

    /**
     * 세션 토큰으로 사용자 조회
     */
    public AppUser getUser(String sessionToken) {
        String userJson = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionToken);
        if (userJson == null) return null;
        return gson.fromJson(userJson, AppUser.class);
    }

    /**
     * 세션 삭제 (로그아웃)
     */
    public void removeSession(String sessionToken) {
        redisTemplate.delete(SESSION_PREFIX + sessionToken);
    }
}
