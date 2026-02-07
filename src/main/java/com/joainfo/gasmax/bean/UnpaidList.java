package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 미수현황
 * @author 백원태
 * @version 1.0
 */
public class UnpaidList {
	
	/**
	 * 영업소 코드 - key
	 * AREA_CODE 
	 */
	private String areaCode;

	/**
	 * 거래처 코드 - key
	 * CU_CODE 
	 */
	private String customerCode;

	/**
	 * 미수 유형 명칭
	 * MISU_GUBUN 
	 */
	private String unpaidTypeName;

	/**
	 * 거래처 명
	 * CU_NAME_VIEW 
	 */
	private String customerName;

	/**
	 * 주소1
	 * CU_ADDR1 
	 */
	private String address1;

	/**
	 * 주소2
	 * CU_ADDR2 
	 */
	private String address2;

	/**
	 * 사원 코드
	 * CU_SW_CODE 
	 */
	private String employeeCode;

	/**
	 * 사원 명
	 * SW_NAME 
	 */
	private String employeeName;

	/**
	 * 전화번호
	 * CU_TEL 
	 */
	private String phoneNumber;

	/**
	 * 휴대전화 번호
	 * CU_HP 
	 */
	private String mobileNumber;

	/**
	 * 일반(중량) 미수
	 * CU_JMISU 
	 */
	private String weightUnpaid;

	/**
	 * 체적 미수
	 * CU_CMISU 
	 */
	private String volumeUnpaid;

	/**
	 * 수금 방법 코드
	 * CU_SUKUMTYPE 
	 */
	private String collectTypeCode;

	/**
	 * 수금 방법 명
	 * SU_NAME 
	 */
	private String collectTypeName;

	/**
	 * 조회 순번
	 */
	private String sequenceNumber;
	
	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("AREA_CODE", getAreaCode());
		keys.put("CU_CODE", getCustomerCode());
		
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
	 * @return the unpaidTypeName
	 */
	public String getUnpaidTypeName() {
		return unpaidTypeName;
	}

	/**
	 * @param unpaidTypeName the unpaidTypeName to set
	 */
	public void setUnpaidTypeName(String unpaidTypeName) {
		this.unpaidTypeName = unpaidTypeName;
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
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the weightUnpaid
	 */
	public String getWeightUnpaid() {
		return weightUnpaid;
	}

	/**
	 * @param weightUnpaid the weightUnpaid to set
	 */
	public void setWeightUnpaid(String weightUnpaid) {
		this.weightUnpaid = weightUnpaid;
	}

	/**
	 * @return the volumeUnpaid
	 */
	public String getVolumeUnpaid() {
		return volumeUnpaid;
	}

	/**
	 * @param volumeUnpaid the volumeUnpaid to set
	 */
	public void setVolumeUnpaid(String volumeUnpaid) {
		this.volumeUnpaid = volumeUnpaid;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UnpaidList [key=" + this.getKeyValue() + ", areaCode=" + areaCode + ", customerCode="
				+ customerCode + ", unpaidTypeName=" + unpaidTypeName
				+ ", customerName=" + customerName + ", address1=" + address1
				+ ", address2=" + address2 + ", employeeCode=" + employeeCode
				+ ", employeeName=" + employeeName + ", phoneNumber="
				+ phoneNumber + ", mobileNumber=" + mobileNumber
				+ ", weightUnpaid=" + weightUnpaid + ", volumeUnpaid="
				+ volumeUnpaid + ", collectTypeCode=" + collectTypeCode
				+ ", collectTypeName=" + collectTypeName 
				+ ", sequenceNumber=" + sequenceNumber 
				+ "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<UnpaidList><key>" + this.getKeyValue()
				+ "</key><areaCode>" + areaCode
				+ "</areaCode><customerCode>" + customerCode
				+ "</customerCode><unpaidTypeName>" + unpaidTypeName
				+ "</unpaidTypeName><customerName><![CDATA[" + customerName
				+ "]]></customerName><address1><![CDATA[" + address1
				+ "]]></address1><address2><![CDATA[" + address2
				+ "]]></address2><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><phoneNumber>" + phoneNumber
				+ "</phoneNumber><mobileNumber>" + mobileNumber
				+ "</mobileNumber><weightUnpaid>" + weightUnpaid
				+ "</weightUnpaid><volumeUnpaid>" + volumeUnpaid
				+ "</volumeUnpaid><collectTypeCode>" + collectTypeCode
				+ "</collectTypeCode><collectTypeName>" + collectTypeName
				+ "</collectTypeName><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber></UnpaidList>";
	}

}
