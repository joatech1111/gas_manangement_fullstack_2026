package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerLatestReadMeter;
import com.joainfo.gasmax.bean.CustomerVolumeReadMeter;
import com.joainfo.gasmax.bean.list.CustomerVolumeReadMeterMap;

/**
 * 거래처별 체적장부 - 검침내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerVolumeReadMeter {

	/**
	 * 거래처별 체적장부 - 검침내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_VOLUME_READ_METER_SELECT_ID = "GASMAX.CustomerVolumeReadMeter.Select";
	
	/**
	 * 거래처별 최종 검침내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_LATEST_READ_METER_SELECT_ID = "GASMAX.CustomerLatestReadMeter.Select";
	
	/**
	 * 거래처별 검침내역 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_READ_METER_INSERT_ID = "GASMAX.CustomerReadMeter.Insert";
	
	/**
	 * 거래처별 장부 - 검침내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_READ_METER_DELETE_ID = "GASMAX.CustomerReadMeter.Delete";
	
	/**
	 * BizCustomerVolumeReadMeter 인스턴스
	 */
	private static BizCustomerVolumeReadMeter bizCustomerVolumeReadMeter;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerVolumeReadMeter(){
	}
	
	/**
	 * Singleton으로 BizCustomerVolumeReadMeter 인스턴스 생성
	 * @return bizCustomerVolumeReadMeter
	 */
	public static BizCustomerVolumeReadMeter getInstance(){
		if (bizCustomerVolumeReadMeter == null){
			bizCustomerVolumeReadMeter = new BizCustomerVolumeReadMeter();
		}
		return bizCustomerVolumeReadMeter;
	}
	
	/**
	 * 키워드로 검색한 거래처별 체적장부 - 검침내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerVolumeReadMeters
	 */
	public CustomerVolumeReadMeterMap getCustomerVolumeReadMeters(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerVolumeReadMeters(serverIp, catalogName, condition);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param readMeterDate
	 * @return
	 */
	public CustomerLatestReadMeter getCustomerLatestReadMeter(String serverIp, String catalogName, String areaCode, String customerCode, String readMeterDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("readMeterDate", readMeterDate);
		
		return selectCustomerLatestReadMeter(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 체적장부 - 검침내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerVolumeReadMeterMap 형식의 거래처별 체적장부 - 검침내역 목록 반환
	 */
	public CustomerVolumeReadMeterMap selectCustomerVolumeReadMeters(String serverIp, String catalogName, Map<String, String> condition){
		CustomerVolumeReadMeterMap customerVolumeReadMeters = new CustomerVolumeReadMeterMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_VOLUME_READ_METER_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerVolumeReadMeter customerVolumeReadMeter = convertCustomerVolumeReadMeter(map);
			customerVolumeReadMeters.setCustomerVolumeReadMeter(customerVolumeReadMeter.getKeyValue(), customerVolumeReadMeter);
		}
		return customerVolumeReadMeters;
	}
	
	/**
	 * 거래처별 최근 체적검침 내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerVolumeReadMeterMap 형식의 거래처별 체적장부 - 검침내역 목록 반환
	 */
	public CustomerLatestReadMeter selectCustomerLatestReadMeter(String serverIp, String catalogName, Map<String, String> condition){
		CustomerLatestReadMeter customerLatestReadMeter = new CustomerLatestReadMeter();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_LATEST_READ_METER_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			customerLatestReadMeter.setAreaCode(map.get("areaCode"));
			customerLatestReadMeter.setCustomerCode(map.get("customerCode"));
			customerLatestReadMeter.setBuildingName(map.get("buildingName"));
			customerLatestReadMeter.setUserName(map.get("userName"));
			customerLatestReadMeter.setEmployeeCode(map.get("employeeCode"));
			customerLatestReadMeter.setEmployeeName(map.get("employeeName"));
			customerLatestReadMeter.setDelayFeeMethodType(map.get("delayFeeMethodType"));
			customerLatestReadMeter.setRoundType(map.get("roundType"));
			customerLatestReadMeter.setPriceType(map.get("priceType"));
			customerLatestReadMeter.setEnvironmentPrice(map.get("environmentPrice"));
			customerLatestReadMeter.setUnitPrice(map.get("unitPrice"));
			customerLatestReadMeter.setDiscountPrice(map.get("discountPrice"));
			customerLatestReadMeter.setDefaultAmountYesNo(map.get("defaultAmountYesNo"));
			customerLatestReadMeter.setDefaultUse(map.get("defaultUse"));
			customerLatestReadMeter.setDefaultAmount(map.get("defaultAmount"));
			customerLatestReadMeter.setManagementAmount(map.get("managementAmount"));
			customerLatestReadMeter.setDelayFeePercent(map.get("delayFeePercent"));
			customerLatestReadMeter.setDiscountPercent(map.get("discountPercent"));
			customerLatestReadMeter.setDelayAmount1(map.get("delayAmount1"));
			customerLatestReadMeter.setDelayAmount2(map.get("delayAmount2"));
			customerLatestReadMeter.setDelayAmount3(map.get("delayAmount3"));
			customerLatestReadMeter.setDelayAmount4(map.get("delayAmount4"));
			customerLatestReadMeter.setDelayAmount5(map.get("delayAmount5"));
			customerLatestReadMeter.setPreSequenceNumber(map.get("preSequenceNumber"));
			customerLatestReadMeter.setPreReadMeterDate(map.get("preReadMeterDate"));
			customerLatestReadMeter.setPreReadMeter(map.get("preReadMeter"));
			customerLatestReadMeter.setPreAmount(map.get("preAmount"));
			customerLatestReadMeter.setPreRemain(map.get("preRemain"));
			customerLatestReadMeter.setCollectDate(map.get("collectDate"));
			customerLatestReadMeter.setPreUnpaidAmount(map.get("preUnpaidAmount"));
			customerLatestReadMeter.setApplyDelayDate(map.get("applyDelayDate"));
			
			break;
		}
		return customerLatestReadMeter;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @param readMeterDate
	 * @param customerName
	 * @param userName
	 * @param employeeCode
	 * @param employeeName
	 * @param preMonthReadMeter
	 * @param thisMonthReadMeter
	 * @param useQuantity
	 * @param price
	 * @param useAmount
	 * @param managementAmount
	 * @param discountAmount
	 * @param delayAmount
	 * @param totalAmount
	 * @param preRemain
	 * @param nowRemain
	 * @param remark
	 * @param startDate
	 * @param endDate
	 * @param preUnpaidAmount
	 * @param applyDelayDate
	 * @param defaultAmount
	 * @param registerDate
	 * @param userId
	 * @return
	 */
	public String insertCustomerReadMeter(
		  String serverIp
		, String catalogName
		, String areaCode 
		, String customerCode
		, String sequenceNumber
		, String readMeterDate
		, String customerName
		, String userName
		, String employeeCode
		, String employeeName
		, String preMonthReadMeter
		, String thisMonthReadMeter
		, String useQuantity
		, String price
		, String useAmount
		, String managementAmount
		, String discountAmount
		, String delayAmount
		, String thisMonthAmount
		, String preRemain
		, String nowRemain
		, String remark
		, String startDate
		, String endDate
		, String preUnpaidAmount
		, String applyDelayDate
		, String defaultDelayAmount
		, String defaultAmountYesNo
		, String defaultAmount
		, String registerDate
		, String userId){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("readMeterDate", readMeterDate);
		param.put("customerName", customerName);
		param.put("userName", userName);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("preMonthReadMeter", new Integer(preMonthReadMeter));
		param.put("thisMonthReadMeter", new Integer(thisMonthReadMeter));
		param.put("useQuantity", new Integer(useQuantity));
		param.put("price", new Float(price));
		param.put("useAmount", new Float(useAmount));
		param.put("managementAmount", new Float(managementAmount));
		param.put("discountAmount", new Float(discountAmount));
		param.put("delayAmount", new Float(delayAmount));
		param.put("thisMonthAmount", new Float(thisMonthAmount));
		param.put("preRemain", new Integer(preRemain));
		param.put("nowRemain", new Integer(nowRemain));
		param.put("remark", remark);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("preUnpaidAmount", new Float(preUnpaidAmount));
		param.put("applyDelayDate", applyDelayDate);
		param.put("defaultDelayAmount", new Float(defaultDelayAmount));
		param.put("defaultAmountYesNo", defaultAmountYesNo);
		param.put("defaultAmount", new Float(defaultAmount));
		param.put("registerDate", registerDate);
		param.put("userId", userId);
		
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_READ_METER_INSERT_ID, param, "outputMessage"); // 에러 코드를 반환한다.

	}

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param sequenceNumber
	 * @return
	 */
	public String deleteCustomerReadMeter(
			  String serverIp
			, String catalogName
			, String areaCode 
			, String customerCode
			, String sequenceNumber
			, String transactionDate
			, String userId
			){
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("catalogName", catalogName);
			param.put("areaCode", areaCode);
			param.put("customerCode", customerCode);
			param.put("sequenceNumber", sequenceNumber);
			param.put("transactionDate", transactionDate);
			param.put("userId", userId);
			
			return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_READ_METER_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	
	/**
	 * HashMap을 CustomerVolumeReadMeter으로 변환
	 * @param map
	 * @return CustomerVolumeReadMeter
	 */
	protected static CustomerVolumeReadMeter convertCustomerVolumeReadMeter(HashMap<String, String> map){
		CustomerVolumeReadMeter customerVolumeReadMeter = new CustomerVolumeReadMeter();
		
		customerVolumeReadMeter.setAreaCode(map.get("areaCode"));
		customerVolumeReadMeter.setCustomerCode(map.get("customerCode"));
		customerVolumeReadMeter.setSequenceNumber(map.get("sequenceNumber"));
		customerVolumeReadMeter.setReadMeterDate(map.get("readMeterDate"));
		customerVolumeReadMeter.setPreMonthReadMeter(map.get("preMonthReadMeter"));
		customerVolumeReadMeter.setNowMonthReadMeter(map.get("nowMonthReadMeter"));
		customerVolumeReadMeter.setUseQuantity(map.get("useQuantity"));
		customerVolumeReadMeter.setPrice(map.get("price"));
		customerVolumeReadMeter.setUseAmount(map.get("useAmount"));
		customerVolumeReadMeter.setManageAmount(map.get("manageAmount"));
		customerVolumeReadMeter.setDiscountAmount(map.get("discountAmount"));
		customerVolumeReadMeter.setDelayAmount(map.get("delayAmount"));
		customerVolumeReadMeter.setNowMonthAmount(map.get("nowMonthAmount"));
		customerVolumeReadMeter.setRemark(map.get("remark"));
		customerVolumeReadMeter.setRemainQuantity(map.get("remainQuantity"));
		customerVolumeReadMeter.setCollectDate(map.get("collectDate"));
		customerVolumeReadMeter.setUnpaidAmount(map.get("unpaidAmount"));
		
		return customerVolumeReadMeter;
	}
	
	/**
	 * CustomerVolumeReadMeter을 HashMap으로 변환
	 * @param customerVolumeReadMeter
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerVolumeReadMeter(CustomerVolumeReadMeter customerVolumeReadMeter){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("areaCode", customerVolumeReadMeter.getAreaCode());
		map.put("customerCode", customerVolumeReadMeter.getCustomerCode());
		map.put("sequenceNumber", customerVolumeReadMeter.getSequenceNumber());
		map.put("readMeterDate", customerVolumeReadMeter.getReadMeterDate());
		map.put("preMonthReadMeter", customerVolumeReadMeter.getPreMonthReadMeter());
		map.put("nowMonthReadMeter", customerVolumeReadMeter.getNowMonthReadMeter());
		map.put("useQuantity", customerVolumeReadMeter.getUseQuantity());
		map.put("price", customerVolumeReadMeter.getPrice());
		map.put("useAmount", customerVolumeReadMeter.getUseAmount());
		map.put("manageAmount", customerVolumeReadMeter.getManageAmount());
		map.put("discountAmount", customerVolumeReadMeter.getDiscountAmount());
		map.put("delayAmount", customerVolumeReadMeter.getDelayAmount());
		map.put("nowMonthAmount", customerVolumeReadMeter.getNowMonthAmount());
		map.put("remark", customerVolumeReadMeter.getRemark());
		map.put("remainQuantity", customerVolumeReadMeter.getRemainQuantity());
		map.put("collectDate", customerVolumeReadMeter.getCollectDate());
		map.put("unpaidAmount", customerVolumeReadMeter.getUnpaidAmount());
		
		return map;
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
		String customerCode = "000-03286";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
//		String startDate = "20090701";
//		String endDate = "20120731";
		
		try{
//			CustomerVolumeReadMeterMap customerVolumeReadMeters = BizCustomerVolumeReadMeter.getInstance().getCustomerVolumeReadMeters(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
//			System.out.println(customerVolumeReadMeters.toXML());
//			CustomerLatestReadMeter customerLatestReadMeter = BizCustomerVolumeReadMeter.getInstance().getCustomerLatestReadMeter(serverIp, catalogName, areaCode, customerCode, "20120920");
//			System.out.println(customerLatestReadMeter.toXML());
			System.out.println(BizCustomerVolumeReadMeter.getInstance().insertCustomerReadMeter(serverIp, catalogName, areaCode, customerCode, "20120903", "20120926", "가족건강원", "1", "1", "1", "0", "1", "1", "1", "1", "0", "0", "0", "1", "0", "1", "111", "20120825", "20120926", "0", "20120926", "0", "Y", "0", "20120926160530", "01043322115백원태"));
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerVolumeReadMeter customerVolumeReadMeter = new CustomerVolumeReadMeter();
//		customerVolumeReadMeter.setCustomerVolumeReadMeterCode("TEST1");
//		customerVolumeReadMeter.setCustomerVolumeReadMeterName("TEST CustomerVolumeReadMeter1");
//		customerVolumeReadMeter.setUseYesNo("Y");
//		BizCustomerVolumeReadMeter.getInstance().applyCustomerVolumeReadMeter(customerVolumeReadMeter);
		
/* DELETE */
//		BizCustomerVolumeReadMeter.getInstance().deleteCustomerVolumeReadMeter("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerVolumeReadMeter.getInstance().deleteCustomerVolumeReadMeters(list);

/* SELECT */
//		BizCustomerVolumeReadMeter.getInstance().initCacheCustomerVolumeReadMeters();
//		System.out.println(cacheCustomerVolumeReadMeters.toXML());
//		

//		System.out.println(cacheCustomerVolumeReadMeters.toXML());
	}
}
