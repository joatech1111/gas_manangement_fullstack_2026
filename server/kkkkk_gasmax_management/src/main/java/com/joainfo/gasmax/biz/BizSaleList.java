package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.SaleList;
import com.joainfo.gasmax.bean.list.SaleListMap;

/**
 * BizSaleList
 * 판매현황 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizSaleList {

	/**
	 * 판매현황 Select 쿼리의 ID
	 */
	public final String GASMAX_SALE_LIST_SELECT_ID = "GASMAX.SaleList.Select";
	
	/**
	 * BizSaleList 인스턴스
	 */
	private static BizSaleList bizSaleList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizSaleList(){
	}
	
	/**
	 * Singleton으로 BizSaleList 인스턴스 생성
	 * @return bizSaleList
	 */
	public static BizSaleList getInstance(){
		if (bizSaleList == null){
			bizSaleList = new BizSaleList();
		}
		return bizSaleList;
	}
	
	/**
	 * 키워드로 검색한 판매현황 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param employeeCode 사원 코드
	 * @param collectTypeCode 입금 유형 코드
	 * @param keyword 검색어
	 * @param noEmployee 담당자 미지정 검색
	 * @return saleLists
	 */
	public SaleListMap getSaleLists(String serverIp, String catalogName, String areaCode, String employeeCode, String saleType, String collectTypeCode, String keyword, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		if ("noEmployee".equals(employeeCode)){
			condition.put("employeeCode", "");
			condition.put("noEmployee", employeeCode);
		} else {
			condition.put("employeeCode", employeeCode==null?"":employeeCode);
		}
		if ("0".equals(collectTypeCode)){ // 입금방법이 현금 일 때는 현금과 현금(영수증) 모두 조회되도록 함.
			condition.put("collectTypeCode_0", collectTypeCode==null?"":collectTypeCode);
		} else {
			condition.put("collectTypeCode", collectTypeCode==null?"":collectTypeCode);
		}
		condition.put("saleType", saleType);
		condition.put("keyword", keyword==null?"":keyword);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectSaleLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 판매현황 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return SaleListMap 형식의 판매현황 목록 반환
	 */
	public SaleListMap selectSaleLists(String serverIp, String catalogName, Map<String, String> condition){
		SaleListMap saleLists = new SaleListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_SALE_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			SaleList saleList = convertSaleList(map);
			saleLists.setSaleList(saleList.getKeyValue(), saleList);
		}
		return saleLists;
	}
	
	/**
	 * HashMap을 SaleList으로 변환
	 * @param map
	 * @return SaleList
	 */
	protected static SaleList convertSaleList(HashMap<String, String> map){
		SaleList saleList = new SaleList();
		
		saleList.setSaleType(map.get("saleType"));
		saleList.setAreaCode(map.get("areaCode"));
		saleList.setSaleDate(map.get("saleDate"));
		saleList.setCustomerCode(map.get("customerCode"));
		saleList.setSequenceNumber(map.get("sequenceNumber"));
		saleList.setCustomerName(map.get("customerName"));
		saleList.setRemark(map.get("remark"));
		saleList.setItemName(map.get("itemName"));
		saleList.setSaleQuantity(map.get("saleQuantity"));
		saleList.setWithdrawQuantity(map.get("withdrawQuantity"));
		saleList.setContainerWithdrawQuantity(map.get("containerWithdrawQuantity"));
		saleList.setVatType(map.get("vatType"));
		saleList.setSalePrice(map.get("salePrice"));
		saleList.setSupplyAmount(map.get("supplyAmount"));
		saleList.setTaxAmount(map.get("taxAmount"));
		saleList.setSaleAmount(map.get("saleAmount"));
		saleList.setDiscountAmount(map.get("discountAmount"));
		saleList.setCollectAmount(map.get("collectAmount"));
		saleList.setUnpaidAmount(map.get("unpaidAmount"));
		saleList.setEmployeeCode(map.get("employeeCode"));
		saleList.setEmployeeName(map.get("employeeName"));
		saleList.setCollectType(map.get("collectType"));
		
		return saleList;
	}
	
	/**
	 * SaleList을 HashMap으로 변환
	 * @param saleList
	 * @return
	 */
	protected static HashMap<String, String> convertSaleList(SaleList saleList){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("saleType", saleList.getSaleType());
		map.put("areaCode", saleList.getAreaCode());
		map.put("sequenceNumber", saleList.getSequenceNumber());
		map.put("saleDate", saleList.getSaleDate());
		map.put("customerCode", saleList.getCustomerCode());
		map.put("customerName", saleList.getCustomerName());
		map.put("remark", saleList.getRemark());
		map.put("itemName", saleList.getItemName());
		map.put("saleQuantity", saleList.getSaleQuantity());
		map.put("withdrawQuantity", saleList.getWithdrawQuantity());
		map.put("containerWithdrawQuantity", saleList.getContainerWithdrawQuantity());
		map.put("vatType", saleList.getVatType());
		map.put("salePrice", saleList.getSalePrice());
		map.put("supplyAmount", saleList.getSupplyAmount());
		map.put("taxAmount", saleList.getTaxAmount());
		map.put("saleAmount", saleList.getSaleAmount());
		map.put("discountAmount", saleList.getDiscountAmount());
		map.put("collectAmount", saleList.getCollectAmount());
		map.put("unpaidAmount", saleList.getUnpaidAmount());
		map.put("employeeCode", saleList.getEmployeeCode());
		map.put("employeeName", saleList.getEmployeeName());
		map.put("collectType", saleList.getCollectType());
		
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
//		String customerCode = "";
//		String customerType = "A";
		String employeeCode = "1";
//		String grantCode = "00111111";
		String collectTypeCode = "0";
		String weightSaleType = "";
		String volumeSaleType = "";
		
		try{
			SaleListMap saleLists = BizSaleList.getInstance().getSaleLists(serverIp, catalogName, areaCode, employeeCode, "일반", collectTypeCode, keyword, weightSaleType, volumeSaleType);
			System.out.println(saleLists.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		SaleList saleList = new SaleList();
//		saleList.setSaleListCode("TEST1");
//		saleList.setSaleListName("TEST SaleList1");
//		saleList.setUseYesNo("Y");
//		BizSaleList.getInstance().applySaleList(saleList);
		
/* DELETE */
//		BizSaleList.getInstance().deleteSaleList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizSaleList.getInstance().deleteSaleLists(list);

/* SELECT */
//		BizSaleList.getInstance().initCacheSaleLists();
//		System.out.println(cacheSaleLists.toXML());
//		

//		System.out.println(cacheSaleLists.toXML());
	}
}
