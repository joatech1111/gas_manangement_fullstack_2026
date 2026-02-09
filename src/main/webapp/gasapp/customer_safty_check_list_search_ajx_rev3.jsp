<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckListMap" %>
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
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CustomerSaftyCheckListMap customerSaftyCheckLists = BizCustomerSaftyCheck.getInstance().getCustomerSaftyCheckList(serverIp, catalogName, areaCode, customerCode, null);
			//session.setAttribute("CUSTOMER_SAFTY_CHECK_LIST", customerSaftyCheckLists);
			if (customerSaftyCheckLists!=null){
				out.print(customerSaftyCheckLists.toXML());
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>