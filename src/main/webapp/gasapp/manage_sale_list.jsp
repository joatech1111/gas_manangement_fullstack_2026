<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
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
    if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
    request.setCharacterEncoding("UTF-8");
%><%
    try {
        // 단말기의 오늘 날짜를 이용하기
        String now = request.getParameter("now");
        String sessionToken = request.getParameter("sessionToken");
//		String startDate = StringUtil.addMonth(now, -1); //한달전
        String startDate = now;
        String endDate = now;
        //todo: 세션에서 사원목록 가져오기
        //	EmployeeCodeMap employeeCodes = (EmployeeCodeMap)session.getAttribute("EMPLOYEE_CODE");

        EmployeeCodeMap employeeCodes = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class);
        // 아직 세션에 사원목록이 등록되지 않았다면 세션에 등록하기
//		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");
//		appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("uuid"));
        AppUser appUser = RedisUtil.getUserFromSessionToken(sessionToken);

        if (appUser != null) {
            String serverIp = appUser.getIpAddress();
            String catalogName = appUser.getDbCatalogName();
            String areaCode = appUser.getAreaCode();
            String userEmployeeCode = appUser.getEmployeeCode();
            String userEmployeeName = appUser.getEmployeeName();
            String menuPermission = appUser.getMenuPermission();
            String[] permission = menuPermission.split(""); //1부터 시작함 0은 공백이 들어감.
            boolean changeEmployee = "0".equals(permission[2])?true:false;

            if (employeeCodes == null) {
                String keyword = "";
                EmployeeCodeMap newEmployeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
                employeeCodes = newEmployeeCodes;
                //session.setAttribute("EMPLOYEE_CODE", newEmployeeCodes);
                try{
                    RedisUtil.saveToRedis(sessionToken, "EMPLOYEE_CODE", newEmployeeCodes);
                }catch(Exception e){
                    e.printStackTrace();
                }

                System.out.println(newEmployeeCodes);
                System.out.println(newEmployeeCodes);
                System.out.println(newEmployeeCodes);
                System.out.println(newEmployeeCodes);
            }
%>
<table style="border: 0px solid #999999 ; border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ;">
    <tr>
        <td style="width: 40px ; vertical-align: middle ; "><span style="font-size: 14px ; ">구분: </span></td>
        <td style="text-align: left ; vertical-align: middle ; ">
            <fieldset data-role="controlgroup" data-type="horizontal">
                <input type="radio" id="all" name="radioSearchOptionManageSaleList" value="" checked="checked"/>
                <label for="all"><span style="font-size: 14px ; ">&nbsp;전 &nbsp;&nbsp; 체&nbsp;</span></label>
                <input type="radio" id="weight" name="radioSearchOptionManageSaleList" value="일반"/>
                <label for="weight"><span style="font-size: 14px ; ">&nbsp;일 &nbsp;&nbsp; 반&nbsp;</span></label>
                <input type="radio" id="volume" name="radioSearchOptionManageSaleList" value="체적"/>
                <label for="volume"><span style="font-size: 14px ; ">&nbsp;체 &nbsp;&nbsp; 적&nbsp;</span></label>
            </fieldset>
        </td>
    </tr>
</table>
<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;">
    <tr>
        <td style="width: 40px ; "><span style="font-size: 14px ; ">사원: </span></td>
        <td style="width: 120px ; ">
            <%
                if (changeEmployee) {
            %>
            <select id="selectEmployeeManageSaleList" data-mini="true" style="font-size: 14px ; ">
                <option value="" data-placeholder="true">전체사원</option>
                <%
                    // 사원 목록을 선택 박스에 채우기
                    java.util.Iterator<String> iterator = employeeCodes.getEmployeeCodes().keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        EmployeeCode employeeCode = employeeCodes.getEmployeeCode(key);
                        String employeeCodeStr = employeeCode.getEmployeeCode();
                        String employeeNameStr = employeeCode.getEmployeeName();
                        String selectedStr = userEmployeeCode.equals(employeeCodeStr) ? "selected" : "";
                %>
                <option value="<%= employeeCodeStr %>" <%=selectedStr %>><%= employeeNameStr %>
                </option>
                <%
                    }
                %>
                <option value="noEmployee">미지정</option>
            </select>
            <%
            } else {
            %>
            <input type="hidden" id="selectEmployeeManageSaleList" value="<%=userEmployeeCode %>"/>
            <%= userEmployeeName %>
            <%
                }
            %>
        </td>
        <td style="width: 120px ; ">
            <select name="selectPayTypeManageSaleList" id="selectPayTypeManageSaleList" data-mini="true"
                    data-inset="false" style="font-size: 14px ; ">
                <option value="" data-placeholder="true">입금방법</option>
                <option value="0">현금</option>
                <option value="2">예금</option>
                <option value="3">카드</option>
                <option value="4">어음</option>
                <option value="A">외상</option>
                <option value="B">현금(영수증)</option>
            </select>
        </td>
        <td>&nbsp;</td>
    </tr>
</table>
<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
    <tr>
        <td style="width: 40px ; "><span style="font-size: 14px ; ">기간: </span></td>
        <td style="width: 110px ; ">
            <input style="width: 100px ; text-align: center ; " id="txtStartDateManageSaleList" type="text"
                   data-mini="true" value="<%=startDate %>" readonly="readonly"
                   onclick="openCapacitorDatePicker('pageManageSaleList', 'txtStartDateManageSaleList')"/>
        </td>
        <td style="width: 20px ; ">
            <span style="font-size: 14px ;"> ~ </span>
        </td>
        <td style="width: 110px ; ">
            <input style="width: 100px ; text-align: center ; " id="txtEndDateManageSaleList" type="text"
                   data-mini="true" value="<%=endDate %>" readonly="readonly"
                   onclick="openCapacitorDatePicker('pageManageSaleList', 'txtEndDateManageSaleList')"/>
        </td>
        <td>&nbsp;</td>
    </tr>
</table>
<table style="border: 0px solid #999999 ; border-bottom: 0px solid #999999 ; border-collapse: collapse ; width: 100% ; ">
    <tr>
        <td style="text-align: left ; ">
            <input type="text" data-mini="true" id="txtKeywordManageSaleList" placeholder="검색어를 입력하세요"
                   style="border-style:solid ; border-width:1px ; border-color:#999999 ; font-size: 14px ; width: 100%">
        </td>
        <td style="width: 100px ; text-align: center ; ">
            <a href="#" data-role="button" data-mini="true" data-theme="b" onclick="searchManageSaleList()"
               data-icon="search">조회</a>
        </td>
    </tr>
</table>
<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ;  border-top: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
    <tr>
        <td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="font-size:14px ; ">판매일</span></td>
        <td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="font-size:14px ; ">상호/비고</span></td>
        <td style="width: 60px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="color:#222222 ; font-size:14px ;">사원</span></td>
    </tr>
</table>
<table style="border: 0px solid #999999 ; border-bottom: 1px solid #999999 ; border-collapse: collapse ; width: 100% ; table-layout: fixed ; ">
    <tr>
        <td style="text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="color:#222222 ; font-size:14px ; ">품명</span></td>
        <td style="width: 50px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="color:#222222 ; font-size:14px ; ">出/入</span></td>
        <td style="width: 120px ; text-align: center ; border-left: 1px solid #999999 ; border-right: 1px solid #999999 ; border-bottom: 1px solid #999999 ; ">
            <span style="color:#222222 ; font-size:14px ;">금액</span></td>
    </tr>
</table>

<%
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
