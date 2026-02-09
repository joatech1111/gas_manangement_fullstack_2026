<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerItemBalanceHPG" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerItemBalanceHPGMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerItemBalanceHPG" %>
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
			String gasType = appUser.getGasType();
			CustomerItemBalanceHPGMap customerItemBalanceHPGs;
			if ("HIGH".equals(gasType)){
				customerItemBalanceHPGs = BizCustomerItemBalanceHPG.getInstance().getCustomerItemBalanceHPGs(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			} else {
				customerItemBalanceHPGs = BizCustomerItemBalanceHPG.getInstance().getCustomerItemBalanceLPGs(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			}
			if (customerItemBalanceHPGs != null){
				session.setAttribute("CUSTOMER_BOOK_ITEM_BALANCE_HPG", customerItemBalanceHPGs);
				HashMap<String, String> pagingXML = new HashMap<String, String>(customerItemBalanceHPGs.toPagingXML(5000));
				session.setAttribute("CUSTOMER_BOOK_ITEM_BALANCE_HPG_PAGING", pagingXML);
				String pageNumber = "1";
				String xml = pagingXML.get(pageNumber);
				String modifiedXml = xml==null?"":xml.replace("<CustomerItemBalanceHPGs>", "<CustomerItemBalanceHPGs><totalRowCount>" + customerItemBalanceHPGs.getTotalRowCount() + "</totalRowCount>");
				out.print(modifiedXml);
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>