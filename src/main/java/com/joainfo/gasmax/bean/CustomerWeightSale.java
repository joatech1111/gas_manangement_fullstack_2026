package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 일반(중량)장부 - 매출내역
 * @author 백원태
 * @version 1.0
 */
public class CustomerWeightSale {
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
	 * PA_TYPE	
	 * 판매구분 - key
	 * 가스,용기,기구,A/S
	 */
	 private String saleType;
	 
	 /**
	  * 	
	  * 판매일자 - key
	  */
	 private String saleDate;
	 
	 /**
	  * 
	  * 판매항번 - key
	  */
	 private String sequenceNumber;

	/**
	 * JP_CODE	
	 * 품목코드 - key
	 */
	 private String itemCode;

	/**
	 * JP_NAME	
	 * 품명
	 */
	 private String itemName;

	/**
	 * SORT	
	 * 판매정렬순서
	 * 1.가스,2.용기,3.기구,4.A/S
	 */
	 private String saleDisplayOrder;

	/**
	 * JP_SORT	
	 * 품목정렬순서
	 */
	 private String itemDisplayOrder;

	/**
	 * JP_QTY	
	 * 납품
	 */
	 private String saleQuantity;

	/**
	 * JP_REQTY	
	 * 회수
	 */
	 private String withdrawQuantity;

	/**
	 * Kum_Sup	
	 * 공급액
	 */
	 private String supplyAmount;

	/**
	 * Kum_Vat	
	 * 세액
	 */
	 private String taxAmount;

	/**
	 * Kum_SUM	
	 * 합계
	 */
	 private String sumAmount;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("CU_CODE", getCustomerCode());
		 keys.put("PA_TYPE", getSaleType());
		 keys.put("JP_CODE", getItemCode());
		 
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
	 * @return the saleDisplayOrder
	 */
	public String getSaleDisplayOrder() {
		return saleDisplayOrder;
	}

	/**
	 * @param saleDisplayOrder the saleDisplayOrder to set
	 */
	public void setSaleDisplayOrder(String saleDisplayOrder) {
		this.saleDisplayOrder = saleDisplayOrder;
	}

	/**
	 * @return the itemDisplayOrder
	 */
	public String getItemDisplayOrder() {
		return itemDisplayOrder;
	}

	/**
	 * @param itemDisplayOrder the itemDisplayOrder to set
	 */
	public void setItemDisplayOrder(String itemDisplayOrder) {
		this.itemDisplayOrder = itemDisplayOrder;
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
	 * @return the sumAmount
	 */
	public String getSumAmount() {
		return sumAmount;
	}

	/**
	 * @param sumAmount the sumAmount to set
	 */
	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerWeightSale [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", saleType="
				+ saleType
				+ ", saleDate="
				+ saleDate
				+ ", sequenceNumber="
				+ sequenceNumber
				+ ", itemCode="
				+ itemCode
				+ ", itemName="
				+ itemName
				+ ", saleDisplayOrder="
				+ saleDisplayOrder
				+ ", itemDisplayOrder="
				+ itemDisplayOrder
				+ ", saleQuantity="
				+ saleQuantity
				+ ", withdrawQuantity="
				+ withdrawQuantity
				+ ", supplyAmount="
				+ supplyAmount
				+ ", taxAmount="
				+ taxAmount
				+ ", sumAmount=" + sumAmount + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerWeightSale><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><saleType>"
				+ saleType
				+ "</saleType><saleDate>"
				+ saleDate
				+ "</saleDate><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><saleDisplayOrder>"
				+ saleDisplayOrder
				+ "</saleDisplayOrder><itemDisplayOrder>"
				+ itemDisplayOrder
				+ "</itemDisplayOrder><saleQuantity>"
				+ saleQuantity
				+ "</saleQuantity><withdrawQuantity>"
				+ withdrawQuantity
				+ "</withdrawQuantity><supplyAmount>"
				+ supplyAmount
				+ "</supplyAmount><taxAmount>"
				+ taxAmount
				+ "</taxAmount><sumAmount>"
				+ sumAmount
				+ "</sumAmount></CustomerWeightSale>";
	}
}
