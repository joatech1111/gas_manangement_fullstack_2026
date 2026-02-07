package com.joainfo.common.biz;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.common.bean.PostalCode;
import com.joainfo.common.bean.list.PostalCodeMap;

/**
 * BizPostalCode
 * 우편 번호 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizPostalCode {

	/**
	 * 우편 번호 Select 쿼리의 ID
	 */
	public final String COMMON_POSTAL_CODE_SELECT_ID = "COMMON.PostalCode.Select";

	/**
	 * BizPostalCode 인스턴스
	 */
	private static BizPostalCode bizPostalCode;
	
	/**
	 * 캐시된 DB 카탈로그 별 우편 번호 코드 목록
	 */
	private static LinkedHashMap<String, PostalCodeMap> cachePostalCodes;
	
	/**
	 * 우편번호 디폴트 DB 카탈로그 명
	 */
	public final static String DEFAULT_POSTAL_CODE_CATATLOG_NAME = "ZipCode";
	
	/**
	 * 디폴트 생성자
	 */
	public BizPostalCode(){
		if(cachePostalCodes == null) {
			initCachePostalCodes();
		}
	}
	
	/**
	 * Singleton으로 BizPostalCode 인스턴스 생성
	 * @return bizPostalCode
	 */
	public static BizPostalCode getInstance(){
		if (bizPostalCode == null){
			bizPostalCode = new BizPostalCode();
		}
		return bizPostalCode;
	}
	
	/**
	 * 우편 번호 코드 캐시의 초기화
	 */
	public void initCachePostalCodes(){
		cachePostalCodes = new LinkedHashMap<String, PostalCodeMap>();
	}
	
	/**
	 * 우편 번호 코드 캐시의 재초기화
	 * @param catalogName
	 * @return PostalCodeMap
	 */
	public PostalCodeMap reloadCachePostalCodes(String catalogName){
		PostalCodeMap postalCodes = this.selectPostalCodes(catalogName);
		cachePostalCodes.put(catalogName, postalCodes);
		return postalCodes;
	}
	
	/**
	 * 우편 번호 코드 반환
	 * @param catalogName
	 * @param key
	 * @return postalCode
	 */
	public PostalCode getPostalCode(String catalogName, String key){
		PostalCodeMap postalCodes = getPostalCodes(catalogName);
		return postalCodes==null?null:postalCodes.getPostalCode(key);
	}
	
	/**
	 * 캐시로부터 우편 번호 코드 목록을 반환
	 * @param catalogName
	 * @return postalCodes
	 */
	public PostalCodeMap getPostalCodes(String catalogName){
		return cachePostalCodes.containsKey(catalogName)?cachePostalCodes.get(catalogName):reloadCachePostalCodes(catalogName);
	}
	
	/**
	 * 캐시된 우편 번호 코드 설정
	 * @param catalogName
	 * @param postalCodes
	 */
	public void setPostalCodes(String catalogName, PostalCodeMap postalCodes){
		cachePostalCodes.put(catalogName, postalCodes);
	}
	
	/**
	 * 우편 번호 코드 조회
	 * @param catalogName
	 * @return PostalCodeMap 형식의 우편 번호 코드 목록 반환
	 */
	public PostalCodeMap selectPostalCodes(String catalogName){
		PostalCodeMap postalCodes = new PostalCodeMap();
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery(COMMON_POSTAL_CODE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			PostalCode postalCode = convertPostalCode(map);
			postalCodes.setPostalCode(postalCode.getKeyValue(), postalCode);
		}
		return postalCodes;
	}
	
	/**
	 * HashMap을 PostalCode으로 변환
	 * @param map
	 * @return PostalCode
	 */
	protected static PostalCode convertPostalCode(HashMap<String, String> map){
		PostalCode postalCode = new PostalCode();
		
		postalCode.setPostalCode(map.get("postalCode"));
		postalCode.setSequenceNumber(map.get("sequenceNumber"));
		postalCode.setCity(map.get("city"));
		postalCode.setState(map.get("state"));
		postalCode.setTown(map.get("town"));
		postalCode.setAddress(map.get("address"));
		postalCode.setDialAreaCode(map.get("dialAreaCode"));
		
		return postalCode;
	}
	
	/**
	 * 비즈니스 로직 테스트용
	 * @param args
	 */
	public static void main(String[] args){

		BizPostalCode bizPostalCode = BizPostalCode.getInstance();
		if(cachePostalCodes.containsKey("ZipCode")){
			System.out.println("ZipCode is contained");
		}else{
			System.out.println("ZipCode is not contained");
		}
		System.out.println(bizPostalCode.getPostalCodes("ZipCode").getPostalCode("_100011_1").toXML());
		if(cachePostalCodes.containsKey("ZipCode")){
			System.out.println("ZipCode is contained");
		}else{
			System.out.println("ZipCode is not contained");
		}
		System.out.println(bizPostalCode.getPostalCodes("ZipCode").getPostalCode("_100011_1").toXML());
/* INSERT OR UPDATE*/
//		PostalCode postalCode = new PostalCode();
//		postalCode.setPostalCodeCode("TEST1");
//		postalCode.setPostalCodeName("TEST PostalCode1");
//		postalCode.setUseYesNo("Y");
//		BizPostalCode.getInstance().applyPostalCode(postalCode);
		
/* DELETE */
//		BizPostalCode.getInstance().deletePostalCode("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizPostalCode.getInstance().deletePostalCodes(list);

/* SELECT */
//		BizPostalCode.getInstance().initCachePostalCodes();
//		System.out.println(cachePostalCodes.toXML());
//		

//		System.out.println(cachePostalCodes.toXML());
	}
}
