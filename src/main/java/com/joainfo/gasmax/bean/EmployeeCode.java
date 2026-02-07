package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;

/**
 * 사원 코드 정보 모델
 * SAWON
 * @author 백원태
 * @version 1.0
 */
public class EmployeeCode {

	/**
	 * 사원 코드 - key
	 */
	private String employeeCode;
	
	/**
	 * 사원 명
	 */
	private String employeeName;
	
	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("SW_CODE", getEmployeeCode());
		
		return keys; 
	}
	
	/**
	 * key 값 반환
	 * @return
	 */
	public String getKeyValue(){
		return StringUtil.getKeyValue(this.getKeyMap()); 
	}
	
	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}

	/**
	 * @param employeeCode the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmployeeCode [key=" + this.getKeyValue() 
				+ ", employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + "]";
	}

	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		return "<EmployeeCode><key>" + this.getKeyValue()
				+ "</key><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName><![CDATA[" + employeeName
				+ "]]></employeeName></EmployeeCode>";
	}
	
}
