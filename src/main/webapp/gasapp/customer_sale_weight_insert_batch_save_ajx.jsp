<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerWeightSale" %>
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
		String insertMode = request.getParameter("insertMode"); // "1": insert, "0": update
		String saleType = request.getParameter("saleType");
		String saleDate = request.getParameter("saleDate");
		saleDate = StringUtil.stringReplace(saleDate); // 날짜에서 "-"를 모두 제거
		String itemCode = request.getParameter("itemCode");
		String itemName = request.getParameter("itemName");
		String itemSpec = request.getParameter("itemSpec");
		String saleQuantity = request.getParameter("saleQuantity");
		String withdrawQuantity = request.getParameter("withdrawQuantity");
		String salePrice = request.getParameter("salePrice");
		String priceType = request.getParameter("priceType");
		String vatType = request.getParameter("vatType");
		String saleAmount = request.getParameter("saleAmount");
		String taxAmount = request.getParameter("taxAmount");
		String totalAmount = request.getParameter("totalAmount");
		String discountAmount = request.getParameter("discountAmount");
		String collectAmount = request.getParameter("collectAmount");
		String unpaidAmount = request.getParameter("unpaidAmount");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String remark = request.getParameter("remark");
		String collectType = request.getParameter("collectType");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String registerDate = StringUtil.nowDateTimeStr();
//			String userId = appUser.getUserId();
			String userId = appUser.getMobileNumber();
			String gasType = appUser.getGasType();

			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			String customerCode = customerSearch.getCustomerCode();
			String customerName = customerSearch.getCustomerName();
			String userName = customerSearch.getUserName();

			String result = "";
			if ("1".equals(insertMode)){ //체적공급 신규 등록일 경우
				if ("HIGH".equals(gasType)){ //고압일 때
					result = BizCustomerWeightSale.getInstance().insertCustomerWeightSale(serverIp, catalogName, areaCode, customerCode, saleType, saleDate, customerName, userName, itemCode, itemName, itemSpec, saleQuantity, withdrawQuantity, salePrice, priceType, vatType, saleAmount, taxAmount, totalAmount, discountAmount, collectAmount, unpaidAmount, employeeCode, employeeName, remark, collectType, registerDate, userId);
				} else { // LPG일 때
					result = BizCustomerWeightSale.getInstance().insertCustomerWeightSaleLPG(serverIp, catalogName, areaCode, customerCode, saleType, saleDate, customerName, userName, itemCode, itemName, itemSpec, saleQuantity, withdrawQuantity, salePrice, priceType, vatType, saleAmount, taxAmount, totalAmount, discountAmount, collectAmount, unpaidAmount, employeeCode, employeeName, remark, collectType, registerDate, userId);
				}
			} else { // 체적공급 수정일 경우

			}

			// 거래처 정보 다시 가져오기
			String keyword = "";
			String customerSearchType = "A";
			String grantCode = appUser.getMenuPermission();
			CustomerSearchMap customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode);
			if (customerSearches!=null){
				customerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
				if (customerSearch!=null){
					//session.setAttribute("CURRENT_CUSTOMER", customerSearch);
					sessionToken = request.getParameter("sessionToken");
					RedisUtil.saveToRedis(sessionToken, "CURRENT_CUSTOMER", customerSearch);
					CustomerSearchMap sessionCustomerSearches = (CustomerSearchMap)session.getAttribute("CUSTOMER_SEARCH");
					sessionCustomerSearches.setCustomerSearch(customerSearch);
				}
			}
			out.println("<result><code>S</code><message>" + result + "</message></result>");
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>
