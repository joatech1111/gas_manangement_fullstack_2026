package com.joainfo.gasmax.bean.list;


import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.EmployeeCode;

/**
 * 사원 코드 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class EmployeeCodeMap {

	/**
	 * EmployeeCode 목록
	 */
	private LinkedHashMap<String, EmployeeCode> employeeCodes;
	
	/**
	 * 디폴트 생성자
	 */
	public EmployeeCodeMap(){
		if (employeeCodes == null) {
			employeeCodes = new LinkedHashMap<String, EmployeeCode>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, EmployeeCode> getEmployeeCodes(){
		return employeeCodes;
	}
	
	/**
	 * @param employeeCodes
	 */
	public void setEmployeeCoders(LinkedHashMap<String, EmployeeCode> employeeCodes){
		this.employeeCodes = employeeCodes;
	}
	
	/**
	 * @param id
	 * @return EmployeeCode
	 */
	public EmployeeCode getEmployeeCode(String id){
		return this.employeeCodes.get(id);
	}
	
	/**
	 * @param id
	 * @param employeeCode
	 */
	public void setEmployeeCode(String id, EmployeeCode employeeCode){
		this.employeeCodes.put(id, employeeCode);
	}
	
	/**
	 * @param employeeCode
	 */
	public void setEmployeeCode(EmployeeCode employeeCode){
		this.employeeCodes.put(employeeCode.getKeyValue(), employeeCode);
	}
	
	/**
	 * @param id
	 */
	public void removeEmployeeCode(String id){
		this.employeeCodes.remove(id);
	}
	
	/**
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.employeeCodes.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return employeeCodes.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<EmployeeCodes>";
				
		java.util.Iterator<String> iterator = employeeCodes.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += employeeCodes.get(key).toXML();
		  }
		xml += "</EmployeeCodes>";
		
		return xml; 
	}
}
