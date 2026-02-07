package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 거래처 검색 결과 단위 모델
 * @author 백원태
 * @version 1.0
 */
public class CustomerSearch {
	
	/**
	 * 검색 순번
	 */
	private String sequenceNumber;
	
	/**
	 * 영업소 코드 - key
	 */
	private String areaCode;
	
	/**
	 * 거래처 코드 - key
	 */
	private String customerCode;
	
	/**
	 * 거래구분코드
	 */
	private String customerType;
	
	/**
	 * 거래구분
	 */
	private String customerTypeName;
	
	/**
	 * 거래상태코드
	 */
	private String customerStatusCode;
	
	/**
	 * 거래상태
	 */
	private String customerStatusName;
	
	/**
	 * 거래처명
	 */
	private String customerName;
	
	/**
	 * 건물명
	 */
	private String buildingName;
	
	/**
	 * 사용자명
	 */
	private String userName;
	
	/**
	 * 전화
	 */
	private String phoneNumber;
	
	/**
	 * 핸드폰
	 */
	private String mobileNumber;
	
	/**
	 * 전화2
	 */
	private String phoneNumber2;
	
	/**
	 * 전화_FIND
	 */
	private String phoneNumberFind;
	
	/**
	 * 핸드폰_FIND
	 */
	private String mobileNumberFind;
	
	/**
	 * 전화2_FIND
	 */
	private String phoneNumberFind2;
	
	/**
	 * 주소1
	 */
	private String address1;
	
	/**
	 * 주소2
	 */
	private String address2;
	
	/**
	 * 비고1
	 */
	private String remark1;
	
	/**
	 * 비고2
	 */
	private String remark2;
	
	/**
	 * 사원코드
	 */
	private String employeeCode;
	
	/**
	 * 사원명
	 */
	private String employeeName;
	
	/**
	 * 소비코드
	 */
	private String consumeTypeCode;
	
	/**
	 * 소비자형태
	 */
	private String consumerTypeName;
	
	/**
	 * 무료시설액
	 */
	private String freeInstallationFee;
	
	/**
	 * 용기보증금
	 */
	private String containerDeposit;
	
	/**
	 * 중량미수
	 */
	private String weightReceivable;
	
	/**
	 * 체적미수
	 */
	private String volumeReceivable;
	
	/**
	 * 메모
	 */
	private String memo;
	
	/**
	 * 부가세적용구분
	 */
	private String vatType;
	
	/**
	 * 중량단가구분
	 */
	private String weightPriceType;
	
	/**
	 * 판매단가할인율
	 */
	private String salePriceDiscountRate;
	
	/**
	 * 계산서발행유무
	 */
	private String issueTaxbillYesNo;
	
	/**
	 * 사업자구분
	 */
	private String registerNumberType;
	
	/**
	 * 사업번호
	 */
	private String registerNumber;
	
	/**
	 * 상호
	 */
	private String registerName;
	
	/**
	 * 대표
	 */
	private String registerOwner;
	
	/**
	 * 사업주소1
	 */
	private String registerAddress1;
	
	/**
	 * 사업주소2
	 */
	private String registerAddress2;
	
	/**
	 * 업태
	 */
	private String businessCondition;
	
	/**
	 * 종목
	 */
	private String businessType;
	
	/**
	 * 담당
	 */
	private String contactName;
	
	/**
	 * 부서명
	 */
	private String contactDepartment;
	
	/**
	 * 이메일
	 */
	private String contactEmail;
	
	/**
	 * 담당전화
	 */
	private String contactPhoneNumber;
	
	/**
	 * 팩스
	 */
	private String contactFaxNumber;
	
	/**
	 * 조정기압력
	 */
	private String regulatorPressure;
	
	/**
	 * 단가구분
	 */
	private String priceType;
	
	/**
	 * 적용단가
	 */
	private String applyPrice;
	
	/**
	 * 환경단가
	 */
	private String environmentPrice;
	
	/**
	 * 개별단가
	 */
	private String individualPrice;
	
	/**
	 * 할인단가
	 */
	private String discountPrice;
	
	/**
	 * 부호
	 */
	private String priceMode;
	
	/**
	 * 할인적용액
	 */
	private String discountAmount;
	
	/**
	 * 연체율
	 */
	private String defaultRate;
	
	/**
	 * 할인율
	 */
	private String discountRate;
	
	/**
	 * 안전관리비
	 */
	private String maintenanceFee;
	
	/**
	 * 시설비
	 */
	private String installationFee;
	
	/**
	 * 계량기교체비
	 */
	private String gaugeReplacementFee;
	
	/**
	 * 수금방법코드
	 */
	private String paymentType;
	
	/**
	 * 수금방법
	 */
	private String paymentTypeName;
	
	/**
	 * 순번
	 */
	private String serialNumber;
	
	/**
	 * 정기검침일
	 */
	private String readMeterDay;
	
	/**
	 * 계약번호
	 */
	private String contractNumber;
	
	/**
	 * 계약일자
	 */
	private String contractDate;
	
	/**
	 * 점검일자
	 */
	private String latestSaftyCheckDate;
	
	/**
	 * 계약자명
	 */
	private String contractName;
	
	/**
	 * 주민번호
	 */
	private String contracterResidentNumber;
	
	/**
	 * 용기소유
	 */
	private String containerOwnerType;
	
	/**
	 * 시설소유
	 */
	private String facilityOwnerType;
	
	/**
	 * 적합유무
	 */
	private String facilityOkYesNo;
	
	/**
	 * 절체기
	 */
	private String switcherCapacity;
	
	/**
	 * 기화기
	 */
	private String vaporizerCapacity;
	
	/**
	 * 계량기
	 */
	private String gaugeVolume;
	
	/**
	 * 퓨즈콕
	 */
	private String fuseCockQuantity;
	
	/**
	 * 호스
	 */
	private String hoseLength;
	
	/**
	 * 밸브
	 */
	private String valve;
	
	/**
	 * 공급관1
	 */
	private String supplyPipe1;
	
	/**
	 * 공급관2
	 */
	private String supplyPipe2;
	
	
	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("AREA_CODE", getAreaCode());
		keys.put("CU_CODE", getCustomerCode());
		
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
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the customerTypeName
	 */
	public String getCustomerTypeName() {
		return customerTypeName;
	}

	/**
	 * @param customerTypeName the customerTypeName to set
	 */
	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	/**
	 * @return the customerStatusCode
	 */
	public String getCustomerStatusCode() {
		return customerStatusCode;
	}

	/**
	 * @param customerStatusCode the customerStatusCode to set
	 */
	public void setCustomerStatusCode(String customerStatusCode) {
		this.customerStatusCode = customerStatusCode;
	}

	/**
	 * @return the customerStatusName
	 */
	public String getCustomerStatusName() {
		return customerStatusName;
	}

	/**
	 * @param customerStatusName the customerStatusName to set
	 */
	public void setCustomerStatusName(String customerStatusName) {
		this.customerStatusName = customerStatusName;
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the phoneNumber2
	 */
	public String getPhoneNumber2() {
		return phoneNumber2;
	}

	/**
	 * @param phoneNumber2 the phoneNumber2 to set
	 */
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}

	/**
	 * @return the phoneNumberFind
	 */
	public String getPhoneNumberFind() {
		return phoneNumberFind;
	}

	/**
	 * @param phoneNumberFind the phoneNumberFind to set
	 */
	public void setPhoneNumberFind(String phoneNumberFind) {
		this.phoneNumberFind = phoneNumberFind;
	}

	/**
	 * @return the mobileNumberFind
	 */
	public String getMobileNumberFind() {
		return mobileNumberFind;
	}

	/**
	 * @param mobileNumberFind the mobileNumberFind to set
	 */
	public void setMobileNumberFind(String mobileNumberFind) {
		this.mobileNumberFind = mobileNumberFind;
	}

	/**
	 * @return the phoneNumberFind2
	 */
	public String getPhoneNumberFind2() {
		return phoneNumberFind2;
	}

	/**
	 * @param phoneNumberFind2 the phoneNumberFind2 to set
	 */
	public void setPhoneNumberFind2(String phoneNumberFind2) {
		this.phoneNumberFind2 = phoneNumberFind2;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the remark1
	 */
	public String getRemark1() {
		return remark1;
	}

	/**
	 * @param remark1 the remark1 to set
	 */
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	/**
	 * @return the remark2
	 */
	public String getRemark2() {
		return remark2;
	}

	/**
	 * @param remark2 the remark2 to set
	 */
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
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
	 * @return the consumeTypeCode
	 */
	public String getConsumeTypeCode() {
		return consumeTypeCode;
	}

	/**
	 * @param consumeTypeCode the consumeTypeCode to set
	 */
	public void setConsumeTypeCode(String consumeTypeCode) {
		this.consumeTypeCode = consumeTypeCode;
	}

	/**
	 * @return the consumerTypeName
	 */
	public String getConsumerTypeName() {
		return consumerTypeName;
	}

	/**
	 * @param consumerTypeName the consumerTypeName to set
	 */
	public void setConsumerTypeName(String consumerTypeName) {
		this.consumerTypeName = consumerTypeName;
	}

	/**
	 * @return the freeInstallationFee
	 */
	public String getFreeInstallationFee() {
		return freeInstallationFee;
	}

	/**
	 * @param freeInstallationFee the freeInstallationFee to set
	 */
	public void setFreeInstallationFee(String freeInstallationFee) {
		this.freeInstallationFee = freeInstallationFee;
	}

	/**
	 * @return the containerDeposit
	 */
	public String getContainerDeposit() {
		return containerDeposit;
	}

	/**
	 * @param containerDeposit the containerDeposit to set
	 */
	public void setContainerDeposit(String containerDeposit) {
		this.containerDeposit = containerDeposit;
	}

	/**
	 * @return the weightReceivable
	 */
	public String getWeightReceivable() {
		return weightReceivable;
	}

	/**
	 * @param weightReceivable the weightReceivable to set
	 */
	public void setWeightReceivable(String weightReceivable) {
		this.weightReceivable = weightReceivable;
	}

	/**
	 * @return the volumeReceivable
	 */
	public String getVolumeReceivable() {
		return volumeReceivable;
	}

	/**
	 * @param volumeReceivable the volumeReceivable to set
	 */
	public void setVolumeReceivable(String volumeReceivable) {
		this.volumeReceivable = volumeReceivable;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the vatType
	 */
	public String getVatType() {
		return vatType;
	}

	/**
	 * @param vatType the vatType to set
	 */
	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	/**
	 * @return the weightPriceType
	 */
	public String getWeightPriceType() {
		return weightPriceType;
	}

	/**
	 * @param weightPriceType the weightPriceType to set
	 */
	public void setWeightPriceType(String weightPriceType) {
		this.weightPriceType = weightPriceType;
	}

	/**
	 * @return the salePriceDiscountRate
	 */
	public String getSalePriceDiscountRate() {
		return salePriceDiscountRate;
	}

	/**
	 * @param salePriceDiscountRate the salePriceDiscountRate to set
	 */
	public void setSalePriceDiscountRate(String salePriceDiscountRate) {
		this.salePriceDiscountRate = salePriceDiscountRate;
	}

	/**
	 * @return the issueTaxbillYesNo
	 */
	public String getIssueTaxbillYesNo() {
		return issueTaxbillYesNo;
	}

	/**
	 * @param issueTaxbillYesNo the issueTaxbillYesNo to set
	 */
	public void setIssueTaxbillYesNo(String issueTaxbillYesNo) {
		this.issueTaxbillYesNo = issueTaxbillYesNo;
	}

	/**
	 * @return the registerNumberType
	 */
	public String getRegisterNumberType() {
		return registerNumberType;
	}

	/**
	 * @param registerNumberType the registerNumberType to set
	 */
	public void setRegisterNumberType(String registerNumberType) {
		this.registerNumberType = registerNumberType;
	}

	/**
	 * @return the registerNumber
	 */
	public String getRegisterNumber() {
		return registerNumber;
	}

	/**
	 * @param registerNumber the registerNumber to set
	 */
	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	/**
	 * @return the registerName
	 */
	public String getRegisterName() {
		return registerName;
	}

	/**
	 * @param registerName the registerName to set
	 */
	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	/**
	 * @return the registerOwner
	 */
	public String getRegisterOwner() {
		return registerOwner;
	}

	/**
	 * @param registerOwner the registerOwner to set
	 */
	public void setRegisterOwner(String registerOwner) {
		this.registerOwner = registerOwner;
	}

	/**
	 * @return the registerAddress1
	 */
	public String getRegisterAddress1() {
		return registerAddress1;
	}

	/**
	 * @param registerAddress1 the registerAddress1 to set
	 */
	public void setRegisterAddress1(String registerAddress1) {
		this.registerAddress1 = registerAddress1;
	}

	/**
	 * @return the registerAddress2
	 */
	public String getRegisterAddress2() {
		return registerAddress2;
	}

	/**
	 * @param registerAddress2 the registerAddress2 to set
	 */
	public void setRegisterAddress2(String registerAddress2) {
		this.registerAddress2 = registerAddress2;
	}

	/**
	 * @return the businessCondition
	 */
	public String getBusinessCondition() {
		return businessCondition;
	}

	/**
	 * @param businessCondition the businessCondition to set
	 */
	public void setBusinessCondition(String businessCondition) {
		this.businessCondition = businessCondition;
	}

	/**
	 * @return the businessType
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactDepartment
	 */
	public String getContactDepartment() {
		return contactDepartment;
	}

	/**
	 * @param contactDepartment the contactDepartment to set
	 */
	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * @param contactEmail the contactEmail to set
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * @return the contactPhoneNumber
	 */
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}

	/**
	 * @param contactPhoneNumber the contactPhoneNumber to set
	 */
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}

	/**
	 * @return the contactFaxNumber
	 */
	public String getContactFaxNumber() {
		return contactFaxNumber;
	}

	/**
	 * @param contactFaxNumber the contactFaxNumber to set
	 */
	public void setContactFaxNumber(String contactFaxNumber) {
		this.contactFaxNumber = contactFaxNumber;
	}

	/**
	 * @return the regulatorPressure
	 */
	public String getRegulatorPressure() {
		return regulatorPressure;
	}

	/**
	 * @param regulatorPressure the regulatorPressure to set
	 */
	public void setRegulatorPressure(String regulatorPressure) {
		this.regulatorPressure = regulatorPressure;
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
	 * @return the applyPrice
	 */
	public String getApplyPrice() {
		return applyPrice;
	}

	/**
	 * @param applyPrice the applyPrice to set
	 */
	public void setApplyPrice(String applyPrice) {
		this.applyPrice = applyPrice;
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
	 * @return the individualPrice
	 */
	public String getIndividualPrice() {
		return individualPrice;
	}

	/**
	 * @param individualPrice the individualPrice to set
	 */
	public void setIndividualPrice(String individualPrice) {
		this.individualPrice = individualPrice;
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
	 * @return the priceMode
	 */
	public String getPriceMode() {
		return priceMode;
	}

	/**
	 * @param priceMode the priceMode to set
	 */
	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
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
	 * @return the defaultRate
	 */
	public String getDefaultRate() {
		return defaultRate;
	}

	/**
	 * @param defaultRate the defaultRate to set
	 */
	public void setDefaultRate(String defaultRate) {
		this.defaultRate = defaultRate;
	}

	/**
	 * @return the discountRate
	 */
	public String getDiscountRate() {
		return discountRate;
	}

	/**
	 * @param discountRate the discountRate to set
	 */
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	/**
	 * @return the maintenanceFee
	 */
	public String getMaintenanceFee() {
		return maintenanceFee;
	}

	/**
	 * @param maintenanceFee the maintenanceFee to set
	 */
	public void setMaintenanceFee(String maintenanceFee) {
		this.maintenanceFee = maintenanceFee;
	}

	/**
	 * @return the installationFee
	 */
	public String getInstallationFee() {
		return installationFee;
	}

	/**
	 * @param installationFee the installationFee to set
	 */
	public void setInstallationFee(String installationFee) {
		this.installationFee = installationFee;
	}

	/**
	 * @return the gaugeReplacementFee
	 */
	public String getGaugeReplacementFee() {
		return gaugeReplacementFee;
	}

	/**
	 * @param gaugeReplacementFee the gaugeReplacementFee to set
	 */
	public void setGaugeReplacementFee(String gaugeReplacementFee) {
		this.gaugeReplacementFee = gaugeReplacementFee;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the paymentTypeName
	 */
	public String getPaymentTypeName() {
		return paymentTypeName;
	}

	/**
	 * @param paymentTypeName the paymentTypeName to set
	 */
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the readMeterDay
	 */
	public String getReadMeterDay() {
		return readMeterDay;
	}

	/**
	 * @param readMeterDay the readMeterDay to set
	 */
	public void setReadMeterDay(String readMeterDay) {
		this.readMeterDay = readMeterDay;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the contractDate
	 */
	public String getContractDate() {
		return contractDate;
	}

	/**
	 * @param contractDate the contractDate to set
	 */
	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	/**
	 * @return the latestSaftyCheckDate
	 */
	public String getLatestSaftyCheckDate() {
		return latestSaftyCheckDate;
	}

	/**
	 * @param latestSaftyCheckDate the latestSaftyCheckDate to set
	 */
	public void setLatestSaftyCheckDate(String latestSaftyCheckDate) {
		this.latestSaftyCheckDate = latestSaftyCheckDate;
	}

	/**
	 * @return the contractName
	 */
	public String getContractName() {
		return contractName;
	}

	/**
	 * @param contractName the contractName to set
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	/**
	 * @return the contracterResidentNumber
	 */
	public String getContracterResidentNumber() {
		return contracterResidentNumber;
	}

	/**
	 * @param contracterResidentNumber the contracterResidentNumber to set
	 */
	public void setContracterResidentNumber(String contracterResidentNumber) {
		this.contracterResidentNumber = contracterResidentNumber;
	}

	/**
	 * @return the containerOwnerType
	 */
	public String getContainerOwnerType() {
		return containerOwnerType;
	}

	/**
	 * @param containerOwnerType the containerOwnerType to set
	 */
	public void setContainerOwnerType(String containerOwnerType) {
		this.containerOwnerType = containerOwnerType;
	}

	/**
	 * @return the facilityOwnerType
	 */
	public String getFacilityOwnerType() {
		return facilityOwnerType;
	}

	/**
	 * @param facilityOwnerType the facilityOwnerType to set
	 */
	public void setFacilityOwnerType(String facilityOwnerType) {
		this.facilityOwnerType = facilityOwnerType;
	}

	/**
	 * @return the facilityOkYesNo
	 */
	public String getFacilityOkYesNo() {
		return facilityOkYesNo;
	}

	/**
	 * @param facilityOkYesNo the facilityOkYesNo to set
	 */
	public void setFacilityOkYesNo(String facilityOkYesNo) {
		this.facilityOkYesNo = facilityOkYesNo;
	}

	/**
	 * @return the switcherCapacity
	 */
	public String getSwitcherCapacity() {
		return switcherCapacity;
	}

	/**
	 * @param switcherCapacity the switcherCapacity to set
	 */
	public void setSwitcherCapacity(String switcherCapacity) {
		this.switcherCapacity = switcherCapacity;
	}

	/**
	 * @return the vaporizerCapacity
	 */
	public String getVaporizerCapacity() {
		return vaporizerCapacity;
	}

	/**
	 * @param vaporizerCapacity the vaporizerCapacity to set
	 */
	public void setVaporizerCapacity(String vaporizerCapacity) {
		this.vaporizerCapacity = vaporizerCapacity;
	}

	/**
	 * @return the gaugeVolume
	 */
	public String getGaugeVolume() {
		return gaugeVolume;
	}

	/**
	 * @param gaugeVolume the gaugeVolume to set
	 */
	public void setGaugeVolume(String gaugeVolume) {
		this.gaugeVolume = gaugeVolume;
	}

	/**
	 * @return the fuseCockQuantity
	 */
	public String getFuseCockQuantity() {
		return fuseCockQuantity;
	}

	/**
	 * @param fuseCockQuantity the fuseCockQuantity to set
	 */
	public void setFuseCockQuantity(String fuseCockQuantity) {
		this.fuseCockQuantity = fuseCockQuantity;
	}

	/**
	 * @return the hoseLength
	 */
	public String getHoseLength() {
		return hoseLength;
	}

	/**
	 * @param hoseLength the hoseLength to set
	 */
	public void setHoseLength(String hoseLength) {
		this.hoseLength = hoseLength;
	}

	/**
	 * @return the valve
	 */
	public String getValve() {
		return valve;
	}

	/**
	 * @param valve the valve to set
	 */
	public void setValve(String valve) {
		this.valve = valve;
	}

	/**
	 * @return the supplyPipe1
	 */
	public String getSupplyPipe1() {
		return supplyPipe1;
	}

	/**
	 * @param supplyPipe1 the supplyPipe1 to set
	 */
	public void setSupplyPipe1(String supplyPipe1) {
		this.supplyPipe1 = supplyPipe1;
	}

	/**
	 * @return the supplyPipe2
	 */
	public String getSupplyPipe2() {
		return supplyPipe2;
	}

	/**
	 * @param supplyPipe2 the supplyPipe2 to set
	 */
	public void setSupplyPipe2(String supplyPipe2) {
		this.supplyPipe2 = supplyPipe2;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerSearch [key=" + this.getKeyValue() + ", sequenceNumber=" 
				+ sequenceNumber + "areaCode=" 
				+ areaCode + ", customerCode="
				+ customerCode + ", customerType=" + customerType
				+ ", customerTypeName=" + customerTypeName
				+ ", customerStatusCode=" + customerStatusCode
				+ ", customerStatusName=" + customerStatusName
				+ ", customerName=" + customerName + ", buildingName="
				+ buildingName + ", userName=" + userName + ", phoneNumber="
				+ phoneNumber + ", mobileNumber=" + mobileNumber
				+ ", phoneNumber2=" + phoneNumber2 + ", phoneNumberFind="
				+ phoneNumberFind + ", mobileNumberFind=" + mobileNumberFind
				+ ", phoneNumberFind2=" + phoneNumberFind2 + ", address1="
				+ address1 + ", address2=" + address2 + ", remark1=" + remark1
				+ ", remark2=" + remark2 + ", employeeCode=" + employeeCode
				+ ", employeeName=" + employeeName + ", consumeTypeCode="
				+ consumeTypeCode + ", consumerTypeName=" + consumerTypeName
				+ ", freeInstallationFee=" + freeInstallationFee
				+ ", containerDeposit=" + containerDeposit
				+ ", weightReceivable=" + weightReceivable
				+ ", volumeReceivable=" + volumeReceivable + ", memo=" + memo
				+ ", vatType=" + vatType + ", weightPriceType="
				+ weightPriceType + ", salePriceDiscountRate="
				+ salePriceDiscountRate + ", issueTaxbillYesNo="
				+ issueTaxbillYesNo + ", registerNumberType="
				+ registerNumberType + ", registerNumber=" + registerNumber
				+ ", registerName=" + registerName + ", registerOwner="
				+ registerOwner + ", registerAddress1=" + registerAddress1
				+ ", registerAddress2=" + registerAddress2
				+ ", businessCondition=" + businessCondition
				+ ", businessType=" + businessType + ", contactName="
				+ contactName + ", contactDepartment=" + contactDepartment
				+ ", contactEmail=" + contactEmail + ", contactPhoneNumber="
				+ contactPhoneNumber + ", contactFaxNumber=" + contactFaxNumber
				+ ", regulatorPressure=" + regulatorPressure + ", priceType="
				+ priceType + ", applyPrice=" + applyPrice
				+ ", environmentPrice=" + environmentPrice
				+ ", individualPrice=" + individualPrice + ", discountPrice="
				+ discountPrice + ", priceMode=" + priceMode
				+ ", discountAmount=" + discountAmount + ", defaultRate="
				+ defaultRate + ", discountRate=" + discountRate
				+ ", maintenanceFee=" + maintenanceFee + ", installationFee="
				+ installationFee + ", gaugeReplacementFee="
				+ gaugeReplacementFee + ", paymentType=" + paymentType
				+ ", paymentTypeName=" + paymentTypeName + ", serialNumber="
				+ serialNumber + ", readMeterDay=" + readMeterDay
				+ ", contractNumber=" + contractNumber + ", contractDate="
				+ contractDate + ", latestSaftyCheckDate="
				+ latestSaftyCheckDate + ", contractName=" + contractName
				+ ", contracterResidentNumber=" + contracterResidentNumber
				+ ", containerOwnerType=" + containerOwnerType
				+ ", facilityOwnerType=" + facilityOwnerType
				+ ", facilityOkYesNo=" + facilityOkYesNo
				+ ", switcherCapacity=" + switcherCapacity
				+ ", vaporizerCapacity=" + vaporizerCapacity + ", gaugeVolume="
				+ gaugeVolume + ", fuseCockQuantity=" + fuseCockQuantity
				+ ", hoseLength=" + hoseLength + ", valve=" + valve
				+ ", supplyPipe1=" + supplyPipe1 + ", supplyPipe2="
				+ supplyPipe2 + "]";
	}

	/**
	 * XML 반환
	 * @return xml
	 */
	public String toXML() {
		return "<CustomerSearch><key>" + this.getKeyValue()
				+ "</key><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><areaCode>" + areaCode
				+ "</areaCode><customerCode>" + customerCode
				+ "</customerCode><customerType>" + customerType
				+ "</customerType><customerTypeName>" + customerTypeName
				+ "</customerTypeName><customerStatusCode>"
				+ customerStatusCode
				+ "</customerStatusCode><customerStatusName>"
				+ customerStatusName + "</customerStatusName><customerName><![CDATA["
				+ customerName + "]]></customerName><buildingName><![CDATA[" + buildingName
				+ "]]></buildingName><userName><![CDATA[" + userName
				+ "]]></userName><phoneNumber>" + phoneNumber
				+ "</phoneNumber><mobileNumber>" + mobileNumber
				+ "</mobileNumber><phoneNumber2>" + phoneNumber2
				+ "</phoneNumber2><phoneNumberFind>" + phoneNumberFind
				+ "</phoneNumberFind><mobileNumberFind>" + mobileNumberFind
				+ "</mobileNumberFind><phoneNumberFind2>" + phoneNumberFind2
				+ "</phoneNumberFind2><address1><![CDATA[" + address1
				+ "]]></address1><address2><![CDATA[" + address2 + "]]></address2><remark1><![CDATA["
				+ remark1 + "]]></remark1><remark2><![CDATA[" + remark2
				+ "]]></remark2><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName><consumeTypeCode>" + consumeTypeCode
				+ "</consumeTypeCode><consumerTypeName>" + consumerTypeName
				+ "</consumerTypeName><freeInstallationFee>"
				+ freeInstallationFee
				+ "</freeInstallationFee><containerDeposit>" + containerDeposit
				+ "</containerDeposit><weightReceivable>" + weightReceivable
				+ "</weightReceivable><volumeReceivable>" + volumeReceivable
				+ "</volumeReceivable><memo><![CDATA[" + memo + "]]></memo><vatType>"
				+ vatType + "</vatType><weightPriceType>" + weightPriceType
				+ "</weightPriceType><salePriceDiscountRate>"
				+ salePriceDiscountRate
				+ "</salePriceDiscountRate><issueTaxbillYesNo>"
				+ issueTaxbillYesNo
				+ "</issueTaxbillYesNo><registerNumberType>"
				+ registerNumberType + "</registerNumberType><registerNumber>"
				+ registerNumber + "</registerNumber><registerName>"
				+ registerName + "</registerName><registerOwner>"
				+ registerOwner + "</registerOwner><registerAddress1>"
				+ registerAddress1 + "</registerAddress1><registerAddress2>"
				+ registerAddress2 + "</registerAddress2><businessCondition>"
				+ businessCondition + "</businessCondition><businessType>"
				+ businessType + "</businessType><contactName><![CDATA[" + contactName
				+ "]]></contactName><contactDepartment>" + contactDepartment
				+ "</contactDepartment><contactEmail><![CDATA[" + contactEmail
				+ "]]></contactEmail><contactPhoneNumber>" + contactPhoneNumber
				+ "</contactPhoneNumber><contactFaxNumber>" + contactFaxNumber
				+ "</contactFaxNumber><regulatorPressure>" + regulatorPressure
				+ "</regulatorPressure><priceType>" + priceType
				+ "</priceType><applyPrice>" + applyPrice
				+ "</applyPrice><environmentPrice>" + environmentPrice
				+ "</environmentPrice><individualPrice>" + individualPrice
				+ "</individualPrice><discountPrice>" + discountPrice
				+ "</discountPrice><priceMode>" + priceMode
				+ "</priceMode><discountAmount>" + discountAmount
				+ "</discountAmount><defaultRate>" + defaultRate
				+ "</defaultRate><discountRate>" + discountRate
				+ "</discountRate><maintenanceFee>" + maintenanceFee
				+ "</maintenanceFee><installationFee>" + installationFee
				+ "</installationFee><gaugeReplacementFee>"
				+ gaugeReplacementFee + "</gaugeReplacementFee><paymentType>"
				+ paymentType + "</paymentType><paymentTypeName>"
				+ paymentTypeName + "</paymentTypeName><serialNumber>"
				+ serialNumber + "</serialNumber><readMeterDay>" + readMeterDay
				+ "</readMeterDay><contractNumber>" + contractNumber
				+ "</contractNumber><contractDate>" + contractDate
				+ "</contractDate><latestSaftyCheckDate>"
				+ latestSaftyCheckDate
				+ "</latestSaftyCheckDate><contractName><![CDATA[" + contractName
				+ "]]></contractName><contracterResidentNumber>"
				+ contracterResidentNumber
				+ "</contracterResidentNumber><containerOwnerType>"
				+ containerOwnerType
				+ "</containerOwnerType><facilityOwnerType>"
				+ facilityOwnerType + "</facilityOwnerType><facilityOkYesNo>"
				+ facilityOkYesNo + "</facilityOkYesNo><switcherCapacity>"
				+ switcherCapacity + "</switcherCapacity><vaporizerCapacity>"
				+ vaporizerCapacity + "</vaporizerCapacity><gaugeVolume>"
				+ gaugeVolume + "</gaugeVolume><fuseCockQuantity>"
				+ fuseCockQuantity + "</fuseCockQuantity><hoseLength>"
				+ hoseLength + "</hoseLength><valve>" + valve
				+ "</valve><supplyPipe1>" + supplyPipe1
				+ "</supplyPipe1><supplyPipe2>" + supplyPipe2
				+ "</supplyPipe2></CustomerSearch>";
	}
	
	

	
}
