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
%><%
try{
	String macNumber = request.getParameter("macNumber");

	if (macNumber != null) macNumber = StringUtil.decodeBase64(macNumber);
	AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
	appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
	if (appUser != null){
		String areaCode = request.getParameter("areaCode");
		String areaName = request.getParameter("areaName");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String phoneAreaNumber = request.getParameter("phoneAreaNumber");
		String address = request.getParameter("address");
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		if (password != null) password = StringUtil.decodeBase64(password);
		String menuPermission = request.getParameter("menuPermission");
		String gasType = request.getParameter("gasType");
		int result = BizAppUser.getInstance().setAppUser(macNumber, areaCode, areaName, employeeCode, employeeName, phoneAreaNumber, address, userId, password, menuPermission, gasType);
		appUser.setAreaCode(areaCode);
		appUser.setAreaName(areaName);
		appUser.setEmployeeCode(employeeCode);
		appUser.setEmployeeName(employeeName);
		appUser.setPhoneAreaNumber(phoneAreaNumber);
		appUser.setAreaAddress(address);
		appUser.setUserId(userId);
		appUser.setPassword(password);
		appUser.setMenuPermission(menuPermission);
		appUser.setGasType(gasType);
		out.print("<Result><code>S</code><description>저장을 완료하였습니다.</description></Result>");
	}else{
		out.print("<session>X</session>");
	}
} catch (Exception e) {
	out.print("<Result><code>N</code><description>환경설정 저장 중에 오류가 발생하였습니다.</description></Result>");
	e.printStackTrace();
}
%>
