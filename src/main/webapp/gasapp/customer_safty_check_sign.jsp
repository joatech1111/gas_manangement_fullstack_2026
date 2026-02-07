<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
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
	String key = request.getParameter("key");
	String sequenceNumber = request.getParameter("sequenceNumber");
	String insertMode = request.getParameter("insertMode"); //0:insert 1:update
	String signatureFilePath = request.getParameter("signatureFilePath"); //서명 이미지 경로
	String signatureFileName = request.getParameter("signatureFileName"); //서명 파일명
	String signImagePath = request.getParameter("signImagePath"); //서버 경로
	String signImageFile = signatureFilePath + "/" + signatureFileName ;
	if ("0".equals(insertMode)){
		if (!"".equals(signatureFileName)){
			signImageFile = "/sdcard/gasmax_sign/" + signatureFileName;
		}
	}
	if ("/".equals(signImageFile)) {
		signImageFile = "images/sign_not_found.jpg";
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
<input type="hidden" id="hdnKeyCustomerSaftyCheckSign" value="<%=key %>" />
<input type="hidden" id="hdnSequenceNumberCustomerSaftyCheckSign" value="<%=sequenceNumber %>" />
<input type="hidden" id="hdnInsertModeCustomerSaftyCheckSign" value="<%=insertMode %>" />

<img id="imgSignImageCustomerSaftyCheckSign" src="<%=signImageFile %>"  style="display: <%= "".equals(signatureFileName)?"none":"inline-block" %> ;" />

<div id="divCanvasCustomerSaftyCheckSign" style="display: <%= "".equals(signatureFileName)?"inline-block":"none" %>; " >
    <canvas id="canvasCustomerSaftyCheckSign" ></canvas>
</div>
<table id="tableButtonGroupCustomerSaftyCheckSign" style="width: 100% ; display: inline-block ; border: 0px solid #999999 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
    		<div id="divEditCustomerSaftyCheckSign" style="display: none">
				<input id="btnEditCustomerSaftyCheckSign" data-mini="true" type="button" value="수정" data-icon="delete" data-corners="false" data-inline="true"  onclick="clickEditCustomerSaftyCheckSign()" >
			</div>
			<div id="divSaveCustomerSaftyCheckSign">
				<input id="btnResetCustomerSaftyCheckSign" data-mini="true" type="button" value="초기화" data-icon="refresh" data-corners="false" data-inline="true"  onclick="clearCanvas('canvasCustomerSaftyCheckSign')" >
				<input id="btnSaveCustomerSaftyCheckSign" data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true"  onclick="clickSaveCustomerSaftyCheckSign('<%=insertMode%>')" >
    		</div>
    	</td>
	</tr>
</table>


<%
	} catch (Exception e){
		e.printStackTrace();
	}
%>
