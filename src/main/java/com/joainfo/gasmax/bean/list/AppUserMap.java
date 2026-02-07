package com.joainfo.gasmax.bean.list;


import java.util.HashMap;
import java.util.LinkedHashMap;

import com.joainfo.gasmax.bean.AppUser;

/**
 * 앱 사용자 정보의 해시 집합
 * @author 백원태
 * @version 1.0
 */
public class AppUserMap {

	/**
	 * AppUser 목록
	 */
	private LinkedHashMap<String, AppUser> appUsers;

	private String totalRowCount;
	
	/**
	 * 디폴트 생성자
	 */
	public AppUserMap(){
		if (appUsers == null) {
			appUsers = new LinkedHashMap<String, AppUser>();
		}
	}
	
	/**
	 * @return LinkedHashMap
	 */
	public LinkedHashMap<String, AppUser> getAppUsers(){
		return appUsers;
	}
	
	/**
	 * @param appUsers
	 */
	public void setAppUsers(LinkedHashMap<String, AppUser> appUsers){
		this.appUsers = appUsers;
	}
	
	/**
	 * @param id
	 * @return AppUser
	 */
	public AppUser getAppUser(String id){
		return this.appUsers.get(id);
	}
	
	/**
	 * @param id
	 * @param appUser
	 */
	public void setAppUser(String id, AppUser appUser){
		this.appUsers.put(id, appUser);
	}
	
	/**
	 * @param appUser
	 */
	public void setAppUser(AppUser appUser){
		this.appUsers.put(appUser.getKeyValue(), appUser);
	}
	
	/**
	 * @param id
	 */
	public void removeAppUser(String id){
		this.appUsers.remove(id);
	}
	
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
	 * @param id
	 * @return 키에 해당하는 값의 존재 여부를 반환
	 */
	public boolean isExist(String id){
		return  this.appUsers.get(id)==null?false:true;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return appUsers.toString();
	}
	
	/**
	 * @return XML
	 */
	public String toXML(){
		String xml = "<AppUsers>";
				
		java.util.Iterator<String> iterator = appUsers.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += appUsers.get(key).toXML();
		  }
		xml += "</AppUsers>";
		
		return xml; 
	}
	
	public HashMap<String, String> toPagingXML(int rowCount){
		int pageNumber = 1;
		int rowNumber = 1;
		int totalRowCount = appUsers.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = appUsers.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			AppUser appUser = appUsers.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += appUser.toXML();
			} else {
				xml +=  appUser.toXML();
				pageXML.put(new Integer(pageNumber).toString(), "<AppUsers>" + new String(xml) + "</AppUsers>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<AppUsers>" + new String(xml) + "</AppUsers>");
		}
		return pageXML;
	}
	
	public String toMultiXML(){
		String xml = "<AppUsers>";
				
		java.util.Iterator<String> iterator = appUsers.keySet().iterator(); 
		
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			xml += appUsers.get(key).toMultiXML();
		  }
		xml += "</AppUsers>";
		return xml; 
	}
	
	public HashMap<String, String> toPagingMultiXML(int rowCount){
		int pageNumber = 1;
		int rowNumber = 1;
		int totalRowCount = appUsers.size();
		this.setTotalRowCount("" + totalRowCount);
		HashMap<String, String> pageXML = new HashMap<String, String>();
		java.util.Iterator<String> iterator = appUsers.keySet().iterator(); 
		String xml = "";
		while (iterator.hasNext()) { 
			String key = iterator.next(); 
			AppUser appUser = appUsers.get(key);
			if (rowNumber < (pageNumber * rowCount)) {
				xml += appUser.toMultiXML();
			} else {
				xml +=  appUser.toMultiXML();
				pageXML.put(new Integer(pageNumber).toString(), "<AppUsers>" + new String(xml) + "</AppUsers>");
				xml = "";
				pageNumber ++;
			}
			rowNumber ++;
		}
		if (!"".equals(xml)){
			pageXML.put(new Integer(pageNumber).toString(),  "<AppUsers><totalRowCount>" + totalRowCount + "</totalRowCount>" + new String(xml) + "</AppUsers>");
		}

		return pageXML;
	}	
	
	
}
