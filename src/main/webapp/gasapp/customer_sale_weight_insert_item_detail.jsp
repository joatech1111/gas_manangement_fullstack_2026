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
		String insertMode = (String)request.getParameter("insertMode"); // 0:수정 1:신규
		String saleDate = (String)request.getParameter("saleDate"); 
		String itemIndex = (String)request.getParameter("itemIndex"); 
		String saleType = (String)request.getParameter("saleType"); 
		if (saleType == null) saleType ="0";
		String itemCode = (String)request.getParameter("itemCode"); 
		String itemName = (String)request.getParameter("itemName"); 
		if (itemName == null) itemName ="";
		String itemSpec = (String)request.getParameter("itemSpec"); 
		if (itemSpec == null) itemSpec ="";
		String salePrice = (String)request.getParameter("salePrice"); 
		if (salePrice == null) salePrice ="0";
		String priceType = (String)request.getParameter("priceType"); 
		if (priceType == null) priceType ="";
		String itemBalance = (String)request.getParameter("itemBalance"); 
		if (itemBalance == null) itemBalance ="0";
		String nowBalance = (String)request.getParameter("nowBalance"); 
		if (nowBalance == null) nowBalance ="0";
		String saleQuantity = (String)request.getParameter("saleQuantity"); 
		if (saleQuantity == null) saleQuantity ="0";
		String withdrawQuantity = (String)request.getParameter("withdrawQuantity"); 
		if (withdrawQuantity == null) withdrawQuantity ="0";
		String vatType = (String)request.getParameter("vatType"); 
		if (vatType == null) vatType ="0";
		String saleAmount = (String)request.getParameter("saleAmount"); 
		if (saleAmount == null) saleAmount ="0";
		String taxAmount = (String)request.getParameter("taxAmount"); 
		if (taxAmount == null) taxAmount ="0";
		String totalAmount = (String)request.getParameter("totalAmount"); 
		if (totalAmount == null) totalAmount ="0";
		String discountAmount = (String)request.getParameter("discountAmount"); 
		if (discountAmount == null) discountAmount ="0";
		String collectAmount = (String)request.getParameter("collectAmount"); 
		if (collectAmount == null) collectAmount ="0";
		String unpaidAmount = (String)request.getParameter("unpaidAmount"); 
		if (unpaidAmount == null) unpaidAmount ="0";
		String userEmployeeCode = (String)request.getParameter("employeeCode"); 
		String userEmployeeName = (String)request.getParameter("employeeName"); 
		String remarkCode = (String)request.getParameter("remarkCode"); 
		if (remarkCode == null) remarkCode ="";
		String remarkText = (String)request.getParameter("remarkText"); 
		if (remarkText == null) remarkText ="";
		String collectType = (String)request.getParameter("collectType"); 
		if (collectType == null) collectType ="0";
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			if (userEmployeeCode == null) userEmployeeCode = appUser.getEmployeeCode();
			if (userEmployeeName == null) userEmployeeName = appUser.getEmployeeName();
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
		
			// 비고 목록 가져오기
			HashMap<String, String> remarks = BizCustomerWeightSale.getInstance().getCustomerWeightSaleRemarks(serverIp, catalogName, areaCode, customerCode, StringUtil.dateFormatStr(null, ""));
			
%>
<input type="hidden" id="hdnInsertModeCustomerSaleWeightInsertItemDetail" value="<%=insertMode %>" />
<input type="hidden" id="hdnItemIndexCustomerSaleWeightInsertItemDetail" value="<%=itemIndex %>" />
<input type="hidden" id="hdnItemCodeCustomerSaleWeightInsertItemDetail" value="<%=itemCode %>" />
<input type="hidden" id="hdnItemNameCustomerSaleWeightInsertItemDetail" value="<%=itemName %>" />
<input type="hidden" id="hdnItemSpecCustomerSaleWeightInsertItemDetail" value="<%=itemSpec %>" />
<input type="hidden" id="hdnPriceTypeCustomerSaleWeightInsertItemDetail" value="<%=priceType %>" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 60px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">판매일자</td>
								<td style="font-size:14px ; border-bottom: 1px solid #999999 ;">
									<input type="text"  id="txtSaleDateCustomerSaleWeightInsertItemDetail" style="width: 100px ; font-size: 14px ; color: blue ; text-align: center ; background-color: #DDDDDD ; " value="<%=saleDate %>" readonly="readonly" />
								</td>
							</tr>
						</table>
						<table id="tbItemListCustomerSaleWeightInsertItemDetail" style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">구분</td>
						        <td colspan="2" style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">품목</td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">단가</td>
						    </tr>
						    <tr>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
<%
					if("0".equals(insertMode)){
						String saleTypeName = "가스";
						if ("1".equals(saleType)) {
							saleTypeName = "용기";
						} else if ("2".equals(saleType)) {
							saleTypeName = "기구";
						}
%>
									<span id="selectSaleTypeCustomerSaleWeightInsertItemDetail" style="font-size: 14px; " value="<%=saleType %>"><%=saleTypeName %></span>
<%
					} else {
%>
									<select id="selectSaleTypeCustomerSaleWeightInsertItemDetail"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="changeSaleTypeCustomerSaleWeightInsertItemDetail()">
										<option value="0" <%="0".equals(saleType)?"selected":"" %> >가스</option>
										<option value="1" <%="1".equals(saleType)?"selected":"" %>>용기</option>
										<option value="2" <%="2".equals(saleType)?"selected":"" %>>기구</option>
									</select>
<%						
					}
%>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtItemCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=itemName + " " + itemSpec %>" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; <%="0".equals(insertMode)?"background-color: #DDDDDD ;":"" %>" />
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<a href="#" data-role="button" id="btnItemCustomerSaleWeightInsertItemDetail" data-mini="true" onclick="clickItemCustomerSaleWeightInsertItemDetail('<%=insertMode %>')" style="<%="0".equals(insertMode)?"display: none ; ": "" %>" data-theme="b" data-icon="search" data-iconpos="notext">검색</a>
						        </td>
						        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" data-role="none" id="txtSalePriceCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=salePrice %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateTotalAmountCustomerSaleWeightInsertItemDetail()"onclick="focusNumber(this)" onblur="blurNumber(this)"  />
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
						        	<input type="text" data-role="none" id="txtItemBalanceCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=itemBalance %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " />
								</td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtSaleQuantityCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=saleQuantity %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="changeSaleQuantityCustomerSaleWeightInsertItemDetail()"onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td id="tdWithdrawCustomerSaleWeightInsertItemDetail" style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtWithdrawQuantityCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=withdrawQuantity %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " onchange="calculateTotalAmountCustomerSaleWeightInsertItemDetail()"onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="width: 40px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtNowBalanceCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=nowBalance %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; display: <%="0".equals(saleType)?"inline-block":"none" %> ; " />
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
									<select id="selectVatTypeCustomerSaleWeightInsertItemDetail"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="calculateTotalAmountCustomerSaleWeightInsertItemDetail()" >
										<option value="0" <%="0".equals(vatType)?"selected":"" %> >VAT별도</option>
										<option value="1" <%="1".equals(vatType)?"selected":"" %> >VAT포함</option>
										<option value="2" <%="2".equals(vatType)?"selected":"" %> >비과세</option>
									</select>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtSaleAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=saleAmount %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtTaxAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=taxAmount %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtTotalAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=totalAmount %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
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
									<select id="selectCollectTypeCustomerSaleWeightInsertItemDetail"  data-mini="true" style="font-size: 14px ; " onchange="changeCollectTypeCustomerSaleWeightInsertItemDetail()" >
<%
		// 입금방법 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator(); 
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
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtCollectAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=collectAmount %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateUnpaidAmountCustomerSaleWeightInsertItemDetail()"onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtDiscountAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=discountAmount %>" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white; " onchange="calculateUnpaidAmountCustomerSaleWeightInsertItemDetail()"onclick="focusNumber(this)" onblur="blurNumber(this)"  />
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">
						        	<input type="text" data-role="none" id="txtUnpaidAmountCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=unpaidAmount %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
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
									<input type="hidden" id="hdnEmployeeNameCustomerSaleWeightInsertItemDetail" value="<%=userEmployeeName %>" />
<%
	if (changeEmployee) { 
%>
									<select id="selectEmployeeCustomerSaleWeightInsertItemDetail"  data-mini="true" style="font-size: 14px ; " onchange="changeEmployeeCustomerSaleWeightInsertItemDetail()" >
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
									<input type="hidden" id ="selectEmployeeCustomerSaleWeightInsertItemDetail" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 0px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtRemarkCustomerSaleWeightInsertItemDetail" data-mini="true" value="<%=remarkText %>" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; " />
						        </td>
						        <td style="text-align: left ; font-size:14px ; border-left: 0px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<a href="#" data-role="button" id="btnRemarkSearchCustomerSaleWeightInsertItemDetail" data-mini="true" onclick="clickRemarkCustomerSaleWeightInsertItemDetail()" data-theme="b" data-icon="search" data-iconpos="notext">검색</a>
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; ">
							<tr>
								<td style="text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveCustomerSaleWeightInsertItemDetail()">저장</a>
<%
	if ("0".equals(insertMode)) { // 수정일 때만 삭제가능
%>
									<a href="#" data-role="button" data-mini="true" data-icon="check" data-corners="false" data-inline="true" onclick="clickDeleteCustomerSaleWeightInsertItemDetail()">삭제</a>
<%
	}
%>
								</td>
							</tr>
						</table>
						<div id="divResultMessageCustomerSaleWeightInsertItemDetail"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
