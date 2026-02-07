package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 검침 현황 정보 객체
 * @author 백원태
 * @version 1.0
 */
public class ReadMeterList {
	/**
	 * Area_Code	영업소코드 - key
	 */
	private String areaCode;

	/**
	 * GJ_CU_Code	거래처코드 - key
	 */
	private String customerCode;

	/**
	 * GJ_GUMYMSNO	검침년월회차 - key
	 */
	private String sequenceNumber;

	/**
	 * GJ_Date	검침일 - key
	 */
	private String readMeterDate;

	/**
	 * CU_Name_View	거래처명
	 */
	private String customerName;

	/**
	 * GJ_JunGum	전월검침
	 */
	private String preReadMeter;

	/**
	 * GJ_Gum	당월검침
	 */
	private String nowReadMeter;

	/**
	 * GJ_JanKg	가스잔량
	 */
	private String remainQuantity;

	/**
	 * GJ_Gage	사용량
	 */
	private String useQuantity;

	/**
	 * GJ_DangKum	당월금액
	 */
	private String nowAmount;

	/**
	 * GJ_ANKUM	기타금액
	 */
	private String otherAmount;

	/**
	 * GJ_Misu	전미수
	 */
	private String unpaidAmount;

	/**
	 * GJ_PerKum	연체료
	 */
	private String chargeAmount;

	/**
	 * GJ_Total	당월금액
	 */
	private String sumNowAmount;

	/**
	 * GJ_MISUJAN	당월미수잔액
	 */
	private String sumUnpaidAmount;

	/**
	 * GJ_SDate	사용시작일
	 */
	private String useStartDate;

	/**
	 * GJ_LDate	사용종료일
	 */
	private String useEndDate;

	/**
	 * GJ_JiroDate	지로출력일
	 */
	private String printGiroDate;

	/**
	 * GJ_Zdate	납부마감일
	 */
	private String payClosingDate;

	/**
	 * GJ_Bigo	비고
	 */
	private String remark;

	/**
	 * CU_SukumType	수금방법
	 */
	private String collectTypeCode;

	/**
	 * CU_SW_Code	담당사원코드
	 */
	private String employeeCode;

	/**
	 * SW_Name	담당사원명
	 */
	private String employeeName;

	 /**
	  * key map 반환
	  * @return
	  */
	 public LinkedHashMap<String, String> getKeyMap(){
		 LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		 keys.put("AREA_CODE", getAreaCode());
		 keys.put("CU_CODE", getCustomerCode());
		 keys.put("GJ_Date", getReadMeterDate());
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
	 * @return the preReadMeter
	 */
	public String getPreReadMeter() {
		return preReadMeter;
	}

	/**
	 * @param preReadMeter the preReadMeter to set
	 */
	public void setPreReadMeter(String preReadMeter) {
		this.preReadMeter = preReadMeter;
	}

	/**
	 * @return the nowReadMeter
	 */
	public String getNowReadMeter() {
		return nowReadMeter;
	}

	/**
	 * @param nowReadMeter the nowReadMeter to set
	 */
	public void setNowReadMeter(String nowReadMeter) {
		this.nowReadMeter = nowReadMeter;
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
	 * @return the nowAmount
	 */
	public String getNowAmount() {
		return nowAmount;
	}

	/**
	 * @param nowAmount the nowAmount to set
	 */
	public void setNowAmount(String nowAmount) {
		this.nowAmount = nowAmount;
	}

	/**
	 * @return the otherAmount
	 */
	public String getOtherAmount() {
		return otherAmount;
	}

	/**
	 * @param otherAmount the otherAmount to set
	 */
	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
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

	/**
	 * @return the chargeAmount
	 */
	public String getChargeAmount() {
		return chargeAmount;
	}

	/**
	 * @param chargeAmount the chargeAmount to set
	 */
	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	/**
	 * @return the sumNowAmount
	 */
	public String getSumNowAmount() {
		return sumNowAmount;
	}

	/**
	 * @param sumNowAmount the sumNowAmount to set
	 */
	public void setSumNowAmount(String sumNowAmount) {
		this.sumNowAmount = sumNowAmount;
	}

	/**
	 * @return the sumUnpaidAmount
	 */
	public String getSumUnpaidAmount() {
		return sumUnpaidAmount;
	}

	/**
	 * @param sumUnpaidAmount the sumUnpaidAmount to set
	 */
	public void setSumUnpaidAmount(String sumUnpaidAmount) {
		this.sumUnpaidAmount = sumUnpaidAmount;
	}

	/**
	 * @return the useStartDate
	 */
	public String getUseStartDate() {
		return useStartDate;
	}

	/**
	 * @param useStartDate the useStartDate to set
	 */
	public void setUseStartDate(String useStartDate) {
		this.useStartDate = useStartDate;
	}

	/**
	 * @return the useEndDate
	 */
	public String getUseEndDate() {
		return useEndDate;
	}

	/**
	 * @param useEndDate the useEndDate to set
	 */
	public void setUseEndDate(String useEndDate) {
		this.useEndDate = useEndDate;
	}

	/**
	 * @return the printGiroDate
	 */
	public String getPrintGiroDate() {
		return printGiroDate;
	}

	/**
	 * @param printGiroDate the printGiroDate to set
	 */
	public void setPrintGiroDate(String printGiroDate) {
		this.printGiroDate = printGiroDate;
	}

	/**
	 * @return the payClosingDate
	 */
	public String getPayClosingDate() {
		return payClosingDate;
	}

	/**
	 * @param payClosingDate the payClosingDate to set
	 */
	public void setPayClosingDate(String payClosingDate) {
		this.payClosingDate = payClosingDate;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReadMeterList [key=" + this.getKeyValue() + ", areaCode=" + areaCode + ", customerCode="
				+ customerCode + ", sequenceNumber=" + sequenceNumber
				+ ", readMeterDate=" + readMeterDate + ", customerName="
				+ customerName + ", preReadMeter=" + preReadMeter
				+ ", nowReadMeter=" + nowReadMeter + ", remainQuantity="
				+ remainQuantity + ", useQuantity=" + useQuantity
				+ ", nowAmount=" + nowAmount + ", otherAmount=" + otherAmount
				+ ", unpaidAmount=" + unpaidAmount + ", chargeAmount="
				+ chargeAmount + ", sumNowAmount=" + sumNowAmount
				+ ", sumUnpaidAmount=" + sumUnpaidAmount
				+ ", useStartDate=" + useStartDate + ", useEndDate="
				+ useEndDate + ", printGiroDate=" + printGiroDate
				+ ", payClosingDate=" + payClosingDate + ", remark=" + remark
				+ ", collectTypeCode=" + collectTypeCode + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName + "]";
	}
	
	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<ReadMeterList><key>" + this.getKeyValue()
				+ "</key><areaCode>" + areaCode
				+ "</areaCode><customerCode>" + customerCode
				+ "</customerCode><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><readMeterDate>" + readMeterDate
				+ "</readMeterDate><customerName><![CDATA[" + customerName
				+ "]]></customerName><preReadMeter>" + preReadMeter
				+ "</preReadMeter><nowReadMeter>" + nowReadMeter
				+ "</nowReadMeter><remainQuantity>" + remainQuantity
				+ "</remainQuantity><useQuantity>" + useQuantity
				+ "</useQuantity><nowAmount>" + nowAmount
				+ "</nowAmount><otherAmount>" + otherAmount
				+ "</otherAmount><unpaidAmount>" + unpaidAmount
				+ "</unpaidAmount><chargeAmount>" + chargeAmount
				+ "</chargeAmount><sumNowAmount>" + sumNowAmount
				+ "</sumNowAmount><sumUnpaidAmount>" + sumUnpaidAmount
				+ "</sumUnpaidAmount><useStartDate>" + useStartDate
				+ "</useStartDate><useEndDate>" + useEndDate
				+ "</useEndDate><printGiroDate>" + printGiroDate
				+ "</printGiroDate><payClosingDate>" + payClosingDate
				+ "</payClosingDate><remark><![CDATA[" + remark
				+ "]]></remark><collectTypeCode>" + collectTypeCode
				+ "</collectTypeCode><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName></ReadMeterList>";
	}

}
