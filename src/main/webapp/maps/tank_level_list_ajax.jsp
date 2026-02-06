<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="utf-8"%><%@
page import="com.joainfo.common.util.StringUtil" %><%@
page import="com.joainfo.gasmaxeye.bean.list.AreaTypeCodeMap"%><%@
page import="com.joainfo.gasmaxeye.bean.list.EmployeeCodeMap"%><%@
page import="com.joainfo.gasmaxeye.bean.list.MyCompanyMap"%><%@
page import="com.joainfo.gasmaxeye.bean.list.TankLevelListMap"%><%@
page import="com.joainfo.gasmaxeye.biz.BizAreaTypeCode"%><%@
page import="com.joainfo.gasmaxeye.biz.BizEmployeeCode"%><%@
page import="com.joainfo.gasmaxeye.biz.BizTankLevelList"%><%@
page import="java.util.Calendar" %><%@
page import="java.util.Locale" %><%@
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
		String sawonCode = request.getParameter("sawonCode");
		String areaTypeCode = request.getParameter("areaTypeCode");
		String keyword = request.getParameter("keyword");
		//String levelStates = request.getParameter("levelStates");
		//String receiveDateOver = request.getParameter("receiveDateOver");
		//String uniformLevel = request.getParameter("uniformLevel");
		//String lowBattery = request.getParameter("lowBattery");
		
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
			if (sawonCode == null) sawonCode = "";
			if (areaTypeCode == null) areaTypeCode = "";
			if (keyword == null) keyword = "";
			//if (levelStates == null) levelStates = "";
			//if (receiveDateOver == null) receiveDateOver = "";
			//if (uniformLevel == null) uniformLevel = "";
			//if (lowBattery == null) lowBattery = "";

			String dayLevel0 = StringUtil.getNowString();
			String dayLevel9 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -9));
			String dayLevel8 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -8));
			String dayLevel7 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -7));
			String dayLevel6 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -6));
			String dayLevel5 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -5));
			String dayLevel4 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -4));
			String dayLevel3 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -3));
			String dayLevel2 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -2));
			String dayLevel1 = StringUtil.stringReplace(StringUtil.addDay(dayLevel0, -1));
			
			String serverIp = BizTankLevelList.DEFAULT_TANK_LEVEL_LIST_SQL_CONFIG;
			String catalogName = BizTankLevelList.DEFAULT_TANK_LEVEL_LIST_CATATLOG_NAME;

			AreaTypeCodeMap areaTypeCodes = BizAreaTypeCode.getInstance().getAreaTypeCodes(serverIp, catalogName, clientNumber);
			EmployeeCodeMap employeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, clientNumber);
			
			//MyCompanyMap myCompanys = BizTankLevelList.getInstance().getMyCompany(clientNumber);
			
			TankLevelListMap tankLevelLists = BizTankLevelList.getInstance().getTankLevelLists(
					clientNumber, sawonCode, areaTypeCode, keyword, "", "", "", "", "",
					dayLevel9, dayLevel8, dayLevel7, dayLevel6, dayLevel5, dayLevel4, dayLevel3, dayLevel2, dayLevel1, dayLevel0);
			
			if (tankLevelLists != null) {
				out.print(String.format("{%s, %s, %s}",areaTypeCodes.toJSON(), employeeCodes.toJSON(), tankLevelLists.toJSON()));
			} else {
				out.print("{\"error\":{\"msg\":\"Invalid result\"}}");
			}
		}
	} catch (Exception e){
		e.printStackTrace();
		
		out.print("{\"error\":{\"msg\":\"Backend Error\"}}");
	}
%>