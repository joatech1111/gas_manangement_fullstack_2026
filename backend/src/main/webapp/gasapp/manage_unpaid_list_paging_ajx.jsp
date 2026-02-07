<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%><%@
page import="java.util.HashMap" %><%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String pageNumber = request.getParameter("pageNumber");
	try{
		@SuppressWarnings("unchecked")
		HashMap<String, String> pagingXML = (HashMap<String, String>)session.getAttribute("UNPAID_LIST_PAGING");
		
		if (pagingXML!=null) {
			String xml = pagingXML.get(pageNumber);
			out.print(xml);
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>