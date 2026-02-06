package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerVolumeReadMeter;

/**
 * 거래처별 체적장부 - 검침내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeReadMeterMap {

	/**
	 * CustomerVolumeReadMeter 목록
	 */
	private LinkedHashMap<String, CustomerVolumeReadMeter> customerVolumeReadMeters;
	
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
	public CustomerVolumeReadMeterMap(){
		if (customerVolumeReadMeters == null) {
			customerVolumeReadMeters = new LinkedHashMap<String, CustomerVolumeReadMeter>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerVolumeReadMeter> getCustomerVolumeReadMeters(){
		return customerVolumeReadMeters;
	}
	
	/**
	 * @param customerVolumeReadMeters
	 */
	public void setCustomerVolumeReadMeters(LinkedHashMap<String, CustomerVolumeReadMeter> customerVolumeReadMeters){
		this.customerVolumeReadMeters = customerVolumeReadMeters;
	}
	
	/**
	 * @param id
	 * @return CustomerVolumeReadMeter
	 */
	public CustomerVolumeReadMeter getCustomerVolumeReadMeter(String id){
		return this.customerVolumeReadMeters.get(id);
	}
	
	/**
	 * @param id
	 * @param customerVolumeReadMeter
	 */
	public void setCustomerVolumeReadMeter(String id, CustomerVolumeReadMeter customerVolumeReadMeter){
		this.customerVolumeReadMeters.put(id, customerVolumeReadMeter);
	}
	
	/**
	 * @param customerVolumeReadMeter
	 */
	public void setCustomerVolumeReadMeter(CustomerVolumeReadMeter customerVolumeReadMeter){
		this.customerVolumeReadMeters.put(customerVolumeReadMeter.getKeyValue(), customerVolumeReadMeter);
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
	public void removeCustomerVolumeReadMeter(String id){
		this.customerVolumeReadMeters.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerVolumeReadMeters.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerVolumeReadMeters.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerVolumeReadMeters>";
				
		java.util.Iterator<String> iterator = customerVolumeReadMeters.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerVolumeReadMeters.get(key).toXML();
		  }
		xml += "</CustomerVolumeReadMeters>";
		
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
		int totalRowCount = customerVolumeReadMeters.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalNowMonthAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerVolumeReadMeters.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerVolumeReadMeter customerVolumeReadMeter = customerVolumeReadMeters.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerVolumeReadMeter.toXML();
			} else {
				xml +=  customerVolumeReadMeter.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerVolumeReadMeters>" + new String(xml) + "</CustomerVolumeReadMeters>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalNowMonthAmount += new Integer(customerVolumeReadMeter.getNowMonthAmount()).intValue();
			} catch (Exception e){
				totalNowMonthAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerVolumeReadMeters>" + new String(xml) + "</CustomerVolumeReadMeters>");
		}
		this.setTotalNowMonthAmount("" + totalNowMonthAmount);
		return pageXML;
	}
	
}
