<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
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
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userEmployeeCode = appUser.getEmployeeCode();
			String userEmployeeName = appUser.getEmployeeName();
			String gasType = appUser.getGasType();
			String menuPermission = appUser.getMenuPermission();
			String [] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
			boolean changeEmployee = "0".equals(permission[2])?true:false;
			//boolean changeEmployee=false;

			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			String customerCode = customerSearch.getCustomerCode();
			String customerName = customerSearch.getCustomerName();
			String buildingName = customerSearch.getBuildingName();
			String userName = customerSearch.getUserName();
			String vatType = customerSearch.getVatType();
			if (vatType == null || vatType.trim().length() == 0) {
				vatType = "0";		// 부가세적용방법이 미설정시에는 "VAT별도(0)" 으로 설정

				if ("LPG".equals(gasType)) {	// LPG인 경우에는 "VAT포함(1)" 으로 설정
					vatType = "1";
				}
			}

			// 세션에서 사원목록 가져오기
//			EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
			EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
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

			// 비고 목록 가져오기
			HashMap<String, String> remarks = BizCustomerWeightSale.getInstance().getCustomerWeightSaleRemarks(serverIp, catalogName, areaCode, customerCode, StringUtil.dateFormatStr(null, ""));

			// 세션에서 일반 품목 목록 가져오기
			CustomerItemMap customerItems = null;
			if ("HIGH".equals(gasType)) {
				customerItems = BizCustomerItem.getInstance().getCustomerItemWeightHPGs(serverIp, catalogName, areaCode, customerCode);
			} else {
				customerItems = BizCustomerItem.getInstance().getCustomerItemWeightLPGs(serverIp, catalogName, areaCode, customerCode);
			}
			int itemCount = customerItems.getCustomerItems().size();
%>
<input type="hidden" id="hdnItemCountCustomerSaleWeightInsertBatch" value="<%=itemCount %>" />
<input type="hidden" id="hdnCustomerNameNameCustomerSaleWeightInsertBatch" value="<%=customerName %>" />
<input type="hidden" id="hdnUserNameNameCustomerSaleWeightInsertBatch" value="<%=userName %>" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 60px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">판매일자</td>
								<td style="width: 110px ; font-size:14px ; border-bottom: 1px solid #999999 ;">
									<input type="text" data-mini="true" id="txtSaleDateCustomerSaleWeightInsertBatch" style="width: 100px ; font-size: 14px ; color: blue ; text-align: center ; " value="<%=StringUtil.dateFormatStr(null) %>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerSaleWeightInsert', 'txtSaleDateCustomerSaleWeightInsertBatch')" />
								</td>
								<td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ;">
									<a href="#" data-role="button" data-mini="true" data-icon="plus" data-theme="b" data-corners="true" data-inline="true" onclick="clickInsertItemCustomerSaleWeightInsertBatch()">추가</a>
								</td>
								<td style="font-size:14px ; border-bottom: 1px solid #999999 ;">&nbsp;
								</td>
							</tr>
						</table>
						<table id="tbItemListCustomerSaleWeightInsertBatch" style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
						    <tr>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">품명</td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">단가</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전일</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">납품</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">회수</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">잔고</td>
						    </tr>
<%
		// 품목 목록을 채우기
		int i = 0;
		String defaultCollectType = "A";//수금유형을 A. 외상으로
		java.util.Iterator<String> iterator = customerItems.getCustomerItems().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CustomerItem customerItem =  customerItems.getCustomerItem(key);
			String itemCodeStr = customerItem.getItemCode();
			String itemNameStr = customerItem.getItemName();
			String itemSpecStr = customerItem.getItemSpec();
			String salePrice = customerItem.getSalePrice();
			String itemBalance = customerItem.getItemBalance();
			String priceType = customerItem.getPriceType();
			String lastUnpaidAmount = customerItem.getLastUnpaidAmount();
			if (i == 0){
				if ("0".equals(lastUnpaidAmount)){ //최종 미수금이 없다면 수금 유형은 현금으로, 그렇지 않으면 A. 외상
					defaultCollectType = "0";
				}
			}
			i++;
%>
							<tr style="height: 40px ;" id="trItemCustomerSaleWeightInsertBatch<%=i %>">
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<a href="#" onclick="clickItemCustomerSaleWeightInsertBatch(<%= i %>)"><%=itemNameStr + " " + itemSpecStr %></a>
<input type="hidden" id="hdnSaleTypeCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnItemCodeCustomerSaleWeightInsertBatch<%=i %>" value="<%=itemCodeStr %>" />
<input type="hidden" id="hdnItemNameCustomerSaleWeightInsertBatch<%=i %>" value="<%=itemNameStr %>" />
<input type="hidden" id="hdnItemSpecCustomerSaleWeightInsertBatch<%=i %>" value="<%=itemSpecStr %>" />
<input type="hidden" id="hdnPriceTypeCustomerSaleWeightInsertBatch<%=i %>" value="<%=priceType %>" />
<input type="hidden" id="hdnVatTypeCustomerSaleWeightInsertBatch<%=i %>" value="<%=vatType %>" />
<input type="hidden" id="hdnSaleAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnTaxAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnTotalAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnCollectAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnDiscountAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnUnpaidAmountCustomerSaleWeightInsertBatch<%=i %>" value="0" />
<input type="hidden" id="hdnEmployeeCodeCustomerSaleWeightInsertBatch<%=i %>" value="<%=userEmployeeCode %>" />
<input type="hidden" id="hdnEmployeeNameCustomerSaleWeightInsertBatch<%=i %>" value="<%=userEmployeeName %>" />
<input type="hidden" id="hdnRemarkCodeCustomerSaleWeightInsertBatch<%=i %>" value="" />
<input type="hidden" id="hdnRemarkTextCustomerSaleWeightInsertBatch<%=i %>" value="" />
<input type="hidden" id="hdnCollectTypeCustomerSaleWeightInsertBatch<%=i %>" value="<%=defaultCollectType %>" />
								</td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtSalePriceCustomerSaleWeightInsertBatch<%=i %>" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(salePrice) %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateTotalAmountCustomerSaleWeightInsertBatch(<%=i %>)"  onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtItemBalanceCustomerSaleWeightInsertBatch<%=i %>" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(itemBalance) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ;  border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
								</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtSaleQuantityCustomerSaleWeightInsertBatch<%=i %>" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ;  border: 1px white ; "  onchange="changeSaleQuantityCustomerSaleWeightInsertBatch(<%=i %>)"  onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtWithdrawQuantityCustomerSaleWeightInsertBatch<%=i %>" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ;  border: 1px white ; "  onchange="calculateTotalAmountCustomerSaleWeightInsertBatch(<%=i %>)"  onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtNowBalanceCustomerSaleWeightInsertBatch<%=i %>" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(itemBalance) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ;  border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
							</tr>
<%
		}
%>
						</table>
						<br />
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">VAT</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">공급액</td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">세액</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">합계</td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectDefaultVatTypeCustomerSaleWeightInsertBatch"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="changeDefaultVatTypeCustomerSaleWeightInsertBatch()">
										<option value="0" <%="0".equals(vatType)?"selected":"" %> >VAT별도</option>
										<option value="1" <%="1".equals(vatType)?"selected":"" %> >VAT포함</option>
										<option value="2" <%="2".equals(vatType)?"selected":"" %> >비과세</option>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">
						        	<input type="text" data-role="none" id="txtSaleAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">
						        	<input type="text" data-role="none" id="txtTaxAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">
						        	<input type="text" data-role="none" id="txtTotalAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금방법</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금액</td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">D/C</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">미입금</td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectDefaultCollectTypeCustomerSaleWeightInsertBatch" data-mini="true" style="font-size: 14px ; " onchange="changeDefaultCollectTypeCustomerSaleWeightInsertBatch()" >
<%
		// 입금방법 목록을 선택 박스에 채우기
		iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CollectTypeCode collectTypeCode =  collectTypeCodes.getCollectTypeCode(key);
			String collectTypeCodeStr = collectTypeCode.getCollectTypeCode();
			String collectTypeNameStr = collectTypeCode.getCollectTypeName();
			String selected = "";
			if (defaultCollectType.equals(collectTypeCodeStr)) {
				selected = "selected";
			}
%>
										<option value="<%= collectTypeCodeStr %>" <%=selected%> ><%= collectTypeNameStr %></option>
<%
		}
%>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;  background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtCollectAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ;  background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;  background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtDiscountAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ;  background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtUnpaidAmountCustomerSaleWeightInsertBatch" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">사원</td>
						        <td colspan="2" style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">비고</td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
							<input type="hidden" id="hdnDefaultEmployeeNameCustomerSaleWeightInsertBatch" data-mini="true" value="<%= userEmployeeName %>" />
<%
	if (changeEmployee) {
%>
									<select id="selectDefaultEmployeeCustomerSaleWeightInsertBatch" data-mini="true" style="font-size: 14px ; " onchange="changeDefaultEmployeeCustomerSaleWeightInsertBatch()" >
										<option value="" data-placeholder="true">사원</option>
<%
		// 사원 목록을 선택 박스에 채우기
		iterator = employeeCodes.getEmployeeCodes().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			EmployeeCode employeeCode =  employeeCodes.getEmployeeCode(key);
			String employeeCodeStr = employeeCode.getEmployeeCode();
			String employeeNameStr = employeeCode.getEmployeeName();
			String selected = "";
			if (userEmployeeCode.equals(employeeCodeStr)) {
				selected = "selected";
			}
%>
										<option value="<%= employeeCodeStr %>" <%=selected%> ><%= employeeNameStr %></option>
<%
		}
%>
									</select>
<%
	} else {
%>
							<input type="hidden" id="selectDefaultEmployeeCustomerSaleWeightInsertBatch" data-mini="true" value="<%= userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtDefaultRemarkCustomerSaleWeightInsert" data-mini="true" value="" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; " onchange="changeRemarkCustomerSaleWeightInsertBatch()" maxlength="10"  onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="enableFixed('footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="width: 40px ; text-align: left ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<a href="#" data-role="button" id="btnRemarkSearchCustomerSaleWeightInsert" data-theme="b" data-mini="true" data-icon="search" onclick="clickRemarkCustomerSaleWeightInsert()" data-iconpos="notext">검색</a>
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; ">
							<tr>
								<td style="text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveInsertCustomerSaleWeightInsertBatch(false)">저장</a>
									<a href="#" data-role="button" data-mini="true" data-icon="plus" data-corners="false" data-inline="true" onclick="clickSaveInsertCustomerSaleWeightInsertBatch(true)">연속등록</a>
								</td>
							</tr>
						</table>
						<div id="divResultMessageCustomerSaleWeightInsertBatch"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
