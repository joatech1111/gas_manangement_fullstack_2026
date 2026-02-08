package com.joainfo.common.util;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import com.joainfo.gasmax.bean.AppUser;

public class RedisUtil {
    private static final String REDIS_HOST; // Redis 서버 주소
    private static final int REDIS_PORT = 6379; // Redis 포트
    private static final Gson gson = new Gson(); // JSON 변환기

    static {
        // 환경변수 REDIS_HOST가 있으면 사용, 없으면 localhost (로컬 개발용)
        String host = System.getenv("REDIS_HOST");
        REDIS_HOST = (host != null && !host.isEmpty()) ? host : "localhost";
        System.out.println("[RedisUtil] Redis host: " + REDIS_HOST + ":" + REDIS_PORT);
    }

    /**
     * ✅ Redis에 sessionToken을 이용해 사용자 정보를 저장하는 메소드
     * @param sessionToken 세션 토큰
     * @param appUser 사용자 정보 객체
     */
    public static void saveUserToRedis(String sessionToken, AppUser appUser) {
        if (sessionToken == null || sessionToken.isEmpty() || appUser == null) {
            return;
        }

        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            String userJson = gson.toJson(appUser);
            jedis.setex("SESSION:" + sessionToken, 3600, userJson); // 60분 유지
        }
    }

    /**
     * ✅ Redis에서 sessionToken을 이용해 사용자 정보를 가져오는 메소드
     * @param sessionToken 클라이언트에서 전달한 세션 토큰
     * @return AppUser 객체 / null (토큰이 만료되었거나 존재하지 않으면)
     */
    public static AppUser getUserFromSessionToken(String sessionToken) {
        if (sessionToken == null || sessionToken.isEmpty()) {
            return null;
        }

        String userJson;
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            userJson = jedis.get("SESSION:" + sessionToken);
        }

        if (userJson == null) {
            return null;
        }

        return gson.fromJson(userJson, AppUser.class);
    }

    /**
     * ✅ Redis에 추가 데이터를 저장하는 메소드 (예: EMPLOYEE_CODE)
     * @param sessionToken 세션 토큰
     * @param key 저장할 데이터의 키 (예: EMPLOYEE_CODE)
     * @param value 저장할 값
     */
    public static void saveToRedis(String sessionToken, String key, Object value) {
        if (sessionToken == null || sessionToken.isEmpty() || key == null || key.isEmpty()) {
            return;
        }

        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            String redisKey = "SESSION:" + sessionToken + ":" + key;
            String jsonValue = gson.toJson(value);
            jedis.setex(redisKey, 3600, jsonValue); // 60분 유지
        }
    }

    /**
     * ✅ Redis에서 추가 데이터를 가져오는 메소드 (예: EMPLOYEE_CODE)
     * @param sessionToken 세션 토큰
     * @param key 가져올 데이터의 키 (예: EMPLOYEE_CODE)
     * @param clazz 변환할 클래스 타입
     * @return 변환된 객체 / null (존재하지 않거나 만료된 경우)
     */
    public static <T> T getFromRedis(String sessionToken, String key, Class<T> clazz) {
        if (sessionToken == null || sessionToken.isEmpty() || key == null || key.isEmpty()) {
            return null;
        }

        String jsonValue;
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            String redisKey = "SESSION:" + sessionToken + ":" + key;
            jsonValue = jedis.get(redisKey);
        }

        if (jsonValue == null) {
            return null;
        }

        return gson.fromJson(jsonValue, clazz);
    }


    public static void removeFromRedis(String sessionToken, String key) {
        if (sessionToken == null || sessionToken.isEmpty() || key == null || key.isEmpty()) return;
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            jedis.del("SESSION:" + sessionToken + ":" + key);
        }
    }
}
