<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%@
page import="com.joainfo.common.util.StringUtil" %><%@
page import="com.joainfo.gasmaxplus.bean.BulkCar" %><%@
page import="com.joainfo.gasmaxplus.bean.EmployeeCode" %><%@
page import="com.joainfo.gasmaxplus.bean.list.BulkCarMap" %><%@
page import="com.joainfo.gasmaxplus.bean.list.MyCompanyMap"%><%@
page import="com.joainfo.gasmaxplus.bean.list.EmployeeCodeMap" %><%@
page import="com.joainfo.gasmaxplus.biz.BizTankLevelList"%><%@
page import="com.joainfo.gasmaxplus.biz.BizManageBulk" %><%@
page import="java.util.Map" %><%@
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
	String mapId = "wysqq8sw41";	// Release

	String queryString = request.getQueryString();

	String clientNumber = null;
	String sawonCode = null;

	MyCompanyMap myCompanys = null;

	// 벌크로리 차량 목록
	BulkCarMap bulkCars = null;
	// 사원 목록
	EmployeeCodeMap employees = null;

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
				}
			}
		}

		if (clientNumber != null && clientNumber.trim().length() > 0) {
			// 벌크로리 차량 목록
			bulkCars = BizManageBulk.getInstance().getBulkCars(clientNumber);
			// 사원 목록
			employees = BizManageBulk.getInstance().getEmployeeCodes(clientNumber);

			out.print("Invalid parameter");

			out.print("Invalid parameter");

			out.print("Invalid parameter");

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
<!-- DATEPICKER -->
<link rel="stylesheet" type="text/css" href="./assets/global/plugins/bootstrap-datepicker/css/datepicker3.css" />
<script type="text/javascript" src="./assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="./assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.kr.js"></script>
<!-- DATEPICKER -->

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
			                	<ul class="bulk_select">
									<li class="bulk_list">
										<select name="bulk_idx" id="bulk_list" class="bs-select form-control" data-live-search="true" data-size="8">
					                    	<option value="">벌크로리 전체</option><%
	for(Map.Entry<String, BulkCar> entry : bulkCars.getValues().entrySet()) {
		BulkCar bulkCar = entry.getValue();
		String bulkCode = bulkCar.getCarCode();
		String bulkName = bulkCar.getCarName();
%>
											<option value="<%= bulkCode %>"><%= bulkName %></option><%
	}
%>
										</select>
									</li>
								</ul>
			                	<ul class="sw_select right">
				                	<li class="sw_list">
					                    <select name="sw_idx" id="sw_list" class="bs-select form-control" data-live-search="true" data-size="8">
					                    	<option value="">사원 전체</option><%
	for(Map.Entry<String, EmployeeCode> entry : employees.getEmployeeCodes().entrySet()) {
		EmployeeCode employee = entry.getValue();
		String employeeCode = employee.getEmployeeCode();
		String employeeName = employee.getEmployeeName();
%>
											<option value="<%= employeeCode %>"><%= employeeName %></option><%
	}
%>
					                    </select>
									</li>
								</ul>
			                </li>
							<li class="search_date">
								<div class="input-group input-append date" id="datepicker_search">
								    <input type="text" class="form-control">
								    <div class="input-group-addon">
								        <span class="glyphicon glyphicon-calendar"></span>
								    </div>
								</div>
			            	</li>
			            	<li class="button divider_vertical">
			            		<button type="button" id="btn_init" class="btn btn-default">초기화</button>
			            		<button type="button" id="btn_search" class="btn btn-default">검색</button>
			            	</li>
			            	<li class="chb divider_vertical">
			                	<div class="form-check">
    								<input type="checkbox" class="form-check-input" id="chb_loc_bulk" checked>
    								<label class="form-check-label" for="chb_loc_bulk">차량위치 표시</label>
		    					</div>
    							<div class="form-check">
    								<input type="checkbox" class="form-check-input" id="chb_loc_charge" checked>
    								<label class="form-check-label" for="chb_loc_charge">충전위치 표시</label>
		    					</div>
		    					<div class="form-check">
    								<input type="checkbox" class="form-check-input" id="chb_route" checked>
    								<label class="form-check-label" for="chb_route">이동경로 표시</label>
		    					</div>
		    					<div class="form-check">
    								<input type="checkbox" class="form-check-input" id="chb_route_time" checked>
    								<label class="form-check-label" for="chb_route_time">충전시간 표시</label>
		    					</div>
			                </li>

			                <li class="bulk_lists" id="bulk-list"></li>
			            </ul>
			        </nav>
			        <form class="form-inline margin-bottom-10" id="fCreate" method="post">
			            <div class="input-group">
			                <input type="hidden" name="current_latitude" id="current_latitude">
			                <input type="hidden" name="current_longitude" id="current_longitude">
			                <input type="hidden" name="indicator_level" id="indicator_level">
			                <input type="hidden" name="indicator_state" id="indicator_state">
			                <input type="hidden" name="client_number" id="client_number" value="<%=clientNumber != null ? clientNumber : ""%>" />
			                <input type="hidden" name="sawon_code" id="sawon_code" value="<%=sawonCode != null ? sawonCode : ""%>" />
			                <input type="hidden" name="qid" id="qid" value="<%=queryString != null ? queryString : ""%>" />
			    		</div>
			        </form>
			    </div>
		    </div>
		    <div class="map-area" id="gmap-container">
		    	<div id="nmap-container" style="width:100%;height:100%;"></div>
		    </div>
		</div>
        <!-- END PAGE CONTENT BODY -->
        <!-- END CONTENT BODY -->

    </div>
    <!-- END CONTENT -->

</div>
<!-- END CONTAINER -->

<!-- BEGIN CORE PLUGINS -->
<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=<%=mapId%>"></script>
<script type="text/javascript">var clientJSON = {<%=myCompanys != null ? myCompanys.toJSON() : ""%>};</script>
<script src="./assets/js/bulk.js" type="text/javascript"></script>

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
