package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerVolumeCollect;
import com.joainfo.gasmax.bean.list.CustomerVolumeCollectMap;

/**
 * 거래처별 체적장부 - 수금내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerVolumeCollect {

	/**
	 * 거래처별 체적장부 - 수금내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_VOLUME_COLLECT_SELECT_ID = "GASMAX.CustomerVolumeCollect.Select";
	
	/**
	 * 거래처별 장부 - 수금내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_COLLECT_DELETE_ID = "GASMAX.CustomerCollect.Delete";
	
	/**
	 * BizCustomerVolumeCollect 인스턴스
	 */
	private static BizCustomerVolumeCollect bizCustomerVolumeCollect;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerVolumeCollect(){
	}
	
	/**
	 * Singleton으로 BizCustomerVolumeCollect 인스턴스 생성
	 * @return bizCustomerVolumeCollect
	 */
	public static BizCustomerVolumeCollect getInstance(){
		if (bizCustomerVolumeCollect == null){
			bizCustomerVolumeCollect = new BizCustomerVolumeCollect();
		}
		return bizCustomerVolumeCollect;
	}
	
	/**
	 * 키워드로 검색한 거래처별 체적장부 - 수금내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerVolumeCollects
	 */
	public CustomerVolumeCollectMap getCustomerVolumeCollects(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerVolumeCollects(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 체적장부 - 수금내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerVolumeCollectMap 형식의 거래처별 체적장부 - 수금내역 목록 반환
	 */
	public CustomerVolumeCollectMap selectCustomerVolumeCollects(String serverIp, String catalogName, Map<String, String> condition){
		CustomerVolumeCollectMap customerVolumeCollects = new CustomerVolumeCollectMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_VOLUME_COLLECT_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerVolumeCollect customerVolumeCollect = convertCustomerVolumeCollect(map);
			customerVolumeCollects.setCustomerVolumeCollect(customerVolumeCollect.getKeyValue(), customerVolumeCollect);
		}
		return customerVolumeCollects;
	}
	/**
	 * HashMap을 CustomerVolumeCollect으로 변환
	 * @param map
	 * @return CustomerVolumeCollect
	 */
	protected static CustomerVolumeCollect convertCustomerVolumeCollect(HashMap<String, String> map){
		CustomerVolumeCollect customerVolumeCollect = new CustomerVolumeCollect();
		
		customerVolumeCollect.setAreaCode(map.get("areaCode"));
		customerVolumeCollect.setCustomerCode(map.get("customerCode"));
		customerVolumeCollect.setCollectDate(map.get("collectDate"));
		customerVolumeCollect.setSequenceNumber(map.get("sequenceNumber"));
		customerVolumeCollect.setCollectTypeCode(map.get("collectTypeCode"));
		customerVolumeCollect.setCollectTypeName(map.get("collectTypeName"));
		customerVolumeCollect.setCollectAmount(map.get("collectAmount"));
		customerVolumeCollect.setDiscountAmount(map.get("discountAmount"));
		customerVolumeCollect.setEmployeeCode(map.get("employeeCode"));
		customerVolumeCollect.setEmployeeName(map.get("employeeName"));
		customerVolumeCollect.setRemark(map.get("remark"));
		
		return customerVolumeCollect;
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
	 * CustomerVolumeCollect을 HashMap으로 변환
	 * @param customerVolumeCollect
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerVolumeCollect(CustomerVolumeCollect customerVolumeCollect){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("areaCode", customerVolumeCollect.getAreaCode());
		map.put("customerCode", customerVolumeCollect.getCustomerCode());
		map.put("collectDate", customerVolumeCollect.getCollectDate());
		map.put("sequenceNumber", customerVolumeCollect.getSequenceNumber());
		map.put("collectTypeCode", customerVolumeCollect.getCollectTypeCode());
		map.put("collectTypeName", customerVolumeCollect.getCollectTypeName());
		map.put("collectAmount", customerVolumeCollect.getCollectAmount());
		map.put("discountAmount", customerVolumeCollect.getDiscountAmount());
		map.put("employeeCode", customerVolumeCollect.getEmployeeCode());
		map.put("employeeName", customerVolumeCollect.getEmployeeName());
		map.put("remark", customerVolumeCollect.getRemark());
		
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
			CustomerVolumeCollectMap customerVolumeCollects = BizCustomerVolumeCollect.getInstance().getCustomerVolumeCollects(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			System.out.println(customerVolumeCollects.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerVolumeCollect customerVolumeCollect = new CustomerVolumeCollect();
//		customerVolumeCollect.setCustomerVolumeCollectCode("TEST1");
//		customerVolumeCollect.setCustomerVolumeCollectName("TEST CustomerVolumeCollect1");
//		customerVolumeCollect.setUseYesNo("Y");
//		BizCustomerVolumeCollect.getInstance().applyCustomerVolumeCollect(customerVolumeCollect);
		
/* DELETE */
//		BizCustomerVolumeCollect.getInstance().deleteCustomerVolumeCollect("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerVolumeCollect.getInstance().deleteCustomerVolumeCollects(list);

/* SELECT */
//		BizCustomerVolumeCollect.getInstance().initCacheCustomerVolumeCollects();
//		System.out.println(cacheCustomerVolumeCollects.toXML());
//		

//		System.out.println(cacheCustomerVolumeCollects.toXML());
	}
}
