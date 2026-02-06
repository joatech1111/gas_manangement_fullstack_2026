package com.joainfo.gasmax.bean.list;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.SaleList;

/**
 * 판매현황 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class SaleListMap {

	/**
	 * SaleList 목록
	 */
	private LinkedHashMap<String, SaleList> saleLists;
	
	/**
	 * 전체 조회 건수
	 */
	private String totalRowCount;
	
	/**
	 * 입금액 합계
	 */
	private String totalCollectAmount;
	
	/**
	 * 미수금액 합계
	 */
	private String totalUnpaidAmount;
	
	/**
	 * 할인금액 합계
	 */
	private String totalDiscountAmount;
	
	/**
	 * 디폴트 생성자
	 */
	public SaleListMap(){
		if (saleLists == null) {
			saleLists = new LinkedHashMap<String, SaleList>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, SaleList> getSaleLists(){
		return saleLists;
	}
	
	/**
	 * @param saleLists
	 */
	public void setSaleLists(LinkedHashMap<String, SaleList> saleLists){
		this.saleLists = saleLists;
	}
	
	/**
	 * @param id
	 * @return SaleList
	 */
	public SaleList getSaleList(String id){
		return this.saleLists.get(id);
	}
	
	/**
	 * @param id
	 * @param saleList
	 */
	public void setSaleList(String id, SaleList saleList){
		this.saleLists.put(id, saleList);
	}
	
	/**
	 * @param saleList
	 */
	public void setSaleList(SaleList saleList){
		this.saleLists.put(saleList.getKeyValue(), saleList);
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
	 * @return the totalUnpaidAmount
	 */
	public String getTotalUnpaidAmount() {
		return totalUnpaidAmount;
	}

	/**
	 * @param totalUnpaidAmount the totalUnpaidAmount to set
	 */
	public void setTotalUnpaidAmount(String totalUnpaidAmount) {
		this.totalUnpaidAmount = totalUnpaidAmount;
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
	public void removeSaleList(String id){
		this.saleLists.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.saleLists.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return saleLists.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<SaleLists>";
				
		java.util.Iterator<String> iterator = saleLists.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += saleLists.get(key).toXML();
		  }
		xml += "</SaleLists>";
		
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
		int totalRowCount = saleLists.size();
		this.setTotalRowCount("" + totalRowCount);
		int totalCollectAmount = 0;
		int totalUnpaidAmount = 0;
		int totalDiscountAmount = 0;
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = saleLists.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			SaleList saleList = saleLists.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += saleList.toXML();
			} else {
				xml +=  saleList.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<SaleLists>" + new String(xml) + "</SaleLists>");
				xml = "";
				pageNumber ++;
			}
			try{
				totalCollectAmount += new Integer(saleList.getCollectAmount()).intValue();
			} catch (Exception e){
				totalCollectAmount += 0;
				e.printStackTrace();
			}
			try{
				totalUnpaidAmount += new Integer(saleList.getUnpaidAmount()).intValue();
			} catch (Exception e){
				totalUnpaidAmount += 0;
				e.printStackTrace();
			}
			try{
				totalDiscountAmount += new Integer(saleList.getDiscountAmount()).intValue();
			} catch (Exception e){
				totalDiscountAmount += 0;
				e.printStackTrace();
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<SaleLists>" + new String(xml) + "</SaleLists>");
		}
		this.setTotalCollectAmount("" + totalCollectAmount);
		this.setTotalUnpaidAmount("" + totalUnpaidAmount);
		this.setTotalDiscountAmount("" + totalDiscountAmount);
		return pageXML;
	}
}
