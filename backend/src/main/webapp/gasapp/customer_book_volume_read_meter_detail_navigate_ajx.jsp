<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeReadMeter" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeReadMeterMap" %>
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
		//System.out.println("key=>" + key);
		String direction = request.getParameter("direction");
		//CustomerVolumeReadMeterMap customerVolumeReadMeters = (CustomerVolumeReadMeterMap)session.getAttribute("CUSTOMER_BOOK_VOLUME");

		String sessionToken = request.getParameter("sessionToken"); // 클라이언트에서 전달된 토큰
		CustomerVolumeReadMeterMap customerVolumeReadMeters = RedisUtil.getFromRedis(sessionToken, "CUSTOMER_BOOK_VOLUME", CustomerVolumeReadMeterMap.class);


		if (customerVolumeReadMeters != null){
			java.util.Iterator<String> iterator = customerVolumeReadMeters.getCustomerVolumeReadMeters().keySet().iterator();
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
	}
%>
