<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page session="true" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.attribute.USER_REGISTER_TYPE" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%>
<%
	try {
		String sessionToken = request.getParameter("sessionToken");

		String[] keys = {
				"CUSTOMER_BOOK_ITEM_BALANCE_HPG_DETAIL_LIST",
				"CUSTOMER_BOOK_ITEM_BALANCE_HPG_DETAIL_LIST_PAGING",
				"CUSTOMER_BOOK_ITEM_BALANCE_HPG",
				"CUSTOMER_BOOK_ITEM_BALANCE_HPG_PAGING",
				"CUSTOMER_BOOK_TAX_INVOICE",
				"CUSTOMER_BOOK_TAX_INVOICE_PAGING",
				"CUSTOMER_BOOK_VOLUME",
				"CUSTOMER_BOOK_VOLUME_PAGING",
				"CUSTOMER_BOOK_WEIGHT",
				"CUSTOMER_BOOK_WEIGHT_PAGING",
				"CUSTOMER_SAFTY_CHECK_LIST"
		};

		if (sessionToken != null && !sessionToken.trim().isEmpty()) {
			for (String key : keys) {
				RedisUtil.removeFromRedis(sessionToken, key);
			}
		} else {
			for (String key : keys) {
				session.removeAttribute(key);
			}
		}

		out.print("<result><code>S</code></result>");
	} catch(Exception e) {
		out.print("<result><code>E</code></result>");
	}
%>
