<?xml version="1.0" encoding="UTF-8"?>

<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8" session="true" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.attribute.USER_REGISTER_TYPE" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.UUID" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%@ page import="redis.clients.jedis.Jedis" %>


<%
    /* =========================
       Response / Request 설정
       ========================= */
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    if ("HTTP/1.1".equals(request.getProtocol())) {
        response.setHeader("Cache-Control", "no-cache");
    }

    request.setCharacterEncoding("UTF-8");

    /* =========================
       파라미터 수신
       ========================= */
    String uuid = request.getParameter("uuid");
    String _uuid = request.getParameter("uuid");

    if (_uuid != null) {
        _uuid = StringUtil.decodeBase64(_uuid);
    }

    String areaCode = request.getParameter("areaCode");
    String userId   = request.getParameter("loginId");
    String password = request.getParameter("loginPw");

    if (password != null) {
        password = StringUtil.decodeBase64(password);
    }

    String userKeyValue = StringUtil.getKeyValue(uuid);

    Gson gson = new Gson();

    boolean auth = true;
    String errorCode = "";
    String errorMessage = "";

    AppUser appUser = new AppUser();

    /* =========================
       입력값 검증
       ========================= */
    if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
        auth = false;
        errorCode = "N";
        errorMessage = "아이디 또는 비밀번호가 입력되지 않았습니다.";
    } else {

        if ("test".equals(userId) && "test".equals(password)) {

            appUser = BizAppUser.getInstance()
                    .getAppUser_id_pwd(
                            BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,
                            userId.trim(),
                            password
                    );

        } else {

            String svrDbName = request.getParameter("svrDbName");

            if (svrDbName != null && !svrDbName.isEmpty() && !"null".equals(svrDbName)) {

                appUser = BizAppUser.getInstance()
                        .getAppUser_id_pwd_uuid_dbCatalogName(
                                BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,
                                userId.trim(),
                                password,
                                uuid,
                                svrDbName
                        );

            } else if (areaCode != null && !areaCode.isEmpty() && !"0".equals(areaCode)) {

                appUser = BizAppUser.getInstance()
                        .getAppUser_id_pwd_uuid_areaCode(
                                BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,
                                userId.trim(),
                                password,
                                uuid,
                                areaCode
                        );

            } else {

                appUser = BizAppUser.getInstance()
                        .getAppUser_id_pwd_uuid(
                                BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,
                                userId.trim(),
                                password,
                                uuid
                        );
            }
        }

        /* =========================
           UUID 체크 (test 계정 제외)
           ========================= */
        if (!("test".equals(userId) && "test".equals(password))) {
            if (uuid == null || uuid.isEmpty() || "null".equals(uuid)) {
                auth = false;
                errorCode = "M";
                errorMessage = "모바일 기기 정보를 가져오는데 실패했습니다.\n앱을 다시 실행해주세요.";
            }
        }

        /* =========================
           사용자 검증
           ========================= */
        if (appUser == null) {
            auth = false;
            errorCode = "H";
            errorMessage = "등록되지 않은 사용자 입니다.";
        } else if (!userId.equals(appUser.getUserId())) {
            auth = false;
            errorCode = "E";
            errorMessage = "사용자명이 맞지 않습니다.";
        } else if (!password.equals(appUser.getPassword())) {
            auth = false;
            errorCode = "C";
            errorMessage = "비밀번호가 맞지 않습니다.";
        } else if (appUser.getGrantStateEnumValue() == USER_REGISTER_TYPE.ApplicationType) {
            auth = false;
            errorCode = "A";
            errorMessage = "사용자 등록 진행중인 사용자 입니다.";
        } else if (appUser.getGrantStateEnumValue() == USER_REGISTER_TYPE.RetiredType) {
            auth = false;
            errorCode = "R";
            errorMessage = "만기된 사용자 입니다.";
        }
    }

    /* =========================
       인증 성공
       ========================= */
    if (auth) {

        String sessionToken = "";

        try {
            sessionToken = UUID.randomUUID().toString();
            RedisUtil.saveUserToRedis(sessionToken, appUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String areaSeq = appUser.getAreaSeq();
        BizAppUser.getInstance()
                .setLatestLoginDate(appUser.getMacNumber(), areaSeq);

        String xml =
                "<AuthCheck>" +
                        "<result>Y</result>" +
                        "<sessionToken>" + sessionToken + "</sessionToken>" +
                        "<areaCode>" + StringUtil.nullToBlank(appUser.getAreaCode()) + "</areaCode>" +
                        "<gasType>" + StringUtil.nullToBlank(appUser.getGasType()) + "</gasType>" +
                        "<signImagePath>" + StringUtil.nullToBlank(appUser.getSignImagePath()) + "</signImagePath>" +
                        "<menuPermission>" + StringUtil.nullToBlank(appUser.getMenuPermission()).trim() + "</menuPermission>" +
                        "</AuthCheck>";

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        out.print(xml);

    } else {

        String xml =
                "<AuthCheck>" +
                        "<result>N</result>" +
                        "<errorCode>" + errorCode + "</errorCode>" +
                        "<errorMessage>" + errorMessage + "</errorMessage>" +
                        "</AuthCheck>";

        out.print(xml);
    }
%>
