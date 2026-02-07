package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerItem {
	/**
	 * JP_Code - key
	 * 코드
	 */
	 private String itemCode;

	/**
	 * JP_Name 
	 * 품명
	 */
	 private String itemName;

	/**
	 * JP_Spec 
	 * 규격
	 */
	 private String itemSpec;

	/**
	 * JP_Unit 
	 * 단위
	 */
	 private String itemUnit;

	/**
	 * JP_Kg 
	 * 용량 
	 */
	 private String capacity;

	/**
	 * JP_OutDanga 
	 * 판매단가 
	 */
	 private String salePrice;

	 /**
	  * JP_JAEGOCU 
	  * 재고
	  */
	 private String itemBalance;
	 
	 /**
	  * 단가 종류
	  * 0:개별단가 1:환경단가 2:직전단가
	  */
	 private String priceType;
	 
	 
	/**
	 * JP_DC_Kum 	
	 * Float	수량별 DC 금액	D/C 금액 * 수량
	 */
	private String discountAmount;

	/**
	 * JP_LAST_QTY	
	 * integer	최근 판매수량
	 */
	private String lastSaleQuantity;
	
	/**
	 * JP_LAST_MISU	
	 * Float	최근 외상판매금액
	 */
	private String lastUnpaidAmount;
	 
	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("JP_Code", getItemCode());
		
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
	 * @return the itemSpec
	 */
	public String getItemSpec() {
		return itemSpec;
	}

	/**
	 * @param itemSpec the itemSpec to set
	 */
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}

	/**
	 * @return the itemUnit
	 */
	public String getItemUnit() {
		return itemUnit;
	}

	/**
	 * @param itemUnit the itemUnit to set
	 */
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	/**
	 * @return the capacity
	 */
	public String getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(String capacity) {
		this.capacity = capacity;
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
	 * @return the itemBalance
	 */
	public String getItemBalance() {
		return itemBalance;
	}

	/**
	 * @param itemBalance the itemBalance to set
	 */
	public void setItemBalance(String itemBalance) {
		this.itemBalance = itemBalance;
	}


	/**
	 * @return the priceType
	 */
	public String getPriceType() {
		return priceType;
	}

	/**
	 * @param priceType the priceType to set
	 */
	public void setPriceType(String priceType) {
		this.priceType = priceType;
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
	 * @return the lastSaleQuantity
	 */
	public String getLastSaleQuantity() {
		return lastSaleQuantity;
	}

	/**
	 * @param lastSaleQuantity the lastSaleQuantity to set
	 */
	public void setLastSaleQuantity(String lastSaleQuantity) {
		this.lastSaleQuantity = lastSaleQuantity;
	}

	/**
	 * @return the lastUnpaidAmount
	 */
	public String getLastUnpaidAmount() {
		return lastUnpaidAmount;
	}

	/**
	 * @param lastUnpaidAmount the lastUnpaidAmount to set
	 */
	public void setLastUnpaidAmount(String lastUnpaidAmount) {
		this.lastUnpaidAmount = lastUnpaidAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerItem [key=" + this.getKeyValue() + ", itemCode="
				+ itemCode + ", itemName=" + itemName + ", itemSpec="
				+ itemSpec + ", itemUnit=" + itemUnit + ", capacity="
				+ capacity + ", salePrice=" + salePrice + ", itemBalance="
				+ itemBalance + ", priceType=" + priceType
				+ ", discountAmount=" + discountAmount + ", lastSaleQuantity="
				+ lastSaleQuantity + ", lastUnpaidAmount=" + lastUnpaidAmount
				+ "]";
	}

	/**
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerItem><key>" + this.getKeyValue() + "</key><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><itemSpec>"
				+ itemSpec
				+ "</itemSpec><itemUnit>"
				+ itemUnit
				+ "</itemUnit><capacity>"
				+ capacity
				+ "</capacity><salePrice>"
				+ salePrice
				+ "</salePrice><itemBalance>"
				+ itemBalance
				+ "</itemBalance><priceType>"
				+ priceType
				+ "</priceType><discountAmount>"
				+ discountAmount
				+ "</discountAmount><lastSaleQuantity>"
				+ lastSaleQuantity
				+ "</lastSaleQuantity><lastUnpaidAmount>"
				+ lastUnpaidAmount
				+ "</lastUnpaidAmount></CustomerItem>";
	}

}
