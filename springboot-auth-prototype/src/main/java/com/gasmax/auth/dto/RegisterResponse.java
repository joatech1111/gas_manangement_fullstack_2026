package com.gasmax.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private boolean success;
    private String message;

    // 가입신청 완료 후 상태: N (관리자 승인 대기)
    private String grantState;
}
