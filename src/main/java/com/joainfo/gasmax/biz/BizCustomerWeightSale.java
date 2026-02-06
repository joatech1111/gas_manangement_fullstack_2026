package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.common.util.StringUtil;
import com.joainfo.gasmax.bean.CustomerWeightSale;
import com.joainfo.gasmax.bean.list.CustomerWeightSaleMap;

/**
 * 거래처별 체적장부 - 매출내역 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerWeightSale {

	/**
	 * 거래처별 체적장부 - 매출내역 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_SELECT_ID = "GASMAX.CustomerWeightSale.Select";
	
	/**
	 * 거래처별 체적장부 - 매출내역 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_INSERT_ID = "GASMAX.CustomerWeightSale.Insert";
	
	/**
	 * 거래처별 체적장부 - LPG 매출내역 Insert 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_LPG_INSERT_ID = "GASMAX.CustomerWeightSaleLPG.Insert";
	
	/**
	 * 거래처별 체적장부 - 고압 매출내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_HPG_DELETE_ID = "GASMAX.CustomerWeightSaleHPG.Delete";
	
	/**
	 * 거래처별 체적장부 - LPG 매출내역 Delete 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_LPG_DELETE_ID = "GASMAX.CustomerWeightSaleLPG.Delete";
	
	/**
	 * 거래처별 체적장부 - 비고 내역 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_WEIGHT_SALE_SELECT_REMARK_ID = "GASMAX.CustomerWeightSale.Select.Remark";
	
	/**
	 * BizCustomerWeightSale 인스턴스
	 */
	private static BizCustomerWeightSale bizCustomerWeightSale;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerWeightSale(){
	}
	
	/**
	 * Singleton으로 BizCustomerWeightSale 인스턴스 생성
	 * @return bizCustomerWeightSale
	 */
	public static BizCustomerWeightSale getInstance(){
		if (bizCustomerWeightSale == null){
			bizCustomerWeightSale = new BizCustomerWeightSale();
		}
		return bizCustomerWeightSale;
	}
	
	/**
	 * 키워드로 검색한 거래처별 체적장부 - 매출내역 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerWeightSales
	 */
	public CustomerWeightSaleMap getCustomerWeightSales(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerWeightSales(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 체적장부 - 매출내역 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerWeightSaleMap 형식의 거래처별 체적장부 - 매출내역 목록 반환
	 */
	public CustomerWeightSaleMap selectCustomerWeightSales(String serverIp, String catalogName, Map<String, String> condition){
		CustomerWeightSaleMap customerWeightSales = new CustomerWeightSaleMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_WEIGHT_SALE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerWeightSale customerWeightSale = convertCustomerWeightSale(map);
			customerWeightSales.setCustomerWeightSale(customerWeightSale.getKeyValue(), customerWeightSale);
		}
		return customerWeightSales;
	}
	
	/**
	 * 일반 판매(고압용) 품목 목록 가져오기
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public HashMap<String, String> getCustomerWeightSaleRemarks(String serverIp, String catalogName, String areaCode, String customerCode, String saleDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("saleDate", StringUtil.addMonth(saleDate, -2));
		
		return selectCustomerWeightSaleRemarks(serverIp, catalogName, condition);
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param condition
	 * @return
	 */
	public HashMap<String, String> selectCustomerWeightSaleRemarks(String serverIp, String catalogName, Map<String, String> condition){
		HashMap<String, String> remarks = new HashMap<String, String>();
		condition.put("catalogName", catalogName);
		
		int i = 0;
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_WEIGHT_SALE_SELECT_REMARK_ID, condition);
		for( HashMap<String, String> map :  list) {
			remarks.put("" + i++, map.get("remark"));
		}
		return remarks;
	}
	
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param saleType
	 * @param saleDate
	 * @param customerName
	 * @param userName
	 * @param itemCode
	 * @param itemName
	 * @param itemSpec
	 * @param saleQuantity
	 * @param withdrawQuantity
	 * @param salePrice
	 * @param priceType
	 * @param vatType
	 * @param saleAmount
	 * @param taxAmount
	 * @param totalAmount
	 * @param discountAmount
	 * @param collectAmount
	 * @param unpaidAmount
	 * @param employeeCode
	 * @param employeeName
	 * @param remark
	 * @param collectType
	 * @param registerDate
	 * @param userId
	 * @return
	 */
	public String insertCustomerWeightSale(
			 String serverIp
			,String catalogName
			,String areaCode
			,String customerCode
			,String saleType
			,String saleDate
			,String customerName
			,String userName
			,String itemCode
			,String itemName
			,String itemSpec
			,String saleQuantity
			,String withdrawQuantity
			,String salePrice
			,String priceType
			,String vatType
			,String saleAmount
			,String taxAmount
			,String totalAmount
			,String discountAmount
			,String collectAmount
			,String unpaidAmount
			,String employeeCode
			,String employeeName
			,String remark
			,String collectType
			,String registerDate
			,String userId
		) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("saleType", saleType);
		param.put("saleDate", saleDate);
		param.put("customerName", customerName);
		param.put("userName", userName);
		param.put("itemCode", itemCode);
		param.put("itemName", itemName);
		param.put("itemSpec", itemSpec);
		param.put("saleQuantity", new Integer(saleQuantity));
		param.put("withdrawQuantity", new Integer(withdrawQuantity));
		param.put("salePrice", new Float(salePrice));
		param.put("priceType", priceType);
		param.put("vatType", vatType);
		param.put("saleAmount", new Float(saleAmount));
		param.put("taxAmount", new Float(taxAmount));
		param.put("totalAmount", new Float(totalAmount));
		param.put("discountAmount", new Float(discountAmount));
		param.put("collectAmount", new Float(collectAmount));
		param.put("unpaidAmount", new Float(unpaidAmount));
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("remark", remark);
		param.put("collectType", collectType);
		param.put("registerDate", registerDate);
		param.put("userId", userId);
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_WEIGHT_SALE_INSERT_ID, param, "outputMessage"); // 에러 코드를 반환한다.

	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param saleType
	 * @param saleDate
	 * @param customerName
	 * @param userName
	 * @param itemCode
	 * @param itemName
	 * @param itemSpec
	 * @param saleQuantity
	 * @param withdrawQuantity
	 * @param salePrice
	 * @param priceType
	 * @param vatType
	 * @param saleAmount
	 * @param taxAmount
	 * @param totalAmount
	 * @param discountAmount
	 * @param collectAmount
	 * @param unpaidAmount
	 * @param employeeCode
	 * @param employeeName
	 * @param remark
	 * @param collectType
	 * @param registerDate
	 * @param userId
	 * @return
	 */
	public String insertCustomerWeightSaleLPG(
			String serverIp
			,String catalogName
			,String areaCode
			,String customerCode
			,String saleType
			,String saleDate
			,String customerName
			,String userName
			,String itemCode
			,String itemName
			,String itemSpec
			,String saleQuantity
			,String withdrawQuantity
			,String salePrice
			,String priceType
			,String vatType
			,String saleAmount
			,String taxAmount
			,String totalAmount
			,String discountAmount
			,String collectAmount
			,String unpaidAmount
			,String employeeCode
			,String employeeName
			,String remark
			,String collectType
			,String registerDate
			,String userId
			) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("saleType", saleType);
		param.put("saleDate", saleDate);
		param.put("customerName", customerName);
		param.put("userName", userName);
		param.put("itemCode", itemCode);
		param.put("itemName", itemName);
		param.put("itemSpec", itemSpec);
		param.put("saleQuantity", new Integer(saleQuantity));
		param.put("withdrawQuantity", new Integer(withdrawQuantity));
		param.put("salePrice", new Float(salePrice));
		param.put("priceType", priceType);
		param.put("vatType", vatType);
		param.put("saleAmount", new Float(saleAmount));
		param.put("taxAmount", new Float(taxAmount));
		param.put("totalAmount", new Float(totalAmount));
		param.put("discountAmount", new Float(discountAmount));
		param.put("collectAmount", new Float(collectAmount));
		param.put("unpaidAmount", new Float(unpaidAmount));
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("remark", remark);
		param.put("collectType", collectType);
		param.put("registerDate", registerDate);
		param.put("userId", userId);
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_WEIGHT_SALE_LPG_INSERT_ID, param, "outputMessage"); // 에러 코드를 반환한다.
		
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param saleType
	 * @param saleDate
	 * @param sequenceNumber
	 * @return
	 */
	public String deleteCustomerWeightSaleHPG(
			  String serverIp
			, String catalogName
			, String areaCode 
			, String customerCode
			, String saleType //0.가스, 1.용기, 2.기구
			, String saleDate
			, String sequenceNumber
			, String transactionDate
			, String userId
			){
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("catalogName", catalogName);
			param.put("areaCode", areaCode);
			param.put("customerCode", customerCode);
			param.put("saleType", saleType);
			param.put("saleDate", saleDate);
			param.put("sequenceNumber", sequenceNumber);
			param.put("transactionDate", transactionDate);
			param.put("userId", userId);
			
			return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_WEIGHT_SALE_HPG_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param saleType
	 * @param saleDate
	 * @param sequenceNumber
	 * @return
	 */
	public String deleteCustomerWeightSaleLPG(
			String serverIp
			, String catalogName
			, String areaCode 
			, String customerCode
			, String saleType //0.가스, 1.용기, 2.기구
			, String saleDate
			, String sequenceNumber
			, String transactionDate
			, String userId
			){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("saleType", saleType);
		param.put("saleDate", saleDate);
		param.put("sequenceNumber", sequenceNumber);
		param.put("transactionDate", transactionDate);
		param.put("userId", userId);
		
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CUSTOMER_WEIGHT_SALE_LPG_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
		
	/**
	 * HashMap을 CustomerWeightSale으로 변환
	 * @param map
	 * @return CustomerWeightSale
	 */
	protected static CustomerWeightSale convertCustomerWeightSale(HashMap<String, String> map){
		CustomerWeightSale customerWeightSale = new CustomerWeightSale();
		
		customerWeightSale.setAreaCode(map.get("areaCode"));
		customerWeightSale.setCustomerCode(map.get("customerCode"));
		customerWeightSale.setSaleType(map.get("saleType"));
		customerWeightSale.setSaleDate(map.get("saleDate"));
		customerWeightSale.setSequenceNumber(map.get("sequenceNumber"));
		customerWeightSale.setItemCode(map.get("itemCode"));
		customerWeightSale.setItemName(map.get("itemName"));
		customerWeightSale.setSaleDisplayOrder(map.get("saleDisplayOrder"));
		customerWeightSale.setItemDisplayOrder(map.get("itemDisplayOrder"));
		customerWeightSale.setSaleQuantity(map.get("saleQuantity"));
		customerWeightSale.setWithdrawQuantity(map.get("withdrawQuantity"));
		customerWeightSale.setSupplyAmount(map.get("supplyAmount"));
		customerWeightSale.setTaxAmount(map.get("taxAmount"));
		customerWeightSale.setSumAmount(map.get("sumAmount"));
		
		return customerWeightSale;
	}
	
	/**
	 * CustomerWeightSale을 HashMap으로 변환
	 * @param customerWeightSale
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerWeightSale(CustomerWeightSale customerWeightSale){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerWeightSale.getAreaCode());
	    map.put("customerCode", customerWeightSale.getCustomerCode());
	    map.put("saleType", customerWeightSale.getSaleType());
	    map.put("saleDate", customerWeightSale.getSaleDate());
	    map.put("sequenceNumber", customerWeightSale.getSequenceNumber());
	    map.put("itemCode", customerWeightSale.getItemCode());
	    map.put("itemName", customerWeightSale.getItemName());
	    map.put("saleDisplayOrder", customerWeightSale.getSaleDisplayOrder());
	    map.put("itemDisplayOrder", customerWeightSale.getItemDisplayOrder());
	    map.put("saleQuantity", customerWeightSale.getSaleQuantity());
	    map.put("withdrawQuantity", customerWeightSale.getWithdrawQuantity());
	    map.put("supplyAmount", customerWeightSale.getSupplyAmount());
	    map.put("taxAmount", customerWeightSale.getTaxAmount());
	    map.put("sumAmount", customerWeightSale.getSumAmount());
		
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
		String customerCode = "000-00123";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
//		String startDate = "20090701";
//		String endDate = "20120731";
		
		try{
			HashMap<String, String>remarks  = BizCustomerWeightSale.getInstance().getCustomerWeightSaleRemarks(serverIp, catalogName, areaCode, customerCode, "2010-09-10");
			System.out.println(remarks);
//			CustomerWeightSaleMap customerWeightSales = BizCustomerWeightSale.getInstance().getCustomerWeightSales(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
//			System.out.println(customerWeightSales.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerWeightSale customerWeightSale = new CustomerWeightSale();
//		customerWeightSale.setCustomerWeightSaleCode("TEST1");
//		customerWeightSale.setCustomerWeightSaleName("TEST CustomerWeightSale1");
//		customerWeightSale.setUseYesNo("Y");
//		BizCustomerWeightSale.getInstance().applyCustomerWeightSale(customerWeightSale);
		
/* DELETE */
//		BizCustomerWeightSale.getInstance().deleteCustomerWeightSale("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerWeightSale.getInstance().deleteCustomerWeightSales(list);

/* SELECT */
//		BizCustomerWeightSale.getInstance().initCacheCustomerWeightSales();
//		System.out.println(cacheCustomerWeightSales.toXML());
//		

//		System.out.println(cacheCustomerWeightSales.toXML());
	}
}
