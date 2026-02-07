package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerWeightCollect;
import com.joainfo.gasmax.bean.list.CustomerWeightCollectMap;

/**
 * 거래처별 일반장부 - 거래내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerWeightCollect {

	/**
	 * 거래처별 일반장부 - 거래내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_COLLECT_SELECT_ID = "GASMAX.CustomerWeightCollect.Select";
	
	
	/**
	 * 거래처별 장부 - 거래내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_COLLECT_DELETE_ID = "GASMAX.CustomerCollect.Delete";
	
	/**
	 * 거래처별 일반장부 - 거래내역 이월잔액 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_COLLECT_CARRIED_OVER_AMOUNT_SELECT_ID = "GASMAX.CustomerWeightCollect.SelectCarriedOverAmount";
	
	/**
	 * BizCustomerWeightCollect 인스턴스
	 */
	private static BizCustomerWeightCollect bizCustomerWeightCollect;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerWeightCollect(){
	}
	
	/**
	 * Singleton으로 BizCustomerWeightCollect 인스턴스 생성
	 * @return bizCustomerWeightCollect
	 */
	public static BizCustomerWeightCollect getInstance(){
		if (bizCustomerWeightCollect == null){
			bizCustomerWeightCollect = new BizCustomerWeightCollect();
		}
		return bizCustomerWeightCollect;
	}
	
	/**
	 * 키워드로 검색한 거래처별 일반장부 - 거래내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerWeightCollects
	 */
	public CustomerWeightCollectMap getCustomerWeightCollects(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerWeightCollects(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 일반장부 - 거래내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerWeightCollectMap 형식의 거래처별 일반장부 - 거래내역 목록 반환
	 */
	public CustomerWeightCollectMap selectCustomerWeightCollects(String serverIp, String catalogName, Map<String, String> condition){
		CustomerWeightCollectMap customerWeightCollects = new CustomerWeightCollectMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_WEIGHT_COLLECT_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerWeightCollect customerWeightCollect = convertCustomerWeightCollect(map);
			customerWeightCollects.setCustomerWeightCollect(customerWeightCollect.getKeyValue(), customerWeightCollect);
		}
		customerWeightCollects.setCarriedOverAmount(selectCarriedOverAmount(serverIp, condition));
		return customerWeightCollects;
	}
	
	/**
	 * 이월잔액 가져오기
	 * @param serverIp
	 * @param condition
	 * @return
	 */
	public String selectCarriedOverAmount(String serverIp, Map<String, String> condition){
		String carriedOverAmount = "0";
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_WEIGHT_COLLECT_CARRIED_OVER_AMOUNT_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			carriedOverAmount = map.get("carriedOverAmount");
			break;
		}
		return carriedOverAmount;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param collectDate
	 * @param sequenceNumber
	 * @return
	 */
	public String deleteCustomerCollect(
			  String serverIp
			, String catalogName
			, String areaCode 
			, String customerCode
			, String collectType //J:일반 C:체적
			, String collectDate
			, String sequenceNumber
			, String transactionDate
			, String userId
			){
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("catalogName", catalogName);
			param.put("areaCode", areaCode);
			param.put("customerCode", customerCode);
			param.put("collectType", collectType);
			param.put("collectDate", collectDate);
			param.put("sequenceNumber", sequenceNumber);
			param.put("transactionDate", transactionDate);
			param.put("userId", userId);
			
			return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_COLLECT_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	
	/**
	 * HashMap을 CustomerWeightCollect으로 변환
	 * @param map
	 * @return CustomerWeightCollect
	 */
	protected static CustomerWeightCollect convertCustomerWeightCollect(HashMap<String, String> map){
		CustomerWeightCollect customerWeightCollect = new CustomerWeightCollect();
		
		customerWeightCollect.setAreaCode(map.get("areaCode"));
		customerWeightCollect.setCustomerCode(map.get("customerCode"));
		customerWeightCollect.setTypeCode(map.get("typeCode"));
		customerWeightCollect.setCollectDate(map.get("collectDate"));
		customerWeightCollect.setSequenceNumber(map.get("sequenceNumber"));
		customerWeightCollect.setItemCode(map.get("itemCode"));
		customerWeightCollect.setItemName(map.get("itemName"));
		customerWeightCollect.setSaleQuantity(map.get("saleQuantity"));
		customerWeightCollect.setWithdrawQuantity(map.get("withdrawQuantity"));
		customerWeightCollect.setPrice(map.get("price"));
		customerWeightCollect.setAmount(map.get("amount"));
		customerWeightCollect.setTax(map.get("tax"));
		customerWeightCollect.setTotalAmount(map.get("totalAmount"));
		customerWeightCollect.setCollectAmount(map.get("collectAmount"));
		customerWeightCollect.setDiscountAmount(map.get("discountAmount"));
		customerWeightCollect.setUnpaidAmount(map.get("unpaidAmount"));
		customerWeightCollect.setEmployeeCode(map.get("employeeCode"));
		customerWeightCollect.setEmployeeName(map.get("employeeName"));
		customerWeightCollect.setRemark(map.get("remark"));
		customerWeightCollect.setCollectType(map.get("collectType"));
		customerWeightCollect.setEquipmentInOutSequenceNumber(map.get("equipmentInOutSequenceNumber"));
		customerWeightCollect.setUnpaidSequenceNumber(map.get("unpaidSequenceNumber"));

		
		return customerWeightCollect;
	}
	
	/**
	 * CustomerWeightCollect을 HashMap으로 변환
	 * @param customerWeightCollect
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerWeightCollect(CustomerWeightCollect customerWeightCollect){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerWeightCollect.getAreaCode());
	    map.put("customerCode", customerWeightCollect.getCustomerCode());
	    map.put("typeCode", customerWeightCollect.getTypeCode());
	    map.put("collectDate", customerWeightCollect.getCollectDate());
	    map.put("sequenceNumber", customerWeightCollect.getSequenceNumber());
	    map.put("itemCode", customerWeightCollect.getItemCode());
	    map.put("itemName", customerWeightCollect.getItemName());
	    map.put("saleQuantity", customerWeightCollect.getSaleQuantity());
	    map.put("withdrawQuantity", customerWeightCollect.getWithdrawQuantity());
	    map.put("price", customerWeightCollect.getPrice());
	    map.put("amount", customerWeightCollect.getAmount());
	    map.put("tax", customerWeightCollect.getTax());
	    map.put("totalAmount", customerWeightCollect.getTotalAmount());
	    map.put("collectAmount", customerWeightCollect.getCollectAmount());
	    map.put("discountAmount", customerWeightCollect.getDiscountAmount());
	    map.put("unpaidAmount", customerWeightCollect.getUnpaidAmount());
	    map.put("employeeCode", customerWeightCollect.getEmployeeCode());
	    map.put("employeeName", customerWeightCollect.getEmployeeName());
	    map.put("remark", customerWeightCollect.getRemark());
	    map.put("collectType", customerWeightCollect.getCollectType());
	    map.put("equipmentInOutSequenceNumber", customerWeightCollect.getEquipmentInOutSequenceNumber());
	    map.put("unpaidSequenceNumber", customerWeightCollect.getUnpaidSequenceNumber());
		
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
			CustomerWeightCollectMap customerWeightCollects = BizCustomerWeightCollect.getInstance().getCustomerWeightCollects(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			System.out.println(customerWeightCollects.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerWeightCollect customerWeightCollect = new CustomerWeightCollect();
//		customerWeightCollect.setCustomerWeightCollectCode("TEST1");
//		customerWeightCollect.setCustomerWeightCollectName("TEST CustomerWeightCollect1");
//		customerWeightCollect.setUseYesNo("Y");
//		BizCustomerWeightCollect.getInstance().applyCustomerWeightCollect(customerWeightCollect);
		
/* DELETE */
//		BizCustomerWeightCollect.getInstance().deleteCustomerWeightCollect("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerWeightCollect.getInstance().deleteCustomerWeightCollects(list);

/* SELECT */
//		BizCustomerWeightCollect.getInstance().initCacheCustomerWeightCollects();
//		System.out.println(cacheCustomerWeightCollects.toXML());
//		

//		System.out.println(cacheCustomerWeightCollects.toXML());
	}
}
