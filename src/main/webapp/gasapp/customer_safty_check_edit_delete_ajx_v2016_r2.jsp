<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSaftyCheck" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	try{
		String customerCode = request.getParameter("customerCode");
		String sequenceNumber = request.getParameter("sequenceNumber");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			BizCustomerSaftyCheck.getInstance().deleteCustomerSaftyCheck(serverIp, catalogName, areaCode, customerCode, sequenceNumber);
			// 서명 삭제
			BizCustomerSaftyCheck.getInstance().deleteCustomerSaftyCheckSignature(serverIp, catalogName, areaCode, customerCode, sequenceNumber);
			// 안전점검 내역 세션을 삭제
			CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
			keys.put("AREA_CODE", areaCode);
			keys.put("ANZ_Cu_Code", customerCode);
			keys.put("ANZ_Sno", sequenceNumber);
			customerSaftyChecks.removeCustomerSaftyCheck(StringUtil.getKeyValue(keys));
			
			out.println("<result><code>S</code><message>삭제를 완료하였습니다.</message></result>");
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>삭제 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>