<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.CollectTypeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.CollectTypeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizCollectTypeCode" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
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
		String now = request.getParameter("now");
		String uuid = request.getParameter("uuid");
//		String startDate = StringUtil.addMonth(now, -1); //한달전
		String startDate = now; 
		String endDate = now;
		// 세션에서 사원목록 가져오기
		//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
		String sessionToken = request.getParameter("sessionToken");
		EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
		// 아직 세션에 사원목록이 등록되지 않았다면 세션에 등록하기
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid, uuid);
		if (appUser != null){
			String keyword = "";
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String userEmployeeCode = appUser.getEmployeeCode();
			String userEmployeeName = appUser.getEmployeeName();
			String menuPermission = appUser.getMenuPermission();
			String [] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
			boolean changeEmployee = "0".equals(permission[2])?true:false;
			
			if(employeeCodes==null){
					EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
					employeeCodes = newEmployeeCodes;
					session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
			}
		
			CollectTypeCodeMap collectTypeCodes = (CollectTypeCodeMap)session.getAttribute("COLLECT_TYPE_CODE");
			if(collectTypeCodes==null){
				CollectTypeCodeMap newCollectTypeCodes = BizCollectTypeCode.getInstance().getCollectTypeCodes(serverIp, catalogName);
				collectTypeCodes = newCollectTypeCodes;
				session.setAttribute("COLLECT_TYPE_CODE", newCollectTypeCodes);
			}

%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 40px ; "><span style="font-size: 14px ; ">사원: </span></td>
								<td style="width: 120px ; text-align: left ; ">
<%
	if (changeEmployee) {
%>
									<select id="selectEmployeeManageReadMeterList" data-mini="true" style="font-size: 14px ; ">
										<option value="" data-placeholder="true">전체사원</option>
<%
		// 사원 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = employeeCodes.getEmployeeCodes().keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			EmployeeCode employeeCode =  employeeCodes.getEmployeeCode(key);
			String employeeCodeStr = employeeCode.getEmployeeCode();
			String employeeNameStr = employeeCode.getEmployeeName();
%>
										<option value="<%= employeeCodeStr %>"><%= employeeNameStr %></option>
<%
		}
%>
										<option value="noEmployee">미지정</option>
									</select>
<%
	} else {
%>
									<input type="hidden" id ="selectEmployeeManageReadMeterList" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
								</td>
								<td style="width: 120px ; text-align: left ; ">
									<select name="selectPayTypeManageReadMeterList" id="selectPayTypeManageReadMeterList" data-mini="true" data-inset="false" style="font-size: 14px ; ">
										<option value="" data-placeholder="true">수금방법</option>
<%
		// 수금방법 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = collectTypeCodes.getCollectTypeCodes().keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CollectTypeCode collectTypeCode =  collectTypeCodes.getCollectTypeCode(key);
			String collectTypeCodeStr = collectTypeCode.getCollectTypeCode();
			String collectTypeNameStr = collectTypeCode.getCollectTypeName();
%>
										<option value="<%= collectTypeCodeStr %>"><%= collectTypeNameStr %></option>
<%
		}
%>
									</select>
								</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 40px ; "><span style="font-size: 14px ; ">기간: </span></td>
								<td style="width: 110px ; ">
									<input style="width: 100px ; text-align: center ; " id="txtStartDateManageReadMeterList" type="text" data-mini="true" value="<%=startDate %>" readonly="readonly" onclick="openCapacitorDatePicker('pageManageReadMeterList', 'txtStartDateManageReadMeterList')" />
								</td>
								<td style="width: 10px ; ">
									<span style="font-size: 14px ;"> ~ </span> 
								</td>
								<td style="width: 110px ; ">
									<input style="width: 100px ; text-align: center ; " id="txtEndDateManageReadMeterList" type="text" data-mini="true" value="<%=endDate %>" readonly="readonly" onclick="openCapacitorDatePicker('pageManageReadMeterList', 'txtEndDateManageReadMeterList')" />
								</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td>
									<input type="text" data-mini="true" id="txtKeywordManageReadMeterList" placeholder="검색어를 입력하세요" style="border-style:solid ; border-width:1px ; border-color:#999999 ; font-size: 14px ; width: 100% ; ">
								</td>
								<td style="width: 100px ; text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-theme="b" onclick="searchManageReadMeterList()" data-icon="search">조회</a>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 83px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">검침일</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">상호</span></td>
								<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">사원</span></td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 40px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">당검</span></td>
								<td style="width: 40px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">사용</span></td>
								<td style="width: 80px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">요금</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">기타</span></td>
								<td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">연체</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">당월금액</span></td>
							</tr>
						</table>
<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>