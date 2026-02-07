<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %><%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %><%@ page import="com.joainfo.gasmax.bean.list.CustomerSearchMap" %><%@ page import="java.util.LinkedHashMap" %><%@ page import="com.joainfo.gasmax.biz.BizCustomerSearch" %><%@ page import="java.util.HashMap" %><%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%
	// Always emit a clean XML document (no leading whitespace / no duplicate XML declaration)
	response.setCharacterEncoding("UTF-8");
	response.setContentType("text/xml; charset=UTF-8");
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if ("HTTP/1.1".equals(request.getProtocol())) {
		response.setHeader("Cache-Control", "no-cache");
	}
	request.setCharacterEncoding("UTF-8");

	try { out.clearBuffer(); } catch (Exception ignore) {}
	out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

	final String empty = "<CustomerSearches></CustomerSearches>";

	try {
		String keyword = request.getParameter("keyword");
		if (keyword == null) keyword = "";

		String uuid = request.getParameter("uuid");
		if (uuid == null) uuid = "";

		AppUser appUser = null;
		if (!uuid.isEmpty()) {
			try {
				appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid, uuid);
			} catch (Exception ex) {
				System.out.println("❌ Error getting appUser: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		if (appUser == null) {
			appUser = (AppUser) session.getAttribute("USER_INFO");
		}

		if (appUser == null) {
			out.write("<CustomerSearches><session>X</session></CustomerSearches>");
			return;
		}

		String serverIp = appUser.getIpAddress() == null ? "" : appUser.getIpAddress();
		String catalogName = appUser.getDbCatalogName() == null ? "" : appUser.getDbCatalogName();
		String areaCode = appUser.getAreaCode() == null ? "" : appUser.getAreaCode();
		String employeeCode = appUser.getEmployeeCode() == null ? "" : appUser.getEmployeeCode();
		String grantCode = appUser.getMenuPermission() == null ? "" : appUser.getMenuPermission();

		String customerCode = "";
		String customerSearchType = "A";

		CustomerSearchMap customerSearches = null;
		try {
			customerSearches = BizCustomerSearch.getInstance().getCustomerSearches(
				serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode
			);
		} catch (Exception ex) {
			System.out.println("❌ Error getting customer searches: " + ex.getMessage());
			ex.printStackTrace();
		}

		if (customerSearches == null) {
			out.write(empty);
			return;
		}

		LinkedHashMap<String, CustomerSearch> innerMap = null;
		try {
			innerMap = customerSearches.getCustomerSearches();
		} catch (Exception ignore) {}

		if (innerMap == null || innerMap.isEmpty()) {
			out.write(empty);
			return;
		}

		session.setAttribute("CUSTOMER_SEARCH", customerSearches);

		HashMap<String, String> pagingXML = null;
		try {
			pagingXML = customerSearches.toPagingXML(5000);
		} catch (Exception ex) {
			System.out.println("❌ Error in toPagingXML: " + ex.getMessage());
			ex.printStackTrace();
		}

		if (pagingXML == null || pagingXML.isEmpty()) {
			pagingXML = new HashMap<String, String>();
			pagingXML.put("1", empty);
		}
		session.setAttribute("CUSTOMER_SEARCH_PAGING", pagingXML);

		String xml = pagingXML.get("1");
		if (xml == null || xml.isEmpty()) {
			xml = empty;
		}

		out.write(xml.trim());
	} catch (Exception e) {
		System.out.println("❌ Error in search_customer_keyword_ajx.jsp: " + e.getMessage());
		e.printStackTrace();
		out.write("<CustomerSearches><error>" + String.valueOf(e.getMessage()) + "</error></CustomerSearches>");
	}
%>
