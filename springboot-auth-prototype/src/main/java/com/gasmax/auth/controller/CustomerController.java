package com.gasmax.auth.controller;

import com.gasmax.auth.model.AppUser;
import com.gasmax.auth.service.DynamicJdbcService;
import com.gasmax.auth.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 거래처 검색 / 품목 조회 REST API
 *
 * GET /api/customers                         - 거래처 검색
 * GET /api/customers/{customerCode}/items    - 거래처 품목 조회
 */
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final RedisService redisService;
    private final DynamicJdbcService dynamicJdbcService;

    /**
     * 거래처 검색
     *
     * Header: X-Session-Token: <token>
     * Params:
     *   keyword      - 검색어 (이름 / 주소 / 전화번호)
     *   type         - 거래처 유형: 0=중량, 1=체적검침, 2=체적공급, A=전체 (기본값: A)
     *   customerCode - 단건 조회 시 거래처 코드 (선택)
     */
    @GetMapping
    public ResponseEntity<?> search(
            @RequestHeader("X-Session-Token") String token,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "A")  String type,
            @RequestParam(defaultValue = "") String customerCode
    ) {
        AppUser user = redisService.getUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다.");
        }

        log.info("거래처 검색 - areaCode: {}, keyword: '{}', type: {}", user.getAreaCode(), keyword, type);

        List<Map<String, Object>> result = dynamicJdbcService.searchCustomers(user, keyword, customerCode, type);
        return ResponseEntity.ok(result);
    }

    /**
     * 거래처 품목 조회
     * 기존: BizCustomerItem (SelectWeightHPG / SelectWeightLPG)
     *
     * Header: X-Session-Token: <token>
     * PathVar: customerCode - 거래처 코드
     * Params:
     *   saleType  - 0=가스(기본), 1=용기  (LPG 전용; HPG 는 자동 무시)
     *   itemCode  - 단건 조회 시 품목 코드 (선택)
     *
     * gasType 은 세션(AppUser.gasType)에서 자동 판별:
     *   HIGH → fn_CUST_JP_Danga    (고압 거래처별 단가)
     *   LPG  → fn_CUST_JP_Danga_LPG (LPG 거래처별 단가)
     */
    @GetMapping("/{customerCode}/items")
    public ResponseEntity<?> items(
            @RequestHeader("X-Session-Token") String token,
            @PathVariable String customerCode,
            @RequestParam(defaultValue = "0") String saleType,
            @RequestParam(defaultValue = "") String itemCode
    ) {
        AppUser user = redisService.getUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다.");
        }

        log.info("거래처 품목 조회 - customerCode: {}, gasType: {}, saleType: {}", customerCode, user.getGasType(), saleType);

        List<Map<String, Object>> result = dynamicJdbcService.findCustomerItems(user, customerCode, saleType, itemCode);
        return ResponseEntity.ok(result);
    }
}
