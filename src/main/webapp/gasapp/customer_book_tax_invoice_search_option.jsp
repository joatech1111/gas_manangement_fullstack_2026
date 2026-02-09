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
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
		}
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
							<tr style="height: 60px ; ">
								<td style="width: 110px ; ">
									<input style="font-size: 14px ; width: 100px ; text-align: center ;" type="text" id="txtStartDateCustomerBookTaxInvoice" data-theme="c" data-mini="true" value="<%=startDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookTaxInvoice', 'txtStartDateCustomerBookTaxInvoice')" />
								</td>
								<td style="width: 20px ; ">
									<span style="font-size: 14px ;"> ~ </span> 
								</td>
								<td style="width: 110px ; ">
									<input style="font-size: 14px ; width: 100px ;  text-align: center ;" type="text" id="txtEndDateCustomerBookTaxInvoice" data-theme="c" data-mini="true" value="<%=endDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookTaxInvoice', 'txtEndDateCustomerBookTaxInvoice')" />
								</td>
								<td>
									<div data-role="controlgroup"  data-type="horizontal" style="text-align: left;  ">
										<a href="#" data-role="button" data-icon="search" data-theme="b" onclick="searchCustomerBookTaxInvoice()" data-inline="true" >조회</a>
									</div>
								</td>
							</tr>
						</table>
						<table id="tableHeaderCustomerBookTaxInvoice" style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">
							<tr>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>
								<td style="width: 80px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">공급액</span></td>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">세액</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">합계</span></td>
								<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">EDI</span></td>
								<td style="width: 30px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">NTS</span></td>
							</tr>
						</table>
<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>