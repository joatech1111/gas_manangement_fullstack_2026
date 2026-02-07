package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.ReadMeterList;

/**
 * 검침현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class ReadMeterListMap {

	/**
	 * ReadMeterList 목록
	 */
	private LinkedHashMap<String, ReadMeterList> readMeterLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 당월금액 합계
	 */
	private String totalNowAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public ReadMeterListMap(){
		if (readMeterLists == null) {
			readMeterLists = new LinkedHashMap<String, ReadMeterList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, ReadMeterList> getReadMeterLists(){
		return readMeterLists;
	}
	
	/**
	 * @param readMeterLists
	 */
	public void setReadMeterLists(LinkedHashMap<String, ReadMeterList> readMeterLists){
		this.readMeterLists = readMeterLists;
	}
	
	/**
	 * @param id
	 * @return ReadMeterList
	 */
	public ReadMeterList getReadMeterList(String id){
		return this.readMeterLists.get(id);
	}
	
	/**
	 * @param id
	 * @param readMeterList
	 */
	public void setReadMeterList(String id, ReadMeterList readMeterList){
		this.readMeterLists.put(id, readMeterList);
	}
	
	/**
	 * @param readMeterList
	 */
	public void setReadMeterList(ReadMeterList readMeterList){
		this.readMeterLists.put(readMeterList.getKeyValue(), readMeterList);
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
	 * @return the totalNowAmount
	 */
	public String getTotalNowAmount() {
		return totalNowAmount;
	}

	/**
	 * @param totalNowAmount the totalNowAmount to set
	 */
	public void setTotalNowAmount(String totalNowAmount) {
		this.totalNowAmount = totalNowAmount;
	}

	/**
	 * @param id
	 */
	public void removeReadMeterList(String id){
		this.readMeterLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.readMeterLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return readMeterLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<ReadMeterLists>";
				
		java.util.Iterator<String> iterator = readMeterLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += readMeterLists.get(key).toXML();
		  }
		xml += "</ReadMeterLists>";
		
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
		int totalRowCount = readMeterLists.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalNowAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = readMeterLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			ReadMeterList readMeterList = readMeterLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += readMeterList.toXML();
			} else {
				xml +=  readMeterList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<ReadMeterLists>" + new String(xml) + "</ReadMeterLists>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalNowAmount += new Integer(readMeterList.getNowAmount()).intValue();
			} catch (Exception e){
				totalNowAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<ReadMeterLists>" + new String(xml) + "</ReadMeterLists>");
		}
		this.setTotalNowAmount("" + totalNowAmount);
		return pageXML;
	}
	
}
