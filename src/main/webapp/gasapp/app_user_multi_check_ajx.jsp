<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page session="true" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.list.AppUserMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="java.util.HashMap" %><%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String macNumber = request.getParameter("macNumber");
	String appVersion = request.getParameter("appVersion");
	String uuid = request.getParameter("uuid");

	if(appVersion == null || "undefined".equals(appVersion) || "".equals(appVersion) ) appVersion = "0";

	if (macNumber != null) {
		macNumber = StringUtil.decodeBase64(macNumber);
	}

    AppUserMap appUsers = BizAppUser.getInstance().getMultiAppUsers(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid);  // 사용자 객체
	//AppUserMap appUsers = BizAppUser.getInstance().getMultiAppUsers(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, "test");  // 사용자 객체
    if(appUsers != null) {
        if ("null".equals(macNumber) || macNumber == null) {
     		out.print("<Result>N</Result>");
        } else {
            session.setAttribute("USER_INFOS", appUsers);
            HashMap<String, String> pagingXML = new HashMap<String, String>(appUsers.toPagingMultiXML(50));
            String pageNumber = "1";
            String xml = pagingXML.get(pageNumber);
            if (xml != null) {
                out.print(xml);
                //String modifiedXml = xml==null?"":xml.replace("<AppUsers>", "<AppUsers><totalRowCount>" + appUsers.getTotalRowCount() + "</totalRowCount>");
            } else {
         		out.print("<Result>N</Result>");
            }
        }
	} else {
 		out.print("<Result>N</Result>");
	}
%>
