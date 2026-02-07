package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 품목 재고(고압용) 상세 내역 정보 모델
 * @author 백원태
 * @version 1.0
 */
public class CustomerItemBalanceHPGDetailList {
	
	/**
	 * AREA_CODE	
	 * 영업소코드
	 */
	 private String areaCode;

	/**
	 * PJ_CU_CODE	
	 * 거래처코드
	 */
	 private String customerCode;

	/**
	 * TR_DT	
	 * 일자
	 */
	 private String issueDate;

	/**
	 * TR_SNO	
	 * 항번
	 */
	 private String sequenceNumber;

	/**
	 * TR_GU	
	 * 등록메뉴구분
	 * 0.판매,  3.용기입출
	 */
	 private String menuType;

	/**
	 * TS_GUBUN	
	 * 구분
	 * 판매,대여,회수
	 */
	 private String issueType;

	/**
	 * TR_SW	
	 * 사원명
	 */
	 private String employeeName;

	/**
	 * JP_Code	
	 * 품목코드
	 */
	 private String itemCode;

	/**
	 * JP_Name	
	 * 품명
	 */
	 private String itemName;

	/**
	 * TR_QTY	
	 * 출고
	 */
	 private String outputQuantity;

	/**
	 * PJ_REQTY	
	 * 입고
	 */
	 private String inputQuantity;

	/**
	 * TR_NAME	
	 * 현장
	 */
	 private String place;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("PJ_CU_CODE", getCustomerCode());
		 keys.put("TR_DT", getIssueDate());
		 keys.put("JP_CODE", getItemCode());
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
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}

	/**
	 * @param menuType the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	/**
	 * @return the issueType
	 */
	public String getIssueType() {
		return issueType;
	}

	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(String issueType) {
		this.issueType = issueType;
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
	 * @return the outputQuantity
	 */
	public String getOutputQuantity() {
		return outputQuantity;
	}

	/**
	 * @param outputQuantity the outputQuantity to set
	 */
	public void setOutputQuantity(String outputQuantity) {
		this.outputQuantity = outputQuantity;
	}

	/**
	 * @return the inputQuantity
	 */
	public String getInputQuantity() {
		return inputQuantity;
	}

	/**
	 * @param inputQuantity the inputQuantity to set
	 */
	public void setInputQuantity(String inputQuantity) {
		this.inputQuantity = inputQuantity;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerItemBalanceHPGDetailList [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", issueDate="
				+ issueDate
				+ ", sequenceNumber="
				+ sequenceNumber
				+ ", menuType="
				+ menuType
				+ ", issueType="
				+ issueType
				+ ", employeeName="
				+ employeeName
				+ ", itemCode="
				+ itemCode
				+ ", itemName="
				+ itemName
				+ ", outputQuantity="
				+ outputQuantity
				+ ", inputQuantity="
				+ inputQuantity
				+ ", place=" + place + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerItemBalanceHPGDetailList><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><issueDate>"
				+ issueDate
				+ "</issueDate><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><menuType>"
				+ menuType
				+ "</menuType><issueType>"
				+ issueType
				+ "</issueType><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><outputQuantity>"
				+ outputQuantity
				+ "</outputQuantity><inputQuantity>"
				+ inputQuantity
				+ "</inputQuantity><place>"
				+ place
				+ "</place></CustomerItemBalanceHPGDetailList>";
	}
	
}
