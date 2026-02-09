<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CidList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CidListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCidList" %>
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
		String key = request.getParameter("key");
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		if ("NA".equals(employeeCode)){
			employeeCode = "";
			employeeName = "";
		}
		String deliveryYesNo = request.getParameter("deliveryYesNo");
		String completeYesNo =request.getParameter("completeYesNo");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String registerDate = StringUtil.nowDateTimeStr();
			String mobileNumber = appUser.getMobileNumber();
			String gasType = appUser.getGasType();
			
			CidListMap cidLists = (CidListMap)session.getAttribute("CID_LIST");
			CidList cidList = cidLists.getCidList(key);
			String cidDate = cidList.getCidDate();
			String sequenceNumber = cidList.getSequenceNumber();
			String saleType = cidList.getSaleTypeCode();
			String customerCode = cidList.getCustomerCode();
			String customerName = cidList.getCustomerName();
			String phoneNumber = cidList.getPhoneNumber();
			String itemCode = cidList.getItemCode();
			String itemName = cidList.getItemName();
			String saleQuantity = cidList.getSaleQuantity();
			String withdrawQuantity = cidList.getWithdrawQuantity();
			String salePrice = cidList.getCidPrice();
			String priceType = "1";
			String vatType = cidList.getVatType();
			String saleAmount = cidList.getCidAmount();
			String taxAmount = cidList.getTaxAmount();
			String totalAmount = cidList.getTotalAmount();
			String discountAmount = cidList.getDiscountAmount();
			String collectAmount = cidList.getCollectAmount();
			String unpaidAmount = cidList.getUnpaidAmount();
			String remark = cidList.getRemark();
			String collectType = cidList.getCollectType();
			
			String result = ""; 
			result = BizCidList.getInstance().updateCidList(serverIp, catalogName, areaCode, sequenceNumber, cidDate, saleType, customerCode, customerName, phoneNumber, itemCode, itemName, saleQuantity, withdrawQuantity, salePrice, priceType, vatType, saleAmount, taxAmount, totalAmount, discountAmount, collectAmount, unpaidAmount, employeeCode, employeeName, remark, deliveryYesNo, completeYesNo, collectType, registerDate, mobileNumber, gasType);
			// 저장 후 세션 업데이트
			cidList.setEmployeeCode(employeeCode);
			cidList.setEmployeeName(employeeName);
			cidList.setDeliveryYesNo("true".equals(deliveryYesNo)?"1":"0");
			cidList.setCompleteYesNo("true".equals(completeYesNo)?"1":"0");
			HashMap<String, String> pagingXML = new HashMap<String, String>(cidLists.toPagingXML(5000));
			session.setAttribute("CID_LIST_PAGING", pagingXML);
			
			out.println("<result><code>S</code><message>" + result + "</message></result>");
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>