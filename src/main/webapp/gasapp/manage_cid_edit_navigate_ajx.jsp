<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.CidList" %>
<%@ page import="com.joainfo.gasmax.bean.list.CidListMap" %>
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
		CidListMap cidLists = (CidListMap)session.getAttribute("CID_LIST");
		if (cidLists != null){
			java.util.Iterator<String> iterator = cidLists.getCidLists().keySet().iterator();
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