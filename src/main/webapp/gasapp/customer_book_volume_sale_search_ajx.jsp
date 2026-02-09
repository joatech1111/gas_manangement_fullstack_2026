<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeSale" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeSaleMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeSale" %>
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

			CustomerVolumeSaleMap customerVolumeSales = BizCustomerVolumeSale.getInstance().getCustomerVolumeSales(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
//			session.setAttribute("CUSTOMER_BOOK_VOLUME", customerVolumeSales);
//			HashMap<String, String> pagingXML = new HashMap<String, String>(customerVolumeSales.toPagingXML(5000));
//			session.setAttribute("CUSTOMER_BOOK_VOLUME_PAGING", pagingXML);

			String sessionToken = request.getParameter("sessionToken");

			// CUSTOMER_BOOK_VOLUME 저장
			RedisUtil.saveToRedis(sessionToken, "CUSTOMER_BOOK_VOLUME", customerVolumeSales);

			// CUSTOMER_BOOK_VOLUME_PAGING 저장
			HashMap<String, String> pagingXML = new HashMap<>(customerVolumeSales.toPagingXML(5000));
			RedisUtil.saveToRedis(sessionToken, "CUSTOMER_BOOK_VOLUME_PAGING", pagingXML);


			String pageNumber = "1";
			String xml = pagingXML.get(pageNumber);
			String modifiedXml = xml==null?"":xml.replace("<CustomerVolumeSales>", "<CustomerVolumeSales><totalRowCount>" + customerVolumeSales.getTotalRowCount() + "</totalRowCount><totalSupplyQuantity>" + customerVolumeSales.getTotalSupplyQuantity() + "</totalSupplyQuantity>");
			out.print(modifiedXml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
