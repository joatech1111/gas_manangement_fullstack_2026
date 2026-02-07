<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
		<link rel="stylesheet" href="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.css" />
		<script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
		<script src="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.js"></script>
		<title>공지사항</title>
		<!--[if IE]>
  		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
	</head>
	<body bgcolor="#C0DFFD">
		<section id="noticeList" data-role="page" data-add-back-btn="true" data-back-btn-text="이전">
			<header data-role="header" data-theme="d">
				<h3>공지사항</h3>
			</header>
			<div data-role="content">
				<table>
					<tr>
						<th width="100%"></th>
					</tr>
					<tr>
						<td>
							<h4>가스경영관리 업그레이드 소식</h4>
							<ul>
								<li >
									<div>[2012-07-10]가스경영관리 앱 2.0 업그레이드 되었습니다.</div>
								</li>
								<li >
									<div>LPG, 고압가스 관리 기능 통합.</div>
								</li>
								<li >
									<div>신제품! 원격감시 앱 출시.</div>
								</li>
							</ul>
						</td>
					</tr>
				</table>
			</div>
			<footer data-role="footer" data-theme="d" data-position="fixed">
				<h3 style="font-size: 12px ;">COPYRIGHT © 2012 WWW.JOAINFO.COM</h3>
				<h3>고객지원센터 <a href="tel:1566-2399" id="lnkPhoneSupport">1566-2399</a></h3>
			</footer>
		</section>
	</body>
</html>
