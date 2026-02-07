package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcIOException;
import com.joainfo.common.util.jdbc.JdbcProcedureException;
import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CollectList;
import com.joainfo.gasmax.bean.list.CollectListMap;

/**
 * BizCollectList
 * 수금현황 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCollectList {

	/**
	 * 수금현황 Select 쿼리의 ID
	 */
	public final String GASMAX_COLLECT_LIST_SELECT_ID = "GASMAX.CollectList.Select";
	
	/**
	 * 수금현황 Insert 쿼리의 ID
	 */
	public final String GASMAX_COLLECT_LIST_INSERT_ID = "GASMAX.CollectList.Insert";
	
	/**
	 * BizCollectList 인스턴스
	 */
	private static BizCollectList bizCollectList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCollectList(){
	}
	
	/**
	 * Singleton으로 BizCollectList 인스턴스 생성
	 * @return bizCollectList
	 */
	public static BizCollectList getInstance(){
		if (bizCollectList == null){
			bizCollectList = new BizCollectList();
		}
		return bizCollectList;
	}
	
	/**
	 * 키워드로 검색한 수금현황 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param employeeCode 사원 코드
	 * @param collectTypeCode 수금 유형 코드
	 * @param keyword 검색어
	 * @param startDate 일반(중량)수금 검색
	 * @param endDate 체적 수금 검색
	 * @return collectLists
	 */
	public CollectListMap getCollectLists(String serverIp, String catalogName, String areaCode, String collectClass, String employeeCode, String collectTypeCode, String keyword, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		if (("".equals(collectClass)) || (collectClass == null)){}else{
			condition.put("collectClass", collectClass);
		}
		if ("noEmployee".equals(employeeCode)){
			condition.put("employeeCode", "");
			condition.put("noEmployee", employeeCode);
		} else {
			condition.put("employeeCode", employeeCode==null?"":employeeCode);
		}
		condition.put("collectTypeCode", collectTypeCode==null?"":collectTypeCode);
		condition.put("keyword", keyword==null?"":keyword);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCollectLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 수금현황 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CollectListMap 형식의 수금현황 목록 반환
	 */
	public CollectListMap selectCollectLists(String serverIp, String catalogName, Map<String, String> condition){
		CollectListMap collectLists = new CollectListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_COLLECT_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CollectList collectList = convertCollectList(map);
			collectLists.setCollectList(collectList.getKeyValue(), collectList);
		}
		return collectLists;
	}
	
	/**
	 * @param serverIp
	 * @param catalogName
	 * @param areaCode
	 * @param customerCode
	 * @param collectType
	 * @param collectDate
	 * @param buildingName
	 * @param userName
	 * @param collectAmount
	 * @param discountAmount
	 * @param collectMethodType
	 * @param employeeCode
	 * @param employeeName
	 * @param remark
	 * @param registerDate
	 * @return S01 처리완료, E10 일반수금 DB저장 오류, E11 체적수금 DB저장 오류, E01 수금구분 오류.
	 * @throws JdbcIOException
	 * @throws JdbcProcedureException
	 */
	public String insertCollectList(
			String serverIp, 
			String catalogName, 
			String areaCode,
			String customerCode,
			String collectType,
			String collectDate,
			String buildingName,
			String userName,
			String collectAmount,
			String discountAmount,
			String collectMethodType,
			String employeeCode,
			String employeeName,
			String remark,
			String registerDate,
			String mobileNumber
			) throws JdbcIOException, JdbcProcedureException{
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("catalogName", catalogName);
		param.put("areaCode", areaCode);
		param.put("customerCode", customerCode);
		param.put("collectType", collectType);
		param.put("collectDate", collectDate);
		param.put("buildingName", buildingName);
		param.put("userName", userName);
		param.put("collectAmount", new Float(collectAmount));
		param.put("discountAmount", new Float(discountAmount));
		param.put("collectMethodType", collectMethodType);
		param.put("employeeCode", employeeCode);
		param.put("employeeName", employeeName);
		param.put("remark", remark);
		param.put("registerDate", registerDate);
		param.put("mobileNumber", mobileNumber);
		
		return JdbcUtil.getInstance(serverIp).executeProcedure(GASMAX_COLLECT_LIST_INSERT_ID, param, "outputMessage"); // 에러 코드를 반환한다.
	}
	
	/**
	 * HashMap을 CollectList으로 변환
	 * @param map
	 * @return CollectList
	 */
	protected static CollectList convertCollectList(HashMap<String, String> map){
		CollectList collectList = new CollectList();
		
		collectList.setCollectClass(map.get("collectClass"));
		collectList.setAreaCode(map.get("areaCode"));
		collectList.setCollectDate(map.get("collectDate"));
		collectList.setCustomerCode(map.get("customerCode"));
		collectList.setSequenceNumber(map.get("sequenceNumber"));
		collectList.setCustomerName(map.get("customerName"));
		collectList.setCollectAmount(map.get("collectAmount"));
		collectList.setDiscountAmount(map.get("discountAmount"));
		collectList.setEmployeeCode(map.get("employeeCode"));
		collectList.setEmployeeName(map.get("employeeName"));
		collectList.setCollectTypeCode(map.get("collectTypeCode"));
		collectList.setCollectTypeName(map.get("collectTypeName"));
		collectList.setRemark(map.get("remark"));
		collectList.setRegisterDate(map.get("registerDate"));
		collectList.setRegisterName(map.get("registerName"));
		
		return collectList;
	}
	
	/**
	 * CollectList을 HashMap으로 변환
	 * @param collectList
	 * @return
	 */
	protected static HashMap<String, String> convertCollectList(CollectList collectList){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("collectClass", collectList.getCollectClass());
		map.put("areaCode", collectList.getAreaCode());
		map.put("collectDate", collectList.getCollectDate());
		map.put("customerCode", collectList.getCustomerCode());
		map.put("sequenceNumber", collectList.getSequenceNumber());
		map.put("customerName", collectList.getCustomerName());
		map.put("collectAmount", collectList.getCollectAmount());
		map.put("discountAmount", collectList.getDiscountAmount());
		map.put("employeeCode", collectList.getEmployeeCode());
		map.put("employeeName", collectList.getEmployeeName());
		map.put("collectTypeCode", collectList.getCollectTypeCode());
		map.put("collectTypeName", collectList.getCollectTypeName());
		map.put("remark", collectList.getRemark());
		map.put("registerDate", collectList.getRegisterDate());
		map.put("registerName", collectList.getRegisterName());
		
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
		String customerCode = "'000-00010";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
//		String startDate = "20100701";
//		String endDate = "20100731";
		
		try{
//			CollectListMap collectLists = BizCollectList.getInstance().getCollectLists(serverIp, catalogName, areaCode, "일반", employeeCode, collectTypeCode, keyword, startDate, endDate);
//			System.out.println(collectLists.toXML());
			System.out.println(BizCollectList.getInstance().insertCollectList(
						serverIp,
						catalogName,
						areaCode,
						customerCode,
						"J",
						"20120914",
						"조아빌딩",
						"사용자",
						"5000",
						"500",
						"0",
						"01",
						"사원",
						"비고",
						"20120914180904",
						"01043322115_백원태"
					)
			);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CollectList collectList = new CollectList();
//		collectList.setCollectListCode("TEST1");
//		collectList.setCollectListName("TEST CollectList1");
//		collectList.setUseYesNo("Y");
//		BizCollectList.getInstance().applyCollectList(collectList);
		
/* DELETE */
//		BizCollectList.getInstance().deleteCollectList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCollectList.getInstance().deleteCollectLists(list);

/* SELECT */
//		BizCollectList.getInstance().initCacheCollectLists();
//		System.out.println(cacheCollectLists.toXML());
//		

//		System.out.println(cacheCollectLists.toXML());
	}
}
