package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerVolumeSale;

/**
 * 거래처별 체적장부 - 공급내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeSaleMap {

	/**
	 * CustomerVolumeSale 목록
	 */
	private LinkedHashMap<String, CustomerVolumeSale> customerVolumeSales;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 공급량 합계
	 */
	private String totalSupplyQuantity;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerVolumeSaleMap(){
		if (customerVolumeSales == null) {
			customerVolumeSales = new LinkedHashMap<String, CustomerVolumeSale>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerVolumeSale> getCustomerVolumeSales(){
		return customerVolumeSales;
	}
	
	/**
	 * @param customerVolumeSales
	 */
	public void setCustomerVolumeSales(LinkedHashMap<String, CustomerVolumeSale> customerVolumeSales){
		this.customerVolumeSales = customerVolumeSales;
	}
	
	/**
	 * @param id
	 * @return CustomerVolumeSale
	 */
	public CustomerVolumeSale getCustomerVolumeSale(String id){
		return this.customerVolumeSales.get(id);
	}
	
	/**
	 * @param id
	 * @param customerVolumeSale
	 */
	public void setCustomerVolumeSale(String id, CustomerVolumeSale customerVolumeSale){
		this.customerVolumeSales.put(id, customerVolumeSale);
	}
	
	/**
	 * @param customerVolumeSale
	 */
	public void setCustomerVolumeSale(CustomerVolumeSale customerVolumeSale){
		this.customerVolumeSales.put(customerVolumeSale.getKeyValue(), customerVolumeSale);
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
	 * @return the totalSupplyQuantity
	 */
	public String getTotalSupplyQuantity() {
		return totalSupplyQuantity;
	}

	/**
	 * @param totalSupplyQuantity the totalSupplyQuantity to set
	 */
	public void setTotalSupplyQuantity(String totalSupplyQuantity) {
		this.totalSupplyQuantity = totalSupplyQuantity;
	}

	/**
	 * @param id
	 */
	public void removeCustomerVolumeSale(String id){
		this.customerVolumeSales.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerVolumeSales.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerVolumeSales.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerVolumeSales>";
				
		java.util.Iterator<String> iterator = customerVolumeSales.keySet().iterator(); 
		int accumulateSupplyQuantity = 0;
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			// 누적 공급량 세팅
			int supplyQuantity = new Integer(customerVolumeSales.get(key).getSupplyQuantity()).intValue();
			accumulateSupplyQuantity += supplyQuantity;
			customerVolumeSales.get(key).setAccumulateSupplyQuantity(""+accumulateSupplyQuantity);
			xml += customerVolumeSales.get(key).toXML();
		  }
		xml += "</CustomerVolumeSales>";
		
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
		int totalRowCount = customerVolumeSales.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalSupplyQuantity = 0;
		int accumulateSupplyQuantity = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerVolumeSales.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerVolumeSale customerVolumeSale = customerVolumeSales.get(key);
			// 누적 공급량 세팅
			int supplyQuantity = new Integer(customerVolumeSales.get(key).getSupplyQuantity()).intValue();
//			int supplyQuantity = new Integer(customerVolumeSales.get(key).getSaleQuantity()).intValue();
			accumulateSupplyQuantity += supplyQuantity;
			customerVolumeSales.get(key).setAccumulateSupplyQuantity(""+accumulateSupplyQuantity);
			
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerVolumeSale.toXML();
			} else {
				xml +=  customerVolumeSale.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerVolumeSales>" + new String(xml) + "</CustomerVolumeSales>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalSupplyQuantity += new Integer(customerVolumeSale.getSupplyQuantity()).intValue();
			} catch (Exception e){
				totalSupplyQuantity += 0;
				e.printStackTrace();
			}

			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerVolumeSales>" + new String(xml) + "</CustomerVolumeSales>");
		}
		this.setTotalSupplyQuantity("" + totalSupplyQuantity);
		return pageXML;
	}
	
}
