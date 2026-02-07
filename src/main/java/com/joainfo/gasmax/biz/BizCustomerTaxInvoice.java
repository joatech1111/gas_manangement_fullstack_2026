package com.joainfo.gasmax.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.CustomerTaxInvoice;
import com.joainfo.gasmax.bean.list.CustomerTaxInvoiceMap;

/**
 * 거래처별 세금계산서 비즈니스 로직 처리 객체
 * @author 백원태
 * @version 1.0
 */
public class BizCustomerTaxInvoice {

	/**
	 * 거래처별 세금계산서 Select 쿼리의 ID
	 */
	public final String GASMAX_CUSTOMER_TAX_INVOICE_SELECT_ID = "GASMAX.CustomerTaxInvoice.Select";
	
	/**
	 * BizCustomerTaxInvoice 인스턴스
	 */
	private static BizCustomerTaxInvoice bizCustomerTaxInvoice;
	
	/**
	 * 디폴트 생성자
	 */
	public BizCustomerTaxInvoice(){
	}
	
	/**
	 * Singleton으로 BizCustomerTaxInvoice 인스턴스 생성
	 * @return bizCustomerTaxInvoice
	 */
	public static BizCustomerTaxInvoice getInstance(){
		if (bizCustomerTaxInvoice == null){
			bizCustomerTaxInvoice = new BizCustomerTaxInvoice();
		}
		return bizCustomerTaxInvoice;
	}
	
	/**
	 * 키워드로 검색한 거래처별 세금계산서 목록을 반환
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param areaCode 영업소 코드
	 * @param customerCode 거래처 코드
	 * @param startDate 검색 시작일
	 * @param endDate 검색 종료일
	 * @return customerTaxInvoices
	 */
	public CustomerTaxInvoiceMap getCustomerTaxInvoices(String serverIp, String catalogName, String areaCode, String customerCode, String startDate, String endDate){
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("areaCode", areaCode);
		condition.put("customerCode", customerCode);
		condition.put("startDate", startDate);
		condition.put("endDate", endDate);
		return selectCustomerTaxInvoices(serverIp, catalogName, condition);
	}
	
	/**
	 * 거래처별 세금계산서 조회
	 * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
	 * @param catalogName DB 카탈로그 명
	 * @param condition 검색 조건
	 * @return CustomerTaxInvoiceMap 형식의 거래처별 세금계산서 목록 반환
	 */
	public CustomerTaxInvoiceMap selectCustomerTaxInvoices(String serverIp, String catalogName, Map<String, String> condition){
		CustomerTaxInvoiceMap customerTaxInvoices = new CustomerTaxInvoiceMap();
		condition.put("catalogName", catalogName);
		
		@SuppressWarnings("rawtypes")
		List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_CUSTOMER_TAX_INVOICE_SELECT_ID, condition);
		for( HashMap<String, String> map :  list) {
			CustomerTaxInvoice customerTaxInvoice = convertCustomerTaxInvoice(map);
			customerTaxInvoices.setCustomerTaxInvoice(customerTaxInvoice.getKeyValue(), customerTaxInvoice);
		}
		return customerTaxInvoices;
	}
	
	/**
	 * HashMap을 CustomerTaxInvoice으로 변환
	 * @param map
	 * @return CustomerTaxInvoice
	 */
	protected static CustomerTaxInvoice convertCustomerTaxInvoice(HashMap<String, String> map){
		CustomerTaxInvoice customerTaxInvoice = new CustomerTaxInvoice();
		
		customerTaxInvoice.setAreaCode(map.get("areaCode"));
		customerTaxInvoice.setCollectSaleType(map.get("collectSaleType"));
		customerTaxInvoice.setCustomerCode(map.get("customerCode"));
		customerTaxInvoice.setSequenceNumber(map.get("sequenceNumber"));
		customerTaxInvoice.setSupplierRegisterNumber(map.get("supplierRegisterNumber"));
		customerTaxInvoice.setSupplierOwnerName(map.get("supplierOwnerName"));
		customerTaxInvoice.setIssueDate(map.get("issueDate"));
		customerTaxInvoice.setSerialNumber(map.get("serialNumber"));
		customerTaxInvoice.setInvoiceType(map.get("invoiceType"));
		customerTaxInvoice.setRemark(map.get("remark"));
		customerTaxInvoice.setRegisterNumberType(map.get("registerNumberType"));
		customerTaxInvoice.setRegisterNumber(map.get("registerNumber"));
		customerTaxInvoice.setSubCompanyNumber(map.get("subCompanyNumber"));
		customerTaxInvoice.setCustomerName(map.get("customerName"));
		customerTaxInvoice.setOwnerName(map.get("ownerName"));
		customerTaxInvoice.setAddress1(map.get("address1"));
		customerTaxInvoice.setAddress2(map.get("address2"));
		customerTaxInvoice.setPostalCode(map.get("postalCode"));
		customerTaxInvoice.setBusinessType(map.get("businessType"));
		customerTaxInvoice.setBusinessItem(map.get("businessItem"));
		customerTaxInvoice.setItemDate1(map.get("itemDate1"));
		customerTaxInvoice.setItemName1(map.get("itemName1"));
		customerTaxInvoice.setItemUnit1(map.get("itemUnit1"));
		customerTaxInvoice.setItemQuantity1(map.get("itemQuantity1"));
		customerTaxInvoice.setItemPrice1(map.get("itemPrice1"));
		customerTaxInvoice.setItemAmount1(map.get("itemAmount1"));
		customerTaxInvoice.setItemTax1(map.get("itemTax1"));
		customerTaxInvoice.setItemDate2(map.get("itemDate2"));
		customerTaxInvoice.setItemName2(map.get("itemName2"));
		customerTaxInvoice.setItemUnit2(map.get("itemUnit2"));
		customerTaxInvoice.setItemQuantity2(map.get("itemQuantity2"));
		customerTaxInvoice.setItemPrice2(map.get("itemPrice2"));
		customerTaxInvoice.setItemAmount2(map.get("itemAmount2"));
		customerTaxInvoice.setItemTax2(map.get("itemTax2"));
		customerTaxInvoice.setItemDate3(map.get("itemDate3"));
		customerTaxInvoice.setItemName3(map.get("itemName3"));
		customerTaxInvoice.setItemUnit3(map.get("itemUnit3"));
		customerTaxInvoice.setItemQuantity3(map.get("itemQuantity3"));
		customerTaxInvoice.setItemPrice3(map.get("itemPrice3"));
		customerTaxInvoice.setItemAmount3(map.get("itemAmount3"));
		customerTaxInvoice.setItemTax3(map.get("itemTax3"));
		customerTaxInvoice.setItemDate4(map.get("itemDate4"));
		customerTaxInvoice.setItemName4(map.get("itemName4"));
		customerTaxInvoice.setItemUnit4(map.get("itemUnit4"));
		customerTaxInvoice.setItemQuantity4(map.get("itemQuantity4"));
		customerTaxInvoice.setItemPrice4(map.get("itemPrice4"));
		customerTaxInvoice.setItemAmount4(map.get("itemAmount4"));
		customerTaxInvoice.setItemTax4(map.get("itemTax4"));
		customerTaxInvoice.setAmount(map.get("amount"));
		customerTaxInvoice.setTax(map.get("tax"));
		customerTaxInvoice.setTotalAmount(map.get("totalAmount"));
		customerTaxInvoice.setDataStartDate(map.get("dataStartDate"));
		customerTaxInvoice.setDateEndDate(map.get("dateEndDate"));
		customerTaxInvoice.setQuantityPricePrintYesNo(map.get("quantityPricePrintYesNo"));
		customerTaxInvoice.setTaxInvoiceType(map.get("taxInvoiceType"));
		customerTaxInvoice.setAmendReasonCode(map.get("amendReasonCode"));
		customerTaxInvoice.setInvoiceSerialNumber(map.get("invoiceSerialNumber"));
		customerTaxInvoice.setContactName(map.get("contactName"));
		customerTaxInvoice.setContactEmailAddress(map.get("contactEmailAddress"));
		customerTaxInvoice.setContactPhoneNumber(map.get("contactPhoneNumber"));
		customerTaxInvoice.setEdiStatusCode(map.get("ediStatusCode"));
		customerTaxInvoice.setEdiStatusName(map.get("ediStatusName"));
		customerTaxInvoice.setNtsStatusCode(map.get("ntsStatusCode"));
		customerTaxInvoice.setNtsStatusName(map.get("ntsStatusName"));
		customerTaxInvoice.setNtsIssueNumber(map.get("ntsIssueNumber"));
		customerTaxInvoice.setEdiErrorMessage(map.get("ediErrorMessage"));
		
		return customerTaxInvoice;
	}
	
	/**
	 * CustomerTaxInvoice을 HashMap으로 변환
	 * @param customerTaxInvoice
	 * @return
	 */
	protected static HashMap<String, String> convertCustomerTaxInvoice(CustomerTaxInvoice customerTaxInvoice){
		HashMap<String, String> map = new HashMap<String, String>();
		
	    map.put("areaCode", customerTaxInvoice.getAreaCode());
	    map.put("collectSaleType", customerTaxInvoice.getCollectSaleType());
	    map.put("customerCode", customerTaxInvoice.getCustomerCode());
	    map.put("sequenceNumber", customerTaxInvoice.getSequenceNumber());
	    map.put("supplierRegisterNumber", customerTaxInvoice.getSupplierRegisterNumber());
	    map.put("supplierOwnerName", customerTaxInvoice.getSupplierOwnerName());
	    map.put("issueDate", customerTaxInvoice.getIssueDate());
	    map.put("serialNumber", customerTaxInvoice.getSerialNumber());
	    map.put("invoiceType", customerTaxInvoice.getInvoiceType());
	    map.put("remark", customerTaxInvoice.getRemark());
	    map.put("registerNumberType", customerTaxInvoice.getRegisterNumberType());
	    map.put("registerNumber", customerTaxInvoice.getRegisterNumber());
	    map.put("subCompanyNumber", customerTaxInvoice.getSubCompanyNumber());
	    map.put("customerName", customerTaxInvoice.getCustomerName());
	    map.put("ownerName", customerTaxInvoice.getOwnerName());
	    map.put("address1", customerTaxInvoice.getAddress1());
	    map.put("address2", customerTaxInvoice.getAddress2());
	    map.put("postalCode", customerTaxInvoice.getPostalCode());
	    map.put("businessType", customerTaxInvoice.getBusinessType());
	    map.put("businessItem", customerTaxInvoice.getBusinessItem());
	    map.put("itemDate1", customerTaxInvoice.getItemDate1());
	    map.put("itemName1", customerTaxInvoice.getItemName1());
	    map.put("itemUnit1", customerTaxInvoice.getItemUnit1());
	    map.put("itemQuantity1", customerTaxInvoice.getItemQuantity1());
	    map.put("itemPrice1", customerTaxInvoice.getItemPrice1());
	    map.put("itemAmount1", customerTaxInvoice.getItemAmount1());
	    map.put("itemTax1", customerTaxInvoice.getItemTax1());
	    map.put("itemDate2", customerTaxInvoice.getItemDate2());
	    map.put("itemName2", customerTaxInvoice.getItemName2());
	    map.put("itemUnit2", customerTaxInvoice.getItemUnit2());
	    map.put("itemQuantity2", customerTaxInvoice.getItemQuantity2());
	    map.put("itemPrice2", customerTaxInvoice.getItemPrice2());
	    map.put("itemAmount2", customerTaxInvoice.getItemAmount2());
	    map.put("itemTax2", customerTaxInvoice.getItemTax2());
	    map.put("itemDate3", customerTaxInvoice.getItemDate3());
	    map.put("itemName3", customerTaxInvoice.getItemName3());
	    map.put("itemUnit3", customerTaxInvoice.getItemUnit3());
	    map.put("itemQuantity3", customerTaxInvoice.getItemQuantity3());
	    map.put("itemPrice3", customerTaxInvoice.getItemPrice3());
	    map.put("itemAmount3", customerTaxInvoice.getItemAmount3());
	    map.put("itemTax3", customerTaxInvoice.getItemTax3());
	    map.put("itemDate4", customerTaxInvoice.getItemDate4());
	    map.put("itemName4", customerTaxInvoice.getItemName4());
	    map.put("itemUnit4", customerTaxInvoice.getItemUnit4());
	    map.put("itemQuantity4", customerTaxInvoice.getItemQuantity4());
	    map.put("itemPrice4", customerTaxInvoice.getItemPrice4());
	    map.put("itemAmount4", customerTaxInvoice.getItemAmount4());
	    map.put("itemTax4", customerTaxInvoice.getItemTax4());
	    map.put("amount", customerTaxInvoice.getAmount());
	    map.put("tax", customerTaxInvoice.getTax());
	    map.put("totalAmount", customerTaxInvoice.getTotalAmount());
	    map.put("dataStartDate", customerTaxInvoice.getDataStartDate());
	    map.put("dateEndDate", customerTaxInvoice.getDateEndDate());
	    map.put("quantityPricePrintYesNo", customerTaxInvoice.getQuantityPricePrintYesNo());
	    map.put("taxInvoiceType", customerTaxInvoice.getTaxInvoiceType());
	    map.put("amendReasonCode", customerTaxInvoice.getAmendReasonCode());
	    map.put("invoiceSerialNumber", customerTaxInvoice.getInvoiceSerialNumber());
	    map.put("contactName", customerTaxInvoice.getContactName());
	    map.put("contactEmailAddress", customerTaxInvoice.getContactEmailAddress());
	    map.put("contactPhoneNumber", customerTaxInvoice.getContactPhoneNumber());
	    map.put("ediStatusCode", customerTaxInvoice.getEdiStatusCode());
	    map.put("ediStatusName", customerTaxInvoice.getEdiStatusName());
	    map.put("ntsStatusCode", customerTaxInvoice.getNtsStatusCode());
	    map.put("ntsStatusName", customerTaxInvoice.getNtsStatusName());
	    map.put("ntsIssueNumber", customerTaxInvoice.getNtsIssueNumber());
	    map.put("ediErrorMessage", customerTaxInvoice.getEdiErrorMessage());
		return map;
	}
	
	/**
	 * 비즈니스 로직 테스트용
	 * @param args
	 */
	public static void main(String[] args){
//		String keyword = "";
		String serverIp = "joatech2.dyndns.org";
		String catalogName = "GM_TestHigh";
		String areaCode = "01";
		String customerCode = "000-00030";
//		String customerType = "A";
//		String employeeCode = "1";
//		String grantCode = "00111111";
//		String collectTypeCode = "0";
		String startDate = "20090701";
		String endDate = "20120731";
		
		try{
			CustomerTaxInvoiceMap customerTaxInvoices = BizCustomerTaxInvoice.getInstance().getCustomerTaxInvoices(serverIp, catalogName, areaCode, customerCode, startDate, endDate);
			System.out.println(customerTaxInvoices.toXML());
		} catch (Exception e){
			e.printStackTrace();
		}
		
/* INSERT OR UPDATE*/
//		CustomerTaxInvoice customerTaxInvoice = new CustomerTaxInvoice();
//		customerTaxInvoice.setCustomerTaxInvoiceCode("TEST1");
//		customerTaxInvoice.setCustomerTaxInvoiceName("TEST CustomerTaxInvoice1");
//		customerTaxInvoice.setUseYesNo("Y");
//		BizCustomerTaxInvoice.getInstance().applyCustomerTaxInvoice(customerTaxInvoice);
		
/* DELETE */
//		BizCustomerTaxInvoice.getInstance().deleteCustomerTaxInvoice("TEST");
		
/* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizCustomerTaxInvoice.getInstance().deleteCustomerTaxInvoices(list);

/* SELECT */
//		BizCustomerTaxInvoice.getInstance().initCacheCustomerTaxInvoices();
//		System.out.println(cacheCustomerTaxInvoices.toXML());
//		

//		System.out.println(cacheCustomerTaxInvoices.toXML());
	}
}
