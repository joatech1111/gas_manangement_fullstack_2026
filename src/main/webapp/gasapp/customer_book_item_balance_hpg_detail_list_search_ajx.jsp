<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerItemBalanceHPGDetailList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerItemBalanceHPGDetailListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerItemBalanceHPGDetailList" %>
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
		String itemCode = request.getParameter("itemCode");
		String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
		String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
		String preBalance = StringUtil.stringReplace(request.getParameter("preBalance"));
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String gasType = appUser.getGasType();
			CustomerItemBalanceHPGDetailListMap customerItemBalanceHPGDetailLists;
			if ("HIGH".equals(gasType)){
				customerItemBalanceHPGDetailLists = BizCustomerItemBalanceHPGDetailList.getInstance().getCustomerItemBalanceHPGDetailLists(serverIp, catalogName, areaCode, customerCode, startDate, endDate, itemCode);
			} else {
				customerItemBalanceHPGDetailLists = BizCustomerItemBalanceHPGDetailList.getInstance().getCustomerItemBalanceLPGDetailLists(serverIp, catalogName, areaCode, customerCode, startDate, endDate, itemCode);
			}
			customerItemBalanceHPGDetailLists.setPreBalance(new Integer(preBalance).intValue());
			if (customerItemBalanceHPGDetailLists != null){
				session.setAttribute("CUSTOMER_BOOK_ITEM_BALANCE_HPG_DETAIL_LIST", customerItemBalanceHPGDetailLists);
				HashMap<String, String> pagingXML = new HashMap<String, String>(customerItemBalanceHPGDetailLists.toPagingXML(5000));
				session.setAttribute("CUSTOMER_BOOK_ITEM_BALANCE_HPG_DETAIL_LIST_PAGING", pagingXML);
				String pageNumber = "1";
				String xml = pagingXML.get(pageNumber);
				String modifiedXml = xml==null?"":xml.replace("<CustomerItemBalanceHPGDetailLists>", "<CustomerItemBalanceHPGDetailLists><totalRowCount>" + customerItemBalanceHPGDetailLists.getTotalRowCount() + "</totalRowCount>");
				out.print(modifiedXml);
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>