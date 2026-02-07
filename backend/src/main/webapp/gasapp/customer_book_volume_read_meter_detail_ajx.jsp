<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerVolumeReadMeter" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerVolumeReadMeterMap" %>
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
        String key = request.getParameter("key");
        String sessionToken = request.getParameter("sessionToken"); // ✅ Redis 키를 위한 토큰 받기
        CustomerVolumeReadMeterMap customerVolumeReadMeters = RedisUtil.getFromRedis(sessionToken, "CUSTOMER_BOOK_VOLUME", CustomerVolumeReadMeterMap.class);

        if (customerVolumeReadMeters != null) {
            CustomerVolumeReadMeter customerVolumeReadMeter = customerVolumeReadMeters.getCustomerVolumeReadMeter(key);
            if (customerVolumeReadMeter != null) {
                out.print(customerVolumeReadMeter.toXML());
            }
        } else {
            out.print("<session>X</session>");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
