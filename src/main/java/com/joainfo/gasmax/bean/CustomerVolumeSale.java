package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 체적 장부 - 공급(판매) 내역
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeSale {
	/**
	 * AREA_CODE	
	 * 영업소코드
	 */
	 private String areaCode;

	/**
	 * PC_CU_CODE	
	 * 거래처코드
	 */
	 private String customerCode;

	/**
	 * PC_Date	
	 * 일자
	 */
	 private String saleDate;

	/**
	 * PC_SNO	
	 * 항번
	 */
	 private String sequenceNumber;

	/**
	 * PC_JP_Code	
	 * 품목코드
	 */
	 private String itemCode;

	/**
	 * PC_JP_Name	
	 * 품명
	 */
	 private String itemName;

	/**
	 * PC_QTY	
	 * 납품
	 */
	 private String saleQuantity;

	/**
	 * PC_REQTY	
	 * 회수
	 */
	 private String withrawQuantity;

	/**
	 * PC_JP_Kg	
	 * 공급량
	 */
	 private String supplyQuantity;
	 
	 /**
	  * 누계
	  */
	 private String accumulateSupplyQuantity;

	/**
	 * PC_SW_CODE	
	 * 사원코드
	 */
	 private String employeeCode;

	/**
	 * PC_SW_Name	
	 * 사원명
	 */
	 private String employeeName;
	 
	 /**
	  * PJ_TS_SNO	
	  * 용기입출항번
	  */
	  private String equipmentInOutSequenceNumber;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("PC_CU_CODE", getCustomerCode());
		 keys.put("PC_DATE", getSaleDate());
		 keys.put("PC_SNO", getSequenceNumber());
		 
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
	 * @return the withrawQuantity
	 */
	public String getWithrawQuantity() {
		return withrawQuantity;
	}

	/**
	 * @param withrawQuantity the withrawQuantity to set
	 */
	public void setWithrawQuantity(String withrawQuantity) {
		this.withrawQuantity = withrawQuantity;
	}

	/**
	 * @return the supplyQuantity
	 */
	public String getSupplyQuantity() {
		return supplyQuantity;
	}

	/**
	 * @param supplyQuantity the supplyQuantity to set
	 */
	public void setSupplyQuantity(String supplyQuantity) {
		this.supplyQuantity = supplyQuantity;
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
	 * @return the accumulateSupplyQuantity
	 */
	public String getAccumulateSupplyQuantity() {
		return accumulateSupplyQuantity;
	}

	/**
	 * @param accumulateSupplyQuantity the accumulateSupplyQuantity to set
	 */
	public void setAccumulateSupplyQuantity(String accumulateSupplyQuantity) {
		this.accumulateSupplyQuantity = accumulateSupplyQuantity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerVolumeSale [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", saleDate="
				+ saleDate
				+ ", sequenceNumber="
				+ sequenceNumber
				+ ", itemCode="
				+ itemCode
				+ ", itemName="
				+ itemName
				+ ", saleQuantity="
				+ saleQuantity
				+ ", withrawQuantity="
				+ withrawQuantity
				+ ", supplyQuantity="
				+ supplyQuantity
				+ ", accumuateSupplyQuantity="
				+ accumulateSupplyQuantity
				+ ", employeeCode="
				+ employeeCode
				+ ", employeeName="
				+ employeeName
				+ ", equipmentInOutSequenceNumber="
				+ equipmentInOutSequenceNumber + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerVolumeSale><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><saleDate>"
				+ saleDate
				+ "</saleDate><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><saleQuantity>"
				+ saleQuantity
				+ "</saleQuantity><withrawQuantity>"
				+ withrawQuantity
				+ "</withrawQuantity><supplyQuantity>"
				+ supplyQuantity
				+ "</supplyQuantity><accumulateSupplyQuantity>"
				+ accumulateSupplyQuantity
				+ "</accumulateSupplyQuantity><employeeCode>"
				+ employeeCode
				+ "</employeeCode><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><equipmentInOutSequenceNumber>"
				+ equipmentInOutSequenceNumber
				+ "</equipmentInOutSequenceNumber></CustomerVolumeSale>";
	}

}
