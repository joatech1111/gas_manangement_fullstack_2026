package com.gasmax.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation 오류 (@NotBlank 등)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst().orElse("입력값이 올바르지 않습니다.");
        return error(HttpStatus.BAD_REQUEST, msg);
    }

    // DB / Redis / 기타 모든 예외 → 전체 예외 체인을 응답에 포함
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        log.error("서버 오류", ex);

        // 전체 예외 원인 체인을 추적
        StringBuilder chain = new StringBuilder();
        Throwable t = ex;
        while (t != null) {
            chain.append("[").append(t.getClass().getSimpleName()).append("] ")
                 .append(t.getMessage() != null ? t.getMessage() : "(no message)");
            t = t.getCause();
            if (t != null) chain.append(" → ");
        }
        return error(HttpStatus.INTERNAL_SERVER_ERROR, chain.toString());
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
