package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.CustomerWeightCollect;

/**
 * 거래처별 일반장부 - 수금내역 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class CustomerWeightCollectMap {

	/**
	 * CustomerWeightCollect 목록
	 */
	private LinkedHashMap<String, CustomerWeightCollect> customerWeightCollects;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 이월 잔액
	 */
	private String carriedOverAmount = "0";
	
	
	/**
	 * 디폴트 생성자
	 */
	public CustomerWeightCollectMap(){
		if (customerWeightCollects == null) {
			customerWeightCollects = new LinkedHashMap<String, CustomerWeightCollect>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, CustomerWeightCollect> getCustomerWeightCollects(){
		return customerWeightCollects;
	}
	
	/**
	 * @param customerWeightCollects
	 */
	public void setCustomerWeightCollects(LinkedHashMap<String, CustomerWeightCollect> customerWeightCollects){
		this.customerWeightCollects = customerWeightCollects;
	}
	
	/**
	 * @param id
	 * @return CustomerWeightCollect
	 */
	public CustomerWeightCollect getCustomerWeightCollect(String id){
		return this.customerWeightCollects.get(id);
	}
	
	/**
	 * @param id
	 * @param customerWeightCollect
	 */
	public void setCustomerWeightCollect(String id, CustomerWeightCollect customerWeightCollect){
		this.customerWeightCollects.put(id, customerWeightCollect);
	}
	
	/**
	 * @param customerWeightCollect
	 */
	public void setCustomerWeightCollect(CustomerWeightCollect customerWeightCollect){
		this.customerWeightCollects.put(customerWeightCollect.getKeyValue(), customerWeightCollect);
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
	 * @return the carriedOverAmount
	 */
	public String getCarriedOverAmount() {
		return carriedOverAmount;
	}

	/**
	 * @param carriedOverAmount the carriedOverAmount to set
	 */
	public void setCarriedOverAmount(String carriedOverAmount) {
		this.carriedOverAmount = carriedOverAmount;
	}

	/**
	 * @param id
	 */
	public void removeCustomerWeightCollect(String id){
		this.customerWeightCollects.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.customerWeightCollects.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return customerWeightCollects.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<CustomerWeightCollects>";
				
		java.util.Iterator<String> iterator = customerWeightCollects.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += customerWeightCollects.get(key).toXML();
		  }
		xml += "</CustomerWeightCollects>";
		
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
		int totalRowCount = customerWeightCollects.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = customerWeightCollects.keySet().iterator(); 
		String xml = "";
		int remainAmount = new Integer(this.getCarriedOverAmount()).intValue();
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			CustomerWeightCollect customerWeightCollect = customerWeightCollects.get(key);
			// 잔액 계산 로직: 미수누계처리 =   이월액  +  합계금액 - 입금액 - D/C
			int totalAmount = new Integer(customerWeightCollect.getTotalAmount()).intValue();
			if ("5".equals(customerWeightCollect.getTypeCode())) { // 거래 유형이 수금일 때 합계금액은 0으로 처리
				customerWeightCollect.setTotalAmount("0");
				totalAmount = 0;
			}
			int collectAmount = new Integer(customerWeightCollect.getCollectAmount()).intValue();
			int discountAmount = new Integer(customerWeightCollect.getDiscountAmount()).intValue();
//			int unpaidAmount = new Integer(customerWeightCollect.getUnpaidAmount()).intValue();
			
//			int remainAmount = new Integer(this.getCarriedOverAmount()).intValue() + totalAmount - collectAmount - discountAmount + unpaidAmount;
//			remainAmount = remainAmount - collectAmount - discountAmount + unpaidAmount;
			remainAmount = remainAmount + totalAmount - collectAmount - discountAmount;
			
			if (rowNumber < (pageNumber * rowCount)) {
				xml += customerWeightCollect.toXML().replace("</CustomerWeightCollect>", "<remainAmount>" + remainAmount + "</remainAmount></CustomerWeightCollect>");
			} else {
				xml +=  customerWeightCollect.toXML().replace("</CustomerWeightCollect>", "<remainAmount>" + remainAmount + "</remainAmount></CustomerWeightCollect>");;
				pageXML.put(new Integer(pageNumber).toString(), "<CustomerWeightCollects><totalRowCount>" + totalRowCount + "</totalRowCount><carriedOverAmount>" + this.getCarriedOverAmount() +"</carriedOverAmount>" + new String(xml) + "</CustomerWeightCollects>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<CustomerWeightCollects><totalRowCount>" + totalRowCount + "</totalRowCount><carriedOverAmount>" + this.getCarriedOverAmount() +"</carriedOverAmount>" + new String(xml) + "</CustomerWeightCollects>");
		}
		return pageXML;
	}
	
}
