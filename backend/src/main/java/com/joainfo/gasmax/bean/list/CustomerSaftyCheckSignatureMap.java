package com.joainfo.gasmax.bean.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerSaftyCheckSignature;

/**
 * 안전점검(서명) 정보의 해시 집합
 * @author JOATECH
 * @version 1.0
 */
public class CustomerSaftyCheckSignatureMap {

	/**
	 * CustomerSaftyCheckSignature 목록
	 */
	private LinkedHashMap<String, CustomerSaftyCheckSignature> customerSaftyCheckSignatures;
	
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
	public CustomerSaftyCheckSignatureMap(){
		if (customerSaftyCheckSignatures == null) {
			customerSaftyCheckSignatures = new LinkedHashMap<String, CustomerSaftyCheckSignature>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerSaftyCheckSignature> getCustomerSaftyCheckSignatures(){
		return customerSaftyCheckSignatures;
	}
	
	/**
	 * @param customerSaftyCheckSignatures
	 */
	public void setCustomerSaftyCheckSignatures(LinkedHashMap<String, CustomerSaftyCheckSignature> customerSaftyCheckSignatures){
		this.customerSaftyCheckSignatures = customerSaftyCheckSignatures;
	}
	
	/**
	 * @param id
	 * @return CustomerSaftyCheckSignature
	 */
	public CustomerSaftyCheckSignature getCustomerSaftyCheckSignature(String id){
		return this.customerSaftyCheckSignatures.get(id);
	}
	
	/**
	 * @return CustomerSaftyCheckSignature
	 */
	public CustomerSaftyCheckSignature getLatestCustomerSaftyCheckSignature(){
		if (("".equals(latestKey)) || (latestKey==null)){
			toXML();
		}
		if (("".equals(latestKey)) || (latestKey==null)){
			return null;
		} else {
			return this.customerSaftyCheckSignatures.get(latestKey);
		}
	}
	
	
	/**
	 * @param id
	 * @param customerSaftyCheckSignature
	 */
	public void setCustomerSaftyCheckSignature(String id, CustomerSaftyCheckSignature customerSaftyCheckSignature){
		this.customerSaftyCheckSignatures.put(id, customerSaftyCheckSignature);
	}
	
	/**
	 * @param customerSaftyCheckSignature
	 */
	public void setCustomerSaftyCheckSignature(CustomerSaftyCheckSignature customerSaftyCheckSignature){
		this.customerSaftyCheckSignatures.put(customerSaftyCheckSignature.getKeyValue(), customerSaftyCheckSignature);
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
	public void removeCustomerSaftyCheckSignature(String id){
		this.customerSaftyCheckSignatures.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerSaftyCheckSignatures.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerSaftyCheckSignatures.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerSaftyCheckSignatures>";
				
		java.util.Iterator<String> iterator = customerSaftyCheckSignatures.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			latestKey = key;
			xml += customerSaftyCheckSignatures.get(key).toXML();
		  }
		xml += "</CustomerSaftyCheckSignatures>";
		
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
		int totalRowCount = customerSaftyCheckSignatures.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerSaftyCheckSignatures.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerSaftyCheckSignature customerSaftyCheckSignature = customerSaftyCheckSignatures.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerSaftyCheckSignature.toXML();
			} else {
				xml +=  customerSaftyCheckSignature.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerSaftyCheckSignatures>" + new String(xml) + "</CustomerSaftyCheckSignatures>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerSaftyCheckSignatures>" + new String(xml) + "</CustomerSaftyCheckSignatures>");
		}
		return pageXML;
	}
	
}
