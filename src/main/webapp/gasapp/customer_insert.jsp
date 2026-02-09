<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.ConsumeTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.ConsumeTypeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizConsumeTypeCode" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
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
		if (consumeTypeCodes==null){
			if (appUser != null){
				String serverIp = appUser.getIpAddress();
				String catalogName = appUser.getDbCatalogName();
				ConsumeTypeCodeMap newConsumeTypeCodes = BizConsumeTypeCode.getInstance().getConsumeTypeCodes(serverIp, catalogName);
				session.setAttribute("CONSUME_TYPE", newConsumeTypeCodes);
				consumeTypeCodes = newConsumeTypeCodes;
			}
		}
%>
 				<table style="font-size:14px ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<th style="width: 80px ;"></th>
						<th></th>
					</tr>
					<tr>
						<td>구분:</td>
						<td>
	        				<select id="selectCustomerSearchInsertCustomerType" data-mini="true" >
	        					<option value="0">일반</option>
	        					<option value="1">체적</option>
	        					<option value="2">둘다</option>
	        					<option value="3">기타</option>
	        					<option value="4">모두</option>
	        				</select>
						</td>
					</tr>
	        		<tr>
	        			<td>거래처명<span style="color: red ;">*</span>:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertCustomerName" value="" maxlength="20"  style="width: 80% ;" />	</td>
	        		</tr>
	        		<tr>
	        			<td>사용자명:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertUserName" value="" maxlength="20" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>전화:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertPhoneNumber" value="" maxlength="14" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>핸드폰:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertMobileNumber" value="" maxlength="14" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>관할주소:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertAddress1" value="" maxlength="40" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>상세주소:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertAddress2" value="" maxlength="40" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>비고1:</td>
        				<td><input data-mini="true" type="text" id="txtCustomerSearchInsertRemark1" value="" maxlength="60" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>비고2:</td>
	        			<td><input data-mini="true" type="text" id="txtCustomerSearchInsertRemark2" value="" maxlength="60" style="width: 80% ;" /></td>
	        		</tr>
	        		<tr>
	        			<td>소비형태:</td>
	        			<td>
	        				<select id="selectCustomerSearchInsertConsumeTypeCode" data-mini="true">
<%
			// 소비형태 유형 목록을 선택 박스에 채우기
			java.util.Iterator<String> iterator = consumeTypeCodes.getConsumeTypeCodes().keySet().iterator(); 
			while (iterator.hasNext()) { 
				String key = iterator.next(); 
				ConsumeTypeCode consumeTypeCodeObject =  consumeTypeCodes.getConsumeTypeCode(key);
				String consumeTypeCodeStr = consumeTypeCodeObject.getConsumeTypeCode();
				String consumeTypeNameStr = consumeTypeCodeObject.getConsumeTypeName();
%>
							<option value="<%= consumeTypeCodeStr %>"  ><%= consumeTypeNameStr %></option>
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
	        				<input data-mini="true" type="button" id="btnCustomerSearchInsertInit" value="초기화" onclick="clickCustomerSearchInsertInit()" data-icon="refresh" data-inline="true"/>&nbsp;
	        				<input data-mini="true" type="button" id="btnCustomerSearchInsertSave" value="저장" onclick="clickCustomerSearchInsertSave()" data-icon="check" data-inline="true"/>
	        			</td>
	        		</tr>
	        	</table>
	        	<div id="divSaveMessageCustomerSearchInsert"></div>
<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
