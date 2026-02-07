<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%@
page import="com.joainfo.common.util.StringUtil" %><%@
page import="com.joainfo.gasmaxeye.bean.list.MyCompanyMap"%><%@
page import="com.joainfo.gasmaxeye.biz.BizTankLevelList"%><%@
page import="java.util.HashMap" %><%@
page import="java.util.StringTokenizer" %><%@
page import="sun.misc.BASE64Decoder" %><%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String mapId = "vaGyoW_QqqhxDOJcqWk0";	// Release
	
	String queryString = request.getQueryString();

	String clientNumber = null;
	String sawonCode = null;
	String areaTypeCode = null;
	String keyword = null;
	String grant = null;
	
	MyCompanyMap myCompanys = null;

	try{
		if (queryString != null) {
			while (queryString.length() % 4 != 0) {
				queryString += "=";
			}
			
			BASE64Decoder decoder = new BASE64Decoder();
			String parameters = new String(decoder.decodeBuffer(queryString));
	
			StringTokenizer st = new StringTokenizer(parameters, "&");
			while(st.hasMoreTokens()) {
				String parameter = st.nextToken();
				int eIndex = parameter.indexOf("=");
				if (eIndex < 0) continue;
				
				String paramKey = parameter.substring(0, eIndex);
				String paramValue = parameter.length() > eIndex + 1 ? parameter.substring(eIndex+1) : "";
	
				if ("clientNumber".equals(paramKey)) {
					clientNumber = paramValue;				
				} else if ("sawonCode".equals(paramKey)) {
					sawonCode = paramValue;
				} else if ("areaTypeCode".equals(paramKey)) {
					areaTypeCode = paramValue;	
				} else if ("keyword".equals(paramKey)) {
					keyword = paramValue;
				} else if ("grant".equals(paramKey)) {
					grant = paramValue;
				}
			}
		}
		
		if (clientNumber != null && clientNumber.trim().length() > 0) {
			myCompanys = BizTankLevelList.getInstance().getMyCompany(clientNumber);
		} else {
			out.print("Invalid parameter");
			return;
		}
	} catch (Exception e){
		e.printStackTrace();
		
		out.print("Error");
		return;
	}
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<link rel="shortcut icon" href="./assets/icons/favicon.ico" /> </head>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="./assets/plugins/pace/1.0.0/pace-theme-flash.css" rel="stylesheet" type="text/css"/>
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css" />
<link href="./assets/global/plugins/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="./assets/global/plugins/simple-line-icons/2.3.2/css/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
<link href="./assets/global/plugins/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="./assets/global/plugins/bootstrap-switch/3.3.2/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
<link href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN THEME GLOBAL STYLES -->
<link href="./assets/global/css/components-rounded.min.css" rel="stylesheet" id="style_components" type="text/css" />
<link href="./assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
<!-- END THEME GLOBAL STYLES -->
<!-- BEGIN THEME LAYOUT STYLES -->
<link href="./assets/layouts/layout3/css/layout.css" rel="stylesheet" type="text/css" />
<link href="./assets/layouts/layout3/css/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME LAYOUT STYLES -->
<!-- END HEAD -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js" type="text/javascript"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/jquery-ui.min.js"></script>
<script src="./assets/js/jquery1.7-migration.js" type="text/javascript"></script>
<script src="./assets/global/scripts/smart_popup.js" type="text/javascript"></script>
<script src="./assets/global/plugins/jquery-slimscroll/1.3.8/jquery.slimscroll.min.js" type="text/javascript"></script>

<link href="./assets/css/maps.css" rel="stylesheet" type="text/css" />
<link href="./assets/global/plugins/bootstrap-select/1.11.0/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />

<body class="page-container-bg-solid page-header-menu-fixed" oncontextmenu="return false;">

<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
        <!-- BEGIN CONTENT BODY -->

		<!-- BEGIN PAGE CONTENT BODY -->
		<div class="map-container">
		    <div class="map-controls">

				<div class="page-sidebar full-height">
			        <nav class="navbar full-height" role="navigation">
			            <ul class="nav navbar-nav margin-bottom-35 full-height">
			                <li>
			                	<ul class="sw_select">
				                	<li class="sw_list">
					                    <select name="sw_idx" id="sw_list" class="bs-select form-control" data-live-search="true" data-size="8">
					                    	<option value="">사원 전체</option>
										</select>
									</li>
								</ul>
								<ul class="area_select">
									<li class="area_list">
										<select name="area_idx" id="area_list" class="bs-select form-control" data-live-search="true" data-size="8">
					                    	<option value="">지역 전체</option>
										</select>
									</li>
								</ul>
			                </li>
							<li class="cust_list">
								<%--
			            		<select name="cust_idx" id="cust_list" class="bs-select form-control" data-live-search="true" data-size="8">
			            			<option value="0">거래처 전체</option>
			            		</select>
			            		--%>
					            <div id="imaginary_container"> 
					                <div class="input-group stylish-input-group">
					                    <input type="text" class="form-control" id="search_keyword" placeholder="거래처명" >
					                    <span class="input-group-addon">
					                        <button type="submit" id="search_btn">
					                            <span class="glyphicon glyphicon-search"></span>
					                        </button>  
					                    </span>
					                </div>
					            </div>
			            	</li>
			                <li>
			                	<ul class="level_select">
			                    	<li class="level_select_btn lv0" label="0" id="level_select_all"></li>
			                    	<li class="level_select_btn lv1" label="1"></li>
			                    	<li class="level_select_btn lv2" label="2"></li>
			                    	<li class="level_select_btn lv3" label="3"></li>
			                    	<li class="level_select_btn lv4" label="4"></li>
			                	</ul>
			                </li>
							<li>
			                	<ul class="state_select">
			                		<li class="state_select_btn lv1" label="1"></li>
			                    	<li class="state_select_btn lv2" label="2"></li>
			                    	<li class="state_select_btn lv3" label="3"></li>
			                    	<li class="state_select_btn lv4" label="4"></li>
			                	</ul>
			                </li>
			                <li class="account_total_list">
			                	<div class="total_acounts_txt" style="width:60%;float:left;">
			                		총 <span id="account_counts">0</span> 개의 거래처
			                	</div>
			                	<div class="excel_dn" style="width:40%;float:right;">
			                		<a href="#" id="excel_download_btn">엑셀다운로드 <i class="fa fa-download"></i></a>
			                	</div>
			                </li>
			                <li class="account_lists" id="account-list"></li>
			            </ul>
			        </nav>
			        <form class="form-inline margin-bottom-10" id="fCreate" method="post">
			            <div class="input-group">
			                <input type="hidden" name="current_latitude" id="current_latitude">
			                <input type="hidden" name="current_longitude" id="current_longitude">
			                <input type="hidden" name="indicator_level" id="indicator_level">
			                <input type="hidden" name="indicator_state" id="indicator_state">
			                <input type="hidden" name="qid" id="qid" value="<%=queryString != null ? queryString : ""%>" />
							<input type="hidden" name="sawon_code" id="sawon_code" value="<%=sawonCode != null ? sawonCode : ""%>" />
							<input type="hidden" name="area_type_code" id="area_type_code" value="<%=areaTypeCode != null ? areaTypeCode : ""%>" />
							<input type="hidden" name="keyword" id="keyword" value="<%=keyword != null ? keyword : ""%>" />
							<input type="hidden" name="grant" id="grant" value="<%=grant != null ? grant : ""%>" />
			    		</div>
			        </form>
			    </div>
		    </div>
		    <div class="map-area" id="gmap-container">
		    	<div id="nmap-container" style="width:100%;height:100%;"></div>
		    	<div class="custom_map_control">
			    	<ul class="traffic_select">
	               		<li id="traffic_select_btn"><a href="#">실시간 교통</a></li>
	               	</ul>
               	</div>
		    </div>
		</div>
        <!-- END PAGE CONTENT BODY -->
        <!-- END CONTENT BODY -->

    </div>
    <!-- END CONTENT -->

</div>
<!-- END CONTAINER -->

<!-- BEGIN CORE PLUGINS -->
<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=<%=mapId%>"></script>
<script type="text/javascript">var clientJSON = {<%=myCompanys != null ? myCompanys.toJSON() : ""%>};</script>
<script src="./assets/js/joatech.js" type="text/javascript"></script>

<!-- Frame For Data Processing -->
<iframe name="hiddenFrame" style="display:none;"></iframe>

<script src="./assets/plugins/pace/1.0.0/pace.min.js" type="text/javascript"></script>
<script src="./assets/global/plugins/bootstrap/3.3.7/js/bootstrap.min.js" type="text/javascript"></script>
<script src="./assets/global/plugins/js.cookie-2.1.3.min.js" type="text/javascript"></script>
<script src="./assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>

<script src="./assets/global/plugins/jquery.blockUI-2.70.0.min.js" type="text/javascript"></script>
<script src="./assets/global/plugins/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="./assets/global/plugins/bootstrap-select/1.11.0/js/bootstrap-select.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->

<!-- BEGIN THEME GLOBAL SCRIPTS -->
<script src="./assets/global/scripts/app.min.js" type="text/javascript"></script>
<!-- END THEME GLOBAL SCRIPTS -->

<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="./assets/layouts/layout/scripts/layout.js" type="text/javascript"></script>
<script src="./assets/layouts/layout/scripts/demo.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

</body>
</html>