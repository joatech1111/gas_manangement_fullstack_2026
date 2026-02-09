<%@ page import="com.google.gson.Gson" %>
<%@ page import="redis.clients.jedis.Jedis" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.common.util.RedisUtil" %>
<%@ page import="com.joainfo.gasmax.bean.list.EmployeeCodeMap" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.joainfo.gasmax.bean.EmployeeCode" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.joainfo.gasmax.biz.BizAppUser" %>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
    request.setCharacterEncoding("UTF-8");

    // 요청에서 `sessionToken` 가져오기
    String sessionToken = request.getParameter("sessionToken");
    //AppUser appUser = RedisUtil.getUserFromSessionToken(sessionToken);

    String uuid = StringUtil.nullToBlank(request.getParameter("uuid"));
    String hpSeq = request.getParameter("hpSeq");
    AppUser appUser = null;
    
    try {
        if (uuid != null && !uuid.isEmpty()) {
            appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, uuid.toLowerCase(), hpSeq);
        }
    } catch (Exception e) {
        System.out.println("❌ Error getting appUser: " + e.getMessage());
        e.printStackTrace();
    }

    try {
        if (sessionToken != null && !sessionToken.isEmpty()) {
            EmployeeCodeMap employeeCodeMap = RedisUtil.getFromRedis(sessionToken, "EMPLOYEE_CODE", EmployeeCodeMap.class);
            if (employeeCodeMap != null) {
                LinkedHashMap<String, EmployeeCode> employeeCodes = employeeCodeMap.getEmployeeCodes();
                if (employeeCodes != null) {
                    System.out.println("Employee codes: " + employeeCodes);
                    EmployeeCode code = employeeCodes.get("01");
                    if (code != null) {
                        System.out.println("Employee name: " + code.getEmployeeName());
                    }
                }
            }
        }
    } catch (Exception e) {
        System.out.println("❌ Error getting employee codes: " + e.getMessage());
    }

    if (appUser == null) {
        // 사용자 정보가 없어도 빈 메뉴를 표시 (에러 대신)
        System.out.println("⚠️ Warning: appUser is null for uuid: " + uuid);
    } else {
        // ✅ 디버깅 로그 출력
        System.out.println("✅ sessionToken: " + sessionToken);
        System.out.println("✅ 사용자 정보: " + appUser);
    }

%>
<div>
</div>

<table style="border: 0px solid #999999 ; border-collapse: collapse ; width: 100% ;">
<%--    <tr>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a href="#" onclick="showPageManageCidList()"><img--%>
<%--                src="images/v2/main_cid_btn.png" widht="107" height="94"/></a></td>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a href="#" onclick="showPageManageSaleList()"><img--%>
<%--                src="images/v2/main_custcenter_btn.png" widht="107" height="94"/></a></td>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a href="#" onclick="showPageManageUnpaidList()"><img--%>
<%--                src="images/v2/main_uncollected_btn.png" widht="107" height="94"/></a></td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <td>&nbsp;</td>--%>
<%--        <td>&nbsp;</td>--%>
<%--        <td>&nbsp;</td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a href="#" onclick="showPageManageCollectList()"><img--%>
<%--                src="images/v2/main_collection_btn.png" widht="107" height="94"/></a></td>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a href="#" onclick="showPageManageReadMeterList()"><img--%>
<%--                src="images/v2/main_register_btn.png" widht="107" height="94"/></a></td>--%>
<%--        <td style="color:#3333FF ; text-align: center ; "><a onclick="openJoaOffice()" target="_self"><img--%>
<%--                src="images/v2/main_joa_portal2_btn.png" widht="107" height="94"></a></td>--%>
<%--    </tr>--%>

<%--    <tr>--%>
<%--        <td>&nbsp;</td>--%>
<%--        <td>&nbsp;</td>--%>
<%--        <td>&nbsp;</td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <td>&nbsp;</td>--%>

<%--        <td style="color:#bbbbc4; text-align: center;">--%>
<%--            <a href="#" onclick="openJoaTech()">--%>
<%--                <img src="images/v2/joatech3.png" width="87" height="80"--%>
<%--                     style="border: 1px solid #b7b7c5; border-radius: 15px;"/>--%>
<%--            </a>--%>
<%--        </td>--%>
<%--        <td>&nbsp;</td>--%>
<%--    </tr>--%>
<%--    <tr>--%>
<%--        <!----%>
<%--                        <td style="color:#3333FF ; text-align: center ; "><a href="https://gasmax2020.page.link/c8Ci" target="_blank">탱크잔량2020 앱실행</a></td>--%>

<%--                        <td style="color:#3333FF ; text-align: center ; "><a href="https://gasmax2020.page.link/u9DC" target="_blank">가스경영관리 앱실행</a></td>--%>
<%--                        <td style="color:#3333FF ; text-align: center ; "><a href="#" name="btnInApp"><img src="images/main_register_btn.png"/></a></td>--%>
<%--        -->--%>
<%--    </tr>--%>

</table>