<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.SaleList" %>
<%@ page import="com.joainfo.gasmax.bean.list.SaleListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizSaleList" %>
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
		String saleType = request.getParameter("searchOption");
		String employeeCode = request.getParameter("employeeCode");
		String collectTypeCode = request.getParameter("collectTypeCode");
		String keyword = request.getParameter("keyword");
		String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
		String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			SaleListMap saleLists = BizSaleList.getInstance().getSaleLists(serverIp, catalogName, areaCode, employeeCode, saleType, collectTypeCode, keyword, startDate, endDate);
			session.setAttribute("SALE_LIST", saleLists);
			HashMap<String, String> pagingXML = new HashMap<String, String>(saleLists.toPagingXML(5000));
			session.setAttribute("SALE_LIST_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<SaleLists>", "<SaleLists><totalRowCount>" + saleLists.getTotalRowCount() + "</totalRowCount><totalCollectAmount>" + saleLists.getTotalCollectAmount() + "</totalCollectAmount><totalUnpaidAmount>" + saleLists.getTotalUnpaidAmount() + "</totalUnpaidAmount><totalDiscountAmount>" + saleLists.getTotalDiscountAmount() + "</totalDiscountAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>