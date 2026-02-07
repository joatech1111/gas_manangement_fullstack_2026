package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CollectTypeCode;
import com.joainfo.gasmax.bean.list.CollectTypeCodeMap;

/**
 * BizCollectTypeCode
 * 수금유형코드 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCollectTypeCode {

	/**
	 * 입금방법코드-판매현황, 수금현황, 검침현황용 Select 쿼리의 ID
	 */
	public final String GASMAX_COLLECT_TYPE_CODE_SELECT_ID = "GASMAX.CollectTypeCode.Select";
	
	
	/**
	 * 수금유형코드-미수현황용 Select 쿼리의 ID
	 */
	public final String GASMAX_COLLECT_TYPE_CODE_SELECT_UNPAID_ID = "GASMAX.CollectTypeCode.Select.Unpaid";
	
	/**
	 * 일반품목 판매용 수금유형코드 Select 쿼리의 ID
	 */
	public final String GASMAX_COLLECT_TYPE_CODE_WEIGHT_ITEM_SELECT_ID = "GASMAX.CollectTypeCode.Select.WeightItem";
	
	/**
	 * BizCollectTypeCode 인스턴스
	 */
	private static BizCollectTypeCode bizCollectTypeCode;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCollectTypeCode(){
	}
	
	/**
	 * Singleton으로 BizCollectTypeCode 인스턴스 생성
	 * @return bizCollectTypeCode
	 */
	public static BizCollectTypeCode getInstance(){
		if (bizCollectTypeCode == null){
			bizCollectTypeCode = new BizCollectTypeCode();
		}
		return bizCollectTypeCode;
	}
	
	/**
	 * 키워드로 검색한 수금유형코드 목록을 반환 - 판매현황용
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @return collectTypeCodes
	 */
	public CollectTypeCodeMap getCollectTypeCodes(String serverIp, String catalogName){
		HashMap<String, String> condition = new HashMap<String, String>();
		return selectCollectTypeCodes(serverIp, catalogName, condition);
	}
	
	/**
	 * 키워드로 검색한 수금유형코드 목록을 반환 - 미수현황용
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @return collectTypeCodes
	 */
	public CollectTypeCodeMap getCollectUnpaidTypeCodes(String serverIp, String catalogName){
		HashMap<String, String> condition = new HashMap<String, String>();
		return selectCollectUnpaidTypeCodes(serverIp, catalogName, condition);
	}
	
	/**
	 * 수금유형코드 조회 - 판매현황용
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CollectTypeCodeMap 형식의 수금유형코드 목록 반환
	 */
	public CollectTypeCodeMap selectCollectTypeCodes(String serverIp, String catalogName, Map<String, String> condition){
		CollectTypeCodeMap collectTypeCodes = new CollectTypeCodeMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_COLLECT_TYPE_CODE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CollectTypeCode collectTypeCode = convertCollectTypeCode(map);
			collectTypeCodes.setCollectTypeCode(collectTypeCode.getKeyValue(), collectTypeCode);
		}
		return collectTypeCodes;
	}
	
	/**
	 * 수금유형코드 조회 - 미수현황용
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CollectTypeCodeMap 형식의 수금유형코드 목록 반환
	 */
	public CollectTypeCodeMap selectCollectUnpaidTypeCodes(String serverIp, String catalogName, Map<String, String> condition){
		CollectTypeCodeMap collectTypeCodes = new CollectTypeCodeMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_COLLECT_TYPE_CODE_SELECT_UNPAID_ID, condition);
		for( HashMap<String, String> map :  list) {
			CollectTypeCode collectTypeCode = convertCollectTypeCode(map);
			collectTypeCodes.setCollectTypeCode(collectTypeCode.getKeyValue(), collectTypeCode);
		}
		return collectTypeCodes;
	}
	
	/**
	 * 키워드로 검색한 수금유형코드 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @return collectTypeCodes
	 */
	public CollectTypeCodeMap getWeightItemCollectTypeCodes(String serverIp, String catalogName){
		HashMap<String, String> condition = new HashMap<String, String>();
		return selectWeightItemCollectTypeCodes(serverIp, catalogName, condition);
	}
	
	/**
	 * 수금유형코드 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CollectTypeCodeMap 형식의 수금유형코드 목록 반환
	 */
	public CollectTypeCodeMap selectWeightItemCollectTypeCodes(String serverIp, String catalogName, Map<String, String> condition){
		CollectTypeCodeMap collectTypeCodes = new CollectTypeCodeMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_COLLECT_TYPE_CODE_WEIGHT_ITEM_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CollectTypeCode collectTypeCode = convertCollectTypeCode(map);
			collectTypeCodes.setCollectTypeCode(collectTypeCode.getKeyValue(), collectTypeCode);
		}
		return collectTypeCodes;
	}
	
	/**
	 * HashMap을 CollectTypeCode으로 변환
	 * @param map
	 * @return CollectTypeCode
	 */
	protected static CollectTypeCode convertCollectTypeCode(HashMap<String, String> map){
		CollectTypeCode collectTypeCode = new CollectTypeCode();
		
		collectTypeCode.setCollectTypeCode(map.get("collectTypeCode"));
		collectTypeCode.setCollectTypeName(map.get("collectTypeName"));
		
		return collectTypeCode;
	}
	
	/**
	 * CollectTypeCode을 HashMap으로 변환
	 * @param collectTypeCode
	 * @return
	 */
	protected static HashMap<String, String> convertCollectTypeCode(CollectTypeCode collectTypeCode){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("collectTypeCode", collectTypeCode.getCollectTypeCode());
		map.put("collectTypeName", collectTypeCode.getCollectTypeName());

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
//		String areaCode = "01";
//		String customerCode = "";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
		try{
			CollectTypeCodeMap collectTypeCodes = BizCollectTypeCode.getInstance().getWeightItemCollectTypeCodes(serverIp, catalogName);
			System.out.println(collectTypeCodes.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CollectTypeCode collectTypeCode = new CollectTypeCode();
//		collectTypeCode.setCollectTypeCodeCode("TEST1");
//		collectTypeCode.setCollectTypeCodeName("TEST CollectTypeCode1");
//		collectTypeCode.setUseYesNo("Y");
//		BizCollectTypeCode.getInstance().applyCollectTypeCode(collectTypeCode);
		
/* DELETE */
//		BizCollectTypeCode.getInstance().deleteCollectTypeCode("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCollectTypeCode.getInstance().deleteCollectTypeCodes(list);

/* SELECT */
//		BizCollectTypeCode.getInstance().initCacheCollectTypeCodes();
//		System.out.println(cacheCollectTypeCodes.toXML());
//		

//		System.out.println(cacheCollectTypeCodes.toXML());
	}
}
