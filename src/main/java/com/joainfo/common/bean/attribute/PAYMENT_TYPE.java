package com.joainfo.common.bean.attribute;


/**
 * PAYMENT_TYPE 을 관리하는 enum
 * @author 백원태
 * @version 1.0
 */
public enum PAYMENT_TYPE {
	 CashType("0", "현금(방문)"),
	 GiroType("1", "지로"),
	 DepositeType("2", "예금"),
	 CreditCardType("3", "카드"),
	 BillType("4", "어음"),
	 EDIType("5", "EDI"),
	 CMSType("6", "CMS");

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
	PAYMENT_TYPE(String code, String name) {
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
	 * @return PAYMENT_TYPE
	 */
	public static PAYMENT_TYPE findByCode(String code)
	{     
		for(PAYMENT_TYPE m : values())
		{         
			if(m.getCode().equals(code))
				return m;              
		}
		return null; 
	}
}
