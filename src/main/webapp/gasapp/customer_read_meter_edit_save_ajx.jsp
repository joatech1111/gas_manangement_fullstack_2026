<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
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
	try{
		String insertMode = request.getParameter("insertMode"); // "1": insert, "0": update
		String customerCode = request.getParameter("customerCode");
		String sequenceNumber = request.getParameter("sequenceNumber");
		sequenceNumber = StringUtil.stringReplace(sequenceNumber);
		String readMeterDate = request.getParameter("readMeterDate");
		readMeterDate = StringUtil.stringReplace(readMeterDate);
		String customerName = request.getParameter("customerName");
		String userName = request.getParameter("userName");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String preMonthReadMeter = request.getParameter("preMonthReadMeter");
		String thisMonthReadMeter = request.getParameter("thisMonthReadMeter");
		String useQuantity = request.getParameter("useQuantity");
		String price = request.getParameter("price");
		String useAmount = request.getParameter("useAmount");
		String managementAmount = request.getParameter("managementAmount");
		String discountAmount = request.getParameter("discountAmount");
		String delayAmount = request.getParameter("delayAmount");
		String thisMonthAmount = request.getParameter("thisMonthAmount");
		String preRemain = request.getParameter("preRemain");
		String nowRemain = request.getParameter("nowRemain");
		String remark = request.getParameter("remark");
		String startDate = request.getParameter("startDate");
		startDate = StringUtil.stringReplace(startDate);
		String endDate = readMeterDate;
		String preUnpaidAmount = request.getParameter("preUnpaidAmount");
		String applyDelayDate = StringUtil.stringReplace(request.getParameter("applyDelayDate"));
		String defaultDelayAmount = request.getParameter("defaultDelayAmount");
		String defaultAmount = request.getParameter("defaultAmount");
		String defaultAmountYesNo = request.getParameter("defaultAmountYesNo");
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		
		
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String registerDate = StringUtil.nowDateTimeStr();
//			String userId = appUser.getUserId();
			String userId = appUser.getMobileNumber();
			String result = "";
			
			System.out.println("검침등록:" + catalogName + ", insertMode :" + insertMode);

			if ("1".equals(insertMode)){ // 체적검침 신규 등록일 경우
				result = BizCustomerVolumeReadMeter.getInstance().insertCustomerReadMeter(serverIp, catalogName, areaCode, customerCode, sequenceNumber, readMeterDate, customerName, userName, employeeCode, employeeName, preMonthReadMeter, thisMonthReadMeter, useQuantity, price, useAmount, managementAmount, discountAmount, delayAmount, thisMonthAmount, preRemain, nowRemain, remark, startDate, endDate, preUnpaidAmount, applyDelayDate, defaultDelayAmount, defaultAmountYesNo, defaultAmount, registerDate, userId);
				
				System.out.println("체적검침 신규 등록 :" + catalogName + ", thisMonthReadMeter :" + thisMonthReadMeter + ",거래처코드:" + customerCode +  ",result : " + result );
				
				
			} else { // 체적검침 수정일 경우

			}
			// 거래처 정보 다시 가져오기
			String keyword = "";
			String customerSearchType = "A";
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
					
					if(sessionCustomerSearches != null){
						sessionCustomerSearches.setCustomerSearch(customerSearch);
					}
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