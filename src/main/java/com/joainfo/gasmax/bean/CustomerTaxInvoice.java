package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 세금계산서 정보 모델
 * @author 백원태
 * @version 1.0
 */
public class CustomerTaxInvoice {
	/**
	 * Area_Code
	 * 영업소코드
	 */
	 private String areaCode;

	/**
	 * SE_YNType
	 * 매입매출분류
	 */
	 private String collectSaleType;

	/**
	 * SE_CU_Code
	 * 거래처코드매입처
	 */
	 private String customerCode;

	/**
	 * SE_Sno
	 * 계산서항번
	 */
	 private String sequenceNumber;

	/**
	 * SE_GongNO
	 * 공급사업자번호
	 */
	 private String supplierRegisterNumber;

	/**
	 * SE_GongSangHo
	 * 공급사업자명
	 */
	 private String supplierOwnerName;

	/**
	 * SE_Date
	 * 작성일자
	 */
	 private String issueDate;

	/**
	 * SE_NO
	 * 일련번호
	 */
	 private String serialNumber;

	/**
	 * SE_KumType
	 * 영수,청구
	 */
	 private String invoiceType;

	/**
	 * SE_Bigo
	 * 비고
	 */
	 private String remark;

	/**
	 * SE_EDI_R_Com_Type
	 * 사업자번호구분코드
	 */
	 private String registerNumberType;

	/**
	 * SE_SSNo
	 * 사업자번호
	 */
	 private String registerNumber;

	/**
	 * SE_EDI_R_Code
	 * 종사업장번호
	 */
	 private String subCompanyNumber;

	/**
	 * SE_Sangho
	 * 상호
	 */
	 private String customerName;

	/**
	 * SE_Sajang
	 * 성명
	 */
	 private String ownerName;

	/**
	 * SE_Addr1
	 * 주소1
	 */
	 private String address1;

	/**
	 * SE_Addr2
	 * 주소2
	 */
	 private String address2;

	/**
	 * SE_ZipCode
	 * 우편번호
	 */
	 private String postalCode;

	/**
	 * SE_Uptae
	 * 업태
	 */
	 private String businessType;

	/**
	 * SE_JongMok
	 * 종목
	 */
	 private String businessItem;

	/**
	 * SE_MMDD1
	 * 월일1
	 */
	 private String itemDate1;

	/**
	 * SE_PMok1
	 * 품목1
	 */
	 private String itemName1;

	/**
	 * SE_DanW1
	 * 단위1
	 */
	 private String itemUnit1;

	/**
	 * SE_QTY1
	 * 수량1
	 */
	 private String itemQuantity1;

	/**
	 * SE_Danga1
	 * 단가1
	 */
	 private String itemPrice1;

	/**
	 * SE_Kum1
	 * 공급가액1
	 */
	 private String itemAmount1;

	/**
	 * SE_VAT1
	 * 세액1
	 */
	 private String itemTax1;

	/**
	 * SE_MMDD2
	 * 월일2
	 */
	 private String itemDate2;

	/**
	 * SE_PMok2
	 * 품목2
	 */
	 private String itemName2;

	/**
	 * SE_DanW2
	 * 단위2
	 */
	 private String itemUnit2;

	/**
	 * SE_QTY2
	 * 수량2
	 */
	 private String itemQuantity2;

	/**
	 * SE_Danga2
	 * 단가2
	 */
	 private String itemPrice2;

	/**
	 * SE_Kum2
	 * 공급가액2
	 */
	 private String itemAmount2;

	/**
	 * SE_VAT2
	 * 세액2
	 */
	 private String itemTax2;

	/**
	 * SE_MMDD3
	 * 월일3
	 */
	 private String itemDate3;

	/**
	 * SE_PMok3
	 * 품목3
	 */
	 private String itemName3;

	/**
	 * SE_DanW3
	 * 단위3
	 */
	 private String itemUnit3;

	/**
	 * SE_QTY3
	 * 수량3
	 */
	 private String itemQuantity3;

	/**
	 * SE_Danga3
	 * 단가3
	 */
	 private String itemPrice3;

	/**
	 * SE_Kum3
	 * 공급가액3
	 */
	 private String itemAmount3;

	/**
	 * SE_VAT3
	 * 세액3
	 */
	 private String itemTax3;

	/**
	 * SE_MMDD4
	 * 월일4
	 */
	 private String itemDate4;

	/**
	 * SE_PMok4
	 * 품목4
	 */
	 private String itemName4;

	/**
	 * SE_DanW4
	 * 단위4
	 */
	 private String itemUnit4;

	/**
	 * SE_QTY4
	 * 수량4
	 */
	 private String itemQuantity4;

	/**
	 * SE_Danga4
	 * 단가4
	 */
	 private String itemPrice4;

	/**
	 * SE_Kum4
	 * 공급가액4
	 */
	 private String itemAmount4;

	/**
	 * SE_VAT4
	 * 세액4
	 */
	 private String itemTax4;

	/**
	 * SE_SumKum
	 * 공급가액
	 */
	 private String amount;

	/**
	 * SE_SumVAT
	 * 세액
	 */
	 private String tax;

	/**
	 * SE_Total
	 * 합계금액
	 */
	 private String totalAmount;

	/**
	 * Se_Turm_F
	 * 매출자료시작일
	 */
	 private String dataStartDate;

	/**
	 * Se_Turm_T
	 * 매출자료종료일
	 */
	 private String dateEndDate;

	/**
	 * SE_QTY_Print
	 * 수량단가인쇄여부
	 */
	 private String quantityPricePrintYesNo;

	/**
	 * SE_EDI_Doctype
	 * 계산서종류
	 */
	 private String taxInvoiceType;

	/**
	 * SE_EDI_StatusCode
	 * 수정사유코드
	 */
	 private String amendReasonCode;

	/**
	 * SE_EDI_Serialnum
	 * 계산서일련번호
	 */
	 private String invoiceSerialNumber;

	/**
	 * SE_EDI_R_a_name
	 * 담당자명1
	 */
	 private String contactName;

	/**
	 * SE_EDI_R_a_email
	 * 담당자이메일1
	 */
	 private String contactEmailAddress;

	/**
	 * SE_EDI_R_a_phone
	 * 담당자전화번호1
	 */
	 private String contactPhoneNumber;

	/**
	 * SE_EDI_State
	 * 상태코드
	 */
	 private String ediStatusCode;

	/**
	 * SE_EDI_NAME
	 * EDI상태명
	 */
	 private String ediStatusName;

	/**
	 * SE_NTS_State
	 * 국세청전송상태
	 */
	 private String ntsStatusCode;

	/**
	 * SE_NTS_NAME
	 * NTS상태명
	 */
	 private String ntsStatusName;

	/**
	 * SE_NTS_ISSUE_ID
	 * 국세청승인번호
	 */
	 private String ntsIssueNumber;

	/**
	 * SE_EDI_Error_Msg
	 * ERROR메세지
	 */
	 private String ediErrorMessage;
	 
	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("SE_CU_Code", getCustomerCode());
		 keys.put("SE_YNType", getCollectSaleType());
		 keys.put("SE_SNO", getSequenceNumber());
		 
		 return keys; 
	 }
	 
	 /**
	  * key 값 반환
	  * @return
	  */
	 public String getKeyValue(){
		 return StringUtil.getKeyValue(this.getKeyMap()); 
	 }

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the collectSaleType
	 */
	public String getCollectSaleType() {
		return collectSaleType;
	}

	/**
	 * @param collectSaleType the collectSaleType to set
	 */
	public void setCollectSaleType(String collectSaleType) {
		this.collectSaleType = collectSaleType;
	}

	/**
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * @param customerCode the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the supplierRegisterNumber
	 */
	public String getSupplierRegisterNumber() {
		return supplierRegisterNumber;
	}

	/**
	 * @param supplierRegisterNumber the supplierRegisterNumber to set
	 */
	public void setSupplierRegisterNumber(String supplierRegisterNumber) {
		this.supplierRegisterNumber = supplierRegisterNumber;
	}

	/**
	 * @return the supplierOwnerName
	 */
	public String getSupplierOwnerName() {
		return supplierOwnerName;
	}

	/**
	 * @param supplierOwnerName the supplierOwnerName to set
	 */
	public void setSupplierOwnerName(String supplierOwnerName) {
		this.supplierOwnerName = supplierOwnerName;
	}

	/**
	 * @return the issueDate
	 */
	public String getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the invoiceType
	 */
	public String getInvoiceType() {
		return invoiceType;
	}

	/**
	 * @param invoiceType the invoiceType to set
	 */
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the registerNumberType
	 */
	public String getRegisterNumberType() {
		return registerNumberType;
	}

	/**
	 * @param registerNumberType the registerNumberType to set
	 */
	public void setRegisterNumberType(String registerNumberType) {
		this.registerNumberType = registerNumberType;
	}

	/**
	 * @return the registerNumber
	 */
	public String getRegisterNumber() {
		return registerNumber;
	}

	/**
	 * @param registerNumber the registerNumber to set
	 */
	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	/**
	 * @return the subCompanyNumber
	 */
	public String getSubCompanyNumber() {
		return subCompanyNumber;
	}

	/**
	 * @param subCompanyNumber the subCompanyNumber to set
	 */
	public void setSubCompanyNumber(String subCompanyNumber) {
		this.subCompanyNumber = subCompanyNumber;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the businessItem
	 */
	public String getBusinessItem() {
		return businessItem;
	}

	/**
	 * @param businessItem the businessItem to set
	 */
	public void setBusinessItem(String businessItem) {
		this.businessItem = businessItem;
	}

	/**
	 * @return the itemDate1
	 */
	public String getItemDate1() {
		return itemDate1;
	}

	/**
	 * @param itemDate1 the itemDate1 to set
	 */
	public void setItemDate1(String itemDate1) {
		this.itemDate1 = itemDate1;
	}

	/**
	 * @return the itemName1
	 */
	public String getItemName1() {
		return itemName1;
	}

	/**
	 * @param itemName1 the itemName1 to set
	 */
	public void setItemName1(String itemName1) {
		this.itemName1 = itemName1;
	}

	/**
	 * @return the itemUnit1
	 */
	public String getItemUnit1() {
		return itemUnit1;
	}

	/**
	 * @param itemUnit1 the itemUnit1 to set
	 */
	public void setItemUnit1(String itemUnit1) {
		this.itemUnit1 = itemUnit1;
	}

	/**
	 * @return the itemQuantity1
	 */
	public String getItemQuantity1() {
		return itemQuantity1;
	}

	/**
	 * @param itemQuantity1 the itemQuantity1 to set
	 */
	public void setItemQuantity1(String itemQuantity1) {
		this.itemQuantity1 = itemQuantity1;
	}

	/**
	 * @return the itemPrice1
	 */
	public String getItemPrice1() {
		return itemPrice1;
	}

	/**
	 * @param itemPrice1 the itemPrice1 to set
	 */
	public void setItemPrice1(String itemPrice1) {
		this.itemPrice1 = itemPrice1;
	}

	/**
	 * @return the itemAmount1
	 */
	public String getItemAmount1() {
		return itemAmount1;
	}

	/**
	 * @param itemAmount1 the itemAmount1 to set
	 */
	public void setItemAmount1(String itemAmount1) {
		this.itemAmount1 = itemAmount1;
	}

	/**
	 * @return the itemTax1
	 */
	public String getItemTax1() {
		return itemTax1;
	}

	/**
	 * @param itemTax1 the itemTax1 to set
	 */
	public void setItemTax1(String itemTax1) {
		this.itemTax1 = itemTax1;
	}

	/**
	 * @return the itemDate2
	 */
	public String getItemDate2() {
		return itemDate2;
	}

	/**
	 * @param itemDate2 the itemDate2 to set
	 */
	public void setItemDate2(String itemDate2) {
		this.itemDate2 = itemDate2;
	}

	/**
	 * @return the itemName2
	 */
	public String getItemName2() {
		return itemName2;
	}

	/**
	 * @param itemName2 the itemName2 to set
	 */
	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}

	/**
	 * @return the itemUnit2
	 */
	public String getItemUnit2() {
		return itemUnit2;
	}

	/**
	 * @param itemUnit2 the itemUnit2 to set
	 */
	public void setItemUnit2(String itemUnit2) {
		this.itemUnit2 = itemUnit2;
	}

	/**
	 * @return the itemQuantity2
	 */
	public String getItemQuantity2() {
		return itemQuantity2;
	}

	/**
	 * @param itemQuantity2 the itemQuantity2 to set
	 */
	public void setItemQuantity2(String itemQuantity2) {
		this.itemQuantity2 = itemQuantity2;
	}

	/**
	 * @return the itemPrice2
	 */
	public String getItemPrice2() {
		return itemPrice2;
	}

	/**
	 * @param itemPrice2 the itemPrice2 to set
	 */
	public void setItemPrice2(String itemPrice2) {
		this.itemPrice2 = itemPrice2;
	}

	/**
	 * @return the itemAmount2
	 */
	public String getItemAmount2() {
		return itemAmount2;
	}

	/**
	 * @param itemAmount2 the itemAmount2 to set
	 */
	public void setItemAmount2(String itemAmount2) {
		this.itemAmount2 = itemAmount2;
	}

	/**
	 * @return the itemTax2
	 */
	public String getItemTax2() {
		return itemTax2;
	}

	/**
	 * @param itemTax2 the itemTax2 to set
	 */
	public void setItemTax2(String itemTax2) {
		this.itemTax2 = itemTax2;
	}

	/**
	 * @return the itemDate3
	 */
	public String getItemDate3() {
		return itemDate3;
	}

	/**
	 * @param itemDate3 the itemDate3 to set
	 */
	public void setItemDate3(String itemDate3) {
		this.itemDate3 = itemDate3;
	}

	/**
	 * @return the itemName3
	 */
	public String getItemName3() {
		return itemName3;
	}

	/**
	 * @param itemName3 the itemName3 to set
	 */
	public void setItemName3(String itemName3) {
		this.itemName3 = itemName3;
	}

	/**
	 * @return the itemUnit3
	 */
	public String getItemUnit3() {
		return itemUnit3;
	}

	/**
	 * @param itemUnit3 the itemUnit3 to set
	 */
	public void setItemUnit3(String itemUnit3) {
		this.itemUnit3 = itemUnit3;
	}

	/**
	 * @return the itemQuantity3
	 */
	public String getItemQuantity3() {
		return itemQuantity3;
	}

	/**
	 * @param itemQuantity3 the itemQuantity3 to set
	 */
	public void setItemQuantity3(String itemQuantity3) {
		this.itemQuantity3 = itemQuantity3;
	}

	/**
	 * @return the itemPrice3
	 */
	public String getItemPrice3() {
		return itemPrice3;
	}

	/**
	 * @param itemPrice3 the itemPrice3 to set
	 */
	public void setItemPrice3(String itemPrice3) {
		this.itemPrice3 = itemPrice3;
	}

	/**
	 * @return the itemAmount3
	 */
	public String getItemAmount3() {
		return itemAmount3;
	}

	/**
	 * @param itemAmount3 the itemAmount3 to set
	 */
	public void setItemAmount3(String itemAmount3) {
		this.itemAmount3 = itemAmount3;
	}

	/**
	 * @return the itemTax3
	 */
	public String getItemTax3() {
		return itemTax3;
	}

	/**
	 * @param itemTax3 the itemTax3 to set
	 */
	public void setItemTax3(String itemTax3) {
		this.itemTax3 = itemTax3;
	}

	/**
	 * @return the itemDate4
	 */
	public String getItemDate4() {
		return itemDate4;
	}

	/**
	 * @param itemDate4 the itemDate4 to set
	 */
	public void setItemDate4(String itemDate4) {
		this.itemDate4 = itemDate4;
	}

	/**
	 * @return the itemName4
	 */
	public String getItemName4() {
		return itemName4;
	}

	/**
	 * @param itemName4 the itemName4 to set
	 */
	public void setItemName4(String itemName4) {
		this.itemName4 = itemName4;
	}

	/**
	 * @return the itemUnit4
	 */
	public String getItemUnit4() {
		return itemUnit4;
	}

	/**
	 * @param itemUnit4 the itemUnit4 to set
	 */
	public void setItemUnit4(String itemUnit4) {
		this.itemUnit4 = itemUnit4;
	}

	/**
	 * @return the itemQuantity4
	 */
	public String getItemQuantity4() {
		return itemQuantity4;
	}

	/**
	 * @param itemQuantity4 the itemQuantity4 to set
	 */
	public void setItemQuantity4(String itemQuantity4) {
		this.itemQuantity4 = itemQuantity4;
	}

	/**
	 * @return the itemPrice4
	 */
	public String getItemPrice4() {
		return itemPrice4;
	}

	/**
	 * @param itemPrice4 the itemPrice4 to set
	 */
	public void setItemPrice4(String itemPrice4) {
		this.itemPrice4 = itemPrice4;
	}

	/**
	 * @return the itemAmount4
	 */
	public String getItemAmount4() {
		return itemAmount4;
	}

	/**
	 * @param itemAmount4 the itemAmount4 to set
	 */
	public void setItemAmount4(String itemAmount4) {
		this.itemAmount4 = itemAmount4;
	}

	/**
	 * @return the itemTax4
	 */
	public String getItemTax4() {
		return itemTax4;
	}

	/**
	 * @param itemTax4 the itemTax4 to set
	 */
	public void setItemTax4(String itemTax4) {
		this.itemTax4 = itemTax4;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the tax
	 */
	public String getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(String tax) {
		this.tax = tax;
	}

	/**
	 * @return the totalAmount
	 */
	public String getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return the dataStartDate
	 */
	public String getDataStartDate() {
		return dataStartDate;
	}

	/**
	 * @param dataStartDate the dataStartDate to set
	 */
	public void setDataStartDate(String dataStartDate) {
		this.dataStartDate = dataStartDate;
	}

	/**
	 * @return the dateEndDate
	 */
	public String getDateEndDate() {
		return dateEndDate;
	}

	/**
	 * @param dateEndDate the dateEndDate to set
	 */
	public void setDateEndDate(String dateEndDate) {
		this.dateEndDate = dateEndDate;
	}

	/**
	 * @return the quantityPricePrintYesNo
	 */
	public String getQuantityPricePrintYesNo() {
		return quantityPricePrintYesNo;
	}

	/**
	 * @param quantityPricePrintYesNo the quantityPricePrintYesNo to set
	 */
	public void setQuantityPricePrintYesNo(String quantityPricePrintYesNo) {
		this.quantityPricePrintYesNo = quantityPricePrintYesNo;
	}

	/**
	 * @return the taxInvoiceType
	 */
	public String getTaxInvoiceType() {
		return taxInvoiceType;
	}

	/**
	 * @param taxInvoiceType the taxInvoiceType to set
	 */
	public void setTaxInvoiceType(String taxInvoiceType) {
		this.taxInvoiceType = taxInvoiceType;
	}

	/**
	 * @return the amendReasonCode
	 */
	public String getAmendReasonCode() {
		return amendReasonCode;
	}

	/**
	 * @param amendReasonCode the amendReasonCode to set
	 */
	public void setAmendReasonCode(String amendReasonCode) {
		this.amendReasonCode = amendReasonCode;
	}

	/**
	 * @return the invoiceSerialNumber
	 */
	public String getInvoiceSerialNumber() {
		return invoiceSerialNumber;
	}

	/**
	 * @param invoiceSerialNumber the invoiceSerialNumber to set
	 */
	public void setInvoiceSerialNumber(String invoiceSerialNumber) {
		this.invoiceSerialNumber = invoiceSerialNumber;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactEmailAddress
	 */
	public String getContactEmailAddress() {
		return contactEmailAddress;
	}

	/**
	 * @param contactEmailAddress the contactEmailAddress to set
	 */
	public void setContactEmailAddress(String contactEmailAddress) {
		this.contactEmailAddress = contactEmailAddress;
	}

	/**
	 * @return the contactPhoneNumber
	 */
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	/**
	 * @param contactPhoneNumber the contactPhoneNumber to set
	 */
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	/**
	 * @return the ediStatusCode
	 */
	public String getEdiStatusCode() {
		return ediStatusCode;
	}

	/**
	 * @param ediStatusCode the ediStatusCode to set
	 */
	public void setEdiStatusCode(String ediStatusCode) {
		this.ediStatusCode = ediStatusCode;
	}

	/**
	 * @return the ediStatusName
	 */
	public String getEdiStatusName() {
		return ediStatusName;
	}

	/**
	 * @param ediStatusName the ediStatusName to set
	 */
	public void setEdiStatusName(String ediStatusName) {
		this.ediStatusName = ediStatusName;
	}

	/**
	 * @return the ntsStatusCode
	 */
	public String getNtsStatusCode() {
		return ntsStatusCode;
	}

	/**
	 * @param ntsStatusCode the ntsStatusCode to set
	 */
	public void setNtsStatusCode(String ntsStatusCode) {
		this.ntsStatusCode = ntsStatusCode;
	}

	/**
	 * @return the ntsStatusName
	 */
	public String getNtsStatusName() {
		return ntsStatusName;
	}

	/**
	 * @param ntsStatusName the ntsStatusName to set
	 */
	public void setNtsStatusName(String ntsStatusName) {
		this.ntsStatusName = ntsStatusName;
	}

	/**
	 * @return the ntsIssueNumber
	 */
	public String getNtsIssueNumber() {
		return ntsIssueNumber;
	}

	/**
	 * @param ntsIssueNumber the ntsIssueNumber to set
	 */
	public void setNtsIssueNumber(String ntsIssueNumber) {
		this.ntsIssueNumber = ntsIssueNumber;
	}

	/**
	 * @return the ediErrorMessage
	 */
	public String getEdiErrorMessage() {
		return ediErrorMessage;
	}

	/**
	 * @param ediErrorMessage the ediErrorMessage to set
	 */
	public void setEdiErrorMessage(String ediErrorMessage) {
		this.ediErrorMessage = ediErrorMessage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerTaxInvoice [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode + ", collectSaleType=" + collectSaleType
				+ ", customerCode=" + customerCode + ", sequenceNumber="
				+ sequenceNumber + ", supplierRegisterNumber="
				+ supplierRegisterNumber + ", supplierOwnerName="
				+ supplierOwnerName + ", issueDate=" + issueDate
				+ ", serialNumber=" + serialNumber + ", invoiceType="
				+ invoiceType + ", remark=" + remark + ", registerNumberType="
				+ registerNumberType + ", registerNumber=" + registerNumber
				+ ", subCompanyNumber=" + subCompanyNumber + ", customerName="
				+ customerName + ", ownerName=" + ownerName + ", address1="
				+ address1 + ", address2=" + address2 + ", postalCode="
				+ postalCode + ", businessType=" + businessType
				+ ", businessItem=" + businessItem + ", itemDate1=" + itemDate1
				+ ", itemName1=" + itemName1 + ", itemUnit1=" + itemUnit1
				+ ", itemQuantity1=" + itemQuantity1 + ", itemPrice1="
				+ itemPrice1 + ", itemAmount1=" + itemAmount1 + ", itemTax1="
				+ itemTax1 + ", itemDate2=" + itemDate2 + ", itemName2="
				+ itemName2 + ", itemUnit2=" + itemUnit2 + ", itemQuantity2="
				+ itemQuantity2 + ", itemPrice2=" + itemPrice2
				+ ", itemAmount2=" + itemAmount2 + ", itemTax2=" + itemTax2
				+ ", itemDate3=" + itemDate3 + ", itemName3=" + itemName3
				+ ", itemUnit3=" + itemUnit3 + ", itemQuantity3="
				+ itemQuantity3 + ", itemPrice3=" + itemPrice3
				+ ", itemAmount3=" + itemAmount3 + ", itemTax3=" + itemTax3
				+ ", itemDate4=" + itemDate4 + ", itemName4=" + itemName4
				+ ", itemUnit4=" + itemUnit4 + ", itemQuantity4="
				+ itemQuantity4 + ", itemPrice4=" + itemPrice4
				+ ", itemAmount4=" + itemAmount4 + ", itemTax4=" + itemTax4
				+ ", amount=" + amount + ", tax=" + tax + ", totalAmount="
				+ totalAmount + ", dataStartDate=" + dataStartDate
				+ ", dateEndDate=" + dateEndDate + ", quantityPricePrintYesNo="
				+ quantityPricePrintYesNo + ", taxInvoiceType="
				+ taxInvoiceType + ", amendReasonCode=" + amendReasonCode
				+ ", invoiceSerialNumber=" + invoiceSerialNumber
				+ ", contactName=" + contactName + ", contactEmailAddress="
				+ contactEmailAddress + ", contactPhoneNumber="
				+ contactPhoneNumber + ", ediStatusCode=" + ediStatusCode
				+ ", ediStatusName=" + ediStatusName + ", ntsStatusCode="
				+ ntsStatusCode + ", ntsStatusName=" + ntsStatusName
				+ ", ntsIssueNumber=" + ntsIssueNumber + ", ediErrorMessage="
				+ ediErrorMessage + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerTaxInvoice><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode + "</areaCode><collectSaleType>" + collectSaleType
				+ "</collectSaleType><customerCode>" + customerCode
				+ "</customerCode><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><supplierRegisterNumber>"
				+ supplierRegisterNumber
				+ "</supplierRegisterNumber><supplierOwnerName><![CDATA["
				+ supplierOwnerName + "]]></supplierOwnerName><issueDate>"
				+ issueDate + "</issueDate><serialNumber>" + serialNumber
				+ "</serialNumber><invoiceType>" + invoiceType
				+ "</invoiceType><remark><![CDATA[" + remark
				+ "]]></remark><registerNumberType>" + registerNumberType
				+ "</registerNumberType><registerNumber>" + registerNumber
				+ "</registerNumber><subCompanyNumber>" + subCompanyNumber
				+ "</subCompanyNumber><customerName><![CDATA[" + customerName
				+ "]]></customerName><ownerName><![CDATA[" + ownerName
				+ "]]></ownerName><address1><![CDATA[" + address1 + "]]></address1><address2><![CDATA["
				+ address2 + "]]></address2><postalCode>" + postalCode
				+ "</postalCode><businessType>" + businessType
				+ "</businessType><businessItem>" + businessItem
				+ "</businessItem><itemDate1>" + itemDate1
				+ "</itemDate1><itemName1>" + itemName1
				+ "</itemName1><itemUnit1>" + itemUnit1
				+ "</itemUnit1><itemQuantity1>" + itemQuantity1
				+ "</itemQuantity1><itemPrice1>" + itemPrice1
				+ "</itemPrice1><itemAmount1>" + itemAmount1
				+ "</itemAmount1><itemTax1>" + itemTax1
				+ "</itemTax1><itemDate2>" + itemDate2
				+ "</itemDate2><itemName2>" + itemName2
				+ "</itemName2><itemUnit2>" + itemUnit2
				+ "</itemUnit2><itemQuantity2>" + itemQuantity2
				+ "</itemQuantity2><itemPrice2>" + itemPrice2
				+ "</itemPrice2><itemAmount2>" + itemAmount2
				+ "</itemAmount2><itemTax2>" + itemTax2
				+ "</itemTax2><itemDate3>" + itemDate3
				+ "</itemDate3><itemName3>" + itemName3
				+ "</itemName3><itemUnit3>" + itemUnit3
				+ "</itemUnit3><itemQuantity3>" + itemQuantity3
				+ "</itemQuantity3><itemPrice3>" + itemPrice3
				+ "</itemPrice3><itemAmount3>" + itemAmount3
				+ "</itemAmount3><itemTax3>" + itemTax3
				+ "</itemTax3><itemDate4>" + itemDate4
				+ "</itemDate4><itemName4>" + itemName4
				+ "</itemName4><itemUnit4>" + itemUnit4
				+ "</itemUnit4><itemQuantity4>" + itemQuantity4
				+ "</itemQuantity4><itemPrice4>" + itemPrice4
				+ "</itemPrice4><itemAmount4>" + itemAmount4
				+ "</itemAmount4><itemTax4>" + itemTax4 + "</itemTax4><amount>"
				+ amount + "</amount><tax>" + tax + "</tax><totalAmount>"
				+ totalAmount + "</totalAmount><dataStartDate>" + dataStartDate
				+ "</dataStartDate><dateEndDate>" + dateEndDate
				+ "</dateEndDate><quantityPricePrintYesNo>"
				+ quantityPricePrintYesNo
				+ "</quantityPricePrintYesNo><taxInvoiceType>" + taxInvoiceType
				+ "</taxInvoiceType><amendReasonCode>" + amendReasonCode
				+ "</amendReasonCode><invoiceSerialNumber>"
				+ invoiceSerialNumber + "</invoiceSerialNumber><contactName><![CDATA["
				+ contactName + "]]></contactName><contactEmailAddress><![CDATA["
				+ contactEmailAddress
				+ "]]></contactEmailAddress><contactPhoneNumber>"
				+ contactPhoneNumber + "</contactPhoneNumber><ediStatusCode>"
				+ ediStatusCode + "</ediStatusCode><ediStatusName>"
				+ ediStatusName + "</ediStatusName><ntsStatusCode>"
				+ ntsStatusCode + "</ntsStatusCode><ntsStatusName>"
				+ ntsStatusName + "</ntsStatusName><ntsIssueNumber>"
				+ ntsIssueNumber + "</ntsIssueNumber><ediErrorMessage>"
				+ ediErrorMessage + "</ediErrorMessage></CustomerTaxInvoice>";
	}

}
