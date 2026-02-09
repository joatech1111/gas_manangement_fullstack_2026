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
		String saleType = (String)request.getParameter("saleType");
		String keyword = (String)request.getParameter("keyword");
		if (keyword != null){
			keyword = keyword.trim();
		}
		if (saleType==null) saleType = "0";
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null) {
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String gasType = appUser.getGasType();
			
			CustomerItemMap customerItems;
			if ("HIGH".equals(gasType)){
				customerItems = BizCustomerItem.getInstance().getCustomerItemWeightAllHPGs(serverIp, catalogName, areaCode, saleType, keyword);
			} else {
				customerItems = BizCustomerItem.getInstance().getCustomerItemWeightAllLPGs(serverIp, catalogName, areaCode, saleType, keyword);
			}
%>
<input type="hidden" id="hdnSaleTypeCustomerSaleWeightInsertItemSearch" value="<%=saleType %>" />
<%
		// 품목 조회 내역
		java.util.Iterator<String> iterator = customerItems.getCustomerItems().keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerItem customerItem =  customerItems.getCustomerItem(key);
			String itemCode = customerItem.getItemCode();
			String itemName = customerItem.getItemName();
			String itemSpec = customerItem.getItemSpec();
			String salePrice = customerItem.getSalePrice();
			String salePriceStr = StringUtil.moneyFormatStr(salePrice);
			String priceType = customerItem.getPriceType();
%>
						<a href="#" onclick="clickItemCustomerSaleWeightInsertItemSearch('<%=itemCode %>')">
							<table style="border: 0px solid #999999 ; border-top: 0px solid #BBBBBB ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
								<tr style="height: 40px ;">
									<td style="text-align: left ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; ">
										<input type="hidden" id="hdnItemNameCustomerSaleWeightInsertItemSearch<%=itemCode %>" value="<%=itemName %>" />
										<input type="hidden" id="hdnItemSpecCustomerSaleWeightInsertItemSearch<%=itemCode %>" value="<%=itemSpec %>" />
										<input type="hidden" id="hdnSalePriceCustomerSaleWeightInsertItemSearch<%=itemCode %>" value="<%=salePrice %>" />
										<input type="hidden" id="hdnPriceTypeCustomerSaleWeightInsertItemSearch<%=itemCode %>" value="<%=priceType %>" />
											<span style="color:#222222 ; font-size:14px ; "><%=itemName + " " + itemSpec %></span>
									</td>
									<td style="width: 80px ; text-align: right ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; "><%=salePriceStr %></span></td>
								</tr>
							</table>
						</a>
<%
		}
%>
						<div id="divResultMessageCustomerSaleWeightInsertItemDetail"></div>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
