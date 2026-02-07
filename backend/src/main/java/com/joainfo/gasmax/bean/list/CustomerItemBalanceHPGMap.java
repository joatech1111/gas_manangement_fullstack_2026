package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerItemBalanceHPG;

/**
 * 거래처별 품목재고 내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerItemBalanceHPGMap {

	/**
	 * CustomerItemBalanceHPG 목록
	 */
	private LinkedHashMap<String, CustomerItemBalanceHPG> customerItemBalanceHPGs;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 당월 금액 합계
	 */
	private String totalNowMonthAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerItemBalanceHPGMap(){
		if (customerItemBalanceHPGs == null) {
			customerItemBalanceHPGs = new LinkedHashMap<String, CustomerItemBalanceHPG>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerItemBalanceHPG> getCustomerItemBalanceHPGs(){
		return customerItemBalanceHPGs;
	}
	
	/**
	 * @param customerItemBalanceHPGs
	 */
	public void setCustomerItemBalanceHPGs(LinkedHashMap<String, CustomerItemBalanceHPG> customerItemBalanceHPGs){
		this.customerItemBalanceHPGs = customerItemBalanceHPGs;
	}
	
	/**
	 * @param id
	 * @return CustomerItemBalanceHPG
	 */
	public CustomerItemBalanceHPG getCustomerItemBalanceHPG(String id){
		return this.customerItemBalanceHPGs.get(id);
	}
	
	/**
	 * @param id
	 * @param customerItemBalanceHPG
	 */
	public void setCustomerItemBalanceHPG(String id, CustomerItemBalanceHPG customerItemBalanceHPG){
		this.customerItemBalanceHPGs.put(id, customerItemBalanceHPG);
	}
	
	/**
	 * @param customerItemBalanceHPG
	 */
	public void setCustomerItemBalanceHPG(CustomerItemBalanceHPG customerItemBalanceHPG){
		this.customerItemBalanceHPGs.put(customerItemBalanceHPG.getKeyValue(), customerItemBalanceHPG);
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
	 * @return the totalNowMonthAmount
	 */
	public String getTotalNowMonthAmount() {
		return totalNowMonthAmount;
	}

	/**
	 * @param totalNowMonthAmount the totalNowMonthAmount to set
	 */
	public void setTotalNowMonthAmount(String totalNowMonthAmount) {
		this.totalNowMonthAmount = totalNowMonthAmount;
	}

	/**
	 * @param id
	 */
	public void removeCustomerItemBalanceHPG(String id){
		this.customerItemBalanceHPGs.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerItemBalanceHPGs.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerItemBalanceHPGs.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerItemBalanceHPGs>";
				
		java.util.Iterator<String> iterator = customerItemBalanceHPGs.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerItemBalanceHPGs.get(key).toXML();
		  }
		xml += "</CustomerItemBalanceHPGs>";
		
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
		int totalRowCount = customerItemBalanceHPGs.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerItemBalanceHPGs.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerItemBalanceHPG customerItemBalanceHPG = customerItemBalanceHPGs.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerItemBalanceHPG.toXML();
			} else {
				xml +=  customerItemBalanceHPG.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerItemBalanceHPGs>" + new String(xml) + "</CustomerItemBalanceHPGs>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerItemBalanceHPGs>" + new String(xml) + "</CustomerItemBalanceHPGs>");
		}
		this.setTotalNowMonthAmount("" + totalNowMonthAmount);
		return pageXML;
	}
	
}
