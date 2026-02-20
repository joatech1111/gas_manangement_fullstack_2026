package com.gasmax.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    // 기기 UUID (선택: 없으면 ID/PW만으로 로그인)
    private String uuid;

    // 회사 순번 (선택: 다중 회사 지원)
    private String areaSeq;
}
