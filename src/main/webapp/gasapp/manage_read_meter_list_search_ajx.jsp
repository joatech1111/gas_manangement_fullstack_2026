<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.ReadMeterList" %>
<%@ page import="com.joainfo.gasmax.bean.list.ReadMeterListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizReadMeterList" %>
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
		String employeeCode = request.getParameter("employeeCode");
		String collectTypeCode = request.getParameter("collectTypeCode");
		String keyword = request.getParameter("keyword");
		String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
		String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			ReadMeterListMap readMeterLists = BizReadMeterList.getInstance().getReadMeterLists(serverIp, catalogName, areaCode, employeeCode, collectTypeCode, keyword, startDate, endDate);
			session.setAttribute("READ_METER_LIST", readMeterLists);
			HashMap<String, String> pagingXML = new HashMap<String, String>(readMeterLists.toPagingXML(3000));
			session.setAttribute("READ_METER_LIST_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<ReadMeterLists>", "<ReadMeterLists><totalRowCount>" + readMeterLists.getTotalRowCount() + "</totalRowCount><totalNowAmount>" + readMeterLists.getTotalNowAmount() + "</totalNowAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>