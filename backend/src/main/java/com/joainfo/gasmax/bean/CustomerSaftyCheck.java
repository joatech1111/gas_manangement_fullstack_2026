package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

public class CustomerSaftyCheck {	 
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
	 * ANZ_GongDate	
	 * 공급계약일		
	 */
	 private String contractDate;

	/**
	 * ANZ_Date	
	 * 정기점검일		
	 */
	 private String scheduledCheckDate;

	/**
	 * ANZ_SW_Code	
	 * 점검 사원코드		
	 */
	 private String employeeCode;

	/**
	 * ANZ_SW_Name	
	 * 점검 사원명		
	 */
	 private String employeeName;

	/**
	 * ANZ_CustName	
	 * 계약자 성명		
	 */
	 private String contractName;

	/**
	 * ANZ_Tel	
	 * 전화번호		
	 */
	 private String phoneNumber;

	/**
	 * ANZ_A_01	
	 * 배관	강관	
	 */
	 private String pipeLength1;

	/**
	 * ANZ_A_02		
	 * 동관	
	 */
	 private String pipeLength2;

	/**
	 * ANZ_A_03		
	 * 호스	
	 */
	 private String pipeLength3;

	/**
	 * ANZ_A_04		
	 * 기타 종류	
	 */
	 private String pipeLength4;

	/**
	 * ANZ_A_05		
	 * 기타 m	
	 */
	 private String pipeLength5;

	/**
	 * ANZ_B_01	
	 * 중간밸브	볼밸브	
	 */
	 private String valveQuantity1;

	/**
	 * ANZ_B_02		
	 * 퓨즈콕	
	 */
	 private String valveQuantity2;

	/**
	 * ANZ_B_03		
	 * 호스콕	
	 */
	 private String valveQuantity3;

	/**
	 * ANZ_B_04		
	 * 기타 명	
	 */
	 private String valveQuantity4;

	/**
	 * ANZ_B_05		
	 * 기타 (개)	
	 */
	 private String valveQuantity5;

	/**
	 * ANZ_C_01	
	 * 기타	기타 명	
	 */
	 private String etcEquipmentName1;

	/**
	 * ANZ_C_02		
	 * 기타 (개)	
	 */
	 private String etcEquipmentQuantity1;

	/**
	 * ANZ_C_03		
	 * 기타 명	
	 */
	 private String etcEquipmentName2;

	/**
	 * ANZ_C_04		
	 * 기타 (개)	
	 */
	 private String etcEquipmentQuantity2;

	/**
	 * ANZ_C_05		
	 * 기타 명	
	 */
	 private String etcEquipmentName3;

	/**
	 * ANZ_C_06		
	 * 기타 (개)	
	 */
	 private String etcEquipmentQuantity3;

	/**
	 * ANZ_C_07		
	 * 기타 명	
	 */
	 private String etcEquipmentName4;

	/**
	 * ANZ_C_08		
	 * 기타 (개)	
	 */
	 private String etcEquipmentQuantity4;

	/**
	 * ANZ_D_01	
	 * 연소기-렌지	2구렌지	
	 */
	 private String combustorRange1;

	/**
	 * ANZ_D_02		
	 * 3구렌지	
	 */
	 private String combustorRange2;

	/**
	 * ANZ_D_03		
	 * 오븐렌지	
	 */
	 private String combustorRange3;

	/**
	 * ANZ_D_04		
	 * 기타 명	
	 */
	 private String combustorRangeEtcName;

	/**
	 * ANZ_D_05		
	 * 기타 (개)	
	 */
	 private String combustorRangeEtcQuantity;

	/**
	 * ANZ_E_01	
	 * 연소기-보일러	형식	0, 1.FF, 2.FE, 3.CF
	 */
	 private String combustorBoilerType;

	/**
	 * ANZ_E_02		
	 * 위치	0, 1.옥내, 2.옥외, 3.전용
	 */
	 private String combustorBoilerPosition;

	/**
	 * ANZ_E_03		
	 * 가스소비량	
	 */
	 private String combustorBoilerConsumption;

	/**
	 * ANZ_E_04		
	 * 시공자	
	 */
	 private String combustorBoilerInstaller;

	/**
	 * ANZ_F_01	
	 * 연소기-온수기	형식	0, 1.FF, 2.FE, 3.CF
	 */
	 private String combustorHeaterType;

	/**
	 * ANZ_F_02		
	 * 위치	0, 1.옥내, 2.옥외
	 */
	 private String combustorHeaterPosition;

	/**
	 * ANZ_F_03		
	 * 가스소비량	
	 */
	 private String combustorHeaterConsumption;

	/**
	 * ANZ_F_04		
	 * 시공자	
	 */
	 private String combustorHeaterInstaller;

	/**
	 * ANZ_G_01	
	 * 연소기-기타	기타 명	
	 */
	 private String combustorEtcName1;

	/**
	 * ANZ_G_02		
	 * 기타 (개)	
	 */
	 private String combustorEtcQuantity1;

	/**
	 * ANZ_G_03		
	 * 기타 명	
	 */
	 private String combustorEtcName2;

	/**
	 * ANZ_G_04		
	 * 기타 (개)	
	 */
	 private String combustorEtcQuantity2;

	/**
	 * ANZ_G_05		
	 * 기타 명	
	 */
	 private String combustorEtcName3;

	/**
	 * ANZ_G_06		
	 * 기타 (개)	
	 */
	 private String combustorEtcQuantity3;

	/**
	 * ANZ_G_07		
	 * 기타 명	
	 */
	 private String combustorEtcName4;

	/**
	 * ANZ_G_08		
	 * 기타 (개)	
	 */
	 private String combustorEtcQuantity4;

	/**
	 * ANZ_Ga	
	 * 시설현황	가	0, 1.적합, 2.부적합
	 */
	 private String acceptable1;

	/**
	 * ANZ_Na		
	 * 나	0, 1.적합, 2.부적합
	 */
	 private String acceptable2;

	/**
	 * ANZ_Da		
	 * 다	0, 1.적합, 2.부적합
	 */
	 private String acceptable3;

	/**
	 * ANZ_Ra		
	 * 라	0, 1.적합, 2.부적합
	 */
	 private String acceptable4;

	/**
	 * ANZ_Ma		
	 * 마	0, 1.적합, 2.부적합
	 */
	 private String acceptable5;

	/**
	 * ANZ_Ba		
	 * 바	0, 1.적합, 2.부적합
	 */
	 private String acceptable6;

	/**
	 * ANZ_Sa		
	 * 사	0, 1.적합, 2.부적합
	 */
	 private String acceptable7;

	/**
	 * ANZ_AA		
	 * 아	0, 1.적합, 2.부적합
	 */
	 private String acceptable8;

	/**
	 * ANZ_Ja		
	 * 자	0, 1.적합, 2.부적합
	 */
	 private String acceptable9;

	/**
	 * ANZ_Cha		
	 * 차	0, 1.적합, 2.부적합
	 */
	 private String acceptable10;

	/**
	 * ANZ_Car		
	 * 카	0, 1.적합, 2.부적합
	 */
	 private String acceptable11;
	 
	/**
	 * ANZ_Cha_IN		
	 * 카	0, 1.적합, 2.부적합
	 */
	 private String acceptable12;

	/**
	 * ANZ_Gae_01	
	 * 개선통지	내용1	
	 */
	 private String notifyRemark1;

	/**
	 * ANZ_Gae_02		
	 * 내용2	
	 */
	 private String notifyRemark2;

	/**
	 * ANZ_Gae_03	
	 * 권장사항	내용1	
	 */
	 private String recommendation1;

	/**
	 * ANZ_Gae_04		
	 * 내용2	
	 */
	 private String recommendation2;

	/**
	 * ANZ_Folder	
	 * 서명화일 저장경로(URL)		
	 */
	 private String signatureFilePath;

	/**
	 * ANZ_FileName	
	 * 파일명(Area_Code+Cu_Code+Sno)		
	 */
	 private String signatureFileName;

	/**
	 * ANZ_User_ID	
	 * APP 사용자명		
	 */
	 private String userId;

	/**
	 * ANZ_Date_Time	
	 * 등록/수정일자		
	 */
	 private String modifyDate;
	 
	/**	
	 * 서명		
	 */
	 private String signatureImage;

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
	 * @return the pipeLength1
	 */
	public String getPipeLength1() {
		return pipeLength1;
	}

	/**
	 * @param pipeLength1 the pipeLength1 to set
	 */
	public void setPipeLength1(String pipeLength1) {
		this.pipeLength1 = pipeLength1;
	}

	/**
	 * @return the pipeLength2
	 */
	public String getPipeLength2() {
		return pipeLength2;
	}

	/**
	 * @param pipeLength2 the pipeLength2 to set
	 */
	public void setPipeLength2(String pipeLength2) {
		this.pipeLength2 = pipeLength2;
	}

	/**
	 * @return the pipeLength3
	 */
	public String getPipeLength3() {
		return pipeLength3;
	}

	/**
	 * @param pipeLength3 the pipeLength3 to set
	 */
	public void setPipeLength3(String pipeLength3) {
		this.pipeLength3 = pipeLength3;
	}

	/**
	 * @return the pipeLength4
	 */
	public String getPipeLength4() {
		return pipeLength4;
	}

	/**
	 * @param pipeLength4 the pipeLength4 to set
	 */
	public void setPipeLength4(String pipeLength4) {
		this.pipeLength4 = pipeLength4;
	}

	/**
	 * @return the pipeLength5
	 */
	public String getPipeLength5() {
		return pipeLength5;
	}

	/**
	 * @param pipeLength5 the pipeLength5 to set
	 */
	public void setPipeLength5(String pipeLength5) {
		this.pipeLength5 = pipeLength5;
	}

	/**
	 * @return the valveQuantity1
	 */
	public String getValveQuantity1() {
		return valveQuantity1;
	}

	/**
	 * @param valveQuantity1 the valveQuantity1 to set
	 */
	public void setValveQuantity1(String valveQuantity1) {
		this.valveQuantity1 = valveQuantity1;
	}

	/**
	 * @return the valveQuantity2
	 */
	public String getValveQuantity2() {
		return valveQuantity2;
	}

	/**
	 * @param valveQuantity2 the valveQuantity2 to set
	 */
	public void setValveQuantity2(String valveQuantity2) {
		this.valveQuantity2 = valveQuantity2;
	}

	/**
	 * @return the valveQuantity3
	 */
	public String getValveQuantity3() {
		return valveQuantity3;
	}

	/**
	 * @param valveQuantity3 the valveQuantity3 to set
	 */
	public void setValveQuantity3(String valveQuantity3) {
		this.valveQuantity3 = valveQuantity3;
	}

	/**
	 * @return the valveQuantity4
	 */
	public String getValveQuantity4() {
		return valveQuantity4;
	}

	/**
	 * @param valveQuantity4 the valveQuantity4 to set
	 */
	public void setValveQuantity4(String valveQuantity4) {
		this.valveQuantity4 = valveQuantity4;
	}

	/**
	 * @return the valveQuantity5
	 */
	public String getValveQuantity5() {
		return valveQuantity5;
	}

	/**
	 * @param valveQuantity5 the valveQuantity5 to set
	 */
	public void setValveQuantity5(String valveQuantity5) {
		this.valveQuantity5 = valveQuantity5;
	}

	/**
	 * @return the etcEquipmentName1
	 */
	public String getEtcEquipmentName1() {
		return etcEquipmentName1;
	}

	/**
	 * @param etcEquipmentName1 the etcEquipmentName1 to set
	 */
	public void setEtcEquipmentName1(String etcEquipmentName1) {
		this.etcEquipmentName1 = etcEquipmentName1;
	}

	/**
	 * @return the etcEquipmentQuantity1
	 */
	public String getEtcEquipmentQuantity1() {
		return etcEquipmentQuantity1;
	}

	/**
	 * @param etcEquipmentQuantity1 the etcEquipmentQuantity1 to set
	 */
	public void setEtcEquipmentQuantity1(String etcEquipmentQuantity1) {
		this.etcEquipmentQuantity1 = etcEquipmentQuantity1;
	}

	/**
	 * @return the etcEquipmentName2
	 */
	public String getEtcEquipmentName2() {
		return etcEquipmentName2;
	}

	/**
	 * @param etcEquipmentName2 the etcEquipmentName2 to set
	 */
	public void setEtcEquipmentName2(String etcEquipmentName2) {
		this.etcEquipmentName2 = etcEquipmentName2;
	}

	/**
	 * @return the etcEquipmentQuantity2
	 */
	public String getEtcEquipmentQuantity2() {
		return etcEquipmentQuantity2;
	}

	/**
	 * @param etcEquipmentQuantity2 the etcEquipmentQuantity2 to set
	 */
	public void setEtcEquipmentQuantity2(String etcEquipmentQuantity2) {
		this.etcEquipmentQuantity2 = etcEquipmentQuantity2;
	}

	/**
	 * @return the etcEquipmentName3
	 */
	public String getEtcEquipmentName3() {
		return etcEquipmentName3;
	}

	/**
	 * @param etcEquipmentName3 the etcEquipmentName3 to set
	 */
	public void setEtcEquipmentName3(String etcEquipmentName3) {
		this.etcEquipmentName3 = etcEquipmentName3;
	}

	/**
	 * @return the etcEquipmentQuantity3
	 */
	public String getEtcEquipmentQuantity3() {
		return etcEquipmentQuantity3;
	}

	/**
	 * @param etcEquipmentQuantity3 the etcEquipmentQuantity3 to set
	 */
	public void setEtcEquipmentQuantity3(String etcEquipmentQuantity3) {
		this.etcEquipmentQuantity3 = etcEquipmentQuantity3;
	}

	/**
	 * @return the etcEquipmentName4
	 */
	public String getEtcEquipmentName4() {
		return etcEquipmentName4;
	}

	/**
	 * @param etcEquipmentName4 the etcEquipmentName4 to set
	 */
	public void setEtcEquipmentName4(String etcEquipmentName4) {
		this.etcEquipmentName4 = etcEquipmentName4;
	}

	/**
	 * @return the etcEquipmentQuantity4
	 */
	public String getEtcEquipmentQuantity4() {
		return etcEquipmentQuantity4;
	}

	/**
	 * @param etcEquipmentQuantity4 the etcEquipmentQuantity4 to set
	 */
	public void setEtcEquipmentQuantity4(String etcEquipmentQuantity4) {
		this.etcEquipmentQuantity4 = etcEquipmentQuantity4;
	}

	/**
	 * @return the combustorRange1
	 */
	public String getCombustorRange1() {
		return combustorRange1;
	}

	/**
	 * @param combustorRange1 the combustorRange1 to set
	 */
	public void setCombustorRange1(String combustorRange1) {
		this.combustorRange1 = combustorRange1;
	}

	/**
	 * @return the combustorRange2
	 */
	public String getCombustorRange2() {
		return combustorRange2;
	}

	/**
	 * @param combustorRange2 the combustorRange2 to set
	 */
	public void setCombustorRange2(String combustorRange2) {
		this.combustorRange2 = combustorRange2;
	}

	/**
	 * @return the combustorRange3
	 */
	public String getCombustorRange3() {
		return combustorRange3;
	}

	/**
	 * @param combustorRange3 the combustorRange3 to set
	 */
	public void setCombustorRange3(String combustorRange3) {
		this.combustorRange3 = combustorRange3;
	}

	/**
	 * @return the combustorRangeEtcName
	 */
	public String getCombustorRangeEtcName() {
		return combustorRangeEtcName;
	}

	/**
	 * @param combustorRangeEtcName the combustorRangeEtcName to set
	 */
	public void setCombustorRangeEtcName(String combustorRangeEtcName) {
		this.combustorRangeEtcName = combustorRangeEtcName;
	}

	/**
	 * @return the combustorRangeEtcQuantity
	 */
	public String getCombustorRangeEtcQuantity() {
		return combustorRangeEtcQuantity;
	}

	/**
	 * @param combustorRangeEtcQuantity the combustorRangeEtcQuantity to set
	 */
	public void setCombustorRangeEtcQuantity(String combustorRangeEtcQuantity) {
		this.combustorRangeEtcQuantity = combustorRangeEtcQuantity;
	}

	/**
	 * @return the combustorBoilerType
	 */
	public String getCombustorBoilerType() {
		return combustorBoilerType;
	}

	/**
	 * @param combustorBoilerType the combustorBoilerType to set
	 */
	public void setCombustorBoilerType(String combustorBoilerType) {
		this.combustorBoilerType = combustorBoilerType;
	}

	/**
	 * @return the combustorBoilerPosition
	 */
	public String getCombustorBoilerPosition() {
		return combustorBoilerPosition;
	}

	/**
	 * @param combustorBoilerPosition the combustorBoilerPosition to set
	 */
	public void setCombustorBoilerPosition(String combustorBoilerPosition) {
		this.combustorBoilerPosition = combustorBoilerPosition;
	}

	/**
	 * @return the combustorBoilerConsumption
	 */
	public String getCombustorBoilerConsumption() {
		return combustorBoilerConsumption;
	}

	/**
	 * @param combustorBoilerConsumption the combustorBoilerConsumption to set
	 */
	public void setCombustorBoilerConsumption(String combustorBoilerConsumption) {
		this.combustorBoilerConsumption = combustorBoilerConsumption;
	}

	/**
	 * @return the combustorBoilerInstaller
	 */
	public String getCombustorBoilerInstaller() {
		return combustorBoilerInstaller;
	}

	/**
	 * @param combustorBoilerInstaller the combustorBoilerInstaller to set
	 */
	public void setCombustorBoilerInstaller(String combustorBoilerInstaller) {
		this.combustorBoilerInstaller = combustorBoilerInstaller;
	}

	/**
	 * @return the combustorHeaterType
	 */
	public String getCombustorHeaterType() {
		return combustorHeaterType;
	}

	/**
	 * @param combustorHeaterType the combustorHeaterType to set
	 */
	public void setCombustorHeaterType(String combustorHeaterType) {
		this.combustorHeaterType = combustorHeaterType;
	}

	/**
	 * @return the combustorHeaterPosition
	 */
	public String getCombustorHeaterPosition() {
		return combustorHeaterPosition;
	}

	/**
	 * @param combustorHeaterPosition the combustorHeaterPosition to set
	 */
	public void setCombustorHeaterPosition(String combustorHeaterPosition) {
		this.combustorHeaterPosition = combustorHeaterPosition;
	}

	/**
	 * @return the combustorHeaterConsumption
	 */
	public String getCombustorHeaterConsumption() {
		return combustorHeaterConsumption;
	}

	/**
	 * @param combustorHeaterConsumption the combustorHeaterConsumption to set
	 */
	public void setCombustorHeaterConsumption(String combustorHeaterConsumption) {
		this.combustorHeaterConsumption = combustorHeaterConsumption;
	}

	/**
	 * @return the combustorHeaterInstaller
	 */
	public String getCombustorHeaterInstaller() {
		return combustorHeaterInstaller;
	}

	/**
	 * @param combustorHeaterInstaller the combustorHeaterInstaller to set
	 */
	public void setCombustorHeaterInstaller(String combustorHeaterInstaller) {
		this.combustorHeaterInstaller = combustorHeaterInstaller;
	}

	/**
	 * @return the combustorEtcName1
	 */
	public String getCombustorEtcName1() {
		return combustorEtcName1;
	}

	/**
	 * @param combustorEtcName1 the combustorEtcName1 to set
	 */
	public void setCombustorEtcName1(String combustorEtcName1) {
		this.combustorEtcName1 = combustorEtcName1;
	}

	/**
	 * @return the combustorEtcQuantity1
	 */
	public String getCombustorEtcQuantity1() {
		return combustorEtcQuantity1;
	}

	/**
	 * @param combustorEtcQuantity1 the combustorEtcQuantity1 to set
	 */
	public void setCombustorEtcQuantity1(String combustorEtcQuantity1) {
		this.combustorEtcQuantity1 = combustorEtcQuantity1;
	}

	/**
	 * @return the combustorEtcName2
	 */
	public String getCombustorEtcName2() {
		return combustorEtcName2;
	}

	/**
	 * @param combustorEtcName2 the combustorEtcName2 to set
	 */
	public void setCombustorEtcName2(String combustorEtcName2) {
		this.combustorEtcName2 = combustorEtcName2;
	}

	/**
	 * @return the combustorEtcQuantity2
	 */
	public String getCombustorEtcQuantity2() {
		return combustorEtcQuantity2;
	}

	/**
	 * @param combustorEtcQuantity2 the combustorEtcQuantity2 to set
	 */
	public void setCombustorEtcQuantity2(String combustorEtcQuantity2) {
		this.combustorEtcQuantity2 = combustorEtcQuantity2;
	}

	/**
	 * @return the combustorEtcName3
	 */
	public String getCombustorEtcName3() {
		return combustorEtcName3;
	}

	/**
	 * @param combustorEtcName3 the combustorEtcName3 to set
	 */
	public void setCombustorEtcName3(String combustorEtcName3) {
		this.combustorEtcName3 = combustorEtcName3;
	}

	/**
	 * @return the combustorEtcQuantity3
	 */
	public String getCombustorEtcQuantity3() {
		return combustorEtcQuantity3;
	}

	/**
	 * @param combustorEtcQuantity3 the combustorEtcQuantity3 to set
	 */
	public void setCombustorEtcQuantity3(String combustorEtcQuantity3) {
		this.combustorEtcQuantity3 = combustorEtcQuantity3;
	}

	/**
	 * @return the combustorEtcName4
	 */
	public String getCombustorEtcName4() {
		return combustorEtcName4;
	}

	/**
	 * @param combustorEtcName4 the combustorEtcName4 to set
	 */
	public void setCombustorEtcName4(String combustorEtcName4) {
		this.combustorEtcName4 = combustorEtcName4;
	}

	/**
	 * @return the combustorEtcQuantity4
	 */
	public String getCombustorEtcQuantity4() {
		return combustorEtcQuantity4;
	}

	/**
	 * @param combustorEtcQuantity4 the combustorEtcQuantity4 to set
	 */
	public void setCombustorEtcQuantity4(String combustorEtcQuantity4) {
		this.combustorEtcQuantity4 = combustorEtcQuantity4;
	}

	/**
	 * @return the acceptable1
	 */
	public String getAcceptable1() {
		return acceptable1;
	}

	/**
	 * @param acceptable1 the acceptable1 to set
	 */
	public void setAcceptable1(String acceptable1) {
		this.acceptable1 = acceptable1;
	}

	/**
	 * @return the acceptable2
	 */
	public String getAcceptable2() {
		return acceptable2;
	}

	/**
	 * @param acceptable2 the acceptable2 to set
	 */
	public void setAcceptable2(String acceptable2) {
		this.acceptable2 = acceptable2;
	}

	/**
	 * @return the acceptable3
	 */
	public String getAcceptable3() {
		return acceptable3;
	}

	/**
	 * @param acceptable3 the acceptable3 to set
	 */
	public void setAcceptable3(String acceptable3) {
		this.acceptable3 = acceptable3;
	}

	/**
	 * @return the acceptable4
	 */
	public String getAcceptable4() {
		return acceptable4;
	}

	/**
	 * @param acceptable4 the acceptable4 to set
	 */
	public void setAcceptable4(String acceptable4) {
		this.acceptable4 = acceptable4;
	}

	/**
	 * @return the acceptable5
	 */
	public String getAcceptable5() {
		return acceptable5;
	}

	/**
	 * @param acceptable5 the acceptable5 to set
	 */
	public void setAcceptable5(String acceptable5) {
		this.acceptable5 = acceptable5;
	}

	/**
	 * @return the acceptable6
	 */
	public String getAcceptable6() {
		return acceptable6;
	}

	/**
	 * @param acceptable6 the acceptable6 to set
	 */
	public void setAcceptable6(String acceptable6) {
		this.acceptable6 = acceptable6;
	}

	/**
	 * @return the acceptable7
	 */
	public String getAcceptable7() {
		return acceptable7;
	}

	/**
	 * @param acceptable7 the acceptable7 to set
	 */
	public void setAcceptable7(String acceptable7) {
		this.acceptable7 = acceptable7;
	}

	/**
	 * @return the acceptable8
	 */
	public String getAcceptable8() {
		return acceptable8;
	}

	/**
	 * @param acceptable8 the acceptable8 to set
	 */
	public void setAcceptable8(String acceptable8) {
		this.acceptable8 = acceptable8;
	}

	/**
	 * @return the acceptable9
	 */
	public String getAcceptable9() {
		return acceptable9;
	}

	/**
	 * @param acceptable9 the acceptable9 to set
	 */
	public void setAcceptable9(String acceptable9) {
		this.acceptable9 = acceptable9;
	}

	/**
	 * @return the acceptable10
	 */
	public String getAcceptable10() {
		return acceptable10;
	}

	/**
	 * @param acceptable10 the acceptable10 to set
	 */
	public void setAcceptable10(String acceptable10) {
		this.acceptable10 = acceptable10;
	}

	/**
	 * @return the acceptable11
	 */
	public String getAcceptable11() {
		return acceptable11;
	}

	/**
	 * @param acceptable11 the acceptable11 to set
	 */
	public void setAcceptable11(String acceptable11) {
		this.acceptable11 = acceptable11;
	}
	
	/**
	 * @return the acceptable12
	 */
	public String getAcceptable12() {
		return acceptable12;
	}

	/**
	 * @param acceptable12 the acceptable12 to set
	 */
	public void setAcceptable12(String acceptable12) {
		this.acceptable12 = acceptable12;
	}

	/**
	 * @return the notifyRemark1
	 */
	public String getNotifyRemark1() {
		return notifyRemark1;
	}

	/**
	 * @param notifyRemark1 the notifyRemark1 to set
	 */
	public void setNotifyRemark1(String notifyRemark1) {
		this.notifyRemark1 = notifyRemark1;
	}

	/**
	 * @return the notifyRemark2
	 */
	public String getNotifyRemark2() {
		return notifyRemark2;
	}

	/**
	 * @param notifyRemark2 the notifyRemark2 to set
	 */
	public void setNotifyRemark2(String notifyRemark2) {
		this.notifyRemark2 = notifyRemark2;
	}

	/**
	 * @return the recommendation1
	 */
	public String getRecommendation1() {
		return recommendation1;
	}

	/**
	 * @param recommendation1 the recommendation1 to set
	 */
	public void setRecommendation1(String recommendation1) {
		this.recommendation1 = recommendation1;
	}

	/**
	 * @return the recommendation2
	 */
	public String getRecommendation2() {
		return recommendation2;
	}

	/**
	 * @param recommendation2 the recommendation2 to set
	 */
	public void setRecommendation2(String recommendation2) {
		this.recommendation2 = recommendation2;
	}

	/**
	 * @return the signatureFilePath
	 */
	public String getSignatureFilePath() {
		return signatureFilePath;
	}

	/**
	 * @param signatureFilePath the signatureFilePath to set
	 */
	public void setSignatureFilePath(String signatureFilePath) {
		this.signatureFilePath = signatureFilePath;
	}

	/**
	 * @return the signatureFileName
	 */
	public String getSignatureFileName() {
		return signatureFileName;
	}

	/**
	 * @param signatureFileName the signatureFileName to set
	 */
	public void setSignatureFileName(String signatureFileName) {
		this.signatureFileName = signatureFileName;
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
				+ ", contractDate="
				+ contractDate
				+ ", scheduledCheckDate="
				+ scheduledCheckDate
				+ ", employeeCode="
				+ employeeCode
				+ ", employeeName="
				+ employeeName
				+ ", contractName="
				+ contractName
				+ ", phoneNumber="
				+ phoneNumber
				+ ", pipeLength1="
				+ pipeLength1
				+ ", pipeLength2="
				+ pipeLength2
				+ ", pipeLength3="
				+ pipeLength3
				+ ", pipeLength4="
				+ pipeLength4
				+ ", pipeLength5="
				+ pipeLength5
				+ ", valveQuantity1="
				+ valveQuantity1
				+ ", valveQuantity2="
				+ valveQuantity2
				+ ", valveQuantity3="
				+ valveQuantity3
				+ ", valveQuantity4="
				+ valveQuantity4
				+ ", valveQuantity5="
				+ valveQuantity5
				+ ", etcEquipmentName1="
				+ etcEquipmentName1
				+ ", etcEquipmentQuantity1="
				+ etcEquipmentQuantity1
				+ ", etcEquipmentName2="
				+ etcEquipmentName2
				+ ", etcEquipmentQuantity2="
				+ etcEquipmentQuantity2
				+ ", etcEquipmentName3="
				+ etcEquipmentName3
				+ ", etcEquipmentQuantity3="
				+ etcEquipmentQuantity3
				+ ", etcEquipmentName4="
				+ etcEquipmentName4
				+ ", etcEquipmentQuantity4="
				+ etcEquipmentQuantity4
				+ ", combustorRange1="
				+ combustorRange1
				+ ", combustorRange2="
				+ combustorRange2
				+ ", combustorRange3="
				+ combustorRange3
				+ ", combustorRangeEtcName="
				+ combustorRangeEtcName
				+ ", combustorRangeEtcQuantity="
				+ combustorRangeEtcQuantity
				+ ", combustorBoilerType="
				+ combustorBoilerType
				+ ", combustorBoilerPosition="
				+ combustorBoilerPosition
				+ ", combustorBoilerConsumption="
				+ combustorBoilerConsumption
				+ ", combustorBoilerInstaller="
				+ combustorBoilerInstaller
				+ ", combustorHeaterType="
				+ combustorHeaterType
				+ ", combustorHeaterPosition="
				+ combustorHeaterPosition
				+ ", combustorHeaterConsumption="
				+ combustorHeaterConsumption
				+ ", combustorHeaterInstaller="
				+ combustorHeaterInstaller
				+ ", combustorEtcName1="
				+ combustorEtcName1
				+ ", combustorEtcQuantity1="
				+ combustorEtcQuantity1
				+ ", combustorEtcName2="
				+ combustorEtcName2
				+ ", combustorEtcQuantity2="
				+ combustorEtcQuantity2
				+ ", combustorEtcName3="
				+ combustorEtcName3
				+ ", combustorEtcQuantity3="
				+ combustorEtcQuantity3
				+ ", combustorEtcName4="
				+ combustorEtcName4
				+ ", combustorEtcQuantity4="
				+ combustorEtcQuantity4
				+ ", acceptable1="
				+ acceptable1
				+ ", acceptable2="
				+ acceptable2
				+ ", acceptable3="
				+ acceptable3
				+ ", acceptable4="
				+ acceptable4
				+ ", acceptable5="
				+ acceptable5
				+ ", acceptable6="
				+ acceptable6
				+ ", acceptable7="
				+ acceptable7
				+ ", acceptable8="
				+ acceptable8
				+ ", acceptable9="
				+ acceptable9
				+ ", acceptable10="
				+ acceptable10
				+ ", acceptable11="
				+ acceptable11
				+ ", acceptable12="
				+ acceptable12
				+ ", notifyRemark1="
				+ notifyRemark1
				+ ", notifyRemark2="
				+ notifyRemark2
				+ ", recommendation1="
				+ recommendation1
				+ ", recommendation2="
				+ recommendation2
				+ ", signatureFilePath="
				+ signatureFilePath
				+ ", signatureFileName="
				+ signatureFileName
				+ ", signatureImage="
				+ signatureImage
				+ ", userId="
				+ userId + ", modifyDate=" + modifyDate + "]";
	}

	/**
	 * @return XML 문자열 반환
	 */
	public String toXML() {
		return "<CustomerSaftyCheck><key>" + this.getKeyValue() + "</key><areaCode>"
				+ areaCode
				+ "</areaCode><customerCode>"
				+ customerCode
				+ "</customerCode><sequenceNumber>"
				+ sequenceNumber
				+ "</sequenceNumber><contractDate>"
				+ contractDate
				+ "</contractDate><scheduledCheckDate>"
				+ scheduledCheckDate
				+ "</scheduledCheckDate><employeeCode>"
				+ employeeCode
				+ "</employeeCode><employeeName><![CDATA["
				+ employeeName
				+ "]]></employeeName><contractName><![CDATA["
				+ contractName
				+ "]]></contractName><phoneNumber>"
				+ phoneNumber
				+ "</phoneNumber><pipeLength1>"
				+ pipeLength1
				+ "</pipeLength1><pipeLength2>"
				+ pipeLength2
				+ "</pipeLength2><pipeLength3>"
				+ pipeLength3
				+ "</pipeLength3><pipeLength4>"
				+ pipeLength4
				+ "</pipeLength4><pipeLength5>"
				+ pipeLength5
				+ "</pipeLength5><valveQuantity1>"
				+ valveQuantity1
				+ "</valveQuantity1><valveQuantity2>"
				+ valveQuantity2
				+ "</valveQuantity2><valveQuantity3>"
				+ valveQuantity3
				+ "</valveQuantity3><valveQuantity4>"
				+ valveQuantity4
				+ "</valveQuantity4><valveQuantity5>"
				+ valveQuantity5
				+ "</valveQuantity5><etcEquipmentName1>"
				+ etcEquipmentName1
				+ "</etcEquipmentName1><etcEquipmentQuantity1>"
				+ etcEquipmentQuantity1
				+ "</etcEquipmentQuantity1><etcEquipmentName2>"
				+ etcEquipmentName2
				+ "</etcEquipmentName2><etcEquipmentQuantity2>"
				+ etcEquipmentQuantity2
				+ "</etcEquipmentQuantity2><etcEquipmentName3>"
				+ etcEquipmentName3
				+ "</etcEquipmentName3><etcEquipmentQuantity3>"
				+ etcEquipmentQuantity3
				+ "</etcEquipmentQuantity3><etcEquipmentName4>"
				+ etcEquipmentName4
				+ "</etcEquipmentName4><etcEquipmentQuantity4>"
				+ etcEquipmentQuantity4
				+ "</etcEquipmentQuantity4><combustorRange1>"
				+ combustorRange1
				+ "</combustorRange1><combustorRange2>"
				+ combustorRange2
				+ "</combustorRange2><combustorRange3>"
				+ combustorRange3
				+ "</combustorRange3><combustorRangeEtcName>"
				+ combustorRangeEtcName
				+ "</combustorRangeEtcName><combustorRangeEtcQuantity>"
				+ combustorRangeEtcQuantity
				+ "</combustorRangeEtcQuantity><combustorBoilerType>"
				+ combustorBoilerType
				+ "</combustorBoilerType><combustorBoilerPosition>"
				+ combustorBoilerPosition
				+ "</combustorBoilerPosition><combustorBoilerConsumption>"
				+ combustorBoilerConsumption
				+ "</combustorBoilerConsumption><combustorBoilerInstaller>"
				+ combustorBoilerInstaller
				+ "</combustorBoilerInstaller><combustorHeaterType>"
				+ combustorHeaterType
				+ "</combustorHeaterType><combustorHeaterPosition>"
				+ combustorHeaterPosition
				+ "</combustorHeaterPosition><combustorHeaterConsumption>"
				+ combustorHeaterConsumption
				+ "</combustorHeaterConsumption><combustorHeaterInstaller>"
				+ combustorHeaterInstaller
				+ "</combustorHeaterInstaller><combustorEtcName1>"
				+ combustorEtcName1
				+ "</combustorEtcName1><combustorEtcQuantity1>"
				+ combustorEtcQuantity1
				+ "</combustorEtcQuantity1><combustorEtcName2>"
				+ combustorEtcName2
				+ "</combustorEtcName2><combustorEtcQuantity2>"
				+ combustorEtcQuantity2
				+ "</combustorEtcQuantity2><combustorEtcName3>"
				+ combustorEtcName3
				+ "</combustorEtcName3><combustorEtcQuantity3>"
				+ combustorEtcQuantity3
				+ "</combustorEtcQuantity3><combustorEtcName4>"
				+ combustorEtcName4
				+ "</combustorEtcName4><combustorEtcQuantity4>"
				+ combustorEtcQuantity4
				+ "</combustorEtcQuantity4><acceptable1>"
				+ acceptable1
				+ "</acceptable1><acceptable2>"
				+ acceptable2
				+ "</acceptable2><acceptable3>"
				+ acceptable3
				+ "</acceptable3><acceptable4>"
				+ acceptable4
				+ "</acceptable4><acceptable5>"
				+ acceptable5
				+ "</acceptable5><acceptable6>"
				+ acceptable6
				+ "</acceptable6><acceptable7>"
				+ acceptable7
				+ "</acceptable7><acceptable8>"
				+ acceptable8
				+ "</acceptable8><acceptable9>"
				+ acceptable9
				+ "</acceptable9><acceptable10>"
				+ acceptable10
				+ "</acceptable10><acceptable11>"
				+ acceptable11
				+ "</acceptable11><acceptable12>"
				+ acceptable12
				+ "</acceptable12><notifyRemark1>"
				+ notifyRemark1
				+ "</notifyRemark1><notifyRemark2>"
				+ notifyRemark2
				+ "</notifyRemark2><recommendation1>"
				+ recommendation1
				+ "</recommendation1><recommendation2>"
				+ recommendation2
				+ "</recommendation2><signatureFilePath>"
				+ signatureFilePath
				+ "</signatureFilePath><signatureFileName>"
				+ signatureFileName
				+ "</signatureFileName><signatureImage><![CDATA["
				+ signatureImage
				+ "]]></signatureImage><userId>"
				+ userId
				+ "</userId><modifyDate>"
				+ modifyDate
				+ "</modifyDate></CustomerSaftyCheck>";
	}

}
