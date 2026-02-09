<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
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
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
		}
		CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
		String sessionToken = request.getParameter("sessionToken");
		customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
		if (customerSearch!=null){
			String contractNumber = customerSearch.getContractNumber();
			String contractName = customerSearch.getContractName();
			String contractDate = customerSearch.getContractDate();
			String latestSaftyCheckDate = customerSearch.getLatestSaftyCheckDate();
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 150px ; font-size: 14px ; ">계약번호: <%=contractNumber %></td>
								<td style="width: 150px ; font-size: 14px ; ">계약자명: <%=contractName %></td>
							</tr>
							<tr>
								<td style="width: 150px ; font-size: 14px ; ">계약일: <%="".equals(contractDate)?"":StringUtil.dateFormatStr(contractDate) %></td>
								<td style="width: 150px ; font-size: 14px ; ">점검일: <%="".equals(latestSaftyCheckDate)?"":StringUtil.dateFormatStr(latestSaftyCheckDate) %></td>
							</tr>
						</table>
						<table id="tableHeaderCustomerSaftyCheck" style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">
							<tr>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">점검구분</span></td>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">점검일자</span></td>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">점검사원</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">부적합 여부</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">서명</span></td>
							</tr>
						</table>
					
<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>