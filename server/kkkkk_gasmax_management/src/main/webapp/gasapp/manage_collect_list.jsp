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
//		String startDate = StringUtil.addMonth(now, -1); // 한달전
		String startDate = now;
		String endDate = now;
		// 세션에서 사원목록 가져오기
		//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
		String sessionToken = request.getParameter("sessionToken");
		EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
		// 아직 세션에 사원목록이 등록되지 않았다면 세션에 등록하기
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
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
			//boolean changeEmployee=false;
			
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
								<td style="width: 40px ; vertical-align: middle ; "><span style="font-size: 14px ; ">구분: </span></td>
								<td style="text-align: left ; vertical-align: middle ; ">
									<fieldset data-role="controlgroup" data-type="horizontal">
										<input type="radio" id="all" name="radioSearchOptionManageCollectList" value="" checked="checked" />
										<label for="all"><span style="font-size: 14px ; ">&nbsp;전 &nbsp;&nbsp; 체&nbsp;</span></label>
										<input type="radio" id="weight" name="radioSearchOptionManageCollectList" value="일반" />
										<label for="weight"><span style="font-size: 14px ; ">&nbsp;일 &nbsp;&nbsp; 반&nbsp;</span></label>
										<input type="radio" id="volume" name="radioSearchOptionManageCollectList" value="체적" />
										<label for="volume"><span style="font-size: 14px ; ">&nbsp;체 &nbsp;&nbsp; 적&nbsp;</span></label>
									</fieldset>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 40px ; "><span style="font-size: 14px ; ">사원: </span></td>
								<td style="width: 120px ; text-align: left ; ">
<%
	if (changeEmployee) { 
%>
									<select id="selectEmployeeManageCollectList" data-mini="true" style="font-size: 14px ; ">
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
									<input type="hidden" id ="selectEmployeeManageCollectList" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>
								</td>
								<td style="width: 120px ; text-align: left ; ">
									<select name="selectCollectTypeManageCollectList" id="selectCollectTypeManageCollectList"  data-mini="true" data-inset="false" style="font-size: 14px ; ">
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
									<input style="width: 100px ; text-align: center ; " data-mini="true" id="txtStartDateManageCollectList" type="text" value="<%=startDate %>" readonly="readonly" onclick="openCapacitorDatePicker('pageManageCollectList', 'txtStartDateManageCollectList')" />
								</td>
								<td style="width: 10px ; ">
									<span style="font-size: 14px ;"> ~ </span> 
								</td>
								<td style="width: 110px ; ">
									<input style="width: 100px ; text-align: center ; " data-mini="true" id="txtEndDateManageCollectList" type="text" value="<%=endDate %>" readonly="readonly" onclick="openCapacitorDatePicker('pageManageCollectList', 'txtEndDateManageCollectList')" />
								</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td>
									<input type="text" data-mini="true" id="txtKeywordManageCollectList" placeholder="검색어를 입력하세요" style="border-style:solid ; border-width:1px ; border-color:#999999 ; font-size: 14px ; width: 100% ; ">
								</td>
								<td style="width: 100px ; text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-theme="b" onclick="searchManageCollectList()" data-icon="search">조회</a>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 70px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">수금일</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="font-size:14px ; ">상호</span></td>
								<td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">담당</span></td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 120px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ; ">수금액</span></td>
								<td style="width: 80px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; " ><span style="color:#222222 ; font-size:14px ; ">D/C</span></td>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; "><span style="color:#222222 ; font-size:14px ;">비고</span></td>
							</tr>
						</table>
<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>