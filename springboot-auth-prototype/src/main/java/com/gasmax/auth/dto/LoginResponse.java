package com.gasmax.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private boolean success;
    private String message;

    // 로그인 성공 시 Redis 세션 토큰
    private String sessionToken;

    // 사용자 기본 정보
    private String userId;
    private String employeeName;
    private String areaCode;
    private String areaName;
    private String gasType;
    private String menuPermission;
    private String grantState;
}
