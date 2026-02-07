package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerLatestReadMeter {
	/**
	 * Area_Code	
	 * VARCHAR(2)	영업소코드	
	 */
	 private String areaCode;

	/**
	 * CU_Code	
	 * VARCHAR(9)	거래처코드	
	 */
	 private String customerCode;

	/**
	 * CU_NAME	
	 * VARCHAR(20)	건물명	
	 */
	 private String buildingName;

	/**
	 * CU_USERNAME	
	 * VARCHAR(20)	사용자명	
	 */
	 private String userName;

	/**
	 * CU_SW_CODE	
	 * VARCHAR(2)	담당 사원코드	
	 */
	 private String employeeCode;

	/**
	 * CU_SW_NAME	
	 * VARCHAR(10)	담당 사원명	
	 */
	 private String employeeName;

	/**
	 * JN_TYPE_PER	
	 * VARCHAR(1)	연체료적용방법	
	 */
	 private String delayFeeMethodType;

	/**
	 * JN_TYPE_WON	
	 * VARCHAR(1)	금액 원단위 계산방법	
	 */
	 private String roundType;

	/**
	 * CU_TYPE_Danga	
	 * VARCHAR(8)	적용할 루베단가	개별단가/환경단가/할인단가
	 */
	 private String priceType;

	/**
	 * CU_Basic_Danga 	
	 * Float	환경단가	
	 */
	 private String environmentPrice;

	/**
	 * CU_User_Danga	
	 * Float	개별단가	
	 */
	 private String unitPrice;

	/**
	 * CU_DC_Danga 	
	 * Float	할인단가	
	 */
	 private String discountPrice;

	/**
	 * CU_BA_Gage_YN	
	 * VARCHAR(1)	기본료 적용유무	Y 일경우 사용량에따라 기본료 적용
	 */
	 private String defaultAmountYesNo;

	/**
	 * CU_BA_Gage_M3	
	 * integer	기본 사용량	사용량이 기본사용량보다 작을경우 기본료적용
	 */
	 private String defaultUse;

	/**
	 * CU_BA_Gage_KUM	
	 * Float	기본료	
	 */
	 private String defaultAmount;

	/**
	 * CU_AnKum	
	 * Float	관리비	
	 */
	 private String managementAmount;

	/**
	 * CU_Per	
	 * Smallint	연체율(%)	연체료적용방법에따라 연체율 적용
	 */
	 private String delayFeePercent;

	/**
	 * CU_CDC	
	 * Float	할인율(%)	
	 */
	 private String discountPercent;

	/**
	 * CU_CMISU	
	 * Float	전미수/연체료 적용 금액 1	1. 미수금 연체적용(등록일 미수기준)
	 */
	 private String delayAmount1;

	/**
	 * CU_PerKum_2	
	 * Float	연체료 적용 금액 2	2. 전월미납 연체적용(등록일 미수기준)
	 */
	 private String delayAmount2;

	/**
	 * CU_PerKum_3	
	 * Float	연체료 적용 금액 3	3. 전월미납 연체적용(연체적용일 미수기준)
	 */
	 private String delayAmount3;

	/**
	 * CU_PerKum_4	
	 * Float	연체료 적용 금액 4	4. 미수금 연체적용(연체적용일 미수기준)
	 */
	 private String delayAmount4;

	/**
	 * CU_PerKum_5	
	 * Float	연체료 적용 금액 5	5. 미납사용료 연체적용(연체적용일 미수기준)
	 */
	 private String delayAmount5;

	/**
	 * GJ_GUMYMSNO	
	 * VARCHAR(8)	전 검침년월회차	
	 */
	 private String preSequenceNumber;

	/**
	 * GJ_DATE	
	 * VARCHAR(8)	전 검침일자	사용시작일 = 전검침일+1일
	 */
	 private String preReadMeterDate;

	/**
	 * GJ_GUM	
	 * integer	전 검침	전검 = 전검침
	 */
	 private String preReadMeter;

	/**
	 * GJ_KUMACK	
	 * Float	전 사용료	
	 */
	 private String preAmount;

	/**
	 * GJ_JanKg	
	 * integer	전 잔량	
	 */
	 private String preRemain;

	/**
	 * GJ_SUKUMDATE	
	 * VARCHAR(8)	수납일자	
	 */
	 private String collectDate;

	/**
	 * GJ_MISUJAN	
	 * Float	전 미수잔액	
	 */
	 private String preUnpaidAmount;

	/**
	 * GJ_Close_Date	
	 * VARCHAR(8)	전 납부마감일(지로출력시)	연체적용일 Defult 값,  '' 이면 검침일과 동일하게
	 */
	 private String applyDelayDate;


	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("Area_Code", getAreaCode());
		keys.put("Cu_Code", getCustomerCode());
		keys.put("GJ_GUMYMSNO", getPreSequenceNumber());
		
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
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the delayFeeMethodType
	 */
	public String getDelayFeeMethodType() {
		return delayFeeMethodType;
	}

	/**
	 * @param delayFeeMethodType the delayFeeMethodType to set
	 */
	public void setDelayFeeMethodType(String delayFeeMethodType) {
		this.delayFeeMethodType = delayFeeMethodType;
	}

	/**
	 * @return the roundType
	 */
	public String getRoundType() {
		return roundType;
	}

	/**
	 * @param roundType the roundType to set
	 */
	public void setRoundType(String roundType) {
		this.roundType = roundType;
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
	 * @return the environmentPrice
	 */
	public String getEnvironmentPrice() {
		return environmentPrice;
	}

	/**
	 * @param environmentPrice the environmentPrice to set
	 */
	public void setEnvironmentPrice(String environmentPrice) {
		this.environmentPrice = environmentPrice;
	}

	/**
	 * @return the unitPrice
	 */
	public String getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the discountPrice
	 */
	public String getDiscountPrice() {
		return discountPrice;
	}

	/**
	 * @param discountPrice the discountPrice to set
	 */
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	/**
	 * @return the defaultAmountYesNo
	 */
	public String getDefaultAmountYesNo() {
		return defaultAmountYesNo;
	}

	/**
	 * @param defaultAmountYesNo the defaultAmountYesNo to set
	 */
	public void setDefaultAmountYesNo(String defaultAmountYesNo) {
		this.defaultAmountYesNo = defaultAmountYesNo;
	}

	/**
	 * @return the defaultUse
	 */
	public String getDefaultUse() {
		return defaultUse;
	}

	/**
	 * @param defaultUse the defaultUse to set
	 */
	public void setDefaultUse(String defaultUse) {
		this.defaultUse = defaultUse;
	}

	/**
	 * @return the defaultAmount
	 */
	public String getDefaultAmount() {
		return defaultAmount;
	}

	/**
	 * @param defaultAmount the defaultAmount to set
	 */
	public void setDefaultAmount(String defaultAmount) {
		this.defaultAmount = defaultAmount;
	}

	/**
	 * @return the managementAmount
	 */
	public String getManagementAmount() {
		return managementAmount;
	}

	/**
	 * @param managementAmount the managementAmount to set
	 */
	public void setManagementAmount(String managementAmount) {
		this.managementAmount = managementAmount;
	}

	/**
	 * @return the delayFeePercent
	 */
	public String getDelayFeePercent() {
		return delayFeePercent;
	}

	/**
	 * @param delayFeePercent the delayFeePercent to set
	 */
	public void setDelayFeePercent(String delayFeePercent) {
		this.delayFeePercent = delayFeePercent;
	}

	/**
	 * @return the discountPercent
	 */
	public String getDiscountPercent() {
		return discountPercent;
	}

	/**
	 * @param discountPercent the discountPercent to set
	 */
	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	/**
	 * @return the delayAmount1
	 */
	public String getDelayAmount1() {
		return delayAmount1;
	}

	/**
	 * @param delayAmount1 the delayAmount1 to set
	 */
	public void setDelayAmount1(String delayAmount1) {
		this.delayAmount1 = delayAmount1;
	}

	/**
	 * @return the delayAmount2
	 */
	public String getDelayAmount2() {
		return delayAmount2;
	}

	/**
	 * @param delayAmount2 the delayAmount2 to set
	 */
	public void setDelayAmount2(String delayAmount2) {
		this.delayAmount2 = delayAmount2;
	}

	/**
	 * @return the delayAmount3
	 */
	public String getDelayAmount3() {
		return delayAmount3;
	}

	/**
	 * @param delayAmount3 the delayAmount3 to set
	 */
	public void setDelayAmount3(String delayAmount3) {
		this.delayAmount3 = delayAmount3;
	}

	/**
	 * @return the delayAmount4
	 */
	public String getDelayAmount4() {
		return delayAmount4;
	}

	/**
	 * @param delayAmount4 the delayAmount4 to set
	 */
	public void setDelayAmount4(String delayAmount4) {
		this.delayAmount4 = delayAmount4;
	}

	/**
	 * @return the delayAmount5
	 */
	public String getDelayAmount5() {
		return delayAmount5;
	}

	/**
	 * @param delayAmount5 the delayAmount5 to set
	 */
	public void setDelayAmount5(String delayAmount5) {
		this.delayAmount5 = delayAmount5;
	}

	/**
	 * @return the preSequenceNumber
	 */
	public String getPreSequenceNumber() {
		return preSequenceNumber;
	}

	/**
	 * @param preSequenceNumber the preSequenceNumber to set
	 */
	public void setPreSequenceNumber(String preSequenceNumber) {
		this.preSequenceNumber = preSequenceNumber;
	}

	/**
	 * @return the preReadMeterDate
	 */
	public String getPreReadMeterDate() {
		return preReadMeterDate;
	}

	/**
	 * @param preReadMeterDate the preReadMeterDate to set
	 */
	public void setPreReadMeterDate(String preReadMeterDate) {
		this.preReadMeterDate = preReadMeterDate;
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
	 * @return the preAmount
	 */
	public String getPreAmount() {
		return preAmount;
	}

	/**
	 * @param preAmount the preAmount to set
	 */
	public void setPreAmount(String preAmount) {
		this.preAmount = preAmount;
	}

	/**
	 * @return the preRemain
	 */
	public String getPreRemain() {
		return preRemain;
	}

	/**
	 * @param preRemain the preRemain to set
	 */
	public void setPreRemain(String preRemain) {
		this.preRemain = preRemain;
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
	 * @return the preUnpaidAmount
	 */
	public String getPreUnpaidAmount() {
		return preUnpaidAmount;
	}

	/**
	 * @param preUnpaidAmount the preUnpaidAmount to set
	 */
	public void setPreUnpaidAmount(String preUnpaidAmount) {
		this.preUnpaidAmount = preUnpaidAmount;
	}

	/**
	 * @return the applyDelayDate
	 */
	public String getApplyDelayDate() {
		return applyDelayDate;
	}

	/**
	 * @param applyDelayDate the applyDelayDate to set
	 */
	public void setApplyDelayDate(String applyDelayDate) {
		this.applyDelayDate = applyDelayDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerLatestReadMeter [key=" + this.getKeyValue() + ", areaCode="
				+ areaCode + ", customerCode=" + customerCode
				+ ", buildingName=" + buildingName + ", userName=" + userName
				+ ", employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", delayFeeMethodType=" + delayFeeMethodType
				+ ", roundType=" + roundType + ", priceType=" + priceType
				+ ", environmentPrice=" + environmentPrice + ", unitPrice="
				+ unitPrice + ", discountPrice=" + discountPrice
				+ ", defaultAmountYesNo=" + defaultAmountYesNo
				+ ", defaultUse=" + defaultUse + ", defaultAmount="
				+ defaultAmount + ", managementAmount=" + managementAmount
				+ ", delayFeePercent=" + delayFeePercent + ", discountPercent="
				+ discountPercent + ", delayAmount1=" + delayAmount1
				+ ", delayAmount2=" + delayAmount2 + ", delayAmount3="
				+ delayAmount3 + ", delayAmount4=" + delayAmount4
				+ ", delayAmount5=" + delayAmount5 + ", preSequenceNumber="
				+ preSequenceNumber + ", preReadMeterDate=" + preReadMeterDate
				+ ", preReadMeter=" + preReadMeter + ", preAmount=" + preAmount
				+ ", preRemain=" + preRemain + ", collectDate=" + collectDate
				+ ", preUnpaidAmount=" + preUnpaidAmount + ", applyDelayDate="
				+ applyDelayDate + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CustomerLatestReadMeter><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><buildingName><![CDATA["
				+ buildingName
				+ "]]></buildingName><userName><![CDATA["
				+ userName
				+ "]]></userName><employeeCode>"
				+ employeeCode
				+ "</employeeCode><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><delayFeeMethodType>"
				+ delayFeeMethodType
				+ "</delayFeeMethodType><roundType>"
				+ roundType
				+ "</roundType><priceType>"
				+ priceType
				+ "</priceType><environmentPrice>"
				+ environmentPrice
				+ "</environmentPrice><unitPrice>"
				+ unitPrice
				+ "</unitPrice><discountPrice>"
				+ discountPrice
				+ "</discountPrice><defaultAmountYesNo>"
				+ defaultAmountYesNo
				+ "</defaultAmountYesNo><defaultUse>"
				+ defaultUse
				+ "</defaultUse><defaultAmount>"
				+ defaultAmount
				+ "</defaultAmount><managementAmount>"
				+ managementAmount
				+ "</managementAmount><delayFeePercent>"
				+ delayFeePercent
				+ "</delayFeePercent><discountPercent>"
				+ discountPercent
				+ "</discountPercent><delayAmount1>"
				+ delayAmount1
				+ "</delayAmount1><delayAmount2>"
				+ delayAmount2
				+ "</delayAmount2><delayAmount3>"
				+ delayAmount3
				+ "</delayAmount3><delayAmount4>"
				+ delayAmount4
				+ "</delayAmount4><delayAmount5>"
				+ delayAmount5
				+ "</delayAmount5><preSequenceNumber>"
				+ preSequenceNumber
				+ "</preSequenceNumber><preReadMeterDate>"
				+ preReadMeterDate
				+ "</preReadMeterDate><preReadMeter>"
				+ preReadMeter
				+ "</preReadMeter><preAmount>"
				+ preAmount
				+ "</preAmount><preRemain>"
				+ preRemain
				+ "</preRemain><collectDate>"
				+ collectDate
				+ "</collectDate><preUnpaidAmount>"
				+ preUnpaidAmount
				+ "</preUnpaidAmount><applyDelayDate>"
				+ applyDelayDate
				+ "</applyDelayDate></CustomerLatestReadMeter>";
	}

}
