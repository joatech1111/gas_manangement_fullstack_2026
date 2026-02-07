package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerItem;
import com.joainfo.gasmax.bean.list.CustomerItemMap;

/**
 * BizCustomerItem
 * 거래처별 품목 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerItem {

	/**
	 * 거래처별 품목 체적 고압 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_VOLUME_HPG_ID = "GASMAX.CustomerItem.SelectVolumeHPG";
	
	/**
	 * 거래처별 품목 체적 고압 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_VOLUME_ALL_HPG_ID = "GASMAX.CustomerItem.SelectVolumeAllHPG";
	
	/**
	 * 거래처별 품목 일반 고압 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_HPG_ID = "GASMAX.CustomerItem.SelectWeightHPG";
	
	/**
	 * 거래처별 품목 일반 LPG Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_LPG_ID = "GASMAX.CustomerItem.SelectWeightLPG";
	
	/**
	 * 거래처별 품목 모든 일반 고압 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_HPG_ID = "GASMAX.CustomerItem.SelectWeightAllHPG";
	
	/**
	 * 거래처별 품목 모든 일반 기구 고압 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_EQUIPMENT_HPG_ID = "GASMAX.CustomerItem.SelectWeightAllEquipmentHPG";
	
	/**
	 * 거래처별 품목 모든 일반 LPG Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_LPG_ID = "GASMAX.CustomerItem.SelectWeightAllLPG";
	
	/**
	 * BizCustomerItem 인스턴스
	 */
	private static BizCustomerItem bizCustomerItem;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerItem(){
	}
	
	/**
	 * Singleton으로 BizCustomerItem 인스턴스 생성
	 * @return bizCustomerItem
	 */
	public static BizCustomerItem getInstance(){
		if (bizCustomerItem == null){
			bizCustomerItem = new BizCustomerItem();
		}
		return bizCustomerItem;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param itemTypeCode 0.LPG 1.부탄  2.고압
	 * @return
	 */
	public CustomerItemMap getCustomerItemVolumeHPGs(String serverIp, String catalogName, String areaCode, String itemTypeCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("itemTypeCode", itemTypeCode);
		return selectCustomerItemVolumeHPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public CustomerItemMap getCustomerItemVolumeAllHPGs(String serverIp, String catalogName, String areaCode, String customerCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		return selectCustomerItemVolumeAllHPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemVolumeHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_VOLUME_HPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemVolumeAllHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_VOLUME_ALL_HPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param itemCode
	 * @return
	 */
	public CustomerItem getCustomerItemWeightHPG(String serverIp, String catalogName, String areaCode, String customerCode, String itemCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("itemCode", itemCode);
		CustomerItemMap customerItems = selectCustomerItemWeightAllHPGs(serverIp, catalogName, condition);
		if (customerItems == null) {
			return null;
		} else {
			return customerItems.getCustomerItem(itemCode);
		}
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightHPGs(String serverIp, String catalogName, String areaCode, String customerCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		return selectCustomerItemWeightHPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemWeightHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_HPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param itemCode
	 * @return
	 */
	public CustomerItem getCustomerItemWeightLPG(String serverIp, String catalogName, String areaCode, String customerCode, String itemCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("itemCode", itemCode);
		CustomerItemMap customerItems = selectCustomerItemWeightAllLPGs(serverIp, catalogName, condition);
		if (customerItems == null) {
			return null;
		} else {
			return customerItems.getCustomerItem(itemCode);
		}
	}
	

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightLPGs(String serverIp, String catalogName, String areaCode, String customerCode){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		return selectCustomerItemWeightHPGs(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemWeightLPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_LPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllHPGs(String serverIp, String catalogName, String areaCode, String saleType){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("saleType", saleType);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllHPGs(serverIp, catalogName, condition);
		}
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllLPGs(String serverIp, String catalogName, String areaCode, String saleType){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("saleType", saleType);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllLPGs(serverIp, catalogName, condition);
		}
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllHPGs(String serverIp, String catalogName, String areaCode, String saleType, String keyword){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("saleType", saleType);
		condition.put("keyword", keyword);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllHPGs(serverIp, catalogName, condition);
		}
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllHPGs(String serverIp, String catalogName, String areaCode, String customerCode, String saleType, String keyword){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("saleType", saleType);
		condition.put("keyword", keyword);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllHPGs(serverIp, catalogName, condition);
		}
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllLPGs(String serverIp, String catalogName, String areaCode, String saleType, String keyword){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("saleType", saleType);
		condition.put("keyword", keyword);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllLPGs(serverIp, catalogName, condition);
		}
	}
	
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param itemType '0': 가스 '1': 용기 '2': 기구
	 * @return
	 */
	public CustomerItemMap getCustomerItemWeightAllLPGs(String serverIp, String catalogName, String areaCode, String customerCode, String saleType, String keyword){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("saleType", saleType);
		condition.put("keyword", keyword);
		if ("2".equals(saleType)) {
			return selectCustomerItemWeightAllEquipmentHPGs(serverIp, catalogName, condition);
		} else {
			return selectCustomerItemWeightAllLPGs(serverIp, catalogName, condition);
		}
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemWeightAllHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_HPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	/**
	 * 거래처별 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemWeightAllEquipmentHPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_EQUIPMENT_HPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}

	/**
	 * 거래처별 LPG 품목 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerItemMap 형식의 거래처별 품목 목록 반환
	 */
	public CustomerItemMap selectCustomerItemWeightAllLPGs(String serverIp, String catalogName, Map<String, String> condition){
		CustomerItemMap customerItems = new CustomerItemMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_ITEM_SELECT_WEIGHT_ALL_LPG_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerItem customerItem = convertCustomerItem(map);
			customerItems.setCustomerItem(customerItem.getKeyValue(), customerItem);
		}
		return customerItems;
	}
	
	/**
	 * HashMap을 CustomerItem으로 변환
	 * @param map
	 * @return CustomerItem
	 */
	protected static CustomerItem convertCustomerItem(HashMap<String, String> map){
		CustomerItem customerItem = new CustomerItem();
		
		customerItem.setItemCode(map.get("itemCode"));
		customerItem.setItemName(map.get("itemName"));
		customerItem.setItemSpec(map.get("itemSpec"));
		customerItem.setCapacity(map.get("capacity"));
		customerItem.setItemUnit(map.get("itemUnit"));
		customerItem.setSalePrice(map.get("salePrice"));
		customerItem.setItemBalance(map.get("itemBalance"));
		customerItem.setPriceType(map.get("priceType"));
		customerItem.setDiscountAmount(map.get("discountAmount"));
		customerItem.setLastSaleQuantity(map.get("lastSaleQuantity"));
		customerItem.setLastUnpaidAmount(map.get("lastUnpaidAmount"));
		
		return customerItem;
	}
	
	/**
	 * CustomerItem을 HashMap으로 변환
	 * @param customerItem
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerItem(CustomerItem customerItem){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("itemCode", customerItem.getItemCode());
		map.put("itemName", customerItem.getItemName());
		map.put("itemSpec", customerItem.getItemSpec());
		map.put("capacity", customerItem.getCapacity());
		map.put("itemUnit", customerItem.getItemUnit());
		map.put("salePrice", customerItem.getSalePrice());
		map.put("itemBalance", customerItem.getItemBalance());
		map.put("priceType", customerItem.getPriceType());
		map.put("discountAmount", customerItem.getDiscountAmount());
		map.put("lastSaleQuantity", customerItem.getLastSaleQuantity());
		map.put("lastUnpaidAmount", customerItem.getLastUnpaidAmount());

		return map;
	}
	
	/**
	 * 비즈니스 로직 테스트용
	 * @param args
	 */
	public static void main(String[] args){
//		String keyword = "두";
		String serverIp = "joatech2.dyndns.org";
		String catalogName = "GM_TestHigh";
		String areaCode = "01";
		String itemType = "0"; // 0:가스 1:용기 2:기구
//		String customerCode = "000-00030";
//		String itemTypeCode = "0"; //0.LPG   1.부탄  2.고압
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
		try{
			CustomerItemMap customerItems = BizCustomerItem.getInstance().getCustomerItemWeightAllLPGs(serverIp, catalogName, areaCode, itemType, "산");
			System.out.println(customerItems.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerItem customerItem = new CustomerItem();
//		customerItem.setCustomerItemCode("TEST1");
//		customerItem.setCustomerItemName("TEST CustomerItem1");
//		customerItem.setUseYesNo("Y");
//		BizCustomerItem.getInstance().applyCustomerItem(customerItem);
		
/* DELETE */
//		BizCustomerItem.getInstance().deleteCustomerItem("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerItem.getInstance().deleteCustomerItems(list);

/* SELECT */
//		BizCustomerItem.getInstance().initCacheCustomerItems();
//		System.out.println(cacheCustomerItems.toXML());
//		

//		System.out.println(cacheCustomerItems.toXML());
	}
}
