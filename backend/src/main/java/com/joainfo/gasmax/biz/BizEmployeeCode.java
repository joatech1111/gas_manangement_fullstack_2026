package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.EmployeeCode;
import com.joainfo.gasmax.bean.list.EmployeeCodeMap;

	/**
	 * BizEmployeeCode
	 * 사원코드 비즈니스 로직 처리 객체
	 * @author 백원태
	 * @version 1.0
	 */
	public class BizEmployeeCode {

		/**
		 * 사원코드 Select 쿼리의 ID
		 */
		public final String GASMAX_EMPLOYEE_CODE_SELECT_ID = "GASMAX.EmployeeCode.Select";
		
		/**
		 * BizEmployeeCode 인스턴스
		 */
		private static BizEmployeeCode bizEmployeeCode;
		
		/**
		 * 디폴트 생성자
		 */
		public BizEmployeeCode(){
		}
		
		/**
		 * Singleton으로 BizEmployeeCode 인스턴스 생성
		 * @return bizEmployeeCode
		 */
		public static BizEmployeeCode getInstance(){
			if (bizEmployeeCode == null){
				bizEmployeeCode = new BizEmployeeCode();
			}
			return bizEmployeeCode;
		}
		
		/**
		 * 키워드로 검색한 사원코드 목록을 반환
		 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
		 * @param catalogName DB 카탈로그 명
		 * @param areaCode 영업소 코드
		 * @return employeeCodes
		 */
		public EmployeeCodeMap getEmployeeCodes(String serverIp, String catalogName, String areaCode){
			HashMap<String, String> condition = new HashMap<String, String>();
			condition.put("areaCode", areaCode);
			return selectEmployeeCodes(serverIp, catalogName, condition);
		}
		
		/**
		 * 사원코드 조회
		 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
		 * @param catalogName DB 카탈로그 명
		 * @param condition 검색 조건
		 * @return EmployeeCodeMap 형식의 사원코드 목록 반환
		 */
		public EmployeeCodeMap selectEmployeeCodes(String serverIp, String catalogName, Map<String, String> condition){

			try{
				EmployeeCodeMap employeeCodes = new EmployeeCodeMap();
				condition.put("catalogName", catalogName);

				@SuppressWarnings("rawtypes")
				List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_EMPLOYEE_CODE_SELECT_ID, condition);
				for( HashMap<String, String> map :  list) {
					EmployeeCode employeeCode = convertEmployeeCode(map);
					employeeCodes.setEmployeeCode(employeeCode.getKeyValue(), employeeCode);
				}
				return employeeCodes;
			}catch (Exception e){

				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
			}

			return null;
		}
		/**
		 * HashMap을 EmployeeCode으로 변환
		 * @param map
		 * @return EmployeeCode
		 */
		protected static EmployeeCode convertEmployeeCode(HashMap<String, String> map){
			EmployeeCode employeeCode = new EmployeeCode();
			
			employeeCode.setEmployeeCode(map.get("employeeCode"));
			employeeCode.setEmployeeName(map.get("employeeName"));
			
			return employeeCode;
		}
		
		/**
		 * EmployeeCode을 HashMap으로 변환
		 * @param employeeCode
		 * @return
		 */
		protected static HashMap<String, String> convertEmployeeCode(EmployeeCode employeeCode){
			HashMap<String, String> map = new HashMap<String, String>();
			
			map.put("employeeCode", employeeCode.getEmployeeCode());
			map.put("employeeName", employeeCode.getEmployeeName());

			return map;
		}
		
		/**
		 * 비즈니스 로직 테스트용
		 * @param args
		 */
		public static void main(String[] args){
//			String keyword = "두";
			String serverIp = "joatech2.dyndns.org";
			String catalogName = "GM_TestHigh";
			String areaCode = "01";
//			String customerCode = "";
//			String customerType = "A";
//			String employeeCode = "1";
//			String grantCode = "00111111";
			try{
				EmployeeCodeMap employeeCodes = BizEmployeeCode.getInstance().getEmployeeCodes(serverIp, catalogName, areaCode);
				System.out.println(employeeCodes.toXML());
			} catch (Exception e){
				e.printStackTrace();
			}
			
	/* INSERT OR UPDATE*/
//			EmployeeCode employeeCode = new EmployeeCode();
//			employeeCode.setEmployeeCodeCode("TEST1");
//			employeeCode.setEmployeeCodeName("TEST EmployeeCode1");
//			employeeCode.setUseYesNo("Y");
//			BizEmployeeCode.getInstance().applyEmployeeCode(employeeCode);
			
	/* DELETE */
//			BizEmployeeCode.getInstance().deleteEmployeeCode("TEST");
			
	/* DELETE LIST */
//			List<String> list = new java.util.ArrayList<String>();
//			list.add("TEST1");
//			list.add("TEST2");
//			BizEmployeeCode.getInstance().deleteEmployeeCodes(list);

	/* SELECT */
//			BizEmployeeCode.getInstance().initCacheEmployeeCodes();
//			System.out.println(cacheEmployeeCodes.toXML());
//			

//			System.out.println(cacheEmployeeCodes.toXML());
		}
	}

