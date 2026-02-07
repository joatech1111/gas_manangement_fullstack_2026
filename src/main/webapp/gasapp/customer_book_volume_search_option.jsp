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
		String readMeterChecked = "0".equals(radioSelectIndex)?"checked":"";
		if (!(("1".equals(radioSelectIndex)) || ("2".equals(radioSelectIndex)))) readMeterChecked = "checked";
		String collectChecked = "1".equals(radioSelectIndex)?"checked":"";
		String saleChecked = "2".equals(radioSelectIndex)?"checked":"";

		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
		}
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 240px ; ">
									<fieldset data-role="controlgroup" data-type="horizontal">
								     	<input type="radio" name="rdoCustomerBookVolume" id="rdoCustomerBookVolumeReadMeter" value="0" <%=readMeterChecked %> onclick="searchCustomerBookVolume()"/>
								     	<label for="rdoCustomerBookVolumeReadMeter"><span style="font-size: 14px ; ">&nbsp;검 &nbsp;&nbsp; 침&nbsp;</span></label>
								     	<input type="radio" name="rdoCustomerBookVolume" id="rdoCustomerBookVolumeCollect" value="1" <%=collectChecked %> onclick="searchCustomerBookVolume()" />
								     	<label for="rdoCustomerBookVolumeCollect"><span style="font-size: 14px ; ">&nbsp;수 &nbsp;&nbsp; 금&nbsp;</span></label>
								     	<input type="radio" name="rdoCustomerBookVolume" id="rdoCustomerBookVolumeSale" value="2" <%=saleChecked %> onclick="searchCustomerBookVolume()" />
								     	<label for="rdoCustomerBookVolumeSale"><span style="font-size: 14px ; ">&nbsp;공 &nbsp;&nbsp; 급&nbsp;</span></label>
								     </fieldset>
							    </td>
							    <td style="width: 60px ; ">
									<div data-role="controlgroup"  data-type="horizontal" style="text-align: left;  ">
										<a href="#" data-role="button" data-icon="search" data-theme="b" onclick="searchCustomerBookVolume()" data-inline="true" >조회</a>
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
									<input style="font-size: 14px ; width: 100px ; text-align: center ;" type="text" id="txtStartDateCustomerBookVolume" data-theme="c" data-mini="true" value="<%=startDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookVolume', 'txtStartDateCustomerBookVolume')" />
								</td>
								<td style="width: 20px ; ">
									<span style="font-size: 14px ;"> ~ </span> 
								</td>
								<td style="width: 110px ; ">
									<input style="font-size: 14px ; width: 100px ; text-align: center ;" type="text" id="txtEndDateCustomerBookVolume" data-theme="c" data-mini="true" value="<%=endDate%>" readonly="readonly" onclick="openCapacitorDatePicker('pageCustomerBookVolume', 'txtEndDateCustomerBookVolume')" />
								</td>
								<td>
									<span style="font-size: 14px ;"> &nbsp; </span> 
								</td>
							</tr>
						</table>
						<table id="tableHeaderCustomerBookVolume" style="border: 0px solid #999999 ; border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; background-color: #DDDDDD ;  table-layout: fixed ; ">
							<tr>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">검침</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">당검</span></td>
								<td style="width: 40px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사용</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">단가</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">금액</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">수납</span></td>
							</tr>
						</table>
<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>