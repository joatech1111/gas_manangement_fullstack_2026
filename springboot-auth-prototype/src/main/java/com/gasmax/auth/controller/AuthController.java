package com.gasmax.auth.controller;

import com.gasmax.auth.dto.LoginRequest;
import com.gasmax.auth.dto.LoginResponse;
import com.gasmax.auth.dto.RegisterRequest;
import com.gasmax.auth.dto.RegisterResponse;
import com.gasmax.auth.model.AppUser;
import com.gasmax.auth.service.AuthService;
import com.gasmax.auth.service.DynamicJdbcService;
import com.gasmax.auth.service.RedisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 로그인 / 가입신청 REST API
 *
 * POST /api/auth/login    → 로그인
 * POST /api/auth/register → 가입신청
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisService redisService;
    private final DynamicJdbcService dynamicJdbcService;

    /**
     * 로그인
     *
     * Request Body:
     * {
     *   "userId": "user01",
     *   "password": "pass1234",
     *   "uuid": "DEVICE-UUID-OPTIONAL",   (선택)
     *   "areaSeq": "1"                    (선택)
     * }
     *
     * Response:
     * {
     *   "success": true,
     *   "message": "로그인 성공",
     *   "sessionToken": "uuid-token",
     *   "userId": "user01",
     *   "employeeName": "홍길동",
     *   "areaCode": "001",
     *   "areaName": "서울사업소",
     *   "gasType": "LPG",
     *   "menuPermission": "YYYYY",
     *   "grantState": "Y"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청 - userId: {}", request.getUserId());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 영업소 코드 목록 (로그인 후 사용 — 세션에서 DB 접속정보 조회)
     * GET /api/auth/area-codes
     * Header: X-Session-Token: <token>
     * 기존: GASMAX.AppUser.Select.AreaCode
     */
    @GetMapping("/area-codes")
    public ResponseEntity<?> areaCodes(@RequestHeader("X-Session-Token") String token) {
        AppUser user = redisService.getUser(token);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다.");
        return ResponseEntity.ok(dynamicJdbcService.findAreaCodes(user));
    }

    /**
     * 지역번호 목록 (로그인 후 사용 — 세션에서 DB 접속정보 조회)
     * GET /api/auth/phone-area-numbers
     * Header: X-Session-Token: <token>
     * 기존: GASMAX.AppUser.Select.PhoneAreaNumber
     */
    @GetMapping("/phone-area-numbers")
    public ResponseEntity<?> phoneAreaNumbers(@RequestHeader("X-Session-Token") String token) {
        AppUser user = redisService.getUser(token);
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다.");
        return ResponseEntity.ok(dynamicJdbcService.findPhoneAreaNumbers(user));
    }

    /**
     * 가입신청
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("가입신청 요청 - userId: {}, uuid: {}", request.getUserId(), request.getUuid());
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}
