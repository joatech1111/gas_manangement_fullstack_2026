<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="com.google.gson.Gson" %>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%@ page session="true" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.attribute.USER_REGISTER_TYPE" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.UUID" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%@ page import="redis.clients.jedis.Jedis" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
    request.setCharacterEncoding("UTF-8");
%><%
    String uuid = request.getParameter("uuid");
    String _uuid = request.getParameter("uuid");

    if (_uuid != null) _uuid = StringUtil.decodeBase64(_uuid);
    //if ((_uuid==null) || "".equals(_uuid)) {
    //_uuid = "01012345678";
    //}
    String areaCode = request.getParameter("areaCode");
    String hpSeq = request.getParameter("hpSeq");

    String userId = request.getParameter("loginId");
    String password = request.getParameter("loginPw");
    if (password != null) {
        password = StringUtil.decodeBase64(password);
    }
    String userKeyValue = StringUtil.getKeyValue(uuid);
    Gson gson = new Gson();
    boolean auth = true; // 권한 체크 통과 여부
    String errorCode = "";
    String errorMessage = "";
    AppUser appUser = new AppUser();


    System.out.println("=== auth_check_s3_ajx_v2.jsp DEBUG START ===");
    System.out.println("userId: " + userId);
    System.out.println("password: " + (password != null ? "***" : "null"));
    System.out.println("uuid: " + uuid);
    System.out.println("areaCode: " + areaCode);
    System.out.println("hpSeq: " + hpSeq);

    if ((userId == null) || ("".equals(userId)) || (password == null) || ("".equals(password))) {
        auth = false;
        errorCode = "N"; // 입력 오류
        errorMessage = "아이디 또는 비밀번호가 입력되지 않았습니다.";
        System.out.println("❌ ERROR: userId or password is empty");
    } else {
        if (userId.equals("test") && password.equals("test")) {
            System.out.println(">>> test/test 로그인 감지");
            System.out.println(">>> DEFAULT_APP_USER_CATATLOG_NAME: " + BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME);
            try {
                appUser = BizAppUser.getInstance().getAppUser_id_pwd(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, userId.trim(), password);
                System.out.println(">>> test 로그인 결과 appUser: " + (appUser != null ? "Found (userId: " + appUser.getUserId() + ")" : "NULL"));
                if (appUser == null) {
                    System.out.println("❌ ERROR: appUser is null for test/test");
                }
            } catch (Exception e) {
                System.out.println("❌ EXCEPTION in test/test login: " + e.getMessage());
                e.printStackTrace();
                auth = false;
                errorCode = "H";
                errorMessage = "로그인 처리 중 오류가 발생했습니다: " + e.getMessage();
            }

        } else {
            // hpSeq가 있으면 hpSeq로 필터링, 없으면 areaCode로 필터링, 둘 다 없으면 전체 조회
            if (hpSeq != null && !hpSeq.isEmpty()) {
                System.out.println(">>> hpSeq로 필터링: " + hpSeq);
                appUser = BizAppUser.getInstance().getAppUser_id_pwd_uuid_hpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, userId.trim(), password, uuid, hpSeq);
                System.out.println("hpSeq 필터링 결과 appUser: " + appUser);
            } else if (areaCode != null && !areaCode.isEmpty() && !"0".equals(areaCode)) {
                System.out.println(">>> areaCode로 필터링: " + areaCode);
                appUser = BizAppUser.getInstance().getAppUser_id_pwd_uuid_areaCode(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, userId.trim(), password, uuid, areaCode);
                System.out.println("areaCode 필터링 결과 appUser: " + appUser);
            } else {
                System.out.println(">>> 필터링 없음 - 전체 조회");
                appUser = BizAppUser.getInstance().getAppUser_id_pwd_uuid(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, userId.trim(), password, uuid);
            }
        }


        if (!("test".equals(userId) && "test".equals(password))) {
            if ("null".equals(uuid) || "".equals(uuid) || uuid == null) {
                auth = false;
                errorCode = "M"; // 등록되지 않은 핸드폰
                errorMessage = "모바일 기기 정보를 가져오는데 실패했습니다.\n앱을 다시 실행해주세요.";
            }
        }
        System.out.println(">>> appUser null check: " + (appUser == null ? "NULL" : "NOT NULL"));
        if (appUser == null || "null".equals(appUser)) {
            auth = false;
            errorCode = "H"; // 등록되지 않은 핸드폰
            //errorMessage = "등록되지 않은 모바일 기기 입니다.";
            errorMessage = "등록되지 않은 사용자 입니다.";
            System.out.println("❌ ERROR: appUser is null or 'null'");
        } else if (!userId.equals(appUser.getUserId())) {
            System.out.println("❌ ERROR: userId mismatch. Expected: " + userId + ", Got: " + appUser.getUserId());
            auth = false;
            errorCode = "E"; // 계정 존재 안함
            errorMessage = "사용자명이 맞지 않습니다.";
        } else if (!password.equals(appUser.getPassword())) {
            auth = false;
            errorCode = "C"; // 비밀번호 정합 오류
            errorMessage = "비밀번호가 맞지 않습니다.";
        } else if (appUser.getGrantStateEnumValue() == USER_REGISTER_TYPE.ApplicationType) {
            auth = false;
            errorCode = "A"; // 허가안 된 사용자
            errorMessage = "사용자 등록 진행중인 사용자 입니다.";
        } else if (appUser.getGrantStateEnumValue() == USER_REGISTER_TYPE.RetiredType) {
            auth = false;
            errorCode = "R"; // 만기된 사용자
            errorMessage = "만기된 사용자 입니다.";
        }
    }
    System.out.println(">>> Final auth status: " + auth);
    System.out.println(">>> Final errorCode: " + errorCode);
    System.out.println(">>> Final errorMessage: " + errorMessage);
    System.out.println("=== auth_check_s3_ajx_v2.jsp DEBUG END ===");

    //auth = true;
    if (auth) {
        // ✅ Redis에 사용자 정보 저장
        String sessionToken = "";
        try {
            sessionToken = UUID.randomUUID().toString();
            RedisUtil.saveUserToRedis(sessionToken, appUser);
        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }

        //todo: ####################
        //todo: setLatestLoginDate
        //todo: ####################
        String areaSeq2 = appUser.getAreaSeq();
        int lastLoginResult = BizAppUser.getInstance().setLatestLoginDate(appUser.getMacNumber(), areaSeq2);
        System.out.println(lastLoginResult);

//		// ✅ 직원 코드 추가 저장
//		String employeeCodes = "EMP12345";
//		RedisUtil.saveToRedis(sessionToken, "EMPLOYEE_CODE", employeeCodes);

        // ✅ XML 응답 생성 (`sessionToken` 포함)
//        String xml = "<AuthCheck>"
//                + "<result>Y</result>"
//                + "<sessionToken>" + sessionToken + "</sessionToken>"
//                + "</AuthCheck>";

        // ✅ XML 응답 생성 (`sessionToken` 포함)
        String xml = "<AuthCheck>"
                + "<result>Y</result>"
                + "<sessionToken>" + sessionToken + "</sessionToken>"
                + "<areaCode>" + StringUtil.nullToBlank(appUser.getAreaCode()) + "</areaCode>"
                + "<gasType>" + StringUtil.nullToBlank(appUser.getGasType()) + "</gasType>"
                + "<signImagePath>" + StringUtil.nullToBlank(appUser.getSignImagePath()) + "</signImagePath>"
                + "<menuPermission>" + StringUtil.nullToBlank(appUser.getMenuPermission().trim()) + "</menuPermission>"
                + "</AuthCheck>";


        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        out.print(xml);
    } else {
        String xml = "<AuthCheck><result>N</result><errorCode>" + errorCode + "</errorCode><errorMessage>" + errorMessage + "</errorMessage></AuthCheck>";
        out.print(xml);
    }
%>
