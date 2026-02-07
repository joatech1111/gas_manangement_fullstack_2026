package com.joainfo.common.util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*", "*.jsp"}) // 모든 요청 + JSP 포함
public class SessionCookieFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 세션 가져오기 (없으면 생성)
        HttpSession session = req.getSession(true);

        // JSESSIONID 쿠키 강제 설정
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(req.isSecure());
        sessionCookie.setMaxAge(30 * 60);
        sessionCookie.setSecure(false);
        // SameSite 설정
        res.addCookie(sessionCookie);
        res.setHeader("Set-Cookie", "JSESSIONID=" + session.getId() + "; Path=/; HttpOnly; Secure; SameSite=None");

        // 필터 통과 후 요청 실행
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
