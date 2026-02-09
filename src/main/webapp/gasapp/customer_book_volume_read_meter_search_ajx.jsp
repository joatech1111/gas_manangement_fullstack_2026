<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeReadMeter" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeReadMeterMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeReadMeter" %>
<%@ page import="java.util.HashMap" %>
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
		String customerCode = request.getParameter("customerCode");
		String startDate = StringUtil.stringReplace(request.getParameter("startDate"));
		String endDate = StringUtil.stringReplace(request.getParameter("endDate"));
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			CustomerVolumeReadMeterMap customerVolumeReadMeters = BizCustomerVolumeReadMeter.getInstance().getCustomerVolumeReadMeters(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			//session.setAttribute("CUSTOMER_BOOK_VOLUME", customerVolumeReadMeters);

			String sessionToken = request.getParameter("sessionToken"); // 클라이언트에서 전달받은 토큰
			RedisUtil.saveToRedis(sessionToken, "CUSTOMER_BOOK_VOLUME", customerVolumeReadMeters);


			HashMap<String, String> pagingXML = new HashMap<String, String>(customerVolumeReadMeters.toPagingXML(5000));
			//session.setAttribute("CUSTOMER_BOOK_VOLUME_PAGING", pagingXML);

			RedisUtil.saveToRedis(sessionToken, "CUSTOMER_BOOK_VOLUME_PAGING", customerVolumeReadMeters);

			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<CustomerVolumeReadMeters>", "<CustomerVolumeReadMeters><totalRowCount>" + customerVolumeReadMeters.getTotalRowCount() + "</totalRowCount><totalNowMonthAmount>" + customerVolumeReadMeters.getTotalNowMonthAmount() + "</totalNowMonthAmount>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
