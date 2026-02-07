<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>세션 데이터 가져오기</title>
</head>
<body>
<%
    // 세션에서 데이터 가져오기
    String userName = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");

    if (userName != null && userEmail != null) {
%>
<p>사용자 이름: <%= userName %></p>
<p>사용자 이메일: <%= userEmail %></p>
<%
} else {
%>
<p>세션에 저장된 데이터가 없습니다.</p>
<%
    }
%>
</body>
</html>
