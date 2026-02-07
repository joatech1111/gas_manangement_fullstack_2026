package com.joainfo.gasmax.bean.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerSaftyCheckList;

/**
 * 안전점검 정보의 해시 집합
 * @version 1.0
 */
public class CustomerSaftyCheckListMap {

	/**
	 * CustomerSaftyCheckList 목록
	 */
	private LinkedHashMap<String, CustomerSaftyCheckList> customerSaftyCheckLists;
	
	/**
	 * 마지막 데이터의 키
	 */
	private String latestKey = "";
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerSaftyCheckListMap(){
		if (customerSaftyCheckLists == null) {
			customerSaftyCheckLists = new LinkedHashMap<String, CustomerSaftyCheckList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerSaftyCheckList> getCustomerSaftyCheckLists(){
		return customerSaftyCheckLists;
	}
	
	/**
	 * @param customerSaftyChecks
	 */
	public void setCustomerSaftyCheckLists(LinkedHashMap<String, CustomerSaftyCheckList> customerSaftyCheckLists){
		this.customerSaftyCheckLists = customerSaftyCheckLists;
	}
	
	/**
	 * @param id
	 * @return CustomerSaftyCheck
	 */
	public CustomerSaftyCheckList getCustomerSaftyCheckList(String id){
		return this.customerSaftyCheckLists.get(id);
	}
	
	/**
	 * @return CustomerSaftyCheck
	 */
	public CustomerSaftyCheckList getLatestCustomerSaftyCheckList(){
		if (("".equals(latestKey)) || (latestKey==null)){
			toXML();
		}
		if (("".equals(latestKey)) || (latestKey==null)){
			return null;
		} else {
			return this.customerSaftyCheckLists.get(latestKey);
		}
	}
	
	
	/**
	 * @param id
	 * @param customerSaftyCheck
	 */
	public void setCustomerSaftyCheckList(String id, CustomerSaftyCheckList customerSaftyCheckList){
		this.customerSaftyCheckLists.put(id, customerSaftyCheckList);
	}
	
	/**
	 * @param customerSaftyCheck
	 */
	public void setCustomerSaftyCheckList(CustomerSaftyCheckList customerSaftyCheckList){
		this.customerSaftyCheckLists.put(customerSaftyCheckList.getKeyValue(), customerSaftyCheckList);
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
	public void removeCustomerSaftyCheckList(String id){
		this.customerSaftyCheckLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerSaftyCheckLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerSaftyCheckLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerSaftyCheckLists>";
				
		java.util.Iterator<String> iterator = customerSaftyCheckLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			latestKey = key;
			xml += customerSaftyCheckLists.get(key).toXML();
		  }
		xml += "</CustomerSaftyCheckLists>";
		
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
		int totalRowCount = customerSaftyCheckLists.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerSaftyCheckLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerSaftyCheckList customerSaftyCheckList = customerSaftyCheckLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerSaftyCheckList.toXML();
			} else {
				xml +=  customerSaftyCheckList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerSaftyCheckLists>" + new String(xml) + "</CustomerSaftyCheckLists>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerSaftyCheckLists>" + new String(xml) + "</CustomerSaftyCheckLists>");
		}
		return pageXML;
	}
	
}
