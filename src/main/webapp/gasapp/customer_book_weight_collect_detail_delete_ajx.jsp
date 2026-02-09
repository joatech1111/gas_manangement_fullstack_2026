<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerWeightCollectMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerWeightSale" %>
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
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userId = appUser.getMobileNumber();
			String transactionDate = StringUtil.nowDateTimeStr();
			String gasType = appUser.getGasType();
			CustomerWeightCollectMap customerWeightCollects = (CustomerWeightCollectMap)session.getAttribute("CUSTOMER_BOOK_WEIGHT");
			CustomerWeightCollect customerWeightCollect = customerWeightCollects.getCustomerWeightCollect(key);
			String customerCode = customerWeightCollect.getCustomerCode();
			String typeCode = customerWeightCollect.getTypeCode(); //0.가스, 1.용기, 2.기구, 4.A/S, 5.수금
			String collectType = "J"; //J:일반 C:체적
			String collectDate = customerWeightCollect.getCollectDate();
			String sequenceNumber = customerWeightCollect.getSequenceNumber();
			String message = "";
			if ("5".equals(typeCode)){ // 수금 삭제
				message = BizCustomerWeightCollect.getInstance().deleteCustomerCollect(serverIp, catalogName, areaCode, customerCode, collectType, collectDate, sequenceNumber, transactionDate, userId);
			} else if (("0".equals(typeCode)) || ("1".equals(typeCode))  || ("2".equals(typeCode)) ){ //판매 삭제
				if ("HIGH".equals(gasType)){
					message = BizCustomerWeightSale.getInstance().deleteCustomerWeightSaleHPG(serverIp, catalogName, areaCode, customerCode, typeCode, collectDate, sequenceNumber, transactionDate, userId);
				} else {
					message = BizCustomerWeightSale.getInstance().deleteCustomerWeightSaleLPG(serverIp, catalogName, areaCode, customerCode, typeCode, collectDate, sequenceNumber, transactionDate, userId);
				}
			}
			// 거래처 거래내역 세션을 삭제
			customerWeightCollects.removeCustomerWeightCollect(key);
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