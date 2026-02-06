<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizEmployeeCode" %>
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
        AppUser appUser = (AppUser) session.getAttribute("USER_INFO");
        String darkMode = request.getParameter("darkMode");
        String uuid = request.getParameter("uuid");
        String sessionToken= request.getParameter("sessionToken");
        System.out.println(uuid);
        System.out.println(darkMode);
        //appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
        try{
            appUser = RedisUtil.getUserFromSessionToken(sessionToken);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }

        if (appUser != null) {
            String serverIp = appUser.getIpAddress();
            String catalogName = appUser.getDbCatalogName();
            String areaCode = appUser.getAreaCode();
            String areaName = appUser.getAreaName();
            String userEmployeeCode = appUser.getEmployeeCode();
            String userEmployeeName = appUser.getEmployeeName();
            String phoneAreaNumber = appUser.getPhoneAreaNumber();
            if (phoneAreaNumber == null) {
                phoneAreaNumber = "02";
            }
            String address = appUser.getAreaAddress();
            String userId = appUser.getUserId();
            String password = appUser.getPassword();
            String gasType = appUser.getGasType();
            String menuPermission = appUser.getMenuPermission();
            String[] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
            boolean changeArea = "0".equals(permission[1]) ? true : false;
            boolean changeEmployee = "0".equals(permission[2]) ? true : false;
            //boolean changeEmployee= false;
            // 영업소 목록
            LinkedHashMap<String, String> areaCodes = BizAppUser.getInstance().getAreaCodes(serverIp, catalogName);

            // 전화 지역번호 목록
            LinkedHashMap<String, String> phoneAreaNumbers = BizAppUser.getInstance().getPhoneAreaNumbers(serverIp, catalogName);

            // 세션에서 사원목록 가져오기
//			EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");
//			if(employeeCodes==null){
            EmployeeCodeMap employeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
//				employeeCodes = newEmployeeCodes;
//				session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
//			}
            java.util.Iterator<String> iterator;
%>
<input type="hidden" id="userEmployeeCodeAppUserEdit" value="<%=userEmployeeCode %>"/>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr>
        <td style="font-size:14px ; text-align: left ; font-weight: bold ; border-bottom: 0px solid #999999 ; background-color: #DDDDDD">
            &nbsp;환경설정
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">영업소명:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
            <input type="hidden" id="hdnAreaNameAppUserEdit" value="<%=areaName%>"/>
            <%
                if (changeArea) {
            %>
            <select id="selectAreaCodeAppUserEdit" data-mini="true" style="font-size: 14px ; "
                    onchange="changeAreaCodeAppUserEdit()">
                <%
                    // 영업소 목록을 채우기
                    int i = 0;
                    iterator = areaCodes.keySet().iterator();
                    while (iterator.hasNext()) {
                        String areaCodeStr = iterator.next();
                        String areaNameStr = areaCodes.get(areaCodeStr);
                        String selected = "";
                        if (areaCode.equals(areaCodeStr)) {
                            selected = "selected";
                        }
                %>
                <option value="<%= areaCodeStr %>" value2="<%= areaNameStr %>" <%=selected%> > [ <%= areaCodeStr %>
                    ] <%= areaNameStr %>
                </option>
                <%
                    }
                %>
            </select>
            <%
            } else {
            %>
            <input type="hidden" id="selectAreaCodeAppUserEdit" data-mini="true" value="<%= areaCode %>"/>
            <input type="text" id="txtAreaNameAppUserEdit" data-mini="true" value="<%= areaName %>" readonly
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; "/>
            <%
                }
            %>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">담당자명:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
            <input type="hidden" id="hdnEmployeeNameAppUserEdit" value="<%=userEmployeeName%>"/>
            <%
                if (changeEmployee) {
            %>
            <select id="selectEmployeeCodeAppUserEdit" data-mini="true" style="font-size: 14px ; "
                    onchange="changeEmployeeCodeAppUserEdit()">
                <%
                    // 사원 목록을 선택 박스에 채우기
                    iterator = employeeCodes.getEmployeeCodes().keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        EmployeeCode employeeCode = employeeCodes.getEmployeeCode(key);
                        String employeeCodeStr = employeeCode.getEmployeeCode();
                        String employeeNameStr = employeeCode.getEmployeeName();
                        String selected = "";
                        if (userEmployeeCode.equals(employeeCodeStr)) {
                            selected = "selected";
                        }
                %>
                <option value="<%= employeeCodeStr %>" value2="<%= employeeNameStr %>" <%=selected%> >
                    [ <%= employeeCodeStr %> ] <%= employeeNameStr %>
                </option>
                <%
                    }
                %>
            </select>
            <%
            } else {
            %>
            <input type="hidden" id="selectEmployeeCodeAppUserEdit" data-mini="true" value="<%= userEmployeeCode %>"/>
            <input type="text" id="txtEmployeeNameAppUserEdit" data-mini="true" value="<%= userEmployeeName %>" readonly
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; "/>
            <%
                }
            %>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">지역번호:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
            <select id="selectPhoneAreaNumberAppUserEdit" data-mini="true" style="font-size: 14px ; ">
                <%
                    // 전화 지역번호 목록을 선택 박스에 채우기
                    iterator = phoneAreaNumbers.keySet().iterator();
                    while (iterator.hasNext()) {
                        String phoneAreaNumberStr = iterator.next();
                        String phoneAreaNameStr = phoneAreaNumbers.get(phoneAreaNumberStr);
                        String selected = "";
                        if (phoneAreaNumber.equals(phoneAreaNumberStr)) {
                            selected = "selected";
                        }
                %>
                <option value="<%= phoneAreaNumberStr %>" <%=selected%> ><%= "[ " + phoneAreaNumberStr + " ] " + phoneAreaNameStr %>
                </option>
                <%
                    }
                %>
            </select>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #666666 ; ">관할주소:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #666666 ;">
            <input type="text" id="txtAddressAppUserEdit" data-mini="true" value="<%=address %>"
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; " maxlength="20"
                   onfocus="disableFixed('footerAppUserEdit')" onblur="enableFixed('footerAppUserEdit')"/>
        </td>
    </tr>
</table>
<br/>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr>
        <td style="font-size:14px ; text-align: left ; font-weight: bold ; border-bottom: 0px solid #999999 ; background-color: #DDDDDD">
            &nbsp;사용자 정보
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">사용자명:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
            <input type="text" id="txtUserIdAppUserEdit" data-mini="true" value="<%=userId %>"
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; " maxlength="15"
                   onfocus="disableFixed('footerAppUserEdit')" onblur="enableFixed('footerAppUserEdit')"/>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #666666 ; ">비밀번호:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #666666 ;">
            <input type="password" id="txtPasswordAppUserEdit" data-mini="true" value="<%=password %>"
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; " maxlength="15"
                   onfocus="disableFixed('footerAppUserEdit')" onblur="enableFixed('footerAppUserEdit')"/>
        </td>
    </tr>
</table>
<table id="tableDevEnvAppUserEdit"
       style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse; display: none ; ">
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #666666 ; ">메뉴권한:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #666666 ;">
            <input type="text" id="txtMenuPermissionAppUserEdit" data-mini="true" value="<%= menuPermission %>"
                   style="width: 80% ; font-size: 14px ; text-align: left ; color: blue ; " maxlength="20"
                   onfocus="disableFixed('footerAppUserEdit')" onblur="enableFixed('footerAppUserEdit')"/>
        </td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #666666 ; ">프로그램:</td>
        <td style="font-size:14px ; border-bottom: 0px solid #666666 ;">
            <select id="selectGasTypeAppUserEdit" data-mini="true" style="font-size: 14px ; ">
                <option value="HIGH" <%="HIGH".equals(gasType) ? "selected" : "" %> >고압</option>
                <option value="LPG" <%="LPG".equals(gasType) ? "selected" : "" %> >LPG</option>
            </select>
        </td>
    </tr>
</table>
<br/>
<!--todo: ######################### -->
<!--todo: 테마 -->
<!--todo: ######################### -->
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr>
        <td style="font-size:14px ; text-align: left ; font-weight: bold ; border-bottom: 0px solid #999999 ; background-color: #DDDDDD">
            &nbsp;테마
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #666666 ; border-bottom: 0px solid #666666 ; border-collapse: collapse;">
    <tr style="height: 40px ; ">
        <td style="font-size:14px; border-bottom: 0px solid #999999; margin-left: 10px; margin-right: 10px; display: flex; justify-content: center; align-items: center; height: 60px;">
            <a onclick="toggleDarkMode()" class="ui-btn ui-corner-all ui-shadow" style="width: 95%; height: 35px; display: flex; align-items: center; justify-content: center; font-size: 16px;">라이트/다크 모드 변경</a>
        </td>
    </tr>

</table>

<table style="border-collapse: collapse ; width: 100% ;  border: 0px solid #999999 ; border-top: 1px solid #666666 ; ">
    <tr style="height: 60px ; ">
        <td style="color: #3333FF  ; font-size:14px ;" align="center">
            <a href="#" data-role="button" data-mini="true" data-corners="false" data-inline="true" data-icon="check"
               onclick="clickSaveAppUserEdit()">저장</a>
        </td>
    </tr>
</table>
<div id="divSaveMessageAppUserEdit"></div>
<%
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>