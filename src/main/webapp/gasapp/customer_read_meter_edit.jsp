<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.AppUser" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerSearch" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerLatestReadMeter" %>
<%@ page import="com.joainfo.gasmax.biz.BizCustomerVolumeReadMeter" %>
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
	String key = request.getParameter("key");
	String readMeterDate = request.getParameter("readMeterDate");
	boolean insertMode = key==null;
	try{
		AppUser appUser = (AppUser)session.getAttribute("USER_INFO");

		String uuid= request.getParameter("uuid");

		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);
		System.out.println(uuid);

		appUser = BizAppUser.getInstance().getAppUserByHpSeq(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME, request.getParameter("uuid"), request.getParameter("hpSeq"));
		if (appUser != null){
			String serverIp = appUser.getIpAddress();
			String catalogName = appUser.getDbCatalogName();
			String areaCode = appUser.getAreaCode();

			CustomerSearch customerSearch = (CustomerSearch)session.getAttribute("CURRENT_CUSTOMER");
			String sessionToken = request.getParameter("sessionToken");
			customerSearch = RedisUtil.getFromRedis(sessionToken, "CURRENT_CUSTOMER", CustomerSearch.class );
			if (customerSearch != null){
				String customerCode = customerSearch.getCustomerCode();
				String customerName = customerSearch.getCustomerName();
				if (("".equals(readMeterDate)) || (readMeterDate == null)){
					readMeterDate = StringUtil.dateFormatStr(null);
				}
				// 최근 검침 내역 가져오기
				CustomerLatestReadMeter customerLatestReadMeter = BizCustomerVolumeReadMeter.getInstance().getCustomerLatestReadMeter(serverIp, catalogName, areaCode, customerCode, readMeterDate);
				if (customerLatestReadMeter != null){
					String buildingName = customerLatestReadMeter.getBuildingName();
					String userName = customerLatestReadMeter.getUserName();
					String employeeCode = customerLatestReadMeter.getEmployeeCode();
					String employeeName = customerLatestReadMeter.getEmployeeName();
					if (("".equals(employeeCode)) || (employeeCode == null)){
						employeeCode = appUser.getEmployeeCode();
						employeeName = appUser.getEmployeeName();
					}
					String delayFeeMethodType = customerLatestReadMeter.getDelayFeeMethodType();
					String roundType = customerLatestReadMeter.getRoundType();
					String priceType = customerLatestReadMeter.getPriceType();
					String environmentPrice = customerLatestReadMeter.getEnvironmentPrice();
					String unitPrice = customerLatestReadMeter.getUnitPrice();
					String discountPrice = customerLatestReadMeter.getDiscountPrice();
					String defaultAmountYesNo = customerLatestReadMeter.getDefaultAmountYesNo();
					String defaultUse = customerLatestReadMeter.getDefaultUse();
					String defaultAmount = customerLatestReadMeter.getDefaultAmount();
					String managementAmount = customerLatestReadMeter.getManagementAmount();
					String delayFeePercent = customerLatestReadMeter.getDelayFeePercent();
					String discountPercent = customerLatestReadMeter.getDiscountPercent();
					String delayAmount1 = customerLatestReadMeter.getDelayAmount1();
					String delayAmount2 = customerLatestReadMeter.getDelayAmount2();
					String delayAmount3 = customerLatestReadMeter.getDelayAmount3();
					String delayAmount4 = customerLatestReadMeter.getDelayAmount4();
					String delayAmount5 = customerLatestReadMeter.getDelayAmount5();
					String preSequenceNumber = customerLatestReadMeter.getPreSequenceNumber();
					String preReadMeterDate = customerLatestReadMeter.getPreReadMeterDate();
					String preReadMeter = customerLatestReadMeter.getPreReadMeter();
					String preAmount = customerLatestReadMeter.getPreAmount();
					String preRemain = customerLatestReadMeter.getPreRemain();
					String collectDate = customerLatestReadMeter.getCollectDate();
					String preUnpaidAmount = customerLatestReadMeter.getPreUnpaidAmount();
					String applyDelayDate = customerLatestReadMeter.getApplyDelayDate();

					String readMeterYYYYMM = readMeterDate.substring(0, 7);
					String preReadMeterYYYYMM = "".equals(preReadMeterDate)?"":preReadMeterDate.substring(0, 7);
					String snoStr = "".equals(preSequenceNumber.trim())?"1":preSequenceNumber.substring(6).trim(); //전 검침년월이 당월이 아니면 당월+전검침회차
					String sNoSelectedValue = snoStr; // 전 검침년월이 당월이 아니면 전월회차를 디폴트로 표시

					int sno = new Integer(snoStr).intValue();
					if (StringUtil.stringReplace(readMeterDate).substring(0, 7).equals(preReadMeterYYYYMM)){ //전월 검침년월이 당월과 같으면
						//당월+전검침회차 + 1
						sno ++;
						//마지막 회차를 디폴트로 표시
						sNoSelectedValue = "" + sno;
					}
					if (sNoSelectedValue.length()==1) sNoSelectedValue = "0" + sNoSelectedValue;

					//연체료 적용방법이 [0,1,2]일경우 숨김처리
					boolean hiddenApplyDelayDate = "0".equals(delayFeeMethodType) || "1".equals(delayFeeMethodType) || "2".equals(delayFeeMethodType);
					String startDate = StringUtil.addDay(preReadMeterDate, 1);
					String price = environmentPrice;
					if ("개별단가".equals(priceType)){
						price = unitPrice;
					} else if("할인단가".equals(priceType)){
						price = discountPrice;
					} else { // 환경단가
						price = environmentPrice;
					}

					//연체료 처리
					int basisDelayAmount = 0;
					if ("0".equals(delayFeeMethodType)) { // 연체료 적용안함
					} else if ("1".equals(delayFeeMethodType)) { // 미수금 연체적용 (등록일 기준). 연체료 = delayAmount1 * 연체율
						basisDelayAmount = new Integer(delayAmount1).intValue();
					} else if ("2".equals(delayFeeMethodType)) { // 전월미납 연체적용 (등록일 기준). 연체료 = delayAmount2 * 연체율
						basisDelayAmount = new Integer(delayAmount2).intValue();
					} else if ("3".equals(delayFeeMethodType)) { // 전월미납 연체적용 (연체적용일 기준). 연체료 = delayAmount3 * 연체율
						basisDelayAmount = new Integer(delayAmount3).intValue();
					} else if ("4".equals(delayFeeMethodType)) { // 미 수 금 연체적용 (연체적용일 기준). 연체료 = delayAmount4 * 연체율
						basisDelayAmount = new Integer(delayAmount4).intValue();
					} else if ("5".equals(delayFeeMethodType)) { // 미납사용료 연체적용(납부마감일기준). 연체료 = delayAmount5 * 연체율
						basisDelayAmount = new Integer(delayAmount5).intValue();
					}
//					float delayFee = new Integer(delayFeePercent).intValue() / 100;
//					float delayFee = new Integer(delayFeePercent).intValue();
					float delayFee = new Float(delayFeePercent).floatValue();
					String delayAmount = "" + new Double(Math.floor(basisDelayAmount * delayFee / 100)).intValue();


%>
<input type="hidden" id="hdnCustomerNameCustomerReadMeterEdit" value="<%=buildingName %>" />
<input type="hidden" id="hdnUserNameCustomerReadMeterEdit" value="<%=userName %>" />
<input type="hidden" id="hdnEmployeeCodeCustomerReadMeterEdit" value="<%=employeeCode %>" />
<input type="hidden" id="hdnEmployeeNameCustomerReadMeterEdit" value="<%=employeeName %>" />
<input type="hidden" id="hdnDelayFeePercentCustomerReadMeterEdit" value="<%=delayFeePercent %>" />
<input type="hidden" id="hdnPreRemainCustomerReadMeterEdit" value="<%=preRemain %>" />
<input type="hidden" id="hdnStartDateCustomerReadMeterEdit" value="<%=startDate %>" />
<input type="hidden" id="hdnDefaultAmountYesNoCustomerReadMeterEdit" value="<%=defaultAmountYesNo %>" />
<input type="hidden" id="hdnDefaultUseCustomerReadMeterEdit" value="<%=defaultUse %>" />
<input type="hidden" id="hdnDiscountPercentCustomerReadMeterEdit" value="<%=discountPercent %>" />
<input type="hidden" id="hdnRoundTypeCustomerReadMeterEdit" value="<%=roundType %>" />
<input type="hidden" id="hdnDelayAmountCustomerReadMeterEdit" value="<%=delayAmount %>" />
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">검침년월</td>
        <td style="width: 80px ; font-size:14px ; border-bottom: 0px solid #999999 ;">
	        <input type="text" data-mini="true" id="txtReadMeterYearMonthCustomerReadMeterEdit" value="<%=readMeterYYYYMM %>" maxLength="7" style="width: 100% ; color: blue ;  text-align: center ; "  onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="enableFixed('footerCustomerReadMeterInsert')" />
        </td>
        <td style="width: 60px; font-size:14px ; border-bottom: 0px solid #999999 ;">
	        <input type="text" data-mini="true" id="txtSequenceNumberCustomerReadMeterEdit" value="<%=sNoSelectedValue %>" maxLength="2" style="width: 70% ; color: blue ; text-align: center ; "  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurSN(this, 'footerCustomerReadMeterInsert')" />
        </td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ;">
	        <span>회차</span>
        </td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
<%
	if (hiddenApplyDelayDate) { //연체료 적용방법이 [0,1,2]일경우 연체적용일 숨김처리
		if (insertMode) { // 신규 등록일 경우 input id
%>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; " >검침일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtReadMeterDateCustomerReadMeterInsert" value="<%=readMeterDate %>" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterInsert', 'txtReadMeterDateCustomerReadMeterInsert')" onchange="changeReadMeterDateCustomerReadMeterEdit('txtReadMeterDateCustomerReadMeterInsert')" />
        </td>
    </tr>
<%
		}else{ // 수정일 경우 input id
%>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; " >검침일자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtReadMeterDateCustomerReadMeterEdit" value="<%=readMeterDate %>" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterEdit', 'txtReadMeterDateCustomerReadMeterEdit')" onchange="changeReadMeterDateCustomerReadMeterEdit('txtReadMeterDateCustomerReadMeterEdit')" />
        </td>
    </tr>
<%
		}
	} else { // 연체료 적용방법이 [0,1,2]이 아닐경우 연체적용일 표시
		if (insertMode) { // 신규 등록일 경우 input id
%>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">검침일자</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtReadMeterDateCustomerReadMeterInsert" value="<%=readMeterDate %>" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterInsert', 'txtReadMeterDateCustomerReadMeterInsert')" onchange="changeReadMeterDateCustomerReadMeterEdit('txtReadMeterDateCustomerReadMeterInsert')"  />
        </td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">연체적용일</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtApplyDelayDateCustomerReadMeterInsert" value="<%=StringUtil.dateFormatStr(applyDelayDate) %>"  readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterInsert', 'txtApplyDelayDateCustomerReadMeterInsert')"  onchange="changeApplyDelayDateCustomerReadMeterEdit()"  />
        </td>
    </tr>
<%
		}else{ // 수정일 경우 input id
%>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">검침일자</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtReadMeterDateCustomerReadMeterEdit" value="<%=readMeterDate %>" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterEdit', 'txtReadMeterDateCustomerReadMeterEdit')" onchange="changeReadMeterDateCustomerReadMeterEdit('txtReadMeterDateCustomerReadMeterEdit')" />
        </td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">연체적용일</td>
        <td style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtApplyDelayDateCustomerReadMeterEdit" value="<%=StringUtil.dateFormatStr(applyDelayDate) %>" readonly style="width: 100px ; color: blue ; " onclick="openCapacitorDatePicker('pageCustomerReadMeterEdit', 'txtApplyDelayDateCustomerReadMeterEdit')" />
        </td>
    </tr>
<%
		}
	}
%>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 0px solid #999999 ; ">사용기간</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 0px solid #999999 ; color: #3333FF ; ">
        	<input type="text" data-mini="true" id="txtUsePeriodCustomerReadMeterEdit" value="<%=startDate + " ~ " + readMeterDate %>" readonly style="width: 90% ; color: blue ; background-color: #DDDDDD ; " />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 1px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전검</td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">당검</td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">사용량</td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">x단가</td>
        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">사용료</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; " >
        	<input type="text" data-role="none" id="txtPreReadMeterCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.stringReplace(preReadMeter) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
        </td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtThisMonthReadMeterCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(preReadMeter) %>" maxLength="8" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateUseQuantityCustomerReadMeterEdit()"  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')"  />
        </td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtUseQuantityCustomerReadMeterEdit" data-mini="true" value="0" readonly  style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ;  border: 1px #DDDDDD ; background-color: #DDDDDD ; " onchange="calculateUseAmountCustomerReadMeterEdit()" />
        </td>
        <td style="width: 50px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtPriceCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(price) %>" maxLength="10" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateUseAmountCustomerReadMeterEdit()"   onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')"   />
        </td>
        <td style="width: 80px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtUseAmountCustomerReadMeterEdit" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " onchange="calculateDiscountAmountCustomerReadMeterEdit('<%=roundType%>')" />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">기본료</td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">+관리비</td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">-할인액</td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">+연체료</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtDefaultAmountCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(defaultAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
        </td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtManagementAmountCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(managementAmount) %>" maxLength="10" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateThisMonthAmountCustomerReadMeterEdit()"  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')"  />
        </td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtDiscountAmountCustomerReadMeterEdit" data-mini="true" value="0" maxLength="10" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateThisMonthAmountCustomerReadMeterEdit()"  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')"  />
        </td>
        <td style="width: 70px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtDelayAmountCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.stringReplaceExceptPoint(delayAmount) %>"  maxLength="10" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; " onchange="calculateThisMonthAmountCustomerReadMeterEdit()"  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')"  />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">전미수</td>
        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">당월금액</td>
        <td style="width: 100px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">청구금액</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtPreUnpaidAmountCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(delayAmount1) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
        </td>
        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtThisMonthAmountCustomerReadMeterEdit" data-mini="true" value="0" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
        </td>
        <td style="width: 90px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">
        	<input type="text" data-role="none" id="txtTotalAmountCustomerReadMeterEdit" data-mini="true" value="<%=StringUtil.moneyFormatStr(preUnpaidAmount) %>" readonly style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px #DDDDDD ; background-color: #DDDDDD ; " />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ; ">잔량</td>
        <td style="width: 200px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 0px solid #222222 ; border-bottom: 1px solid #222222 ; background-color: #DDDDDD ;">비고</td>
    </tr>
    <tr style="height: 40px ; ">
        <td style="width: 60px ; text-align: center ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="number" data-role="none" id="txtNowRemainCustomerReadMeterEdit" data-mini="true" value="0" style="width: 90% ; font-size: 14px ; text-align: right ; color: blue ; border: 1px white ; "  onclick="focusNumber(this, 'footerCustomerReadMeterInsert')" onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="blurNumber(this, 'footerCustomerReadMeterInsert')" />
        </td>
        <td style="width: 200px ; text-align: left ; font-size:14px ; border-left: 1px solid #222222 ; border-right: 1px solid #222222 ; border-top: 1px solid #222222 ; border-bottom: 1px solid #222222 ; ">
        	<input type="text" data-mini="true" id="txtRemarkCustomerReadMeterEdit" data-mini="true" value="" style="width: 100% ; font-size: 14px ; text-align: left ; color: blue ;  "  onfocus="disableFixed('footerCustomerReadMeterInsert')" onblur="enableFixed('footerCustomerReadMeterInsert')" />
        </td>
    </tr>
</table>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 0px solid #222222 ; border-collapse: collapse;">
    <tr>
    	<td style="text-align: center ;">
<%
					if (insertMode) {
%>
<input data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveInsertCustomerReadMeterEdit(false)" ></input>
<input data-mini="true" type="button" value="연속등록" data-icon="plus" data-corners="false" data-inline="true" onclick="clickSaveInsertCustomerReadMeterEdit(true)" ></input>
<%
					} else {
%>
<input data-mini="true" type="button" value="저장" data-icon="check" data-corners="false" data-inline="true" onclick="clickSaveUpdateCustomerReadMeterEdit()" ></input>
<input data-mini="true" type="button" value="삭제" data-icon="delete" data-corners="false" data-inline="true" onclick="clickDeleteCustomerReadMeterEdit()" ></input>
<%
					}
%>
		</td>
	</tr>
</table>
<%
				}
			}
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>
