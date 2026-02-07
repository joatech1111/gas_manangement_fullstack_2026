package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerItemBalanceHPG;
import com.joainfo.gasmax.bean.list.CustomerItemBalanceHPGMap;

/**
 * 거래처별 품목재고 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerItemBalanceHPG {

	/**
	 * 거래처별 고압 품목재고 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_BALANCE_HPG_SELECT_ID = "GASMAX.CustomerItemBalanceHPG.Select";
	
	/**
	 * 거래처별 LPG 품목재고 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_BALANCE_LPG_SELECT_ID = "GASMAX.CustomerItemBalanceLPG.Select";
	
	/**
	 * BizCustomerItemBalanceHPG 인스턴스
	 */
	private static BizCustomerItemBalanceHPG bizCustomerItemBalanceHPG;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerItemBalanceHPG(){
	}
	
	/**
	 * Singleton으로 BizCustomerItemBalanceHPG 인스턴스 생성
	 * @return bizCustomerItemBalanceHPG
	 */
	public static BizCustomerItemBalanceHPG getInstance(){
		if (bizCustomerItemBalanceHPG == null){
			bizCustomerItemBalanceHPG = new BizCustomerItemBalanceHPG();
		}
		return bizCustomerItemBalanceHPG;
	}
	
	/**
	 * 키워드로 검색한 거래처별 고압 품목재고 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerItemBalanceHPGs
	 */
	public CustomerItemBalanceHPGMap getCustomerItemBalanceHPGs(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerItemBalanceHPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 고압 품목재고 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemBalanceHPGMap 형식의 거래처별 품목재고 목록 반환
	 */
	public CustomerItemBalanceHPGMap selectCustomerItemBalanceHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemBalanceHPGMap customerItemBalanceHPGs = new CustomerItemBalanceHPGMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_BALANCE_HPG_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItemBalanceHPG customerItemBalanceHPG = convertCustomerItemBalanceHPG(map);
			customerItemBalanceHPGs.setCustomerItemBalanceHPG(customerItemBalanceHPG.getKeyValue(), customerItemBalanceHPG);
		}
		return customerItemBalanceHPGs;
	}
	
	/**
	 * 키워드로 검색한 LPG 거래처별 품목재고 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerItemBalanceHPGs
	 */
	public CustomerItemBalanceHPGMap getCustomerItemBalanceLPGs(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerItemBalanceLPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 LPG 품목재고 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemBalanceHPGMap 형식의 거래처별 품목재고 목록 반환
	 */
	public CustomerItemBalanceHPGMap selectCustomerItemBalanceLPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemBalanceHPGMap customerItemBalanceLPGs = new CustomerItemBalanceHPGMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_BALANCE_LPG_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItemBalanceHPG customerItemBalanceLPG = convertCustomerItemBalanceHPG(map);
			customerItemBalanceLPGs.setCustomerItemBalanceHPG(customerItemBalanceLPG.getKeyValue(), customerItemBalanceLPG);
		}
		return customerItemBalanceLPGs;
	}
	
	/**
	 * HashMap을 CustomerItemBalanceHPG으로 변환
	 * @param map
	 * @return CustomerItemBalanceHPG
	 */
	protected static CustomerItemBalanceHPG convertCustomerItemBalanceHPG(HashMap<String, String> map){
		CustomerItemBalanceHPG customerItemBalanceHPG = new CustomerItemBalanceHPG();
		
		customerItemBalanceHPG.setAreaCode(map.get("areaCode"));
		customerItemBalanceHPG.setCustomerCode(map.get("customerCode"));
		customerItemBalanceHPG.setSortOrder(map.get("sortOrder"));
		customerItemBalanceHPG.setItemCode(map.get("itemCode"));
		customerItemBalanceHPG.setItemName(map.get("itemName"));
		customerItemBalanceHPG.setCurrentBalance(map.get("currentBalance"));
		customerItemBalanceHPG.setPreBalance(map.get("preBalance"));
		customerItemBalanceHPG.setItemOutput(map.get("itemOutput"));
		customerItemBalanceHPG.setItemInput(map.get("itemInput"));
		customerItemBalanceHPG.setBalance(map.get("balance"));
		customerItemBalanceHPG.setSalePrice(map.get("salePrice"));
		
		return customerItemBalanceHPG;
	}
	
	/**
	 * CustomerItemBalanceHPG을 HashMap으로 변환
	 * @param customerItemBalanceHPG
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerItemBalanceHPG(CustomerItemBalanceHPG customerItemBalanceHPG){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerItemBalanceHPG.getAreaCode());
	    map.put("customerCode", customerItemBalanceHPG.getCustomerCode());
	    map.put("sortOrder", customerItemBalanceHPG.getSortOrder());
	    map.put("itemCode", customerItemBalanceHPG.getItemCode());
	    map.put("itemName", customerItemBalanceHPG.getItemName());
	    map.put("currentBalance", customerItemBalanceHPG.getCurrentBalance());
	    map.put("preBalance", customerItemBalanceHPG.getPreBalance());
	    map.put("itemOutput", customerItemBalanceHPG.getItemOutput());
	    map.put("itemInput", customerItemBalanceHPG.getItemInput());
	    map.put("balance", customerItemBalanceHPG.getBalance());
	    map.put("salePrice", customerItemBalanceHPG.getSalePrice());
	    
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
			CustomerItemBalanceHPGMap customerItemBalanceHPGs = BizCustomerItemBalanceHPG.getInstance().getCustomerItemBalanceHPGs(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			System.out.println(customerItemBalanceHPGs.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerItemBalanceHPG customerItemBalanceHPG = new CustomerItemBalanceHPG();
//		customerItemBalanceHPG.setCustomerItemBalanceHPGCode("TEST1");
//		customerItemBalanceHPG.setCustomerItemBalanceHPGName("TEST CustomerItemBalanceHPG1");
//		customerItemBalanceHPG.setUseYesNo("Y");
//		BizCustomerItemBalanceHPG.getInstance().applyCustomerItemBalanceHPG(customerItemBalanceHPG);
		
/* DELETE */
//		BizCustomerItemBalanceHPG.getInstance().deleteCustomerItemBalanceHPG("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerItemBalanceHPG.getInstance().deleteCustomerItemBalanceHPGs(list);

/* SELECT */
//		BizCustomerItemBalanceHPG.getInstance().initCacheCustomerItemBalanceHPGs();
//		System.out.println(cacheCustomerItemBalanceHPGs.toXML());
//		

//		System.out.println(cacheCustomerItemBalanceHPGs.toXML());
	}
}
