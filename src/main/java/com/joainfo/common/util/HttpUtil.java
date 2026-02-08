package com.joainfo.common.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * HTTP/HTTPS 통신 유틸리티 클래스
 * TLS 1.2 지원
 */
public class HttpUtil {
    
    /**
     * TLS 1.2를 사용하도록 SSL 컨텍스트 초기화
     */
    static {
        try {
            // TLS 1.2를 명시적으로 사용
            System.setProperty("https.protocols", "TLSv1.2");
            System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
            
            // SSL 컨텍스트 설정 (모든 인증서 신뢰 - 개발용)
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * HTTPS URL 연결을 위한 HttpURLConnection 생성 (TLS 1.2 사용)
     * @param urlString URL 문자열
     * @return HttpURLConnection
     * @throws Exception
     */
    public static HttpURLConnection createHttpsConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        // HTTPS인 경우 TLS 1.2 설정
        if (con instanceof HttpsURLConnection) {
            HttpsURLConnection httpsCon = (HttpsURLConnection) con;
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, new java.security.SecureRandom());
            httpsCon.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        
        return con;
    }
}
