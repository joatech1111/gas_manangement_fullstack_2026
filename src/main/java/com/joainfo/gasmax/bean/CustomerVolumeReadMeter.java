package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처별 체적 장부 - 검침 내역
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeReadMeter {
	/**
	 * AREA_CODE	
	 * 영업소코드 -key
	 */
	 private String areaCode;

	/**
	 * GJ_CU_CODE	
	 * 거래처코드 -key
	 */
	 private String customerCode;

	/**
	 * GJ_GUMYMSNO	
	 * 검침회차-key
	 */
	 private String sequenceNumber;

	/**
	 * GJ_DATE	
	 * 검침일자-key
	 */
	 private String readMeterDate;

	/**
	 * GJ_JUNGUM	
	 * 전월검침
	 */
	 private String preMonthReadMeter;

	/**
	 * GJ_GUM	
	 * 당월검침
	 */
	 private String nowMonthReadMeter;

	/**
	 * GJ_GAGE	
	 * 사용량
	 */
	 private String useQuantity;

	/**
	 * GJ_DANGA	
	 * 단가
	 */
	 private String price;

	/**
	 * GJ_KUMACK	
	 * 사용료
	 */
	 private String useAmount;

	/**
	 * GJ_ANKUM	
	 * 관리비
	 */
	 private String manageAmount;

	/**
	 * GJ_DC	
	 * 할인액
	 */
	 private String discountAmount;

	/**
	 * GJ_PERKUM	
	 * 연체료
	 */
	 private String delayAmount;

	/**
	 * GJ_TOTAL	
	 * 당월금액
	 */
	 private String nowMonthAmount;

	/**
	 * GJ_BIGO	
	 * 비고
	 */
	 private String remark;

	/**
	 * GJ_JANKG	
	 * 잔량
	 */
	 private String remainQuantity;

	/**
	 * GJ_SUKUMDATE	
	 * 수납일
	 */
	 private String collectDate;

	/**
	 * GJ_MISUJAN	
	 * 미납잔액
	 */
	 private String unpaidAmount;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("GJ_CU_CODE", getCustomerCode());
		 keys.put("GJ_DATE", getReadMeterDate());
		 keys.put("GJ_GUMYMSNO", getSequenceNumber());
		 
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
	 * @return the readMeterDate
	 */
	public String getReadMeterDate() {
		return readMeterDate;
	}

	/**
	 * @param readMeterDate the readMeterDate to set
	 */
	public void setReadMeterDate(String readMeterDate) {
		this.readMeterDate = readMeterDate;
	}

	/**
	 * @return the preMonthReadMeter
	 */
	public String getPreMonthReadMeter() {
		return preMonthReadMeter;
	}

	/**
	 * @param preMonthReadMeter the preMonthReadMeter to set
	 */
	public void setPreMonthReadMeter(String preMonthReadMeter) {
		this.preMonthReadMeter = preMonthReadMeter;
	}

	/**
	 * @return the nowMonthReadMeter
	 */
	public String getNowMonthReadMeter() {
		return nowMonthReadMeter;
	}

	/**
	 * @param nowMonthReadMeter the nowMonthReadMeter to set
	 */
	public void setNowMonthReadMeter(String nowMonthReadMeter) {
		this.nowMonthReadMeter = nowMonthReadMeter;
	}

	/**
	 * @return the useQuantity
	 */
	public String getUseQuantity() {
		return useQuantity;
	}

	/**
	 * @param useQuantity the useQuantity to set
	 */
	public void setUseQuantity(String useQuantity) {
		this.useQuantity = useQuantity;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the useAmount
	 */
	public String getUseAmount() {
		return useAmount;
	}

	/**
	 * @param useAmount the useAmount to set
	 */
	public void setUseAmount(String useAmount) {
		this.useAmount = useAmount;
	}

	/**
	 * @return the manageAmount
	 */
	public String getManageAmount() {
		return manageAmount;
	}

	/**
	 * @param manageAmount the manageAmount to set
	 */
	public void setManageAmount(String manageAmount) {
		this.manageAmount = manageAmount;
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
	 * @return the delayAmount
	 */
	public String getDelayAmount() {
		return delayAmount;
	}

	/**
	 * @param delayAmount the delayAmount to set
	 */
	public void setDelayAmount(String delayAmount) {
		this.delayAmount = delayAmount;
	}

	/**
	 * @return the nowMonthAmount
	 */
	public String getNowMonthAmount() {
		return nowMonthAmount;
	}

	/**
	 * @param nowMonthAmount the nowMonthAmount to set
	 */
	public void setNowMonthAmount(String nowMonthAmount) {
		this.nowMonthAmount = nowMonthAmount;
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
	 * @return the remainQuantity
	 */
	public String getRemainQuantity() {
		return remainQuantity;
	}

	/**
	 * @param remainQuantity the remainQuantity to set
	 */
	public void setRemainQuantity(String remainQuantity) {
		this.remainQuantity = remainQuantity;
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
	 * @return the unpaidAmount
	 */
	public String getUnpaidAmount() {
		return unpaidAmount;
	}

	/**
	 * @param unpaidAmount the unpaidAmount to set
	 */
	public void setUnpaidAmount(String unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerVolumeReadMeter [key=" + this.getKeyValue() + ", areaCode=" + areaCode + ", customerCode="
				+ customerCode + ", sequenceNumber="
				+ sequenceNumber + ", readMeterDate=" + readMeterDate
				+ ", preMonthReadMeter=" + preMonthReadMeter
				+ ", nowMonthReadMeter=" + nowMonthReadMeter + ", useQuantity="
				+ useQuantity + ", price=" + price + ", useAmount=" + useAmount
				+ ", manageAmount=" + manageAmount + ", discountAmount="
				+ discountAmount + ", delayAmount=" + delayAmount
				+ ", nowMonthAmount=" + nowMonthAmount + ", remark=" + remark
				+ ", remainQuantity=" + remainQuantity + ", collectDate=" + collectDate
				+ ", unpaidAmount=" + unpaidAmount + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerVolumeReadMeter><key>" + this.getKeyValue()
				+ "</key><areaCode>" + areaCode
				+ "</areaCode><customerCode>" + customerCode
				+ "</customerCode><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><readMeterDate>" + readMeterDate
				+ "</readMeterDate><preMonthReadMeter>" + preMonthReadMeter
				+ "</preMonthReadMeter><nowMonthReadMeter>" + nowMonthReadMeter
				+ "</nowMonthReadMeter><useQuantity>" + useQuantity
				+ "</useQuantity><price>" + price + "</price><useAmount>"
				+ useAmount + "</useAmount><manageAmount>" + manageAmount
				+ "</manageAmount><discountAmount>" + discountAmount
				+ "</discountAmount><delayAmount>" + delayAmount
				+ "</delayAmount><nowMonthAmount>" + nowMonthAmount
				+ "</nowMonthAmount><remark><![CDATA[" + remark
				+ "]]></remark><remainQuantity>" + remainQuantity
				+ "</remainQuantity><collectDate>" + collectDate
				+ "</collectDate><unpaidAmount>" + unpaidAmount
				+ "</unpaidAmount></CustomerVolumeReadMeter>";
	}

}
