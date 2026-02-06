<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (request.getProtocol().equals("HTTP/1.1"))
        response.setHeader("Cache-Control", "no-cache");
    request.setCharacterEncoding("UTF-8");
%><%
    try {
        // [2017.11.01][Rev3] 인증번호 표시 대응
        String macNumber = request.getParameter("macNumber");
        String uuid = request.getParameter("uuid");
        String phoneNumber = request.getParameter("phoneNumber");

        System.out.println(uuid);
        System.out.println(uuid);
        System.out.println(uuid);

%>
<%
    String userAgent = request.getHeader("User-Agent");
    boolean isIOS = (userAgent != null && (userAgent.contains("iPhone") || userAgent.contains("iPad") || userAgent.contains("iPod")));
%>
<ul data-role="listview" data-inset="true" data-mini="true">
    <%
        if (macNumber != null && !macNumber.isEmpty()) {
    %>
    <li>
        <table style="border: 0px solid blue; border-collapse: collapse; width: 100%;">
            <tr>
                <td style="color: black !important; width: 100px;">
                    인증번호:
                </td>
                <td>
                    <input type="text" id="txtAuthCode" value="<%=uuid.toLowerCase()%>" readonly


                           onfocus="this.select();"
                           style="width: 100%; color: #0e1669;font-weight:bold; text-align: left; text-transform: lowercase !important; border: none; background: transparent;"/>
                </td>
            </tr>
        </table>
    </li>
    <%
        }
    %>
    <li>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">업체명:</td>
                <td><input type="text" data-mini="true" id="txtAreaNameAppUserInsert" value="" placeholder="업체명 입력"
                           maxlength="15" size="30" onfocus="disableFixed('footerJoin')"
                           onblur="enableFixed('footerJoin')"/></td>
            </tr>
        </table>
    </li>
    <li>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">성명:</td>
                <td><input type="text" data-mini="true" id="txtEmployeeNameAppUserInsert" value=""
                           placeholder="가입신청자 이름 입력" maxlength="4" size="30" onfocus="disableFixed('footerJoin')"
                           onblur="enableFixed('footerJoin')"/></td>
            </tr>
        </table>
    </li>
    <li>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">사용자명:</td>
                <td><input type="text" data-mini="true" id="txtUserIdAppUserInsert" value="" placeholder="아이디 입력"
                           maxlength="15" size="30" onfocus="disableFixed('footerJoin')"
                           onblur="enableFixed('footerJoin')"/></td>
            </tr>
        </table>
    </li>
    <li>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">비밀번호:</td>
                <td><input type="password" data-mini="true" id="txtPasswordAppUserInsert" autocomplete="off"
                           placeholder="비밀번호 입력" maxlength="15" size="30" onfocus="disableFixed('footerJoin')"
                           onblur="enableFixed('footerJoin')"/></td>
            </tr>
        </table>
    </li>
    <li>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">비번확인:</td>
                <td><input type="password" data-mini="true" id="txtPasswordConfirmAppUserInsert" autocomplete="off"
                           placeholder="비밀번호 재입력" maxlength="15" size="30" onfocus="disableFixed('footerJoin')"
                           onblur="enableFixed('footerJoin')"/></td>
            </tr>
        </table>
    </li>
    <li <%= isIOS ? "style='display:none;'" : "" %>>
        <table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
            <tr>
                <td style="width: 100px ; ">전화번호:</td>
                <td><input value="<%=phoneNumber%>" readonly disabled/></td>
            </tr>
        </table>
    </li>
</ul>

<table style="border-collapse: collapse ; width: 100% ;">
    <tr>
        <td style="color: #3333FF  ; font-size:12px ;" align="center">
            <input type="button" id="btnSaveAppUserInsert" data-theme="b" data-mini="true" value="회원가입신청"
                   data-icon="check" onclick="clickSaveAppUserInsert()"></input>
        </td>
    </tr>
</table>
<div id="divSaveMessageAppUserInsert"></div>
<%
    } catch (Exception e) {
        e.printStackTrace();
    }
%>