package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerVolumeCollect;

/**
 * 거래처별 체적장부 - 수금내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerVolumeCollectMap {

	/**
	 * CustomerVolumeCollect 목록
	 */
	private LinkedHashMap<String, CustomerVolumeCollect> customerVolumeCollects;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 수금액 합계
	 */
	private String totalCollectAmount;
	
	/**
	 * 할인액 합계
	 */
	private String totalDiscountAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerVolumeCollectMap(){
		if (customerVolumeCollects == null) {
			customerVolumeCollects = new LinkedHashMap<String, CustomerVolumeCollect>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerVolumeCollect> getCustomerVolumeCollects(){
		return customerVolumeCollects;
	}
	
	/**
	 * @param customerVolumeCollects
	 */
	public void setCustomerVolumeCollects(LinkedHashMap<String, CustomerVolumeCollect> customerVolumeCollects){
		this.customerVolumeCollects = customerVolumeCollects;
	}
	
	/**
	 * @param id
	 * @return CustomerVolumeCollect
	 */
	public CustomerVolumeCollect getCustomerVolumeCollect(String id){
		return this.customerVolumeCollects.get(id);
	}
	
	/**
	 * @param id
	 * @param customerVolumeCollect
	 */
	public void setCustomerVolumeCollect(String id, CustomerVolumeCollect customerVolumeCollect){
		this.customerVolumeCollects.put(id, customerVolumeCollect);
	}
	
	/**
	 * @param customerVolumeCollect
	 */
	public void setCustomerVolumeCollect(CustomerVolumeCollect customerVolumeCollect){
		this.customerVolumeCollects.put(customerVolumeCollect.getKeyValue(), customerVolumeCollect);
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
	 * @return the totalCollectAmount
	 */
	public String getTotalCollectAmount() {
		return totalCollectAmount;
	}

	/**
	 * @param totalCollectAmount the totalCollectAmount to set
	 */
	public void setTotalCollectAmount(String totalCollectAmount) {
		this.totalCollectAmount = totalCollectAmount;
	}

	/**
	 * @return the totalDiscountAmount
	 */
	public String getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	/**
	 * @param totalDiscountAmount the totalDiscountAmount to set
	 */
	public void setTotalDiscountAmount(String totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	/**
	 * @param id
	 */
	public void removeCustomerVolumeCollect(String id){
		this.customerVolumeCollects.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerVolumeCollects.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerVolumeCollects.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerVolumeCollects>";
				
		java.util.Iterator<String> iterator = customerVolumeCollects.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerVolumeCollects.get(key).toXML();
		  }
		xml += "</CustomerVolumeCollects>";
		
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
		int totalRowCount = customerVolumeCollects.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalCollectAmount = 0;
		int totalDiscountAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerVolumeCollects.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerVolumeCollect customerVolumeCollect = customerVolumeCollects.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerVolumeCollect.toXML();
			} else {
				xml +=  customerVolumeCollect.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerVolumeCollects>" + new String(xml) + "</CustomerVolumeCollects>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalCollectAmount += new Integer(customerVolumeCollect.getCollectAmount()).intValue();
			} catch (Exception e){
				totalCollectAmount += 0;
				e.printStackTrace();
			}
			try{
				totalDiscountAmount += new Integer(customerVolumeCollect.getDiscountAmount()).intValue();
			} catch (Exception e){
				totalDiscountAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerVolumeCollects>" + new String(xml) + "</CustomerVolumeCollects>");
		}
		this.setTotalCollectAmount("" + totalCollectAmount);
		this.setTotalDiscountAmount("" + totalDiscountAmount);
		return pageXML;
	}
	
}
