<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeSale" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeSaleMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeSale" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
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
	try{
		String key = request.getParameter("key");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userId = appUser.getMobileNumber();
			String transactionDate = StringUtil.nowDateTimeStr();
			//CustomerVolumeSaleMap customerVolumeSales = (CustomerVolumeSaleMap)session.getAttribute("CUSTOMER_BOOK_VOLUME");


			String sessionToken = request.getParameter("sessionToken"); // 세션 토큰 전달 필요
			CustomerVolumeSaleMap customerVolumeSales = RedisUtil.getFromRedis(sessionToken, "CUSTOMER_BOOK_VOLUME", CustomerVolumeSaleMap.class);





			CustomerVolumeSale customerVolumeSale = customerVolumeSales.getCustomerVolumeSale(key);
			String customerCode = customerVolumeSale.getCustomerCode();
			String saleDate = customerVolumeSale.getSaleDate();
			String sequenceNumber = customerVolumeSale.getSequenceNumber();
			String message = BizCustomerVolumeSale.getInstance().deleteCustomerVolumeSale(serverIp, catalogName, areaCode, customerCode, saleDate, sequenceNumber, transactionDate, userId);
			// 거래처 거래내역 세션을 삭제
			customerVolumeSales.removeCustomerVolumeSale(key);
			// 거래처 정보 다시 가져오기
			String keyword = "";
			String customerSearchType = "A";
			String employeeCode = appUser.getEmployeeCode();
			String grantCode = appUser.getMenuPermission();
			CustomerSearch customerSearch = null;
			CustomerSearchMap customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode);
			if (customerSearches!=null){
				customerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
				if (customerSearch!=null){
					//session.setAttribute("CURRENT_CUSTOMER", customerSearch);
					String sessionToken = request.getParameter("sessionToken");
					RedisUtil.saveToRedis(sessionToken, "CURRENT_CUSTOMER", customerSearch);
					CustomerSearchMap sessionCustomerSearches = (CustomerSearchMap)session.getAttribute("CUSTOMER_SEARCH");
					sessionCustomerSearches.setCustomerSearch(customerSearch);
				}
			}
			String code = message.startsWith("E")?"E":"S";
			out.println("<result><code>"+ code + "</code><message>" + message + "</message></result>");
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>삭제 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>
