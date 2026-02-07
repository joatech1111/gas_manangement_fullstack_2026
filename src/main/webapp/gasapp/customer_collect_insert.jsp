<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CollectTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.CollectTypeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCollectTypeCode" %>
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
		//AppUser appUser = (AppUser)session.getAttribute("USER_INFO");

		System.out.println("sdlfklsdkflksdflksdlfklsdkflksdlkf");
		System.out.println("sdlfklsdkflksdflksdlfklsdkflksdlkf");
		System.out.println("sdlfklsdkflksdflksdlfklsdkflksdlkf");
		System.out.println("sdlfklsdkflksdflksdlfklsdkflksdlkf");
		String uuid=  request.getParameter("uuid");

		System.out.println(uuid);
		System.out.println(uuid);

		AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userEmployeeCode = appUser.getEmployeeCode();
			String userEmployeeName = appUser.getEmployeeName();
			String menuPermission = appUser.getMenuPermission();
			String [] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
			boolean changeEmployee = "0".equals(permission[2])?true:false;

			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );

			String customerType = customerSearch.getCustomerType();
			String customerTypeCode = (new Integer(customerType).intValue() > 4)?"1":(new Integer(customerType).intValue() == 1)?"1":"0";
			String weightReceivable = customerSearch.getWeightReceivable();
			String volumeReceivable = customerSearch.getVolumeReceivable();
			String buildingName = customerSearch.getBuildingName();
			String userName = customerSearch.getUserName();
			final String DEFAULT_COLLECT_TYPE_CODE = "0";

			// 세션에서 사원목록 가져오기
//			EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");

			EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
			if(employeeCodes==null){
				String keyword = "";
				EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				employeeCodes = newEmployeeCodes;
				session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
			}
			CollectTypeCodeMap collectTypeCodes = (CollectTypeCodeMap)session.getAttribute("COLLECT_TYPE_CODE");
			if(collectTypeCodes==null){
				String keyword = "";
				CollectTypeCodeMap newCollectTypeCodes = BizCollectTypeCode.getInstance().getCollectTypeCodes(serverIp, catalogName);
				collectTypeCodes = newCollectTypeCodes;
				session.setAttribute("COLLECT_TYPE_CODE", newCollectTypeCodes);
			}
%>
<input type="hidden" id="hdnBuildingNameCustomerCollectInsert" value="<%=buildingName %>" />
<input type="hidden" id="hdnUserNameCustomerCollectInsert" value="<%=userName %>" />
<input type="hidden" id="hdnWeightReceivableCustomerCollectInsert" value="<%= weightReceivable %>" />
<input type="hidden" id="hdnVolumeReceivableCustomerCollectInsert" value="<%= volumeReceivable %>" />

						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr style="height: 40px ; ">
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="text-align: left ; width: 80px ; font-size:14px ;">미수구분</td>
								<td style="text-align: left ; text-align: left ; font-size:14px ;">
									<fieldset data-role="controlgroup" data-type="horizontal">
										<input type="radio" data-mini="true" name="rdoCollectTypeCustomerCollectInsert" id="rdoWeightCollectTypeCustomerCollectInsert" value="J" <%="0".equals(customerTypeCode)?"checked":"" %> onclick="calculateRemainAmountCustomerCollectInsert()" />
								     	<label for="rdoWeightCollectTypeCustomerCollectInsert"><span style="font-size: 14px ; color: blue ; ">일반</span></label>
								     	<input type="radio" data-mini="true" name="rdoCollectTypeCustomerCollectInsert" id="rdoVolumeCollectTypeCustomerCollectInsert" value="C" <%="1".equals(customerTypeCode)?"checked":"" %> onclick="calculateRemainAmountCustomerCollectInsert()" />
								     	<label for="rdoVolumeCollectTypeCustomerCollectInsert"><span style="font-size: 14px ; color: blue ; ">체적</span></label>
									</fieldset>
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="text-align: left ; width: 80px ; font-size:14px ;">수금일자</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="text" id="txtCollectDateCustomerCollectInsert" data-mini="true" style="width: 100px ; font-size: 14px ; color: blue ; text-align: center ; background-color: #CCCCCC " readonly value="<%=StringUtil.dateFormatStr(null) %>" />
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
						</table>
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="text-align: left ; width: 80px ; font-size:14px ;">미수액</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="text"  id="txtReceivableCustomerCollectInsert" data-mini="true" value="<%=StringUtil.moneyFormatStr("0".equals(customerTypeCode)?""+weightReceivable:""+volumeReceivable) %>" style="width: 80%; font-size: 14px ; text-align: right ; color: blue ; background-color: #CCCCCC " readonly/>
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">수금액</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="number"  id="txtCollectAmountCustomerCollectInsert" data-mini="true" value="0" onchange="calculateRemainAmountCustomerCollectInsert()" style="width: 80%; font-size: 14px ; text-align: right ; color: blue ; "  onclick="focusNumber(this, 'footersCustomerCollect')" onfocus="disableFixed('footersCustomerCollect')" onblur="blurNumber(this, 'footersCustomerCollect')" />
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">D/C</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="number"  id="txtDiscountAmountCustomerCollectInsert" data-mini="true" value="0" onchange="calculateRemainAmountCustomerCollectInsert()" style="width: 80%; font-size: 14px ; text-align: right ; color: blue ; "   onclick="focusNumber(this, 'footersCustomerCollect')" onfocus="disableFixed('footersCustomerCollect')" onblur="blurNumber(this, 'footersCustomerCollect')" />
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">처리후잔액</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="text"  id="txtRemainAmountCustomerCollectInsert" data-mini="true" value="<%=StringUtil.moneyFormatStr("0".equals(customerTypeCode)?""+weightReceivable:""+volumeReceivable) %>" style="width: 80%; font-size: 14px ; text-align: right ; color: blue ; background-color: #CCCCCC " readonly/>
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
						</table>
						<br />
						<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">수금방법</td>
								<td style="text-align: left ; font-size:14px ;">
									<select name="selectCollectTypeCustomerCollectInsert" id="selectCollectTypeCustomerCollectInsert"  data-mini="true" data-inset="false" style="font-size: 14px ; ">
										<option value="" data-placeholder="true">수금방법</option>
<%
		// 수금방법 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			CollectTypeCode collectTypeCode =  collectTypeCodes.getCollectTypeCode(key);
			String collectTypeCodeStr = collectTypeCode.getCollectTypeCode();
			String collectTypeNameStr = collectTypeCode.getCollectTypeName();
			String selected = "";
			if (DEFAULT_COLLECT_TYPE_CODE.equals(collectTypeCodeStr)) {
				selected = "selected";
			}
%>
										<option value="<%= collectTypeCodeStr %>" <%=selected %>><%= collectTypeNameStr %></option>
<%
		}
%>
									</select>
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">사원</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="hidden" id="hdnEmployeeNameCustomerCollectInsert" value="<%=userEmployeeName %>" />
 <%
	if (changeEmployee) {
%>
									<select id="selectEmployeeCustomerCollectInsert"  data-mini="true" style="font-size: 14px ; " onchange="changeEmployeeCustomerCollectInsert()">
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
									<input type="hidden" id ="selectEmployeeCustomerCollectInsert" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
							<tr>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; font-size:14px ;">비고</td>
								<td style="text-align: left ; font-size:14px ;">
									<input type="text" id="txtRemarkCustomerCollectInsert" data-mini="true" value="" style="width: 80%; font-size: 14px ; text-align: left ; color: blue ; " maxlength="10" onfocus="disableFixed('footersCustomerCollect')" onblur="enableFixed('footersCustomerCollect')" />
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
							</tr>
						</table>
						<table style="width: 100%">
							<tr>
								<td style="text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-corners="false" data-icon="check" data-inline="true" onclick="clickSaveAndEditCustomerCollectInsert()">저장</a>
									<a href="#" data-role="button" data-mini="true" data-corners="false" data-icon="plus" data-inline="true" onclick="clickSaveAndInsertCustomerCollectInsert()">연속등록</a>
								</td>
							</tr>
						</table>
						<div id="divMessageCustomerCollectInsert"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
