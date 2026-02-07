package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.ReadMeterList;
import com.joainfo.gasmax.bean.list.ReadMeterListMap;

/**
 * BizReadMeterList
 * 검침현황 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizReadMeterList {

	/**
	 * 검침현황 Select 쿼리의 ID
	 */
	public final String GASMAX_READ_METER_LIST_SELECT_ID = "GASMAX.ReadMeterList.Select";
	
	/**
	 * BizReadMeterList 인스턴스
	 */
	private static BizReadMeterList bizReadMeterList;
	
	/**
	 * 디폴트 생성자
	 */
	public BizReadMeterList(){
	}
	
	/**
	 * Singleton으로 BizReadMeterList 인스턴스 생성
	 * @return bizReadMeterList
	 */
	public static BizReadMeterList getInstance(){
		if (bizReadMeterList == null){
			bizReadMeterList = new BizReadMeterList();
		}
		return bizReadMeterList;
	}
	
	/**
	 * 키워드로 검색한 검침현황 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param employeeCode 사원 코드
	 * @param collectTypeCode 수금 유형 코드
	 * @param keyword 검색어
	 * @param weightReadMeterType 일반(중량)검침 검색
	 * @param volumeReadMeterType 체적 검침 검색
	 * @param noEmployee 담당자 미지정 검색
	 * @return readMeterLists
	 */
	public ReadMeterListMap getReadMeterLists(String serverIp, String catalogName, String areaCode, String employeeCode, String collectTypeCode, String keyword, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
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
		return selectReadMeterLists(serverIp, catalogName, condition);
	}
	
	/**
	 * 검침현황 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return ReadMeterListMap 형식의 검침현황 목록 반환
	 */
	public ReadMeterListMap selectReadMeterLists(String serverIp, String catalogName, Map<String, String> condition){
		ReadMeterListMap readMeterLists = new ReadMeterListMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_READ_METER_LIST_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			ReadMeterList readMeterList = convertReadMeterList(map);
			readMeterLists.setReadMeterList(readMeterList.getKeyValue(), readMeterList);
		}
		return readMeterLists;
	}
	/**
	 * HashMap을 ReadMeterList으로 변환
	 * @param map
	 * @return ReadMeterList
	 */
	protected static ReadMeterList convertReadMeterList(HashMap<String, String> map){
		ReadMeterList readMeterList = new ReadMeterList();
		
		readMeterList.setAreaCode(map.get("areaCode"));
		readMeterList.setCustomerCode(map.get("customerCode"));
		readMeterList.setSequenceNumber(map.get("sequenceNumber"));
		readMeterList.setReadMeterDate(map.get("readMeterDate"));
		readMeterList.setCustomerName(map.get("customerName"));
		readMeterList.setPreReadMeter(map.get("preReadMeter"));
		readMeterList.setNowReadMeter(map.get("nowReadMeter"));
		readMeterList.setRemainQuantity(map.get("remainQuantity"));
		readMeterList.setUseQuantity(map.get("useQuantity"));
		readMeterList.setNowAmount(map.get("nowAmount"));
		readMeterList.setOtherAmount(map.get("otherAmount"));
		readMeterList.setUnpaidAmount(map.get("unpaidAmount"));
		readMeterList.setChargeAmount(map.get("chargeAmount"));
		readMeterList.setSumNowAmount(map.get("sumNowAmount"));
		readMeterList.setSumUnpaidAmount(map.get("sumUnpaidAmount"));
		readMeterList.setUseStartDate(map.get("useStartDate"));
		readMeterList.setUseEndDate(map.get("useEndDate"));
		readMeterList.setPrintGiroDate(map.get("printGiroDate"));
		readMeterList.setPayClosingDate(map.get("payClosingDate"));
		readMeterList.setRemark(map.get("remark"));
		readMeterList.setCollectTypeCode(map.get("collectTypeCode"));
		readMeterList.setEmployeeCode(map.get("employeeCode"));
		readMeterList.setEmployeeName(map.get("employeeName"));
		
		return readMeterList;
	}
	
	/**
	 * ReadMeterList을 HashMap으로 변환
	 * @param readMeterList
	 * @return
	 */
	protected static HashMap<String, String> convertReadMeterList(ReadMeterList readMeterList){
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("areaCode", readMeterList.getAreaCode());
		map.put("customerCode", readMeterList.getCustomerCode());
		map.put("sequenceNumber", readMeterList.getSequenceNumber());
		map.put("readMeterDate", readMeterList.getReadMeterDate());
		map.put("customerName", readMeterList.getCustomerName());
		map.put("preReadMeter", readMeterList.getPreReadMeter());
		map.put("nowReadMeter", readMeterList.getNowReadMeter());
		map.put("remainQuantity", readMeterList.getRemainQuantity());
		map.put("useQuantity", readMeterList.getUseQuantity());
		map.put("nowAmount", readMeterList.getNowAmount());
		map.put("otherAmount", readMeterList.getOtherAmount());
		map.put("unpaidAmount", readMeterList.getUnpaidAmount());
		map.put("chargeAmount", readMeterList.getChargeAmount());
		map.put("sumNowAmount", readMeterList.getSumNowAmount());
		map.put("sumUnpaidAmount", readMeterList.getSumUnpaidAmount());
		map.put("useStartDate", readMeterList.getUseStartDate());
		map.put("useEndDate", readMeterList.getUseEndDate());
		map.put("printGiroDate", readMeterList.getPrintGiroDate());
		map.put("payClosingDate", readMeterList.getPayClosingDate());
		map.put("remark", readMeterList.getRemark());
		map.put("collectTypeCode", readMeterList.getCollectTypeCode());
		map.put("employeeCode", readMeterList.getEmployeeCode());
		map.put("employeeName", readMeterList.getEmployeeName());
		
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
		String weightReadMeterType = "";
		String volumeReadMeterType = "";
		
		try{
			ReadMeterListMap readMeterLists = BizReadMeterList.getInstance().getReadMeterLists(serverIp, catalogName, areaCode, employeeCode, collectTypeCode, keyword, weightReadMeterType, volumeReadMeterType);
			System.out.println(readMeterLists.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		ReadMeterList readMeterList = new ReadMeterList();
//		readMeterList.setReadMeterListCode("TEST1");
//		readMeterList.setReadMeterListName("TEST ReadMeterList1");
//		readMeterList.setUseYesNo("Y");
//		BizReadMeterList.getInstance().applyReadMeterList(readMeterList);
		
/* DELETE */
//		BizReadMeterList.getInstance().deleteReadMeterList("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizReadMeterList.getInstance().deleteReadMeterLists(list);

/* SELECT */
//		BizReadMeterList.getInstance().initCacheReadMeterLists();
//		System.out.println(cacheReadMeterLists.toXML());
//		

//		System.out.println(cacheReadMeterLists.toXML());
	}
}
