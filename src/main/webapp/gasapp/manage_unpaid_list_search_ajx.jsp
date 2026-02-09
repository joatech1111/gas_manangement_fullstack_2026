<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.UnpaidList" %>
<%@ page import="com.joainfo.gasmax.bean.list.UnpaidListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizUnpaidList" %>
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
		String searchOption = request.getParameter("searchOption");
		String employeeCode = request.getParameter("employeeCode");
		String collectTypeCode = request.getParameter("collectTypeCode");
		String keyword = request.getParameter("keyword");
		String weightUnpaidType = "1".equals(searchOption)?"1":null;
		String volumeUnpaidType = "2".equals(searchOption)?"1":null;
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			UnpaidListMap unpaidLists = BizUnpaidList.getInstance().getUnpaidLists(serverIp, catalogName, areaCode, employeeCode, collectTypeCode, keyword, weightUnpaidType, volumeUnpaidType);
			session.setAttribute("UNPAID_LIST", unpaidLists);
			HashMap<String, String> pagingXML = new HashMap<String, String>(unpaidLists.toPagingXML(5000));
			session.setAttribute("UNPAID_LIST_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<UnpaidLists>", "<UnpaidLists><totalRowCount>" + unpaidLists.getTotalRowCount() + "</totalRowCount><totalUnpaidAmount>" + unpaidLists.getTotalUnpaidAmount() + "</totalUnpaidAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>