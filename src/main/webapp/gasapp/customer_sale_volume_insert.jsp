<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerItem" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerItemMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerItem" %>
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
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		String uuid = request.getParameter("uuid");
		String hpSeq = request.getParameter("hpSeq");
		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);

		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
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
			String buildingName = customerSearch.getBuildingName();
			String userName = customerSearch.getUserName();
			String priceType = customerSearch.getPriceType();
			String cubicPrice = customerSearch.getIndividualPrice(); // 개별단가
			if ("0".equals(priceType)){
				cubicPrice = customerSearch.getEnvironmentPrice(); // 환경단가
			} else if ("1".equals(priceType)){
				cubicPrice = customerSearch.getDiscountPrice(); // 할인단가
			}

			// 세션에서 사원목록 가져오기
			//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
			EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
			if(employeeCodes==null){
				String keyword = "";
				EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				employeeCodes = newEmployeeCodes;
				session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
			}

			CustomerItemMap customerItems = BizCustomerItem.getInstance().getCustomerItemVolumeAllHPGs(serverIp, catalogName, areaCode, customerCode);
%>
<input type="hidden" id="hdnItemCapacityCustomerSaleVolumeInsert" value="0" />
<input type="hidden" id="hdnItemNameCustomerSaleVolumeInsert" value="" />
<input type="hidden" id="hdnItemSpecCustomerSaleVolumeInsert" value="" />
<input type="hidden" id="hdnItemPriceCustomerSaleVolumeInsert" value="" />
<input type="hidden" id="hdnLastSaleQuantityCustomerSaleVolumeInsert" value="" />
<input type="hidden" id="hdnItemBalanceCustomerSaleVolumeInsert" value="" />
<input type="hidden" id="hdnBuildingNameCustomerSaleVolumeInsert" value="<%= buildingName %>" />
<input type="hidden" id="hdnCubicPriceCustomerSaleVolumeInsert" value="<%= cubicPrice %>" />
<input type="hidden" id="hdnSaleAmountCustomerSaleVolumeInsert" value="0" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">판매일자</td>
								<td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
									<input type="text" data-mini="true" id="txtSaleDateCustomerSaleVolumeInsert" style="width: 100px ; font-size: 14px ; color: blue ; text-align: center ; " value="<%=StringUtil.dateFormatStr(null) %>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerSaleVolumeInsert', 'txtSaleDateCustomerSaleVolumeInsert')" />
								</td>
							</tr>
							<tr>
								<td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">품명</td>
								<td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
									<select name="selectCustomerItemCustomerSaleVolumeInsert" id="selectCustomerItemCustomerSaleVolumeInsert"  data-mini="true" data-inset="false" style="font-size: 14px ; " onchange="changeItemCodeCustomerSaleVolume()">
<%
		// LPG 품목 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = customerItems.getCustomerItems().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CustomerItem customerItem =  customerItems.getCustomerItem(key);
			String itemCodeStr = customerItem.getItemCode();
			String itemNameStr = customerItem.getItemName();
			String itemSpecStr = customerItem.getItemSpec();
			String itemCapacity = customerItem.getCapacity();
			String itemPrice = customerItem.getSalePrice();
			String itemBalance = customerItem.getItemBalance();
			String lastSaleQuantity = customerItem.getLastSaleQuantity();
%>
										<option value="<%= itemCodeStr %>" value2="<%=itemCapacity %>"  value3="<%=itemNameStr %>" value4="<%=itemPrice %>" value5="<%=lastSaleQuantity %>" value6="<%=itemBalance %>" ><%= itemNameStr + " " + itemSpecStr  %></option>
<%
		}
%>
									</select>
								</td>
							</tr>
						</table>
						<br />
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전재고</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">공급</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">회수</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">현재고</td>
						    </tr>
						    <tr style="height: 40px ; ">
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtPreBalanceCustomerSaleVolumeInsert" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ;  background-color: #DDDDDD ; " />
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtSaleQuantityCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="changeSaleQuantityCustomerSaleVolume()" onclick="focusNumber(this, 'footersCustomerSaleVolumeInsert')" onfocus="disableFixed('footersCustomerSaleVolumeInsert')" onblur="blurNumber(this, 'footersCustomerSaleVolumeInsert')"/>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtWithdrawQuantityCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateSaleQuantityCustomerSaleVolume()" onclick="focusNumber(this, 'footersCustomerSaleVolumeInsert')" onfocus="disableFixed('footersCustomerSaleVolumeInsert')" onblur="blurNumber(this, 'footersCustomerSaleVolumeInsert')"/>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtNowBalanceCustomerSaleVolumeInsert" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
						        </td>
						    </tr>
						    <tr>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">배달검침</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">잔량</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">공급량</td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">후잔량</td>
						    </tr>
						    <tr style="height: 40px ; ">
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtReadMeterQuantityCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onclick="focusNumber(this, 'footersCustomerSaleVolumeInsert')" onfocus="disableFixed('footersCustomerSaleVolumeInsert')" onblur="blurNumber(this, 'footersCustomerSaleVolumeInsert')"/>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="number" data-role="none" id="txtRemainQuantityCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateRemainAfterSaleQuantityCustomerSaleVolume()" onclick="focusNumber(this, 'footersCustomerSaleVolumeInsert')" onfocus="disableFixed('footersCustomerSaleVolumeInsert')" onblur="blurNumber(this, 'footersCustomerSaleVolumeInsert')"/>
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtSaleVolumeCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " readonly />
						        </td>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
						        	<input type="text" data-role="none" id="txtRemainAfterSaleQuantityCustomerSaleVolumeInsert" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " readonly />
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
						    <tr>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">사원</td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">비고</td>
						    </tr>
						    <tr>
						        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
									<input type="hidden" id="hdnEmployeeNameCustomerSaleVolumeInsert" value="<%=userEmployeeName %>" />
<%
	if (changeEmployee) {
%>
									<select id="selectEmployeeCustomerSaleVolumeInsert"  data-mini="true" style="font-size: 14px ; " onchange="changeEmployeeCustomerSaleVolumeInsert()">
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
									<input type="hidden" id ="selectEmployeeCustomerSaleVolumeInsert" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
						        </td>
						        <td style="text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; ">
						        	<input type="text" id="txtRemarkCustomerSaleVolumeInsert" data-mini="true" value="" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ; " maxlength="10"  onfocus="disableFixed('footersCustomerSaleVolumeInsert')" onblur="enableFixed('footersCustomerSaleVolumeInsert')" />
						        </td>
						    </tr>
						</table>
						<table style="width: 100% ; ">
							<tr>
								<td style="text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-corners="false" data-icon="check" data-inline="true" onclick="clickSaveInsertCustomerSaleVolumeInsert(false)">저장</a>
									<a href="#" data-role="button" data-mini="true" data-corners="false" data-icon="plus" data-inline="true" onclick="clickSaveInsertCustomerSaleVolumeInsert(true)">연속등록</a>
								</td>
							</tr>
						</table>
						<div id="divResultMessageCustomerSaleVolumeInsert"></div>
						<br /><br /><br />

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>

<script type="text/javascript">
	$("#hdnItemCapacityCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value2"));
	$("#hdnItemNameCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value3"));
	$("#hdnItemPriceCustomerSaleVolumeInsert").attr("value", $("#selectCustomerItemCustomerSaleVolumeInsert > option:selected").attr("value4"));
</script>
