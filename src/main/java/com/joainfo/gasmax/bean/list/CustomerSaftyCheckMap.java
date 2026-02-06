package com.joainfo.gasmax.bean.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerSaftyCheck;

/**
 * 수금현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerSaftyCheckMap {

	/**
	 * CustomerSaftyCheck 목록
	 */
	private LinkedHashMap<String, CustomerSaftyCheck> customerSaftyChecks;
	
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
	public CustomerSaftyCheckMap(){
		if (customerSaftyChecks == null) {
			customerSaftyChecks = new LinkedHashMap<String, CustomerSaftyCheck>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerSaftyCheck> getCustomerSaftyChecks(){
		return customerSaftyChecks;
	}
	
	/**
	 * @param customerSaftyChecks
	 */
	public void setCustomerSaftyChecks(LinkedHashMap<String, CustomerSaftyCheck> customerSaftyChecks){
		this.customerSaftyChecks = customerSaftyChecks;
	}
	
	/**
	 * @param id
	 * @return CustomerSaftyCheck
	 */
	public CustomerSaftyCheck getCustomerSaftyCheck(String id){
		return this.customerSaftyChecks.get(id);
	}
	
	/**
	 * @return CustomerSaftyCheck
	 */
	public CustomerSaftyCheck getLatestCustomerSaftyCheck(){
		if (("".equals(latestKey)) || (latestKey==null)){
			toXML();
		}
		if (("".equals(latestKey)) || (latestKey==null)){
			return null;
		} else {
			return this.customerSaftyChecks.get(latestKey);
		}
	}
	
	
	/**
	 * @param id
	 * @param customerSaftyCheck
	 */
	public void setCustomerSaftyCheck(String id, CustomerSaftyCheck customerSaftyCheck){
		this.customerSaftyChecks.put(id, customerSaftyCheck);
	}
	
	/**
	 * @param customerSaftyCheck
	 */
	public void setCustomerSaftyCheck(CustomerSaftyCheck customerSaftyCheck){
		this.customerSaftyChecks.put(customerSaftyCheck.getKeyValue(), customerSaftyCheck);
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
	public void removeCustomerSaftyCheck(String id){
		this.customerSaftyChecks.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerSaftyChecks.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerSaftyChecks.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerSaftyChecks>";
				
		java.util.Iterator<String> iterator = customerSaftyChecks.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			latestKey = key;
			xml += customerSaftyChecks.get(key).toXML();
		  }
		xml += "</CustomerSaftyChecks>";
		
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
		int totalRowCount = customerSaftyChecks.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerSaftyChecks.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerSaftyCheck customerSaftyCheck = customerSaftyChecks.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerSaftyCheck.toXML();
			} else {
				xml +=  customerSaftyCheck.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerSaftyChecks>" + new String(xml) + "</CustomerSaftyChecks>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerSaftyChecks>" + new String(xml) + "</CustomerSaftyChecks>");
		}
		return pageXML;
	}
	
}
