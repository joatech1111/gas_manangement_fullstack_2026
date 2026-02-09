<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CidList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CidListMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCidList" %>
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
		String cidDate = request.getParameter("cidDate");
		cidDate = StringUtil.stringReplace(cidDate);
		String employeeCode = request.getParameter("employeeCode");
		String newDelivery = request.getParameter("newDelivery");
		if ("false".equals(newDelivery)) newDelivery = null;
		String delivered = request.getParameter("delivered");
		if ("false".equals(delivered)) delivered = null;
		String deliveryComplete = request.getParameter("deliveryComplete");
		if ("false".equals(deliveryComplete)) deliveryComplete = null;
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CidListMap cidLists = BizCidList.getInstance().getCidLists(serverIp, catalogName, areaCode, cidDate, employeeCode, newDelivery, delivered, deliveryComplete);
			session.setAttribute("CID_LIST", cidLists);
			HashMap<String, String> pagingXML = new HashMap<String, String>(cidLists.toPagingXML(5000));
			session.setAttribute("CID_LIST_PAGING", pagingXML);
			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"": xml.replace("<CidLists>", "<CidLists><totalRowCount>" + cidLists.getTotalRowCount() + "</totalRowCount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>