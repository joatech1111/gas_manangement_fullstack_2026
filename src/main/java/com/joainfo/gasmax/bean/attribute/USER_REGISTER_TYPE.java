package com.joainfo.gasmax.bean.attribute;

/**
 * USER_REGISTER_TYPE 을 관리하는 enum
 * @author 백원태
 * @version 1.0
 */
public enum USER_REGISTER_TYPE {
	 RegisteredType("Y", "인증등록"),
	 ApplicationType("N", "등록요청"),
	 RetiredType("X", "중지");

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
	USER_REGISTER_TYPE(String code, String name) {
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
	 * @return USER_REGISTER_TYPE
	 */
	public static USER_REGISTER_TYPE findByCode(String code)
	{     
		for(USER_REGISTER_TYPE m : values())
		{         
			if(m.getCode().equals(code))
				return m;              
		}
		return null; 
	}
}
