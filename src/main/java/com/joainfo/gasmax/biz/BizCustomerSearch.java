package com.joainfo.gasmax.biz;

import com.joainfo.common.util.jdbc.JdbcIOException;
import com.joainfo.common.util.jdbc.JdbcProcedureException;
import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.AppUser;
import com.joainfo.gasmax.bean.CustomerSearch;
import com.joainfo.gasmax.bean.list.CustomerSearchMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BizCustomerSearch
 * 거래처 코드 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerSearch {

	/**
	 * 거래처 코드 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SEARCH_SELECT_ID = "GASMAX.CustomerSearch.Select";
	
	/**
	 * 거래처 코드 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SEARCH_INSERT_ID = "GASMAX.CustomerSearch.Insert";

	/**
	 * 거래처 코드 Update 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_SEARCH_UPDATE_ID = "GASMAX.CustomerSearch.Update";
	
	/**
	 * BizCustomerSearch 인스턴스
	 */
	private static BizCustomerSearch bizCustomerSearch;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerSearch(){
	}
	
	/**
	 * Singleton으로 BizCustomerSearch 인스턴스 생성
	 * @return bizCustomerSearch
	 */
	public static BizCustomerSearch getInstance(){
		if (bizCustomerSearch == null){
			bizCustomerSearch = new BizCustomerSearch();
		}
		return bizCustomerSearch;
	}
	
	/**
	 * 키워드로 검색한 거래처 코드 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드(1건만 검색할 때 필요)
	 * @param customerSearchType ( 0:중량, 1:체적검침(건물제외), 2:체적공급, A:전체거래처) 
	 * @param employeeCode 사원 코드
	 * @param keyword 검색어
	 * @param grantCode 권한 코크
	 * @return customerSearches
	 */
	public CustomerSearchMap getCustomerSearches(String serverIp, String catalogName, String areaCode, String customerCode, String customerSearchType, String employeeCode, String keyword, String grantCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode==null?"":customerCode);
		condition.put("customerSearchType", customerSearchType==null?"A":customerSearchType);
		condition.put("employeeCode", employeeCode);
		condition.put("keyword", keyword);
		condition.put("grantCode", grantCode);
		
		return selectCustomerSearches(serverIp, catalogName, condition);
	}


	/**
	 *
	 * @param appUser
	 * @param keyword
	 * @return
	 */
	public static CustomerSearch getCustomerSearch(AppUser appUser, String keyword) {
		if (appUser != null) {
			CustomerSearchMap customerSearchMap = BizCustomerSearch.getInstance().getCustomerSearches(
					appUser.getIpAddress(),
					appUser.getDbCatalogName(),
					appUser.getAreaCode(),
					"", // customerCode
					"A", // customerSearchType
					appUser.getEmployeeCode(),
					keyword,
					appUser.getMenuPermission() // 권한 코드 전체 전달
			);

			// 첫 번째 요소 가져오기
			if (customerSearchMap != null && customerSearchMap.getCustomerSearches() != null
					&& !customerSearchMap.getCustomerSearches().isEmpty()) {
				return customerSearchMap.getCustomerSearches().values().iterator().next();
			}
		}

		return null; // appUser가 없거나, 검색 결과가 없을 경우 null 반환
	}

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param customerSearchType
	 * @param employeeCode
	 * @param keyword
	 * @param grantCode
	 * @return
	 */
	public CustomerSearch getCustomerSearch(String serverIp, String catalogName, String areaCode, String customerCode, String customerSearchType, String employeeCode, String keyword, String grantCode){
		CustomerSearchMap customerSearches = getCustomerSearches(serverIp, catalogName, areaCode, customerCode, customerSearchType, employeeCode, keyword, grantCode);
		return customerSearches.getCustomerSearch(customerSearches.getKey(areaCode, customerCode));
	}
	
	/**
	 * 거래처 코드 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerSearchMap 형식의 거래처 코드 목록 반환
	 */
	public CustomerSearchMap selectCustomerSearches(String serverIp, String catalogName, Map<String, String> condition){
		try{
			CustomerSearchMap customerSearches = new CustomerSearchMap();
			condition.put("catalogName", catalogName);

			@SuppressWarnings("rawtypes")
			List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery("GASMAX.CustomerSearch.Select", condition);
			for( HashMap<String, String> map :  list) {
				CustomerSearch customerSearch = convertCustomerSearch(map);
				customerSearches.setCustomerSearch(customerSearch.getKeyValue(), customerSearch);
			}
			return customerSearches;
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 거래처 코드정보를 한 건 추가한 후 등록된 신규 거래처 코드 반환
	 * @param serverIp
	 * @param catalogName
	 * @param appUserMobileNumber
	 * @param customerSearch
	 * @return 처리 건수
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public String insertCustomerSearch(String serverIp, String catalogName, String appUserMobileNumber, CustomerSearch customerSearch) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, Object> param = convertCustomerSearch(customerSearch);
		param.put("catalogName", catalogName);
		param.put("appUserMobileNumber", appUserMobileNumber);
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_SEARCH_INSERT_ID, param, "outputCustomerCode"); // 신규로 등록된 거래처 코드를 반환한다.
	}
	
	/**
	 * 거래처 코드정보를 한 건 수정
	 * @param serverIp
	 * @param catalogName
	 * @param appUserMobileNumber
	 * @param customerSearch
	 * @return 처리 건수
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public int updateCustomerSearch(String serverIp, String catalogName, String appUserMobileNumber, CustomerSearch customerSearch) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, Object> param = convertCustomerSearch(customerSearch);
		param.put("catalogName", catalogName);
		param.put("appUserMobileNumber", appUserMobileNumber);
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_SEARCH_UPDATE_ID, param);
	}
	
	/**
	 * HashMap을 CustomerSearch으로 변환
	 * @param map
	 * @return CustomerSearch
	 */
	protected static CustomerSearch convertCustomerSearch(HashMap<String, String> map){
		CustomerSearch customerSearch = new CustomerSearch();
		
		customerSearch.setAreaCode(map.get("areaCode"));
		customerSearch.setCustomerCode(map.get("customerCode"));
		customerSearch.setCustomerType(map.get("customerType"));
		customerSearch.setCustomerTypeName(map.get("customerTypeName"));
		customerSearch.setCustomerStatusCode(map.get("customerStatusCode"));
		customerSearch.setCustomerStatusName(map.get("customerStatusName"));
		customerSearch.setCustomerName(map.get("customerName"));
		customerSearch.setBuildingName(map.get("buildingName"));
		customerSearch.setUserName(map.get("userName"));
		customerSearch.setPhoneNumber(map.get("phoneNumber"));
		customerSearch.setMobileNumber(map.get("mobileNumber"));
		customerSearch.setPhoneNumber2(map.get("phoneNumber2"));
		customerSearch.setPhoneNumberFind(map.get("phoneNumberFind"));
		customerSearch.setMobileNumberFind(map.get("mobileNumberFind"));
		customerSearch.setPhoneNumberFind2(map.get("phoneNumberFind2"));
		customerSearch.setAddress1(map.get("address1"));
		customerSearch.setAddress2(map.get("address2"));
		customerSearch.setRemark1(map.get("remark1"));
		customerSearch.setRemark2(map.get("remark2"));
		customerSearch.setEmployeeCode(map.get("employeeCode"));
		customerSearch.setEmployeeName(map.get("employeeName"));
		customerSearch.setConsumeTypeCode(map.get("consumeTypeCode"));
		customerSearch.setConsumerTypeName(map.get("consumerTypeName"));
		customerSearch.setFreeInstallationFee(map.get("freeInstallationFee"));
		customerSearch.setContainerDeposit(map.get("containerDeposit"));
		customerSearch.setWeightReceivable(map.get("weightReceivable"));
		customerSearch.setVolumeReceivable(map.get("volumeReceivable"));
		customerSearch.setMemo(map.get("memo"));
		customerSearch.setVatType(map.get("vatType"));
		customerSearch.setWeightPriceType(map.get("weightPriceType"));
		customerSearch.setSalePriceDiscountRate(map.get("salePriceDiscountRate"));
		customerSearch.setIssueTaxbillYesNo(map.get("issueTaxbillYesNo"));
		customerSearch.setRegisterNumberType(map.get("registerNumberType"));
		customerSearch.setRegisterNumber(map.get("registerNumber"));
		customerSearch.setRegisterName(map.get("registerName"));
		customerSearch.setRegisterOwner(map.get("registerOwner"));
		customerSearch.setRegisterAddress1(map.get("registerAddress1"));
		customerSearch.setRegisterAddress2(map.get("registerAddress2"));
		customerSearch.setBusinessCondition(map.get("businessCondition"));
		customerSearch.setBusinessType(map.get("businessType"));
		customerSearch.setContactName(map.get("contactName"));
		customerSearch.setContactDepartment(map.get("contactDepartment"));
		customerSearch.setContactEmail(map.get("contactEmail"));
		customerSearch.setContactPhoneNumber(map.get("contactPhoneNumber"));
		customerSearch.setContactFaxNumber(map.get("contactFaxNumber"));
		customerSearch.setRegulatorPressure(map.get("regulatorPressure"));
		customerSearch.setPriceType(map.get("priceType"));
		customerSearch.setApplyPrice(map.get("applyPrice"));
		customerSearch.setEnvironmentPrice(map.get("environmentPrice"));
		customerSearch.setIndividualPrice(map.get("individualPrice"));
		customerSearch.setDiscountPrice(map.get("discountPrice"));
		customerSearch.setPriceMode(map.get("priceMode"));
		customerSearch.setDiscountAmount(map.get("discountAmount"));
		customerSearch.setDefaultRate(map.get("defaultRate"));
		customerSearch.setDiscountRate(map.get("discountRate"));
		customerSearch.setMaintenanceFee(map.get("maintenanceFee"));
		customerSearch.setInstallationFee(map.get("installationFee"));
		customerSearch.setGaugeReplacementFee(map.get("gaugeReplacementFee"));
		customerSearch.setPaymentType(map.get("paymentType"));
		customerSearch.setPaymentTypeName(map.get("paymentTypeName"));
		customerSearch.setSerialNumber(map.get("serialNumber"));
		customerSearch.setReadMeterDay(map.get("readMeterDay"));
		customerSearch.setContractNumber(map.get("contractNumber"));
		customerSearch.setContractDate(map.get("contractDate"));
		customerSearch.setLatestSaftyCheckDate(map.get("latestSaftyCheckDate"));
		customerSearch.setContractName(map.get("contractName"));
		customerSearch.setContracterResidentNumber(map.get("contracterResidentNumber"));
		customerSearch.setContainerOwnerType(map.get("containerOwnerType"));
		customerSearch.setFacilityOwnerType(map.get("facilityOwnerType"));
		customerSearch.setFacilityOkYesNo(map.get("facilityOkYesNo"));
		customerSearch.setSwitcherCapacity(map.get("switcherCapacity"));
		customerSearch.setVaporizerCapacity(map.get("vaporizerCapacity"));
		customerSearch.setGaugeVolume(map.get("gaugeVolume"));
		customerSearch.setFuseCockQuantity(map.get("fuseCockQuantity"));
		customerSearch.setHoseLength(map.get("hoseLength"));
		customerSearch.setValve(map.get("valve"));
		customerSearch.setSupplyPipe1(map.get("supplyPipe1"));
		customerSearch.setSupplyPipe2(map.get("supplyPipe2"));

		
		return customerSearch;
	}
	
	/**
	 * CustomerSearch을 HashMap으로 변환
	 * @param customerSearch
	 * @return
	 */
	protected static HashMap<String, Object> convertCustomerSearch(CustomerSearch customerSearch){
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("areaCode", customerSearch.getAreaCode());
		map.put("customerCode", customerSearch.getCustomerCode());
		map.put("customerType", customerSearch.getCustomerType());
		map.put("customerTypeName", customerSearch.getCustomerTypeName());
		map.put("customerStatusCode", customerSearch.getCustomerStatusCode());
		map.put("customerStatusName", customerSearch.getCustomerStatusName());
		map.put("customerName", customerSearch.getCustomerName());
		map.put("buildingName", customerSearch.getBuildingName());
		map.put("userName", customerSearch.getUserName());
		map.put("phoneNumber", customerSearch.getPhoneNumber());
		map.put("mobileNumber", customerSearch.getMobileNumber());
		map.put("phoneNumber2", customerSearch.getPhoneNumber2());
		map.put("phoneNumberFind", customerSearch.getPhoneNumberFind());
		map.put("mobileNumberFind", customerSearch.getMobileNumberFind());
		map.put("phoneNumberFind2", customerSearch.getPhoneNumberFind2());
		map.put("address1", customerSearch.getAddress1());
		map.put("address2", customerSearch.getAddress2());
		map.put("remark1", customerSearch.getRemark1());
		map.put("remark2", customerSearch.getRemark2());
		map.put("employeeCode", customerSearch.getEmployeeCode());
		map.put("employeeName", customerSearch.getEmployeeName());
		map.put("consumeTypeCode", customerSearch.getConsumeTypeCode());
		map.put("consumerTypeName", customerSearch.getConsumerTypeName());
		map.put("freeInstallationFee", customerSearch.getFreeInstallationFee());
		map.put("containerDeposit", customerSearch.getContainerDeposit());
		map.put("weightReceivable", customerSearch.getWeightReceivable());
		map.put("volumeReceivable", customerSearch.getVolumeReceivable());
		map.put("memo", customerSearch.getMemo());
		map.put("vatType", customerSearch.getVatType());
		map.put("weightPriceType", customerSearch.getWeightPriceType());
		map.put("salePriceDiscountRate", customerSearch.getSalePriceDiscountRate());
		map.put("issueTaxbillYesNo", customerSearch.getIssueTaxbillYesNo());
		map.put("registerNumberType", customerSearch.getRegisterNumberType());
		map.put("registerNumber", customerSearch.getRegisterNumber());
		map.put("registerName", customerSearch.getRegisterName());
		map.put("registerOwner", customerSearch.getRegisterOwner());
		map.put("registerAddress1", customerSearch.getRegisterAddress1());
		map.put("registerAddress2", customerSearch.getRegisterAddress2());
		map.put("businessCondition", customerSearch.getBusinessCondition());
		map.put("businessType", customerSearch.getBusinessType());
		map.put("contactName", customerSearch.getContactName());
		map.put("contactDepartment", customerSearch.getContactDepartment());
		map.put("contactEmail", customerSearch.getContactEmail());
		map.put("contactPhoneNumber", customerSearch.getContactPhoneNumber());
		map.put("contactFaxNumber", customerSearch.getContactFaxNumber());
		map.put("regulatorPressure", customerSearch.getRegulatorPressure());
		map.put("priceType", customerSearch.getPriceType());
		map.put("applyPrice", customerSearch.getApplyPrice());
		map.put("environmentPrice", customerSearch.getEnvironmentPrice());
		map.put("individualPrice", customerSearch.getIndividualPrice());
		map.put("discountPrice", customerSearch.getDiscountPrice());
		map.put("priceMode", customerSearch.getPriceMode());
		map.put("discountAmount", customerSearch.getDiscountAmount());
		map.put("defaultRate", customerSearch.getDefaultRate());
		map.put("discountRate", customerSearch.getDiscountRate());
		map.put("maintenanceFee", customerSearch.getMaintenanceFee());
		map.put("installationFee", customerSearch.getInstallationFee());
		map.put("gaugeReplacementFee", customerSearch.getGaugeReplacementFee());
		map.put("paymentType", customerSearch.getPaymentType());
		map.put("paymentTypeName", customerSearch.getPaymentTypeName());
		map.put("serialNumber", customerSearch.getSerialNumber());
		map.put("readMeterDay", customerSearch.getReadMeterDay());
		map.put("contractNumber", customerSearch.getContractNumber());
		map.put("contractDate", customerSearch.getContractDate());
		map.put("latestSaftyCheckDate", customerSearch.getLatestSaftyCheckDate());
		map.put("contractName", customerSearch.getContractName());
		map.put("contracterResidentNumber", customerSearch.getContracterResidentNumber());
		map.put("containerOwnerType", customerSearch.getContainerOwnerType());
		map.put("facilityOwnerType", customerSearch.getFacilityOwnerType());
		map.put("facilityOkYesNo", customerSearch.getFacilityOkYesNo());
		map.put("switcherCapacity", customerSearch.getSwitcherCapacity());
		map.put("vaporizerCapacity", customerSearch.getVaporizerCapacity());
		map.put("gaugeVolume", customerSearch.getGaugeVolume());
		map.put("fuseCockQuantity", customerSearch.getFuseCockQuantity());
		map.put("hoseLength", customerSearch.getHoseLength());
		map.put("valve", customerSearch.getValve());
		map.put("supplyPipe1", customerSearch.getSupplyPipe1());
		map.put("supplyPipe2", customerSearch.getSupplyPipe2());

		return map;
	}
	
	/**
	 * 비즈니스 로직 테스트용
	 * @param args
	 */
	public static void main(String[] args){
		String keyword = "";
		String serverIp = "joatech2.dyndns.org";
		String catalogName = "GM_TestHigh";
		String areaCode = "01";
		String customerCode = "000-01981";
		String customerType = "A";
		String employeeCode = "";
		String grantCode = "00111111";
		try{
			CustomerSearch customerSearch = BizCustomerSearch.getInstance().getCustomerSearch(serverIp, catalogName, areaCode, customerCode, customerType, employeeCode, keyword, grantCode);
			System.out.println(customerSearch.toXML());
//			System.out.println(pagingXML.get("1"));
//			System.out.println(pagingXML.get("2"));
//			System.out.println(pagingXML.get("3"));
//			System.out.println(pagingXML.get("4"));
		} catch (Exception e){
			e.printStackTrace();
		}

/* INSERT OR UPDATE*/
//		CustomerSearch customerSearch = new CustomerSearch();
//		customerSearch.setCustomerSearchCode("TEST1");
//		customerSearch.setCustomerSearchName("TEST CustomerSearch1");
//		customerSearch.setUseYesNo("Y");
//		BizCustomerSearch.getInstance().applyCustomerSearch(customerSearch);
		
/* DELETE */
//		BizCustomerSearch.getInstance().deleteCustomerSearch("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerSearch.getInstance().deleteCustomerSearches(list);

/* SELECT */
//		BizCustomerSearch.getInstance().initCacheCustomerSearches();
//		System.out.println(cacheCustomerSearches.toXML());
//		

//		System.out.println(cacheCustomerSearches.toXML());
	}
}
