<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
    request.setCharacterEncoding("UTF-8");
%><%
    try {
      //  CustomerSearch customerSearch = (CustomerSearch) session.getAttribute("CURRENT_CUSTOMER");
        String sessionToken = request.getParameter("sessionToken");
        CustomerSearch customerSearch =RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class);

        System.out.println(customerSearch);

        if (customerSearch != null) {
            out.print(customerSearch.toXML().replace("&", "&amp;"));
        } else {
            out.print("<session>X</session>");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>