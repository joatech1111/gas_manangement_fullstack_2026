package com.joainfo.common.util;

/**
 * TLS 1.2 설정을 위한 유틸리티 클래스
 * 애플리케이션 시작 시 자동으로 TLS 1.2를 활성화합니다.
 */
public class TLSConfig {
    
    static {
        // TLS 1.2를 기본 프로토콜로 설정
        System.setProperty("https.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
        
        // Java 8에서 TLS 1.2 강제 사용
        System.setProperty("com.sun.net.ssl.checkRevocation", "false");
    }
    
    /**
     * TLS 설정을 초기화합니다.
     * 이 메서드를 호출하면 TLS 1.2가 활성화됩니다.
     */
    public static void init() {
        // static 블록에서 이미 설정됨
    }
}
