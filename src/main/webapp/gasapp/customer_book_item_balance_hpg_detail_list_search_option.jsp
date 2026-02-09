<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
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
		String itemName = request.getParameter("itemName");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));

		if (appUser != null){
		}
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 100% ; text-align: left ;"><span style="color: blue ; font-size: 14px ; "><%=itemName %></span></td>
							</tr>
							<tr>
								<td style="width: 100% ; text-align: left ;"><span style="color: black ; font-size: 14px ; ">(<%=StringUtil.dateFormatStr(startDate) %> ~ <%=StringUtil.dateFormatStr(endDate) %>)</span></td>
							</tr>
						</table>
						<table id="tableHeaderCustomerBookItemBalanceHPGDetailList" style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">
							<tr>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>
								<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">구분</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">出/入</span></td>
								<td style="width: 40px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">재고</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사원</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">현장</span></td>
							</tr>
						</table>
<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>