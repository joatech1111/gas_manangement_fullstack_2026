package com.joainfo.common.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 우편번호를 관리하는 정보 모델
 * @author 백원태
 * @version 1.0
 */
public class PostalCode {

	/**
	 * 우편번호 - key
	 */
	private String postalCode;
	
	/**
	 * 순번 - key
	 */
	private String sequenceNumber;
	
	/**
	 * 시, 도
	 */
	private String city;
	
	/**
	 * 구, 군
	 */
	private String state;
	
	/**
	 * 동, 면, 리
	 */
	private String town;
	
	/**
	 * 주소
	 */
	private String address;
	
	/**
	 * 전화 지역번호
	 */
	private String dialAreaCode;

	/**
	 * key 값 반환
	 * @return
	 */
	public String getKeyValue(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("ZIP_CD", getPostalCode());
		keys.put("ZIP_SNO", getSequenceNumber());
		
		return StringUtil.getKeyValue(keys); 
	}
	
	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the dialAreaCode
	 */
	public String getDialAreaCode() {
		return dialAreaCode;
	}

	/**
	 * @param dialAreaCode the dialAreaCode to set
	 */
	public void setDialAreaCode(String dialAreaCode) {
		this.dialAreaCode = dialAreaCode;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PostalCode [key=" + this.getKeyValue() +", postalCode=" + postalCode + ", sequenceNumber="
				+ sequenceNumber + ", city=" + city + ", state=" + state
				+ ", town=" + town + ", address=" + address + ", dialAreaCode="
				+ dialAreaCode + "]";
	}

	/**
	 * @return XML
	 */
	public String toXML(){
		return "<PostalCode><key>" + this.getKeyValue()
				+ "</key><postalCode>" + postalCode
				+ "</postalCode><sequenceNumber>" + sequenceNumber
				+ "</sequenceNumber><city>" + city + "</city><state>" + state
				+ "</state><town>" + town + "</town><address>" + address
				+ "</address><dialAreaCode>" + dialAreaCode
				+ "</dialAreaCode></PostalCode>";
	}
	
}
