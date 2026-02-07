<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="utf-8"%><%@
page import="com.joainfo.common.util.StringUtil" %><%@ 
page import="com.joainfo.gasmaxplus.bean.list.BulkChargeStateMap" %><%@ 
page import="com.joainfo.gasmaxplus.bean.list.BulkGpsLineMap" %><%@ 
page import="com.joainfo.gasmaxplus.bean.list.BulkLastGpsMap" %><%@ 
page import="com.joainfo.gasmaxplus.biz.BizManageBulk" %><%@
page import="java.util.Map" %><%@
page import="java.util.HashMap" %><%@
page import="java.util.StringTokenizer" %><%@
page import="sun.misc.BASE64Decoder" %><%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	try{
		String qid = request.getParameter("qid");
		String clientNumber = null;
		String bulkCode = request.getParameter("bulkCode");
		String sawonCode = request.getParameter("sawonCode");
		String searchDate = request.getParameter("searchDate");		// yyyymmdd
		
		while (qid != null && qid.length() % 4 != 0) {
			qid += "=";
		}
		
		if (qid != null) {
			try {
				BASE64Decoder decoder = new BASE64Decoder();
				String parameters = new String(decoder.decodeBuffer(qid));
		
				StringTokenizer st = new StringTokenizer(parameters, "&");
				while(st.hasMoreTokens()) {
					String parameter = st.nextToken();
					int eIndex = parameter.indexOf("=");
					if (eIndex < 0) continue;
					
					String paramKey = parameter.substring(0, eIndex);
					String paramValue = parameter.length() > eIndex + 1 ? parameter.substring(eIndex+1) : "";
		
					if ("clientNumber".equals(paramKey)) {
						clientNumber = paramValue;
						break;
					}
				}
			} catch(Exception ex) {}
		}
		
		if (clientNumber == null || "".equals(clientNumber.trim())) {
			// Error
			out.print("{\"error\":{\"msg\":\"Invalid parameter\"}}");
		} else {			
			if (bulkCode == null) bulkCode = "";
			if (sawonCode == null) sawonCode = "";
			if (searchDate == null) searchDate = "";
			
			// 벌크로리 충전위치
			BulkChargeStateMap bulkChargeStates = BizManageBulk.getInstance().getBulkChargeStates(clientNumber, searchDate, bulkCode, "");

			// 벌크로리궤적
			BulkGpsLineMap bulkGpsLines = BizManageBulk.getInstance().getBulkGpsLines(clientNumber, bulkCode, searchDate);
			
			// 차량 현재 위치
			BulkLastGpsMap bulkLastGpss = BizManageBulk.getInstance().getBulkLastGps(clientNumber, bulkCode, searchDate);
			
			out.print(String.format("{%s, %s, %s}", bulkChargeStates.toJSON(), bulkGpsLines.toJSON(), bulkLastGpss.toJSON()));
		}
	} catch (Exception e){
		e.printStackTrace();
		
		out.print("{\"error\":{\"msg\":\"Server Error\"}}");
	}
%>