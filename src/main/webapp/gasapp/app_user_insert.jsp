<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%>
		<ul data-role="listview" data-inset="true" data-mini="true">
			<li>
				<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<td style="width: 100px ; ">업체명:</td>
						<td><input type="text"  data-mini="true" id="txtAreaNameAppUserInsert" value="" placeholder="업체명 입력"  maxlength="15" size="30" onfocus="disableFixed('footerJoin')" onblur="enableFixed('footerJoin')" /></td>
					</tr>
				</table>
			</li>
			<li>
				<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<td style="width: 100px ; ">성명:</td>
						<td><input type="text"  data-mini="true" id="txtEmployeeNameAppUserInsert" value="" placeholder="가입신청자 이름 입력"  maxlength="4" size="30" onfocus="disableFixed('footerJoin')" onblur="enableFixed('footerJoin')" /></td>
					</tr>
				</table>
			</li>
			<li>
				<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<td style="width: 100px ; ">사용자명:</td>
						<td><input type="text"  data-mini="true" id="txtUserIdAppUserInsert" value="" placeholder="아이디 입력"  maxlength="15" size="30" onfocus="disableFixed('footerJoin')" onblur="enableFixed('footerJoin')" /></td>
					</tr>
				</table>
			</li>
			<li>
				<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<td style="width: 100px ; ">비밀번호:</td>
						<td><input type="password" data-mini="true" id="txtPasswordAppUserInsert" autocomplete="off" placeholder="비밀번호 입력" maxlength="15" size="30" onfocus="disableFixed('footerJoin')" onblur="enableFixed('footerJoin')" /></td>
					</tr>
				</table>
			</li>
			<li>
				<table style="border: 0px solid blue ; border-collapse: collapse ; width: 100% ;">
					<tr>
						<td style="width: 100px ; ">비번확인:</td>
						<td><input type="password" data-mini="true" id="txtPasswordConfirmAppUserInsert" autocomplete="off" placeholder="비밀번호 재입력" maxlength="15" size="30" onfocus="disableFixed('footerJoin')" onblur="enableFixed('footerJoin')" /></td>
					</tr>
				</table>
			</li>
		</ul>
				
       	<table style="border-collapse: collapse ; width: 100% ;">
       		<tr>
       			<td style="color: #3333FF  ; font-size:12px ;" align="center">
       				<input type="button" id="btnSaveAppUserInsert" data-theme="b" data-mini="true" value="회원가입신청" data-icon="check" onclick="clickSaveAppUserInsert()"></input>
       			</td>
       		</tr>
       	</table>
		<div id="divSaveMessageAppUserInsert"></div>
