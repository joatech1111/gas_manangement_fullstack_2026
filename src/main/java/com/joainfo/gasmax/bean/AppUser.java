package com.joainfo.gasmax.bean;

import java.util.LinkedHashMap;

import com.joainfo.common.util.StringUtil;
import com.joainfo.gasmax.bean.attribute.USER_REGISTER_TYPE;

/**
 * 앱 사용자 정보
 * @author 백원태
 * @version 1.0
 */
public class AppUser {
	/**
	 * 핸드폰 식별번호(MAC) - key
	 */
	private String macNumber;

	/**
	 * 다중 회사 선택
	 */
	private String areaSeq;
	
	
	/**
	 * 인증 상태 - key
	 * Y: 인증등록
	 * N: 등록요청
	 * X: 중지
	 */
	private String grantState;

	/**
	 * 핸드폰 모델명
	 */
	private String phoneModel;

	/**
	 * 핸드폰 번호
	 */
	private String mobileNumber;

	/**
	 * 서버 IP/Dns
	 */
	private String ipAddress;

	/**
	 * Database Name
	 */
	private String dbCatalogName;

	/**
	 * Database UserName
	 */
	private String dbUserID;

	/**
	 * Database PassWord
	 */
	private String dbPassword;

	/**
	 * 접속 Port
	 */
	private String port;

	/**
	 * 영업소코드
	 */
	private String areaCode;

	/**
	 * 영업소명
	 */
	private String areaName;

	/**
	 * 사용자명
	 */
	private String userId;

	/**
	 * 비밀번호
	 */
	private String password;

	/**
	 * 최종 로그인 영업소
	 */
	private String lastAreaCode;

	/**
	 * 기본사원코드
	 */
	private String employeeCode;

	/**
	 * 기본사원명
	 */
	private String employeeName;

	/**
	 * 관할주소
	 */
	private String areaAddress;

	/**
	 * 지역번호
	 */
	private String phoneAreaNumber;

	/**
	 * 전자서명 저장 경로
	 */
	private String signImagePath;

	/**
	 * 인증기간
	 */
	private String licenseDate;

	/**
	 * 등록 신청일시
	 */
	private String joinDate;

	/**
	 * 최종 로그인 시간
	 */
	private String lastLoginDate;

	/**
	 * 해지 일자
	 */
	private String expiryDate;

	/**
	 * 메뉴별 사용권한 설정
	 */
	private String menuPermission;

	/**
	 * 개별 공지사항
	 */
	private String individualNotice;

	/**
	 * 메모사항
	 */
	private String remark;
	
	/**
	 * 고압/LPG 구분 
	 * HIGH: 고압, LPG: LPG
	 */
	private String gasType;

	/**
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("HP_Number", getMacNumber());
		
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
	 * key map 반환
	 * @return
	 */
	public LinkedHashMap<String, String> getMultiKeyMap(){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("MAC_NO", getMacNumber());
		keys.put("HP_SEQ", getAreaSeq());
		
		return keys; 
	}

	/**
	 * key 값 반환
	 * @return
	 */
	public String getMultiKeyValue(){
		return StringUtil.getKeyValue(this.getMultiKeyMap()); 
	}	
	
	/**
	 * @return the macNumber
	 */
	public String getMacNumber() {
		return macNumber;
	}

	/**
	 * @param macNumber the macNumber to set
	 */
	public void setMacNumber(String macNumber) {
		this.macNumber = macNumber;
	}

	/**
	 * @return the areaSeq
	 */
	public String getAreaSeq() {
		return areaSeq;
	}

	/**
	 * @param areaSeq the areaSeq to set
	 */
	public void setAreaSeq(String areaSeq) {
		this.areaSeq = areaSeq;
	}
	
	/**
	 * @return the grantState
	 */
	public String getGrantState() {
		return grantState;
	}
	
	/**
	 * @return the name of grantState
	 */
	public String getGrantStateName() {
		return grantState==null?null:USER_REGISTER_TYPE.findByCode(grantState).getName();
	}
	
	/**
	 * @return the enum value of grantState
	 */
	public USER_REGISTER_TYPE getGrantStateEnumValue() {
		return USER_REGISTER_TYPE.findByCode(grantState);
	}

	/**
	 * @param grantState the grantState to set
	 */
	public void setGrantState(String grantState) {
		this.grantState = grantState;
	}
	
	/**
	 * @param grantState the enum value of grantState to set
	 */
	public void setGrantState(USER_REGISTER_TYPE grantState) {
		this.grantState = grantState==null?null:grantState.getCode();
	}

	/**
	 * @return the phoneModel
	 */
	public String getPhoneModel() {
		return phoneModel;
	}

	/**
	 * @param phoneModel the phoneModel to set
	 */
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return DB 접속 키 ("ip;port;dbName;user;password" 형식)
	 *         JdbcUtil에서 동적으로 SqlMapConfig XML을 생성할 때 사용
	 */
	public String getIpAddress() {
		return ipAddress + ";" + port + ";" + dbCatalogName + ";" + dbUserID + ";" + dbPassword;
	}
	
	/**
	 * @return the ipAddress
	 */
	public String getOnlyIpAddress() {
		return ipAddress;
	}
	
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setOnlyIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the dbCatalogName
	 */
	public String getDbCatalogName() {
		return dbCatalogName;
	}

	/**
	 * @param dbCatalogName the dbCatalogName to set
	 */
	public void setDbCatalogName(String dbCatalogName) {
		this.dbCatalogName = dbCatalogName;
	}

	/**
	 * @return the dbUserID
	 */
	public String getDbUserID() {
		return dbUserID;
	}

	/**
	 * @param dbUserID the dbUserID to set
	 */
	public void setDbUserID(String dbUserID) {
		this.dbUserID = dbUserID;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the lastAreaCode
	 */
	public String getLastAreaCode() {
		return lastAreaCode;
	}

	/**
	 * @param lastAreaCode the lastAreaCode to set
	 */
	public void setLastAreaCode(String lastAreaCode) {
		this.lastAreaCode = lastAreaCode;
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

	/**
	 * @return the areaAddress
	 */
	public String getAreaAddress() {
		return areaAddress;
	}

	/**
	 * @param areaAddress the areaAddress to set
	 */
	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
	}

	/**
	 * @return the phoneAreaNumber
	 */
	public String getPhoneAreaNumber() {
		return phoneAreaNumber;
	}

	/**
	 * @param phoneAreaNumber the phoneAreaNumber to set
	 */
	public void setPhoneAreaNumber(String phoneAreaNumber) {
		this.phoneAreaNumber = phoneAreaNumber;
	}

	/**
	 * @return the signImagePath
	 */
	public String getSignImagePath() {
		return signImagePath;
	}

	/**
	 * @param signImagePath the signImagePath to set
	 */
	public void setSignImagePath(String signImagePath) {
		this.signImagePath = signImagePath;
	}

	/**
	 * @return the licenseDate
	 */
	public String getLicenseDate() {
		return licenseDate;
	}

	/**
	 * @param licenseDate the licenseDate to set
	 */
	public void setLicenseDate(String licenseDate) {
		this.licenseDate = licenseDate;
	}

	/**
	 * @return the joinDate
	 */
	public String getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the lastLoginDate
	 */
	public String getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * @param lastLoginDate the lastLoginDate to set
	 */
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the menuPermission
	 */
	public String getMenuPermission() {
		return menuPermission;
	}

	/**
	 * @param menuPermission the menuPermission to set
	 */
	public void setMenuPermission(String menuPermission) {
		this.menuPermission = menuPermission;
	}

	/**
	 * @return the individualNotice
	 */
	public String getIndividualNotice() {
		return individualNotice;
	}

	/**
	 * @param individualNotice the individualNotice to set
	 */
	public void setIndividualNotice(String individualNotice) {
		this.individualNotice = individualNotice;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the gasType
	 */
	public String getGasType() {
		return gasType;
	}

	/**
	 * @param gasType the gasType to set
	 */
	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppUser [key=" + this.getKeyValue() 
				+ ", macNumber=" + macNumber 
				+ ", areaSeq=" + areaSeq
				+ ", grantState=" + grantState
				+ ", grantStateName=" + this.getGrantStateName()
				+ ", phoneModel=" + phoneModel + ", mobileNumber="
				+ mobileNumber + ", ipAddress=" + ipAddress
				+ ", dbCatalogName=" + dbCatalogName + ", dbUserID=" + dbUserID
				+ ", dbPassword=" + dbPassword + ", port=" + port
				+ ", areaCode=" + areaCode + ", areaName=" + areaName
				+ ", userId=" + userId + ", password=" + password
				+ ", lastAreaCode=" + lastAreaCode + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName
				+ ", areaAddress=" + areaAddress + ", phoneAreaNumber="
				+ phoneAreaNumber + ", signImagePath=" + signImagePath
				+ ", licenseDate=" + licenseDate + ", joinDate=" + joinDate
				+ ", lastLoginDate=" + lastLoginDate + ", expiryDate="
				+ expiryDate + ", menuPermission=" + menuPermission
				+ ", individualNotice=" + individualNotice + ", remark="
				+ remark + ", gasType=" + gasType + "]";
	}
	


	public static String escapeXml(String input) {
		if (input == null) return "";
		return input.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&apos;");
	}



	/**
	 * XML 문자열 반환
	 * @return XML
	 */
	public String toXML() {
		String safeKey = this.getKeyValue() == null ? "" : this.getKeyValue();
		String safeMacNumber = macNumber == null ? "" : macNumber;
		String safeAreaSeq = areaSeq == null ? "" : areaSeq;
		String safeGrantState = grantState == null ? "" : grantState;
		String safeGrantStateName = this.getGrantStateName() == null ? "" : this.getGrantStateName();
		String safePhoneModel = phoneModel == null ? "" : phoneModel;
		String safeMobileNumber = mobileNumber == null ? "" : mobileNumber;
		String safeIpAddress = ipAddress == null ? "" : ipAddress;
		String safeDbCatalogName = dbCatalogName == null ? "" : dbCatalogName;
		String safeDbUserID = dbUserID == null ? "" : dbUserID;
		String safeDbPassword = dbPassword == null ? "" : dbPassword;
		String safePort = port == null ? "" : port;
		String safeAreaCode = areaCode == null ? "" : areaCode;
		String safeAreaName = areaName == null ? "" : areaName;
		String safeUserId = userId == null ? "" : userId;
		String safePassword = password == null ? "" : password;
		String safeLastAreaCode = lastAreaCode == null ? "" : lastAreaCode;
		String safeEmployeeCode = employeeCode == null ? "" : employeeCode;
		String safeEmployeeName = employeeName == null ? "" : employeeName;
		String safeAreaAddress = areaAddress == null ? "" : areaAddress;
		String safePhoneAreaNumber = phoneAreaNumber == null ? "" : phoneAreaNumber;
		String safeSignImagePath = signImagePath == null ? "" : signImagePath;
		String safeLicenseDate = licenseDate == null ? "" : licenseDate;
		String safeJoinDate = joinDate == null ? "" : joinDate;
		String safeLastLoginDate = lastLoginDate == null ? "" : lastLoginDate;
		String safeExpiryDate = expiryDate == null ? "" : expiryDate;
		String safeMenuPermission = menuPermission == null ? "" : menuPermission;
		String safeIndividualNotice = individualNotice == null ? "" : individualNotice;
		String safeRemark = remark == null ? "" : remark;
		String safeGasType = gasType == null ? "" : gasType;

		return "<AppUser><key>" + safeKey
				+ "</key><macNumber>" + safeMacNumber
				+ "</macNumber><areaSeq>" + safeAreaSeq
				+ "</areaSeq><grantState>" + safeGrantState
				+ "</grantState><grantStateName>" + safeGrantStateName
				+ "</grantStateName><phoneModel>" + safePhoneModel
				+ "</phoneModel><mobileNumber>" + safeMobileNumber
				+ "</mobileNumber><ipAddress>" + safeIpAddress
				+ "</ipAddress><dbCatalogName>" + safeDbCatalogName
				+ "</dbCatalogName><dbUserID>" + safeDbUserID
				+ "</dbUserID><dbPassword>" + safeDbPassword
				+ "</dbPassword><port>" + safePort
				+ "</port><areaCode>" + safeAreaCode
				+ "</areaCode><areaName><![CDATA[" + safeAreaName + "]]></areaName><userId>"
				+ safeUserId + "</userId><password>" + safePassword
				+ "</password><lastAreaCode>" + safeLastAreaCode
				+ "</lastAreaCode><employeeCode>" + safeEmployeeCode
				+ "</employeeCode><employeeName>" + safeEmployeeName
				+ "</employeeName><areaAddress>" + safeAreaAddress
				+ "</areaAddress><phoneAreaNumber>" + safePhoneAreaNumber
				+ "</phoneAreaNumber><signImagePath>" + safeSignImagePath
				+ "</signImagePath><licenseDate>" + safeLicenseDate
				+ "</licenseDate><joinDate>" + safeJoinDate
				+ "</joinDate><lastLoginDate>" + safeLastLoginDate
				+ "</lastLoginDate><expiryDate>" + safeExpiryDate
				+ "</expiryDate><menuPermission>" + safeMenuPermission
				+ "</menuPermission><individualNotice>" + safeIndividualNotice
				+ "</individualNotice><remark>" + safeRemark
				+ "</remark><gasType>" + safeGasType
				+ "</gasType></AppUser>";
	}


	public String toMultiString() {
		return "AppUser [key=" + this.getMultiKeyValue() 
				+ ", macNumber=" + macNumber 
				+ ", areaSeq=" + areaSeq
				+ ", grantState=" + grantState
				+ ", grantStateName=" + this.getGrantStateName()
				+ ", phoneModel=" + phoneModel + ", mobileNumber="
				+ mobileNumber + ", ipAddress=" + ipAddress
				+ ", dbCatalogName=" + dbCatalogName + ", dbUserID=" + dbUserID
				+ ", dbPassword=" + dbPassword + ", port=" + port
				+ ", areaCode=" + areaCode + ", areaName=" + areaName
				+ ", userId=" + userId + ", password=" + password
				+ ", lastAreaCode=" + lastAreaCode + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName
				+ ", areaAddress=" + areaAddress + ", phoneAreaNumber="
				+ phoneAreaNumber + ", signImagePath=" + signImagePath
				+ ", licenseDate=" + licenseDate + ", joinDate=" + joinDate
				+ ", lastLoginDate=" + lastLoginDate + ", expiryDate="
				+ expiryDate + ", menuPermission=" + menuPermission
				+ ", individualNotice=" + individualNotice + ", remark="
				+ remark + ", gasType=" + gasType + "]";
	}
	
	
	public String toMultiXML() {
		return "<AppUser><key>" + this.getMultiKeyValue()
				+ "</key><macNumber>" + macNumber 
				+ "</macNumber><areaSeq>" + areaSeq 
				+ "</areaSeq><grantState>" + grantState 
				+ "</grantState><grantStateName>" + this.getGrantStateName() 
				+ "</grantStateName><phoneModel>" + phoneModel
				+ "</phoneModel><mobileNumber>" + mobileNumber
				+ "</mobileNumber><ipAddress>" + ipAddress
				+ "</ipAddress><dbCatalogName>" + dbCatalogName
				+ "</dbCatalogName><dbUserID>" + dbUserID
				+ "</dbUserID><dbPassword>" + dbPassword
				+ "</dbPassword><port>" + port + "</port><areaCode>" + areaCode
				+ "</areaCode><areaName><![CDATA[" + areaName + "]]></areaName><userId>"
				+ userId + "</userId><password>" + password
				+ "</password><lastAreaCode>" + lastAreaCode
				+ "</lastAreaCode><employeeCode>" + employeeCode
				+ "</employeeCode><employeeName>" + employeeName
				+ "</employeeName><areaAddress>" + areaAddress
				+ "</areaAddress><phoneAreaNumber>" + phoneAreaNumber
				+ "</phoneAreaNumber><signImagePath>" + signImagePath
				+ "</signImagePath><licenseDate>" + licenseDate
				+ "</licenseDate><joinDate>" + joinDate
				+ "</joinDate><lastLoginDate>" + lastLoginDate
				+ "</lastLoginDate><expiryDate>" + expiryDate
				+ "</expiryDate><menuPermission>" + menuPermission
				+ "</menuPermission><individualNotice>" + individualNotice
				+ "</individualNotice><remark>" + remark
				+ "</remark><gasType>" + gasType
				+ "</gasType></AppUser>";
	}
	
}
