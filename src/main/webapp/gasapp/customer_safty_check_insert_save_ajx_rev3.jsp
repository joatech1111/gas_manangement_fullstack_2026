<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckList" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckSignature" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap" %>
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
		String phoneNumber  = request.getParameter("phoneNumber");
		String phoneNumberFind  = StringUtil.stringReplace(phoneNumber);
		String address1  = request.getParameter("address1");
		String address2  = request.getParameter("address2");
		String contractNumber  = request.getParameter("contractNumber");
		String contractName  = request.getParameter("contractName");
		String contractDate  = request.getParameter("contractDate");
		contractDate = StringUtil.stringReplace(contractDate);
		
//		String areaCode = request.getParameter("areaCode");
		String customerCode = request.getParameter("customerCode");
		String sequenceNumber = request.getParameter("sequenceNumber");
		String scheduledCheckDate = request.getParameter("scheduledCheckDate");
		scheduledCheckDate = StringUtil.stringReplace(scheduledCheckDate);
		String employeeCode = request.getParameter("employeeCode");
		String employeeName = request.getParameter("employeeName");
		String pipeLength1 = request.getParameter("pipeLength1");
		String pipeLength2 = request.getParameter("pipeLength2");
		String pipeLength3 = request.getParameter("pipeLength3");
		String pipeLength4 = request.getParameter("pipeLength4");
		String pipeLength5 = request.getParameter("pipeLength5");
		String valveQuantity1 = request.getParameter("valveQuantity1");
		String valveQuantity2 = request.getParameter("valveQuantity2");
		String valveQuantity3 = request.getParameter("valveQuantity3");
		String valveQuantity4 = request.getParameter("valveQuantity4");
		String valveQuantity5 = request.getParameter("valveQuantity5");
		String etcEquipmentName1 = request.getParameter("etcEquipmentName1");
		String etcEquipmentQuantity1 = request.getParameter("etcEquipmentQuantity1");
		String etcEquipmentName2 = request.getParameter("etcEquipmentName2");
		String etcEquipmentQuantity2 = request.getParameter("etcEquipmentQuantity2");
		//String etcEquipmentName3 = ""; //request.getParameter("etcEquipmentName3");
		//String etcEquipmentQuantity3 = ""; //request.getParameter("etcEquipmentQuantity3");
		//String etcEquipmentName4 = ""; //request.getParameter("etcEquipmentName4");
		//String etcEquipmentQuantity4 = ""; //request.getParameter("etcEquipmentQuantity4");
		String etcEquipmentName3 = request.getParameter("etcEquipmentName3");
		String etcEquipmentQuantity3 = request.getParameter("etcEquipmentQuantity3");
		String etcEquipmentName4 = request.getParameter("etcEquipmentName4");
		String etcEquipmentQuantity4 = request.getParameter("etcEquipmentQuantity4");
		String combustorRange1 = request.getParameter("combustorRange1");
		String combustorRange2 = request.getParameter("combustorRange2");
		String combustorRange3 = request.getParameter("combustorRange3");
		String combustorRangeEtcName = request.getParameter("combustorRangeEtcName");
		String combustorRangeEtcQuantity = request.getParameter("combustorRangeEtcQuantity");
		String combustorBoilerType = request.getParameter("combustorBoilerType");
		String combustorBoilerPosition = request.getParameter("combustorBoilerPosition");
		String combustorBoilerConsumption = request.getParameter("combustorBoilerConsumption");
		String combustorBoilerInstaller = request.getParameter("combustorBoilerInstaller");
		String combustorHeaterType = request.getParameter("combustorHeaterType");
		String combustorHeaterPosition = request.getParameter("combustorHeaterPosition");
		String combustorHeaterConsumption = request.getParameter("combustorHeaterConsumption");
		String combustorHeaterInstaller = request.getParameter("combustorHeaterInstaller");
		String combustorEtcName1 = request.getParameter("combustorEtcName1");
		String combustorEtcQuantity1 = request.getParameter("combustorEtcQuantity1");
		String combustorEtcName2 = request.getParameter("combustorEtcName2");
		String combustorEtcQuantity2 = request.getParameter("combustorEtcQuantity2");
		//String combustorEtcName3 = ""; //request.getParameter("combustorEtcName3");
		//String combustorEtcQuantity3 = ""; //request.getParameter("combustorEtcQuantity3");
		//String combustorEtcName4 = ""; //request.getParameter("combustorEtcName4");
		//String combustorEtcQuantity4 = ""; //request.getParameter("combustorEtcQuantity4");
		String combustorEtcName3 = request.getParameter("combustorEtcName3");
		String combustorEtcQuantity3 = request.getParameter("combustorEtcQuantity3");
		String combustorEtcName4 = request.getParameter("combustorEtcName4");
		String combustorEtcQuantity4 = request.getParameter("combustorEtcQuantity4");
		String acceptable1 = request.getParameter("acceptable1");
		String acceptable2 = request.getParameter("acceptable2");
		String acceptable3 = request.getParameter("acceptable3");
		String acceptable4 = request.getParameter("acceptable4");
		String acceptable5 = request.getParameter("acceptable5");
		String acceptable6 = request.getParameter("acceptable6");
		String acceptable7 = request.getParameter("acceptable7");
		String acceptable8 = request.getParameter("acceptable8");
		String acceptable9 = request.getParameter("acceptable9");
		String acceptable10 = request.getParameter("acceptable10");
		String acceptable11 = request.getParameter("acceptable11");
		String acceptable12 = request.getParameter("acceptable12");
		String notifyRemark1 = request.getParameter("notifyRemark1");
		String notifyRemark2 = request.getParameter("notifyRemark2");
		String recommendation1 = request.getParameter("recommendation1");
		String recommendation2 = request.getParameter("recommendation2");
		String signatureFilePath = request.getParameter("signatureFilePath");
		String signatureFileName = request.getParameter("signatureFileName");
		String facilityOkYesNo  = "Y";
		if ("2".equals(acceptable1)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable2)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable3)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable4)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable5)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable6)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable7)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable8)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable9)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable10)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable11)) facilityOkYesNo  = "N";
		if ("2".equals(acceptable12)) facilityOkYesNo  = "N";
		
		String signatureImage = request.getParameter("signatureImage");
		if (signatureImage == null) signatureImage = "";

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
//			String userId = appUser.getUserId();
			String userId = appUser.getMobileNumber();
			String modifyDate = StringUtil.nowDateTimeStr();
			BizCustomerSaftyCheck.getInstance().updateCustomerInfo(serverIp, catalogName, areaCode, customerCode, phoneNumber, phoneNumberFind, address1, address2, contractNumber, contractName, contractDate, scheduledCheckDate, facilityOkYesNo, userId);
			
			sequenceNumber = BizCustomerSaftyCheck.getInstance().selectSno(serverIp, catalogName, areaCode, customerCode); // 신규등록이라면 Sno 새로 발급 받아 처리
			CustomerSaftyCheck resultCustomerSaftyCheck = BizCustomerSaftyCheck.getInstance().insertCustomerSaftyCheckRev3(serverIp, catalogName, areaCode, customerCode, sequenceNumber, contractDate, scheduledCheckDate, employeeCode, employeeName, contractName, phoneNumber, pipeLength1, pipeLength2, pipeLength3, pipeLength4, pipeLength5, valveQuantity1, valveQuantity2, valveQuantity3, valveQuantity4, valveQuantity5, etcEquipmentName1, etcEquipmentQuantity1, etcEquipmentName2, etcEquipmentQuantity2, etcEquipmentName3, etcEquipmentQuantity3, etcEquipmentName4, etcEquipmentQuantity4, combustorRange1, combustorRange2, combustorRange3, combustorRangeEtcName, combustorRangeEtcQuantity, combustorBoilerType, combustorBoilerPosition, combustorBoilerConsumption, combustorBoilerInstaller, combustorHeaterType, combustorHeaterPosition, combustorHeaterConsumption, combustorHeaterInstaller, combustorEtcName1, combustorEtcQuantity1, combustorEtcName2, combustorEtcQuantity2, combustorEtcName3, combustorEtcQuantity3, combustorEtcName4, combustorEtcQuantity4, acceptable1, acceptable2, acceptable3, acceptable4, acceptable5, acceptable6, acceptable7, acceptable8, acceptable9, acceptable10, acceptable11, acceptable12, notifyRemark1, notifyRemark2, recommendation1, recommendation2, signatureFilePath, signatureFileName, userId, modifyDate);

			// 서명 저장
			CustomerSaftyCheckSignature resultCustomerSaftyCheckSignature = BizCustomerSaftyCheck.getInstance().insertCustomerSaftyCheckSignature(serverIp, catalogName, areaCode, customerCode, sequenceNumber, scheduledCheckDate, signatureImage, userId, modifyDate);
			resultCustomerSaftyCheck.setSignatureImage(signatureImage);
			
			// 안전점검 내역 세션을 최신으로 업데이트하거나 신규 등록
			//CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			//customerSaftyChecks.setCustomerSaftyCheck(resultCustomerSaftyCheck);
			//CustomerSaftyCheckListMap customerSaftyCheckLists = (CustomerSaftyCheckListMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			//customerSaftyCheckLists.setCustomerSaftyCheck(resultCustomerSaftyCheck);
			
			// 세션 내 거래처 정보 중 수정된 내역을 업데이트
			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			customerSearch.setAddress1(address1);
			customerSearch.setAddress2(address2);
			customerSearch.setContractNumber(contractNumber);
			customerSearch.setContractDate(contractDate);
			customerSearch.setPhoneNumber(phoneNumber);
			customerSearch.setPhoneNumberFind(phoneNumberFind);
			customerSearch.setLatestSaftyCheckDate(scheduledCheckDate);
			
			out.println(resultCustomerSaftyCheck.toXML());
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		out.println("<result><code>E</code><message>저장 중에 오류가 발생하였습니다.</message></result>");
		e.printStackTrace();
	}
%>