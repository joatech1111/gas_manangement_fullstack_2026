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
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	try{
		String customerCode = (String)request.getParameter("customerCode");
		String keyword = (String)request.getParameter("keyword");
		if (keyword == null) keyword = "";
		keyword = keyword.trim();
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			
			// 비고 목록 가져오기
			HashMap<String, String> remarks = BizCustomerWeightSale.getInstance().getCustomerWeightSaleRemarks(serverIp, catalogName, areaCode, customerCode, StringUtil.dateFormatStr(null, ""));
%>
<%
		// 비고 조회 내역
		java.util.Iterator<String> iterator = remarks.keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			String remark =  remarks.get(key);
%>
						<a href="#" onclick="clickCustomerSaleWeightInsertRemarkSearch('<%=remark %>')">
							<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
								<tr style="height: 40px ; ">
									<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
										<span style="color:#222222 ; font-size:14px ; "><%=remark %></span>
									</td>
								</tr>
							</table>
						</a>
<%
		}
%>
						<div id="divResultMessageCustomerSaleWeightInsertRemarkSearch"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
