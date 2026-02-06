package com.joainfo.common.bean.attribute;

/**
 * EMPLOYEE_TYPE 을 관리하는 enum
 * @author 백원태
 * @version 1.0
 */
public enum EMPLOYEE_TYPE {
	 DeliveryType("0", "배달사원"),
	 TransportType("1", "수송사원"),
	 ETCType("2", "기타");

	 /**
	 * 코드
	 */
	private String code;
	
	/**
	 * 명칭
	 */
	private String name;
	
	/**
	 * enum의 구성
	 * @param code 코드
	 * @param name 명칭
	 */
	EMPLOYEE_TYPE(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	/**
	 * 코드를 반환
	 * @return 코드
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * 명칭을 반환
	 * @return 명칭
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 코드를 이용하여 값을 반환
	 * @param code
	 * @return EMPLOYEE_TYPE
	 */
	public static EMPLOYEE_TYPE findByCode(String code)
	{     
		for(EMPLOYEE_TYPE m : values())
		{         
			if(m.getCode().equals(code))
				return m;              
		}
		return null; 
	}
}
