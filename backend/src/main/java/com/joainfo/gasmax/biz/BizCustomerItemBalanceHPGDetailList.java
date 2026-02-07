package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerItemBalanceHPGDetailList;
import com.joainfo.gasmax.bean.list.CustomerItemBalanceHPGDetailListMap;

/**
 * 거래처별 품목재고 상세내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerItemBalanceHPGDetailList {

	/**
	 * 거래처별 고압 품목재고 상세내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_BALANCE_HPG_DETAIL_LIST_SELECT_ID = "GASMAX.CustomerItemBalanceHPGDetailList.Select";
	
	/**
	 * 거래처별 LPG 품목재고 상세내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_BALANCE_LPG_DETAIL_LIST_SELECT_ID = "GASMAX.CustomerItemBalanceLPGDetailList.Select";
	
	/**
	 * BizCustomerItemBalanceHPGDetailList 인스턴스
	 */
	private static BizCustomerItemBalanceHPGDetailList bizCustomerItemBalanceHPGDetailList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerItemBalanceHPGDetailList(){
	}
	
	/**
	 * Singleton으로 BizCustomerItemBalanceHPGDetailList 인스턴스 생성
	 * @return bizCustomerItemBalanceHPGDetailList
	 */
	public static BizCustomerItemBalanceHPGDetailList getInstance(){
		if (bizCustomerItemBalanceHPGDetailList == null){
			bizCustomerItemBalanceHPGDetailList = new BizCustomerItemBalanceHPGDetailList();
		}
		return bizCustomerItemBalanceHPGDetailList;
	}
	
	/**
	 * 키워드로 검색한 거래처별 고압 품목재고 상세내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerItemBalanceHPGDetailLists
	 */
	public CustomerItemBalanceHPGDetailListMap getCustomerItemBalanceHPGDetailLists(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate, String itemCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		condition.put("itemCode", itemCode);
		return selectCustomerItemBalanceHPGDetailLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 고압 품목재고 상세내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemBalanceHPGDetailListMap 형식의 거래처별 품목재고 상세내역 목록 반환
	 */
	public CustomerItemBalanceHPGDetailListMap selectCustomerItemBalanceHPGDetailLists(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemBalanceHPGDetailListMap customerItemBalanceHPGDetailLists = new CustomerItemBalanceHPGDetailListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_BALANCE_HPG_DETAIL_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList = convertCustomerItemBalanceHPGDetailList(map);
			customerItemBalanceHPGDetailLists.setCustomerItemBalanceHPGDetailList(customerItemBalanceHPGDetailList.getKeyValue(), customerItemBalanceHPGDetailList);
		}
		return customerItemBalanceHPGDetailLists;
	}
	
	
	/**
	 * 키워드로 검색한 거래처별 LPG 품목재고 상세내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerItemBalanceHPGDetailLists
	 */
	public CustomerItemBalanceHPGDetailListMap getCustomerItemBalanceLPGDetailLists(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate, String itemCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		condition.put("itemCode", itemCode);
		return selectCustomerItemBalanceLPGDetailLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 LPG 품목재고 상세내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemBalanceHPGDetailListMap 형식의 거래처별 품목재고 상세내역 목록 반환
	 */
	public CustomerItemBalanceHPGDetailListMap selectCustomerItemBalanceLPGDetailLists(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemBalanceHPGDetailListMap customerItemBalanceLPGDetailLists = new CustomerItemBalanceHPGDetailListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_BALANCE_LPG_DETAIL_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItemBalanceHPGDetailList customerItemBalanceLPGDetailList = convertCustomerItemBalanceHPGDetailList(map);
			customerItemBalanceLPGDetailLists.setCustomerItemBalanceHPGDetailList(customerItemBalanceLPGDetailList.getKeyValue(), customerItemBalanceLPGDetailList);
		}
		return customerItemBalanceLPGDetailLists;
	}
	
	/**
	 * HashMap을 CustomerItemBalanceHPGDetailList으로 변환
	 * @param map
	 * @return CustomerItemBalanceHPGDetailList
	 */
	protected static CustomerItemBalanceHPGDetailList convertCustomerItemBalanceHPGDetailList(HashMap<String, String> map){
		CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList = new CustomerItemBalanceHPGDetailList();
		
		customerItemBalanceHPGDetailList.setAreaCode(map.get("areaCode"));
		customerItemBalanceHPGDetailList.setCustomerCode(map.get("customerCode"));
		customerItemBalanceHPGDetailList.setIssueDate(map.get("issueDate"));
		customerItemBalanceHPGDetailList.setSequenceNumber(map.get("sequenceNumber"));
		customerItemBalanceHPGDetailList.setMenuType(map.get("menuType"));
		customerItemBalanceHPGDetailList.setIssueType(map.get("issueType"));
		customerItemBalanceHPGDetailList.setEmployeeName(map.get("employeeName"));
		customerItemBalanceHPGDetailList.setItemCode(map.get("itemCode"));
		customerItemBalanceHPGDetailList.setItemName(map.get("itemName"));
		customerItemBalanceHPGDetailList.setOutputQuantity(map.get("outputQuantity"));
		customerItemBalanceHPGDetailList.setInputQuantity(map.get("inputQuantity"));
		customerItemBalanceHPGDetailList.setPlace(map.get("place"));
		
		return customerItemBalanceHPGDetailList;
	}
	
	/**
	 * CustomerItemBalanceHPGDetailList을 HashMap으로 변환
	 * @param customerItemBalanceHPGDetailList
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerItemBalanceHPGDetailList(CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerItemBalanceHPGDetailList.getAreaCode());
	    map.put("customerCode", customerItemBalanceHPGDetailList.getCustomerCode());
	    map.put("issueDate", customerItemBalanceHPGDetailList.getIssueDate());
	    map.put("sequenceNumber", customerItemBalanceHPGDetailList.getSequenceNumber());
	    map.put("menuType", customerItemBalanceHPGDetailList.getMenuType());
	    map.put("issueType", customerItemBalanceHPGDetailList.getIssueType());
	    map.put("employeeName", customerItemBalanceHPGDetailList.getEmployeeName());
	    map.put("itemCode", customerItemBalanceHPGDetailList.getItemCode());
	    map.put("itemName", customerItemBalanceHPGDetailList.getItemName());
	    map.put("outputQuantity", customerItemBalanceHPGDetailList.getOutputQuantity());
	    map.put("inputQuantity", customerItemBalanceHPGDetailList.getInputQuantity());
	    map.put("place", customerItemBalanceHPGDetailList.getPlace());
	    
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
		String customerCode = "000-00003";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
		String startDate = "20090701";
		String endDate = "20120731";
		String itemCode = "22";
		
		try{
			CustomerItemBalanceHPGDetailListMap customerItemBalanceHPGDetailLists = BizCustomerItemBalanceHPGDetailList.getInstance().getCustomerItemBalanceHPGDetailLists(serverIp, catalogName, areaCode, customerCode, startDate, endDate, itemCode);
			System.out.println(customerItemBalanceHPGDetailLists.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList = new CustomerItemBalanceHPGDetailList();
//		customerItemBalanceHPGDetailList.setCustomerItemBalanceHPGDetailListCode("TEST1");
//		customerItemBalanceHPGDetailList.setCustomerItemBalanceHPGDetailListName("TEST CustomerItemBalanceHPGDetailList1");
//		customerItemBalanceHPGDetailList.setUseYesNo("Y");
//		BizCustomerItemBalanceHPGDetailList.getInstance().applyCustomerItemBalanceHPGDetailList(customerItemBalanceHPGDetailList);
		
/* DELETE */
//		BizCustomerItemBalanceHPGDetailList.getInstance().deleteCustomerItemBalanceHPGDetailList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerItemBalanceHPGDetailList.getInstance().deleteCustomerItemBalanceHPGDetailLists(list);

/* SELECT */
//		BizCustomerItemBalanceHPGDetailList.getInstance().initCacheCustomerItemBalanceHPGDetailLists();
//		System.out.println(cacheCustomerItemBalanceHPGDetailLists.toXML());
//		

//		System.out.println(cacheCustomerItemBalanceHPGDetailLists.toXML());
	}
}
