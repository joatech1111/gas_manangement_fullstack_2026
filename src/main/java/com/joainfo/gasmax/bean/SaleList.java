package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 판매 현황 정보 객체
 * @author 백원태
 * @version 1.0
 */
public class SaleList {
	
	/**
	 * Sale_Type	판매구분
	 */
	private String saleType;

	/**
	 * AREA_CODE	영업소코드 - key
	 */
	private String areaCode;

	/**
	 * PJ_Date	판매일자 - key
	 */
	private String saleDate;

	/**
	 * PJ_Sno	판매항번 - key
	 */
	private String sequenceNumber;

	/**
	 * PJ_CU_Code	거래처코드 - key
	 */
	private String customerCode;

	/**
	 * PJ_CU_Name	거래처명
	 */
	private String customerName;

	/**
	 * PJ_CU_Site	현장/비고
	 */
	private String remark;

	/**
	 * JP_Name	품명
	 */
	private String itemName;

	/**
	 * PJ_QTY	납품수량
	 */
	private String saleQuantity;

	/**
	 * PJ_REQTY	회수수량
	 */
	private String withdrawQuantity;

	/**
	 * PJ_REQTY_SIL	실병회수수량
	 */
	private String containerWithdrawQuantity;

	/**
	 * PJ_VAT_Div	Vat구분
	 */
	private String vatType;

	/**
	 * PJ_Danga	판매단가
	 */
	private String salePrice;

	/**
	 * PJ_Kum_Sup	공급액
	 */
	private String supplyAmount;

	/**
	 * PJ_Kum_Vat	세액
	 */
	private String taxAmount;

	/**
	 * PJ_Kumack	판매금액
	 */
	private String saleAmount;

	/**
	 * PJ_DC	D/C
	 */
	private String discountAmount;

	/**
	 * PJ_INKum	입금액
	 */
	private String collectAmount;

	/**
	 * PJ_MISUKUM	미수금액
	 */
	private String unpaidAmount;

	/**
	 * PJ_SW_Code	사원코드
	 */
	private String employeeCode;

	/**
	 * PJ_SW_Name	사원명
	 */
	private String employeeName;

	/**
	 * PJ_InKumType	입금구분
	 */
	 private String collectType;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("CU_CODE", getCustomerCode());
		 keys.put("PJ_DATE", getSaleDate());
		 keys.put("PJ_SNO", getSequenceNumber());
		 
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
	 * @return the saleType
	 */
	public String getSaleType() {
		return saleType;
	}

	/**
	 * @param saleType the saleType to set
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
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
	 * @return the saleDate
	 */
	public String getSaleDate() {
		return saleDate;
	}

	/**
	 * @param saleDate the saleDate to set
	 */
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
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
	 * @return the containerWithdrawQuantity
	 */
	public String getContainerWithdrawQuantity() {
		return containerWithdrawQuantity;
	}

	/**
	 * @param containerWithdrawQuantity the containerWithdrawQuantity to set
	 */
	public void setContainerWithdrawQuantity(String containerWithdrawQuantity) {
		this.containerWithdrawQuantity = containerWithdrawQuantity;
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
	 * @return the salePrice
	 */
	public String getSalePrice() {
		return salePrice;
	}

	/**
	 * @param salePrice the salePrice to set
	 */
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @return the supplyAmount
	 */
	public String getSupplyAmount() {
		return supplyAmount;
	}

	/**
	 * @param supplyAmount the supplyAmount to set
	 */
	public void setSupplyAmount(String supplyAmount) {
		this.supplyAmount = supplyAmount;
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
	 * @return the saleAmount
	 */
	public String getSaleAmount() {
		return saleAmount;
	}

	/**
	 * @param saleAmount the saleAmount to set
	 */
	public void setSaleAmount(String saleAmount) {
		this.saleAmount = saleAmount;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SaleList [key=" + this.getKeyValue() + ", saleType=" + saleType + ", areaCode=" + areaCode
				+ ", saleDate=" + saleDate + ", sequenceNumber="
				+ sequenceNumber + ", customerCode=" + customerCode
				+ ", customerName=" + customerName + ", remark=" + remark
				+ ", itemName=" + itemName + ", saleQuantity=" + saleQuantity
				+ ", withdrawQuantity=" + withdrawQuantity
				+ ", containerWithdrawQuantity=" + containerWithdrawQuantity
				+ ", vatType=" + vatType + ", salePrice=" + salePrice
				+ ", supplyAmount=" + supplyAmount + ", taxAmount=" + taxAmount
				+ ", saleAmount=" + saleAmount + ", discountAmount="
				+ discountAmount + ", collectAmount=" + collectAmount
				+ ", unpaidAmount=" + unpaidAmount + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName
				+ ", collectType=" + collectType + "]";
	}
	
	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<SaleList><key>" + this.getKeyValue()
				+ "</key><saleType>" + saleType + "</saleType><areaCode>"
				+ areaCode + "</areaCode><saleDate>" + saleDate
				+ "</saleDate><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><customerCode>" + customerCode
				+ "</customerCode><customerName><![CDATA[" + customerName
				+ "]]></customerName><remark><![CDATA[" + remark + "]]></remark><itemName>"
				+ itemName + "</itemName><saleQuantity>" + saleQuantity
				+ "</saleQuantity><withdrawQuantity>" + withdrawQuantity
				+ "</withdrawQuantity><containerWithdrawQuantity>"
				+ containerWithdrawQuantity
				+ "</containerWithdrawQuantity><vatType>" + vatType
				+ "</vatType><salePrice>" + salePrice
				+ "</salePrice><supplyAmount>" + supplyAmount
				+ "</supplyAmount><taxAmount>" + taxAmount
				+ "</taxAmount><saleAmount>" + saleAmount
				+ "</saleAmount><discountAmount>" + discountAmount
				+ "</discountAmount><collectAmount>" + collectAmount
				+ "</collectAmount><unpaidAmount>" + unpaidAmount
				+ "</unpaidAmount><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><collectType>" + collectType
				+ "</collectType></SaleList>";
	}

}
