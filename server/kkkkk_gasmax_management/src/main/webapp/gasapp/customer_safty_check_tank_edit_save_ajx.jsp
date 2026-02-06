<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckTank" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckList" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckSignature" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckTankMap" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckListMap" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckSignatureMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSaftyCheck" %>
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
		String scheduledCheckDate = request.getParameter("scheduledCheckDate");
		scheduledCheckDate = StringUtil.stringReplace(scheduledCheckDate);
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String tankCapacity1 = request.getParameter("tankCapacity1");
		String tankCapacity2 = request.getParameter("tankCapacity2");
		String acceptable1 = request.getParameter("acceptable1");
		String acceptable1Comment = request.getParameter("acceptable1Comment");
		String acceptable2 = request.getParameter("acceptable2");
		String acceptable2Comment = request.getParameter("acceptable2Comment");
		String acceptable3 = request.getParameter("acceptable3");
		String acceptable3Comment = request.getParameter("acceptable3Comment");
		String acceptable4 = request.getParameter("acceptable4");
		String acceptable4Comment = request.getParameter("acceptable4Comment");
		String acceptable5 = request.getParameter("acceptable5");
		String acceptable5Comment = request.getParameter("acceptable5Comment");
		String acceptable6 = request.getParameter("acceptable6");
		String acceptable6Comment = request.getParameter("acceptable6Comment");
		String acceptable7 = request.getParameter("acceptable7");
		String acceptable7Comment = request.getParameter("acceptable7Comment");
		String acceptable8 = request.getParameter("acceptable8");
		String acceptable8Comment = request.getParameter("acceptable8Comment");
		String acceptable9 = request.getParameter("acceptable9");
		String acceptable9Comment = request.getParameter("acceptable9Comment");
		String acceptable10Content = request.getParameter("acceptable10Content");
		String acceptable10 = request.getParameter("acceptable10");
		String acceptable10Comment = request.getParameter("acceptable10Comment");
		String acceptable11Content = request.getParameter("acceptable11Content");
		String acceptable11 = request.getParameter("acceptable11");
		String acceptable11Comment = request.getParameter("acceptable11Comment");
		String acceptable12Content = request.getParameter("acceptable12Content");
		String acceptable12 = request.getParameter("acceptable12");
		String acceptable12Comment = request.getParameter("acceptable12Comment");
		String employeeComment1 = request.getParameter("employeeComment1");
		String employeeComment2 = request.getParameter("employeeComment2");
		String customerName = request.getParameter("customerName");
		String signatureYn = request.getParameter("signatureYn");
		String signatureImage = request.getParameter("signatureImage");
		
		if (signatureImage != null && signatureImage.length() > 0) { 
			signatureYn = "Y";
		} else {
			signatureYn = "N";
		}

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
//			String userId = appUser.getUserId();
			String userId = appUser.getMobileNumber();
			String modifyDate = StringUtil.nowDateTimeStr();
			
			CustomerSaftyCheckTank resultCustomerSaftyCheckTank = BizCustomerSaftyCheck.getInstance().updateCustomerSaftyCheckTank(serverIp, catalogName, areaCode, customerCode, sequenceNumber, scheduledCheckDate, employeeCode, employeeName, tankCapacity1, tankCapacity2, acceptable1, acceptable1Comment, acceptable2, acceptable2Comment, acceptable3, acceptable3Comment, acceptable4, acceptable4Comment, acceptable5, acceptable5Comment, acceptable6, acceptable6Comment, acceptable7, acceptable7Comment, acceptable8, acceptable8Comment, acceptable9, acceptable9Comment, acceptable10Content, acceptable10, acceptable10Comment, acceptable11Content, acceptable11, acceptable11Comment, acceptable12Content, acceptable12, acceptable12Comment, employeeComment1, employeeComment2, customerName, signatureYn, userId, modifyDate);
			if (signatureImage != null && signatureImage.length() > 0) {
				// 서명 저장
				CustomerSaftyCheckSignature resultCustomerSaftyCheckSignature = BizCustomerSaftyCheck.getInstance().updateCustomerSaftyCheckSignature(serverIp, catalogName, areaCode, StringUtil.convertCustomerCodeToTankFormat(customerCode), sequenceNumber, scheduledCheckDate, signatureImage, userId, modifyDate);
				resultCustomerSaftyCheckTank.setSignatureImage(signatureImage);
			} else {
				resultCustomerSaftyCheckTank.setSignatureImage("");
			}
			
			// 안전점검 내역 세션을 최신으로 업데이트하거나 신규 등록
			//CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			//customerSaftyChecks.setCustomerSaftyCheck(resultCustomerSaftyCheck);
			
			// 세션 내 거래처 정보 중 수정된 내역을 업데이트
			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			customerSearch.setLatestSaftyCheckDate(scheduledCheckDate);
			
			out.println(resultCustomerSaftyCheckTank.toXML());
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>