<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.list.AppUserMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.HashMap" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%>
<%
try{
	String macNumber = request.getParameter("macNumber");
	String phoneNumber = request.getParameter("phoneNumber");

	System.out.println(phoneNumber);
	System.out.println(phoneNumber);
	System.out.println(phoneNumber.trim());
	phoneNumber = phoneNumber.trim();

	if (macNumber != null) macNumber = StringUtil.decodeBase64(macNumber);

//	AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, macNumber);
//AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, macNumber);
	AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));

	System.out.println(appUser);

	if (appUser != null){
		String grantState = appUser.getGrantState();
		if ("Y".equals(grantState)) {
			out.print("<Result><code>E</code><description>이미 가입된 핸드폰 입니다.</description></Result>");
		} else if ("N".equals(grantState)) {
			out.print("<Result><code>E</code><description> 아직 회원가입 등록절차가 진행 중입니다.</description></Result>");
		} else if ("X".equals(grantState)) {
			out.print("<Result><code>E</code><description>서비스가 중지된 핸드폰 입니다.</description></Result>");
		}
	} else {
		String mobileNumber = request.getParameter("mobileNumber");
		if (mobileNumber != null) {
			mobileNumber = StringUtil.decodeBase64(mobileNumber);
		}
		String areaName = request.getParameter("areaName");
		String employeeName = request.getParameter("employeeName");
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		if (password != null) password = StringUtil.decodeBase64(password);
		String phoneAreaNumber = request.getParameter("phoneAreaNumber");
		String result = String.valueOf(BizAppUser.getInstance().appendAppUser(macNumber, "2016", phoneNumber, areaName, employeeName, userId, password, phoneAreaNumber));
		out.print("<Result><code>N</code><description>" + result + "</description></Result>");
	}
} catch (Exception e) {
	out.print("<Result><code>N</code><description>회원가입 신청 중에 오류가 발생하였습니다.</description></Result>");
	e.printStackTrace();
}
%>
