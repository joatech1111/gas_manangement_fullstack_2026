<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerLatestReadMeter" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeReadMeter" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String readMeterDate = request.getParameter("readMeterDate");
	try{
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
	
			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			if (customerSearch != null){
				String customerCode = customerSearch.getCustomerCode();
				String customerName = customerSearch.getCustomerName();
				if (("".equals(readMeterDate)) || (readMeterDate == null)){
					readMeterDate = StringUtil.dateFormatStr(null);
				}
				// 최근 검침 내역 가져오기
				CustomerLatestReadMeter customerLatestReadMeter = BizCustomerVolumeReadMeter.getInstance().getCustomerLatestReadMeter(serverIp, catalogName, areaCode, customerCode, readMeterDate);
				String xml = customerLatestReadMeter.toXML();
				out.println(xml);
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>