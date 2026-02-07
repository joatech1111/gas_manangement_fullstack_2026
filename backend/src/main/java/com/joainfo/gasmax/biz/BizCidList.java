package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcIOException;
import com.joainfo.common.util.jdbc.JdbcProcedureException;
import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CidList;
import com.joainfo.gasmax.bean.list.CidListMap;

/**
 * BizCidList
 * CID 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCidList {

	/**
	 * CID Select 쿼리의 ID
	 */
	public final String GASMAX_CID_LIST_SELECT_ID = "GASMAX.CidList.Select";
	
	/**
	 * 통화구분 Select 쿼리의 ID
	 */
	public final String GASMAX_CID_LIST_SELECT_SALE_TYPE_ID = "GASMAX.CidList.Select.SaleType";
	
	/**
	 * CID Insert 쿼리의 ID
	 */
	public final String GASMAX_CID_LIST_INSERT_ID = "GASMAX.CidList.Insert";
	
	/**
	 * CID Update 쿼리의 ID
	 */
	public final String GASMAX_CID_LIST_UPDATE_ID = "GASMAX.CidList.Update";
	
	/**
	 * CID Delete 쿼리의 ID
	 */
	public final String GASMAX_CID_LIST_DELETE_ID = "GASMAX.CidList.Delete";
	
	/**
	 * BizCidList 인스턴스
	 */
	private static BizCidList bizCidList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCidList(){
	}
	
	/**
	 * Singleton으로 BizCidList 인스턴스 생성
	 * @return bizCidList
	 */
	public static BizCidList getInstance(){
		if (bizCidList == null){
			bizCidList = new BizCidList();
		}
		return bizCidList;
	}
	
	/**
	 * 키워드로 검색한 CID 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param cidDate CID Date
	 * @param newDelivery
	 * @param delivered
	 * @param deliveryComplete
	 * @param employeeCode 사원 코드
	 * @return cidLists
	 * @return
	 */
	public CidListMap getCidLists(String serverIp, String catalogName, String areaCode, String cidDate, String employeeCode, String newDelivery, String delivered, String deliveryComplete){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("cidDate", cidDate);
		condition.put("employeeCode", employeeCode==null?"":employeeCode);
		if ("NA".equals(employeeCode)) {
			condition.put("employeeCode", "");
			condition.put("noEmployee", employeeCode);
		}
		boolean newBool = new Boolean(newDelivery).booleanValue();
		boolean deliveredBool = new Boolean(delivered).booleanValue();
		boolean deliveryCompleteBool = new Boolean(deliveryComplete).booleanValue();
		if (newBool && !deliveredBool && !deliveryCompleteBool){
			condition.put("onlyNew", "true");
		} else if (!newBool && deliveredBool && !deliveryCompleteBool){
			condition.put("onlyDelivered", "true");
		} else if (!newBool && !deliveredBool && deliveryCompleteBool){
			condition.put("onlyDeliveryComplete", "true");
		} else if (newBool && deliveredBool && !deliveryCompleteBool){
			condition.put("newAndDeilvered", "true");
		} else if (!newBool && deliveredBool && deliveryCompleteBool){
			condition.put("DeilveredAndDeliveryComplete", "true");
		}
		return selectCidLists(serverIp, catalogName, condition);
	}
	
	/**
	 * CID 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CidListMap 형식의 CID 목록 반환
	 */
	public CidListMap selectCidLists(String serverIp, String catalogName, Map<String, String> condition){
		CidListMap cidLists = new CidListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CID_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CidList cidList = convertCidList(map);
			cidLists.setCidList(cidList.getKeyValue(), cidList);
		}
		return cidLists;
	}
	

	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param cidDate
	 * @param saleType
	 * @param customerCode
	 * @param customerName
	 * @param phoneNumber
	 * @param itemCode
	 * @param itemName
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
	 * @param deliveryYesNo
	 * @param completeYesNo
	 * @param collectType
	 * @param registerDate
	 * @param mobileNumber
	 * @param gasType
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public String insertCidList(
			    String serverIp
			  , String catalogName 
			  , String areaCode
			  , String cidDate
			  , String saleType
			  , String customerCode
			  , String customerName
			  , String phoneNumber
			  , String itemCode
			  , String itemName
			  , String saleQuantity
			  , String withdrawQuantity
			  , String salePrice
			  , String priceType
			  , String vatType
			  , String saleAmount
			  , String taxAmount
			  , String totalAmount
			  , String discountAmount
			  , String collectAmount
			  , String unpaidAmount
			  , String employeeCode
			  , String employeeName
			  , String remark
			  , String deliveryYesNo
			  , String completeYesNo
			  , String collectType
			  , String registerDate
			  , String mobileNumber
			  , String gasType
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("areaCode", areaCode);
		param.put("cidDate", cidDate);
		param.put("saleType", saleType);
		param.put("customerCode", customerCode);
		param.put("customerName", customerName);
		param.put("phoneNumber", phoneNumber);
		param.put("itemCode", itemCode);
		param.put("itemName", itemName);
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
		param.put("deliveryYesNo", new Boolean(deliveryYesNo)); //"true": true else false
		param.put("completeYesNo", new Boolean(completeYesNo)); //"true": true else false
		param.put("collectType", collectType);
		param.put("registerDate", registerDate);
		param.put("mobileNumber", mobileNumber);
		param.put("gasType", gasType);
		
		return JdbcUtil.getInstance(serverIp).executeProcedureByIntegerReturn(GASMAX_CID_LIST_INSERT_ID, param, "sequenceNumber"); // 에러 코드를 반환한다.
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param sequenceNumber
	 * @param cidDate
	 * @param saleType
	 * @param customerCode
	 * @param customerName
	 * @param phoneNumber
	 * @param itemCode
	 * @param itemName
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
	 * @param deliveryYesNo
	 * @param completeYesNo
	 * @param collectType
	 * @param registerDate
	 * @param mobileNumber
	 * @param gasType
	 * @return
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public String updateCidList(
		    String serverIp
		  , String catalogName 
		  , String areaCode
		  , String sequenceNumber
		  , String cidDate
		  , String saleType
		  , String customerCode
		  , String customerName
		  , String phoneNumber
		  , String itemCode
		  , String itemName
		  , String saleQuantity
		  , String withdrawQuantity
		  , String salePrice
		  , String priceType
		  , String vatType
		  , String saleAmount
		  , String taxAmount
		  , String totalAmount
		  , String discountAmount
		  , String collectAmount
		  , String unpaidAmount
		  , String employeeCode
		  , String employeeName
		  , String remark
		  , String deliveryYesNo
		  , String completeYesNo
		  , String collectType
		  , String registerDate
		  , String mobileNumber
		  , String gasType
		) throws JdbcIOException, JdbcProcedureException{
	HashMap<String, Object> param = new HashMap<String, Object>();
	param.put("areaCode", areaCode);
	param.put("sequenceNumber", sequenceNumber);
	param.put("cidDate", cidDate);
	param.put("saleType", saleType);
	param.put("customerCode", customerCode);
	param.put("customerName", customerName);
	param.put("phoneNumber", phoneNumber);
	param.put("itemCode", itemCode);
	param.put("itemName", itemName);
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
	param.put("deliveryYesNo", new Boolean(deliveryYesNo)); //"true": true else false
	param.put("completeYesNo", new Boolean(completeYesNo)); //"true": true else false
	param.put("collectType", collectType);
	param.put("registerDate", registerDate);
	param.put("mobileNumber", mobileNumber);
	param.put("gasType", gasType);
	
	return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CID_LIST_UPDATE_ID, param, "outputMessage"); // 에러 코드를 반환한다.
}

	/**
	 * 통화구분코드 목록 조회
	 * @param catalogName
	 * @return 
	 */
	public LinkedHashMap<String, String> selectSaleTypes(String serverIp, String catalogName){
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CID_LIST_SELECT_SALE_TYPE_ID, condition);
		for( HashMap<String, String> map :  list) {
			result.put(map.get("saleTypeCode"), map.get("saleTypeName"));
		}
		return result;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param sequenceNumber
	 * @param cidDate
	 * @return
	 */
	public String deleteCidList(
		    String serverIp
		  , String catalogName 
		  , String areaCode
		  , String sequenceNumber
		  , String cidDate
			){
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("areaCode", areaCode);
		param.put("sequenceNumber", sequenceNumber);
		param.put("cidDate", cidDate);
		
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_CID_LIST_DELETE_ID, param, "outputMessage"); // 에러 코드를 반환한다.

	}
	
	/**
	 * HashMap을 CidList으로 변환
	 * @param map
	 * @return CidList
	 */
	protected static CidList convertCidList(HashMap<String, String> map){
		CidList cidList = new CidList();
		
		cidList.setAreaCode(map.get("areaCode"));
		cidList.setCidDate(map.get("cidDate"));
		cidList.setSequenceNumber(map.get("sequenceNumber"));
		cidList.setCidTime(map.get("cidTime"));
		cidList.setAutoRegisterType(map.get("autoRegisterType"));
		cidList.setPhoneNumber(map.get("phoneNumber"));
		cidList.setSaleTypeCode(map.get("saleTypeCode"));
		cidList.setSaleTypeName(map.get("saleTypeName"));
		cidList.setCustomerCode(map.get("customerCode"));
		cidList.setCustomerName(map.get("customerName"));
		cidList.setItemCode(map.get("itemCode"));
		cidList.setItemName(map.get("itemName"));
		cidList.setSaleQuantity(map.get("saleQuantity"));
		cidList.setWithdrawQuantity(map.get("withdrawQuantity"));
		cidList.setCidPrice(map.get("cidPrice"));
		cidList.setVatType(map.get("vatType"));
		cidList.setCidAmount(map.get("cidAmount"));
		cidList.setTaxAmount(map.get("taxAmount"));
		cidList.setTotalAmount(map.get("totalAmount"));
		cidList.setDiscountAmount(map.get("discountAmount"));
		cidList.setCollectAmount(map.get("collectAmount"));
		cidList.setUnpaidAmount(map.get("unpaidAmount"));
		cidList.setEmployeeCode(map.get("employeeCode"));
		cidList.setEmployeeName(map.get("employeeName"));
		cidList.setRemark(map.get("remark"));
		cidList.setDeliveryYesNo(map.get("deliveryYesNo"));
		cidList.setCompleteYesNo(map.get("completeYesNo"));
		cidList.setCollectType(map.get("collectType"));
		cidList.setAddress(map.get("address"));
		cidList.setCustomerType(map.get("customerType"));
		
		return cidList;
	}
	
	/**
	 * CidList을 HashMap으로 변환
	 * @param cidList
	 * @return
	 */
	protected static HashMap<String, String> convertCidList(CidList cidList){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", cidList.getAreaCode());
	    map.put("cidDate", cidList.getCidDate());
	    map.put("sequenceNumber", cidList.getSequenceNumber());
	    map.put("cidTime", cidList.getCidTime());
	    map.put("autoRegisterType", cidList.getAutoRegisterType());
	    map.put("phoneNumber", cidList.getPhoneNumber());
	    map.put("saleTypeCode", cidList.getSaleTypeCode());
	    map.put("saleTypeName", cidList.getSaleTypeName());
	    map.put("customerCode", cidList.getCustomerCode());
	    map.put("customerName", cidList.getCustomerName());
	    map.put("itemCode", cidList.getItemCode());
	    map.put("itemName", cidList.getItemName());
	    map.put("saleQuantity", cidList.getSaleQuantity());
	    map.put("withdrawQuantity", cidList.getWithdrawQuantity());
	    map.put("cidPrice", cidList.getCidPrice());
	    map.put("vatType", cidList.getVatType());
	    map.put("cidAmount", cidList.getCidAmount());
	    map.put("taxAmount", cidList.getTaxAmount());
	    map.put("totalAmount", cidList.getTotalAmount());
	    map.put("discountAmount", cidList.getDiscountAmount());
	    map.put("collectAmount", cidList.getCollectAmount());
	    map.put("unpaidAmount", cidList.getUnpaidAmount());
	    map.put("employeeCode", cidList.getEmployeeCode());
	    map.put("employeeName", cidList.getEmployeeName());
	    map.put("remark", cidList.getRemark());
	    map.put("deliveryYesNo", cidList.getDeliveryYesNo());
	    map.put("completeYesNo", cidList.getCompleteYesNo());
	    map.put("collectType", cidList.getCollectType());
	    map.put("address", cidList.getAddress());
	    map.put("customerType", cidList.getCustomerType());
		
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
//		String customerCode = "'000-00010";
//		String customerType = "A";
		String employeeCode = "NA";
//		String grantCode = "00111111";
//		String cidTypeCode = "0";
//		String startDate = "20100701";
//		String endDate = "20100731";
		String newDelivery = "1";
		String Delivered = "1";
		String deliveryComplete = "1";
		
		try{
			CidListMap cidLists = BizCidList.getInstance().getCidLists(serverIp, catalogName, areaCode, "20101026", employeeCode, newDelivery, Delivered, deliveryComplete);
			System.out.println(cidLists.toXML());
//			System.out.println(BizCidList.getInstance().insertCidList(
//						serverIp,
//						catalogName,
//						areaCode,
//						customerCode,
//						"J",
//						"20120914",
//						"조아빌딩",
//						"사용자",
//						"5000",
//						"500",
//						"0",
//						"01",
//						"사원",
//						"비고",
//						"20120914180904",
//						"01043322115_백원태"
//					)
//			);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CidList cidList = new CidList();
//		cidList.setCidListCode("TEST1");
//		cidList.setCidListName("TEST CidList1");
//		cidList.setUseYesNo("Y");
//		BizCidList.getInstance().applyCidList(cidList);
		
/* DELETE */
//		BizCidList.getInstance().deleteCidList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCidList.getInstance().deleteCidLists(list);

/* SELECT */
//		BizCidList.getInstance().initCacheCidLists();
//		System.out.println(cacheCidLists.toXML());
//		

//		System.out.println(cacheCidLists.toXML());
	}
}
