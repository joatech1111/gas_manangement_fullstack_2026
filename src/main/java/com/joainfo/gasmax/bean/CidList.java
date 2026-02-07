package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CidList {
	/**
	 * Area_Code	
	 * String      2	영업소코드	
	 */
	 private String areaCode;

	/**
	 * CID_Date	
	 * String      8	일자	
	 */
	 private String cidDate;

	/**
	 * CID_Date_Sno	
	 * Integer     10	항번	
	 */
	 private String sequenceNumber;

	/**
	 * CID_Time	
	 * String      6	시간	
	 */
	 private String cidTime;

	/**
	 * CID_AutoGubun	
	 * String      1	자동등록유무	0 자동등록자료, 1.  , 2.수기등록
	 */
	 private String autoRegisterType;

	/**
	 * CID_Tel	
	 * String      14	전화번호	
	 */
	 private String phoneNumber;

	/**
	 * CID_Sale_TYPE	
	 * String      1	판매구분	SELECT Code, CodeName   FROM BA_Code  WHERE Gubun = 'CID'
	 */
	 private String saleTypeCode;

	/**
	 * CID_Sale_Name	
	 * String      10	판매구분명	배달, 수금, A/S, 점검, 시설, 기타
	 */
	 private String saleTypeName;

	/**
	 * CID_CU_Code	
	 * String      9	거래처코드	
	 */
	 private String customerCode;

	/**
	 * CID_CU_Name	
	 * String      40	거래처명	
	 */
	 private String customerName;

	/**
	 * CID_JP_Code	
	 * String      2	품목코드	
	 */
	 private String itemCode;

	/**
	 * CID_JP_Name	
	 * String      31	품명	
	 */
	 private String itemName;

	/**
	 * CID_QTY	
	 * Integer     10	납품수량	
	 */
	 private String saleQuantity;

	/**
	 * CID_REQTY	
	 * Integer     10	회수수량	
	 */
	 private String withdrawQuantity;

	/**
	 * CID_JP_Danga	
	 * Float       10	단가	
	 */
	 private String cidPrice;
	 
	 /**
	  * CID_VAT_Div	
	  * 부가세적용방법
	  */
	 private String vatType;
	 
	 /**
	  * CID_Kum_Sup	
	  * 공급액
	  */
	 private String cidAmount;
	 
	 /**
	  * CID_Kum_Vat	
	  * 세액
	  */
	 private String taxAmount;

	/**
	 * CID_Kumack	
	 * Float       10	금액	
	 */
	 private String totalAmount;
	 
	/**
	 * CID_DC	
	 * Float       10	D/C	
	 */
	 private String discountAmount;

	/**
	 * CID_INKum	
	 * Float       10	입금액	
	 */
	 private String collectAmount;

	/**
	 * CID_MisuKum	
	 * Float       10	미수금액	
	 */
	 private String unpaidAmount;

	/**
	 * CID_SW_Code	
	 * String      2	사원코드	
	 */
	 private String employeeCode;

	/**
	 * CID_SW_Name	
	 * String      10	사원명	
	 */
	 private String employeeName;

	/**
	 * CID_Bigo	
	 * String      40	비고	
	 */
	 private String remark;

	/**
	 * CID_OK_YN	
	 * Boolean     5	배달유무	True/False  OR 0/1
	 */
	 private String deliveryYesNo;

	/**
	 * CID_INKum_YN	
	 * Boolean     5	완료유무	True/False  OR 0/1
	 */
	 private String completeYesNo;
	 
	 /**
	  * CID_InType	
	  * 입금방법 0현금,2예금,3카드,4어음,A외상,B현금영수증 
	  */
	 private String collectType;
	 
	 /**
	  * CU_ADDR	
	  * String      80	주소
	  */
	 private String address;
	 
	 
	 /**
	  * CU_Type	
	  * 거래처 유형 
	  * 0.중량, 1.체적, 2.둘다, 3.기타, 4.모두, 7.체적건물, 8.체적사용자	
	  */
	 private String customerType;


	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("AREA_CODE", getAreaCode());
		keys.put("CID_Date", getCidDate());
		keys.put("CID_Date_Sno", getSequenceNumber());
		
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
	 * @return the cidDate
	 */
	public String getCidDate() {
		return cidDate;
	}

	/**
	 * @param cidDate the cidDate to set
	 */
	public void setCidDate(String cidDate) {
		this.cidDate = cidDate;
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
	 * @return the cidTime
	 */
	public String getCidTime() {
		return cidTime;
	}

	/**
	 * @param cidTime the cidTime to set
	 */
	public void setCidTime(String cidTime) {
		this.cidTime = cidTime;
	}

	/**
	 * @return the autoRegisterType
	 */
	public String getAutoRegisterType() {
		return autoRegisterType;
	}

	/**
	 * @param autoRegisterType the autoRegisterType to set
	 */
	public void setAutoRegisterType(String autoRegisterType) {
		this.autoRegisterType = autoRegisterType;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the saleTypeCode
	 */
	public String getSaleTypeCode() {
		return saleTypeCode;
	}

	/**
	 * @param saleTypeCode the saleTypeCode to set
	 */
	public void setSaleTypeCode(String saleTypeCode) {
		this.saleTypeCode = saleTypeCode;
	}

	/**
	 * @return the saleTypeName
	 */
	public String getSaleTypeName() {
		return saleTypeName;
	}

	/**
	 * @param saleTypeName the saleTypeName to set
	 */
	public void setSaleTypeName(String saleTypeName) {
		this.saleTypeName = saleTypeName;
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
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the saleQuantity
	 */
	public String getSaleQuantity() {
		return saleQuantity;
	}

	/**
	 * @param saleQuantity the saleQuantity to set
	 */
	public void setSaleQuantity(String saleQuantity) {
		this.saleQuantity = saleQuantity;
	}

	/**
	 * @return the withdrawQuantity
	 */
	public String getWithdrawQuantity() {
		return withdrawQuantity;
	}

	/**
	 * @param withdrawQuantity the withdrawQuantity to set
	 */
	public void setWithdrawQuantity(String withdrawQuantity) {
		this.withdrawQuantity = withdrawQuantity;
	}

	/**
	 * @return the cidPrice
	 */
	public String getCidPrice() {
		return cidPrice;
	}

	/**
	 * @param cidPrice the cidPrice to set
	 */
	public void setCidPrice(String cidPrice) {
		this.cidPrice = cidPrice;
	}

	/**
	 * @return the cidAmount
	 */
	public String getCidAmount() {
		return cidAmount;
	}

	/**
	 * @param cidAmount the cidAmount to set
	 */
	public void setCidAmount(String cidAmount) {
		this.cidAmount = cidAmount;
	}

	/**
	 * @return the discountAmount
	 */
	public String getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * @param discountAmount the discountAmount to set
	 */
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * @return the collectAmount
	 */
	public String getCollectAmount() {
		return collectAmount;
	}

	/**
	 * @param collectAmount the collectAmount to set
	 */
	public void setCollectAmount(String collectAmount) {
		this.collectAmount = collectAmount;
	}

	/**
	 * @return the unpaidAmount
	 */
	public String getUnpaidAmount() {
		return unpaidAmount;
	}

	/**
	 * @param unpaidAmount the unpaidAmount to set
	 */
	public void setUnpaidAmount(String unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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
	 * @return the deliveryYesNo
	 */
	public String getDeliveryYesNo() {
		return deliveryYesNo;
	}

	/**
	 * @param deliveryYesNo the deliveryYesNo to set
	 */
	public void setDeliveryYesNo(String deliveryYesNo) {
		this.deliveryYesNo = deliveryYesNo;
	}

	/**
	 * @return the completeYesNo
	 */
	public String getCompleteYesNo() {
		return completeYesNo;
	}

	/**
	 * @param completeYesNo the completeYesNo to set
	 */
	public void setCompleteYesNo(String completeYesNo) {
		this.completeYesNo = completeYesNo;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the vatType
	 */
	public String getVatType() {
		return vatType;
	}

	/**
	 * @param vatType the vatType to set
	 */
	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	/**
	 * @return the taxAmount
	 */
	public String getTaxAmount() {
		return taxAmount;
	}

	/**
	 * @param taxAmount the taxAmount to set
	 */
	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
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
	 * @return the collectType
	 */
	public String getCollectType() {
		return collectType;
	}

	/**
	 * @param collectType the collectType to set
	 */
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CidList [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode + ", cidDate=" + cidDate + ", sequenceNumber="
				+ sequenceNumber + ", cidTime=" + cidTime
				+ ", autoRegisterType=" + autoRegisterType + ", phoneNumber="
				+ phoneNumber + ", saleTypeCode=" + saleTypeCode
				+ ", saleTypeName=" + saleTypeName + ", customerCode="
				+ customerCode + ", customerName=" + customerName
				+ ", itemCode=" + itemCode + ", itemName=" + itemName
				+ ", saleQuantity=" + saleQuantity + ", withdrawQuantity="
				+ withdrawQuantity + ", cidPrice=" + cidPrice + ", vatType="
				+ vatType + ", cidAmount=" + cidAmount + ", taxAmount="
				+ taxAmount + ", totalAmount=" + totalAmount
				+ ", discountAmount=" + discountAmount + ", collectAmount="
				+ collectAmount + ", unpaidAmount=" + unpaidAmount
				+ ", employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", remark=" + remark + ", deliveryYesNo="
				+ deliveryYesNo + ", completeYesNo=" + completeYesNo
				+ ", collectType=" + collectType + ", address=" + address 
				+ ", customerType=" + customerType + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CidList><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode + "</areaCode><cidDate>" + cidDate
				+ "</cidDate><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><cidTime>" + cidTime
				+ "</cidTime><autoRegisterType>" + autoRegisterType
				+ "</autoRegisterType><phoneNumber>" + phoneNumber
				+ "</phoneNumber><saleTypeCode>" + saleTypeCode
				+ "</saleTypeCode><saleTypeName>" + saleTypeName
				+ "</saleTypeName><customerCode>" + customerCode
				+ "</customerCode><customerName><![CDATA[" + customerName
				+ "]]></customerName><itemCode>" + itemCode
				+ "</itemCode><itemName><![CDATA[" + itemName
				+ "]]></itemName><saleQuantity>" + saleQuantity
				+ "</saleQuantity><withdrawQuantity>" + withdrawQuantity
				+ "</withdrawQuantity><cidPrice>" + cidPrice
				+ "</cidPrice><vatType>" + vatType + "</vatType><cidAmount>"
				+ cidAmount + "</cidAmount><taxAmount>" + taxAmount
				+ "</taxAmount><totalAmount>" + totalAmount
				+ "</totalAmount><discountAmount>" + discountAmount
				+ "</discountAmount><collectAmount>" + collectAmount
				+ "</collectAmount><unpaidAmount>" + unpaidAmount
				+ "</unpaidAmount><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><remark><![CDATA[" + remark
				+ "]]></remark><deliveryYesNo>" + deliveryYesNo
				+ "</deliveryYesNo><completeYesNo>" + completeYesNo
				+ "</completeYesNo><collectType>" + collectType
				+ "</collectType><address><![CDATA[" + address 
				+ "]]></address><customerType>" + customerType 
				+ "</customerType></CidList>";
	}

}
