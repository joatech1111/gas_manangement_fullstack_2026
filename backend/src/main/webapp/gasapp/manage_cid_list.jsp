<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
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
		// 단말기의 오늘 날짜를 이용하기
		String now = request.getParameter("now");


		String uuid = request.getParameter("uuid");

		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		String sessionToken = request.getParameter("sessionToken");

		AppUser appUser = RedisUtil.getUserFromSessionToken(sessionToken);

		// 세션에서 사원목록 가져오기
		//EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
		EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );




		if (appUser != null){
			String keyword = "";
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();
			String areaAddress = appUser.getAreaAddress();
			String phoneAreaNumber = appUser.getPhoneAreaNumber();
			String userEmployeeCode = appUser.getEmployeeCode();
			String userEmployeeName = appUser.getEmployeeName();
			String menuPermission = appUser.getMenuPermission();
			String [] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
			boolean changeEmployee = "0".equals(permission[2])?true:false;

			if (userEmployeeCode == null){
				userEmployeeCode = "";
			}

			if(employeeCodes==null){
				EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				employeeCodes = newEmployeeCodes;
				session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
				RedisUtil.saveToRedis(sessionToken, "EMPLOYEE_CODE", newEmployeeCodes);

				EmployeeCodeMap employeeCodes2 = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class );
				System.out.println(employeeCodes2);
				System.out.println(employeeCodes2);
				System.out.println(employeeCodes2);
				System.out.println(employeeCodes2);
			}
%>
<input type="hidden" id="hdnAreaAddressManageCidList" value="<%=areaAddress %>" />
<input type="hidden" id="hdnPhoneAreaNumberManageCidList" value="<%=phoneAreaNumber %>" />
						<table style="border: 0px solid #999999 ; border-top: 1px solid #999999 ; border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;">
							<tr>
								<td style="width: 110px ; font-size: 14px ;" >
<%--									<input style="width: 90% ; text-align: center ; " data-mini="true" id="txtCidDateManageCidList" type="text" value="<%=now %>" readonly="readonly" onclick="showDialogDatePicker('pageManageCidList', 'txtCidDateManageCidList')" />--%>

									<input style="width: 90% ; text-align: center ; " data-mini="true" id="txtCidDateManageCidList" type="text" value="<%=now %>" readonly="readonly" onclick="openCapacitorDatePicker('pageManageCidList', 'txtCidDateManageCidList')" />
								</td>
								<td style="width: 180px ; text-align: center ; ">
									<a href="#" type="button" data-mini="true" data-theme="b" onclick="searchManageCidList('<%=areaAddress %>', '<%=phoneAreaNumber %>')" data-icon="search" data-inline="true" >조회</a>
									<a href="#" type="button" data-mini="true" data-theme="b" onclick="clickInsertCidManageCidList()" data-icon="plus" data-inline="true" >신규</a>
								</td>
								<td>&nbsp;
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-top: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style="width: 250px ; ">
									<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
										<tr>
											<td>
												<fieldset data-role="controlgroup" data-mini="true">
													<input type="checkbox" id="ckbNewDeliveryManageCidList" checked="checked" />
													<label for="ckbNewDeliveryManageCidList"><span style="font-size: 14px ; ">신규</span></label>
												</fieldset>
											</td>
											<td>
												<fieldset data-role="controlgroup" data-mini="true">
													<input type="checkbox" id="ckbDeliveredManageCidList" checked="checked" />
													<label for="ckbDeliveredManageCidList"><span style="font-size: 14px ; ">배달</span></label>
												</fieldset>
											</td>
											<td>
												<fieldset data-role="controlgroup" data-mini="true">
													<input type="checkbox" id="ckbDeliveryCompleteManageCidList" checked="checked" />
													<label for="ckbDeliveryCompleteManageCidList"><span style="font-size: 14px ; ">완료</span></label>
												</fieldset>
											</td>
										</tr>
									</table>
								</td>
								<td style="text-align: center ; ">
<%
	if (changeEmployee) {
%>
											<select id="selectEmployeeManageCidList"  data-native-menu="true" data-mini="true" data-inset="false" style="font-size: 14px ; ">
												<option value="" data-placeholder="true">전체사원</option>
												<option value="NA" >미지정</option>
		<%
						// 사원 목록을 선택 박스에 채우기
						java.util.Iterator<String> iterator = employeeCodes.getEmployeeCodes().keySet().iterator();
						while (iterator.hasNext()) {
							String key = iterator.next();
							EmployeeCode employeeCode =  employeeCodes.getEmployeeCode(key);
							String employeeCodeStr = employeeCode.getEmployeeCode();
							String employeeNameStr = employeeCode.getEmployeeName();
		%>
												<option value="<%= employeeCodeStr%>" <%=userEmployeeCode.equals(employeeCodeStr)?"selected":"" %>><%= employeeNameStr %></option>
		<%
						}
		%>
											</select>
<%
	} else {
%>
									<input type="hidden" id ="selectEmployeeManageCidList" value="<%=userEmployeeCode %>" />
							사원: <%= userEmployeeName %>
<%
	}
%>
								</td>
							</tr>
						</table>
						<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
							<tr>
								<td style='text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; '><span style='font-size:14px ; '>거래처/주소/메모</span></td>
								<td style='width: 100px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; '><span style='color:#222222 ; font-size:14px ;'>배달/완료</span></td>
							</tr>
						</table>

<%
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>
