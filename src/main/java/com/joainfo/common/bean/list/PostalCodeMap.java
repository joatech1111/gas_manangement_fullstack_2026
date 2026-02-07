package com.joainfo.common.bean.list;


import java.util.LinkedHashMap;

import com.joainfo.common.bean.PostalCode;

/**
 * 거래처 지역분류 코드 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class PostalCodeMap {

	/**
	 * PostalCode 목록
	 */
	private LinkedHashMap<String, PostalCode> postalCodes;
	
	/**
	 * 디폴트 생성자
	 */
	public PostalCodeMap(){
		if (postalCodes == null) {
			postalCodes = new LinkedHashMap<String, PostalCode>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, PostalCode> getPostalCodes(){
		return postalCodes;
	}
	
	/**
	 * @param postalCodes
	 */
	public void setPostalCodes(LinkedHashMap<String, PostalCode> postalCodes){
		this.postalCodes = postalCodes;
	}
	
	/**
	 * @param id
	 * @return PostalCode
	 */
	public PostalCode getPostalCode(String id){
		return this.postalCodes.get(id);
	}
	
	/**
	 * @param id
	 * @param customer
	 */
	public void setPostalCode(String id, PostalCode postalCode){
		this.postalCodes.put(id, postalCode);
	}
	
	/**
	 * @param customer
	 */
	public void setPostalCode(PostalCode postalCode){
		this.postalCodes.put(postalCode.getKeyValue(), postalCode);
	}
	
	/**
	 * @param id
	 */
	public void removePostalCode(String id){
		this.postalCodes.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.postalCodes.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return postalCodes.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<PostalCodes>";
				
		java.util.Iterator<String> iterator = postalCodes.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += postalCodes.get(key).toXML();
		  }
		xml += "</PostalCodes>";
		
		return xml; 
	}
}
