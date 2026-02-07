package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerSaftyCheckList {
	/**
	 * Check_TYPE
	 * 점검구분	Key	
	 */
	 private String checkType;
	 
	/**
	 * Check_Name
	 * 점검구분명	Key	
	 */
	 private String checkName;
	 
	/**
	 * Area_Code	
	 * 영업소코드	Key	
	 */
	 private String areaCode;

	/**
	 * ANZ_Cu_Code	
	 * 거래처코드	Key	
	 */
	 private String customerCode;

	/**
	 * ANZ_Sno	
	 * 항번	Key	
	 */
	 private String sequenceNumber;

	/**
	 * ANZ_Date	
	 * 정기점검일		
	 */
	 private String scheduledCheckDate;

	/**
	 * ANZ_SW_Code	
	 * 점검 사원코드		
	 */
	 private String employeeCode;

	/**
	 * ANZ_SW_Name	
	 * 점검 사원명		
	 */
	 private String employeeName;

	/**
	 * ANZ_01	
	 * 시설현황	가	0, 1.적합, 2.부적합
	 */
	 private String acceptable1;

	/**
	 * ANZ_02		
	 * 나	0, 1.적합, 2.부적합
	 */
	 private String acceptable2;

	/**
	 * ANZ_03		
	 * 다	0, 1.적합, 2.부적합
	 */
	 private String acceptable3;

	/**
	 * ANZ_04	
	 * 라	0, 1.적합, 2.부적합
	 */
	 private String acceptable4;

	/**
	 * ANZ_05
	 * 마	0, 1.적합, 2.부적합
	 */
	 private String acceptable5;

	/**
	 * ANZ_06
	 * 바	0, 1.적합, 2.부적합
	 */
	 private String acceptable6;

	/**
	 * ANZ_07
	 * 사	0, 1.적합, 2.부적합
	 */
	 private String acceptable7;

	/**
	 * ANZ_08
	 * 아	0, 1.적합, 2.부적합
	 */
	 private String acceptable8;

	/**
	 * ANZ_09
	 * 자	0, 1.적합, 2.부적합
	 */
	 private String acceptable9;

	/**
	 * ANZ_10
	 * 차	0, 1.적합, 2.부적합
	 */
	 private String acceptable10;

	/**
	 * ANZ_11
	 * 카	0, 1.적합, 2.부적합
	 */
	 private String acceptable11;
	 
	/**
	 * ANZ_12
	 * 타	0, 1.적합, 2.부적합
	 */
	 private String acceptable12;

	/**
	 * ANZ_Date_Time	
	 * 등록/수정일자		
	 */
	 private String modifyDate;
	 
	/**	
	 * ANZ_Sign_YN
	 * 서명여부
	 */
	 private String signatureYn;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("ANZ_Cu_Code", getCustomerCode());
		 keys.put("ANZ_Sno", getSequenceNumber());
		 
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
	 * @return the checkType
	 */
	public String getCheckType() {
		return checkType;
	}

	/**
	 * @param checkType the checkType to set
	 */
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
	/**
	 * @return the checkName
	 */
	public String getCheckName() {
		return checkName;
	}

	/**
	 * @param checkName the checkName to set
	 */
	public void setCheckName(String checkName) {
		this.checkName = checkName;
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
	 * @return the scheduledCheckDate
	 */
	public String getScheduledCheckDate() {
		return scheduledCheckDate;
	}

	/**
	 * @param scheduledCheckDate the scheduledCheckDate to set
	 */
	public void setScheduledCheckDate(String scheduledCheckDate) {
		this.scheduledCheckDate = scheduledCheckDate;
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
	 * @return the acceptable1
	 */
	public String getAcceptable1() {
		return acceptable1;
	}

	/**
	 * @param acceptable1 the acceptable1 to set
	 */
	public void setAcceptable1(String acceptable1) {
		this.acceptable1 = acceptable1;
	}

	/**
	 * @return the acceptable2
	 */
	public String getAcceptable2() {
		return acceptable2;
	}

	/**
	 * @param acceptable2 the acceptable2 to set
	 */
	public void setAcceptable2(String acceptable2) {
		this.acceptable2 = acceptable2;
	}

	/**
	 * @return the acceptable3
	 */
	public String getAcceptable3() {
		return acceptable3;
	}

	/**
	 * @param acceptable3 the acceptable3 to set
	 */
	public void setAcceptable3(String acceptable3) {
		this.acceptable3 = acceptable3;
	}

	/**
	 * @return the acceptable4
	 */
	public String getAcceptable4() {
		return acceptable4;
	}

	/**
	 * @param acceptable4 the acceptable4 to set
	 */
	public void setAcceptable4(String acceptable4) {
		this.acceptable4 = acceptable4;
	}

	/**
	 * @return the acceptable5
	 */
	public String getAcceptable5() {
		return acceptable5;
	}

	/**
	 * @param acceptable5 the acceptable5 to set
	 */
	public void setAcceptable5(String acceptable5) {
		this.acceptable5 = acceptable5;
	}

	/**
	 * @return the acceptable6
	 */
	public String getAcceptable6() {
		return acceptable6;
	}

	/**
	 * @param acceptable6 the acceptable6 to set
	 */
	public void setAcceptable6(String acceptable6) {
		this.acceptable6 = acceptable6;
	}

	/**
	 * @return the acceptable7
	 */
	public String getAcceptable7() {
		return acceptable7;
	}

	/**
	 * @param acceptable7 the acceptable7 to set
	 */
	public void setAcceptable7(String acceptable7) {
		this.acceptable7 = acceptable7;
	}

	/**
	 * @return the acceptable8
	 */
	public String getAcceptable8() {
		return acceptable8;
	}

	/**
	 * @param acceptable8 the acceptable8 to set
	 */
	public void setAcceptable8(String acceptable8) {
		this.acceptable8 = acceptable8;
	}

	/**
	 * @return the acceptable9
	 */
	public String getAcceptable9() {
		return acceptable9;
	}

	/**
	 * @param acceptable9 the acceptable9 to set
	 */
	public void setAcceptable9(String acceptable9) {
		this.acceptable9 = acceptable9;
	}

	/**
	 * @return the acceptable10
	 */
	public String getAcceptable10() {
		return acceptable10;
	}

	/**
	 * @param acceptable10 the acceptable10 to set
	 */
	public void setAcceptable10(String acceptable10) {
		this.acceptable10 = acceptable10;
	}

	/**
	 * @return the acceptable11
	 */
	public String getAcceptable11() {
		return acceptable11;
	}

	/**
	 * @param acceptable11 the acceptable11 to set
	 */
	public void setAcceptable11(String acceptable11) {
		this.acceptable11 = acceptable11;
	}
	
	/**
	 * @return the acceptable12
	 */
	public String getAcceptable12() {
		return acceptable12;
	}

	/**
	 * @param acceptable12 the acceptable12 to set
	 */
	public void setAcceptable12(String acceptable12) {
		this.acceptable12 = acceptable12;
	}

	/**
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @return the signatureYn
	 */
	public String getSignatureYn() {
		return signatureYn;
	}

	/**
	 * @param signatureYn the signatureYn to set
	 */
	public void setSignatureYn(String signatureYn) {
		this.signatureYn = signatureYn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerSaftyCheck [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", sequenceNumber="
				+ sequenceNumber
				+ ", checkType="
				+ checkType
				+ ", checkName="
				+ checkName
				+ ", scheduledCheckDate="
				+ scheduledCheckDate
				+ ", employeeCode="
				+ employeeCode
				+ ", employeeName="
				+ employeeName
				+ ", acceptable1="
				+ acceptable1
				+ ", acceptable2="
				+ acceptable2
				+ ", acceptable3="
				+ acceptable3
				+ ", acceptable4="
				+ acceptable4
				+ ", acceptable5="
				+ acceptable5
				+ ", acceptable6="
				+ acceptable6
				+ ", acceptable7="
				+ acceptable7
				+ ", acceptable8="
				+ acceptable8
				+ ", acceptable9="
				+ acceptable9
				+ ", acceptable10="
				+ acceptable10
				+ ", acceptable11="
				+ acceptable11
				+ ", acceptable12="
				+ acceptable12
				+ ", signatureYn="
				+ signatureYn 
				+ ", modifyDate=" + modifyDate + "]";
	}

	/**
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerSaftyCheckList><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><checkType>"
				+ checkType
				+ "</checkType><checkName>"
				+ checkName
				+ "</checkName><scheduledCheckDate>"
				+ scheduledCheckDate
				+ "</scheduledCheckDate><employeeCode>"
				+ employeeCode
				+ "</employeeCode><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><acceptable1>"
				+ acceptable1
				+ "</acceptable1><acceptable2>"
				+ acceptable2
				+ "</acceptable2><acceptable3>"
				+ acceptable3
				+ "</acceptable3><acceptable4>"
				+ acceptable4
				+ "</acceptable4><acceptable5>"
				+ acceptable5
				+ "</acceptable5><acceptable6>"
				+ acceptable6
				+ "</acceptable6><acceptable7>"
				+ acceptable7
				+ "</acceptable7><acceptable8>"
				+ acceptable8
				+ "</acceptable8><acceptable9>"
				+ acceptable9
				+ "</acceptable9><acceptable10>"
				+ acceptable10
				+ "</acceptable10><acceptable11>"
				+ acceptable11
				+ "</acceptable11><acceptable12>"
				+ acceptable12
				+ "</acceptable12><signatureYn>"
				+ signatureYn
				+ "</signatureYn><modifyDate>"
				+ modifyDate
				+ "</modifyDate></CustomerSaftyCheckList>";
	}
}
