package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CollectList {
	/**
	 * SU_Gubun	
	 * 수금구분
	 */
	 private String collectClass;

	/**
	 * Area_Code	
	 * 영업소코드 - key
	 */
	 private String areaCode;

	/**
	 * S_Date	
	 * 수금일자 - key
	 */
	 private String collectDate;

	/**
	 * S_CU_Code	
	 * 거래처코드 - key
	 */
	 private String customerCode;

	/**
	 * S_Sno	
	 * 수금항번 - key
	 */
	 private String sequenceNumber;

	/**
	 * CU_Name_View	
	 * 거래처명
	 */
	 private String customerName;

	/**
	 * S_Kumack	
	 * 수금액
	 */
	 private String collectAmount;

	/**
	 * S_DC	
	 * DC금액
	 */
	 private String discountAmount;

	/**
	 * SW_Code	
	 * 수금사원코드
	 */
	 private String employeeCode;

	/**
	 * SW_Name	
	 * 사금사원명
	 */
	 private String employeeName;

	/**
	 * SukumType	
	 * 수금방법 	 코드
	 */
	 private String collectTypeCode;

	/**
	 * SU_NAME	
	 * 수금방법
	 */
	 private String collectTypeName;

	/**
	 * Bigo	
	 * 비고
	 */
	 private String remark;

	/**
	 * ins_Date	
	 * 등록일자
	 */
	 private String registerDate;

	/**
	 * User_ID 	
	 * 등록자명
	 */
	 private String registerName;
	 
	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("CU_CODE", getCustomerCode());
		 keys.put("S_DATE", getCollectDate());
		 keys.put("S_SNO", getSequenceNumber());
		 
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
	 * @return the collectClass
	 */
	public String getCollectClass() {
		return collectClass;
	}

	/**
	 * @param collectClass the collectClass to set
	 */
	public void setCollectClass(String collectClass) {
		this.collectClass = collectClass;
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
	 * @return the registerDate
	 */
	public String getRegisterDate() {
		return registerDate;
	}

	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * @return the registerName
	 */
	public String getRegisterName() {
		return registerName;
	}

	/**
	 * @param registerName the registerName to set
	 */
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectList [key=" + this.getKeyValue() + ", collectClass=" + collectClass + ", areaCode="
				+ areaCode + ", collectDate=" + collectDate + ", customerCode="
				+ customerCode + ", sequenceNumber=" + sequenceNumber
				+ ", customerName=" + customerName + ", collectAmount="
				+ collectAmount + ", discountAmount=" + discountAmount
				+ ", employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", collectTypeCode=" + collectTypeCode
				+ ", collectTypeName=" + collectTypeName + ", remark=" + remark
				+ ", registerDate=" + registerDate + ", registerName="
				+ registerName + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CollectList><key>" + this.getKeyValue()
				+ "</key><collectClass>" + collectClass
				+ "</collectClass><areaCode>" + areaCode
				+ "</areaCode><collectDate>" + collectDate
				+ "</collectDate><customerCode>" + customerCode
				+ "</customerCode><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><customerName><![CDATA[" + customerName
				+ "]]></customerName><collectAmount>" + collectAmount
				+ "</collectAmount><discountAmount>" + discountAmount
				+ "</discountAmount><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><collectTypeCode>" + collectTypeCode
				+ "</collectTypeCode><collectTypeName>" + collectTypeName
				+ "</collectTypeName><remark><![CDATA[" + remark
				+ "]]></remark><registerDate>" + registerDate
				+ "</registerDate><registerName><![CDATA[" + registerName
				+ "]]></registerName></CollectList>";
	}

}
