package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerSaftyCheckTank {
	/**
	 * Area_Code 영업소코드 Key
	 */
	private String areaCode;

	/**
	 * ANZ_Cu_Code 거래처코드 Key
	 */
	private String customerCode;

	/**
	 * ANZ_Sno 항번 Key
	 */
	private String sequenceNumber;

	/**
	 * ANZ_Date 정기점검일
	 */
	private String scheduledCheckDate;

	/**
	 * ANZ_SW_Code 점검 사원코드
	 */
	private String employeeCode;

	/**
	 * ANZ_SW_Name 점검 사원명
	 */
	private String employeeName;

	/**
	 * ANZ_TANK_KG_01 탱크용량1
	 */
	private String tankCapacity1;

	/**
	 * ANZ_TANK_KG_01 탱크용량1
	 */
	private String tankCapacity2;

	/**
	 * ANZ_TANK_01 0, 1.적합, 2.부적합
	 */
	private String acceptable1;

	/**
	 * ANZ_TANK_01_Bigo
	 */
	private String acceptable1Comment;
	
	/**
	 * ANZ_TANK_02 0, 1.적합, 2.부적합
	 */
	private String acceptable2;

	/**
	 * ANZ_TANK_02_Bigo
	 */
	private String acceptable2Comment;
	
	/**
	 * ANZ_TANK_03 0, 1.적합, 2.부적합
	 */
	private String acceptable3;

	/**
	 * ANZ_TANK_03_Bigo
	 */
	private String acceptable3Comment;
	
	/**
	 * ANZ_TANK_04 0, 1.적합, 2.부적합
	 */
	private String acceptable4;

	/**
	 * ANZ_TANK_04_Bigo
	 */
	private String acceptable4Comment;
	
	/**
	 * ANZ_TANK_05 0, 1.적합, 2.부적합
	 */
	private String acceptable5;

	/**
	 * ANZ_TANK_05_Bigo
	 */
	private String acceptable5Comment;
	
	/**
	 * ANZ_TANK_06 0, 1.적합, 2.부적합
	 */
	private String acceptable6;

	/**
	 * ANZ_TANK_06_Bigo
	 */
	private String acceptable6Comment;
	
	/**
	 * ANZ_TANK_07 0, 1.적합, 2.부적합
	 */
	private String acceptable7;

	/**
	 * ANZ_TANK_07_Bigo
	 */
	private String acceptable7Comment;
	
	/**
	 * ANZ_TANK_08 0, 1.적합, 2.부적합
	 */
	private String acceptable8;

	/**
	 * ANZ_TANK_08_Bigo
	 */
	private String acceptable8Comment;
	
	/**
	 * ANZ_TANK_09 0, 1.적합, 2.부적합
	 */
	private String acceptable9;

	/**
	 * ANZ_TANK_09_Bigo
	 */
	private String acceptable9Comment;
	
	/**
	 * ANZ_Check_item_10
	 */
	private String acceptable10Content;
	
	/**
	 * ANZ_TANK_10 0, 1.적합, 2.부적합
	 */
	private String acceptable10;

	/**
	 * ANZ_TANK_10_Bigo
	 */
	private String acceptable10Comment;
	
	/**
	 * ANZ_Check_item_11
	 */
	private String acceptable11Content;
	
	/**
	 * ANZ_TANK_11 0, 1.적합, 2.부적합
	 */
	private String acceptable11;

	/**
	 * ANZ_TANK_11_Bigo
	 */
	private String acceptable11Comment;
	
	/**
	 * ANZ_Check_item_12
	 */
	private String acceptable12Content;
	
	/**
	 * ANZ_TANK_12 0, 1.적합, 2.부적합
	 */
	private String acceptable12;

	/**
	 * ANZ_TANK_12_Bigo
	 */
	private String acceptable12Comment;

	/**
	 * ANZ_TANK_SW_Bigo1 점검자의견 내용1
	 */
	private String employeeComment1;

	/**
	 * ANZ_TANK_SW_Bigo1 점검자의견 내용2
	 */
	private String employeeComment2;

	/**
	 * ANZ_CustName 업소확인자
	 */
	private String customerName;

	/**
	 * ANZ_Sign_YN 서명유무(Y/N)
	 */
	private String signatureYn;
	
	/**
	 * 서명이미지
	 */
	private String signatureImage;

	/**
	 * ANZ_User_ID APP 사용자명
	 */
	private String userId;

	/**
	 * ANZ_Date_Time 등록/수정일자
	 */
	private String modifyDate;

	/**
	 * key map 반환
	 * 
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap() {
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("AREA_CODE", getAreaCode());
		keys.put("ANZ_Cu_Code", getCustomerCode());
		keys.put("ANZ_Sno", getSequenceNumber());

		return keys;
	}

	/**
	 * key 값 반환
	 * 
	 * @return
	 */
	public String getKeyValue() {
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
	 * @return the tankCapacity1
	 */
	public String getTankCapacity1() {
		return tankCapacity1;
	}

	/**
	 * @param tankCapacity1 the tankCapacity1 to set
	 */
	public void setTankCapacity1(String tankCapacity1) {
		this.tankCapacity1 = tankCapacity1;
	}

	/**
	 * @return the tankCapacity2
	 */
	public String getTankCapacity2() {
		return tankCapacity2;
	}

	/**
	 * @param tankCapacity2 the tankCapacity2 to set
	 */
	public void setTankCapacity2(String tankCapacity2) {
		this.tankCapacity2 = tankCapacity2;
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
	 * @return the acceptable1Comment
	 */
	public String getAcceptable1Comment() {
		return acceptable1Comment;
	}

	/**
	 * @param acceptable1Comment the acceptable1Comment to set
	 */
	public void setAcceptable1Comment(String acceptable1Comment) {
		this.acceptable1Comment = acceptable1Comment;
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
	 * @return the acceptable2Comment
	 */
	public String getAcceptable2Comment() {
		return acceptable2Comment;
	}

	/**
	 * @param acceptable2Comment the acceptable2Comment to set
	 */
	public void setAcceptable2Comment(String acceptable2Comment) {
		this.acceptable2Comment = acceptable2Comment;
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
	 * @return the acceptable3Comment
	 */
	public String getAcceptable3Comment() {
		return acceptable3Comment;
	}

	/**
	 * @param acceptable3Comment the acceptable3Comment to set
	 */
	public void setAcceptable3Comment(String acceptable3Comment) {
		this.acceptable3Comment = acceptable3Comment;
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
	 * @return the acceptable4Comment
	 */
	public String getAcceptable4Comment() {
		return acceptable4Comment;
	}

	/**
	 * @param acceptable4Comment the acceptable4Comment to set
	 */
	public void setAcceptable4Comment(String acceptable4Comment) {
		this.acceptable4Comment = acceptable4Comment;
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
	 * @return the acceptable5Comment
	 */
	public String getAcceptable5Comment() {
		return acceptable5Comment;
	}

	/**
	 * @param acceptable5Comment the acceptable5Comment to set
	 */
	public void setAcceptable5Comment(String acceptable5Comment) {
		this.acceptable5Comment = acceptable5Comment;
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
	 * @return the acceptable6Comment
	 */
	public String getAcceptable6Comment() {
		return acceptable6Comment;
	}

	/**
	 * @param acceptable6Comment the acceptable6Comment to set
	 */
	public void setAcceptable6Comment(String acceptable6Comment) {
		this.acceptable6Comment = acceptable6Comment;
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
	 * @return the acceptable7Comment
	 */
	public String getAcceptable7Comment() {
		return acceptable7Comment;
	}

	/**
	 * @param acceptable7Comment the acceptable7Comment to set
	 */
	public void setAcceptable7Comment(String acceptable7Comment) {
		this.acceptable7Comment = acceptable7Comment;
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
	 * @return the acceptable8Comment
	 */
	public String getAcceptable8Comment() {
		return acceptable8Comment;
	}

	/**
	 * @param acceptable8Comment the acceptable8Comment to set
	 */
	public void setAcceptable8Comment(String acceptable8Comment) {
		this.acceptable8Comment = acceptable8Comment;
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
	 * @return the acceptable9Comment
	 */
	public String getAcceptable9Comment() {
		return acceptable9Comment;
	}

	/**
	 * @param acceptable9Comment the acceptable9Comment to set
	 */
	public void setAcceptable9Comment(String acceptable9Comment) {
		this.acceptable9Comment = acceptable9Comment;
	}

	/**
	 * @return the acceptable10Content
	 */
	public String getAcceptable10Content() {
		return acceptable10Content;
	}

	/**
	 * @param acceptable10Content the acceptable10Content to set
	 */
	public void setAcceptable10Content(String acceptable10Content) {
		this.acceptable10Content = acceptable10Content;
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
	 * @return the acceptable10Comment
	 */
	public String getAcceptable10Comment() {
		return acceptable10Comment;
	}

	/**
	 * @param acceptable10Comment the acceptable10Comment to set
	 */
	public void setAcceptable10Comment(String acceptable10Comment) {
		this.acceptable10Comment = acceptable10Comment;
	}

	/**
	 * @return the acceptable11Content
	 */
	public String getAcceptable11Content() {
		return acceptable11Content;
	}

	/**
	 * @param acceptable11Content the acceptable11Content to set
	 */
	public void setAcceptable11Content(String acceptable11Content) {
		this.acceptable11Content = acceptable11Content;
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
	 * @return the acceptable11Comment
	 */
	public String getAcceptable11Comment() {
		return acceptable11Comment;
	}

	/**
	 * @param acceptable11Comment the acceptable11Comment to set
	 */
	public void setAcceptable11Comment(String acceptable11Comment) {
		this.acceptable11Comment = acceptable11Comment;
	}

	/**
	 * @return the acceptable12Content
	 */
	public String getAcceptable12Content() {
		return acceptable12Content;
	}

	/**
	 * @param acceptable12Content the acceptable12Content to set
	 */
	public void setAcceptable12Content(String acceptable12Content) {
		this.acceptable12Content = acceptable12Content;
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
	 * @return the acceptable12Comment
	 */
	public String getAcceptable12Comment() {
		return acceptable12Comment;
	}

	/**
	 * @param acceptable12Comment the acceptable12Comment to set
	 */
	public void setAcceptable12Comment(String acceptable12Comment) {
		this.acceptable12Comment = acceptable12Comment;
	}

	/**
	 * @return the employeeComment1
	 */
	public String getEmployeeComment1() {
		return employeeComment1;
	}

	/**
	 * @param employeeComment1 the employeeComment1 to set
	 */
	public void setEmployeeComment1(String employeeComment1) {
		this.employeeComment1 = employeeComment1;
	}

	/**
	 * @return the employeeComment2
	 */
	public String getEmployeeComment2() {
		return employeeComment2;
	}

	/**
	 * @param employeeComment2 the employeeComment2 to set
	 */
	public void setEmployeeComment2(String employeeComment2) {
		this.employeeComment2 = employeeComment2;
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
	
	/**
	 * @return the signatureImage
	 */
	public String getSignatureImage() {
		return signatureImage;
	}

	/**
	 * @param signatureImage the signatureImage to set
	 */
	public void setSignatureImage(String signatureImage) {
		this.signatureImage = signatureImage;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerSaftyCheckTank [key=" + this.getKeyValue() 
				+ ", areaCode=" + areaCode 
				+ ", customerCode=" + customerCode
				+ ", sequenceNumber=" + sequenceNumber 
				+ ", scheduledCheckDate=" + scheduledCheckDate
				+ ", employeeCode=" + employeeCode 
				+ ", employeeName=" + employeeName
				+ ", tankCapacity1=" + tankCapacity1
				+ ", tankCapacity2=" + tankCapacity2
				+ ", acceptable1=" + acceptable1 
				+ ", acceptable1Comment=" + acceptable1Comment
				+ ", acceptable2=" + acceptable2
				+ ", acceptable2Comment=" + acceptable2Comment
				+ ", acceptable3=" + acceptable3
				+ ", acceptable3Comment=" + acceptable3Comment
				+ ", acceptable4=" + acceptable4
				+ ", acceptable4Comment=" + acceptable4Comment
				+ ", acceptable5=" + acceptable5
				+ ", acceptable5Comment=" + acceptable5Comment
				+ ", acceptable6=" + acceptable6
				+ ", acceptable6Comment=" + acceptable6Comment
				+ ", acceptable7=" + acceptable7
				+ ", acceptable7Comment=" + acceptable7Comment
				+ ", acceptable8=" + acceptable8
				+ ", acceptable8Comment=" + acceptable8Comment
				+ ", acceptable9=" + acceptable9
				+ ", acceptable9Comment=" + acceptable9Comment
				+ ", acceptable10Content=" + acceptable10Content
				+ ", acceptable10=" + acceptable10
				+ ", acceptable10Comment=" + acceptable10Comment
				+ ", acceptable11Content=" + acceptable11Content
				+ ", acceptable11=" + acceptable11
				+ ", acceptable11Comment=" + acceptable11Comment
				+ ", acceptable12Content=" + acceptable12Content
				+ ", acceptable12=" + acceptable12
				+ ", acceptable12Comment=" + acceptable12Comment
				+ ", employeeComment1=" + employeeComment1
				+ ", employeeComment2=" + employeeComment2
				+ ", customerName=" + customerName
				+ ", signatureYn=" + signatureYn
				+ ", signatureImage=" + signatureImage
				+ ", userId=" + userId
				+ ", modifyDate=" + modifyDate + "]";
	}

	/**
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerSaftyCheckTank><key>" + this.getKeyValue()
				+ "</key><areaCode>" + areaCode + 
				"</areaCode><customerCode>" + customerCode + 
				"</customerCode><sequenceNumber>" + sequenceNumber + 
				"</sequenceNumber><<scheduledCheckDate>" + scheduledCheckDate + 
				"</scheduledCheckDate><employeeCode>" + employeeCode + 
				"</employeeCode><employeeName><![CDATA[" + employeeName 
				+ "]]></employeeName><tankCapacity1>" + tankCapacity1
				+ "</tankCapacity1><tankCapacity2>" + tankCapacity2
				+ "</tankCapacity2><acceptable1>" + acceptable1
				+ "</acceptable1><acceptable1Comment><![CDATA[" + acceptable1Comment
				+ "]]></acceptable1Comment><acceptable2>" + acceptable2
				+ "</acceptable2><acceptable2Comment><![CDATA[" + acceptable2Comment
				+ "]]></acceptable2Comment><acceptable3>" + acceptable3
				+ "</acceptable3><acceptable3Comment><![CDATA[" + acceptable3Comment
				+ "]]></acceptable3Comment><acceptable4>" + acceptable4
				+ "</acceptable4><acceptable4Comment><![CDATA[" + acceptable4Comment
				+ "]]></acceptable4Comment><acceptable5>" + acceptable5
				+ "</acceptable5><acceptable5Comment><![CDATA[" + acceptable5Comment
				+ "]]></acceptable5Comment><acceptable6>" + acceptable6
				+ "</acceptable6><acceptable6Comment><![CDATA[" + acceptable6Comment
				+ "]]></acceptable6Comment><acceptable7>" + acceptable7
				+ "</acceptable7><acceptable7Comment><![CDATA[" + acceptable7Comment
				+ "]]></acceptable7Comment><acceptable8>" + acceptable8
				+ "</acceptable8><acceptable8Comment><![CDATA[" + acceptable8Comment
				+ "]]></acceptable8Comment><acceptable9>" + acceptable9
				+ "</acceptable9><acceptable9Comment><![CDATA[" + acceptable9Comment
				+ "]]></acceptable9Comment><acceptable10Content><![CDATA[" + acceptable10Content
				+ "]]></acceptable10Content><acceptable10>" + acceptable10
				+ "</acceptable10><acceptable10Comment><![CDATA[" + acceptable10Comment
				+ "]]></acceptable10Comment><acceptable11Content><![CDATA[" + acceptable11Content
				+ "]]></acceptable11Content><acceptable11>" + acceptable11
				+ "</acceptable11><acceptable11Comment><![CDATA[" + acceptable11Comment
				+ "]]></acceptable11Comment><acceptable12Content><![CDATA[" + acceptable12Content
				+ "]]></acceptable12Content><acceptable12>" + acceptable12
				+ "</acceptable12><acceptable12Comment><![CDATA[" + acceptable12Comment
				+ "]]></acceptable12Comment><employeeComment1><![CDATA[" + employeeComment1
				+ "]]></employeeComment1><employeeComment2><![CDATA[" + employeeComment2
				+ "]]></employeeComment2><customerName><![CDATA[" + customerName
				+ "]]></customerName><signatureYn>" + signatureYn
				+ "</signatureYn><signatureImage><![CDATA[" + signatureImage
				+ "]]></signatureImage><userId>" + userId
				+ "</userId><modifyDate>" + modifyDate
				+ "</modifyDate></CustomerSaftyCheckTank>";
	}

}
