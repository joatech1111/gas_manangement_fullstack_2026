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
		//appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		String macNumber = request.getParameter("uuid");
		AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,macNumber);

		String gasType ="HIGH";
		if (appUser != null) {
			gasType = appUser.getGasType();
		}
		CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
		String sessionToken = request.getParameter("sessionToken");
		customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
		if (customerSearch!=null){
			String customerType = customerSearch.getCustomerType();
//			String customerTypeName = customerSearch.getCustomerTypeName();
			String customerTypeCode = (new Integer(customerType).intValue() > 4)?"1":customerType;
			String customerTypeIcon = "images/lbl_customer_type_"+ customerTypeCode + ".png";
//			String customerCode = customerSearch.getCustomerCode();
			String customerName = customerSearch.getCustomerName();
			String customerStatusCode = customerSearch.getCustomerStatusCode();
			String customerStatusName = "[" + customerSearch.getCustomerStatusName() + "]" ;
			if ("0".equals(customerStatusCode)) {
				customerStatusName = "";
			}
			String customerNameStyle = ("0".equals(customerStatusCode))?"#222222":"#999999";
			String phoneNumber = customerSearch.getPhoneNumber();
			String mobileNumber = customerSearch.getMobileNumber();
			String address = customerSearch.getAddress1() + " " + customerSearch.getAddress2();
			String containerDeposit = customerSearch.getContainerDeposit();
			containerDeposit = String.format("%,d", new Integer(containerDeposit));
			String weightReceivable = customerSearch.getWeightReceivable();
			String volumeReceivable = customerSearch.getVolumeReceivable();
			String receivable = String.format("%,d", new Integer(weightReceivable).intValue() + new Integer(volumeReceivable).intValue());
			String latestSaftyCheckDate = customerSearch.getLatestSaftyCheckDate();
			latestSaftyCheckDate = StringUtil.dateFormatStr(latestSaftyCheckDate);

			// 보증금이 있을 경우 색깔 변경
			String containerDepositStyle = !"0".equals(containerDeposit)?"#FF0000":"#222222";

			// 미수금(중량미수, 체적미수)이 있을 경우 색깔 변경
			String receivableStyle =!"0".equals(receivable)?"#FF0000":"#222222";

			// 안전점검 대상 체크
//			String saftyCheckYesNoText = "안전점검대상";
			String latestSaftyCheckDateStyle = "#222222";
			String latestSaftyCheckDateIcon = "";
			if (!"".equals(latestSaftyCheckDate)){
				int diff = StringUtil.dateDifference(latestSaftyCheckDate);
				int satryCheckPeriod = 365; // 체적이 아니면 1년마다 체크
				if ("0".equals(customerType)) satryCheckPeriod = 183; // 체적일 때 6개월 마다 체크
				if (diff >=  satryCheckPeriod) latestSaftyCheckDateStyle = "#FF0000"; // 안전점검 체크 기간이면 빨간색으로.
				latestSaftyCheckDateIcon = "<img src=\"images/lbl_latest_safty_check.png\" />";
			}
%>
<table style="border: 0px solid #999999 ; border-top: 1px solid #BBBBBB ;border-collapse: collapse ; width: 100% ; ">
	<tr>
		<td style="text-align: left ; vertical-align: center ;">
			<img src="<%=customerTypeIcon %>" ></img>
			<span style="color: <%=customerNameStyle %> ; font-size:16px ;"><%= customerName %></span><span style="color: red ; font-size: 16px ; " ><%=customerStatusName %> </span>
			<br/>
			<a href="tel:<%=phoneNumber %>" style="color: #3300FF ; font-size:14px ;"><%= phoneNumber %></a>
			<a href="tel:<%=mobileNumber %>" style="color: #3300FF ; font-size:14px ;"><%= mobileNumber %></a>
			<br/>
			<span style="color:#222222 ; font-size:14px ;"><%= address %></span>
			<br/>
			<%
				if ("LPG".equals(gasType)) {
			%>
			<%= latestSaftyCheckDateIcon %>
			<span style="color: <%= latestSaftyCheckDateStyle %> ; font-size:14px ;"><%= latestSaftyCheckDate %></span>
			<%
				}
			%>
			<img src="images/lbl_container_deposit.png" /><span style="color: <%=containerDepositStyle%> ; font-size:14px ;"> <%= containerDeposit %></span>
			<img src="images/lbl_unpaid.png" /><span style="color: <%=receivableStyle %> ; font-size:14px ;"> <%= receivable %></span>
		</td>
	</tr>
</table>
<%
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
