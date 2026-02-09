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
		String radioSelectIndex = request.getParameter("radioIndex");
		String collectChecked = "0".equals(radioSelectIndex)?"checked":"";
		if (!("1".equals(radioSelectIndex))) collectChecked = "checked";
		String saleChecked = "1".equals(radioSelectIndex)?"checked":"";

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
		}
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 240px ; ">
									<fieldset data-role="controlgroup" data-type="horizontal">
								     	<input type="radio" name="rdoCustomerBookWeight" id="rdoCustomerBookWeightCollect" value="0" <%=collectChecked %> onclick="searchCustomerBookWeight()"/>
								     	<label for="rdoCustomerBookWeightCollect"><span style="font-size: 14px ; ">&nbsp;&nbsp;거 래 &nbsp; 내 역&nbsp;&nbsp;</span></label>
								     	<input type="radio" name="rdoCustomerBookWeight" id="rdoCustomerBookWeightSale" value="1" <%=saleChecked %> onclick="searchCustomerBookWeight()"/>
								     	<label for="rdoCustomerBookWeightSale"><span style="font-size: 14px ; ">&nbsp;&nbsp;매 출  &nbsp; 집 계&nbsp;&nbsp;</span></label>
								    </fieldset>
							    </td>
							    <td style="width: 60px ; ">
									<div data-role="controlgroup"  data-type="horizontal" style="text-align: left;  ">
										<a href="#" data-role="button" data-icon="search" data-theme="b" onclick="searchCustomerBookWeight()" data-inline="true" >조회</a>
									</div>
							    </td>
								<td>
									<span style="font-size: 14px ;"> &nbsp; </span> 
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 110px ; ">
									<input style="font-size: 14px ; width: 100px ; text-align: center ;" type="text" id="txtStartDateCustomerBookWeight" data-theme="c" data-mini="true" value="<%=startDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookWeight', 'txtStartDateCustomerBookWeight')" />
								</td>
								<td style="width: 20px ; text-align: left ; ">
									<span style="font-size: 14px ;"> ~ </span> 
								</td>
								<td style="width: 110px ; ">
									<input style="font-size: 14px ; width: 100px ;  text-align: center ;" type="text" id="txtEndDateCustomerBookWeight" data-theme="c" data-mini="true" value="<%=endDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookWeight', 'txtEndDateCustomerBookWeight')" />
								</td>
								<td>
									<span style="font-size: 14px ;"> &nbsp; </span> 
								</td>
							</tr>
						</table>
						<div id="tableHeaderCustomerBookWeight" >
							<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">
								<tr>
									<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">일자</span></td>
									<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; white-space: nowrap ; text-overflow: ellipsis ; overflow: hidden ; " ><span style="color:#222222 ; font-size:14px ; ">품명/비고</span></td>
									<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">出/入</span></td>
									<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사원</span></td>
								</tr>
							</table>
							<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ; table-layout: fixed ; ">
								<tr>
									<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">입금액</span></td>
									<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">D/C</span></td>
									<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">미수금액</span></td>
									<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">잔액</span></td>
								</tr>
							</table>
						</div>
<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>