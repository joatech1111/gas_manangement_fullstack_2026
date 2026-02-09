<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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

		String uuid= request.getParameter("uuid");

		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		// 아직 세션에 사원목록이 등록되지 않았다면 세션에 등록하기
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		// 세션에서 사원목록 가져오기
//		EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
		String sessionToken = request.getParameter("sessionToken");
		EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
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

			CollectTypeCodeMap collectTypeCodes = (CollectTypeCodeMap)session.getAttribute("COLLECT_UNPAID_TYPE_CODE");
			if(collectTypeCodes==null){
				CollectTypeCodeMap newCollectTypeCodes = BizCollectTypeCode.getInstance().getCollectUnpaidTypeCodes(serverIp, catalogName);
				collectTypeCodes = newCollectTypeCodes;
				session.setAttribute("COLLECT_UNPAID_TYPE_CODE", newCollectTypeCodes);
			}
%>
						<table style="border: 0px solid #999999 ; border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 40px ; vertical-align: middle ; "><span style="font-size: 14px ; ">구분: </span></td>
								<td style="text-align: left ; vertical-align: middle ; ">
									<fieldset data-role="controlgroup" data-type="horizontal">
										<input type="radio" id="all" name="radioSearchOptionManageUnpaidList" value="0" checked="checked" />
										<label for="all"><span style="font-size: 14px ; ">&nbsp;전 &nbsp;&nbsp; 체&nbsp;</span></label>
										<input type="radio" id="weight" name="radioSearchOptionManageUnpaidList" value="1" />
										<label for="weight"><span style="font-size: 14px ; ">&nbsp;일 &nbsp;&nbsp; 반&nbsp;</span></label>
										<input type="radio" id="volume" name="radioSearchOptionManageUnpaidList" value="2" />
										<label for="volume"><span style="font-size: 14px ; ">&nbsp;체 &nbsp;&nbsp; 적&nbsp;</span></label>
									</fieldset>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td style="width: 40px ; "><span style="font-size: 14px ; ">사원: </span></td>
								<td style="width: 120px ; ">
<%
	if (changeEmployee) { 
%>
									<select id="selectEmployeeManageUnpaid"  data-mini="true" style="font-size: 14px ; ">
										<option value="" data-placeholder="true">전체사원</option>
<%
		// 사원 목록을 선택 박스에 채우기
		java.util.Iterator<String> iterator = employeeCodes.getEmployeeCodes().keySet().iterator(); 
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			EmployeeCode employeeCode =  employeeCodes.getEmployeeCode(key);
			String employeeCodeStr = employeeCode.getEmployeeCode();
			String employeeNameStr = employeeCode.getEmployeeName();
			String selectedStr = userEmployeeCode.equals(employeeCodeStr)?"selected":"";
%>
										<option value="<%= employeeCodeStr %>" <%=selectedStr %>><%= employeeNameStr %></option>
<%
		}
%>
										<option value="noEmployee">미지정</option>
									</select>
<%
	} else {
%>
									<input type="hidden" id ="selectEmployeeManageUnpaid" value="<%=userEmployeeCode %>" />
							<%= userEmployeeName %>
<%
	}
%>		
								</td>
								<td style="width: 120px ; ">
									<select name="selectCollectTypeManageUnpaid" id="selectCollectTypeManageUnpaid"  data-mini="true" data-inset="false" style="font-size: 14px ; ">
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
						<table style="border: 0px solid #999999 ; border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
							<tr>
								<td>
									<input type="text" data-mini="true" id="txtKeywordManageUnpaidList" placeholder="검색어를 입력하세요" style=" font-size: 14px ; width: 100% ; ">
								</td>
								<td style="width: 10px ; ">&nbsp;</td>
								<td style="width: 80px ; text-align: center ; ">
									<a href="#" data-role="button" data-mini="true" data-theme="b" data-inline="true" onclick="searchManageUnpaidList()" data-icon="search">검색</a>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 0px solid #999999 ;  border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; "><span style="font-size:14px ; " >상호 / 주소</span></td>
								<td style="width: 90px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 0px solid #999999 ; "><span style="color: #222222 ; font-size:14px ; " >미수/사원</span></td>
							</tr>
						</table>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>