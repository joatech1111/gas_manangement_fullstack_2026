<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeSale" %>
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
		String insertMode = request.getParameter("insertMode"); // "1": insert, "0": update
		String customerCode = request.getParameter("customerCode");
		String saleDate = request.getParameter("saleDate");
		saleDate = StringUtil.stringReplace(saleDate);
		String buildingName = request.getParameter("buildingName");
		String itemCode = request.getParameter("itemCode");
		String itemName = request.getParameter("itemName");
		String itemCapacity = request.getParameter("itemCapacity");
		String salePrice = request.getParameter("salePrice");
		String saleQuantity = request.getParameter("saleQuantity");
		String withdrawQuantity = request.getParameter("withdrawQuantity");
		String saleAmount = request.getParameter("saleAmount");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String cubicPrice = request.getParameter("cubicPrice");
		String readMeterQuantity = request.getParameter("readMeterQuantity");
		String remainQuantity = request.getParameter("remainQuantity");
		String remark = request.getParameter("remark");

		String uuid = request.getParameter("uuid");


		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String registerDate = StringUtil.nowDateTimeStr();
//			String userId = appUser.getUserId();
			String userId = appUser.getMobileNumber();

			String result = "";
			if ("1".equals(insertMode)){ //체적공급 신규 등록일 경우
				result = BizCustomerVolumeSale.getInstance().insertCustomerVolumeSale(serverIp, catalogName, areaCode, customerCode, saleDate, buildingName, itemCode, itemName, itemCapacity, salePrice, saleQuantity, withdrawQuantity, saleAmount, employeeCode, employeeName, cubicPrice, readMeterQuantity, remainQuantity, remark, registerDate, userId);
			} else { // 체적공급 수정일 경우

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
