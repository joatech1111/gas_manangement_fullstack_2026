<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizCollectList" %>
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
		String customerCode = request.getParameter("customerCode");
		String collectType = request.getParameter("collectType");
		String collectDate = request.getParameter("collectDate");
		collectDate = StringUtil.stringReplace(collectDate);
		String buildingName = request.getParameter("buildingName");
		String userName = request.getParameter("userName");
		String collectAmount = request.getParameter("collectAmount");
		String discountAmount = request.getParameter("discountAmount");
		String collectMethodType = request.getParameter("collectMethodType");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String remark = request.getParameter("remark");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String mobileNumber = appUser.getMobileNumber();
			String registerDate = StringUtil.nowDateTimeStr();
			// 거래처 신규 등록 저장
			String message = BizCollectList.getInstance().insertCollectList(serverIp, catalogName, areaCode, customerCode, collectType, collectDate, buildingName, userName, collectAmount, discountAmount, collectMethodType, employeeCode, employeeName, remark, registerDate, mobileNumber);
			
			System.out.println("수금조건:" + catalogName );
			System.out.println("수금처리:" + message);
			
			// 거래처 정보 다시 가져오기
			String keyword = "";
			String customerSearchType = "A";
			String grantCode = appUser.getMenuPermission();
			CustomerSearch customerSearch = null;


			CustomerSearchMap customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode);
			
			
			if (customerSearches!=null){
				customerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
     			System.out.println("거래처 정보:" + customerCode);

				if (customerSearch!=null){
					
					//session.setAttribute("CURRENT_CUSTOMER", customerSearch);
					String sessionToken = request.getParameter("sessionToken");
					RedisUtil.saveToRedis(sessionToken, "CURRENT_CUSTOMER", customerSearch);
					CustomerSearchMap sessionCustomerSearches = (CustomerSearchMap)session.getAttribute("CUSTOMER_SEARCH");
					
					if (sessionCustomerSearches !=null) {
						sessionCustomerSearches.setCustomerSearch(customerSearch);
					}
				}
				
     			System.out.println("거래처 정보 종료  -----------------------------------" );
				
			}
			
			String code = message.startsWith("E")?"E":"S";
			String xml = "<result><code>" + code + "</code><message>" + message + "</message></result>";
			out.print(xml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
