package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerTaxInvoice;

/**
 * 거래처별 세금계산서 내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerTaxInvoiceMap {

	/**
	 * CustomerTaxInvoice 목록
	 */
	private LinkedHashMap<String, CustomerTaxInvoice> customerTaxInvoices;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 공급액 합계
	 */
	private String totalSupplyAmount;
	
	/**
	 * 세액 합계
	 */
	private String totalTaxAmount;
	
	/**
	 * 합계금액 합계
	 */
	private String totalSumAmount;
	/**
	 * 당월 금액 합계
	 */
	private String totalNowMonthAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerTaxInvoiceMap(){
		if (customerTaxInvoices == null) {
			customerTaxInvoices = new LinkedHashMap<String, CustomerTaxInvoice>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerTaxInvoice> getCustomerTaxInvoices(){
		return customerTaxInvoices;
	}
	
	/**
	 * @param customerTaxInvoices
	 */
	public void setCustomerTaxInvoices(LinkedHashMap<String, CustomerTaxInvoice> customerTaxInvoices){
		this.customerTaxInvoices = customerTaxInvoices;
	}
	
	/**
	 * @param id
	 * @return CustomerTaxInvoice
	 */
	public CustomerTaxInvoice getCustomerTaxInvoice(String id){
		return this.customerTaxInvoices.get(id);
	}
	
	/**
	 * @param id
	 * @param customerTaxInvoice
	 */
	public void setCustomerTaxInvoice(String id, CustomerTaxInvoice customerTaxInvoice){
		this.customerTaxInvoices.put(id, customerTaxInvoice);
	}
	
	/**
	 * @param customerTaxInvoice
	 */
	public void setCustomerTaxInvoice(CustomerTaxInvoice customerTaxInvoice){
		this.customerTaxInvoices.put(customerTaxInvoice.getKeyValue(), customerTaxInvoice);
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
	public void removeCustomerTaxInvoice(String id){
		this.customerTaxInvoices.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerTaxInvoices.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerTaxInvoices.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerTaxInvoices>";
				
		java.util.Iterator<String> iterator = customerTaxInvoices.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerTaxInvoices.get(key).toXML();
		  }
		xml += "</CustomerTaxInvoices>";
		
		return xml; 
	}

	/**
	 * @return the totalSupplyAmount
	 */
	public String getTotalSupplyAmount() {
		return totalSupplyAmount;
	}

	/**
	 * @param totalSupplyAmount the totalSupplyAmount to set
	 */
	public void setTotalSupplyAmount(String totalSupplyAmount) {
		this.totalSupplyAmount = totalSupplyAmount;
	}

	/**
	 * @return the totalTaxAmount
	 */
	public String getTotalTaxAmount() {
		return totalTaxAmount;
	}

	/**
	 * @param totalTaxAmount the totalTaxAmount to set
	 */
	public void setTotalTaxAmount(String totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	/**
	 * @return the totalSumAmount
	 */
	public String getTotalSumAmount() {
		return totalSumAmount;
	}

	/**
	 * @param totalSumAmount the totalSumAmount to set
	 */
	public void setTotalSumAmount(String totalSumAmount) {
		this.totalSumAmount = totalSumAmount;
	}

	/**
	 * XML을 페이지로 나눠서 HashMap으로 반환
	 * @param rowCount 한 페이지에 들어갈 행의 개수
	 * @return 페이지로 나눠 XML을 담은 HashMap
	 */
	public HashMap<String, String> toPagingXML(int rowCount){
		int pageNumber = 1;
		int rowNumber = 1;
		int totalRowCount = customerTaxInvoices.size();
		int totalSupplyAmount = 0;
		int totalTaxAmount = 0;
		int totalSumAmount = 0;
		
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerTaxInvoices.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerTaxInvoice customerTaxInvoice = customerTaxInvoices.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerTaxInvoice.toXML();
			} else {
				xml +=  customerTaxInvoice.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerTaxInvoices>" + new String(xml) + "</CustomerTaxInvoices>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalSupplyAmount += new Integer(customerTaxInvoice.getAmount()).intValue();
			} catch (Exception e){
				totalSupplyAmount += 0;
				e.printStackTrace();
			}
			try{
				totalTaxAmount += new Integer(customerTaxInvoice.getTax()).intValue();
			} catch (Exception e){
				totalTaxAmount += 0;
				e.printStackTrace();
			}
			try{
				totalSumAmount += new Integer(customerTaxInvoice.getTotalAmount()).intValue();
			} catch (Exception e){
				totalSumAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerTaxInvoices>" + new String(xml) + "</CustomerTaxInvoices>");
		}
		this.setTotalNowMonthAmount("" + totalNowMonthAmount);
		this.setTotalSupplyAmount("" + totalSupplyAmount);
		this.setTotalTaxAmount("" + totalTaxAmount);
		this.setTotalSumAmount("" + totalSumAmount);
		return pageXML;
	}
	
}
