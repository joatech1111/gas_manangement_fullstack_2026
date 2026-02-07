package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 품목 재고(고압용) 정보모델
 * @author 백원태
 * @version 1.0
 */
public class CustomerItemBalanceHPG {
	/**
	 * AREA_CODE	
	 * 영업소코드
	 */
	 private String areaCode;

	/**
	 * JC_CU_CODE	
	 * 거래처코드
	 */
	 private String customerCode;

	/**
	 * JP_SORT	
	 * 품목정렬순서
	 */
	 private String sortOrder;

	/**
	 * JC_JP_CODE	
	 * 품목코드
	 */
	 private String itemCode;

	/**
	 * JP_Name	
	 * 품명
	 */
	 private String itemName;

	/**
	 * JP_Jaego	
	 * 현재잔고
	 */
	 private String currentBalance;

	/**
	 * QTY_PRE	
	 * 전재고
	 */
	 private String preBalance;

	/**
	 * QTY_PA	
	 * 출고
	 */
	 private String itemOutput;

	/**
	 * QTY_RE	
	 * 입고
	 */
	 private String itemInput;

	/**
	 * QTY_NOW	
	 * 재고
	 */
	 private String balance;
	 
	 /**
	  * 단가
	  */
	 private String salePrice;
	 
	 
	 

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("JC_CU_CODE", getCustomerCode());
		 keys.put("JC_JP_CODE", getItemCode());
		 
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
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
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
	 * @return the currentBalance
	 */
	public String getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance the currentBalance to set
	 */
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @return the preBalance
	 */
	public String getPreBalance() {
		return preBalance;
	}

	/**
	 * @param preBalance the preBalance to set
	 */
	public void setPreBalance(String preBalance) {
		this.preBalance = preBalance;
	}

	/**
	 * @return the itemOutput
	 */
	public String getItemOutput() {
		return itemOutput;
	}

	/**
	 * @param itemOutput the itemOutput to set
	 */
	public void setItemOutput(String itemOutput) {
		this.itemOutput = itemOutput;
	}

	/**
	 * @return the itemInput
	 */
	public String getItemInput() {
		return itemInput;
	}

	/**
	 * @param itemInput the itemInput to set
	 */
	public void setItemInput(String itemInput) {
		this.itemInput = itemInput;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerItemBalanceHPG [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode
				+ ", customerCode="
				+ customerCode
				+ ", sortOrder="
				+ sortOrder
				+ ", itemCode="
				+ itemCode
				+ ", itemName="
				+ itemName
				+ ", currentBalance="
				+ currentBalance
				+ ", preBalance="
				+ preBalance
				+ ", itemOutput="
				+ itemOutput
				+ ", itemInput=" + itemInput 
				+ ", balance=" + balance
				+ ", salePrice=" + salePrice + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML 문자열
	 */
	public String toXML() {
		return "<CustomerItemBalanceHPG><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><sortOrder>"
				+ sortOrder
				+ "</sortOrder><itemCode>"
				+ itemCode
				+ "</itemCode><itemName>"
				+ itemName
				+ "</itemName><currentBalance>"
				+ currentBalance
				+ "</currentBalance><preBalance>"
				+ preBalance
				+ "</preBalance><itemOutput>"
				+ itemOutput
				+ "</itemOutput><itemInput>"
				+ itemInput
				+ "</itemInput><balance>"
				+ balance
				+ "</balance><salePrice>"
				+ salePrice
				+ "</salePrice></CustomerItemBalanceHPG>";
	}

}
