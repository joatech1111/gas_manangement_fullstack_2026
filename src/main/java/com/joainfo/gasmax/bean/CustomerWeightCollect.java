package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 일반장부 거래(수금) 내역 정보모델
 * @author 백원태
 * @version 1.0
 */
public class CustomerWeightCollect {
	/**
	 * AREA_CODE	
	 * 영업소코드 - key
	 */
	 private String areaCode;

	/**
	 * CU_CODE	
	 * 거래처코드 - key
	 */
	 private String customerCode;

	/**
	 * TR_GU	
	 * 구분 - key
	 * 0.가스, 1.용기, 2.기구, 4.A/S, 5.수금 
	 */
	 private String typeCode;

	/**
	 * TR_Date	
	 * 일자 - key
	 */
	 private String collectDate;

	/**
	 * TR_Sno	
	 * 판매항번 - key
	 */
	 private String sequenceNumber;

	/**
	 * JP_CODE	
	 * 품목코드
	 */
	 private String itemCode;

	/**
	 * JP_Name	
	 * 품명
	 */
	 private String itemName;

	/**
	 * PJ_QTY	
	 * 납품
	 */
	 private String saleQuantity;

	/**
	 * PJ_REQTY	
	 * 회수
	 */
	 private String withdrawQuantity;

	/**
	 * TR_Danga	
	 * 단가
	 */
	 private String price;

	/**
	 * TR_Sup	
	 * 공급액
	 */
	 private String amount;

	/**
	 * TR_Vat	
	 * 세액
	 */
	 private String tax;

	/**
	 * TR_KUM	
	 * 합계
	 */
	 private String totalAmount;

	/**
	 * TR_IN	
	 * 입금액
	 */
	 private String collectAmount;

	/**
	 * TR_DC	
	 * DC
	 */
	 private String discountAmount;

	/**
	 * TR_MISU	
	 * 미입금액
	 */
	 private String unpaidAmount;

	/**
	 * SW_CODE	
	 * 사원코드
	 */
	 private String employeeCode;

	/**
	 * SW_Name	
	 * 사원명
	 */
	 private String employeeName;

	/**
	 * TR_NAME	
	 * 비고현장명
	 */
	 private String remark;

	/**
	 * TR_InKumType	
	 * 입금구분 0현금,2예금,3카드,4어음,A외상,B현금영수증
	 */
	 private String collectType;
	 
	 /**
	  * PJ_TS_SNO	
	  * 용기입출항번
	  */
	  private String equipmentInOutSequenceNumber;

	 /**
	  * PJ_PAP_NO	
	  * 미수항번
	  */
	  private String unpaidSequenceNumber;


	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("CU_CODE", getCustomerCode());
		 keys.put("TR_GU", getTypeCode());
		 keys.put("TR_DATE", getCollectDate());
		 keys.put("TR_SNO", getSequenceNumber());
		 
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
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @return the collectDate
	 */
	public String getCollectDate() {
		return collectDate;
	}

	/**
	 * @param collectDate the collectDate to set
	 */
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
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
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
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
	 * @return the equipmentInOutSequenceNumber
	 */
	public String getEquipmentInOutSequenceNumber() {
		return equipmentInOutSequenceNumber;
	}

	/**
	 * @param equipmentInOutSequenceNumber the equipmentInOutSequenceNumber to set
	 */
	public void setEquipmentInOutSequenceNumber(String equipmentInOutSequenceNumber) {
		this.equipmentInOutSequenceNumber = equipmentInOutSequenceNumber;
	}

	/**
	 * @return the unpaidSequenceNumber
	 */
	public String getUnpaidSequenceNumber() {
		return unpaidSequenceNumber;
	}

	/**
	 * @param unpaidSequenceNumber the unpaidSequenceNumber to set
	 */
	public void setUnpaidSequenceNumber(String unpaidSequenceNumber) {
		this.unpaidSequenceNumber = unpaidSequenceNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerWeightCollect [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", typeCode="
				+ typeCode
				+ ", collectDate="
				+ collectDate
				+ ", sequenceNumber="
				+ sequenceNumber
				+ ", itemCode="
				+ itemCode
				+ ", itemName="
				+ itemName
				+ ", saleQuantity="
				+ saleQuantity
				+ ", withdrawQuantity="
				+ withdrawQuantity
				+ ", price="
				+ price
				+ ", amount="
				+ amount
				+ ", tax="
				+ tax
				+ ", totalAmount="
				+ totalAmount
				+ ", collectAmount="
				+ collectAmount
				+ ", discountAmount="
				+ discountAmount
				+ ", unpaidAmount="
				+ unpaidAmount
				+ ", employeeCode="
				+ employeeCode
				+ ", employeeName="
				+ employeeName
				+ ", remark="
				+ remark
				+ ", collectType="
				+ collectType
				+ ", equipmentInOutSequenceNumber="
				+ equipmentInOutSequenceNumber
				+ ", unpaidSequenceNumber="
				+ unpaidSequenceNumber + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerWeightCollect><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><typeCode>"
				+ typeCode
				+ "</typeCode><collectDate>"
				+ collectDate
				+ "</collectDate><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><saleQuantity>"
				+ saleQuantity
				+ "</saleQuantity><withdrawQuantity>"
				+ withdrawQuantity
				+ "</withdrawQuantity><price>"
				+ price
				+ "</price><amount>"
				+ amount
				+ "</amount><tax>"
				+ tax
				+ "</tax><totalAmount>"
				+ totalAmount
				+ "</totalAmount><collectAmount>"
				+ collectAmount
				+ "</collectAmount><discountAmount>"
				+ discountAmount
				+ "</discountAmount><unpaidAmount>"
				+ unpaidAmount
				+ "</unpaidAmount><employeeCode>"
				+ employeeCode
				+ "</employeeCode><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><remark><![CDATA["
				+ remark
				+ "]]></remark><collectType>"
				+ collectType
				+ "</collectType><equipmentInOutSequenceNumber>"
				+ equipmentInOutSequenceNumber
				+ "</equipmentInOutSequenceNumber><unpaidSequenceNumber>"
				+ unpaidSequenceNumber
				+ "</unpaidSequenceNumber></CustomerWeightCollect>";
	}

}
