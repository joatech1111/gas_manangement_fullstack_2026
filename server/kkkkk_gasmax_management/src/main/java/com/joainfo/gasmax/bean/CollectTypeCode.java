package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 수금유형 코드
 * @author 백원태
 * @version 1.0
 */
public class CollectTypeCode {

	/**
	 * 수금 유형 코드 - key
	 */
	private String collectTypeCode;
	
	/**
	 * 수금 유형 명
	 */
	private String collectTypeName;
	
	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("CODE", getCollectTypeCode());
		
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
	 * @return the collectTypeCode
	 */
	public String getCollectTypeCode() {
		return collectTypeCode;
	}

	/**
	 * @param collectTypeCode the collectTypeCode to set
	 */
	public void setCollectTypeCode(String collectTypeCode) {
		this.collectTypeCode = collectTypeCode;
	}

	/**
	 * @return the collectTypeName
	 */
	public String getCollectTypeName() {
		return collectTypeName;
	}

	/**
	 * @param collectTypeName the collectTypeName to set
	 */
	public void setCollectTypeName(String collectTypeName) {
		this.collectTypeName = collectTypeName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectTypeCode [key=" + this.getKeyValue() +", collectTypeCode=" + collectTypeCode
				+ ", collectTypeName=" + collectTypeName + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<CollectTypeCode><key>" + this.getKeyValue()
				+ "</key><collectTypeCode>" + collectTypeCode
				+ "</collectTypeCode><collectTypeName>" + collectTypeName
				+ "</collectTypeName></CollectTypeCode>";
	}
	
}
