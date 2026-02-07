package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.ConsumeTypeCode;
import com.joainfo.gasmax.bean.list.ConsumeTypeCodeMap;

/**
 * BizConsumeTypeCode
 * 소비형태코드 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizConsumeTypeCode {

	/**
	 * 소비형태코드 Select 쿼리의 ID
	 */
	public final String GASMAX_CONSUME_TYPE_CODE_SELECT_ID = "GASMAX.ConsumeTypeCode.Select";
	
	/**
	 * BizConsumeTypeCode 인스턴스
	 */
	private static BizConsumeTypeCode bizConsumeTypeCode;
	
	/**
	 * 디폴트 생성자
	 */
	public BizConsumeTypeCode(){
	}
	
	/**
	 * Singleton으로 BizConsumeTypeCode 인스턴스 생성
	 * @return bizConsumeTypeCode
	 */
	public static BizConsumeTypeCode getInstance(){
		if (bizConsumeTypeCode == null){
			bizConsumeTypeCode = new BizConsumeTypeCode();
		}
		return bizConsumeTypeCode;
	}
	
	/**
	 * 키워드로 검색한 소비형태코드 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 소비형태코드(1건만 검색할 때 필요)
	 * @param consumeTypeCodeType ( 0:중량, 1:체적검침(건물제외), 2:체적공급, A:전체거래처) 
	 * @param employeeCode 사원 코드
	 * @param keyword 검색어
	 * @param grantCode 권한 코크
	 * @return consumeTypeCodes
	 */
	public ConsumeTypeCodeMap getConsumeTypeCodes(String serverIp, String catalogName){
		HashMap<String, String> condition = new HashMap<String, String>();
		return selectConsumeTypeCodes(serverIp, catalogName, condition);
	}
	
	/**
	 * 소비형태코드 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return ConsumeTypeCodeMap 형식의 소비형태코드 목록 반환
	 */
	public ConsumeTypeCodeMap selectConsumeTypeCodes(String serverIp, String catalogName, Map<String, String> condition){
		ConsumeTypeCodeMap consumeTypeCodes = new ConsumeTypeCodeMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CONSUME_TYPE_CODE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			ConsumeTypeCode consumeTypeCode = convertConsumeTypeCode(map);
			consumeTypeCodes.setConsumeTypeCode(consumeTypeCode.getKeyValue(), consumeTypeCode);
		}
		return consumeTypeCodes;
	}
	/**
	 * HashMap을 ConsumeTypeCode으로 변환
	 * @param map
	 * @return ConsumeTypeCode
	 */
	protected static ConsumeTypeCode convertConsumeTypeCode(HashMap<String, String> map){
		ConsumeTypeCode consumeTypeCode = new ConsumeTypeCode();
		
		consumeTypeCode.setConsumeTypeCode(map.get("consumeTypeCode"));
		consumeTypeCode.setConsumeTypeName(map.get("consumeTypeName"));
		
		return consumeTypeCode;
	}
	
	/**
	 * ConsumeTypeCode을 HashMap으로 변환
	 * @param consumeTypeCode
	 * @return
	 */
	protected static HashMap<String, String> convertConsumeTypeCode(ConsumeTypeCode consumeTypeCode){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("consumeTypeCode", consumeTypeCode.getConsumeTypeCode());
		map.put("consumeTypeName", consumeTypeCode.getConsumeTypeName());

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
			ConsumeTypeCodeMap consumeTypeCodes = BizConsumeTypeCode.getInstance().getConsumeTypeCodes(serverIp, catalogName);
			System.out.println(consumeTypeCodes.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		ConsumeTypeCode consumeTypeCode = new ConsumeTypeCode();
//		consumeTypeCode.setConsumeTypeCodeCode("TEST1");
//		consumeTypeCode.setConsumeTypeCodeName("TEST ConsumeTypeCode1");
//		consumeTypeCode.setUseYesNo("Y");
//		BizConsumeTypeCode.getInstance().applyConsumeTypeCode(consumeTypeCode);
		
/* DELETE */
//		BizConsumeTypeCode.getInstance().deleteConsumeTypeCode("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizConsumeTypeCode.getInstance().deleteConsumeTypeCodes(list);

/* SELECT */
//		BizConsumeTypeCode.getInstance().initCacheConsumeTypeCodes();
//		System.out.println(cacheConsumeTypeCodes.toXML());
//		

//		System.out.println(cacheConsumeTypeCodes.toXML());
	}
}
