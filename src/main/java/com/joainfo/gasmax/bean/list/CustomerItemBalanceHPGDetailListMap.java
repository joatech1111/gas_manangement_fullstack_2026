package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerItemBalanceHPGDetailList;

/**
 * 거래처별 품목재고 상세내역 목록정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerItemBalanceHPGDetailListMap {

	/**
	 * CustomerItemBalanceHPGDetailList 목록
	 */
	private LinkedHashMap<String, CustomerItemBalanceHPGDetailList> customerItemBalanceHPGDetailLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 당월 금액 합계
	 */
	private String totalNowMonthAmount;
	
	/**
	 * 총 재고
	 */
	private int preBalance = 0;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerItemBalanceHPGDetailListMap(){
		if (customerItemBalanceHPGDetailLists == null) {
			customerItemBalanceHPGDetailLists = new LinkedHashMap<String, CustomerItemBalanceHPGDetailList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerItemBalanceHPGDetailList> getCustomerItemBalanceHPGDetailLists(){
		return customerItemBalanceHPGDetailLists;
	}
	
	/**
	 * @param customerItemBalanceHPGDetailLists
	 */
	public void setCustomerItemBalanceHPGDetailLists(LinkedHashMap<String, CustomerItemBalanceHPGDetailList> customerItemBalanceHPGDetailLists){
		this.customerItemBalanceHPGDetailLists = customerItemBalanceHPGDetailLists;
	}
	
	/**
	 * @param id
	 * @return CustomerItemBalanceHPGDetailList
	 */
	public CustomerItemBalanceHPGDetailList getCustomerItemBalanceHPGDetailList(String id){
		return this.customerItemBalanceHPGDetailLists.get(id);
	}
	
	/**
	 * @param id
	 * @param customerItemBalanceHPGDetailList
	 */
	public void setCustomerItemBalanceHPGDetailList(String id, CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList){
		this.customerItemBalanceHPGDetailLists.put(id, customerItemBalanceHPGDetailList);
	}
	
	/**
	 * @param customerItemBalanceHPGDetailList
	 */
	public void setCustomerItemBalanceHPGDetailList(CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList){
		this.customerItemBalanceHPGDetailLists.put(customerItemBalanceHPGDetailList.getKeyValue(), customerItemBalanceHPGDetailList);
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
	 * @return the preBalance
	 */
	public int getPreBalance() {
		return preBalance;
	}

	/**
	 * @param preBalance the preBalance to set
	 */
	public void setPreBalance(int preBalance) {
		this.preBalance = preBalance;
	}

	/**
	 * @param id
	 */
	public void removeCustomerItemBalanceHPGDetailList(String id){
		this.customerItemBalanceHPGDetailLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerItemBalanceHPGDetailLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerItemBalanceHPGDetailLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerItemBalanceHPGDetailLists>";
				
		java.util.Iterator<String> iterator = customerItemBalanceHPGDetailLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerItemBalanceHPGDetailLists.get(key).toXML();
		  }
		xml += "</CustomerItemBalanceHPGDetailLists>";
		
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
		int totalRowCount = customerItemBalanceHPGDetailLists.size();
		int currentBalance = 0;
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerItemBalanceHPGDetailLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerItemBalanceHPGDetailList customerItemBalanceHPGDetailList = customerItemBalanceHPGDetailLists.get(key);
			int outputQuantity = new Integer(customerItemBalanceHPGDetailList.getOutputQuantity()).intValue();
			int inputQuantity = new Integer(customerItemBalanceHPGDetailList.getInputQuantity()).intValue();
			currentBalance = currentBalance + outputQuantity - inputQuantity;
			int balance = preBalance + currentBalance;
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerItemBalanceHPGDetailList.toXML().replace("</CustomerItemBalanceHPGDetailList>", "<balance>" + balance + "</balance></CustomerItemBalanceHPGDetailList>");
			} else {
				xml +=  customerItemBalanceHPGDetailList.toXML().replace("</CustomerItemBalanceHPGDetailList>", "<balance>" + balance + "</balance></CustomerItemBalanceHPGDetailList>");
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerItemBalanceHPGDetailLists>" + new String(xml) + "</CustomerItemBalanceHPGDetailLists>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerItemBalanceHPGDetailLists>" + new String(xml) + "</CustomerItemBalanceHPGDetailLists>");
		}
		this.setTotalNowMonthAmount("" + totalNowMonthAmount);
		return pageXML;
	}
	
}
