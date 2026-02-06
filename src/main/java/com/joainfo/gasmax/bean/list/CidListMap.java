package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CidList;

/**
 * CID현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CidListMap {

	/**
	 * CidList 목록
	 */
	private LinkedHashMap<String, CidList> cidLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 디폴트 생성자
	 */
	public CidListMap(){
		if (cidLists == null) {
			cidLists = new LinkedHashMap<String, CidList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CidList> getCidLists(){
		return cidLists;
	}
	
	/**
	 * @param cidLists
	 */
	public void setCidLists(LinkedHashMap<String, CidList> cidLists){
		this.cidLists = cidLists;
	}
	
	/**
	 * @param id
	 * @return CidList
	 */
	public CidList getCidList(String id){
		return this.cidLists.get(id);
	}
	
	/**
	 * @param id
	 * @param cidList
	 */
	public void setCidList(String id, CidList cidList){
		this.cidLists.put(id, cidList);
	}
	
	/**
	 * @param cidList
	 */
	public void setCidList(CidList cidList){
		this.cidLists.put(cidList.getKeyValue(), cidList);
	}
	
	/**
	 * @return the totalRowCount
	 */
	public String getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * @param totalRowCount the totalRowCount to set
	 */
	public void setTotalRowCount(String totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	/**
	 * @param id
	 */
	public void removeCidList(String id){
		this.cidLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.cidLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return cidLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CidLists>";
				
		java.util.Iterator<String> iterator = cidLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += cidLists.get(key).toXML();
		  }
		xml += "</CidLists>";
		
		return xml; 
	}

	/**
	 * XML을 페이지로 나눠서 HashMap으로 반환
	 * @param rowCount 한 페이지에 들어갈 행의 개수
	 * @return 페이지로 나눠 XML을 담은 HashMap
	 */
	public HashMap<String, String> toPagingXML(int rowCount){
		int pageNumber = 1;
		int rowNumber = 1;
		int totalRowCount = cidLists.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = cidLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CidList cidList = cidLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += cidList.toXML();
			} else {
				xml +=  cidList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CidLists>" + new String(xml) + "</CidLists>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CidLists>" + new String(xml) + "</CidLists>");
		}
		return pageXML;
	}
	
}
