package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.StringUtil;
import com.joainfo.common.util.jdbc.JdbcIOException;
import com.joainfo.common.util.jdbc.JdbcProcedureException;
import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerSaftyCheck;
import com.joainfo.gasmax.bean.CustomerSaftyCheckList;
import com.joainfo.gasmax.bean.CustomerSaftyCheckTank;
import com.joainfo.gasmax.bean.CustomerSaftyCheckSignature;
import com.joainfo.gasmax.bean.list.CustomerSaftyCheckMap;
import com.joainfo.gasmax.bean.list.CustomerSaftyCheckListMap;
import com.joainfo.gasmax.bean.list.CustomerSaftyCheckTankMap;
import com.joainfo.gasmax.bean.list.CustomerSaftyCheckSignatureMap;

/**
 * BizCustomerSaftyCheck
 * 거래처별 안전점검 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerSaftyCheck {
	
	/**
	 * 거래처별 소비설비 안전점검 이력 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_LIST_SELECT_ID = "GASMAX.CustomerSaftyCheckList.Select";

	/**
	 * 거래처별 소비설비 안전점검 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_ID = "GASMAX.CustomerSaftyCheck.Select";
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_REV2_ID = "GASMAX.CustomerSaftyCheck.Select.Rev2";
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_REV3_ID = "GASMAX.CustomerSaftyCheck.Select.Rev3";
	
	/**
	 * 거래처별 소비설비 안전점검(최근) Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_LAST_SELECT_ID = "GASMAX.CustomerSaftyCheckLast.Select";
	
	/**
	 * 거래처별 소비설비 안전점검 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_INSERT_ID = "GASMAX.CustomerSaftyCheck.Insert";
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_INSERT_REV3_ID = "GASMAX.CustomerSaftyCheck.Insert.Rev3";
	
	/**
	 * 거래처별 소비설비 안전점검 Update 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_ID = "GASMAX.CustomerSaftyCheck.Update";
	
	/**
	 * 거래처별 소비설비 안전점검 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_DELETE_ID = "GASMAX.CustomerSaftyCheck.Delete";
	
	/**
	 * 거래처별 소비설비 안전점검 거래처 정보 Update 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_CUSTOMER_INFO_ID = "GASMAX.CustomerSaftyCheck.UpdateCustomerInfo";
	
	/**
	 * 거래처별 소비설비 안전점검 다음순번 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_SNO_ID = "GASMAX.CustomerSaftyCheck.SelectSno";
	
	/**
	 * 거래처별 안전점검서명 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_SELECT_ID = "GASMAX.CustomerSaftyCheckSignature.Select";
	
	/**
	 * 거래처별 안전점검서명 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_INSERT_ID = "GASMAX.CustomerSaftyCheckSignature.Insert";
	
	/**
	 * 거래처별 안전점검서명 Update 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_UPDATE_ID = "GASMAX.CustomerSaftyCheckSignature.Update";
	
	/**
	 * 거래처별 안전점검서명 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_DELETE_ID = "GASMAX.CustomerSaftyCheckSignature.Delete";
	
	/**
	 * 거래처별 저장탱크 안전점검 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_SELECT_ID = "GASMAX.CustomerSaftyCheckTank.Select";
	
	/**
	 * 거래처별 저장탱크 안전점검(최근) Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_LAST_SELECT_ID = "GASMAX.CustomerSaftyCheckTankLast.Select";
	
	/**
	 * 거래처별 저장탱크 안전점검 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_INSERT_ID = "GASMAX.CustomerSaftyCheckTank.Insert";
	
	/**
	 * 거래처별 저장탱크 안전점검 Update 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_UPDATE_ID = "GASMAX.CustomerSaftyCheckTank.Update";
	
	/**
	 * 거래처별 저장탱크 안전점검 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_DELETE_ID = "GASMAX.CustomerSaftyCheckTank.Delete";
	
	/**
	 * 거래처별 저장탱크 안전점검 다음순번 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SAFTY_CHECK_TANK_SELECT_SNO_ID = "GASMAX.CustomerSaftyCheckTank.SelectSno";
	
	
	/**
	 * BizCustomerSaftyCheck 인스턴스
	 */
	private static BizCustomerSaftyCheck bizCustomerSaftyCheck;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerSaftyCheck(){
	}
	
	/**
	 * Singleton으로 BizCustomerSaftyCheck 인스턴스 생성
	 * @return bizCustomerSaftyCheck
	 */
	public static BizCustomerSaftyCheck getInstance(){
		if (bizCustomerSaftyCheck == null){
			bizCustomerSaftyCheck = new BizCustomerSaftyCheck();
		}
		return bizCustomerSaftyCheck;
	}
	
	/**
	 * 최근 거래처별 소비설비 안전점검 상세정보 취득
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public CustomerSaftyCheck getCustomerSaftyCheckLast(String serverIp, String catalogName, String areaCode, String customerCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);

		return selectCustomerSaftyCheckLast(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheck selectCustomerSaftyCheckLast(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheck customerSaftyCheck = null;
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_LAST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			customerSaftyCheck = convertCustomerSaftyCheck(map);
		}
		return customerSaftyCheck;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 */
	public CustomerSaftyCheckMap getCustomerSaftyChecks(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		return selectCustomerSaftyChecks(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheckMap getCustomerSaftyChecksRev2(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		return selectCustomerSaftyChecksRev2(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheckMap getCustomerSaftyChecksRev3(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		return selectCustomerSaftyChecksRev3(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheckListMap getCustomerSaftyCheckList(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("customerCodeTankFormat", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		/*
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		*/
		return selectCustomerSaftyCheckList(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 안전점검 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerSaftyCheckMap 형식의 거래처별 안전점검 목록 반환
	 */
	public CustomerSaftyCheckMap selectCustomerSaftyChecks(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckMap customerSaftyChecks = new CustomerSaftyCheckMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheck customerSaftyCheck = convertCustomerSaftyCheck(map);
			customerSaftyChecks.setCustomerSaftyCheck(customerSaftyCheck.getKeyValue(), customerSaftyCheck);
		}
		return customerSaftyChecks;
	}
	
	public CustomerSaftyCheckMap selectCustomerSaftyChecksRev2(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckMap customerSaftyChecks = new CustomerSaftyCheckMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_REV2_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheck customerSaftyCheck = convertCustomerSaftyCheck(map);
			customerSaftyChecks.setCustomerSaftyCheck(customerSaftyCheck.getKeyValue(), customerSaftyCheck);
		}
		return customerSaftyChecks;
	}
	
	public CustomerSaftyCheckMap selectCustomerSaftyChecksRev3(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckMap customerSaftyChecks = new CustomerSaftyCheckMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_REV3_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheck customerSaftyCheck = convertCustomerSaftyCheck(map);
			customerSaftyChecks.setCustomerSaftyCheck(customerSaftyCheck.getKeyValue(), customerSaftyCheck);
		}
		return customerSaftyChecks;
	}
	
	public CustomerSaftyCheckListMap selectCustomerSaftyCheckList(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckListMap customerSaftyCheckLists = new CustomerSaftyCheckListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheckList customerSaftyCheckList = convertCustomerSaftyCheckList(map);
			customerSaftyCheckLists.setCustomerSaftyCheckList(customerSaftyCheckList.getKeyValue(), customerSaftyCheckList);
		}
		return customerSaftyCheckLists;
	}

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param contractDate
	 * @param scheduledCheckDate
	 * @param employeeCode
	 * @param employeeName
	 * @param contractName
	 * @param phoneNumber
	 * @param pipeLength1
	 * @param pipeLength2
	 * @param pipeLength3
	 * @param pipeLength4
	 * @param pipeLength5
	 * @param valveQuantity1
	 * @param valveQuantity2
	 * @param valveQuantity3
	 * @param valveQuantity4
	 * @param valveQuantity5
	 * @param etcEquipmentName1
	 * @param etcEquipmentQuantity1
	 * @param etcEquipmentName2
	 * @param etcEquipmentQuantity2
	 * @param etcEquipmentName3
	 * @param etcEquipmentQuantity3
	 * @param etcEquipmentName4
	 * @param etcEquipmentQuantity4
	 * @param combustorRange1
	 * @param combustorRange2
	 * @param combustorRange3
	 * @param combustorRangeEtcName
	 * @param combustorRangeEtcQuantity
	 * @param combustorBoilerType
	 * @param combustorBoilerPosition
	 * @param combustorBoilerConsumption
	 * @param combustorBoilerInstaller
	 * @param combustorHeaterType
	 * @param combustorHeaterPosition
	 * @param combustorHeaterConsumption
	 * @param combustorHeaterInstaller
	 * @param combustorEtcName1
	 * @param combustorEtcQuantity1
	 * @param combustorEtcName2
	 * @param combustorEtcQuantity2
	 * @param combustorEtcName3
	 * @param combustorEtcQuantity3
	 * @param combustorEtcName4
	 * @param combustorEtcQuantity4
	 * @param acceptable1
	 * @param acceptable2
	 * @param acceptable3
	 * @param acceptable4
	 * @param acceptable5
	 * @param acceptable6
	 * @param acceptable7
	 * @param acceptable8
	 * @param acceptable9
	 * @param acceptable10
	 * @param acceptable11
	 * @param notifyRemark1
	 * @param notifyRemark2
	 * @param recommendation1
	 * @param recommendation2
	 * @param signatureFilePath
	 * @param signatureFileName
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheck insertCustomerSaftyCheck(
			  String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String contractDate
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String contractName
			, String phoneNumber
			, String pipeLength1
			, String pipeLength2
			, String pipeLength3
			, String pipeLength4
			, String pipeLength5
			, String valveQuantity1
			, String valveQuantity2
			, String valveQuantity3
			, String valveQuantity4
			, String valveQuantity5
			, String etcEquipmentName1
			, String etcEquipmentQuantity1
			, String etcEquipmentName2
			, String etcEquipmentQuantity2
			, String etcEquipmentName3
			, String etcEquipmentQuantity3
			, String etcEquipmentName4
			, String etcEquipmentQuantity4
			, String combustorRange1
			, String combustorRange2
			, String combustorRange3
			, String combustorRangeEtcName
			, String combustorRangeEtcQuantity
			, String combustorBoilerType
			, String combustorBoilerPosition
			, String combustorBoilerConsumption
			, String combustorBoilerInstaller
			, String combustorHeaterType
			, String combustorHeaterPosition
			, String combustorHeaterConsumption
			, String combustorHeaterInstaller
			, String combustorEtcName1
			, String combustorEtcQuantity1
			, String combustorEtcName2
			, String combustorEtcQuantity2
			, String combustorEtcName3
			, String combustorEtcQuantity3
			, String combustorEtcName4
			, String combustorEtcQuantity4
			, String acceptable1
			, String acceptable2
			, String acceptable3
			, String acceptable4
			, String acceptable5
			, String acceptable6
			, String acceptable7
			, String acceptable8
			, String acceptable9
			, String acceptable10
			, String acceptable11
			, String notifyRemark1
			, String notifyRemark2
			, String recommendation1
			, String recommendation2
			, String signatureFilePath
			, String signatureFileName
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("contractDate", contractDate);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("contractName", contractName);
		param.put("phoneNumber", phoneNumber);
		param.put("pipeLength1", pipeLength1);
		param.put("pipeLength2", pipeLength2);
		param.put("pipeLength3", pipeLength3);
		param.put("pipeLength4", pipeLength4);
		param.put("pipeLength5", pipeLength5);
		param.put("valveQuantity1", valveQuantity1);
		param.put("valveQuantity2", valveQuantity2);
		param.put("valveQuantity3", valveQuantity3);
		param.put("valveQuantity4", valveQuantity4);
		param.put("valveQuantity5", valveQuantity5);
		param.put("etcEquipmentName1", etcEquipmentName1);
		param.put("etcEquipmentQuantity1", etcEquipmentQuantity1);
		param.put("etcEquipmentName2", etcEquipmentName2);
		param.put("etcEquipmentQuantity2", etcEquipmentQuantity2);
		param.put("etcEquipmentName3", etcEquipmentName3);
		param.put("etcEquipmentQuantity3", etcEquipmentQuantity3);
		param.put("etcEquipmentName4", etcEquipmentName4);
		param.put("etcEquipmentQuantity4", etcEquipmentQuantity4);
		param.put("combustorRange1", combustorRange1);
		param.put("combustorRange2", combustorRange2);
		param.put("combustorRange3", combustorRange3);
		param.put("combustorRangeEtcName", combustorRangeEtcName);
		param.put("combustorRangeEtcQuantity", combustorRangeEtcQuantity);
		param.put("combustorBoilerType", combustorBoilerType);
		param.put("combustorBoilerPosition", combustorBoilerPosition);
		param.put("combustorBoilerConsumption", combustorBoilerConsumption);
		param.put("combustorBoilerInstaller", combustorBoilerInstaller);
		param.put("combustorHeaterType", combustorHeaterType);
		param.put("combustorHeaterPosition", combustorHeaterPosition);
		param.put("combustorHeaterConsumption", combustorHeaterConsumption);
		param.put("combustorHeaterInstaller", combustorHeaterInstaller);
		param.put("combustorEtcName1", combustorEtcName1);
		param.put("combustorEtcQuantity1", combustorEtcQuantity1);
		param.put("combustorEtcName2", combustorEtcName2);
		param.put("combustorEtcQuantity2", combustorEtcQuantity2);
		param.put("combustorEtcName3", combustorEtcName3);
		param.put("combustorEtcQuantity3", combustorEtcQuantity3);
		param.put("combustorEtcName4", combustorEtcName4);
		param.put("combustorEtcQuantity4", combustorEtcQuantity4);
		param.put("acceptable1", acceptable1);
		param.put("acceptable2", acceptable2);
		param.put("acceptable3", acceptable3);
		param.put("acceptable4", acceptable4);
		param.put("acceptable5", acceptable5);
		param.put("acceptable6", acceptable6);
		param.put("acceptable7", acceptable7);
		param.put("acceptable8", acceptable8);
		param.put("acceptable9", acceptable9);
		param.put("acceptable10", acceptable10);
		param.put("acceptable11", acceptable11);
		param.put("notifyRemark1", notifyRemark1);
		param.put("notifyRemark2", notifyRemark2);
		param.put("recommendation1", recommendation1);
		param.put("recommendation2", recommendation2);
		param.put("signatureFilePath", signatureFilePath);
		param.put("signatureFileName", signatureFileName);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
			
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_INSERT_ID, param); 
		HashMap<String, String> newParam = param;
		return convertCustomerSaftyCheck(newParam); 
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param contractDate
	 * @param scheduledCheckDate
	 * @param employeeCode
	 * @param employeeName
	 * @param contractName
	 * @param phoneNumber
	 * @param pipeLength1
	 * @param pipeLength2
	 * @param pipeLength3
	 * @param pipeLength4
	 * @param pipeLength5
	 * @param valveQuantity1
	 * @param valveQuantity2
	 * @param valveQuantity3
	 * @param valveQuantity4
	 * @param valveQuantity5
	 * @param etcEquipmentName1
	 * @param etcEquipmentQuantity1
	 * @param etcEquipmentName2
	 * @param etcEquipmentQuantity2
	 * @param etcEquipmentName3
	 * @param etcEquipmentQuantity3
	 * @param etcEquipmentName4
	 * @param etcEquipmentQuantity4
	 * @param combustorRange1
	 * @param combustorRange2
	 * @param combustorRange3
	 * @param combustorRangeEtcName
	 * @param combustorRangeEtcQuantity
	 * @param combustorBoilerType
	 * @param combustorBoilerPosition
	 * @param combustorBoilerConsumption
	 * @param combustorBoilerInstaller
	 * @param combustorHeaterType
	 * @param combustorHeaterPosition
	 * @param combustorHeaterConsumption
	 * @param combustorHeaterInstaller
	 * @param combustorEtcName1
	 * @param combustorEtcQuantity1
	 * @param combustorEtcName2
	 * @param combustorEtcQuantity2
	 * @param combustorEtcName3
	 * @param combustorEtcQuantity3
	 * @param combustorEtcName4
	 * @param combustorEtcQuantity4
	 * @param acceptable1
	 * @param acceptable2
	 * @param acceptable3
	 * @param acceptable4
	 * @param acceptable5
	 * @param acceptable6
	 * @param acceptable7
	 * @param acceptable8
	 * @param acceptable9
	 * @param acceptable10
	 * @param acceptable11
	 * @param acceptable12
	 * @param notifyRemark1
	 * @param notifyRemark2
	 * @param recommendation1
	 * @param recommendation2
	 * @param signatureFilePath
	 * @param signatureFileName
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheck insertCustomerSaftyCheckRev3(
			  String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String contractDate
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String contractName
			, String phoneNumber
			, String pipeLength1
			, String pipeLength2
			, String pipeLength3
			, String pipeLength4
			, String pipeLength5
			, String valveQuantity1
			, String valveQuantity2
			, String valveQuantity3
			, String valveQuantity4
			, String valveQuantity5
			, String etcEquipmentName1
			, String etcEquipmentQuantity1
			, String etcEquipmentName2
			, String etcEquipmentQuantity2
			, String etcEquipmentName3
			, String etcEquipmentQuantity3
			, String etcEquipmentName4
			, String etcEquipmentQuantity4
			, String combustorRange1
			, String combustorRange2
			, String combustorRange3
			, String combustorRangeEtcName
			, String combustorRangeEtcQuantity
			, String combustorBoilerType
			, String combustorBoilerPosition
			, String combustorBoilerConsumption
			, String combustorBoilerInstaller
			, String combustorHeaterType
			, String combustorHeaterPosition
			, String combustorHeaterConsumption
			, String combustorHeaterInstaller
			, String combustorEtcName1
			, String combustorEtcQuantity1
			, String combustorEtcName2
			, String combustorEtcQuantity2
			, String combustorEtcName3
			, String combustorEtcQuantity3
			, String combustorEtcName4
			, String combustorEtcQuantity4
			, String acceptable1
			, String acceptable2
			, String acceptable3
			, String acceptable4
			, String acceptable5
			, String acceptable6
			, String acceptable7
			, String acceptable8
			, String acceptable9
			, String acceptable10
			, String acceptable11
			, String acceptable12
			, String notifyRemark1
			, String notifyRemark2
			, String recommendation1
			, String recommendation2
			, String signatureFilePath
			, String signatureFileName
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("contractDate", contractDate);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("contractName", contractName);
		param.put("phoneNumber", phoneNumber);
		param.put("pipeLength1", pipeLength1);
		param.put("pipeLength2", pipeLength2);
		param.put("pipeLength3", pipeLength3);
		param.put("pipeLength4", pipeLength4);
		param.put("pipeLength5", pipeLength5);
		param.put("valveQuantity1", valveQuantity1);
		param.put("valveQuantity2", valveQuantity2);
		param.put("valveQuantity3", valveQuantity3);
		param.put("valveQuantity4", valveQuantity4);
		param.put("valveQuantity5", valveQuantity5);
		param.put("etcEquipmentName1", etcEquipmentName1);
		param.put("etcEquipmentQuantity1", etcEquipmentQuantity1);
		param.put("etcEquipmentName2", etcEquipmentName2);
		param.put("etcEquipmentQuantity2", etcEquipmentQuantity2);
		param.put("etcEquipmentName3", etcEquipmentName3);
		param.put("etcEquipmentQuantity3", etcEquipmentQuantity3);
		param.put("etcEquipmentName4", etcEquipmentName4);
		param.put("etcEquipmentQuantity4", etcEquipmentQuantity4);
		param.put("combustorRange1", combustorRange1);
		param.put("combustorRange2", combustorRange2);
		param.put("combustorRange3", combustorRange3);
		param.put("combustorRangeEtcName", combustorRangeEtcName);
		param.put("combustorRangeEtcQuantity", combustorRangeEtcQuantity);
		param.put("combustorBoilerType", combustorBoilerType);
		param.put("combustorBoilerPosition", combustorBoilerPosition);
		param.put("combustorBoilerConsumption", combustorBoilerConsumption);
		param.put("combustorBoilerInstaller", combustorBoilerInstaller);
		param.put("combustorHeaterType", combustorHeaterType);
		param.put("combustorHeaterPosition", combustorHeaterPosition);
		param.put("combustorHeaterConsumption", combustorHeaterConsumption);
		param.put("combustorHeaterInstaller", combustorHeaterInstaller);
		param.put("combustorEtcName1", combustorEtcName1);
		param.put("combustorEtcQuantity1", combustorEtcQuantity1);
		param.put("combustorEtcName2", combustorEtcName2);
		param.put("combustorEtcQuantity2", combustorEtcQuantity2);
		param.put("combustorEtcName3", combustorEtcName3);
		param.put("combustorEtcQuantity3", combustorEtcQuantity3);
		param.put("combustorEtcName4", combustorEtcName4);
		param.put("combustorEtcQuantity4", combustorEtcQuantity4);
		param.put("acceptable1", acceptable1);
		param.put("acceptable2", acceptable2);
		param.put("acceptable3", acceptable3);
		param.put("acceptable4", acceptable4);
		param.put("acceptable5", acceptable5);
		param.put("acceptable6", acceptable6);
		param.put("acceptable7", acceptable7);
		param.put("acceptable8", acceptable8);
		param.put("acceptable9", acceptable9);
		param.put("acceptable10", acceptable10);
		param.put("acceptable11", acceptable11);
		param.put("acceptable12", acceptable12);
		param.put("notifyRemark1", notifyRemark1);
		param.put("notifyRemark2", notifyRemark2);
		param.put("recommendation1", recommendation1);
		param.put("recommendation2", recommendation2);
		param.put("signatureFilePath", signatureFilePath);
		param.put("signatureFileName", signatureFileName);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
			
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_INSERT_REV3_ID, param); 
		HashMap<String, String> newParam = param;
		return convertCustomerSaftyCheck(newParam); 
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param customerSaftyCheck
	 * @return
	 */
	public CustomerSaftyCheck insertCustomerSaftyCheck(String serverIp , String catalogName, CustomerSaftyCheck customerSaftyCheck){
		HashMap<String, String> param = convertCustomerSaftyCheck(customerSaftyCheck);
		param.put("catalogName", catalogName);
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_INSERT_ID, param);
		return customerSaftyCheck;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param contractDate
	 * @param scheduledCheckDate
	 * @param employeeCode
	 * @param employeeName
	 * @param contractName
	 * @param phoneNumber
	 * @param pipeLength1
	 * @param pipeLength2
	 * @param pipeLength3
	 * @param pipeLength4
	 * @param pipeLength5
	 * @param valveQuantity1
	 * @param valveQuantity2
	 * @param valveQuantity3
	 * @param valveQuantity4
	 * @param valveQuantity5
	 * @param etcEquipmentName1
	 * @param etcEquipmentQuantity1
	 * @param etcEquipmentName2
	 * @param etcEquipmentQuantity2
	 * @param etcEquipmentName3
	 * @param etcEquipmentQuantity3
	 * @param etcEquipmentName4
	 * @param etcEquipmentQuantity4
	 * @param combustorRange1
	 * @param combustorRange2
	 * @param combustorRange3
	 * @param combustorRangeEtcName
	 * @param combustorRangeEtcQuantity
	 * @param combustorBoilerType
	 * @param combustorBoilerPosition
	 * @param combustorBoilerConsumption
	 * @param combustorBoilerInstaller
	 * @param combustorHeaterType
	 * @param combustorHeaterPosition
	 * @param combustorHeaterConsumption
	 * @param combustorHeaterInstaller
	 * @param combustorEtcName1
	 * @param combustorEtcQuantity1
	 * @param combustorEtcName2
	 * @param combustorEtcQuantity2
	 * @param combustorEtcName3
	 * @param combustorEtcQuantity3
	 * @param combustorEtcName4
	 * @param combustorEtcQuantity4
	 * @param acceptable1
	 * @param acceptable2
	 * @param acceptable3
	 * @param acceptable4
	 * @param acceptable5
	 * @param acceptable6
	 * @param acceptable7
	 * @param acceptable8
	 * @param acceptable9
	 * @param acceptable10
	 * @param acceptable11
	 * @param notifyRemark1
	 * @param notifyRemark2
	 * @param recommendation1
	 * @param recommendation2
	 * @param signatureFilePath
	 * @param signatureFileName
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheck updateCustomerSaftyCheck(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String contractDate
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String contractName
			, String phoneNumber
			, String pipeLength1
			, String pipeLength2
			, String pipeLength3
			, String pipeLength4
			, String pipeLength5
			, String valveQuantity1
			, String valveQuantity2
			, String valveQuantity3
			, String valveQuantity4
			, String valveQuantity5
			, String etcEquipmentName1
			, String etcEquipmentQuantity1
			, String etcEquipmentName2
			, String etcEquipmentQuantity2
			, String etcEquipmentName3
			, String etcEquipmentQuantity3
			, String etcEquipmentName4
			, String etcEquipmentQuantity4
			, String combustorRange1
			, String combustorRange2
			, String combustorRange3
			, String combustorRangeEtcName
			, String combustorRangeEtcQuantity
			, String combustorBoilerType
			, String combustorBoilerPosition
			, String combustorBoilerConsumption
			, String combustorBoilerInstaller
			, String combustorHeaterType
			, String combustorHeaterPosition
			, String combustorHeaterConsumption
			, String combustorHeaterInstaller
			, String combustorEtcName1
			, String combustorEtcQuantity1
			, String combustorEtcName2
			, String combustorEtcQuantity2
			, String combustorEtcName3
			, String combustorEtcQuantity3
			, String combustorEtcName4
			, String combustorEtcQuantity4
			, String acceptable1
			, String acceptable2
			, String acceptable3
			, String acceptable4
			, String acceptable5
			, String acceptable6
			, String acceptable7
			, String acceptable8
			, String acceptable9
			, String acceptable10
			, String acceptable11
			, String notifyRemark1
			, String notifyRemark2
			, String recommendation1
			, String recommendation2
			, String signatureFilePath
			, String signatureFileName
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("contractDate", contractDate);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("contractName", contractName);
		param.put("phoneNumber", phoneNumber);
		param.put("pipeLength1", pipeLength1);
		param.put("pipeLength2", pipeLength2);
		param.put("pipeLength3", pipeLength3);
		param.put("pipeLength4", pipeLength4);
		param.put("pipeLength5", pipeLength5);
		param.put("valveQuantity1", valveQuantity1);
		param.put("valveQuantity2", valveQuantity2);
		param.put("valveQuantity3", valveQuantity3);
		param.put("valveQuantity4", valveQuantity4);
		param.put("valveQuantity5", valveQuantity5);
		param.put("etcEquipmentName1", etcEquipmentName1);
		param.put("etcEquipmentQuantity1", etcEquipmentQuantity1);
		param.put("etcEquipmentName2", etcEquipmentName2);
		param.put("etcEquipmentQuantity2", etcEquipmentQuantity2);
		param.put("etcEquipmentName3", etcEquipmentName3);
		param.put("etcEquipmentQuantity3", etcEquipmentQuantity3);
		param.put("etcEquipmentName4", etcEquipmentName4);
		param.put("etcEquipmentQuantity4", etcEquipmentQuantity4);
		param.put("combustorRange1", combustorRange1);
		param.put("combustorRange2", combustorRange2);
		param.put("combustorRange3", combustorRange3);
		param.put("combustorRangeEtcName", combustorRangeEtcName);
		param.put("combustorRangeEtcQuantity", combustorRangeEtcQuantity);
		param.put("combustorBoilerType", combustorBoilerType);
		param.put("combustorBoilerPosition", combustorBoilerPosition);
		param.put("combustorBoilerConsumption", combustorBoilerConsumption);
		param.put("combustorBoilerInstaller", combustorBoilerInstaller);
		param.put("combustorHeaterType", combustorHeaterType);
		param.put("combustorHeaterPosition", combustorHeaterPosition);
		param.put("combustorHeaterConsumption", combustorHeaterConsumption);
		param.put("combustorHeaterInstaller", combustorHeaterInstaller);
		param.put("combustorEtcName1", combustorEtcName1);
		param.put("combustorEtcQuantity1", combustorEtcQuantity1);
		param.put("combustorEtcName2", combustorEtcName2);
		param.put("combustorEtcQuantity2", combustorEtcQuantity2);
		param.put("combustorEtcName3", combustorEtcName3);
		param.put("combustorEtcQuantity3", combustorEtcQuantity3);
		param.put("combustorEtcName4", combustorEtcName4);
		param.put("combustorEtcQuantity4", combustorEtcQuantity4);
		param.put("acceptable1", acceptable1);
		param.put("acceptable2", acceptable2);
		param.put("acceptable3", acceptable3);
		param.put("acceptable4", acceptable4);
		param.put("acceptable5", acceptable5);
		param.put("acceptable6", acceptable6);
		param.put("acceptable7", acceptable7);
		param.put("acceptable8", acceptable8);
		param.put("acceptable9", acceptable9);
		param.put("acceptable10", acceptable10);
		param.put("acceptable11", acceptable11);
		param.put("notifyRemark1", notifyRemark1);
		param.put("notifyRemark2", notifyRemark2);
		param.put("recommendation1", recommendation1);
		param.put("recommendation2", recommendation2);
		param.put("signatureFilePath", signatureFilePath);
		param.put("signatureFileName", signatureFileName);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
		
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_ID, param);
		
		return convertCustomerSaftyCheck(param);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param contractDate
	 * @param scheduledCheckDate
	 * @param employeeCode
	 * @param employeeName
	 * @param contractName
	 * @param phoneNumber
	 * @param pipeLength1
	 * @param pipeLength2
	 * @param pipeLength3
	 * @param pipeLength4
	 * @param pipeLength5
	 * @param valveQuantity1
	 * @param valveQuantity2
	 * @param valveQuantity3
	 * @param valveQuantity4
	 * @param valveQuantity5
	 * @param etcEquipmentName1
	 * @param etcEquipmentQuantity1
	 * @param etcEquipmentName2
	 * @param etcEquipmentQuantity2
	 * @param etcEquipmentName3
	 * @param etcEquipmentQuantity3
	 * @param etcEquipmentName4
	 * @param etcEquipmentQuantity4
	 * @param combustorRange1
	 * @param combustorRange2
	 * @param combustorRange3
	 * @param combustorRangeEtcName
	 * @param combustorRangeEtcQuantity
	 * @param combustorBoilerType
	 * @param combustorBoilerPosition
	 * @param combustorBoilerConsumption
	 * @param combustorBoilerInstaller
	 * @param combustorHeaterType
	 * @param combustorHeaterPosition
	 * @param combustorHeaterConsumption
	 * @param combustorHeaterInstaller
	 * @param combustorEtcName1
	 * @param combustorEtcQuantity1
	 * @param combustorEtcName2
	 * @param combustorEtcQuantity2
	 * @param combustorEtcName3
	 * @param combustorEtcQuantity3
	 * @param combustorEtcName4
	 * @param combustorEtcQuantity4
	 * @param acceptable1
	 * @param acceptable2
	 * @param acceptable3
	 * @param acceptable4
	 * @param acceptable5
	 * @param acceptable6
	 * @param acceptable7
	 * @param acceptable8
	 * @param acceptable9
	 * @param acceptable10
	 * @param acceptable11
	 * @param acceptable12
	 * @param notifyRemark1
	 * @param notifyRemark2
	 * @param recommendation1
	 * @param recommendation2
	 * @param signatureFilePath
	 * @param signatureFileName
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheck updateCustomerSaftyCheckRev3(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String contractDate
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String contractName
			, String phoneNumber
			, String pipeLength1
			, String pipeLength2
			, String pipeLength3
			, String pipeLength4
			, String pipeLength5
			, String valveQuantity1
			, String valveQuantity2
			, String valveQuantity3
			, String valveQuantity4
			, String valveQuantity5
			, String etcEquipmentName1
			, String etcEquipmentQuantity1
			, String etcEquipmentName2
			, String etcEquipmentQuantity2
			, String etcEquipmentName3
			, String etcEquipmentQuantity3
			, String etcEquipmentName4
			, String etcEquipmentQuantity4
			, String combustorRange1
			, String combustorRange2
			, String combustorRange3
			, String combustorRangeEtcName
			, String combustorRangeEtcQuantity
			, String combustorBoilerType
			, String combustorBoilerPosition
			, String combustorBoilerConsumption
			, String combustorBoilerInstaller
			, String combustorHeaterType
			, String combustorHeaterPosition
			, String combustorHeaterConsumption
			, String combustorHeaterInstaller
			, String combustorEtcName1
			, String combustorEtcQuantity1
			, String combustorEtcName2
			, String combustorEtcQuantity2
			, String combustorEtcName3
			, String combustorEtcQuantity3
			, String combustorEtcName4
			, String combustorEtcQuantity4
			, String acceptable1
			, String acceptable2
			, String acceptable3
			, String acceptable4
			, String acceptable5
			, String acceptable6
			, String acceptable7
			, String acceptable8
			, String acceptable9
			, String acceptable10
			, String acceptable11
			, String acceptable12
			, String notifyRemark1
			, String notifyRemark2
			, String recommendation1
			, String recommendation2
			, String signatureFilePath
			, String signatureFileName
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("contractDate", contractDate);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("contractName", contractName);
		param.put("phoneNumber", phoneNumber);
		param.put("pipeLength1", pipeLength1);
		param.put("pipeLength2", pipeLength2);
		param.put("pipeLength3", pipeLength3);
		param.put("pipeLength4", pipeLength4);
		param.put("pipeLength5", pipeLength5);
		param.put("valveQuantity1", valveQuantity1);
		param.put("valveQuantity2", valveQuantity2);
		param.put("valveQuantity3", valveQuantity3);
		param.put("valveQuantity4", valveQuantity4);
		param.put("valveQuantity5", valveQuantity5);
		param.put("etcEquipmentName1", etcEquipmentName1);
		param.put("etcEquipmentQuantity1", etcEquipmentQuantity1);
		param.put("etcEquipmentName2", etcEquipmentName2);
		param.put("etcEquipmentQuantity2", etcEquipmentQuantity2);
		param.put("etcEquipmentName3", etcEquipmentName3);
		param.put("etcEquipmentQuantity3", etcEquipmentQuantity3);
		param.put("etcEquipmentName4", etcEquipmentName4);
		param.put("etcEquipmentQuantity4", etcEquipmentQuantity4);
		param.put("combustorRange1", combustorRange1);
		param.put("combustorRange2", combustorRange2);
		param.put("combustorRange3", combustorRange3);
		param.put("combustorRangeEtcName", combustorRangeEtcName);
		param.put("combustorRangeEtcQuantity", combustorRangeEtcQuantity);
		param.put("combustorBoilerType", combustorBoilerType);
		param.put("combustorBoilerPosition", combustorBoilerPosition);
		param.put("combustorBoilerConsumption", combustorBoilerConsumption);
		param.put("combustorBoilerInstaller", combustorBoilerInstaller);
		param.put("combustorHeaterType", combustorHeaterType);
		param.put("combustorHeaterPosition", combustorHeaterPosition);
		param.put("combustorHeaterConsumption", combustorHeaterConsumption);
		param.put("combustorHeaterInstaller", combustorHeaterInstaller);
		param.put("combustorEtcName1", combustorEtcName1);
		param.put("combustorEtcQuantity1", combustorEtcQuantity1);
		param.put("combustorEtcName2", combustorEtcName2);
		param.put("combustorEtcQuantity2", combustorEtcQuantity2);
		param.put("combustorEtcName3", combustorEtcName3);
		param.put("combustorEtcQuantity3", combustorEtcQuantity3);
		param.put("combustorEtcName4", combustorEtcName4);
		param.put("combustorEtcQuantity4", combustorEtcQuantity4);
		param.put("acceptable1", acceptable1);
		param.put("acceptable2", acceptable2);
		param.put("acceptable3", acceptable3);
		param.put("acceptable4", acceptable4);
		param.put("acceptable5", acceptable5);
		param.put("acceptable6", acceptable6);
		param.put("acceptable7", acceptable7);
		param.put("acceptable8", acceptable8);
		param.put("acceptable9", acceptable9);
		param.put("acceptable10", acceptable10);
		param.put("acceptable11", acceptable11);
		param.put("acceptable12", acceptable12);
		param.put("notifyRemark1", notifyRemark1);
		param.put("notifyRemark2", notifyRemark2);
		param.put("recommendation1", recommendation1);
		param.put("recommendation2", recommendation2);
		param.put("signatureFilePath", signatureFilePath);
		param.put("signatureFileName", signatureFileName);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
		
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_ID, param);
		
		return convertCustomerSaftyCheck(param);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param customerSaftyCheck
	 * @return
	 */
	public CustomerSaftyCheck updateCustomerSaftyCheck(String serverIp, String catalogName, CustomerSaftyCheck customerSaftyCheck){
		HashMap<String, String> param = convertCustomerSaftyCheck(customerSaftyCheck);
		param.put("catalogName", catalogName);
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_ID, param);
		return customerSaftyCheck;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param phoneNumber
	 * @param phoneNumberFind
	 * @param address1
	 * @param address2
	 * @param contractNumber
	 * @param contractName
	 * @param contractDate
	 * @param latestSaftyCheckDate
	 * @param facilityOkYesNo
	 * @param appUserName
	 * @return
	 */
	public int updateCustomerInfo(
			String serverIp, 
			String catalogName, 
			String areaCode, 
			String customerCode, 
			String phoneNumber, 
			String phoneNumberFind, 
			String address1, 
			String address2, 
			String contractNumber, 
			String contractName, 
			String contractDate, 
			String latestSaftyCheckDate, 
			String facilityOkYesNo, 
			String appUserName
			){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("phoneNumber", phoneNumber);
		param.put("phoneNumberFind", phoneNumberFind);
		param.put("address1", address1);
		param.put("address2", address2);
		param.put("contractNumber", contractNumber);
		param.put("contractName", contractName);
		param.put("contractDate", contractDate);
		param.put("latestSaftyCheckDate", latestSaftyCheckDate);
		param.put("facilityOkYesNo", facilityOkYesNo);
		param.put("appUserName", appUserName);
		param.put("registerDate", StringUtil.nowDateTimeStr());
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_CUSTOMER_INFO_ID, param); // 처리건수를 반환한다.
	}
	
	/**
	 * 서명정보 수정
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param signatureFilePath
	 * @param signatureFileName
	 * @return
	 */
	public CustomerSaftyCheck updateSignInfo(
			String serverIp, 
			String catalogName, 
			String areaCode, 
			String customerCode, 
			String sequenceNumber, 
			String signatureFilePath, 
			String signatureFileName
			){
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("signatureFilePath", signatureFilePath);
		param.put("signatureFileName", signatureFileName);
		
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_UPDATE_ID, param);
		
		return convertCustomerSaftyCheck(param);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return sequenceNumber를 반환
	 */
	public String selectSno(
			String serverIp, 
			String catalogName, 
			String areaCode, 
			String customerCode 
			){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_SAFTY_CHECK_SELECT_SNO_ID, param); // 처리건수를 반환한다.
		String sequenceNumber = (String)param.get("sequenceNumber");
		return sequenceNumber;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public int deleteCustomerSaftyCheck(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		
		return JdbcUtil.getInstance(serverIp).deleteQuery(GASMAX_CUSTOMER_SAFTY_CHECK_DELETE_ID, param); // 처리건수를 반환한다.
	}
	
	/**
	 * HashMap을 CustomerSaftyCheck으로 변환
	 * @param map
	 * @return CustomerSaftyCheck
	 */
	protected static CustomerSaftyCheck convertCustomerSaftyCheck(HashMap<String, String> map){
		CustomerSaftyCheck customerSaftyCheck = new CustomerSaftyCheck();
		
		customerSaftyCheck.setAreaCode(map.get("areaCode"));
		customerSaftyCheck.setCustomerCode(map.get("customerCode"));
		customerSaftyCheck.setSequenceNumber(map.get("sequenceNumber"));
		customerSaftyCheck.setContractDate(map.get("contractDate"));
		customerSaftyCheck.setScheduledCheckDate(map.get("scheduledCheckDate"));
		customerSaftyCheck.setEmployeeCode(map.get("employeeCode"));
		customerSaftyCheck.setEmployeeName(map.get("employeeName"));
		customerSaftyCheck.setContractName(map.get("contractName"));
		customerSaftyCheck.setPhoneNumber(map.get("phoneNumber"));
		customerSaftyCheck.setPipeLength1(map.get("pipeLength1"));
		customerSaftyCheck.setPipeLength2(map.get("pipeLength2"));
		customerSaftyCheck.setPipeLength3(map.get("pipeLength3"));
		customerSaftyCheck.setPipeLength4(map.get("pipeLength4"));
		customerSaftyCheck.setPipeLength5(map.get("pipeLength5"));
		customerSaftyCheck.setValveQuantity1(map.get("valveQuantity1"));
		customerSaftyCheck.setValveQuantity2(map.get("valveQuantity2"));
		customerSaftyCheck.setValveQuantity3(map.get("valveQuantity3"));
		customerSaftyCheck.setValveQuantity4(map.get("valveQuantity4"));
		customerSaftyCheck.setValveQuantity5(map.get("valveQuantity5"));
		customerSaftyCheck.setEtcEquipmentName1(map.get("etcEquipmentName1"));
		customerSaftyCheck.setEtcEquipmentQuantity1(map.get("etcEquipmentQuantity1"));
		customerSaftyCheck.setEtcEquipmentName2(map.get("etcEquipmentName2"));
		customerSaftyCheck.setEtcEquipmentQuantity2(map.get("etcEquipmentQuantity2"));
		customerSaftyCheck.setEtcEquipmentName3(map.get("etcEquipmentName3"));
		customerSaftyCheck.setEtcEquipmentQuantity3(map.get("etcEquipmentQuantity3"));
		customerSaftyCheck.setEtcEquipmentName4(map.get("etcEquipmentName4"));
		customerSaftyCheck.setEtcEquipmentQuantity4(map.get("etcEquipmentQuantity4"));
		customerSaftyCheck.setCombustorRange1(map.get("combustorRange1"));
		customerSaftyCheck.setCombustorRange2(map.get("combustorRange2"));
		customerSaftyCheck.setCombustorRange3(map.get("combustorRange3"));
		customerSaftyCheck.setCombustorRangeEtcName(map.get("combustorRangeEtcName"));
		customerSaftyCheck.setCombustorRangeEtcQuantity(map.get("combustorRangeEtcQuantity"));
		customerSaftyCheck.setCombustorBoilerType(map.get("combustorBoilerType"));
		customerSaftyCheck.setCombustorBoilerPosition(map.get("combustorBoilerPosition"));
		customerSaftyCheck.setCombustorBoilerConsumption(map.get("combustorBoilerConsumption"));
		customerSaftyCheck.setCombustorBoilerInstaller(map.get("combustorBoilerInstaller"));
		customerSaftyCheck.setCombustorHeaterType(map.get("combustorHeaterType"));
		customerSaftyCheck.setCombustorHeaterPosition(map.get("combustorHeaterPosition"));
		customerSaftyCheck.setCombustorHeaterConsumption(map.get("combustorHeaterConsumption"));
		customerSaftyCheck.setCombustorHeaterInstaller(map.get("combustorHeaterInstaller"));
		customerSaftyCheck.setCombustorEtcName1(map.get("combustorEtcName1"));
		customerSaftyCheck.setCombustorEtcQuantity1(map.get("combustorEtcQuantity1"));
		customerSaftyCheck.setCombustorEtcName2(map.get("combustorEtcName2"));
		customerSaftyCheck.setCombustorEtcQuantity2(map.get("combustorEtcQuantity2"));
		customerSaftyCheck.setCombustorEtcName3(map.get("combustorEtcName3"));
		customerSaftyCheck.setCombustorEtcQuantity3(map.get("combustorEtcQuantity3"));
		customerSaftyCheck.setCombustorEtcName4(map.get("combustorEtcName4"));
		customerSaftyCheck.setCombustorEtcQuantity4(map.get("combustorEtcQuantity4"));
		customerSaftyCheck.setAcceptable1(map.get("acceptable1"));
		customerSaftyCheck.setAcceptable2(map.get("acceptable2"));
		customerSaftyCheck.setAcceptable3(map.get("acceptable3"));
		customerSaftyCheck.setAcceptable4(map.get("acceptable4"));
		customerSaftyCheck.setAcceptable5(map.get("acceptable5"));
		customerSaftyCheck.setAcceptable6(map.get("acceptable6"));
		customerSaftyCheck.setAcceptable7(map.get("acceptable7"));
		customerSaftyCheck.setAcceptable8(map.get("acceptable8"));
		customerSaftyCheck.setAcceptable9(map.get("acceptable9"));
		customerSaftyCheck.setAcceptable10(map.get("acceptable10"));
		customerSaftyCheck.setAcceptable11(map.get("acceptable11"));
		customerSaftyCheck.setAcceptable12(map.get("acceptable12"));
		customerSaftyCheck.setNotifyRemark1(map.get("notifyRemark1"));
		customerSaftyCheck.setNotifyRemark2(map.get("notifyRemark2"));
		customerSaftyCheck.setRecommendation1(map.get("recommendation1"));
		customerSaftyCheck.setRecommendation2(map.get("recommendation2"));
		customerSaftyCheck.setSignatureFilePath(map.get("signatureFilePath"));
		customerSaftyCheck.setSignatureFileName(map.get("signatureFileName"));
		customerSaftyCheck.setSignatureImage(map.get("signatureImage"));
		customerSaftyCheck.setUserId(map.get("userId"));
		customerSaftyCheck.setModifyDate(map.get("modifyDate"));
		
		return customerSaftyCheck;
	}
	
	/**
	 * CustomerSaftyCheck을 HashMap으로 변환
	 * @param customerSaftyCheck
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerSaftyCheck(CustomerSaftyCheck customerSaftyCheck){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerSaftyCheck.getAreaCode());
	    map.put("customerCode", customerSaftyCheck.getCustomerCode());
	    map.put("sequenceNumber", customerSaftyCheck.getSequenceNumber());
	    map.put("contractDate", customerSaftyCheck.getContractDate());
	    map.put("scheduledCheckDate", customerSaftyCheck.getScheduledCheckDate());
	    map.put("employeeCode", customerSaftyCheck.getEmployeeCode());
	    map.put("employeeName", customerSaftyCheck.getEmployeeName());
	    map.put("contractName", customerSaftyCheck.getContractName());
	    map.put("phoneNumber", customerSaftyCheck.getPhoneNumber());
	    map.put("pipeLength1", customerSaftyCheck.getPipeLength1());
	    map.put("pipeLength2", customerSaftyCheck.getPipeLength2());
	    map.put("pipeLength3", customerSaftyCheck.getPipeLength3());
	    map.put("pipeLength4", customerSaftyCheck.getPipeLength4());
	    map.put("pipeLength5", customerSaftyCheck.getPipeLength5());
	    map.put("valveQuantity1", customerSaftyCheck.getValveQuantity1());
	    map.put("valveQuantity2", customerSaftyCheck.getValveQuantity2());
	    map.put("valveQuantity3", customerSaftyCheck.getValveQuantity3());
	    map.put("valveQuantity4", customerSaftyCheck.getValveQuantity4());
	    map.put("valveQuantity5", customerSaftyCheck.getValveQuantity5());
	    map.put("etcEquipmentName1", customerSaftyCheck.getEtcEquipmentName1());
	    map.put("etcEquipmentQuantity1", customerSaftyCheck.getEtcEquipmentQuantity1());
	    map.put("etcEquipmentName2", customerSaftyCheck.getEtcEquipmentName2());
	    map.put("etcEquipmentQuantity2", customerSaftyCheck.getEtcEquipmentQuantity2());
	    map.put("etcEquipmentName3", customerSaftyCheck.getEtcEquipmentName3());
	    map.put("etcEquipmentQuantity3", customerSaftyCheck.getEtcEquipmentQuantity3());
	    map.put("etcEquipmentName4", customerSaftyCheck.getEtcEquipmentName4());
	    map.put("etcEquipmentQuantity4", customerSaftyCheck.getEtcEquipmentQuantity4());
	    map.put("combustorRange1", customerSaftyCheck.getCombustorRange1());
	    map.put("combustorRange2", customerSaftyCheck.getCombustorRange2());
	    map.put("combustorRange3", customerSaftyCheck.getCombustorRange3());
	    map.put("combustorRangeEtcName", customerSaftyCheck.getCombustorRangeEtcName());
	    map.put("combustorRangeEtcQuantity", customerSaftyCheck.getCombustorRangeEtcQuantity());
	    map.put("combustorBoilerType", customerSaftyCheck.getCombustorBoilerType());
	    map.put("combustorBoilerPosition", customerSaftyCheck.getCombustorBoilerPosition());
	    map.put("combustorBoilerConsumption", customerSaftyCheck.getCombustorBoilerConsumption());
	    map.put("combustorBoilerInstaller", customerSaftyCheck.getCombustorBoilerInstaller());
	    map.put("combustorHeaterType", customerSaftyCheck.getCombustorHeaterType());
	    map.put("combustorHeaterPosition", customerSaftyCheck.getCombustorHeaterPosition());
	    map.put("combustorHeaterConsumption", customerSaftyCheck.getCombustorHeaterConsumption());
	    map.put("combustorHeaterInstaller", customerSaftyCheck.getCombustorHeaterInstaller());
	    map.put("combustorEtcName1", customerSaftyCheck.getCombustorEtcName1());
	    map.put("combustorEtcQuantity1", customerSaftyCheck.getCombustorEtcQuantity1());
	    map.put("combustorEtcName2", customerSaftyCheck.getCombustorEtcName2());
	    map.put("combustorEtcQuantity2", customerSaftyCheck.getCombustorEtcQuantity2());
	    map.put("combustorEtcName3", customerSaftyCheck.getCombustorEtcName3());
	    map.put("combustorEtcQuantity3", customerSaftyCheck.getCombustorEtcQuantity3());
	    map.put("combustorEtcName4", customerSaftyCheck.getCombustorEtcName4());
	    map.put("combustorEtcQuantity4", customerSaftyCheck.getCombustorEtcQuantity4());
	    map.put("acceptable1", customerSaftyCheck.getAcceptable1());
	    map.put("acceptable2", customerSaftyCheck.getAcceptable2());
	    map.put("acceptable3", customerSaftyCheck.getAcceptable3());
	    map.put("acceptable4", customerSaftyCheck.getAcceptable4());
	    map.put("acceptable5", customerSaftyCheck.getAcceptable5());
	    map.put("acceptable6", customerSaftyCheck.getAcceptable6());
	    map.put("acceptable7", customerSaftyCheck.getAcceptable7());
	    map.put("acceptable8", customerSaftyCheck.getAcceptable8());
	    map.put("acceptable9", customerSaftyCheck.getAcceptable9());
	    map.put("acceptable10", customerSaftyCheck.getAcceptable10());
	    map.put("acceptable11", customerSaftyCheck.getAcceptable11());
	    map.put("notifyRemark1", customerSaftyCheck.getNotifyRemark1());
	    map.put("notifyRemark2", customerSaftyCheck.getNotifyRemark2());
	    map.put("recommendation1", customerSaftyCheck.getRecommendation1());
	    map.put("recommendation2", customerSaftyCheck.getRecommendation2());
	    map.put("signatureFilePath", customerSaftyCheck.getSignatureFilePath());
	    map.put("signatureFileName", customerSaftyCheck.getSignatureFileName());
	    map.put("signatureImage", customerSaftyCheck.getSignatureImage());
	    map.put("userId", customerSaftyCheck.getUserId());
	    map.put("modifyDate", customerSaftyCheck.getModifyDate());
		
		return map;
	}
	
	/**
	 * HashMap을 CustomerSaftyCheck으로 변환
	 * @param map
	 * @return CustomerSaftyCheck
	 */
	protected static CustomerSaftyCheckList convertCustomerSaftyCheckList(HashMap<String, String> map){
		CustomerSaftyCheckList customerSaftyCheckList = new CustomerSaftyCheckList();
		
		customerSaftyCheckList.setAreaCode(map.get("areaCode"));
		customerSaftyCheckList.setCustomerCode(map.get("customerCode"));
		customerSaftyCheckList.setSequenceNumber(map.get("sequenceNumber"));
		customerSaftyCheckList.setCheckType(map.get("checkType"));
		customerSaftyCheckList.setCheckName(map.get("checkName"));
		customerSaftyCheckList.setScheduledCheckDate(map.get("scheduledCheckDate"));
		customerSaftyCheckList.setEmployeeCode(map.get("employeeCode"));
		customerSaftyCheckList.setEmployeeName(map.get("employeeName"));
		customerSaftyCheckList.setAcceptable1(map.get("acceptable1"));
		customerSaftyCheckList.setAcceptable2(map.get("acceptable2"));
		customerSaftyCheckList.setAcceptable3(map.get("acceptable3"));
		customerSaftyCheckList.setAcceptable4(map.get("acceptable4"));
		customerSaftyCheckList.setAcceptable5(map.get("acceptable5"));
		customerSaftyCheckList.setAcceptable6(map.get("acceptable6"));
		customerSaftyCheckList.setAcceptable7(map.get("acceptable7"));
		customerSaftyCheckList.setAcceptable8(map.get("acceptable8"));
		customerSaftyCheckList.setAcceptable9(map.get("acceptable9"));
		customerSaftyCheckList.setAcceptable10(map.get("acceptable10"));
		customerSaftyCheckList.setAcceptable11(map.get("acceptable11"));
		customerSaftyCheckList.setAcceptable12(map.get("acceptable12"));
		customerSaftyCheckList.setSignatureYn(map.get("signatureYn"));
		customerSaftyCheckList.setModifyDate(map.get("modifyDate"));
		
		return customerSaftyCheckList;
	}
	
	/**
	 * CustomerSaftyCheck을 HashMap으로 변환
	 * @param customerSaftyCheck
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerSaftyCheckList(CustomerSaftyCheckList customerSaftyCheckList){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerSaftyCheckList.getAreaCode());
	    map.put("customerCode", customerSaftyCheckList.getCustomerCode());
	    map.put("sequenceNumber", customerSaftyCheckList.getSequenceNumber());
	    map.put("checkType", customerSaftyCheckList.getCheckType());
	    map.put("checkName", customerSaftyCheckList.getCheckName());
	    map.put("scheduledCheckDate", customerSaftyCheckList.getScheduledCheckDate());
	    map.put("employeeCode", customerSaftyCheckList.getEmployeeCode());
	    map.put("employeeName", customerSaftyCheckList.getEmployeeName());
	    map.put("acceptable1", customerSaftyCheckList.getAcceptable1());
	    map.put("acceptable2", customerSaftyCheckList.getAcceptable2());
	    map.put("acceptable3", customerSaftyCheckList.getAcceptable3());
	    map.put("acceptable4", customerSaftyCheckList.getAcceptable4());
	    map.put("acceptable5", customerSaftyCheckList.getAcceptable5());
	    map.put("acceptable6", customerSaftyCheckList.getAcceptable6());
	    map.put("acceptable7", customerSaftyCheckList.getAcceptable7());
	    map.put("acceptable8", customerSaftyCheckList.getAcceptable8());
	    map.put("acceptable9", customerSaftyCheckList.getAcceptable9());
	    map.put("acceptable10", customerSaftyCheckList.getAcceptable10());
	    map.put("acceptable11", customerSaftyCheckList.getAcceptable11());
	    map.put("acceptable12", customerSaftyCheckList.getAcceptable12());
	    map.put("signatureYn", customerSaftyCheckList.getSignatureYn());
	    map.put("modifyDate", customerSaftyCheckList.getModifyDate());
		
		return map;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 */
	public CustomerSaftyCheckSignatureMap getCustomerSaftyCheckSignatures(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		return selectCustomerSaftyCheckSignatures(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 안전점검 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerSaftyCheckSignatureMap 형식의 거래처별 안전점검 목록 반환
	 */
	public CustomerSaftyCheckSignatureMap selectCustomerSaftyCheckSignatures(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckSignatureMap customerSaftyCheckSignatures = new CustomerSaftyCheckSignatureMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheckSignature customerSaftyCheckSignature = convertCustomerSaftyCheckSignature(map);
			customerSaftyCheckSignatures.setCustomerSaftyCheckSignature(customerSaftyCheckSignature.getKeyValue(), customerSaftyCheckSignature);
		}
		return customerSaftyCheckSignatures;
	}

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param scheduledCheckDate
	 * @param signatureImage
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheckSignature insertCustomerSaftyCheckSignature(
			  String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String scheduledCheckDate
			, String signatureImage
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("signatureImage", signatureImage);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
			
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_INSERT_ID, param); 
		HashMap<String, String> newParam = param;
		return convertCustomerSaftyCheckSignature(newParam); 
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param customerSaftyCheckSignature
	 * @return
	 */
	public CustomerSaftyCheckSignature insertCustomerSaftyCheckSignature(String serverIp , String catalogName, CustomerSaftyCheckSignature customerSaftyCheckSignature){
		HashMap<String, String> param = convertCustomerSaftyCheckSignature(customerSaftyCheckSignature);
		param.put("catalogName", catalogName);
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_INSERT_ID, param);
		return customerSaftyCheckSignature;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param scheduledCheckDate
	 * @param signatureImage
	 * @param userId
	 * @param modifyDate
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public CustomerSaftyCheckSignature updateCustomerSaftyCheckSignature(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String scheduledCheckDate
			, String signatureImage
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("signatureImage", signatureImage);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
		
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_UPDATE_ID, param);
		
		return convertCustomerSaftyCheckSignature(param);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param customerSaftyCheckSignature
	 * @return
	 */
	public CustomerSaftyCheckSignature updateCustomerSaftyCheckSignature(String serverIp, String catalogName, CustomerSaftyCheckSignature customerSaftyCheckSignature){
		HashMap<String, String> param = convertCustomerSaftyCheckSignature(customerSaftyCheckSignature);
		param.put("catalogName", catalogName);
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_UPDATE_ID, param);
		return customerSaftyCheckSignature;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public int deleteCustomerSaftyCheckSignature(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		
		return JdbcUtil.getInstance(serverIp).deleteQuery(GASMAX_CUSTOMER_SAFTY_CHECK_SIGNATURE_DELETE_ID, param); // 처리건수를 반환한다.
	}
	
	/**
	 * HashMap을 CustomerSaftyCheckSignature으로 변환
	 * @param map
	 * @return CustomerSaftyCheckSignature
	 */
	protected static CustomerSaftyCheckSignature convertCustomerSaftyCheckSignature(HashMap<String, String> map){
		CustomerSaftyCheckSignature customerSaftyCheckSignature = new CustomerSaftyCheckSignature();
		
		customerSaftyCheckSignature.setAreaCode(map.get("areaCode"));
		customerSaftyCheckSignature.setCustomerCode(map.get("customerCode"));
		customerSaftyCheckSignature.setSequenceNumber(map.get("sequenceNumber"));
		customerSaftyCheckSignature.setScheduledCheckDate(map.get("scheduledCheckDate"));
		customerSaftyCheckSignature.setSignatureImage(map.get("signatureImage"));
		customerSaftyCheckSignature.setUserId(map.get("userId"));
		customerSaftyCheckSignature.setModifyDate(map.get("modifyDate"));
		
		return customerSaftyCheckSignature;
	}
	
	/**
	 * CustomerSaftyCheckSignature을 HashMap으로 변환
	 * @param customerSaftyCheckSignature
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerSaftyCheckSignature(CustomerSaftyCheckSignature customerSaftyCheckSignature){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerSaftyCheckSignature.getAreaCode());
	    map.put("customerCode", customerSaftyCheckSignature.getCustomerCode());
	    map.put("sequenceNumber", customerSaftyCheckSignature.getSequenceNumber());
	    map.put("scheduledCheckDate", customerSaftyCheckSignature.getScheduledCheckDate());
	    map.put("signatureImage", customerSaftyCheckSignature.getSignatureImage());
	    map.put("userId", customerSaftyCheckSignature.getUserId());
	    map.put("modifyDate", customerSaftyCheckSignature.getModifyDate());
		
		return map;
	}
	
	
	/**
	 * 최근 거래처별 저장탱크 안전점검 상세정보 취득
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public CustomerSaftyCheckTank getCustomerSaftyCheckTankLast(String serverIp, String catalogName, String areaCode, String customerCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));

		return selectCustomerSaftyCheckTankLast(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheckTank selectCustomerSaftyCheckTankLast(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckTank customerSaftyCheckTank = null;
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_LAST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			customerSaftyCheckTank = convertCustomerSaftyCheckTank(map);
		}
		return customerSaftyCheckTank;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 */
	public CustomerSaftyCheckTankMap getCustomerSaftyCheckTanks(String serverIp, String catalogName, String areaCode, String customerCode, String sequenceNumber){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		if (("".equals(sequenceNumber)) || (sequenceNumber == null)){
		} else {
			condition.put("sequenceNumber", sequenceNumber);
		}
		return selectCustomerSaftyCheckTanks(serverIp, catalogName, condition);
	}
	
	public CustomerSaftyCheckTankMap selectCustomerSaftyCheckTanks(String serverIp, String catalogName, Map<String, String> condition){
		CustomerSaftyCheckTankMap customerSaftyCheckTanks = new CustomerSaftyCheckTankMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerSaftyCheckTank customerSaftyCheckTank = convertCustomerSaftyCheckTank(map);
			customerSaftyCheckTanks.setCustomerSaftyCheckTank(customerSaftyCheckTank.getKeyValue(), customerSaftyCheckTank);
		}
		return customerSaftyCheckTanks;
	}
	
	/**
	 * HashMap을 CustomerSaftyCheckTank으로 변환
	 * @param map
	 * @return CustomerSaftyCheckTank
	 */
	protected static CustomerSaftyCheckTank convertCustomerSaftyCheckTank(HashMap<String, String> map){
		CustomerSaftyCheckTank customerSaftyCheckTank = new CustomerSaftyCheckTank();
		
		customerSaftyCheckTank.setAreaCode(map.get("areaCode"));
		customerSaftyCheckTank.setCustomerCode(map.get("customerCode"));
		customerSaftyCheckTank.setSequenceNumber(map.get("sequenceNumber"));
		customerSaftyCheckTank.setScheduledCheckDate(map.get("scheduledCheckDate"));
		customerSaftyCheckTank.setEmployeeCode(map.get("employeeCode"));
		customerSaftyCheckTank.setEmployeeName(map.get("employeeName"));
		customerSaftyCheckTank.setTankCapacity1(map.get("tankCapacity1"));
		customerSaftyCheckTank.setTankCapacity2(map.get("tankCapacity2"));
		customerSaftyCheckTank.setAcceptable1(map.get("acceptable1"));
		customerSaftyCheckTank.setAcceptable1Comment(map.get("acceptable1Comment"));
		customerSaftyCheckTank.setAcceptable2(map.get("acceptable2"));
		customerSaftyCheckTank.setAcceptable2Comment(map.get("acceptable2Comment"));
		customerSaftyCheckTank.setAcceptable3(map.get("acceptable3"));
		customerSaftyCheckTank.setAcceptable3Comment(map.get("acceptable3Comment"));
		customerSaftyCheckTank.setAcceptable4(map.get("acceptable4"));
		customerSaftyCheckTank.setAcceptable4Comment(map.get("acceptable4Comment"));
		customerSaftyCheckTank.setAcceptable5(map.get("acceptable5"));
		customerSaftyCheckTank.setAcceptable5Comment(map.get("acceptable5Comment"));
		customerSaftyCheckTank.setAcceptable6(map.get("acceptable6"));
		customerSaftyCheckTank.setAcceptable6Comment(map.get("acceptable6Comment"));
		customerSaftyCheckTank.setAcceptable7(map.get("acceptable7"));
		customerSaftyCheckTank.setAcceptable7Comment(map.get("acceptable7Comment"));
		customerSaftyCheckTank.setAcceptable8(map.get("acceptable8"));
		customerSaftyCheckTank.setAcceptable8Comment(map.get("acceptable8Comment"));
		customerSaftyCheckTank.setAcceptable9(map.get("acceptable9"));
		customerSaftyCheckTank.setAcceptable9Comment(map.get("acceptable9Comment"));
		customerSaftyCheckTank.setAcceptable10Content(map.get("acceptable10Content"));
		customerSaftyCheckTank.setAcceptable10(map.get("acceptable10"));
		customerSaftyCheckTank.setAcceptable10Comment(map.get("acceptable10Comment"));
		customerSaftyCheckTank.setAcceptable11Content(map.get("acceptable11Content"));
		customerSaftyCheckTank.setAcceptable11(map.get("acceptable11"));
		customerSaftyCheckTank.setAcceptable11Comment(map.get("acceptable11Comment"));
		customerSaftyCheckTank.setAcceptable12Content(map.get("acceptable12Content"));
		customerSaftyCheckTank.setAcceptable12(map.get("acceptable12"));
		customerSaftyCheckTank.setAcceptable12Comment(map.get("acceptable12Comment"));
		customerSaftyCheckTank.setEmployeeComment1(map.get("employeeComment1"));
		customerSaftyCheckTank.setEmployeeComment2(map.get("employeeComment2"));
		customerSaftyCheckTank.setCustomerName(map.get("customerName"));
		customerSaftyCheckTank.setSignatureYn(map.get("signatureYn"));
		customerSaftyCheckTank.setSignatureImage(map.get("signatureImage"));
		customerSaftyCheckTank.setUserId(map.get("userId"));
		customerSaftyCheckTank.setModifyDate(map.get("modifyDate"));
		
		return customerSaftyCheckTank;
	}
	
	public CustomerSaftyCheckTank insertCustomerSaftyCheckTank(
			  String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String tankCapacity1
			, String tankCapacity2
			, String acceptable1
			, String acceptable1Comment
			, String acceptable2
			, String acceptable2Comment
			, String acceptable3
			, String acceptable3Comment
			, String acceptable4
			, String acceptable4Comment
			, String acceptable5
			, String acceptable5Comment
			, String acceptable6
			, String acceptable6Comment
			, String acceptable7
			, String acceptable7Comment
			, String acceptable8
			, String acceptable8Comment
			, String acceptable9
			, String acceptable9Comment
			, String acceptable10Content
			, String acceptable10
			, String acceptable10Comment
			, String acceptable11Content
			, String acceptable11
			, String acceptable11Comment
			, String acceptable12Content
			, String acceptable12
			, String acceptable12Comment
			, String employeeComment1
			, String employeeComment2
			, String customerName
			, String signatureYn
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		param.put("sequenceNumber", sequenceNumber);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("tankCapacity1", tankCapacity1);
		param.put("tankCapacity2", tankCapacity2);
		param.put("acceptable1", acceptable1);
		param.put("acceptable1Comment", acceptable1Comment);
		param.put("acceptable2", acceptable2);
		param.put("acceptable2Comment", acceptable2Comment);
		param.put("acceptable3", acceptable3);
		param.put("acceptable3Comment", acceptable3Comment);
		param.put("acceptable4", acceptable4);
		param.put("acceptable4Comment", acceptable4Comment);
		param.put("acceptable5", acceptable5);
		param.put("acceptable5Comment", acceptable5Comment);
		param.put("acceptable6", acceptable6);
		param.put("acceptable6Comment", acceptable6Comment);
		param.put("acceptable7", acceptable7);
		param.put("acceptable7Comment", acceptable7Comment);
		param.put("acceptable8", acceptable8);
		param.put("acceptable8Comment", acceptable8Comment);
		param.put("acceptable9", acceptable9);
		param.put("acceptable9Comment", acceptable9Comment);
		param.put("acceptable10Content", acceptable10Content);
		param.put("acceptable10", acceptable10);
		param.put("acceptable10Comment", acceptable10Comment);
		param.put("acceptable11Content", acceptable11Content);
		param.put("acceptable11", acceptable11);
		param.put("acceptable11Comment", acceptable11Comment);
		param.put("acceptable12Content", acceptable12Content);
		param.put("acceptable12", acceptable12);
		param.put("acceptable12Comment", acceptable12Comment);
		param.put("employeeComment1", employeeComment1);
		param.put("employeeComment2", employeeComment2);
		param.put("customerName", customerName);
		param.put("signatureYn", signatureYn);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
			
		JdbcUtil.getInstance(serverIp).insertQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_INSERT_ID, param);
		HashMap<String, String> newParam = param;
		return convertCustomerSaftyCheckTank(newParam);
	}
	
	public CustomerSaftyCheckTank updateCustomerSaftyCheckTank(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber
			, String scheduledCheckDate
			, String employeeCode
			, String employeeName
			, String tankCapacity1
			, String tankCapacity2
			, String acceptable1
			, String acceptable1Comment
			, String acceptable2
			, String acceptable2Comment
			, String acceptable3
			, String acceptable3Comment
			, String acceptable4
			, String acceptable4Comment
			, String acceptable5
			, String acceptable5Comment
			, String acceptable6
			, String acceptable6Comment
			, String acceptable7
			, String acceptable7Comment
			, String acceptable8
			, String acceptable8Comment
			, String acceptable9
			, String acceptable9Comment
			, String acceptable10Content
			, String acceptable10
			, String acceptable10Comment
			, String acceptable11Content
			, String acceptable11
			, String acceptable11Comment
			, String acceptable12Content
			, String acceptable12
			, String acceptable12Comment
			, String employeeComment1
			, String employeeComment2
			, String customerName
			, String signatureYn
			, String userId
			, String modifyDate
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		param.put("sequenceNumber", sequenceNumber);
		param.put("scheduledCheckDate", scheduledCheckDate);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("tankCapacity1", tankCapacity1);
		param.put("tankCapacity2", tankCapacity2);
		param.put("acceptable1", acceptable1);
		param.put("acceptable1Comment", acceptable1Comment);
		param.put("acceptable2", acceptable2);
		param.put("acceptable2Comment", acceptable2Comment);
		param.put("acceptable3", acceptable3);
		param.put("acceptable3Comment", acceptable3Comment);
		param.put("acceptable4", acceptable4);
		param.put("acceptable4Comment", acceptable4Comment);
		param.put("acceptable5", acceptable5);
		param.put("acceptable5Comment", acceptable5Comment);
		param.put("acceptable6", acceptable6);
		param.put("acceptable6Comment", acceptable6Comment);
		param.put("acceptable7", acceptable7);
		param.put("acceptable7Comment", acceptable7Comment);
		param.put("acceptable8", acceptable8);
		param.put("acceptable8Comment", acceptable8Comment);
		param.put("acceptable9", acceptable9);
		param.put("acceptable9Comment", acceptable9Comment);
		param.put("acceptable10Content", acceptable10Content);
		param.put("acceptable10", acceptable10);
		param.put("acceptable10Comment", acceptable10Comment);
		param.put("acceptable11Content", acceptable11Content);
		param.put("acceptable11", acceptable11);
		param.put("acceptable11Comment", acceptable11Comment);
		param.put("acceptable12Content", acceptable12Content);
		param.put("acceptable12", acceptable12);
		param.put("acceptable12Comment", acceptable12Comment);
		param.put("employeeComment1", employeeComment1);
		param.put("employeeComment2", employeeComment2);
		param.put("customerName", customerName);
		param.put("signatureYn", signatureYn);
		param.put("userId", userId);
		param.put("modifyDate", modifyDate);
		
		JdbcUtil.getInstance(serverIp).updateQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_UPDATE_ID, param);
		
		return convertCustomerSaftyCheckTank(param);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public int deleteCustomerSaftyCheckTank(
			String serverIp 
			, String catalogName 
			, String areaCode
			, String customerCode
			, String sequenceNumber) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		param.put("sequenceNumber", sequenceNumber);
		
		return JdbcUtil.getInstance(serverIp).deleteQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_DELETE_ID, param); // 처리건수를 반환한다.
	}
	
	public String selectTankSno(
			String serverIp, 
			String catalogName, 
			String areaCode, 
			String customerCode 
			){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", StringUtil.convertCustomerCodeToTankFormat(customerCode));
		
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_SAFTY_CHECK_TANK_SELECT_SNO_ID, condition); // 처리건수를 반환한다.
		String sequenceNumber = null;
		for( HashMap<String, String> map :  list) {
			sequenceNumber = map.get("sequenceNumber");
		}
		
		return sequenceNumber;
	}
	
	/**
	 * 비즈니스 로직 테스트용
	 * @param args
	 */
	public static void main(String[] args){
//		String keyword = "";
		String serverIp = "joatech2.dyndns.org";
		String catalogName = "GM_TestHigh";
		String areaCode = "01";
		String customerCode = "000-00984";
//		String sequenceNumber = "003";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
//		String startDate = "20100701";
//		String endDate = "20100731";
		
		try{
			System.out.println(BizCustomerSaftyCheck.getInstance().selectSno(serverIp, catalogName, areaCode, customerCode));
//			System.out.println(BizCustomerSaftyCheck.getInstance().deleteCustomerSaftyCheck(serverIp, catalogName, areaCode, customerCode, sequenceNumber));
//			CustomerSaftyCheck customerSaftyCheck = new CustomerSaftyCheck();
//			customerSaftyCheck.setAreaCode(areaCode);
//			customerSaftyCheck.setCustomerCode(customerCode);
//			customerSaftyCheck.setSequenceNumber(sequenceNumber);
//			customerSaftyCheck.setContractDate("20120919");
//			BizCustomerSaftyCheck.getInstance().updateCustomerSaftyCheck(serverIp, catalogName, customerSaftyCheck);
//			System.out.println(customerSaftyCheck.toXML());
//			CustomerSaftyCheckMap customerSaftyChecks = BizCustomerSaftyCheck.getInstance().getCustomerSaftyChecks(serverIp, catalogName, areaCode, customerCode, sequenceNumber);
//			System.out.println(customerSaftyChecks.toXML());
//			System.out.println(BizCustomerSaftyCheck.getInstance().insertCustomerSaftyCheck(
//						serverIp,
//						catalogName,
//						areaCode,
//						customerCode,
//						"J",
//						"20120914",
//						"조아빌딩",
//						"사용자",
//						"5000",
//						"500",
//						"0",
//						"01",
//						"사원",
//						"비고",
//						"20120914180904",
//						"01043322115_백원태"
//					)
//			);
//			
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerSaftyCheck customerSaftyCheck = new CustomerSaftyCheck();
//		customerSaftyCheck.setCustomerSaftyCheckCode("TEST1");
//		customerSaftyCheck.setCustomerSaftyCheckName("TEST CustomerSaftyCheck1");
//		customerSaftyCheck.setUseYesNo("Y");
//		BizCustomerSaftyCheck.getInstance().applyCustomerSaftyCheck(customerSaftyCheck);
		
/* DELETE */
//		BizCustomerSaftyCheck.getInstance().deleteCustomerSaftyCheck("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerSaftyCheck.getInstance().deleteCustomerSaftyChecks(list);

/* SELECT */
//		BizCustomerSaftyCheck.getInstance().initCacheCustomerSaftyChecks();
//		System.out.println(cacheCustomerSaftyChecks.toXML());
//		

//		System.out.println(cacheCustomerSaftyChecks.toXML());
	}
}
