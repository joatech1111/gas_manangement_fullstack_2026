<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerWeightSale" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerWeightSaleMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerWeightSale" %>
<%@ page import="java.util.HashMap" %>
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
		String customerCode = request.getParameter("customerCode");
		String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
		String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CustomerWeightSaleMap customerWeightSales = BizCustomerWeightSale.getInstance().getCustomerWeightSales(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			session.setAttribute("CUSTOMER_BOOK_WEIGHT", customerWeightSales);
			HashMap<String, String> pagingXML = new HashMap<String, String>(customerWeightSales.toPagingXML(5000));
			session.setAttribute("CUSTOMER_BOOK_WEIGHT_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<CustomerWeightSales>", "<CustomerWeightSales><totalSupplyAmount>" + customerWeightSales.getTotalSupplyAmount() + "</totalSupplyAmount><totalTaxAmount>" + customerWeightSales.getTotalTaxAmount() + "</totalTaxAmount><totalSumAmount>" + customerWeightSales.getTotalSumAmount() + "</totalSumAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>