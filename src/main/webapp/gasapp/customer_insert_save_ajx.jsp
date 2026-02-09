<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
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
		String customerType = request.getParameter("customerType");
		String customerName = request.getParameter("customerName");
		String userName = request.getParameter("userName");
		String phoneNumber = request.getParameter("phoneNumber");
		String mobileNumber = request.getParameter("mobileNumber");
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String remark1 = request.getParameter("remark1");
		String remark2 = request.getParameter("remark2");
		String consumeTypeCode = request.getParameter("consumeTypeCode");
		
		String phoneNumberFind = StringUtil.onlyNumber(phoneNumber);
		String mobileNumberFind = StringUtil.onlyNumber(mobileNumber);

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String keyword = "";
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String customerCode = "";
			String customerSearchType = "A";
			String employeeCode = appUser.getEmployeeCode();
			String grantCode = appUser.getMenuPermission();
			String employeeName = appUser.getEmployeeName();
			String appUserMobileNumber = appUser.getMobileNumber();
			
			//거래처 저장을 위한 데이타 모델 설정
			CustomerSearch customerSearch = new CustomerSearch();
			customerSearch.setAreaCode(areaCode);
			customerSearch.setCustomerType(customerType);
			customerSearch.setCustomerName(customerName);
			customerSearch.setUserName(userName);
			customerSearch.setPhoneNumber(phoneNumber);
			customerSearch.setPhoneNumberFind(phoneNumberFind);
			customerSearch.setMobileNumber(mobileNumber);
			customerSearch.setMobileNumberFind(mobileNumberFind);
			customerSearch.setAddress1(address1);
			customerSearch.setAddress2(address2);
			customerSearch.setRemark1(remark1);
			customerSearch.setRemark2(remark2);
			customerSearch.setEmployeeCode(employeeCode);
			customerSearch.setEmployeeName(employeeName);
			customerSearch.setConsumeTypeCode(consumeTypeCode);
			
			// 거래처 신규 등록 저장
			String newCustomerCode = BizCustomerSearch.getInstance().insertCustomerSearch(serverIp, catalogName, appUserMobileNumber, customerSearch);
			// 거래처 정보 다시 가져오기
			CustomerSearchMap customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode);
			CustomerSearch newCustomerSearch = customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, newCustomerCode));
			//session.setAttribute("CURRENT_CUSTOMER", newCustomerSearch);
			String sessionToken = request.getParameter("sessionToken");
			RedisUtil.saveToRedis(sessionToken, "CURRENT_CUSTOMER", newCustomerSearch);

			if (newCustomerSearch != null){
				String xml = newCustomerSearch.toXML().replace("&", "&amp;");
				//newCustomerSearch.toXML().replace("&", "&amp;");
				out.print(xml);
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
