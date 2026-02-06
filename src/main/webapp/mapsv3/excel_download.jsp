<%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8" pageEncoding="utf-8"%><%@
page import="com.joainfo.common.util.StringUtil" %><%@
page import="com.joainfo.gasmaxeye.bean.TankLevelList"%><%@
page import="com.joainfo.gasmaxeye.bean.list.TankLevelListMap"%><%@
page import="com.joainfo.gasmaxeye.biz.BizTankLevelList"%><%@
page import="java.util.Calendar" %><%@
page import="java.util.Locale" %><%@
page import="java.util.LinkedHashMap" %><%@
page import="java.util.HashMap" %><%@
page import="java.util.StringTokenizer" %><%@
page import="java.text.SimpleDateFormat" %><%@
page import="java.util.TimeZone" %><%@
page import="sun.misc.BASE64Decoder" %><%
	//response.setHeader("Cache-Control", "no-store");
	//response.setHeader("Pragma", "no-cache");
	//response.setDateHeader("Expires", 0);
	//if(request.getProtocol().equals("HTTP/1.1"))
		//response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	try{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		String now = dateFormat.format(c.getTime());
		
		response.setHeader("Content-Disposition", "attachment;filename=" + now.substring(0, 10) + "_customer.xls");
		response.setContentType("application/vnd.ms-excel");
		
		String qid = request.getParameter("qid");
		String clientNumber = null;
		String sawonCode = request.getParameter("sawon_code");
		String areaTypeCode = request.getParameter("area_type_code");
		String keyword = request.getParameter("keyword");
		String levelStates = request.getParameter("indicator_level");
		String state = request.getParameter("indicator_state");
		
		while (qid != null && qid.length() % 4 != 0) {
			qid += "=";
		}
		
		if (qid != null) {
			try {
				BASE64Decoder decoder = new BASE64Decoder();
				String parameters = new String(decoder.decodeBuffer(qid));
		
				StringTokenizer paramSt = new StringTokenizer(parameters, "&");
				while(paramSt.hasMoreTokens()) {
					String parameter = paramSt.nextToken();
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
			out.print("<div>Invalid parameter</div>");
		} else {
			if (sawonCode == null) sawonCode = "";
			if (areaTypeCode == null) areaTypeCode = "";
			if (keyword == null) keyword = "";
			if (levelStates == null) levelStates = "";
			if (state == null) state = "";
			
			//levelStates.replaceAll("|", ",");
			String strLevels = "";
			StringTokenizer st = new StringTokenizer(levelStates, "|");
			while(st.hasMoreTokens()) {
				int level = Integer.parseInt(st.nextToken());
				
				if (level > 0) {
					if (strLevels.length() > 0) strLevels += ",";
					strLevels += (level-1);
				}
			}
			
			String noneMapPoint = "1".equals(state) ? "Y" : "";
			String receiveDateOver = "2".equals(state) ? "Y" : "";
			String uniformLevel = "3".equals(state) ? "Y" : "";
			String lowBattery = "4".equals(state) ? "Y" : "";			
			
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
			
			TankLevelListMap tankLevelLists = BizTankLevelList.getInstance().getTankLevelLists(
					clientNumber, sawonCode, areaTypeCode, keyword, strLevels, noneMapPoint, receiveDateOver, uniformLevel, lowBattery,
					dayLevel9, dayLevel8, dayLevel7, dayLevel6, dayLevel5, dayLevel4, dayLevel3, dayLevel2, dayLevel1, dayLevel0);
			
			if (tankLevelLists != null) {
				out.print("<div>생성일 "+ now + "</div>");
				
				out.print("<table border=1>");
				out.print("<thead><tr><th>거래처명</th><th>잔량</th><th>배터리</th><th>수신일시</th><th>주소</th></tr></thead>");
				out.print("<tbody>");
				
				LinkedHashMap<String, TankLevelList> tankLevelList = tankLevelLists.getTankLevelLists();
				java.util.Iterator<String> iterator = tankLevelList.keySet().iterator(); 
				while (iterator.hasNext()) { 
					String key = iterator.next();
					TankLevelList tankLevel = tankLevelList.get(key);
					
					String lastReceiveDateTime = "";
					if (tankLevel.getLastReceiveDate().length() == 8 && tankLevel.getLastReceiveTime().length() == 6) {
						lastReceiveDateTime = tankLevel.getLastReceiveDate().substring(0, 4) 
								+ "-" + tankLevel.getLastReceiveDate().substring(4, 6)
								+ "-" + tankLevel.getLastReceiveDate().substring(6, 8)
								+ " " + tankLevel.getLastReceiveTime().substring(0, 2)
								+ ":" + tankLevel.getLastReceiveTime().substring(2, 4)
								+ ":" + tankLevel.getLastReceiveTime().substring(4, 6);
					}
					
					out.print("<tr><td>" + tankLevel.getCustomerName() 
							+ "</td><td>" + tankLevel.getLastLevel() 
							+ "</td><td>" + (Integer.parseInt(tankLevel.getBatteryPercent()) > 0 ? tankLevel.getBatteryPercent() : "")
							+ "</td><td>" + lastReceiveDateTime 
							+ "</td><td>" + tankLevel.getAddress() + "</td></tr>");
		  		}
				
				out.print("</tbody></table>");
			} else {
				out.print("<div>Invalid result</div>");
			}
		}
	} catch (Exception e){
		e.printStackTrace();
		
		out.print("<div>Backend Error</div>");
	}
%>