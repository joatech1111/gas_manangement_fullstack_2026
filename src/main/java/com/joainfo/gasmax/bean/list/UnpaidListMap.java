package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.UnpaidList;

/**
 * 미수현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class UnpaidListMap {

	/**
	 * UnpaidList 목록
	 */
	private LinkedHashMap<String, UnpaidList> unpaidLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 미수금 합계
	 */
	private String totalUnpaidAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public UnpaidListMap(){
		if (unpaidLists == null) {
			unpaidLists = new LinkedHashMap<String, UnpaidList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, UnpaidList> getUnpaidLists(){
		return unpaidLists;
	}
	
	/**
	 * @param unpaidLists
	 */
	public void setUnpaidLists(LinkedHashMap<String, UnpaidList> unpaidLists){
		this.unpaidLists = unpaidLists;
	}
	
	/**
	 * @param id
	 * @return UnpaidList
	 */
	public UnpaidList getUnpaidList(String id){
		return this.unpaidLists.get(id);
	}
	
	/**
	 * @param id
	 * @param unpaidList
	 */
	public void setUnpaidList(String id, UnpaidList unpaidList){
		this.unpaidLists.put(id, unpaidList);
	}
	
	/**
	 * @param unpaidList
	 */
	public void setUnpaidList(UnpaidList unpaidList){
		this.unpaidLists.put(unpaidList.getKeyValue(), unpaidList);
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
	 * @return the totalUnpaidAmount
	 */
	public String getTotalUnpaidAmount() {
		return totalUnpaidAmount;
	}

	/**
	 * @param totalUnpaidAmount the totalUnpaidAmount to set
	 */
	public void setTotalUnpaidAmount(String totalUnpaidAmount) {
		this.totalUnpaidAmount = totalUnpaidAmount;
	}

	/**
	 * @param id
	 */
	public void removeUnpaidList(String id){
		this.unpaidLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.unpaidLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return unpaidLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<UnpaidLists>";
				
		java.util.Iterator<String> iterator = unpaidLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += unpaidLists.get(key).toXML();
		  }
		xml += "</UnpaidLists>";
		
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
		int totalRowCount = unpaidLists.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalUnpaidAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = unpaidLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			UnpaidList unpaidList = unpaidLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += unpaidList.toXML();
			} else {
				xml +=  unpaidList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<UnpaidLists>" + new String(xml) + "</UnpaidLists>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalUnpaidAmount += new Integer(unpaidList.getWeightUnpaid()).intValue() + new Integer(unpaidList.getVolumeUnpaid()).intValue();
			} catch (Exception e){
				totalUnpaidAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<UnpaidLists>" + new String(xml) + "</UnpaidLists>");
		}
		this.setTotalUnpaidAmount("" + totalUnpaidAmount);
		return pageXML;
	}
	
}
