package com.joainfo.gasmax.bean.list;


import com.joainfo.common.util.StringUtil;
import com.joainfo.gasmax.bean.CustomerSearch;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 거래처 검색 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerSearchMap {

	/**
	 * CustomerSearch 목록
	 */
	private LinkedHashMap<String, CustomerSearch> customerSearches;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerSearchMap(){
		if (customerSearches == null) {
			customerSearches = new LinkedHashMap<String, CustomerSearch>();
		}
	}
	
	/**
	 * 키 값 가져오기
	 * @param areaCode
	 * @param customerCode
	 * @return
	 */
	public String getKey(String areaCode, String customerCode){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("AREA_CODE", areaCode);
		keys.put("CUSTOMER_CODE", customerCode);
		return StringUtil.getKeyValue(keys);
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerSearch> getCustomerSearches(){
		return customerSearches;
	}
	
	/**
	 * @param customerSearches
	 */
	public void setCustomerSearches(LinkedHashMap<String, CustomerSearch> customerSearches){
		this.customerSearches = customerSearches;
	}
	
	/**
	 * @param id
	 * @return CustomerSearch
	 */
	public CustomerSearch getCustomerSearch(String id){
		return this.customerSearches.get(id);
	}
	
	/**
	 * @param id
	 * @param customerSearch
	 */
	public void setCustomerSearch(String id, CustomerSearch customerSearch){
		this.customerSearches.put(id, customerSearch);
	}
	
	/**
	 * @param customerSearch
	 */
	public void setCustomerSearch(CustomerSearch customerSearch){
		this.customerSearches.put(customerSearch.getKeyValue(), customerSearch);
	}
	
	/**
	 * @param id
	 */
	public void removeCustomerSearch(String id){
		this.customerSearches.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerSearches.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerSearches.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerSearches>";
				
		java.util.Iterator<String> iterator = customerSearches.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerSearches.get(key).toXML();
		  }
		xml += "</CustomerSearches>";
		
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
		int totalRowCount = customerSearches.size();
		HashMap<String, String> pageXML = new HashMap<>();
		java.util.Iterator<String> iterator = customerSearches.keySet().iterator();
		StringBuilder xml = new StringBuilder();

		while (iterator.hasNext()) {
			if (rowNumber < (pageNumber * rowCount)) {
				String key = iterator.next();
				CustomerSearch customerSearch = customerSearches.get(key);
				customerSearch.setSequenceNumber(Integer.toString(totalRowCount--));

				// XML 변환 후 엔티티 처리 적용
				xml.append(escapeXml(customerSearch.toXML()));
			} else {
				String key = iterator.next();
				CustomerSearch customerSearch = customerSearches.get(key);
				customerSearch.setSequenceNumber(Integer.toString(totalRowCount--));

				// XML 변환 후 엔티티 처리 적용
				xml.append(escapeXml(customerSearch.toXML()));

				pageXML.put(Integer.toString(pageNumber), "<CustomerSearches>" + xml.toString() + "</CustomerSearches>");
				xml.setLength(0);
				pageNumber++;
			}
			rowNumber++;
		}
		if (xml.length() > 0){
			pageXML.put(Integer.toString(pageNumber), "<CustomerSearches>" + xml.toString() + "</CustomerSearches>");
		}

		return pageXML;
	}


	/**
	 * XML 특수 문자를 엔티티로 변환하는 함수
	 */
	public static String escapeXml(String input) {
		if (input == null) return "";
		return input.replace("&", "&amp;");
	}


}
