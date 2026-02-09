<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.ConsumeTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.ConsumeTypeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizConsumeTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
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
		// 세션에서 소비형태 유형 목록 가져오기
		ConsumeTypeCodeMap consumeTypeCodes = (ConsumeTypeCodeMap)session.getAttribute("CONSUME_TYPE");
		// 세션에 아직 소비형태 유형 목록이 등록되지 않았다면 세션에 등록하기
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		String serverIp = appUser.getIpAddress();
		String catalogName = appUser.getDbCatalogName();

		if (consumeTypeCodes==null){
			if (appUser != null){
				ConsumeTypeCodeMap newConsumeTypeCodes = BizConsumeTypeCode.getInstance().getConsumeTypeCodes(serverIp, catalogName);
				session.setAttribute("CONSUME_TYPE", newConsumeTypeCodes);
				consumeTypeCodes = newConsumeTypeCodes;
			}
		}
		// 현재 선택된 거래처 정보 가져오기
		CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
		String sessionToken = request.getParameter("sessionToken");
		customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
		if (customerSearch != null){
			String areaCode = customerSearch.getAreaCode();
			String customerCode = customerSearch.getCustomerCode();
			String customerType = customerSearch.getCustomerType();
			String customerName = customerSearch.getCustomerName();
			String buildingName = customerSearch.getBuildingName();
			String userName = customerSearch.getUserName();
			String phoneNumber = customerSearch.getPhoneNumber();
			String mobileNumber = customerSearch.getMobileNumber();
			String address1 = customerSearch.getAddress1();
			String address2 = customerSearch.getAddress2();
			String remark1 = customerSearch.getRemark1();
			String remark2 = customerSearch.getRemark2();
			String consumeTypeCode = customerSearch.getConsumeTypeCode();
			String employeeCodeM = customerSearch.getEmployeeCode();
			
			EmployeeCodeMap employeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
			
			java.util.Iterator<String> iterator;
			
		
%>
<input type="hidden" id="hdnCustomerUpdateAreaCode" value="<%=areaCode %>" />
<input type="hidden" id="hdnCustomerUpdateCustomerCode" value="<%=customerCode %>" />

    		    <table style="font-size:14px ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<th style="width: 70px ;"></th>
						<th></th>
					</tr>
					<tr>
	        			<td>구분<span style="color: red ;">*</span>:</td>
	        			<td>
	        				<select id="selectCustomerUpdateCustomerType" data-mini="true" >
	        					<option value="0" <%= "0".equals(customerType)?"selected":"" %>>일반</option>
	        					<option value="1" <%= ("1".equals(customerType)||"7".equals(customerType)||"8".equals(customerType))?"selected":"" %>>체적</option>
	        					<option value="2" <%= "2".equals(customerType)?"selected":"" %>>둘다</option>
	        					<option value="3" <%= "3".equals(customerType)?"selected":"" %>>기타</option>
	        					<option value="4" <%= "4".equals(customerType)?"selected":"" %>>모두</option>
	        				</select>
	        			</td>
	        		</tr>
	        		<tr>
	        			<td>거래처명<span style="color: red ;">*</span>:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateCustomerName" value="<%=buildingName %>" maxlength="20" size="25" /></td>
	        		</tr>
	        		<tr>
	        			<td>사용자명:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateUserName" value="<%=userName %>" maxlength="20" size="25" /></td>
	        		</tr>
	        		<tr>
	        			<td>전화:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdatePhoneNumber" value="<%=phoneNumber %>" maxlength="14" size="15" /></td>
	        		</tr>
	        		<tr>
	        			<td>핸드폰:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateMobileNumber" value="<%=mobileNumber %>" maxlength="14" size="15" /></td>
	        		</tr>
	        		<tr>
	        			<td>관할주소:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateAddress1" value="<%=address1 %>" maxlength="40" size="25" /></td>
	        		</tr>
	        		<tr>
	        			<td>상세주소:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateAddress2" value="<%=address2 %>" maxlength="40" size="25" /></td>
	        		</tr>
	        		<tr>
	        			<td>비고1:</td>
        				<td><input data-mini="true" type="text" id="txtCustomerUpdateRemark1" value="<%=remark1 %>" maxlength="60" size="25" /></td>
	        		</tr>
	        		<tr>
	        			<td>비고2:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerUpdateRemark2" value="<%=remark2 %>" maxlength="60" size="25" /></td>
	        		</tr>
					<tr>
						<td>담당자명:</td>
						<td>
							<select id="selectCustomerUpdateEmlpoyeeCode" data-mini="true" >
								<option value="" value2="" >&nbsp;</option>

<%
			// 사원 목록을 선택 박스에 채우기
			iterator = employeeCodes.getEmployeeCodes().keySet().iterator(); 
			while (iterator.hasNext()) { 
				String key = iterator.next(); 
				EmployeeCode employeeCode =  employeeCodes.getEmployeeCode(key);
				String employeeCodeStr = employeeCode.getEmployeeCode();
				String employeeNameStr = employeeCode.getEmployeeName();
				String employeeCodeSeleted = employeeCodeStr.equals(employeeCodeM)?"selected":"";
			
%>
								<option value="<%= employeeCodeStr %>" value2="<%= employeeNameStr %>" <%=employeeCodeSeleted%> >[ <%= employeeCodeStr %> ] <%= employeeNameStr %></option>
<%
			}
%>
							</select>
				        </td>
					</tr>				
					
					
	        		<tr>
	        			<td>소비형태:</td>
	        			<td>
	        				<select id="selectCustomerUpdateConsumeTypeCode"  data-mini="true" >
<%
			// 소비형태 유형 목록을 선택 박스에 채우기
			iterator = consumeTypeCodes.getConsumeTypeCodes().keySet().iterator(); 
			while (iterator.hasNext()) { 
				String key = iterator.next(); 
				ConsumeTypeCode consumeTypeCodeObject =  consumeTypeCodes.getConsumeTypeCode(key);
				String consumeTypeCodeStr = consumeTypeCodeObject.getConsumeTypeCode();
				String consumeTypeNameStr = consumeTypeCodeObject.getConsumeTypeName();
				String consumeTypeCodeSeleted = consumeTypeCodeStr.equals(consumeTypeCode)?"selected":"";
%>
							<option value="<%= consumeTypeCodeStr %>"  <%=consumeTypeCodeSeleted %>><%= consumeTypeNameStr %></option>
<%
			}
%>
	        				</select>
	        			</td>
	        		</tr>
	        	</table>
	        	<br />
	        	<table style="font-size:14px ; border-collapse: collapse ; width: 100% ;">
	        		<tr>
	        			<td style="text-align: center ; ">
	        				<input data-mini="true" type="button" id="btnCustomerUpdateSave" data-icon="check" data-corners="false" data-inline="true" value="저장" onclick="clickCustomerUpdateSave()" />
	        				<input data-mini="true" type="button" id="btnCustomerUpdateCancel" data-icon="delete" data-corners="false" data-inline="true"  value="취소" onclick="showPageCustomerDetail()" />
	        			</td>
	        		</tr>
	        	</table>
	        	<div id="divCustomerUpdateSavingMessage"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>