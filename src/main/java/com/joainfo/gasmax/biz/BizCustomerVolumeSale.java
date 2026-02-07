package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerVolumeSale;
import com.joainfo.gasmax.bean.list.CustomerVolumeSaleMap;

/**
 * 거래처별 체적장부 - 공급(판매)내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerVolumeSale {

	/**
	 * 거래처별 체적장부 - 공급(판매)내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_VOLUME_SALE_SELECT_ID = "GASMAX.CustomerVolumeSale.Select";
	
	/**
	 * 거래처별 체적장부 - 공급(판매)내역 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_VOLUME_SALE_INSERT_ID = "GASMAX.CustomerVolumeSale.Insert";
	
	/**
	 * 거래처별 체적장부 - 공급(판매)내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_VOLUME_SALE_DELETE_ID = "GASMAX.CustomerVolumeSale.Delete";
	
	/**
	 * BizCustomerVolumeSale 인스턴스
	 */
	private static BizCustomerVolumeSale bizCustomerVolumeSale;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerVolumeSale(){
	}
	
	/**
	 * Singleton으로 BizCustomerVolumeSale 인스턴스 생성
	 * @return bizCustomerVolumeSale
	 */
	public static BizCustomerVolumeSale getInstance(){
		if (bizCustomerVolumeSale == null){
			bizCustomerVolumeSale = new BizCustomerVolumeSale();
		}
		return bizCustomerVolumeSale;
	}
	
	/**
	 * 키워드로 검색한 거래처별 체적장부 - 공급(판매)내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerVolumeSales
	 */
	public CustomerVolumeSaleMap getCustomerVolumeSales(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerVolumeSales(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 체적장부 - 공급(판매)내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerVolumeSaleMap 형식의 거래처별 체적장부 - 공급(판매)내역 목록 반환
	 */
	public CustomerVolumeSaleMap selectCustomerVolumeSales(String serverIp, String catalogName, Map<String, String> condition){
		CustomerVolumeSaleMap customerVolumeSales = new CustomerVolumeSaleMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_VOLUME_SALE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerVolumeSale customerVolumeSale = convertCustomerVolumeSale(map);
			customerVolumeSales.setCustomerVolumeSale(customerVolumeSale.getKeyValue(), customerVolumeSale);
		}
		return customerVolumeSales;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param sequenceNumber
	 * @return
	 */
	public String insertCustomerVolumeSale(
	  	  String serverIp
		, String catalogName
		, String areaCode 
		, String customerCode
		, String saleDate
		, String buildingName
		, String itemCode
		, String itemName
		, String itemCapacity
		, String salePrice
		, String saleQuantity
		, String withdrawQuantity
		, String saleAmount
		, String employeeCode
		, String employeeName
		, String cubicPrice
		, String readMeterQuantity
		, String remainQuantity
		, String remark
		, String registerDate
		, String userId
		) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("saleDate", saleDate);
		param.put("buildingName", buildingName);
		param.put("itemCode", itemCode);
		param.put("itemName", itemName);
		param.put("itemCapacity", new Integer(itemCapacity));
		param.put("salePrice", new Float(salePrice));
		param.put("saleQuantity", new Integer(saleQuantity));
		param.put("withdrawQuantity", new Integer(withdrawQuantity));
		param.put("saleAmount", new Float(saleAmount));
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("cubicPrice", new Float(cubicPrice));
		param.put("readMeterQuantity", new Integer(readMeterQuantity));
		param.put("remainQuantity", new Integer(remainQuantity));
		param.put("remark", remark);
		param.put("registerDate", registerDate);
		param.put("userId", userId);
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_VOLUME_SALE_INSERT_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param saleDate
	 * @param sequenceNumber
	 * @return
	 */
	public String deleteCustomerVolumeSale(
			  String serverIp
			, String catalogName
			, String areaCode 
			, String customerCode
			, String saleDate
			, String sequenceNumber
			, String transactionDate
			, String userId
			){
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("catalogName", catalogName);
			param.put("areaCode", areaCode);
			param.put("customerCode", customerCode);
			param.put("saleDate", saleDate);
			param.put("sequenceNumber", sequenceNumber);
			param.put("transactionDate", transactionDate);
			param.put("userId", userId);
			
			return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_VOLUME_SALE_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	
	/**
	 * HashMap을 CustomerVolumeSale으로 변환
	 * @param map
	 * @return CustomerVolumeSale
	 */
	protected static CustomerVolumeSale convertCustomerVolumeSale(HashMap<String, String> map){
		CustomerVolumeSale customerVolumeSale = new CustomerVolumeSale();
		
		customerVolumeSale.setAreaCode(map.get("areaCode"));
		customerVolumeSale.setCustomerCode(map.get("customerCode"));
		customerVolumeSale.setSaleDate(map.get("saleDate"));
		customerVolumeSale.setSequenceNumber(map.get("sequenceNumber"));
		customerVolumeSale.setItemCode(map.get("itemCode"));
		customerVolumeSale.setItemName(map.get("itemName"));
		customerVolumeSale.setSaleQuantity(map.get("saleQuantity"));
		customerVolumeSale.setWithrawQuantity(map.get("withrawQuantity"));
		customerVolumeSale.setSupplyQuantity(map.get("supplyQuantity"));
		customerVolumeSale.setEmployeeCode(map.get("employeeCode"));
		customerVolumeSale.setEmployeeName(map.get("employeeName"));
		customerVolumeSale.setEquipmentInOutSequenceNumber(map.get("equipmentInOutSequenceNumber"));
		
		return customerVolumeSale;
	}
	
	/**
	 * CustomerVolumeSale을 HashMap으로 변환
	 * @param customerVolumeSale
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerVolumeSale(CustomerVolumeSale customerVolumeSale){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("areaCode", customerVolumeSale.getAreaCode());
		map.put("customerCode", customerVolumeSale.getCustomerCode());
		map.put("saleDate", customerVolumeSale.getSaleDate());
		map.put("sequenceNumber", customerVolumeSale.getSequenceNumber());
		map.put("itemCode", customerVolumeSale.getItemCode());
		map.put("itemName", customerVolumeSale.getItemName());
		map.put("saleQuantity", customerVolumeSale.getSaleQuantity());
		map.put("withrawQuantity", customerVolumeSale.getWithrawQuantity());
		map.put("supplyQuantity", customerVolumeSale.getSupplyQuantity());
		map.put("employeeCode", customerVolumeSale.getEmployeeCode());
		map.put("employeeName", customerVolumeSale.getEmployeeName());
		map.put("equipmentInOutSequenceNumber", customerVolumeSale.getEquipmentInOutSequenceNumber());
		
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
		String customerCode = "000-00030";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
		String startDate = "20090701";
		String endDate = "20120731";
		
		try{
			CustomerVolumeSaleMap customerVolumeSales = BizCustomerVolumeSale.getInstance().getCustomerVolumeSales(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			System.out.println(customerVolumeSales.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerVolumeSale customerVolumeSale = new CustomerVolumeSale();
//		customerVolumeSale.setCustomerVolumeSaleCode("TEST1");
//		customerVolumeSale.setCustomerVolumeSaleName("TEST CustomerVolumeSale1");
//		customerVolumeSale.setUseYesNo("Y");
//		BizCustomerVolumeSale.getInstance().applyCustomerVolumeSale(customerVolumeSale);
		
/* DELETE */
//		BizCustomerVolumeSale.getInstance().deleteCustomerVolumeSale("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerVolumeSale.getInstance().deleteCustomerVolumeSales(list);

/* SELECT */
//		BizCustomerVolumeSale.getInstance().initCacheCustomerVolumeSales();
//		System.out.println(cacheCustomerVolumeSales.toXML());
//		

//		System.out.println(cacheCustomerVolumeSales.toXML());
	}
}
