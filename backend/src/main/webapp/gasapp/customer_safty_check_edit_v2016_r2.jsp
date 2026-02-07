<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSaftyCheck" %>
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
	String key = request.getParameter("key");
	boolean insertMode = key==null;
	if (key==null) {
		key = "";
	}
	try{
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userEmployeeCode = appUser.getEmployeeCode();
			String userEmployeeName = appUser.getEmployeeName();
			String menuPermission = appUser.getMenuPermission();
			String [] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
			boolean changeEmployee = "0".equals(permission[2])?true:false;
			//boolean changeEmployee=false;
			
			// 사원정보 가져오기
			EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMLOYEE_CODE");
			if(employeeCodes==null){
				EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				employeeCodes = newEmployeeCodes;
				session.setAttribute("EMPLOYEE_CODE", employeeCodes);
			}
	
			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			//만일 안전점검내역이 조회된 적이 없다면
			if (customerSaftyChecks==null){
				customerSaftyChecks = BizCustomerSaftyCheck.getInstance().getCustomerSaftyChecksRev2(serverIp, catalogName, areaCode, customerSearch.getCustomerCode(), null);
				session.setAttribute("CUSTOMER_SAFTY_CHECK_LIST", customerSaftyChecks);
			}
			
			if (customerSaftyChecks!=null) {
				CustomerSaftyCheck customerSaftyCheck;
				if (insertMode) { // 등록 모드일 때는 최종 데이터로 초기화
					customerSaftyCheck = customerSaftyChecks.getLatestCustomerSaftyCheck();
					if (customerSaftyCheck == null) { // 최초 안전점검이라면,
						customerSaftyCheck = new CustomerSaftyCheck();
						customerSaftyCheck.setSequenceNumber("001");
						// 계약일을 오늘로 설정
						customerSaftyCheck.setContractDate(StringUtil.dateFormatStr(null, ""));
						
						// 디폴트를 일부 항목은 적합으로 설정
						customerSaftyCheck.setAcceptable1("1");
						customerSaftyCheck.setAcceptable2("1");
						customerSaftyCheck.setAcceptable3("1");
						customerSaftyCheck.setAcceptable4("1");
						customerSaftyCheck.setAcceptable5("0");
						customerSaftyCheck.setAcceptable6("0");
						customerSaftyCheck.setAcceptable7("0");
						customerSaftyCheck.setAcceptable8("0");
						customerSaftyCheck.setAcceptable9("1");
						customerSaftyCheck.setAcceptable10("1");
						customerSaftyCheck.setAcceptable11("1");
					} else { // 최종데이타가 있을 경우 순번 처리
						String prevSequenceNumber = customerSaftyCheck.getSequenceNumber().replaceAll("0", "");
						int intSequenceNumber = new Integer(prevSequenceNumber).intValue() +1;
						String newSequenceNumber = "" + intSequenceNumber;
						if (newSequenceNumber.length() == 1) {
							newSequenceNumber = "00" + newSequenceNumber;
						} else if (newSequenceNumber.length() == 2) {
							newSequenceNumber = "0" + newSequenceNumber;
						}
						customerSaftyCheck.setSequenceNumber(newSequenceNumber);
					}
					// 점검일자 오늘날짜로 설정함.
					customerSaftyCheck.setScheduledCheckDate(StringUtil.dateFormatStr(null, ""));
					customerSaftyCheck.setEmployeeCode(appUser.getEmployeeCode());
					customerSaftyCheck.setEmployeeName(appUser.getEmployeeName());
					customerSaftyCheck.setSignatureFilePath("");
					customerSaftyCheck.setSignatureFileName("");
					customerSaftyCheck.setSignatureImage("");
				} else { // 수정 모드일 때는 선택된 데이터로 초기화
					customerSaftyCheck = customerSaftyChecks.getCustomerSaftyCheck(key);
				}
				
				if ((customerSaftyCheck != null) && (customerSearch!=null)){
					String contractNumber = customerSearch.getContractNumber();
					String address1 = customerSearch.getAddress1();
					String address2 = customerSearch.getAddress2();
					
					String customerCode = customerSaftyCheck.getCustomerCode();                                // 거래처코드 Key
					String sequenceNumber = customerSaftyCheck.getSequenceNumber();                            // 항번 Key
					String contractDate = customerSaftyCheck.getContractDate();                                // 공급계약일
					String scheduledCheckDate = customerSaftyCheck.getScheduledCheckDate();                    // 정기점검일
					String employeeCode = customerSaftyCheck.getEmployeeCode();                                // 점검 사원코드
					String employeeName = customerSaftyCheck.getEmployeeName();                                // 점검 사원명
					String contractName = customerSaftyCheck.getContractName();                                // 계약자 성명
					String phoneNumber = customerSaftyCheck.getPhoneNumber();                                  // 전화번호
					String pipeLength1 = customerSaftyCheck.getPipeLength1();                                  // 배관 강관
					String pipeLength2 = customerSaftyCheck.getPipeLength2();                                  // 동관
					String pipeLength3 = customerSaftyCheck.getPipeLength3();                                  // 호스
					String pipeLength4 = customerSaftyCheck.getPipeLength4();                                  // 기타 종류
					String pipeLength5 = customerSaftyCheck.getPipeLength5();                                  // 기타 m
					String valveQuantity1 = customerSaftyCheck.getValveQuantity1();                            // 중간밸브 볼밸브
					String valveQuantity2 = customerSaftyCheck.getValveQuantity2();                            // 퓨즈콕
					String valveQuantity3 = customerSaftyCheck.getValveQuantity3();                            // 호스콕
					String valveQuantity4 = customerSaftyCheck.getValveQuantity4();                            // 기타 명
					String valveQuantity5 = customerSaftyCheck.getValveQuantity5();                            // 기타 (개)
					String etcEquipmentName1 = customerSaftyCheck.getEtcEquipmentName1();                      // 기타 기타 명
					String etcEquipmentQuantity1 = customerSaftyCheck.getEtcEquipmentQuantity1();              // 기타 (개)
					String etcEquipmentName2 = customerSaftyCheck.getEtcEquipmentName2();                      // 기타 명
					String etcEquipmentQuantity2 = customerSaftyCheck.getEtcEquipmentQuantity2();              // 기타 (개)
					String etcEquipmentName3 = customerSaftyCheck.getEtcEquipmentName3();                      // 기타 명
					String etcEquipmentQuantity3 = customerSaftyCheck.getEtcEquipmentQuantity3();              // 기타 (개)
					String etcEquipmentName4 = customerSaftyCheck.getEtcEquipmentName4();                      // 기타 명
					String etcEquipmentQuantity4 = customerSaftyCheck.getEtcEquipmentQuantity4();              // 기타 (개)
					String combustorRange1 = customerSaftyCheck.getCombustorRange1();                          // 연소기-렌지 2구렌지
					String combustorRange2 = customerSaftyCheck.getCombustorRange2();                          // 3구렌지
					String combustorRange3 = customerSaftyCheck.getCombustorRange3();                          // 오븐렌지
					String combustorRangeEtcName = customerSaftyCheck.getCombustorRangeEtcName();              // 기타 명
					String combustorRangeEtcQuantity = customerSaftyCheck.getCombustorRangeEtcQuantity();      // 기타 (개)
					String combustorBoilerType = customerSaftyCheck.getCombustorBoilerType();                  // 연소기-보일러 형식 0, 1.FF, 2.FE, 3.CF
					String combustorBoilerPosition = customerSaftyCheck.getCombustorBoilerPosition();          // 위치 0, 1.옥내, 2.옥외, 3.전용
					String combustorBoilerConsumption = customerSaftyCheck.getCombustorBoilerConsumption();    // 가스소비량
					String combustorBoilerInstaller = customerSaftyCheck.getCombustorBoilerInstaller();        // 시공자
					String combustorHeaterType = customerSaftyCheck.getCombustorHeaterType();                  // 연소기-온수기 형식 0, 1.FF, 2.FE, 3.CF
					String combustorHeaterPosition = customerSaftyCheck.getCombustorHeaterPosition();          // 위치 0, 1.옥내, 2.옥외
					String combustorHeaterConsumption = customerSaftyCheck.getCombustorHeaterConsumption();    // 가스소비량
					String combustorHeaterInstaller = customerSaftyCheck.getCombustorHeaterInstaller();        // 시공자
					String combustorEtcName1 = customerSaftyCheck.getCombustorEtcName1();                      // 연소기-기타 기타 명
					String combustorEtcQuantity1 = customerSaftyCheck.getCombustorEtcQuantity1();              // 기타 (개)
					String combustorEtcName2 = customerSaftyCheck.getCombustorEtcName2();                      // 기타 명
					String combustorEtcQuantity2 = customerSaftyCheck.getCombustorEtcQuantity2();              // 기타 (개)
					String combustorEtcName3 = customerSaftyCheck.getCombustorEtcName3();                      // 기타 명
					String combustorEtcQuantity3 = customerSaftyCheck.getCombustorEtcQuantity3();              // 기타 (개)
					String combustorEtcName4 = customerSaftyCheck.getCombustorEtcName4();                      // 기타 명
					String combustorEtcQuantity4 = customerSaftyCheck.getCombustorEtcQuantity4();              // 기타 (개)
					String acceptable1 = customerSaftyCheck.getAcceptable1();                                  // 시설현황 가 0, 1.적합, 2.부적합
					String acceptable2 = customerSaftyCheck.getAcceptable2();                                  // 나 0, 1.적합, 2.부적합
					String acceptable3 = customerSaftyCheck.getAcceptable3();                                  // 다 0, 1.적합, 2.부적합
					String acceptable4 = customerSaftyCheck.getAcceptable4();                                  // 라 0, 1.적합, 2.부적합
					String acceptable5 = customerSaftyCheck.getAcceptable5();                                  // 마 0, 1.적합, 2.부적합
					String acceptable6 = customerSaftyCheck.getAcceptable6();                                  // 바 0, 1.적합, 2.부적합
					String acceptable7 = customerSaftyCheck.getAcceptable7();                                  // 사 0, 1.적합, 2.부적합
					String acceptable8 = customerSaftyCheck.getAcceptable8();                                  // 아 0, 1.적합, 2.부적합
					String acceptable9 = customerSaftyCheck.getAcceptable9();                                  // 자 0, 1.적합, 2.부적합
					String acceptable10 = customerSaftyCheck.getAcceptable10();                                // 차 0, 1.적합, 2.부적합
					String acceptable11 = customerSaftyCheck.getAcceptable11();                                // 카 0, 1.적합, 2.부적합
					String notifyRemark1 = customerSaftyCheck.getNotifyRemark1();                              // 개선통지 내용1
					String notifyRemark2 = customerSaftyCheck.getNotifyRemark2();                              // 내용2
					String recommendation1 = customerSaftyCheck.getRecommendation1();                          // 권장사항 내용1
					String recommendation2 = customerSaftyCheck.getRecommendation2();                          // 내용2
					String signatureFilePath = customerSaftyCheck.getSignatureFilePath();                      // 서명화일 저장경로(URL)
					String signatureFileName = customerSaftyCheck.getSignatureFileName();                      // 파일명(Area_Code+Cu_Code+Sno)
					String signatureImage = customerSaftyCheck.getSignatureImage();                            // 서명이미지(DataURL)
					String userId = customerSaftyCheck.getUserId();                                            // APP 사용자명
					String modifyDate = customerSaftyCheck.getModifyDate();                                    // 등록/수정일자
					
					boolean isSigned = false;
					if (signatureImage != null && signatureImage.length() > 0) {
						isSigned = true;
					}
%>
<input type="hidden" id="hdnKeyCustomerSaftyCheckEdit" value="<%=key %>" />
<input type="hidden" id="hdnSequenceNumberCustomerSaftyCheckEdit" value="<%=sequenceNumber %>" />
<input type="hidden" id="hdnInsertModeCustomerSaftyCheckEdit" value="<%=insertMode?"0":"1" %>" />
<input type="hidden" id="hdnSignatureFilePathCustomerSaftyCheckEdit" value="<%=signatureFilePath %>" />
<input type="hidden" id="hdnSignatureFileNameCustomerSaftyCheckEdit" value="<%=signatureFileName %>" />
<input type="hidden" id="hdnSignatureImageCustomerSaftyCheckEdit" value="<%=signatureImage %>" />
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약번호</td>
        <td style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractNumberCustomerSaftyCheckEdit" value="<%=contractNumber==null?"":contractNumber %>" maxLength="7" style="width: 80% ; color: blue ; " /></td>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약자명</td>
        <td style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractNameCustomerSaftyCheckEdit" value="<%=contractName==null?"":contractName %>" maxLength="5" style="width: 80% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">전화</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtPhoneNumberCustomerSaftyCheckEdit" value="<%=phoneNumber==null?"":phoneNumber %>" maxLength="13" style="width: 90% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">관할주소</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtAddress1CustomerSaftyCheckEdit" value="<%=address1==null?"":address1 %>" maxLength="20" style="width: 90% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">상세주소</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtAddress2CustomerSaftyCheckEdit" value="<%=address2==null?"":address2 %>" maxLength="20" style="width: 90% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractDateCustomerSaftyCheckEdit" value="<%=StringUtil.dateFormatStr(contractDate) %>" maxLength="7" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerSaftyCheckEdit', 'txtContractDateCustomerSaftyCheckEdit')" /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">점검일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtScheduledCheckDateCustomerSaftyCheckEdit" value="<%=StringUtil.dateFormatStr(scheduledCheckDate) %>" maxLength="5" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerSaftyCheckEdit', 'txtScheduledCheckDateCustomerSaftyCheckEdit')" /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">점검사원</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; ">
 			<input type="hidden" id="hdnEmployeeNameCustomerSaftyCheckEdit" value="<%=userEmployeeName %>" />
 <%
	if (changeEmployee) { 
%>
			<select id="selectEmployeeCodeCustomerSaftyCheckEdit"  data-mini="true" style="font-size: 14px ; color: blue ; " onchange="changeEmployeeCustomerSaftyCheckEdit()">
				<option value="" data-placeholder="true" >점검사원</option>
<%
					// 사원 목록을 선택 박스에 채우기
					java.util.Iterator<String> iterator = employeeCodes.getEmployeeCodes().keySet().iterator(); 
					while (iterator.hasNext()) { 
						String keyEmployeeCode = iterator.next(); 
						EmployeeCode ec =  employeeCodes.getEmployeeCode(keyEmployeeCode);
						String employeeCodeStr = ec.getEmployeeCode();
						String employeeNameStr = ec.getEmployeeName();
						String selected = employeeCodeStr.equals(employeeCode)?"selected":"";
%>
				<option value="<%= employeeCodeStr %>" <%=selected %>><%= employeeNameStr %></option>
<%
					}
%>
			</select>
<%
	} else {
%>
			<input type="hidden" id ="selectEmployeeCustomerSaftyCheckEdit" value="<%=userEmployeeCode %>" />
			<%= userEmployeeName %>
<%
	}
%>
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
	<tr>
		<th style="width: 15px" />
		<th style="width: 59px" />
		<th style="width: 68px" />
		<th style="width: 68px" />
		<th style="width: 68px" />
		<th style="width: 68px" />
	</tr>
    <tr style="height: 40px ; ">
        <td colspan="2" rowspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">배관</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">강관</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength1CustomerSaftyCheckEdit" value="<%=pipeLength1==null?"":pipeLength1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()" /> m</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">동관</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength2CustomerSaftyCheckEdit" value="<%=pipeLength2==null?"":pipeLength2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()" /> m</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">호스</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtPipeLength3CustomerSaftyCheckEdit" value="<%=pipeLength3==null?"":pipeLength3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()" /> m</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtPipeLength4CustomerSaftyCheckEdit" value="<%=pipeLength4==null?"":pipeLength4 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength5CustomerSaftyCheckEdit" value="<%=pipeLength5==null?"":pipeLength5 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> m</td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" rowspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">중간<br/>밸브</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">볼밸브</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity1CustomerSaftyCheckEdit" value="<%=valveQuantity1==null?"":valveQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">퓨즈콕</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity2CustomerSaftyCheckEdit" value="<%=valveQuantity2==null?"":valveQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">호스콕</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity3CustomerSaftyCheckEdit" value="<%=valveQuantity3==null?"":valveQuantity3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtValveQuantity4CustomerSaftyCheckEdit" value="<%=valveQuantity4==null?"":valveQuantity4 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity5CustomerSaftyCheckEdit" value="<%=valveQuantity5==null?"":valveQuantity5 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ;  border: 1px white ; " onclick="this.select()" /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">기타</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName1CustomerSaftyCheckEdit" value="<%=etcEquipmentName1==null?"":etcEquipmentName1 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity1CustomerSaftyCheckEdit" value="<%=etcEquipmentQuantity1==null?"":etcEquipmentQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName2CustomerSaftyCheckEdit" value="<%=etcEquipmentName2==null?"":etcEquipmentName2 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity2CustomerSaftyCheckEdit" value="<%=etcEquipmentQuantity2==null?"":etcEquipmentQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ;  border: 1px white ; " onclick="this.select()" /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="7" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">연<br/>소<br/>기</td>
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">2구렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange1CustomerSaftyCheckEdit" value="<%=combustorRange1==null?"":combustorRange1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">3구렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange2CustomerSaftyCheckEdit" value="<%=combustorRange2==null?"":combustorRange2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">오븐렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange3CustomerSaftyCheckEdit" value="<%=combustorRange3==null?"":combustorRange3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorRangeEtcNameCustomerSaftyCheckEdit" value="<%=combustorRangeEtcName==null?"":combustorRangeEtcName %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRangeEtcQuantityCustomerSaftyCheckEdit" value="<%=combustorRangeEtcQuantity==null?"":combustorRangeEtcQuantity %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">보일러</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">형식</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorBoilerTypeCustomerSaftyCheckEdit" id="selectCombustorBoilerTypeCustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " onchange="changeBoilerAndHeaterTypeCustomerSaftyCheckEdit()">
				<option value="0" <%= "0".equals(combustorBoilerType)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorBoilerType)?"selected":"" %> >F F</option>
				<option value="2" <%= "2".equals(combustorBoilerType)?"selected":"" %>>F E</option>
				<option value="3" <%= "3".equals(combustorBoilerType)?"selected":"" %>>C F</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">위치</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorBoilerPositionCustomerSaftyCheckEdit" id="selectCombustorBoilerPositionCustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; ">
				<option value="0" <%= "0".equals(combustorBoilerPosition)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorBoilerPosition)?"selected":"" %> >옥내</option>
				<option value="2" <%= "2".equals(combustorBoilerPosition)?"selected":"" %>>옥외</option>
				<option value="3" <%= "3".equals(combustorBoilerPosition)?"selected":"" %>>전용</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">소비량</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorBoilerConsumptionCustomerSaftyCheckEdit" value="<%=combustorBoilerConsumption==null?"":combustorBoilerConsumption %>" maxLength="6" style="width: 50% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> kg/h</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">시공자</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtCombustorBoilerInstallerCustomerSaftyCheckEdit" value="<%=combustorBoilerInstaller==null?"":combustorBoilerInstaller %>" maxLength="5" style="width: 90% ; font-size: 14px ; border: 1px white ; "  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">온수기</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">형식</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorHeaterTypeCustomerSaftyCheckEdit" id="selectCombustorHeaterTypeCustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; "  onchange="changeBoilerAndHeaterTypeCustomerSaftyCheckEdit()">
				<option value="0" <%= "0".equals(combustorHeaterType)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorHeaterType)?"selected":"" %> >F F</option>
				<option value="2" <%= "2".equals(combustorHeaterType)?"selected":"" %>>F E</option>
				<option value="3" <%= "3".equals(combustorHeaterType)?"selected":"" %>>C F</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">위치</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorHeaterPositionCustomerSaftyCheckEdit" id="selectCombustorHeaterPositionCustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(combustorHeaterPosition)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorHeaterPosition)?"selected":"" %> >옥내</option>
				<option value="2" <%= "2".equals(combustorHeaterPosition)?"selected":"" %>>옥외</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">소비량</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorHeaterConsumptionCustomerSaftyCheckEdit" value="<%=combustorHeaterConsumption==null?"":combustorHeaterConsumption %>" maxLength="6" style="width: 50% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> kg/h</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">시공자</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtCombustorHeaterInstallerCustomerSaftyCheckEdit" value="<%=combustorHeaterInstaller==null?"":combustorHeaterInstaller %>" maxLength="5" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">기타</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName1CustomerSaftyCheckEdit" value="<%=combustorEtcName1==null?"":combustorEtcName1 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity1CustomerSaftyCheckEdit" value="<%=combustorEtcQuantity1==null?"":combustorEtcQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName2CustomerSaftyCheckEdit" value="<%=combustorEtcName2==null?"":combustorEtcName2 %>" maxLength="3" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity2CustomerSaftyCheckEdit" value="<%=combustorEtcQuantity2==null?"":combustorEtcQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="this.select()"  /> 개</td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
	<tr>
		<th style="width: 150px" />
		<th  />
		<th style="width: 70px" />
	</tr>
    <tr>
        <td colspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">점검내용</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">판정</td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">가. 가스누출검사 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable1CustomerSaftyCheckEdit" id="selectAcceptable1CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable1)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable1)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable1)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">나. 검사품 검사표시 유무</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable2CustomerSaftyCheckEdit" id="selectAcceptable2CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable2)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable2)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable2)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">다. 중간밸브 설치여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable3CustomerSaftyCheckEdit" id="selectAcceptable3CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable3)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable3)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable3)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">라. 호스 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable4CustomerSaftyCheckEdit" id="selectAcceptable4CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable4)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable4)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable4)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">마. 연소기 설치규정 준수 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable5CustomerSaftyCheckEdit" id="selectAcceptable5CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable5)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable5)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable5)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">바. 연소기 전용보일러실 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable6CustomerSaftyCheckEdit" id="selectAcceptable6CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable6)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable6)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable6)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">사. 연소기 배기통재료 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable7CustomerSaftyCheckEdit" id="selectAcceptable7CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable7)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable7)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable7)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">아. 연소기 배기통의 막힘 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable8CustomerSaftyCheckEdit" id="selectAcceptable8CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable8)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable8)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable8)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">자. 용기의 옥내설치 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable9CustomerSaftyCheckEdit" id="selectAcceptable9CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable9)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable9)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable9)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">차. 그 밖에 사고 유발 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable10CustomerSaftyCheckEdit" id="selectAcceptable10CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable10)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable10)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable10)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">카. 권장사용기간경과 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable11CustomerSaftyCheckEdit" id="selectAcceptable11CustomerSaftyCheckEdit"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable11)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable11)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable11)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">개선통지사항</td>
        <td colspan="2" style="width: 70px ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtNotifyRemark1CustomerSaftyCheckEdit" value="<%=notifyRemark1==null?"":notifyRemark1 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="width: 70px ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtNotifyRemark2CustomerSaftyCheckEdit" value="<%=notifyRemark2==null?"":notifyRemark2 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">가스용품 교체<br />권장사항</td>
        <td colspan="2" style="width: 70px ; font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtRecommendation1CustomerSaftyCheckEdit" value="<%= recommendation1==null?"":recommendation1 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="width: 70px ; font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtRecommendation2CustomerSaftyCheckEdit" value="<%= recommendation2==null?"":recommendation2 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><span id="spnSignCustomerSaftyCheckEdit"><%=isSigned?"서명됨":"서명안됨" %></span></td>
        <td colspan="2" style="font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
			<input id="btnSignCustomerSaftyCheckEdit" data-mini="true" type="button" value="<%=isSigned?"서명보기":"서명등록" %>" data-corners="false" data-inline="true" onclick="showSignatureCustomerSaftyCheckEdit()" />
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
			<input data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true" onclick="clickUpdateCustomerSaftyCheckEdit()" ></input>
			<input data-mini="true" type="button" value="삭제" data-icon="delete" data-corners="false" data-inline="true" onclick="clickDeleteCustomerSaftyCheckEdit()" ></input>
		</td>
	</tr>
</table>
<%
				}
			}
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
