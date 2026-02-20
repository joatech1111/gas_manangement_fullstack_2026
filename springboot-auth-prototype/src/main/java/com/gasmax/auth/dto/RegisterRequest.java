package com.gasmax.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    // 기기 UUID (HP_Number) - varchar(255)
    @NotBlank(message = "기기 UUID를 입력해주세요.")
    @Size(max = 255, message = "UUID는 255자 이하여야 합니다.")
    private String uuid;

    // 폰 기종 (HP_Model) - varchar(20)
    @Size(max = 20, message = "폰 기종은 20자 이하여야 합니다.")
    private String phoneModel;

    // 직원명 (BA_SW_Name) - varchar(10)
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10자 이하여야 합니다.")
    private String employeeName;

    // 로그인 아이디 (Login_User) - varchar(15)
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(max = 15, message = "아이디는 15자 이하여야 합니다.")
    private String userId;

    // 로그인 비밀번호 (Login_Pass) - varchar(15)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 15, message = "비밀번호는 15자 이하여야 합니다.")
    private String password;

    // 지역 전화번호 (AREA_DDD) - varchar(3)
    @Size(max = 3, message = "지역번호는 3자 이하여야 합니다.")
    private String phoneAreaNumber;

    // 사업소 코드 (Login_Area) - varchar(2) ← 반드시 2자리 이하!
    @NotBlank(message = "사업소 코드를 입력해주세요.")
    @Size(max = 2, message = "사업소 코드는 2자 이하여야 합니다. (예: 01, 02)")
    private String areaCode;

    // 사업소명 (Login_Co) - varchar(30)
    @NotBlank(message = "사업소명을 입력해주세요.")
    @Size(max = 30, message = "사업소명은 30자 이하여야 합니다.")
    private String areaName;
}
