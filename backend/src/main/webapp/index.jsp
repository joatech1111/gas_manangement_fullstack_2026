<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>세션 데이터 저장</title>
</head>
<body>
<%
    // 세션 객체에 데이터 저장
    session.setAttribute("userName", "홍길동");
    session.setAttribute("userEmail", "hong@example.com");
%>
<p>joatech gasmax management server start!!!!!.</p>
<%--<a href="getSessionData.jsp">세션 데이터 확인</a>--%>
</body>
</html>
