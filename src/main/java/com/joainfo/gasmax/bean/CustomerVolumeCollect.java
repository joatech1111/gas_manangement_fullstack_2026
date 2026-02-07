package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 체적 장부 - 수금 내역
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeCollect {
	/**
	 * AREA_CODE	
	 * 영업소코드
	 */
	 private String areaCode;

	/**
	 * GS_CU_CODE	
	 * 거래처코드
	 */
	 private String customerCode;

	/**
	 * GS_DATE	
	 * 일자
	 */
	 private String collectDate;

	/**
	 * GS_SNO	
	 * 항번
	 */
	 private String sequenceNumber;

	/**
	 * GS_SUKUMTYPE	
	 * 수금방법코드
	 */
	 private String collectTypeCode;

	/**
	 * Su_Name	
	 * 수금방법
	 */
	 private String collectTypeName;

	/**
	 * GS_KUMACK	
	 * 수금액
	 */
	 private String collectAmount;

	/**
	 * GS_DC	
	 * D/C
	 */
	 private String discountAmount;

	/**
	 * GS_SW_CODE	
	 * 사원코드
	 */
	 private String employeeCode;

	/**
	 * GS_SW_NAME	
	 * 사원명
	 */
	 private String employeeName;

	/**
	 * GS_BIGO	
	 * 비고
	 */
	 private String remark;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("GS_CU_CODE", getCustomerCode());
		 keys.put("GS_DATE", getCollectDate());
		 keys.put("GS_SNO", getSequenceNumber());
		 
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
	 * @return the collectTypeCode
	 */
	public String getCollectTypeCode() {
		return collectTypeCode;
	}

	/**
	 * @param collectTypeCode the collectTypeCode to set
	 */
	public void setCollectTypeCode(String collectTypeCode) {
		this.collectTypeCode = collectTypeCode;
	}

	/**
	 * @return the collectTypeName
	 */
	public String getCollectTypeName() {
		return collectTypeName;
	}

	/**
	 * @param collectTypeName the collectTypeName to set
	 */
	public void setCollectTypeName(String collectTypeName) {
		this.collectTypeName = collectTypeName;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerVolumeCollect [key=" + this.getKeyValue() + ", areaCode=" + areaCode
				+ ", customerCode=" + customerCode + ", collectDate="
				+ collectDate + ", sequenceNumber=" + sequenceNumber
				+ ", collectTypeCode=" + collectTypeCode + ", collectTypeName="
				+ collectTypeName + ", collectAmount=" + collectAmount
				+ ", discountAmount=" + discountAmount + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName + ", remark="
				+ remark + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerVolumeCollect><key>" + this.getKeyValue()
				+ "</key><areaCode>" + areaCode
				+ "</areaCode><customerCode>" + customerCode
				+ "</customerCode><collectDate>" + collectDate
				+ "</collectDate><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><collectTypeCode>" + collectTypeCode
				+ "</collectTypeCode><collectTypeName>" + collectTypeName
				+ "</collectTypeName><collectAmount>" + collectAmount
				+ "</collectAmount><discountAmount>" + discountAmount
				+ "</discountAmount><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><remark><![CDATA[" + remark
				+ "]]></remark></CustomerVolumeCollect>";
	}
	 
}
