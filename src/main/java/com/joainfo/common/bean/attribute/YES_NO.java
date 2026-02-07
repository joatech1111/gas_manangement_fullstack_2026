package com.joainfo.common.bean.attribute;


/**
 * YES_NO 를 관리하는 enum
 * @author 백원태
 * @version 1.0
 */
public enum YES_NO {
	 YesType("Y", "Yes"),
	 NoType("N", "No");

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
	YES_NO(String code, String name) {
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
	 * @return YES_NO
	 */
	public static YES_NO findByCode(String code)
	{     
		for(YES_NO m : values())
		{         
			if(m.getCode().equals(code))
				return m;              
		}
		return null; 
	}
}
