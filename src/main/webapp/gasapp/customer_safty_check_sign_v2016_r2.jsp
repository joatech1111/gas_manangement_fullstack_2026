<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSaftyCheck" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String insertMode = request.getParameter("insertMode"); //0:insert 1:update
	String signatureYN = request.getParameter("signatureYN"); // 서명 여부
	
	boolean isSigned = false;
	if ("Y".equals(signatureYN)) {
		isSigned = true;
	}
	
	try{
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
		}
%>
<style type="text/css">
  .inner{
    width : 1px;
    height : 30px;
    float : left;
  }
</style>
<%
		if ("0".equals(insertMode)) {
%>
<img id="imgSignatureImageCustomerSaftyCheckInsert" style="display: <%= isSigned?"inline-block":"none" %> ;" />

<div id="divCanvasCustomerSaftyCheckInsert" style="display: <%= isSigned?"none":"inline-block" %>; " >
    <canvas id="canvasCustomerSaftyCheckInsert" ></canvas>
</div>
<table id="tableButtonGroupCustomerSaftyCheckInsert" style="width: 100% ; display: inline-table ; border: 0px solid #999999 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
    		<div id="divEditSignatureCustomerSaftyCheckInsert" style="display: none;">
				<input id="btnEditSignatureCustomerSaftyCheckInsert" data-mini="true" type="button" value="수정" data-icon="delete" data-corners="false" data-inline="true"  onclick="clickEditSignatureCustomerSaftyCheckInsert()" >
			</div>
			<div id="divSaveSignatureCustomerSaftyCheckInsert" style="display: <%= isSigned?"none":"inline-block" %>;">
				<input id="btnResetSignatureCustomerSaftyCheckInsert" data-mini="true" type="button" value="초기화" data-icon="refresh" data-corners="false" data-inline="true"  onclick="clearCanvas('canvasCustomerSaftyCheckInsert')" >
				<input id="btnSaveSignatureCustomerSaftyCheckInsert" data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true"  onclick="clickSaveSignatureCustomerSaftyCheckInsert('canvasCustomerSaftyCheckInsert')" >
    		</div>
    	</td>
	</tr>
</table>
<%
		} else {
%>
<img id="imgSignatureImageCustomerSaftyCheckEdit" style="display: <%= isSigned?"inline-block":"none" %> ;" />

<div id="divCanvasCustomerSaftyCheckEdit" style="display: <%= isSigned?"none":"inline-block" %>; " >
    <canvas id="canvasCustomerSaftyCheckEdit" ></canvas>
</div>
<table id="tableButtonGroupCustomerSaftyCheckEdit" style="width: 100% ; display: inline-table ; border: 0px solid #999999 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
    		<div id="divEditSignatureCustomerSaftyCheckEdit" style="display: none;">
				<input id="btnEditSignatureCustomerSaftyCheckEdit" data-mini="true" type="button" value="수정" data-icon="delete" data-corners="false" data-inline="true"  onclick="clickEditSignatureCustomerSaftyCheckEdit()" >
			</div>
			<div id="divSaveSignatureCustomerSaftyCheckEdit" style="display: <%= isSigned?"none":"inline-block" %>;">
				<input id="btnResetSignatureCustomerSaftyCheckEdit" data-mini="true" type="button" value="초기화" data-icon="refresh" data-corners="false" data-inline="true"  onclick="clearCanvas('canvasCustomerSaftyCheckEdit')" >
				<input id="btnSaveSignatureCustomerSaftyCheckEdit" data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true"  onclick="clickSaveSignatureCustomerSaftyCheckEdit('canvasCustomerSaftyCheckEdit')" >
    		</div>
    	</td>
	</tr>
</table>
<%
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>