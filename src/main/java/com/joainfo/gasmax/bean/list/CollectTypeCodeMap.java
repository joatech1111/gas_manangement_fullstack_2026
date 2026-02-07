package com.joainfo.gasmax.bean.list;


import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CollectTypeCode;

/**
 * 수금유형코드 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CollectTypeCodeMap {

	/**
	 * CollectTypeCode 목록
	 */
	private LinkedHashMap<String, CollectTypeCode> collectTypeCodes;
	
	/**
	 * 디폴트 생성자
	 */
	public CollectTypeCodeMap(){
		if (collectTypeCodes == null) {
			collectTypeCodes = new LinkedHashMap<String, CollectTypeCode>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CollectTypeCode> getCollectTypeCodes(){
		return collectTypeCodes;
	}
	
	/**
	 * @param collectTypeCodes
	 */
	public void setCollectTypeCodes(LinkedHashMap<String, CollectTypeCode> collectTypeCodes){
		this.collectTypeCodes = collectTypeCodes;
	}
	
	/**
	 * @param id
	 * @return CollectTypeCode
	 */
	public CollectTypeCode getCollectTypeCode(String id){
		return this.collectTypeCodes.get(id);
	}
	
	/**
	 * @param id
	 * @param collectTypeCode
	 */
	public void setCollectTypeCode(String id, CollectTypeCode collectTypeCode){
		this.collectTypeCodes.put(id, collectTypeCode);
	}
	
	/**
	 * @param collectTypeCode
	 */
	public void setCollectTypeCode(CollectTypeCode collectTypeCode){
		this.collectTypeCodes.put(collectTypeCode.getKeyValue(), collectTypeCode);
	}
	
	/**
	 * @param id
	 */
	public void removeCollectTypeCode(String id){
		this.collectTypeCodes.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.collectTypeCodes.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return collectTypeCodes.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CollectTypeCodes>";
				
		java.util.Iterator<String> iterator = collectTypeCodes.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += collectTypeCodes.get(key).toXML();
		  }
		xml += "</CollectTypeCodes>";
		
		return xml; 
	}
	
}
