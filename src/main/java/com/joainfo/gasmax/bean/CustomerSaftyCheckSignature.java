package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerSaftyCheckSignature {
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
	 * ANZ_Image
	 * 서명이미지		
	 */
	 private String signatureImage;

	/**
	 * ANZ_ID	
	 * APP 사용자명		
	 */
	 private String userId;

	/**
	 * ANZ_Date_DT	
	 * 등록/수정일자		
	 */
	 private String modifyDate;

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
				+ ", scheduledCheckDate="
				+ scheduledCheckDate
				+ ", signatureImage="
				+ signatureImage
				+ ", userId="
				+ userId + ", modifyDate=" + modifyDate + "]";
	}

	/**
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerSaftyCheckSignature><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><scheduledCheckDate>"
				+ scheduledCheckDate
				+ "</scheduledCheckDate><signatureImage><![CDATA["
				+ signatureImage
				+ "]]></signatureImage><userId>"
				+ userId
				+ "</userId><modifyDate>"
				+ modifyDate
				+ "</modifyDate></CustomerSaftyCheckSignature>";
	}

}
