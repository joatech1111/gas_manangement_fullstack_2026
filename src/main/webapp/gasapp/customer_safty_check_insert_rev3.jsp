<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
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
	if (key==null) {
		key = "";
	}
	try{
		//AppUser appUser = (AppUser)session.getAttribute("USER_INFO");



		String _uuid= request.getParameter("uuid");


		System.out.println("sdfksdflksdlfklsdkflsdkflsdkflksdlfk");
		System.out.println("sdfksdflksdlfklsdkflsdkflsdkflksdlfk");
		System.out.println("sdfksdflksdlfklsdkflsdkflsdkflksdlfk");
		System.out.println("sdfksdflksdlfklsdkflsdkflsdkflksdlfk");

		System.out.println(_uuid);
		System.out.println(_uuid);
		System.out.println(_uuid);

		AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
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
			/*
			CustomerSaftyCheckMap customerSaftyChecks = (CustomerSaftyCheckMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_LIST");
			//만일 안전점검내역이 조회된 적이 없다면
			if (customerSaftyChecks==null){
				customerSaftyChecks = BizCustomerSaftyCheck.getInstance().getCustomerSaftyChecksRev3(serverIp, catalogName, areaCode, customerSearch.getCustomerCode(), null);
				session.setAttribute("CUSTOMER_SAFTY_CHECK_LIST", customerSaftyChecks);
			}

			if (customerSaftyChecks!=null) {
				CustomerSaftyCheck customerSaftyCheck = customerSaftyChecks.getLatestCustomerSaftyCheck();
				if (customerSaftyCheck == null) { // 최초 안전점검이라면,
			*/
			// 최근 안전점검 내역 가져오기
			CustomerSaftyCheck customerSaftyCheck = BizCustomerSaftyCheck.getInstance().getCustomerSaftyCheckLast(serverIp, catalogName, areaCode, customerSearch.getCustomerCode());
			if (customerSaftyCheck == null) {	// 이전에 안전점검을 한 내역이 없다면, 기본 안전점검 내역설정
				customerSaftyCheck = new CustomerSaftyCheck();
				customerSaftyCheck.setSequenceNumber("001");	// 순번
				// 계약일을 오늘로 설정
				customerSaftyCheck.setContractDate(StringUtil.dateFormatStr(null, ""));

				// 점검내용의 기본값 설정 (0:해당없음, 1:적합, 2:부적합)
				customerSaftyCheck.setAcceptable1("1");		// 가
				customerSaftyCheck.setAcceptable2("1");		// 나
				customerSaftyCheck.setAcceptable3("1");		// 다
				customerSaftyCheck.setAcceptable4("1");		// 라
				customerSaftyCheck.setAcceptable5("0");		// 마
				customerSaftyCheck.setAcceptable6("0");		// 바
				customerSaftyCheck.setAcceptable7("0");		// 사
				customerSaftyCheck.setAcceptable8("0");		// 아
				customerSaftyCheck.setAcceptable9("1");		// 자
				customerSaftyCheck.setAcceptable10("1");	// 카
				customerSaftyCheck.setAcceptable11("1");	// 타
				customerSaftyCheck.setAcceptable12("1");	// 차(신규)

			}
			/*
			else { // 최종데이타가 있을 경우 순번 처리
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
			*/
			if (customerSaftyCheck.getSequenceNumber() == null) {
				customerSaftyCheck.setSequenceNumber("001");
			}

			// 점검일자 오늘날짜로 설정함.
			customerSaftyCheck.setScheduledCheckDate(StringUtil.dateFormatStr(null, ""));
			customerSaftyCheck.setEmployeeCode(appUser.getEmployeeCode());
			customerSaftyCheck.setEmployeeName(appUser.getEmployeeName());
			customerSaftyCheck.setSignatureFilePath("");
			customerSaftyCheck.setSignatureFileName("");
			customerSaftyCheck.setSignatureImage("");

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
				String acceptable10 = customerSaftyCheck.getAcceptable10();                                // 카 0, 1.적합, 2.부적합
				String acceptable11 = customerSaftyCheck.getAcceptable11();                                // 타 0, 1.적합, 2.부적합
				String acceptable12 = customerSaftyCheck.getAcceptable12();                                // 차 0, 1.적합, 2.부적합
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
<input type="hidden" id="hdnKeyCustomerSaftyCheckInsert" value="<%=key %>" />
<input type="hidden" id="hdnSequenceNumberCustomerSaftyCheckInsert" value="<%=sequenceNumber %>" />
<input type="hidden" id="hdnInsertModeCustomerSaftyCheckInsert" value="0" />
<input type="hidden" id="hdnSignatureFilePathCustomerSaftyCheckInsert" value="<%=signatureFilePath %>" />
<input type="hidden" id="hdnSignatureFileNameCustomerSaftyCheckInsert" value="<%=signatureFileName %>" />
<input type="hidden" id="hdnSignatureImageCustomerSaftyCheckInsert" value="<%=signatureImage %>" />
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약번호</td>
        <td style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractNumberCustomerSaftyCheckInsert" value="<%=contractNumber==null?"":contractNumber %>" maxLength="7" style="width: 80% ; color: blue ; " /></td>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약자명</td>
        <td style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractNameCustomerSaftyCheckInsert" value="<%=contractName==null?"":contractName %>" maxLength="5" style="width: 80% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">전화</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtPhoneNumberCustomerSaftyCheckInsert" value="<%=phoneNumber==null?"":phoneNumber %>" maxLength="13" style="width: 90% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">관할주소</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtAddress1CustomerSaftyCheckInsert" value="<%=address1==null?"":address1 %>" maxLength="20" style="width: 90% ; color: blue ; " /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">상세주소</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtAddress2CustomerSaftyCheckInsert" value="<%=address2==null?"":address2 %>" maxLength="20" style="width: 90% ; color: blue ; " /></td>
    </tr>
	<tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">계약일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtContractDateCustomerSaftyCheckInsert" value="<%=StringUtil.dateFormatStr(contractDate) %>" maxLength="7" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerSaftyCheckInsert', 'txtContractDateCustomerSaftyCheckInsert')" /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 0px solid #222222 ; ">점검일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #222222 ; color: #3333FF ; "><input type="text" data-mini="true" id="txtScheduledCheckDateCustomerSaftyCheckInsert" value="<%=StringUtil.dateFormatStr(scheduledCheckDate) %>" maxLength="5" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerSaftyCheckInsert', 'txtScheduledCheckDateCustomerSaftyCheckInsert')" /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">점검사원</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; ">
 			<input type="hidden" id="hdnEmployeeNameCustomerSaftyCheckInsert" value="<%=userEmployeeName %>" />
 <%
	if (changeEmployee) {
%>
			<select id="selectEmployeeCodeCustomerSaftyCheckInsert"  data-mini="true" style="font-size: 14px ; color: blue ; " onchange="changeEmployeeCustomerSaftyCheckInsert()">
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
			<input type="hidden" id ="selectEmployeeCustomerSaftyCheckInsert" value="<%=userEmployeeCode %>" />
			<%= userEmployeeName %>
<%
	}
%>
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
	<tr>
		<th style="width: 17px" />
		<th style="width: 17px" />
		<th style="width: 80px" />
		<th style="width: 76px" />
		<th style="width: 80px" />
		<th style="width: 76px" />
	</tr>
    <tr style="height: 40px ; ">
        <td colspan="2" rowspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">배관</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " onclick="showDialogPipeLengthOptions('pageCustomerSaftyCheckInsert', 'txtPipeLength1CustomerSaftyCheckInsert')">강관</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength1CustomerSaftyCheckInsert" value="<%=pipeLength1==null?"":pipeLength1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " /> m</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " onclick="showDialogPipeLengthOptions('pageCustomerSaftyCheckInsert', 'txtPipeLength2CustomerSaftyCheckInsert')">동관</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength2CustomerSaftyCheckInsert" value="<%=pipeLength2==null?"":pipeLength2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="" /> m</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">호스</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtPipeLength3CustomerSaftyCheckInsert" value="<%=pipeLength3==null?"":pipeLength3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick="" /> m</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtPipeLength4CustomerSaftyCheckInsert" value="<%=pipeLength4==null?"":pipeLength4 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtPipeLength5CustomerSaftyCheckInsert" value="<%=pipeLength5==null?"":pipeLength5 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> m</td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" rowspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">중간<br/>밸브</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " onclick="showDialogPipeLengthOptions('pageCustomerSaftyCheckInsert', 'txtValveQuantity1CustomerSaftyCheckInsert')">볼밸브</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtValveQuantity1CustomerSaftyCheckInsert" value="<%=valveQuantity1==null?"":valveQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">퓨즈콕</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity2CustomerSaftyCheckInsert" value="<%=valveQuantity2==null?"":valveQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">호스콕</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity3CustomerSaftyCheckInsert" value="<%=valveQuantity3==null?"":valveQuantity3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtValveQuantity4CustomerSaftyCheckInsert" value="<%=valveQuantity4==null?"":valveQuantity4 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtValveQuantity5CustomerSaftyCheckInsert" value="<%=valveQuantity5==null?"":valveQuantity5 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ;  border: 1px white ; " onclick="" /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" rowspan="2" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">기타</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName1CustomerSaftyCheckInsert" value="<%=etcEquipmentName1==null?"":etcEquipmentName1 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity1CustomerSaftyCheckInsert" value="<%=etcEquipmentQuantity1==null?"":etcEquipmentQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName2CustomerSaftyCheckInsert" value="<%=etcEquipmentName2==null?"":etcEquipmentName2 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity2CustomerSaftyCheckInsert" value="<%=etcEquipmentQuantity2==null?"":etcEquipmentQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ;  border: 1px white ; " onclick="" /> 개</td>
    </tr>
	<tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName3CustomerSaftyCheckInsert" value="<%=etcEquipmentName3==null?"":etcEquipmentName3 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity3CustomerSaftyCheckInsert" value="<%=etcEquipmentQuantity3==null?"":etcEquipmentQuantity3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtEtcEquipmentName4CustomerSaftyCheckInsert" value="<%=etcEquipmentName4==null?"":etcEquipmentName4 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtEtcEquipmentQuantity4CustomerSaftyCheckInsert" value="<%=etcEquipmentQuantity4==null?"":etcEquipmentQuantity4 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ;  border: 1px white ; " onclick="" /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="8" style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">연<br/>소<br/>기</td>
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">렌<br/>지</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">2구렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange1CustomerSaftyCheckInsert" value="<%=combustorRange1==null?"":combustorRange1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">3구렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange2CustomerSaftyCheckInsert" value="<%=combustorRange2==null?"":combustorRange2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">오븐렌지</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRange3CustomerSaftyCheckInsert" value="<%=combustorRange3==null?"":combustorRange3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorRangeEtcNameCustomerSaftyCheckInsert" value="<%=combustorRangeEtcName==null?"":combustorRangeEtcName %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorRangeEtcQuantityCustomerSaftyCheckInsert" value="<%=combustorRangeEtcQuantity==null?"":combustorRangeEtcQuantity %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">보<br/>일<br/>러</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">형식</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorBoilerTypeCustomerSaftyCheckInsert" id="selectCombustorBoilerTypeCustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " onchange="changeBoilerAndHeaterTypeCustomerSaftyCheckInsert()">
				<option value="0" <%= "0".equals(combustorBoilerType)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorBoilerType)?"selected":"" %> >F F</option>
				<option value="2" <%= "2".equals(combustorBoilerType)?"selected":"" %>>F E</option>
				<option value="3" <%= "3".equals(combustorBoilerType)?"selected":"" %>>C F</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">위치</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorBoilerPositionCustomerSaftyCheckInsert" id="selectCombustorBoilerPositionCustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; ">
				<option value="0" <%= "0".equals(combustorBoilerPosition)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorBoilerPosition)?"selected":"" %> >옥내</option>
				<option value="2" <%= "2".equals(combustorBoilerPosition)?"selected":"" %>>옥외</option>
				<option value="3" <%= "3".equals(combustorBoilerPosition)?"selected":"" %>>전용</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">소비량</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorBoilerConsumptionCustomerSaftyCheckInsert" value="<%=combustorBoilerConsumption==null?"":combustorBoilerConsumption %>" maxLength="6" style="width: 50% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> kg/h</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">시공자</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtCombustorBoilerInstallerCustomerSaftyCheckInsert" value="<%=combustorBoilerInstaller==null?"":combustorBoilerInstaller %>" maxLength="5" style="width: 90% ; font-size: 14px ; border: 1px white ; "  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">온<br/>수<br/>기</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">형식</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorHeaterTypeCustomerSaftyCheckInsert" id="selectCombustorHeaterTypeCustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; "  onchange="changeBoilerAndHeaterTypeCustomerSaftyCheckInsert()">
				<option value="0" <%= "0".equals(combustorHeaterType)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorHeaterType)?"selected":"" %> >F F</option>
				<option value="2" <%= "2".equals(combustorHeaterType)?"selected":"" %>>F E</option>
				<option value="3" <%= "3".equals(combustorHeaterType)?"selected":"" %>>C F</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">위치</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectCombustorHeaterPositionCustomerSaftyCheckInsert" id="selectCombustorHeaterPositionCustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(combustorHeaterPosition)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(combustorHeaterPosition)?"selected":"" %> >옥내</option>
				<option value="2" <%= "2".equals(combustorHeaterPosition)?"selected":"" %>>옥외</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">소비량</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorHeaterConsumptionCustomerSaftyCheckInsert" value="<%=combustorHeaterConsumption==null?"":combustorHeaterConsumption %>" maxLength="6" style="width: 50% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> kg/h</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">시공자</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtCombustorHeaterInstallerCustomerSaftyCheckInsert" value="<%=combustorHeaterInstaller==null?"":combustorHeaterInstaller %>" maxLength="5" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">기<br/>타</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName1CustomerSaftyCheckInsert" value="<%=combustorEtcName1==null?"":combustorEtcName1 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity1CustomerSaftyCheckInsert" value="<%=combustorEtcQuantity1==null?"":combustorEtcQuantity1 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName2CustomerSaftyCheckInsert" value="<%=combustorEtcName2==null?"":combustorEtcName2 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity2CustomerSaftyCheckInsert" value="<%=combustorEtcQuantity2==null?"":combustorEtcQuantity2 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
    </tr>
	<tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName3CustomerSaftyCheckInsert" value="<%=combustorEtcName3==null?"":combustorEtcName3 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity3CustomerSaftyCheckInsert" value="<%=combustorEtcQuantity3==null?"":combustorEtcQuantity3 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtCombustorEtcName4CustomerSaftyCheckInsert" value="<%=combustorEtcName4==null?"":combustorEtcName4 %>" maxLength="6" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="number" data-role="none" id="txtCombustorEtcQuantity4CustomerSaftyCheckInsert" value="<%=combustorEtcQuantity4==null?"":combustorEtcQuantity4 %>" maxLength="6" style="width: 70% ; text-align: right ; font-size: 14px ; border: 1px white ; " onclick=""  /> 개</td>
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
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">가. 가스누출 여부와 마감조치 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable1CustomerSaftyCheckInsert" id="selectAcceptable1CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable1)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable1)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable1)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">나. 검사품의 검사표시 유무</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable2CustomerSaftyCheckInsert" id="selectAcceptable2CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable2)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable2)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable2)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">다. 중간밸브 연소기마다 설치여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable3CustomerSaftyCheckInsert" id="selectAcceptable3CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable3)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable3)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable3)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">라. 호스 T형 연결금지 및 밴드 접속 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable4CustomerSaftyCheckInsert" id="selectAcceptable4CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable4)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable4)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable4)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">마. 보일러, 온수기 설치규정 준수 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable5CustomerSaftyCheckInsert" id="selectAcceptable5CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable5)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable5)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable5)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">바.  전용보일러실에 보일러 설치 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable6CustomerSaftyCheckInsert" id="selectAcceptable6CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable6)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable6)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable6)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">사. 배기통재료 내식성, 불연성 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable7CustomerSaftyCheckInsert" id="selectAcceptable7CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable7)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable7)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable7)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">아. 배기통의 막힘 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable8CustomerSaftyCheckInsert" id="selectAcceptable8CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable8)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable8)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable8)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">자. 용기의 옥내설치(보관) 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable9CustomerSaftyCheckInsert" id="selectAcceptable9CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable9)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable9)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable9)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">차. 중간밸브까지 배관 적합설치 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable12CustomerSaftyCheckInsert" id="selectAcceptable12CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable12)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable12)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable12)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">카. 그 밖에 가스사고 유발 우려 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable10CustomerSaftyCheckInsert" id="selectAcceptable10CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable10)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable10)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable10)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">타. 가스용품의 권장사용기간 경과 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable11CustomerSaftyCheckInsert" id="selectAcceptable11CustomerSaftyCheckInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable11)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable11)?"selected":"" %> >적합</option>
				<option value="2" <%= "2".equals(acceptable11)?"selected":"" %>>부적합</option>
			</select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">개선통지사항</td>
        <td colspan="2" style="width: 70px ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtNotifyRemark1CustomerSaftyCheckInsert" value="<%=notifyRemark1==null?"":notifyRemark1 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="width: 70px ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtNotifyRemark2CustomerSaftyCheckInsert" value="<%=notifyRemark2==null?"":notifyRemark2 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td rowspan="2" style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">가스용품 교체<br />권장사항</td>
        <td colspan="2" style="width: 70px ; font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtRecommendation1CustomerSaftyCheckInsert" value="<%= recommendation1==null?"":recommendation1 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td colspan="2" style="width: 70px ; font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " ><input type="text" data-role="none" id="txtRecommendation2CustomerSaftyCheckInsert" value="<%= recommendation2==null?"":recommendation2 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><span id="spnSignCustomerSaftyCheckInsert"><%=isSigned?"서명됨":"서명안됨" %></span></td>
        <td colspan="2" style="font-size:14px  ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<input id="btnSignCustomerSaftyCheckInsert" data-mini="true" type="button" value="<%=isSigned?"서명보기":"서명등록" %>" data-corners="false" data-inline="true" onclick="showSignatureCustomerSaftyCheckInsert()" />
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
			<input data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveCustomerSaftyCheckInsert(false)" ></input>
		</td>
	</tr>
</table>
<%
			}
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
