<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.joainfo.common.util.StringUtil" %>
<%@ page import="com.joainfo.gasmax.bean.CustomerTaxInvoice" %>
<%@ page import="com.joainfo.gasmax.bean.list.CustomerTaxInvoiceMap" %>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%
	String key = request.getParameter("key");
	String period = request.getParameter("period");
	try{
		CustomerTaxInvoiceMap customerTaxInvoices = (CustomerTaxInvoiceMap)session.getAttribute("CUSTOMER_BOOK_TAX_INVOICE");
		if (customerTaxInvoices!=null) {
			CustomerTaxInvoice customerTaxInvoice = customerTaxInvoices.getCustomerTaxInvoice(key);
			if (customerTaxInvoice != null){
				String invoiceSerialNumber = customerTaxInvoice.getInvoiceSerialNumber();       // 일련번호
				String ediStatusName = customerTaxInvoice.getEdiStatusName();                   // EDI상태
				String ntsStatusName = customerTaxInvoice.getNtsStatusName();                   // NTS상태
				String supplierRegisterNumber = customerTaxInvoice.getSupplierRegisterNumber(); // 공급자 사업자번호
				String supplierOwnerName = customerTaxInvoice.getSupplierOwnerName();           // 공급자 상호
				String registerNumberType = customerTaxInvoice.getRegisterNumberType();         // 거래처 사업자번호구분 01:사업번호, 02: 주민번호, 03 외국인
				String registerNumberTypeName = "사업자등록번호";
				if ("02".equals(registerNumberType)) registerNumberTypeName = "주민등록번호";
				else if ("03".equals(registerNumberType)) registerNumberTypeName = "외국인여권번호";
				String registerNumber = customerTaxInvoice.getRegisterNumber();                 // 거래처 사업자번호
				if ("01".equals(registerNumberType)) {
					registerNumber = registerNumber.substring(0,3) + "-" + registerNumber.substring(3,5) + "-" + registerNumber.substring(5,10);
				}
				String customerName = customerTaxInvoice.getCustomerName();                     // 거래처 상호
				String ownerName = customerTaxInvoice.getOwnerName();                           // 거래처 대표자
				String address1 = customerTaxInvoice.getAddress1();                             // 거래처 주소1
				String address2 = customerTaxInvoice.getAddress2();                             // 거래처 주소2
				String subCompanyNumber = customerTaxInvoice.getSubCompanyNumber();             // 거래처 종사업장
				if (!"".equals(subCompanyNumber)) subCompanyNumber = "종사업장: " + subCompanyNumber;
				String businessType = customerTaxInvoice.getBusinessType();                     // 거래처 업태
				String businessItem = customerTaxInvoice.getBusinessItem();                     // 거래처 종목
				String issueDate = StringUtil.dateFormatStr(customerTaxInvoice.getIssueDate());                           // 작성일자
				String serialNumber = customerTaxInvoice.getSerialNumber();                     // 번호
				String remark = customerTaxInvoice.getRemark();                                 // 비고
				String amendReasonCode = customerTaxInvoice.getAmendReasonCode();               // 수정사유 01기재사항정정,02공급액변동,03환입,04해제
				String amendReasonName = "";
				if ("01".equals(amendReasonCode)) amendReasonName = "기재사항정정";
				else if ("02".equals(amendReasonCode)) amendReasonName = "공급액변동";
				else if ("03".equals(amendReasonCode)) amendReasonName = "환입";
				else if ("04".equals(amendReasonCode)) amendReasonName = "해제";

				//품목 1
				String itemDate1 = customerTaxInvoice.getItemDate1();
				String itemDate1MMDD = "";
				if (itemDate1 != null) itemDate1MMDD = itemDate1.substring(0, 2) + "/" + itemDate1.substring(2, 4);
				String itemName1 = customerTaxInvoice.getItemName1();
				String itemUnit1 = customerTaxInvoice.getItemUnit1();
				String itemQuantity1 = customerTaxInvoice.getItemQuantity1();
				String itemPrice1 = customerTaxInvoice.getItemPrice1();
				String itemAmount1 = customerTaxInvoice.getItemAmount1();
				String itemTax1 = customerTaxInvoice.getItemTax1();
				//품목 2
				String itemDate2 = customerTaxInvoice.getItemDate2();
				String itemDate2MMDD = "";
				if (!"".equals(itemDate2)) itemDate2MMDD = itemDate2.substring(0, 2) + "/" + itemDate2.substring(2, 4);
				String itemName2 = customerTaxInvoice.getItemName2();
				String itemUnit2 = customerTaxInvoice.getItemUnit2();
				String itemQuantity2 = customerTaxInvoice.getItemQuantity2();
				String itemPrice2 = customerTaxInvoice.getItemPrice2();
				String itemAmount2 = customerTaxInvoice.getItemAmount2();
				String itemTax2 = customerTaxInvoice.getItemTax2();
				//품목 3
				String itemDate3 = customerTaxInvoice.getItemDate3();
				String itemDate3MMDD = "";
				if (!"".equals(itemDate3)) itemDate3MMDD = itemDate3.substring(0, 2) + "/" + itemDate3.substring(2, 4);
				String itemName3 = customerTaxInvoice.getItemName3();
				String itemUnit3 = customerTaxInvoice.getItemUnit3();
				String itemQuantity3 = customerTaxInvoice.getItemQuantity3();
				String itemPrice3 = customerTaxInvoice.getItemPrice3();
				String itemAmount3 = customerTaxInvoice.getItemAmount3();
				String itemTax3 = customerTaxInvoice.getItemTax3();
				//품목 4
				String itemDate4 = customerTaxInvoice.getItemDate4();
				String itemDate4MMDD = "";
				if (!"".equals(itemDate4)) itemDate4MMDD = itemDate4.substring(0, 2) + "/" + itemDate4.substring(2, 4);
				String itemName4 = customerTaxInvoice.getItemName4();
				String itemUnit4 = customerTaxInvoice.getItemUnit4();
				String itemQuantity4 = customerTaxInvoice.getItemQuantity4();
				String itemPrice4 = customerTaxInvoice.getItemPrice4();
				String itemAmount4 = customerTaxInvoice.getItemAmount4();
				String itemTax4 = customerTaxInvoice.getItemTax4();

				String amount = customerTaxInvoice.getAmount();                                 // 공급액
				String tax = customerTaxInvoice.getTax();                                       // 세액
				String totalAmount = customerTaxInvoice.getTotalAmount();                       // 합계금액
				String invoiceType = customerTaxInvoice.getInvoiceType();                       // 영수/청구 구분 Y/N (영수,청구)
				String invoiceTypeName = "";
				if ("Y".equals(invoiceType)) invoiceTypeName = "영수";
				else if ("N".equals(invoiceType)) invoiceTypeName = "청구";
				String contactName = customerTaxInvoice.getContactName();                       // 담당자
				String contactPhoneNumber = customerTaxInvoice.getContactPhoneNumber();         // 전화번호
				String contactDepartment = "";                                                  // 부서명
				String contactEmailAddress = customerTaxInvoice.getContactEmailAddress();       // Email 주소
%>

<table style="width: 100% ; border: 0px solid #999999 ; border-top: 2px solid #222222 ; border-collapse: collapse;">
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">EDI</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; "><%=ediStatusName %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">NTS</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; "><%=ntsStatusName%>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">공급자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 1px solid #222222 ; "><%=supplierRegisterNumber %>&nbsp;<%=supplierOwnerName%>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 110px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">공급받는자</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "> <%=registerNumber %> (과세)</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">상호</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=customerName %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">성명</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=ownerName %>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">주소</td>
        <td colspan="3" style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=address1 + " " + address2 %> <%=subCompanyNumber %></td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">업태</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; "><%=businessType %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #222222 ; ">종목</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #222222 ; color: #3333FF ; "><%=businessItem %>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">작성일자</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=issueDate %></td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">수정사유</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=amendReasonName %>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">비고</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=remark %></td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 2px solid #222222 ; ">매출기간</td>
        <td colspan="3" style="font-size:14px ; border-bottom: 2px solid #222222 ; color: #3333FF ; "><%=period %></td>
    </tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-collapse: collapse;">
	<tr>
		<td style="width: 35px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">월/일</td>
		<td style="width: 70px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">품목</td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">단위</td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">수량</td>
		<td style="width: 50px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">단가</td>
		<td style="width: 50px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">공급액</td>
		<td style="width: 40px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #222222 ; ">세액</td>
	</tr>
	<tr>
		<td style="width: 35px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ; color: blue ; "><%= itemDate1MMDD %></td>
		<td style="width: 70px ; font-size:14px ; text-align: left ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= "".equals(itemName1)?"&nbsp;":itemName1 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ; color: blue ; "><%= itemUnit1 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemQuantity1) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemPrice1) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemAmount1) %></td>
		<td style="width: 40px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemTax1) %></td>
	</tr>
	<tr>
		<td style="width: 35px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= itemDate2MMDD %></td>
		<td style="width: 70px ; font-size:14px ; text-align: left ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= "".equals(itemName2)?"&nbsp;":itemName1 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= itemUnit2 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemQuantity2) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemPrice2) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemAmount2) %></td>
		<td style="width: 40px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemTax2) %></td>
	</tr>
	<tr>
		<td style="width: 35px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= itemDate3MMDD %></td>
		<td style="width: 70px ; font-size:14px ; text-align: left ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= "".equals(itemName3)?"&nbsp;":itemName1 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= itemUnit3 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemQuantity3) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemPrice3) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemAmount3) %></td>
		<td style="width: 40px ; font-size:14px ; text-align: right ; border-bottom: 1px solid #999999 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemTax3) %></td>
	</tr>
	<tr>
		<td style="width: 35px ; font-size:14px ; text-align: center ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= itemDate4MMDD %></td>
		<td style="width: 70px ; font-size:14px ; text-align: left ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= "".equals(itemName4)?"&nbsp;":itemName1 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: center ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= itemUnit4 %></td>
		<td style="width: 30px ; font-size:14px ; text-align: right ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemQuantity4) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemPrice4) %></td>
		<td style="width: 50px ; font-size:14px ; text-align: right ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemAmount4) %></td>
		<td style="width: 40px ; font-size:14px ; text-align: right ; border-bottom: 2px solid #222222 ;  color: blue ; "><%= StringUtil.moneyFormatStr(itemTax4) %></td>
	</tr>
</table>
<table style="width: 100% ; border: 0px solid #999999 ; border-collapse: collapse;">
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">공급액</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=StringUtil.moneyFormatStr(amount) %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">세액</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=StringUtil.moneyFormatStr(tax)%>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 2px solid #222222 ; ">합계</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 2px solid #222222 ; color: #3333FF ; "><%=StringUtil.moneyFormatStr(totalAmount) %></td>
        <td colspan="2" style="font-size:14px ; border-bottom: 2px solid #222222 ; ">위 금액을 <span style="color: #3333FF"><%=invoiceTypeName %></span> 함</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">담당자</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=contactName %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 1px solid #999999 ; ">전화</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 1px solid #999999 ; color: #3333FF ; "><%=contactPhoneNumber %>&nbsp;</td>
    </tr>
    <tr>
        <td style="width: 70px ; font-size:14px ; border-bottom: 2px solid #222222 ; ">부서명</td>
        <td style="width: 100px ; font-size:14px ; border-bottom: 2px solid #222222 ; color: #3333FF ; "><%=contactDepartment %></td>
        <td style="width: 70px ; font-size:14px ; border-bottom: 2px solid #222222 ; ">E-mail</td>
        <td style="width: 100px ;font-size:14px ; border-bottom: 2px solid #222222 ; color: #3333FF ; "><%=contactEmailAddress %>&nbsp;</td>
    </tr>
    <table style="width: 100% ; ">
		<tr>
			<td align="center">
				<input type="button" data-mini="true" data-icon="arrow-l" id="btnPriorCustomerBookTaxInvoiceDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookTaxInvoiceDetail('<%=key%>', 'prior')" value="이전"></input>						
				<input type="button" data-mini="true" data-icon="arrow-r" id="btnNextCustomerBookTaxInvoiceDetail" data-corners="false" data-inline="true" onclick="navigateCustomerBookTaxInvoiceDetail('<%=key%>', 'next')" value="다음"></input>
			</td>
		</tr>
	</table>
    
</table>

<%
			}
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>