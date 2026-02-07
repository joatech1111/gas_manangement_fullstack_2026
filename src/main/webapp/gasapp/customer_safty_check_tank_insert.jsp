<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheckTank" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckTankMap" %>
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
			/*
			CustomerSaftyCheckTankMap customerSaftyCheckTanks = (CustomerSaftyCheckTankMap)session.getAttribute("CUSTOMER_SAFTY_CHECK_TANK_LIST");
			//만일 안전점검내역이 조회된 적이 없다면
			if (customerSaftyCheckTanks==null){
				customerSaftyCheckTanks = BizCustomerSaftyCheck.getInstance().getCustomerSaftyCheckTanks(serverIp, catalogName, areaCode, customerSearch.getCustomerCode(), null);
				session.setAttribute("CUSTOMER_SAFTY_CHECK_TANK_LIST", customerSaftyCheckTanks);
			}
			
			if (customerSaftyCheckTanks!=null) {
				CustomerSaftyCheckTank customerSaftyCheckTank = customerSaftyCheckTanks.getLatestCustomerSaftyCheckTank();
				if (customerSaftyCheckTank == null) { // 최초 안전점검이라면,
			*/
			// 최근 안전점검 내역 가져오기			
			CustomerSaftyCheckTank customerSaftyCheckTank = BizCustomerSaftyCheck.getInstance().getCustomerSaftyCheckTankLast(serverIp, catalogName, areaCode, customerSearch.getCustomerCode());
			if (customerSaftyCheckTank == null) {	// 이전에 안전점검을 한 내역이 없다면, 기본 안전점검 내역설정
				customerSaftyCheckTank = new CustomerSaftyCheckTank();
				customerSaftyCheckTank.setSequenceNumber("T001");
				
				// 디폴트를 일부 항목은 적합으로 설정
				customerSaftyCheckTank.setAcceptable1("0");
				customerSaftyCheckTank.setAcceptable2("0");
				customerSaftyCheckTank.setAcceptable3("0");
				customerSaftyCheckTank.setAcceptable4("0");
				customerSaftyCheckTank.setAcceptable5("0");
				customerSaftyCheckTank.setAcceptable6("0");
				customerSaftyCheckTank.setAcceptable7("0");
				customerSaftyCheckTank.setAcceptable8("0");
				customerSaftyCheckTank.setAcceptable9("0");
				customerSaftyCheckTank.setAcceptable10("0");
				customerSaftyCheckTank.setAcceptable11("0");
				customerSaftyCheckTank.setAcceptable12("0");
			}
			/*
			else { // 최종데이타가 있을 경우 순번 처리
				String prevSequenceNumber = customerSaftyCheckTank.getSequenceNumber().substring(1).replaceAll("0", "");
				int intSequenceNumber = new Integer(prevSequenceNumber).intValue() +1;
				String newSequenceNumber = "" + intSequenceNumber;
				if (newSequenceNumber.length() == 1) {
					newSequenceNumber = "00" + newSequenceNumber;
				} else if (newSequenceNumber.length() == 2) {
					newSequenceNumber = "0" + newSequenceNumber;
				}
				customerSaftyCheckTank.setSequenceNumber("T"+newSequenceNumber);
			}
			*/
			if (customerSaftyCheckTank.getSequenceNumber() == null) {
				customerSaftyCheckTank.setSequenceNumber("T001");
			}
			
			// 점검일자 오늘날짜로 설정함.
			customerSaftyCheckTank.setScheduledCheckDate(StringUtil.dateFormatStr(null, ""));
			customerSaftyCheckTank.setEmployeeCode(appUser.getEmployeeCode());
			customerSaftyCheckTank.setEmployeeName(appUser.getEmployeeName());
			
			if ((customerSaftyCheckTank != null) && (customerSearch!=null)){
				String contractNumber = customerSearch.getContractNumber();
				String address1 = customerSearch.getAddress1();
				String address2 = customerSearch.getAddress2();
				
				String customerCode = customerSaftyCheckTank.getCustomerCode();                                // 거래처코드 Key
				String sequenceNumber = customerSaftyCheckTank.getSequenceNumber();                            // 항번 Key
				String scheduledCheckDate = customerSaftyCheckTank.getScheduledCheckDate();                    // 정기점검일
				String employeeCode = customerSaftyCheckTank.getEmployeeCode();                                // 점검 사원코드
				String employeeName = customerSaftyCheckTank.getEmployeeName();                                // 점검 사원명

				String tankCapacity1 = customerSaftyCheckTank.getTankCapacity1();                              // 탱크용량1
				String tankCapacity2 = customerSaftyCheckTank.getTankCapacity2();                              // 탱크용량2
				String acceptable1 = customerSaftyCheckTank.getAcceptable1();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable1Comment = customerSaftyCheckTank.getAcceptable1Comment();                    // 비고
				String acceptable2 = customerSaftyCheckTank.getAcceptable2();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable2Comment = customerSaftyCheckTank.getAcceptable2Comment();                    // 비고
				String acceptable3 = customerSaftyCheckTank.getAcceptable3();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable3Comment = customerSaftyCheckTank.getAcceptable3Comment();                    // 비고
				String acceptable4 = customerSaftyCheckTank.getAcceptable4();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable4Comment = customerSaftyCheckTank.getAcceptable4Comment();                    // 비고
				String acceptable5 = customerSaftyCheckTank.getAcceptable5();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable5Comment = customerSaftyCheckTank.getAcceptable5Comment();                    // 비고
				String acceptable6 = customerSaftyCheckTank.getAcceptable6();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable6Comment = customerSaftyCheckTank.getAcceptable6Comment();                    // 비고
				String acceptable7 = customerSaftyCheckTank.getAcceptable7();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable7Comment = customerSaftyCheckTank.getAcceptable7Comment();                    // 비고
				String acceptable8 = customerSaftyCheckTank.getAcceptable8();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable8Comment = customerSaftyCheckTank.getAcceptable8Comment();                    // 비고
				String acceptable9 = customerSaftyCheckTank.getAcceptable9();                                  // 0.해당없음, 1.적합, 2.부적합
				String acceptable9Comment = customerSaftyCheckTank.getAcceptable9Comment();                    // 비고
				String acceptable10Content = customerSaftyCheckTank.getAcceptable10Content();                  // 내용
				String acceptable10 = customerSaftyCheckTank.getAcceptable10();                                // 0.해당없음, 1.적합, 2.부적합
				String acceptable10Comment = customerSaftyCheckTank.getAcceptable10Comment();                  // 비고
				String acceptable11Content = customerSaftyCheckTank.getAcceptable11Content();                  // 내용
				String acceptable11 = customerSaftyCheckTank.getAcceptable11();                                // 0.해당없음, 1.적합, 2.부적합
				String acceptable11Comment = customerSaftyCheckTank.getAcceptable11Comment();                  // 비고
				String acceptable12Content = customerSaftyCheckTank.getAcceptable12Content();                  // 내용
				String acceptable12 = customerSaftyCheckTank.getAcceptable12();                                // 0.해당없음, 1.적합, 2.부적합
				String acceptable12Comment = customerSaftyCheckTank.getAcceptable12Comment();                  // 비고
				String employeeComment1 = customerSaftyCheckTank.getEmployeeComment1();                        // 점검자의견1
				String employeeComment2 = customerSaftyCheckTank.getEmployeeComment2();                        // 점검자의견2
				String customerName = customerSaftyCheckTank.getCustomerName();                                // 업소확인자
				//String signatureYn = customerSaftyCheckTank.getSignatureYn();                                  // 사인유무
				String signatureYn = "N";
				String userId = customerSaftyCheckTank.getUserId();                                            // APP 사용자명
				String modifyDate = customerSaftyCheckTank.getModifyDate();                                    // 등록/수정일자				
%>
<input type="hidden" id="hdnKeyCustomerSaftyCheckTankInsert" value="<%=key %>" />
<input type="hidden" id="hdnSequenceNumberCustomerSaftyCheckTankInsert" value="<%=sequenceNumber %>" />
<input type="hidden" id="hdnInsertModeCustomerSaftyCheckTankInsert" value="0" />
<input type="hidden" id="hdnSignatureImageCustomerSaftyCheckTankInsert" />
<table style="width: 100% ; border: 0px solid #999999 ; border-collapse: collapse;">
    <tr>
        <td style="width: 60px ; font-size:14px ;">점검일자</td>
        <td colspan="4" style="font-size:14px ; color: #3333FF ; "><input type="text" data-mini="true" id="txtScheduledCheckDateCustomerSaftyCheckTankInsert" value="<%=StringUtil.dateFormatStr(scheduledCheckDate) %>" maxLength="5" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerSaftyCheckTankInsert', 'txtScheduledCheckDateCustomerSaftyCheckTankInsert')" /></td>
    </tr>
    <tr>
        <td style="width: 60px ; font-size:14px ;">점검사원</td>
        <td colspan="4" style="font-size:14px ; color: #3333FF ; ">
 			<input type="hidden" id="hdnEmployeeNameCustomerSaftyCheckTankInsert" value="<%=userEmployeeName %>" />
 <%
	if (changeEmployee) { 
%>
			<select id="selectEmployeeCodeCustomerSaftyCheckTankInsert"  data-mini="true" style="font-size: 14px ; color: blue ; " onchange="changeEmployeeCustomerSaftyCheckTankInsert()">
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
			<input type="hidden" id ="selectEmployeeCustomerSaftyCheckTankInsert" value="<%=userEmployeeCode %>" />
			<%= userEmployeeName %>
<%
	}
%>
        </td>
	</tr>
	<tr>
        <td style="width: 60px; font-size: 14px;">탱크용량</td>
        <td style="font-size: 14px; color: #3333FF;">
        	<input type="number" data-mini="true" id="txtTankCapacity1CustomerSaftyCheckTankInsert" value="<%=tankCapacity1==null?"":tankCapacity1 %>" maxLength="8" style="width: 100%; color: blue; text-align: right;" />
        </td>
        <td style="font-size: 14px;">kg</td>
        <td style="font-size: 14px; color: #3333FF;">
        	<input type="number" data-mini="true" id="txtTankCapacity2CustomerSaftyCheckTankInsert" value="<%=tankCapacity2==null?"":tankCapacity2 %>" maxLength="8" style="width: 100%; color: blue; text-align: right;" />
        </td>
        <td style="font-size: 14px;">kg</td>
    </tr>
</table>

<table style="width: 100% ; border-collapse: collapse;">
	<tr>
		<th />
		<th style="width: 70px" />
		<th style="width: 70px" />
	</tr>
    <tr>
        <td style="font-size:14px ; text-align: center ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">점검내용</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">결과</td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">비고</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">1. 저장탱크 경계표시 및 도색</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable1CustomerSaftyCheckTankInsert" id="selectAcceptable1CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable1)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable1)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable1)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable1CommentCustomerSaftyCheckTankInsert" value="<%=acceptable1Comment==null?"":acceptable1Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">2. 저장탱크 화기와의 거리</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable2CustomerSaftyCheckTankInsert" id="selectAcceptable2CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable2)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable2)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable2)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable2CommentCustomerSaftyCheckTankInsert" value="<%=acceptable2Comment==null?"":acceptable2Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">3. 가스누설경보기 설치 및 상태</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable3CustomerSaftyCheckTankInsert" id="selectAcceptable3CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable3)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable3)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable3)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable3CommentCustomerSaftyCheckTankInsert" value="<%=acceptable3Comment==null?"":acceptable3Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">4. 배관의 도색, 고정, 표시 상태</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable4CustomerSaftyCheckTankInsert" id="selectAcceptable4CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable4)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable4)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable4)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable4CommentCustomerSaftyCheckTankInsert" value="<%=acceptable4Comment==null?"":acceptable4Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">5. 안전밸브의 설치 및 작동 상태</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable5CustomerSaftyCheckTankInsert" id="selectAcceptable5CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable5)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable5)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable5)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable5CommentCustomerSaftyCheckTankInsert" value="<%=acceptable5Comment==null?"":acceptable5Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">6. 정기검사 실시 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable6CustomerSaftyCheckTankInsert" id="selectAcceptable6CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable6)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable6)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable6)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable6CommentCustomerSaftyCheckTankInsert" value="<%=acceptable6Comment==null?"":acceptable6Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">7. 안전관리자 선임 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable7CustomerSaftyCheckTankInsert" id="selectAcceptable7CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable7)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable7)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable7)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable7CommentCustomerSaftyCheckTankInsert" value="<%=acceptable7Comment==null?"":acceptable7Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">8. 손해배상 보험가입 여부</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable8CustomerSaftyCheckTankInsert" id="selectAcceptable8CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable8)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable8)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable8)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable8CommentCustomerSaftyCheckTankInsert" value="<%=acceptable8Comment==null?"":acceptable8Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">9. 소화기 비치 여부 및 개수</td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable9CustomerSaftyCheckTankInsert" id="selectAcceptable9CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable9)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable9)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable9)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable9CommentCustomerSaftyCheckTankInsert" value="<%=acceptable9Comment==null?"":acceptable9Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">10. <input type="text" data-role="none" id="txtAcceptable10ContentCustomerSaftyCheckTankInsert" value="<%=acceptable10Content==null?"":acceptable10Content %>" maxLength="32" style="width: 80% ; font-size: 14px ; border: 1px white ; " onclick=""/></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable10CustomerSaftyCheckTankInsert" id="selectAcceptable10CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable10)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable10)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable10)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable10CommentCustomerSaftyCheckTankInsert" value="<%=acceptable10Comment==null?"":acceptable10Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">11. <input type="text" data-role="none" id="txtAcceptable11ContentCustomerSaftyCheckTankInsert" value="<%=acceptable11Content==null?"":acceptable11Content %>" maxLength="32" style="width: 80% ; font-size: 14px ; border: 1px white ; " onclick=""/></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable11CustomerSaftyCheckTankInsert" id="selectAcceptable11CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable11)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable11)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable11)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable11CommentCustomerSaftyCheckTankInsert" value="<%=acceptable11Comment==null?"":acceptable11Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="font-size:14px ; text-align: left ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">12. <input type="text" data-role="none" id="txtAcceptable12ContentCustomerSaftyCheckTankInsert" value="<%=acceptable12Content==null?"":acceptable12Content %>" maxLength="32" style="width: 80% ; font-size: 14px ; border: 1px white ; " onclick=""/></td>
        <td style="width: 70px ; font-size:14px ; text-align: right ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
        	<select name="selectAcceptable12CustomerSaftyCheckTankInsert" id="selectAcceptable12CustomerSaftyCheckTankInsert"  data-role="none" data-mini="true" style="font-size: 14px ; color: blue ; " >
				<option value="0" <%= "0".equals(acceptable12)?"selected":"" %>>&nbsp;</option>
				<option value="1" <%= "1".equals(acceptable12)?"selected":"" %>>적합</option>
				<option value="2" <%= "2".equals(acceptable12)?"selected":"" %>>부적합</option>
			</select>
        </td>
        <td style="width: 70px ; font-size:14px ; text-align: center ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; "><input type="text" data-role="none" id="txtAcceptable12CommentCustomerSaftyCheckTankInsert" value="<%=acceptable12Comment==null?"":acceptable12Comment %>" maxLength="15" style="width: 90% ; font-size: 14px ; border: 1px white ; " onclick=""  /></td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #222222 ;border-collapse: collapse;">
	<tr>
		<th style="border-bottom: 1px solid #222222 ;"/>
	</tr>
	<tr>
        <td style="font-size:14px ;">점검자의견</td>
    </tr>
    <tr style="height: 40px; text-align: center;">
        <td style="font-size:14px ; border-left: 0px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;"><input type="text" data-role="none" id="txtEmployeeComment1CustomerSaftyCheckTankInsert" value="<%=employeeComment1==null?"":employeeComment1 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
    <tr style="height: 40px; text-align: center;">
        <td style="font-size:14px ; border-left: 0px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;"><input type="text" data-role="none" id="txtEmployeeComment2CustomerSaftyCheckTankInsert" value="<%=employeeComment2==null?"":employeeComment2 %>" maxLength="32" style="width: 90% ; font-size: 14px ; border: 1px white ; " /></td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-collapse: collapse;">
	<tr>
		<th colspan="3" style="border-bottom: 1px solid #222222 ;"/>
	</tr>
    <tr>
        <td style="width: 70px; font-size: 14px;">업소 확인</td>
        <td style="font-size: 14px; color: #3333FF;"><input type="text" data-mini="true" id="txtCustomerNameCustomerSaftyCheckTankInsert" value="<%=customerName==null?"":customerName %>" maxLength="5" style="width: 80% ; color: blue ; " /></td>
        <td style="font-size: 14px;" >
        	<input id="btnSignCustomerSaftyCheckTankInsert" data-mini="true" type="button" value="<%= "Y".equals(signatureYn)?"서명보기":"서명등록" %>" data-corners="false" data-inline="true" onclick="showSignatureCustomerSaftyCheckTankInsert()" />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
			<input data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveCustomerSaftyCheckTankInsert(false)" ></input>
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
