package com.joainfo.gasmax.bean.list;


import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.ConsumeTypeCode;

/**
 * 소비형태코드 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class ConsumeTypeCodeMap {

	/**
	 * ConsumeTypeCode 목록
	 */
	private LinkedHashMap<String, ConsumeTypeCode> consumeTypeCodes;
	
	/**
	 * 디폴트 생성자
	 */
	public ConsumeTypeCodeMap(){
		if (consumeTypeCodes == null) {
			consumeTypeCodes = new LinkedHashMap<String, ConsumeTypeCode>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, ConsumeTypeCode> getConsumeTypeCodes(){
		return consumeTypeCodes;
	}
	
	/**
	 * @param consumeTypeCodes
	 */
	public void setConsumeTypeCodes(LinkedHashMap<String, ConsumeTypeCode> consumeTypeCodes){
		this.consumeTypeCodes = consumeTypeCodes;
	}
	
	/**
	 * @param id
	 * @return ConsumeTypeCode
	 */
	public ConsumeTypeCode getConsumeTypeCode(String id){
		return this.consumeTypeCodes.get(id);
	}
	
	/**
	 * @param id
	 * @param consumeTypeCode
	 */
	public void setConsumeTypeCode(String id, ConsumeTypeCode consumeTypeCode){
		this.consumeTypeCodes.put(id, consumeTypeCode);
	}
	
	/**
	 * @param consumeTypeCode
	 */
	public void setConsumeTypeCode(ConsumeTypeCode consumeTypeCode){
		this.consumeTypeCodes.put(consumeTypeCode.getKeyValue(), consumeTypeCode);
	}
	
	/**
	 * @param id
	 */
	public void removeConsumeTypeCode(String id){
		this.consumeTypeCodes.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.consumeTypeCodes.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return consumeTypeCodes.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<ConsumeTypeCodes>";
				
		java.util.Iterator<String> iterator = consumeTypeCodes.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += consumeTypeCodes.get(key).toXML();
		  }
		xml += "</ConsumeTypeCodes>";
		
		return xml; 
	}
	
}
