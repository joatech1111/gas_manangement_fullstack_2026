<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerTaxInvoice" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerTaxInvoiceMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerTaxInvoice" %>
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
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CustomerTaxInvoiceMap customerTaxInvoices = BizCustomerTaxInvoice.getInstance().getCustomerTaxInvoices(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			session.setAttribute("CUSTOMER_BOOK_TAX_INVOICE", customerTaxInvoices);
			HashMap<String, String> pagingXML = new HashMap<String, String>(customerTaxInvoices.toPagingXML(20));
			session.setAttribute("CUSTOMER_BOOK_TAX_INVOICE_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<CustomerTaxInvoices>", "<CustomerTaxInvoices><totalRowCount>" + customerTaxInvoices.getTotalRowCount() + "</totalRowCount><totalSupplyAmount>" + customerTaxInvoices.getTotalSupplyAmount() + "</totalSupplyAmount><totalTaxAmount>" + customerTaxInvoices.getTotalTaxAmount() + "</totalTaxAmount><totalSumAmount>" + customerTaxInvoices.getTotalSumAmount() + "</totalSumAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>