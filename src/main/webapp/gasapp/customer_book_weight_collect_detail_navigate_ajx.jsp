<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.CustomerWeightCollect" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerWeightCollectMap" %>
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
		String key = request.getParameter("key");
		String direction = request.getParameter("direction");
		String sessionToken = request.getParameter("sessionToken");
		CustomerWeightCollectMap customerWeightCollects = RedisUtil.getFromRedis(sessionToken, "CUSTOMER_BOOK_WEIGHT", CustomerWeightCollectMap.class);
		if (customerWeightCollects != null){
			java.util.Iterator<String> iterator = customerWeightCollects.getCustomerWeightCollects().keySet().iterator();
			String priorKey = "X";
			String id = "X";
			while (iterator.hasNext()) { 
				priorKey = new String(id);
				id = (String)iterator.next();
				if (id.equals(key)) break;
			}
			String newKey = "X";
			if ("prior".equals(direction)){
				newKey = priorKey;
			} else if ("next".equals(direction)){
				if (iterator.hasNext()){
					newKey = (String)iterator.next();
				} else {
					newKey = "X";
				}
			}
			out.print("<key>" + newKey + "</key>");
		}else{
			out.print("<session>X</session>");
		}
	} catch (Exception e){
		e.printStackTrace();
		out.print("<session>X</session>");
	}
%>