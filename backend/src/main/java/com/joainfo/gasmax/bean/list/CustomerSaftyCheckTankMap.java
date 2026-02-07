package com.joainfo.gasmax.bean.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerSaftyCheckTank;

/**
 * 안전점검-저장탱크 점검 정보의 해시 집합
 * @version 1.0
 */
public class CustomerSaftyCheckTankMap {

	/**
	 * CustomerSaftyCheckTank 목록
	 */
	private LinkedHashMap<String, CustomerSaftyCheckTank> customerSaftyCheckTanks;
	
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
	public CustomerSaftyCheckTankMap(){
		if (customerSaftyCheckTanks == null) {
			customerSaftyCheckTanks = new LinkedHashMap<String, CustomerSaftyCheckTank>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerSaftyCheckTank> getCustomerSaftyCheckTanks(){
		return customerSaftyCheckTanks;
	}
	
	/**
	 * @param customerSaftyCheckTanks
	 */
	public void setCustomerSaftyCheckTanks(LinkedHashMap<String, CustomerSaftyCheckTank> customerSaftyCheckTanks){
		this.customerSaftyCheckTanks = customerSaftyCheckTanks;
	}
	
	/**
	 * @param id
	 * @return CustomerSaftyCheckTank
	 */
	public CustomerSaftyCheckTank getCustomerSaftyCheckTank(String id){
		return this.customerSaftyCheckTanks.get(id);
	}
	
	/**
	 * @return CustomerSaftyCheckTank
	 */
	public CustomerSaftyCheckTank getLatestCustomerSaftyCheckTank(){
		if (("".equals(latestKey)) || (latestKey==null)){
			toXML();
		}
		if (("".equals(latestKey)) || (latestKey==null)){
			return null;
		} else {
			return this.customerSaftyCheckTanks.get(latestKey);
		}
	}
	
	
	/**
	 * @param id
	 * @param customerSaftyCheckTank
	 */
	public void setCustomerSaftyCheckTank(String id, CustomerSaftyCheckTank customerSaftyCheckTank){
		this.customerSaftyCheckTanks.put(id, customerSaftyCheckTank);
	}
	
	/**
	 * @param customerSaftyCheckTank
	 */
	public void setCustomerSaftyCheckTank(CustomerSaftyCheckTank customerSaftyCheckTank){
		this.customerSaftyCheckTanks.put(customerSaftyCheckTank.getKeyValue(), customerSaftyCheckTank);
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
	public void removeCustomerSaftyCheckTank(String id){
		this.customerSaftyCheckTanks.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerSaftyCheckTanks.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerSaftyCheckTanks.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerSaftyCheckTanks>";
				
		java.util.Iterator<String> iterator = customerSaftyCheckTanks.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			latestKey = key;
			xml += customerSaftyCheckTanks.get(key).toXML();
		  }
		xml += "</CustomerSaftyCheckTanks>";
		
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
		int totalRowCount = customerSaftyCheckTanks.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerSaftyCheckTanks.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerSaftyCheckTank customerSaftyCheckTank = customerSaftyCheckTanks.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerSaftyCheckTank.toXML();
			} else {
				xml +=  customerSaftyCheckTank.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerSaftyCheckTanks>" + new String(xml) + "</CustomerSaftyCheckTanks>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerSaftyCheckTanks>" + new String(xml) + "</CustomerSaftyCheckTanks>");
		}
		return pageXML;
	}
	
}
