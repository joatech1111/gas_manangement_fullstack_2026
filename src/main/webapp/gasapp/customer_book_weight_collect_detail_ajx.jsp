<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.CustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerWeightCollectMap" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	try{
		String key = request.getParameter("key");
		CustomerWeightCollectMap customerWeightCollects = (CustomerWeightCollectMap)session.getAttribute("CUSTOMER_BOOK_WEIGHT");
		if (customerWeightCollects != null){
			CustomerWeightCollect customerWeightCollect = customerWeightCollects.getCustomerWeightCollect(key);
			if (customerWeightCollect!=null){
				out.print(customerWeightCollect.toXML());
			}
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>