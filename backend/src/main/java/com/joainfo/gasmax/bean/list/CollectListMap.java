package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CollectList;

/**
 * 수금현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CollectListMap {

	/**
	 * CollectList 목록
	 */
	private LinkedHashMap<String, CollectList> collectLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 입금액 합계
	 */
	private String totalCollectAmount;
	
	/**
	 * 할인액 합계
	 */
	private String totalDiscountAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public CollectListMap(){
		if (collectLists == null) {
			collectLists = new LinkedHashMap<String, CollectList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CollectList> getCollectLists(){
		return collectLists;
	}
	
	/**
	 * @param collectLists
	 */
	public void setCollectLists(LinkedHashMap<String, CollectList> collectLists){
		this.collectLists = collectLists;
	}
	
	/**
	 * @param id
	 * @return CollectList
	 */
	public CollectList getCollectList(String id){
		return this.collectLists.get(id);
	}
	
	/**
	 * @param id
	 * @param collectList
	 */
	public void setCollectList(String id, CollectList collectList){
		this.collectLists.put(id, collectList);
	}
	
	/**
	 * @param collectList
	 */
	public void setCollectList(CollectList collectList){
		this.collectLists.put(collectList.getKeyValue(), collectList);
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
	 * @return the totalCollectAmount
	 */
	public String getTotalCollectAmount() {
		return totalCollectAmount;
	}

	/**
	 * @param totalCollectAmount the totalCollectAmount to set
	 */
	public void setTotalCollectAmount(String totalCollectAmount) {
		this.totalCollectAmount = totalCollectAmount;
	}

	/**
	 * @return the totalDiscountAmount
	 */
	public String getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	/**
	 * @param totalDiscountAmount the totalDiscountAmount to set
	 */
	public void setTotalDiscountAmount(String totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	/**
	 * @param id
	 */
	public void removeCollectList(String id){
		this.collectLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.collectLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return collectLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CollectLists>";
				
		java.util.Iterator<String> iterator = collectLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += collectLists.get(key).toXML();
		  }
		xml += "</CollectLists>";
		
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
		int totalRowCount = collectLists.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalCollectAmount = 0;
		int totalDiscountAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = collectLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CollectList collectList = collectLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += collectList.toXML();
			} else {
				xml +=  collectList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CollectLists>" + new String(xml) + "</CollectLists>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalCollectAmount += new Integer(collectList.getCollectAmount()).intValue();
			} catch (Exception e){
				totalCollectAmount += 0;
				e.printStackTrace();
			}
			try{
				totalDiscountAmount += new Integer(collectList.getDiscountAmount()).intValue();
			} catch (Exception e){
				totalDiscountAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CollectLists>" + new String(xml) + "</CollectLists>");
		}
		this.setTotalCollectAmount("" + totalCollectAmount);
		this.setTotalDiscountAmount("" + totalDiscountAmount);
		return pageXML;
	}
	
}
