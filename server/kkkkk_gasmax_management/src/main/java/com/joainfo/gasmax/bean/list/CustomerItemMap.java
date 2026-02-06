package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerItem;

/**
 * 거래처 품목 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerItemMap {

	/**
	 * CustomerItem 목록
	 */
	private LinkedHashMap<String, CustomerItem> customerItems;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerItemMap(){
		if (customerItems == null) {
			customerItems = new LinkedHashMap<String, CustomerItem>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerItem> getCustomerItems(){
		return customerItems;
	}
	
	/**
	 * @param customerItems
	 */
	public void setCustomerItems(LinkedHashMap<String, CustomerItem> customerItems){
		this.customerItems = customerItems;
	}
	
	/**
	 * @param id
	 * @return CustomerItem
	 */
	public CustomerItem getCustomerItem(String id){
		return this.customerItems.get(id);
	}
	
	/**
	 * @param id
	 * @param customerItem
	 */
	public void setCustomerItem(String id, CustomerItem customerItem){
		this.customerItems.put(id, customerItem);
	}
	
	/**
	 * @param customerItem
	 */
	public void setCustomerItem(CustomerItem customerItem){
		this.customerItems.put(customerItem.getKeyValue(), customerItem);
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
	public void removeCustomerItem(String id){
		this.customerItems.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerItems.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerItems.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerItems>";
				
		java.util.Iterator<String> iterator = customerItems.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerItems.get(key).toXML();
		  }
		xml += "</CustomerItems>";
		
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
		int totalRowCount = customerItems.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerItems.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerItem customerItem = customerItems.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerItem.toXML();
			} else {
				xml +=  customerItem.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerItems>" + new String(xml) + "</CustomerItems>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerItems>" + new String(xml) + "</CustomerItems>");
		}
		return pageXML;
	}
	
}
