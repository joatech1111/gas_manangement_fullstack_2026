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
		
			// 세션에서 사원목록 가져오기
			//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
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
			
			//초기 품목 정보 가져오기
			/** Defult 값  - 최근판매품목 자동적용 
				구분 : 가스판매 check,  
				품명 : 거래처 사용품목 리스트의 첫번째 품목
				단가 : JP_DANGA  
				전재고 : JP_JAEGOCU
				납품 / 회수 :  JP_LAST_QTY
				Vat : 1 포함
				입금방법 : 최근판매내역이  IF JP_LAST_MISU = 0 THEN 현금 ELSE 외상
				D/C : JP_DC_Kum * 수량
				입금액 : IF 입금방법 = 현금 THEN 합계금액 - DC금액 ELSE 0
				미입금액 : 합계금액 - DC금액 - 입금액
				사원 : APPUSER 담당사원
				회수수량 : 납품수량 변경시 회수수량은 납품수량으로 자동변경
			*/
			String saleType = "0"; // 가스
			String keyword = "";
			CustomerItemMap customerItems = BizCustomerItem.getInstance().getCustomerItemWeightAllLPGs(serverIp, catalogName, areaCode, saleType, keyword);
			String itemCode = "";
			String itemName = "";
			String itemSpec = "";
			String priceType = "";
			String salePrice = "0";
			String itemBalance = "0";
			String discountAmount = "0";
			String lastSaleQuantity = "0";
			String lastUnpaidAmount = "0";
			String saleQuantity = "0";
			vatType = "1";
			String collectType = "외상" ; //외상
			if (customerItems != null){
				java.util.Iterator<String> iterator = customerItems.getCustomerItems().keySet().iterator();
				if (iterator.hasNext()){
					String key = (String)iterator.next();
					CustomerItem customerItem = customerItems.getCustomerItem(key);
					itemCode = customerItem.getItemCode();
					itemName = customerItem.getItemName();
					itemSpec = customerItem.getItemSpec();
					priceType = customerItem.getPriceType();
					salePrice = customerItem.getSalePrice();
					itemBalance = customerItem.getItemBalance();
					discountAmount = customerItem.getDiscountAmount();
					lastSaleQuantity = customerItem.getLastSaleQuantity();
					lastUnpaidAmount = customerItem.getLastUnpaidAmount();
					if ("0".equals(lastUnpaidAmount)) {
						collectType = "현금"; //현금
					}
				}
			}
			
			// 비고 목록 가져오기
			HashMap<String, String> remarks = BizCustomerWeightSale.getInstance().getCustomerWeightSaleRemarks(serverIp, catalogName, areaCode, customerCode, StringUtil.dateFormatStr(null, ""));
			
%>
<input type="hidden" id="hdnSaleTypeCustomerSaleWeightInsertLPG" value="0" />
<input type="hidden" id="hdnItemCodeCustomerSaleWeightInsertLPG" value="<%=itemCode %>" />
<input type="hidden" id="hdnItemNameCustomerSaleWeightInsertLPG" value="<%=itemName %>" />
<input type="hidden" id="hdnItemSpecCustomerSaleWeightInsertLPG" value="<%=itemSpec %>" />
<input type="hidden" id="hdnPriceTypeCustomerSaleWeightInsertLPG" value="<%=priceType %>" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 60px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">판매일자</td>
								<td style="font-size:14px ; border-bottom: 1px solid #999999 ;">
									<input type="text" data-mini="true" id="txtSaleDateCustomerSaleWeightInsertLPG" style="width: 100px ; font-size: 14px ; color: blue ; text-align: center ; " value="<%=StringUtil.dateFormatStr(null) %>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerSaleWeightInsert', 'txtSaleDateCustomerSaleWeightInsertLPG')" />
								</td>
							</tr>
						</table>
						<table id="tbItemListCustomerSaleWeightInsertLPG" style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">구분</td>
						        <td colspan="2" style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">품목</td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">단가</td>
						    </tr>
						    <tr>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectSaleTypeCustomerSaleWeightInsertLPG"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="changeSaleTypeCustomerSaleWeightInsertLPG()">
										<option value="0" selected >가스</option>
										<option value="1" >용기</option>
										<option value="2" >기구</option>
									</select>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtItemCustomerSaleWeightInsertLPG" data-mini="true" value="<%=itemName + " " + itemSpec %>" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; " onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="enableFixed('footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<a href="#" data-role="button" id="btnItemCustomerSaleWeightInsertLPG" data-mini="true" onclick="clickItemCustomerSaleWeightInsertLPG()" data-theme="b" data-icon="search" data-iconpos="notext">검색</a>
						        </td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" data-role="none" id="txtSalePriceCustomerSaleWeightInsertLPG" data-mini="true" value="<%=salePrice %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateTotalAmountCustomerSaleWeightInsertLPG()" onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')"   />
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전일</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">납품</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">회수</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">잔고</td>
							</tr>
							<tr style="height: 40px ; ">
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtItemBalanceCustomerSaleWeightInsertLPG" data-mini="true" value="<%=itemBalance %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD  ; " />
								</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtSaleQuantityCustomerSaleWeightInsertLPG" data-mini="true" value="<%=saleQuantity %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="changeSaleQuantityCustomerSaleWeightInsertLPG()" onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')" />
						        </td>
						        <td id="tdWithdrawCustomerSaleWeightInsertLPG" style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtWithdrawQuantityCustomerSaleWeightInsertLPG" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateTotalAmountCustomerSaleWeightInsertLPG()" onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')"  />
						        </td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtNowBalanceCustomerSaleWeightInsertLPG" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
							</tr>
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
									<select id="selectVatTypeCustomerSaleWeightInsertLPG"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="calculateTotalAmountCustomerSaleWeightInsertLPG()" >
										<option value="0" <%="0".equals(vatType)?"selected":"" %> >VAT별도</option>
										<option value="1" <%="1".equals(vatType)?"selected":"" %> >VAT포함</option>
										<option value="2" <%="2".equals(vatType)?"selected":"" %> >비과세</option>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtSaleAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtTaxAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtTotalAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금방법</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">입금액</td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">D/C</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">미입금액</td>
						    </tr>
						    <tr>
						        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<select id="selectCollectTypeCustomerSaleWeightInsertLPG"  data-mini="true" style="font-size: 14px ; " onchange="changeCollectTypeCustomerSaleWeightInsertLPG()" >
<%
		// 입금방법 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CollectTypeCode collectTypeCode =  collectTypeCodes.getCollectTypeCode(key);
			String collectTypeCodeStr = collectTypeCode.getCollectTypeCode();
			String collectTypeNameStr = collectTypeCode.getCollectTypeName();
%>
										<option value="<%= collectTypeCodeStr %>"  <%= collectType.equals(collectTypeNameStr)?"selected":"" %>><%= collectTypeNameStr %></option>
<%
		}
%>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtCollectAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateUnpaidAmountCustomerSaleWeightInsertLPG()" onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')"  />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtDiscountAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateUnpaidAmountCustomerSaleWeightInsertLPG()" onclick="focusNumber(this, 'footersCustomerSaleWeightInsert')" onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="blurNumber(this, 'footersCustomerSaleWeightInsert')" />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">
						        	<input type="text" data-role="none" id="txtUnpaidAmountCustomerSaleWeightInsertLPG" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
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
									<input type="hidden" id="hdnEmployeeNameCustomerSaleWeightInsertLPG" value="<%=userEmployeeName %>" />
<%
	if (changeEmployee) { 
%>
									<select id="selectEmployeeCustomerSaleWeightInsertLPG"  data-mini="true" style="font-size: 14px ; " onchange="changeEmployeeCustomerSaleWeightInsertLPG()" >
										<option value="" data-placeholder="true">미지정</option>
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
									<input type="hidden" id ="selectEmployeeCustomerSaleWeightInsertLPG" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtRemarkCustomerSaleWeightInsertLPG" data-mini="true" value="" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; " onfocus="disableFixed('footersCustomerSaleWeightInsert')" onblur="enableFixed('footersCustomerSaleWeightInsert')"/>
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; ">
							<tr>
								<td style="text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-icon="check" data-corners="false" data-inline="true" onclick="saveInsertCustomerSaleWeightInsertLPG(false)">저장</a>
									<a href="#" data-role="button" data-mini="true" data-icon="plus" data-corners="false" data-inline="true" onclick="saveInsertCustomerSaleWeightInsertLPG(true)">연속등록</a>
								</td>
							</tr>
						</table>
						<div id="divResultMessageCustomerSaleWeightInsertLPG"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
