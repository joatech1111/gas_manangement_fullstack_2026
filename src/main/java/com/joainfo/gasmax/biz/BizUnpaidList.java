package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.UnpaidList;
import com.joainfo.gasmax.bean.list.UnpaidListMap;

/**
 * BizUnpaidList
 * 미수현황 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizUnpaidList {

	/**
	 * 미수현황 Select 쿼리의 ID
	 */
	public final String GASMAX_UNPAID_LIST_SELECT_ID = "GASMAX.UnpaidList.Select";
	
	/**
	 * BizUnpaidList 인스턴스
	 */
	private static BizUnpaidList bizUnpaidList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizUnpaidList(){
	}
	
	/**
	 * Singleton으로 BizUnpaidList 인스턴스 생성
	 * @return bizUnpaidList
	 */
	public static BizUnpaidList getInstance(){
		if (bizUnpaidList == null){
			bizUnpaidList = new BizUnpaidList();
		}
		return bizUnpaidList;
	}
	
	/**
	 * 키워드로 검색한 미수현황 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param employeeCode 사원 코드
	 * @param collectTypeCode 수금 유형 코드
	 * @param keyword 검색어
	 * @param weightUnpaidType 일반(중량)미수 검색
	 * @param volumeUnpaidType 체적 미수 검색
	 * @param noEmployee 담당자 미지정 검색
	 * @return unpaidLists
	 */
	public UnpaidListMap getUnpaidLists(String serverIp, String catalogName, String areaCode, String employeeCode, String collectTypeCode, String keyword, String weightUnpaidType, String volumeUnpaidType){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		if ("noEmployee".equals(employeeCode)){
			condition.put("employeeCode", "");
			condition.put("noEmployee", employeeCode);
		} else {
			condition.put("employeeCode", employeeCode==null?"":employeeCode);
		}
		condition.put("collectTypeCode", collectTypeCode==null?"":collectTypeCode);
		condition.put("collectTypeCode", collectTypeCode==null?"":collectTypeCode);
		condition.put("keyword", keyword==null?"":keyword);
		condition.put("weightUnpaidType", weightUnpaidType);
		condition.put("volumeUnpaidType", volumeUnpaidType);
		return selectUnpaidLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 미수현황 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return UnpaidListMap 형식의 미수현황 목록 반환
	 */
	public UnpaidListMap selectUnpaidLists(String serverIp, String catalogName, Map<String, String> condition){
		UnpaidListMap unpaidLists = new UnpaidListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_UNPAID_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			UnpaidList unpaidList = convertUnpaidList(map);
			unpaidLists.setUnpaidList(unpaidList.getKeyValue(), unpaidList);
		}
		return unpaidLists;
	}
	/**
	 * HashMap을 UnpaidList으로 변환
	 * @param map
	 * @return UnpaidList
	 */
	protected static UnpaidList convertUnpaidList(HashMap<String, String> map){
		UnpaidList unpaidList = new UnpaidList();
		
		unpaidList.setAreaCode(map.get("areaCode"));
		unpaidList.setCustomerCode(map.get("customerCode"));
		unpaidList.setUnpaidTypeName(map.get("unpaidTypeName"));
		unpaidList.setCustomerName(map.get("customerName"));
		unpaidList.setAddress1(map.get("address1"));
		unpaidList.setAddress2(map.get("address2"));
		unpaidList.setEmployeeCode(map.get("employeeCode"));
		unpaidList.setEmployeeName(map.get("employeeName"));
		unpaidList.setPhoneNumber(map.get("phoneNumber"));
		unpaidList.setMobileNumber(map.get("mobileNumber"));
		unpaidList.setWeightUnpaid(map.get("weightUnpaid"));
		unpaidList.setVolumeUnpaid(map.get("volumeUnpaid"));
		unpaidList.setCollectTypeCode(map.get("collectTypeCode"));
		unpaidList.setCollectTypeName(map.get("collectTypeName"));
		
		return unpaidList;
	}
	
	/**
	 * UnpaidList을 HashMap으로 변환
	 * @param unpaidList
	 * @return
	 */
	protected static HashMap<String, String> convertUnpaidList(UnpaidList unpaidList){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("areaCode", unpaidList.getAreaCode());
		map.put("customerCode", unpaidList.getCustomerCode());
		map.put("unpaidTypeName", unpaidList.getUnpaidTypeName());
		map.put("customerName", unpaidList.getCustomerName());
		map.put("address1", unpaidList.getAddress1());
		map.put("address2", unpaidList.getAddress2());
		map.put("employeeCode", unpaidList.getEmployeeCode());
		map.put("employeeName", unpaidList.getEmployeeName());
		map.put("phoneNumber", unpaidList.getPhoneNumber());
		map.put("mobileNumber", unpaidList.getMobileNumber());
		map.put("weightUnpaid", unpaidList.getWeightUnpaid());
		map.put("volumeUnpaid", unpaidList.getVolumeUnpaid());
		map.put("collectTypeCode", unpaidList.getCollectTypeCode());
		map.put("collectTypeName", unpaidList.getCollectTypeName());
		
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
		String weightUnpaidType = "";
		String volumeUnpaidType = "";
		
		try{
			UnpaidListMap unpaidLists = BizUnpaidList.getInstance().getUnpaidLists(serverIp, catalogName, areaCode, employeeCode, collectTypeCode, keyword, weightUnpaidType, volumeUnpaidType);
			System.out.println(unpaidLists.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		UnpaidList unpaidList = new UnpaidList();
//		unpaidList.setUnpaidListCode("TEST1");
//		unpaidList.setUnpaidListName("TEST UnpaidList1");
//		unpaidList.setUseYesNo("Y");
//		BizUnpaidList.getInstance().applyUnpaidList(unpaidList);
		
/* DELETE */
//		BizUnpaidList.getInstance().deleteUnpaidList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizUnpaidList.getInstance().deleteUnpaidLists(list);

/* SELECT */
//		BizUnpaidList.getInstance().initCacheUnpaidLists();
//		System.out.println(cacheUnpaidLists.toXML());
//		

//		System.out.println(cacheUnpaidLists.toXML());
	}
}
