package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 소비 형태 코드
 * @author 백원태
 * @version 1.0
 */
public class ConsumeTypeCode {
	
	/**
	 * 소비형태 코드 - key
	 */
	private String consumeTypeCode;
	
	/**
	 * 소비형태 명
	 */
	private String consumeTypeName;

	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("CONSUME_TYPE_CODE", getConsumeTypeCode());
		
		return keys; 
	}
	
	/**
	 * key 값 반환
	 * @return
	 */
	public String getKeyValue(){
		return StringUtil.getKeyValue(this.getKeyMap()); 
	}

	/**
	 * @return the consumeTypeCode
	 */
	public String getConsumeTypeCode() {
		return consumeTypeCode;
	}

	/**
	 * @param consumeTypeCode the consumeTypeCode to set
	 */
	public void setConsumeTypeCode(String consumeTypeCode) {
		this.consumeTypeCode = consumeTypeCode;
	}

	/**
	 * @return the consumeTypeName
	 */
	public String getConsumeTypeName() {
		return consumeTypeName;
	}

	/**
	 * @param consumeTypeName the consumeTypeName to set
	 */
	public void setConsumeTypeName(String consumeTypeName) {
		this.consumeTypeName = consumeTypeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConsumeTypeCode [key=" + this.getKeyValue() 
				+ ", consumeTypeCode=" + consumeTypeCode
				+ ", consumeTypeName=" + consumeTypeName + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<ConsumeTypeCode><key>" + this.getKeyValue()
				+ "</key><consumeTypeCode>" + consumeTypeCode
				+ "</consumeTypeCode><consumeTypeName>" + consumeTypeName
				+ "</consumeTypeName></ConsumeTypeCode>";
	}
	
	
}
