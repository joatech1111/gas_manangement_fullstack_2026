<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CidList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CidListMap" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.bean.CollectTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.CollectTypeCodeMap" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerItem" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerItemMap" %>
<%@ page import="com.joainfo.gasmax.biz.*" %>
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
		String insertMode = (String)request.getParameter("insertMode"); // 0:수정 1:신규
		String keyValue = (String)request.getParameter("key");
		String address = "";

//		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
//		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));

		String sessionToken = request.getParameter("sessionToken");
		AppUser appUser = RedisUtil.getUserFromSessionToken(sessionToken);

		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String grantCode = appUser.getMenuPermission();
			String gasType = appUser.getGasType();
			String displayStyle = "";
			if ("HIGH".equals(gasType)){
//				displayStyle = "display: none ; ";
			}

			String cidDateTime = StringUtil.nowDateTimeStr();
			String cidDate = cidDateTime.substring(0,8);
			String cidDateStr = StringUtil.dateFormatStr(cidDate, "-");
			String cidTime = cidDateTime.substring(8,12);
			String hour = cidTime.substring(0,2);
			String minute = cidTime.substring(2,4);
			int hh = new Integer(hour).intValue();
			if (hh>12) {
				hour = "오후 " + (hh-12);
			} else if (hh==12) {
				hour = "오후 " + hh;
			} else {
			    hour = "오전 " + hh;
			}
			String cidTimeStr = hour + ":" + minute;
			String sequenceNumber = "";
			String saleType = "0";
			String customerCode = "";
			String customerName = "";
			String phoneNumber = "";
			String itemCode = "";
			String itemName = "";
			String saleQuantity = "0";
			String withdrawQuantity = "0";
			String cidPrice = "0";
			String priceType = "1"; // default 1: 환경단가
			String vatType = "0";
			if ("LPG".equals(gasType)){		// LPG인 경우에는 "VAT포함(1)" 으로 설정
				vatType = "1";
			}
			String cidAmount = "0";
			String taxAmount = "0";
			String totalAmount = "0";
			String discountAmount = "0";
			String collectAmount = "0";
			String unpaidAmount = "0";
			String employeeCode = "";
			String employeeName = "";
			String remark = "";
			String deliveryYesNo = "0"; //0:false 1:true
			String completeYesNo = "0"; //0:false 1:true
			String collectType = "0";

			if ("0".equals(insertMode)){ // 수정일 경우
				CidListMap cidLists = (CidListMap)session.getAttribute("CID_LIST");
				CidList cidList = cidLists.getCidList(keyValue);

				cidDate = cidList.getCidDate();
				cidDateStr = StringUtil.dateFormatStr(cidDate, "-");
				cidTime = cidList.getCidTime();
				hour = cidTime.substring(0,2);
				minute = cidTime.substring(2,4);
				hh = new Integer(hour).intValue();
				if (hh>12) {
					hour = "오후 " + (hh-12);
				} else if (hh==12) {
					hour = "오후 " + hh;
				} else {
				    hour = "오전 " + hh;
				}
				cidTimeStr = hour + ":" + minute;
				sequenceNumber = cidList.getSequenceNumber();
				saleType = cidList.getSaleTypeCode();
				customerCode = cidList.getCustomerCode();
				customerName = cidList.getCustomerName();
				phoneNumber = cidList.getPhoneNumber();
				if (!"".equals(phoneNumber)) {
					if (!"0".equals(phoneNumber.substring(0,1))){
						phoneNumber = appUser.getPhoneAreaNumber() + "-" + phoneNumber;
					}
				}
				itemCode = cidList.getItemCode();
				itemName = cidList.getItemName();
				saleQuantity = cidList.getSaleQuantity();
				withdrawQuantity = cidList.getWithdrawQuantity();
				cidPrice = cidList.getCidPrice();
				priceType = "1"; // default 1: 환경단가
				vatType = cidList.getVatType();
				cidAmount = cidList.getCidAmount();
				taxAmount = cidList.getTaxAmount();
				totalAmount = cidList.getTotalAmount();
				discountAmount = cidList.getDiscountAmount();
				collectAmount = cidList.getCollectAmount();
				unpaidAmount = cidList.getUnpaidAmount();
				employeeCode = cidList.getEmployeeCode();
				employeeName = cidList.getEmployeeName();
				remark = cidList.getRemark();
				deliveryYesNo = cidList.getDeliveryYesNo();
				completeYesNo = cidList.getCompleteYesNo();
				collectType = cidList.getCollectType();
				if ((collectType == null) || ("".equals(collectType))){
					collectType = "0";
				}
			}
			//todo: 세션에서 사원목록 가져오기
			//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");


			EmployeeCodeMap employeeCodes=  RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class);



			if(employeeCodes==null){
				String keyword = "";
				EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				employeeCodes = newEmployeeCodes;
				session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
			}

			// 세션에서 일반품목 판매용 입금방법 목록 가져오기
			CollectTypeCodeMap collectTypeCodes = (CollectTypeCodeMap)session.getAttribute("WEIGHT_ITEM_COLLECT_TYPE_CODE");
			if(collectTypeCodes==null){
				String keyword = "";
				CollectTypeCodeMap newCollectTypeCodes = BizCollectTypeCode.getInstance().getWeightItemCollectTypeCodes(serverIp, catalogName);
				collectTypeCodes = newCollectTypeCodes;
				session.setAttribute("WEIGHT_ITEM_COLLECT_TYPE_CODE", newCollectTypeCodes);
			}

			// 통화구분 목록 가져오기
			HashMap<String, String> saleTypes = BizCidList.getInstance().selectSaleTypes(serverIp, catalogName);
			String itemBalance= "0";
			String nowBalance = "0";
			String itemSpec = "";
			if ("".equals(itemCode)){ // 품목이 없을 때
			} else { // 품목코드가 있을 때
				// 품목 정보 가져오기
				CustomerItem customerItem = BizCustomerItem.getInstance().getCustomerItemWeightHPG(serverIp, catalogName, areaCode, customerCode, itemCode);
				if (customerItem != null){
					itemBalance = customerItem.getItemBalance();
					if ((itemBalance == null) || ("".equals(itemBalance))) {
						itemBalance = "0";
					}
					nowBalance = "" + ((new Integer(itemBalance).intValue()) + (new Integer(saleQuantity).intValue()) - (new Integer(withdrawQuantity).intValue()));
					itemSpec = customerItem.getItemSpec();
				}
			}
			if ("".equals(customerCode)) {
%>
					<div id="customerSummaryManageCidEdit">
						<a href="#" onclick="changeCustomerManageCidEdit()">
							<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-bottom: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
								<tr>
									<td style="text-align: left ; vertical-align: center ;">
								        &nbsp;
										&nbsp;
										<br/>
										&nbsp; 거래처 선택
										&nbsp;
										<br/>
										&nbsp;
										<br/>
								 		&nbsp;
										&nbsp;
										&nbsp;
										&nbsp;
									</td>
								</tr>
							</table>
						</a>
					</div>
<%
			} else {
				CustomerSearch customerSearch = BizCustomerSearch.getInstance().getCustomerSearch(serverIp, catalogName, areaCode, customerCode, "A", "", "", grantCode);
				if (customerSearch!=null){
					String customerType = customerSearch.getCustomerType();
	//				String customerTypeName = customerSearch.getCustomerTypeName();
					String customerTypeCode = (new Integer(customerType).intValue() > 4)?"1":customerType;
					String customerTypeIcon = "images/lbl_customer_type_"+ customerTypeCode + ".png";
	//				String customerCode = customerSearch.getCustomerCode();
					String customerStatusCode = customerSearch.getCustomerStatusCode();
					String customerNameStyle = (customerStatusCode!="1")?"#555555":"#222222";
					String mobileNumber = customerSearch.getMobileNumber();
					address = customerSearch.getAddress1() + " " + customerSearch.getAddress2();
					String containerDeposit = customerSearch.getContainerDeposit();
					String weightReceivable = customerSearch.getWeightReceivable();
					String volumeReceivable = customerSearch.getVolumeReceivable();
					String receivable = String.format("%,d", new Integer(StringUtil.stringReplace(weightReceivable)).intValue() + new Integer(StringUtil.stringReplace(volumeReceivable)).intValue());
					String latestSaftyCheckDate = customerSearch.getLatestSaftyCheckDate();
					latestSaftyCheckDate = StringUtil.dateFormatStr(latestSaftyCheckDate);
					// 보증금이 있을 경우 색깔 변경
					String containerDepositStyle = !"0".equals(containerDeposit)?"#FF0000":"#222222";

					// 미수금(중량미수, 체적미수)이 있을 경우 색깔 변경
					String receivableStyle =!"0".equals(receivable)?"#FF0000":"#222222";

					// 안전점검 대상 체크
	//				String saftyCheckYesNoText = "안전점검대상";
					String latestSaftyCheckDateStyle = "#222222";
					String latestSaftyCheckDateIcon = "";
					if (!"".equals(latestSaftyCheckDate)){
						int diff = StringUtil.dateDifference(latestSaftyCheckDate);
						int satryCheckPeriod = 365; // 체적이 아니면 1년마다 체크
						if ("0".equals(customerType)) satryCheckPeriod = 183; // 체적일 때 6개월 마다 체크
						if (diff >=  satryCheckPeriod) latestSaftyCheckDateStyle = "#FF0000"; // 안전점검 체크 기간이면 빨간색으로.
						latestSaftyCheckDateIcon = "<img src=\"images/lbl_latest_safty_check.png\" />";
					}
%>
					<div id="customerSummaryManageCidEdit">
						<a href="#" onclick="<%="0".equals(insertMode)?"$('#hdnCallPageDiaglogCustomerBizMenu').attr('value', 'pageManageCidEdit');showDialogCustomerBizMenu('" + customerCode + "', '" + customerType + "')":"changeCustomerManageCidEdit()"%>">
							<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-bottom: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
								<tr>
									<td style="text-align: left ; vertical-align: center ;">
								        <img src="<%=customerTypeIcon %>" ></img>
										<span style="color: <%=customerNameStyle %> ; font-size:16px ;"><%= customerName %></span>
										<br/>
										<a href="tel:<%=phoneNumber %>" style="color: #3300FF ; font-size:14px ;"><%= phoneNumber %></a>
										<a href="tel:<%=mobileNumber %>" style="color: #3300FF ; font-size:14px ;"><%= mobileNumber %></a>
										<br/>
										<span style="color:#222222 ; font-size:14px ;"><%= address %></span>
										<br/>
<%
	if ("LPG".equals(gasType)) {
%>
				 		<%= latestSaftyCheckDateIcon %>
						<span style="color: <%= latestSaftyCheckDateStyle %> ; font-size:14px ;"><%= latestSaftyCheckDate %></span>
<%
	}
%>
										<img src="images/lbl_container_deposit.png" /><span style="color: <%=containerDepositStyle%> ; font-size:14px ;"> <%= containerDeposit %> 원</span>
										<img src="images/lbl_unpaid.png" /><span style="color: <%=receivableStyle %> ; font-size:14px ;"> <%= receivable %> 원</span>
									</td>
								</tr>
							</table>
						</a>
					</div>
<%
				}else{
%>
					<div id="customerSummaryManageCidEdit">
						<a href="#" onclick="changeCustomerManageCidEdit()">
							<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-bottom: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
								<tr>
									<td style="text-align: left ; vertical-align: center ;">
								        &nbsp;
										&nbsp;
										<br/>
										&nbsp; 거래처 선택
										&nbsp;
										<br/>
										&nbsp;
										<br/>
								 		&nbsp;
										&nbsp;
										&nbsp;
										&nbsp;
									</td>
								</tr>
							</table>
						</a>
					</div>
<%
				}
			}
%>
<input type="hidden" id="hdnKeyManageCidEdit" value="<%=keyValue %>" />
<input type="hidden" id="hdnAddressManageCidEdit" value="<%=address %>" />
<input type="hidden" id="hdnInsertModeManageCidEdit" value="<%=insertMode %>" />
<input type="hidden" id="hdnCustomerCodeManageCidEdit" value="<%=customerCode %>" />
<input type="hidden" id="hdnCustomerNameManageCidEdit" value="<%=customerName %>" />
<input type="hidden" id="hdnSequenceNumberManageCidEdit" value="<%=sequenceNumber %>" />
<input type="hidden" id="hdnPhoneNumberManageCidEdit" value="<%=phoneNumber %>" />
<input type="hidden" id="hdnCidDateManageCidEdit" value="<%=cidDate %>" />
<input type="hidden" id="hdnCidTimeManageCidEdit" value="<%=cidTime %>" />
<input type="hidden" id="hdnItemCodeManageCidEdit" value="<%=itemCode %>" />
<input type="hidden" id="hdnItemNameManageCidEdit" value="<%=itemName %>" />
<input type="hidden" id="hdnPriceTypeManageCidEdit" value="<%=priceType %>" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse; background-color: yellow ; ">
							<tr>
								<td style="font-size:14px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ;" ><span style="font-size:14px ; "><%=cidDateStr + " " + cidTimeStr %></span></td>
								<td rowspan="2" style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #999999 ; border-right: 0px solid #999999 ;">
									<a id="aSmsPhoneNumberManageCidEdit" href="sms:<%=phoneNumber %>" data-role="none" type="button"><img src="images/sms0.png" style="width: 30px ; "/></a>
								</td>
								<td rowspan="2" style="width: 50px ; text-align: center ; font-size:14px ; border-bottom: 0px solid #999999 ; border-left: 0px solid #999999 ; border-right: 1px solid #999999 ;">
									<a id="aTelPhoneNumberManageCidEdit"  href="tel:<%=phoneNumber %>" data-role="none" type="button"><img src="images/tel0.png" style="width: 30px ; "/></a>
								</td>
							</tr>
							<tr>
								<td style="font-size:14px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ;"><span id="spnPhoneNumberManageCidEdit" style="font-size:16px ; color: blue ; font-weight: bold ; "><%=phoneNumber %></span></td>
							</tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">통화구분</td>
						        <td colspan="2" style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">품목</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">단가</td>
						    </tr>
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectSaleTypeManageCidEdit"  data-mini="true" style="font-size: 14px ; ">
<%
		// 통화구분을 선택 박스에 채우기
		java.util.Iterator<String> iterator = saleTypes.keySet().iterator();
		while (iterator.hasNext()) {
			String saleTypeCode = iterator.next();
			String saleTypeName =  saleTypes.get(saleTypeCode);
%>
										<option value="<%= saleTypeCode %>"  <%= saleType.equals(saleTypeCode)?"selected":"" %> ><%= saleTypeName %></option>
<%
		}
%>
									</select>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtItemManageCidEdit" data-mini="true" value="<%=itemName %>" style="width: 90% ; font-size: 14px ; text-align: left ; color: blue ; " />
						        </td>
								<td style="text-align: left ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<a href="#" data-role="button" id="btnItemSearchManageCidEdit" data-mini="true" onclick="clickItemManageCidEdit('<%=insertMode %>')" data-theme="b" data-icon="search" data-iconpos="notext" data-inset="true">검색</a>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text"  data-role="none" id="txtCidPriceManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(cidPrice) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateTotalAmountManageCidEdit()" onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						    </tr>
						</table>
					<div style="<%=displayStyle %>">
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전재고</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">납품</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">회수</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">잔고</td>
							</tr>
							<tr style="height: 40px ; ">
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtItemBalanceManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(itemBalance) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " />
								</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number"  data-role="none" id="txtSaleQuantityManageCidEdit" data-mini="true" value="<%=StringUtil.stringReplace(saleQuantity) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="changeSaleQuantityManageCidEdit()" onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number"  data-role="none" id="txtWithdrawQuantityManageCidEdit" data-mini="true" value="<%=StringUtil.stringReplace(withdrawQuantity) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " onchange="calculateTotalAmountManageCidEdit()" onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;  background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtNowBalanceManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(nowBalance) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " />
						        </td>
							</tr>
						</table>
						<br />
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">VAT</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">공급액</td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">세액</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">합계</td>
						    </tr>
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectVatTypeManageCidEdit"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="calculateTotalAmountManageCidEdit()" >
										<option value="0" <%="0".equals(vatType)?"selected":"" %> >별도</option>
										<option value="1" <%="1".equals(vatType)?"selected":"" %> >포함</option>
										<option value="2" <%="2".equals(vatType)?"selected":"" %> >비과세</option>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtCidAmountManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(cidAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtTaxAmountManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(taxAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtTotalAmountManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(totalAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금방법</td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금액</td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">D/C</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">미입금액</td>
						    </tr>
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectCollectTypeManageCidEdit"  data-mini="true" style="font-size: 14px ; " onchange="changeCollectTypeManageCidEdit()" >
<%
		// 입금방법 목록을 선택 박스에 채우기
		iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CollectTypeCode collectTypeCode =  collectTypeCodes.getCollectTypeCode(key);
			String collectTypeCodeStr = collectTypeCode.getCollectTypeCode();
			String collectTypeNameStr = collectTypeCode.getCollectTypeName();
			String selected = "";
			if (collectType.equals(collectTypeCodeStr)) {
				selected = "selected";
			}
%>
										<option value="<%= collectTypeCodeStr %>" <%=selected%> ><%= collectTypeNameStr %></option>
<%
		}
%>
									</select>
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; " >
						        	<input type="number"  data-role="none" id="txtCollectAmountManageCidEdit" data-mini="true" value="<%=StringUtil.stringReplace(collectAmount) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateUnpaidAmountManageCidEdit()" onclick="focusNumber(this)" onblur="blurNumber(this)" <%="1".equals(completeYesNo)?"readonly":"" %> />
						        </td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number"  data-role="none" id="txtDiscountAmountManageCidEdit" data-mini="true" value="<%=StringUtil.stringReplace(discountAmount) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateUnpaidAmountManageCidEdit()" onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text"  data-role="none" id="txtUnpaidAmountManageCidEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(unpaidAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">사원</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">메모</td>
						    </tr>
						    <tr>
						        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectEmployeeManageCidEdit"  data-mini="true" style="font-size: 14px ; ">
										<option value="NA" data-placeholder="true">미지정</option>
<%
		// 사원 목록을 선택 박스에 채우기
		iterator = employeeCodes.getEmployeeCodes().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			EmployeeCode employeeCd =  employeeCodes.getEmployeeCode(key);
			String employeeCodeStr = employeeCd.getEmployeeCode();
			String employeeNameStr = employeeCd.getEmployeeName();
			String selected = "";
			if (employeeCode.equals(employeeCodeStr)) {
				selected = "selected";
			}
%>
										<option value="<%= employeeCodeStr %>" <%=selected%> ><%= employeeNameStr %></option>
<%
		}
%>
									</select>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtRemarkManageCidEdit" data-mini="true" value="<%=remark %>" style="width: 90% ; font-size: 14px ; text-align: left ; color: blue ; " />
						        	<input type="button" data-role="none" id="btnRemarkSearchManageCidEdit" data-mini="true" value="검색" onclick="clickRemarkManageCidEdit()" style="display: none ; "/>
						        </td>
						    </tr>
						</table>
					</div>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
						    <tr style="height: 40px ; ">
						    	<td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">&nbsp;</td>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<fieldset data-role="controlgroup" data-mini="true">
										<input type="checkbox" id="ckbDeliveryManageCidEdit" <%="1".equals(deliveryYesNo)?"checked":""%>  onclick="clickDeliveryCheckBoxSaveManageCidEdit('<%=insertMode%>')"/>
										<label for="ckbDeliveryManageCidEdit">배달</label>
									</fieldset>
						        </td>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<fieldset data-role="controlgroup" data-mini="true">
							        	<input type="checkbox" id="ckbCompleteManageCidEdit" data-mini="true"  <%="1".equals(completeYesNo)?"checked":"" %>  onclick="clickCompleteCheckBoxSaveManageCidEdit('<%=insertMode%>')" />
										<label for="ckbCompleteManageCidEdit">완료</label>
									</fieldset>
						        </td>
						    	<td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">&nbsp;</td>
						    </tr>
						</table>
						<table style="width: 100% ; ">
							<tr style="text-align: center ; ">
								<td>
<%
	if ("0".equals(insertMode)) { // 수정일 때만 이전 다음 가능
%>
									<input type="button" data-mini="true" data-icon="arrow-l" id="btnPriorManageCidEdit" data-corners="false" data-inline="true" onclick="navigateManageCidEdit('<%=keyValue%>', 'prior')" value="이전"></input>
									<input type="button" data-mini="true" data-icon="arrow-r" id="btnNextManageCidEdit" data-corners="false" data-inline="true" onclick="navigateManageCidEdit('<%=keyValue%>', 'next')" value="다음"></input>
<%
	}
%>
<%
	String styleDisplaySaveButton = "";
	if ("1".equals(completeYesNo)){ // 완료 상태일 때 저장 버튼 안보이게
		styleDisplaySaveButton = "display: none ; ";
	}
%>
									<a href="#" data-role="button" id="btnSaveManageCidEdit" data-mini="true" data-corners="false" data-inline="true" data-icon="check" onclick="clickSaveManageCidEdit('<%=insertMode%>')" style="<%=styleDisplaySaveButton %>">저장</a>
<%
	String styleDisplayDeleteButton = "";
	if ("1".equals(completeYesNo)){ // 완료상태일 때 삭제버튼 안보이게
		styleDisplayDeleteButton = "display: none ; ";
	}
	if ("0".equals(insertMode)) { // 수정일 때만 삭제가능
%>
									<a href="#" data-role="button" id="btnDeleteManageCidEdit" data-mini="true" data-corners="false" data-inline="true" data-icon="delete" onclick="clickDeleteManageCidEdit()" style="<%=styleDisplayDeleteButton %>">삭제</a>
<%
	}
%>
								</td>
							</tr>
						</table>
						<div id="divMessageManageCidEdit"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
