package com.gasmax.auth.service;

import com.gasmax.auth.dto.LoginRequest;
import com.gasmax.auth.dto.LoginResponse;
import com.gasmax.auth.dto.RegisterRequest;
import com.gasmax.auth.dto.RegisterResponse;
import com.gasmax.auth.mapper.AppUserMapper;
import com.gasmax.auth.model.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserMapper appUserMapper;
    private final RedisService redisService;

    public LoginResponse login(LoginRequest request) {
        List<AppUser> users;

        if (request.getUuid() != null && !request.getUuid().isBlank()) {
            users = appUserMapper.findByUserIdPasswordAndUuid(
                    request.getUserId(), request.getPassword(), request.getUuid());
        } else {
            users = appUserMapper.findByUserIdAndPassword(
                    request.getUserId(), request.getPassword());
        }

        if (users == null || users.isEmpty()) {
            log.warn("로그인 실패 - userId: {}", request.getUserId());
            return LoginResponse.builder()
                    .success(false)
                    .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                    .build();
        }

        // areaSeq 지정 시 해당 회사, 없으면 첫 번째 계정 사용
        AppUser appUser;
        if (request.getAreaSeq() != null && !request.getAreaSeq().isBlank()) {
            appUser = users.stream()
                    .filter(u -> request.getAreaSeq().equals(u.getAreaSeq()))
                    .findFirst()
                    .orElse(users.get(0));
        } else {
            appUser = users.get(0);
        }

        // 다중 회사 계정이면 목록 정보도 응답에 포함
        if (users.size() > 1) {
            log.info("다중 회사 계정 감지 - userId: {}, 계정수: {}", request.getUserId(), users.size());
        }

        try {
            appUserMapper.updateLastLoginDate(appUser.getMacNumber(), appUser.getAreaSeq());
        } catch (Exception e) {
            log.warn("마지막 로그인 일시 업데이트 실패: {}", e.getMessage());
        }

        String sessionToken = redisService.saveSession(appUser);
        log.info("로그인 성공 - userId: {}, employeeName: {}", appUser.getUserId(), appUser.getEmployeeName());

        return LoginResponse.builder()
                .success(true)
                .message(users.size() > 1
                        ? "로그인 성공 (총 " + users.size() + "개 회사 계정)"
                        : "로그인 성공")
                .sessionToken(sessionToken)
                .userId(appUser.getUserId())
                .employeeName(appUser.getEmployeeName())
                .areaCode(appUser.getAreaCode())
                .areaName(appUser.getAreaName())
                .gasType(appUser.getGasType())
                .menuPermission(appUser.getMenuPermission())
                .grantState(appUser.getGrantState())
                .build();
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        int count = appUserMapper.countByUuid(request.getUuid());
        if (count > 0) {
            log.warn("가입신청 실패 - 이미 등록된 UUID: {}", request.getUuid());
            return RegisterResponse.builder()
                    .success(false)
                    .message("이미 가입신청된 기기입니다.")
                    .build();
        }

        AppUser newUser = new AppUser();
        newUser.setMacNumber(request.getUuid());
        newUser.setPhoneModel(request.getPhoneModel());
        newUser.setEmployeeName(request.getEmployeeName());
        newUser.setUserId(request.getUserId());
        newUser.setPassword(request.getPassword());
        newUser.setPhoneAreaNumber(request.getPhoneAreaNumber());
        newUser.setAreaCode(request.getAreaCode());
        newUser.setAreaName(request.getAreaName());

        int result = appUserMapper.insertAppUser(newUser);
        if (result <= 0) {
            return RegisterResponse.builder()
                    .success(false)
                    .message("가입신청 처리 중 오류가 발생했습니다.")
                    .build();
        }

        log.info("가입신청 완료 - userId: {}, uuid: {}", request.getUserId(), request.getUuid());
        return RegisterResponse.builder()
                .success(true)
                .message("가입신청이 완료되었습니다. 관리자 승인 후 사용 가능합니다.")
                .grantState("N")
                .build();
    }
}
