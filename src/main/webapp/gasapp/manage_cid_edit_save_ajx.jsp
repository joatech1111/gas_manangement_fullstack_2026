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
		String insertMode = request.getParameter("insertMode"); // "1": insert, "0": update
		String key = request.getParameter("key");
		String cidDate = request.getParameter("cidDate");
		cidDate = StringUtil.stringReplace(cidDate);
		if ("1".equals(insertMode)){
			cidDate = StringUtil.dateFormatStr(null, "");
		}
		String sequenceNumber = request.getParameter("sequenceNumber");
		String saleType = request.getParameter("saleType");
		String customerCode = request.getParameter("customerCode");
		String customerName = request.getParameter("customerName");
		String phoneNumber = request.getParameter("phoneNumber");
		String itemCode = request.getParameter("itemCode");
		String itemName = request.getParameter("itemName");
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
		String deliveryYesNo = request.getParameter("deliveryYesNo");
		String completeYesNo = request.getParameter("completeYesNo");
		String collectType = request.getParameter("collectType");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String registerDate = StringUtil.nowDateTimeStr();
			String mobileNumber = appUser.getMobileNumber();
			String gasType = appUser.getGasType();
			String result = ""; 
			if ("1".equals(insertMode)){ //CID 신규 등록일 경우
				sequenceNumber = BizCidList.getInstance().insertCidList(serverIp, catalogName, areaCode, cidDate, saleType, customerCode, customerName, phoneNumber, itemCode, itemName, saleQuantity, withdrawQuantity, salePrice, priceType, vatType, saleAmount, taxAmount, totalAmount, discountAmount, collectAmount, unpaidAmount, employeeCode, employeeName, remark, deliveryYesNo, completeYesNo, collectType, registerDate, mobileNumber, gasType);
				result = sequenceNumber;
			} else { // CID 수정일 경우 저장한 다음 세션 업데이트
			
				System.out.println(mobileNumber + "명칭:" + catalogName + "," + customerCode + "," + customerName );
			
				result = BizCidList.getInstance().updateCidList(serverIp, catalogName, areaCode, sequenceNumber, cidDate, saleType, customerCode, customerName, phoneNumber, itemCode, itemName, saleQuantity, withdrawQuantity, salePrice, priceType, vatType, saleAmount, taxAmount, totalAmount, discountAmount, collectAmount, unpaidAmount, employeeCode, employeeName, remark, deliveryYesNo, completeYesNo, collectType, registerDate, mobileNumber, gasType);
				CidListMap cidLists = (CidListMap)session.getAttribute("CID_LIST");
				CidList cidList = cidLists.getCidList(key);
				cidList.setCustomerCode(customerCode);
				cidList.setCustomerName(customerName);
				cidList.setSaleTypeCode(saleType);
				cidList.setPhoneNumber(phoneNumber);
				cidList.setItemCode(itemCode);
				cidList.setItemName(itemName);
				cidList.setSaleQuantity(saleQuantity);
				cidList.setWithdrawQuantity(withdrawQuantity);
				cidList.setCidPrice(salePrice);
				cidList.setVatType(vatType);
				cidList.setCidAmount(saleAmount);
				cidList.setTaxAmount(taxAmount);
				cidList.setTotalAmount(totalAmount);
				cidList.setDiscountAmount(discountAmount);
				cidList.setCollectAmount(collectAmount);
				cidList.setUnpaidAmount(unpaidAmount);
				cidList.setEmployeeCode(employeeCode);
				cidList.setEmployeeName(employeeName);
				cidList.setRemark(remark);
				cidList.setDeliveryYesNo("true".equals(deliveryYesNo)?"1":"0");
				cidList.setCompleteYesNo("true".equals(completeYesNo)?"1":"0");
				cidList.setCollectType(collectType);
				HashMap<String, String> pagingXML = new HashMap<String, String>(cidLists.toPagingXML(5000));
				session.setAttribute("CID_LIST_PAGING", pagingXML);
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