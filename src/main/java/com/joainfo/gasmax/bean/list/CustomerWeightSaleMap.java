package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerWeightSale;

/**
 * 거래처별 일반장부 - 매출내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerWeightSaleMap {

	/**
	 * CustomerWeightSale 목록
	 */
	private LinkedHashMap<String, CustomerWeightSale> customerWeightSales;
	
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
	 * 디폴트 생성자
	 */
	public CustomerWeightSaleMap(){
		if (customerWeightSales == null) {
			customerWeightSales = new LinkedHashMap<String, CustomerWeightSale>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerWeightSale> getCustomerWeightSales(){
		return customerWeightSales;
	}
	
	/**
	 * @param customerWeightSales
	 */
	public void setCustomerWeightSales(LinkedHashMap<String, CustomerWeightSale> customerWeightSales){
		this.customerWeightSales = customerWeightSales;
	}
	
	/**
	 * @param id
	 * @return CustomerWeightSale
	 */
	public CustomerWeightSale getCustomerWeightSale(String id){
		return this.customerWeightSales.get(id);
	}
	
	/**
	 * @param id
	 * @param customerWeightSale
	 */
	public void setCustomerWeightSale(String id, CustomerWeightSale customerWeightSale){
		this.customerWeightSales.put(id, customerWeightSale);
	}
	
	/**
	 * @param customerWeightSale
	 */
	public void setCustomerWeightSale(CustomerWeightSale customerWeightSale){
		this.customerWeightSales.put(customerWeightSale.getKeyValue(), customerWeightSale);
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
	 * @param id
	 */
	public void removeCustomerWeightSale(String id){
		this.customerWeightSales.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerWeightSales.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerWeightSales.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerWeightSales>";
				
		java.util.Iterator<String> iterator = customerWeightSales.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerWeightSales.get(key).toXML();
		  }
		xml += "</CustomerWeightSales>";
		
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
		int totalRowCount = customerWeightSales.size();
		int totalSupplyAmount = 0;
		int totalTaxAmount = 0;
		int totalSumAmount = 0;
		
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerWeightSales.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerWeightSale customerWeightSale = customerWeightSales.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerWeightSale.toXML();
			} else {
				xml +=  customerWeightSale.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerWeightSales><totalRowCount>" + totalRowCount + "</totalRowCount>" + new String(xml) + "</CustomerWeightSales>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalSupplyAmount += new Integer(customerWeightSale.getSupplyAmount()).intValue();
			} catch (Exception e){
				totalSupplyAmount += 0;
				e.printStackTrace();
			}
			try{
				totalTaxAmount += new Integer(customerWeightSale.getTaxAmount()).intValue();
			} catch (Exception e){
				totalTaxAmount += 0;
				e.printStackTrace();
			}
			try{
				totalSumAmount += new Integer(customerWeightSale.getSumAmount()).intValue();
			} catch (Exception e){
				totalSumAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerWeightSales><totalRowCount>" + totalRowCount + "</totalRowCount>" + new String(xml) + "</CustomerWeightSales>");
		}
		this.setTotalSupplyAmount("" + totalSupplyAmount);
		this.setTotalTaxAmount("" + totalTaxAmount);
		this.setTotalSumAmount("" + totalSumAmount);
		return pageXML;
	}
	
}
