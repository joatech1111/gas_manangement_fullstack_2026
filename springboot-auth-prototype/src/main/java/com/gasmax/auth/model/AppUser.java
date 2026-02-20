package com.gasmax.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 기존 Spring MVC 프로젝트의 AppUser.java 기반
 * DB 테이블: GasMax_App.dbo.AppUser
 */
@Data
@NoArgsConstructor
public class AppUser {

    // HP_Number - 기기 UUID (식별자)
    private String macNumber;

    // HP_SEQ - 회사/지역 순번
    private String areaSeq;

    // HP_State - 인증상태: Y(승인), N(가입신청), X(중지)
    private String grantState;

    // HP_Model - 폰 기종
    private String phoneModel;

    // Login_User - 로그인 아이디
    private String userId;

    // Login_Pass - 로그인 비밀번호
    private String password;

    // BA_SW_Name - 직원명
    private String employeeName;

    // BA_SW_CODE - 직원코드
    private String employeeCode;

    // Login_Area - 사업소 코드
    private String areaCode;

    // Login_Co - 사업소명
    private String areaName;

    // AREA_DDD - 지역 전화번호
    private String phoneAreaNumber;

    // SVR_IP - 서버 IP
    private String ipAddress;

    // SVR_DBName - DB 카탈로그명
    private String dbCatalogName;

    // SVR_User - DB 유저ID
    private String dbUserId;

    // SVR_Pass - DB 비밀번호
    private String dbPassword;

    // SVR_Port - 포트
    private String port;

    // APP_Cert - 메뉴 권한
    private String menuPermission;

    // APP_PG_TYPE - 가스 타입: HIGH(고압), LPG
    private String gasType;

    // Login_LastDate - 마지막 로그인 일시
    private String lastLoginDate;

    // Login_StartDate - 가입일
    private String joinDate;

    // Login_EndDate - 만료일
    private String expiryDate;

    // Sign_Folder - 전자서명 경로
    private String signImagePath;
}
