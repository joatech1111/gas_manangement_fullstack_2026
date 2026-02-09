<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
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
//		String areaCode = request.getParameter("areaCode");
		String customerCode = request.getParameter("customerCode");
		String sequenceNumber = request.getParameter("sequenceNumber");
		String signatureFilePath = request.getParameter("signatureFilePath");
		String signatureFileName = request.getParameter("signatureFileName");
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CustomerSaftyCheck resultCustomerSaftyCheck = BizCustomerSaftyCheck.getInstance().updateSignInfo(serverIp, catalogName, areaCode, customerCode, sequenceNumber, signatureFilePath, signatureFileName);
			
			// 안전점검 내역 세션을 최신으로 업데이트하거나 신규 등록
			CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			customerSaftyChecks.setCustomerSaftyCheck(resultCustomerSaftyCheck);
			out.println(resultCustomerSaftyCheck.toXML());
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>