package com.joainfo.gasmax.biz;

import com.joainfo.common.util.jdbc.JdbcIOException;
import com.joainfo.common.util.jdbc.JdbcProcedureException;
import com.joainfo.common.util.jdbc.JdbcUtil;
import com.joainfo.gasmax.bean.AppUser;
import com.joainfo.gasmax.bean.list.AppUserMap;

import java.util.*;

/**
 * BizAppUser
 * 앱 사용자 코드 비즈니스 로직 처리 객체
 *
 * @author 백원태
 * @version 1.0
 */
public class BizAppUser {

    /**
     * 앱 사용자 코드 Select 쿼리의 ID
     */
    public final String GASMAX_APP_USER_SELECT_ID = "GASMAX.AppUser.Select";

    public final String GASMAX_MULTI_APP_USER_SELECT_ID = "GASMAX.MultiAppUser.Select";

    /**
     * 앱 사용자의 사업장 코드 Select 쿼리의 ID
     */
    public final String GASMAX_APP_USER_SELECT_AREA_CODE_ID = "GASMAX.AppUser.Select.AreaCode";

    /**
     * 전화 지역번호 Select 쿼리의 ID
     */
    public final String GASMAX_APP_USER_SELECT_PHONE_AREA_NUMBER_ID = "GASMAX.AppUser.Select.PhoneAreaNumber";

    /**
     * 앱 사용자 코드 Insert 쿼리의 ID
     */
    public final String GASMAX_APP_USER_INSERT_ID = "GASMAX.AppUser.Insert";


    /**
     * 앱 사용자 코드 UpdateLatestLoginDate 쿼리의 ID
     */
    public final String GASMAX_APP_USER_UPDATE_LATEST_LOGIN_DATE_ID = "GASMAX.AppUser.UpdateLatestLoginDate";

    /**
     * 앱 사용자 코드 Update 쿼리의 ID
     */
    public final String GASMAX_APP_USER_UPDATE_ID = "GASMAX.AppUser.Update";

    /**
     * BizAppUser 인스턴스
     */
    private static BizAppUser bizAppUser;

    /**
     * 앱 사용자 디폴트 DB 카탈로그 명
     */
    public final static String DEFAULT_APP_USER_CATATLOG_NAME = "GasMax_App";

    /**
     * 디폴트 생성자
     */
    public BizAppUser() {
    }

    /**
     * Singleton으로 BizAppUser 인스턴스 생성
     *
     * @return bizAppUser
     */
    public static BizAppUser getInstance() {
        if (bizAppUser == null) {
            bizAppUser = new BizAppUser();
        }
        return bizAppUser;
    }

    /**
     * 앱 사용자 코드 반환
     *
     * @param catalogName
     * @param key
     * @return appUser
     */
    public AppUser getAppUser(String catalogName, String key) {
        AppUserMap appUsers = getAppUsers(catalogName);
        return appUsers == null ? null : appUsers.getAppUser(key);
    }

    public AppUser getAppUser(String catalogName, String mobileNumber, String uuid) {
        AppUserMap appUsers = getAppUsers(catalogName, mobileNumber);
        AppUser appUser = appUsers.getAppUser(uuid.toLowerCase());
        return appUser;
    }

    public AppUser getAppUser_id_pwd(String catalogName, String id, String pwd) {
        AppUserMap appUsers = getAppUsers_idPwd(catalogName, id, pwd);
        AppUser appUser = appUsers.getAppUser("test".toLowerCase());
        return appUser;
    }

    public AppUser getAppUser_id_pwd_uuid(String catalogName, String id, String pwd, String uuid) {
        AppUserMap appUsers = selectAppUsers_idPwdUUid(catalogName, id, pwd, uuid);
        AppUser appUser = appUsers.getAppUser(uuid.toLowerCase());

        System.out.println(appUsers);
        System.out.println(appUsers);
        System.out.println(appUsers);
        System.out.println(appUsers);
        System.out.println(appUsers);
        return appUser;
    }

    public AppUser getAppUser_id_pwd_uuid_areaCode(String catalogName, String id, String pwd, String uuid, String areaCode) {
        AppUserMap appUsers = selectAppUsers_idPwdUUid_areaCode(catalogName, id, pwd, uuid, areaCode);
        AppUser appUser = appUsers.getAppUser(uuid.toLowerCase() + "_" + areaCode);

        System.out.println("getAppUser_id_pwd_uuid_areaCode called with areaCode: " + areaCode);
        System.out.println(appUsers);
        return appUser;
    }

    public AppUser getMultiAppUser(String catalogName, String macNumber, String key) {
        AppUserMap appUsers = getMultiAppUsers(catalogName, macNumber);
        return appUsers == null ? null : appUsers.getAppUser(key);
    }

    /**
     * 캐시로부터 앱 사용자 코드 목록을 반환
     *
     * @param catalogName
     * @return appUsers
     */
    public AppUserMap getAppUsers(String catalogName) {
        return selectAppUsers(catalogName);
    }

    public AppUserMap getAppUsers(String catalogName, String mobileNumber) {
        return selectAppUsers(catalogName, mobileNumber);
    }

    public AppUserMap getAppUsers_idPwd(String catalogName, String userId, String pwd) {
        return selectAppUsers_idPwd(catalogName, userId, pwd);
    }

    public AppUserMap getAppUsers_idPwdUUid(String catalogName, String userId, String pwd, String uuid) {
        return selectAppUsers_idPwdUUid(catalogName, userId, pwd, uuid);
    }


    public AppUserMap getMultiAppUsers(String catalogName, String macNumber) {
        return selectMultiAppUsers(catalogName, macNumber);
    }

    /**
     * @param macNumber
     * @return
     */
    public int setLatestLoginDate(String macNumber) {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("macNumber", macNumber);

        return updateLatestLoginDate(condition);
    }

    public int setLatestLoginDate(String macNumber, String areaSeq) {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("macNumber", macNumber);
        condition.put("areaSeq", areaSeq);


        return updateLatestLoginDate(condition);
    }


    /**
     * @param macNumber
     * @return
     */
    public int setAppUser(
            String macNumber
            , String areaCode
            , String areaName
            , String employeeCode
            , String employeeName
            , String phoneAreaNumber
            , String address
            , String userId
            , String password
            , String menuPermission
            , String gasType) {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("macNumber", macNumber);
        condition.put("uuid", macNumber);
        condition.put("areaCode", areaCode);
        condition.put("areaName", areaName);
        condition.put("employeeCode", employeeCode);
        condition.put("employeeName", employeeName);
        condition.put("phoneAreaNumber", phoneAreaNumber);
        condition.put("address", address);
        condition.put("userId", userId);
        condition.put("password", password);
        condition.put("menuPermission", menuPermission);
        condition.put("gasType", gasType);
        return updateAppUser(condition);
    }

    public int setAppUser(
            String macNumber
            , String areaSeq
            , String areaCode
            , String areaName
            , String employeeCode
            , String employeeName
            , String phoneAreaNumber
            , String address
            , String userId
            , String password
            , String menuPermission
            , String gasType) {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("macNumber", macNumber);
        condition.put("areaSeq", areaSeq);
        condition.put("areaCode", areaCode);
        condition.put("areaName", areaName);
        condition.put("employeeCode", employeeCode);
        condition.put("employeeName", employeeName);
        condition.put("phoneAreaNumber", phoneAreaNumber);
        condition.put("address", address);
        condition.put("userId", userId);
        condition.put("password", password);
        condition.put("menuPermission", menuPermission);
        condition.put("gasType", gasType);
        return updateAppUser(condition);
    }


    /**
     * 사업장 목록 가져오기
     *
     * @param catalogName
     * @return
     */
    public LinkedHashMap<String, String> getAreaCodes(String serverIp, String catalogName) {
        return selectAreaCodes(serverIp, catalogName);
    }

    /**
     * 전화 지역번호 목록 가져오기
     *
     * @param catalogName
     * @return
     */
    public LinkedHashMap<String, String> getPhoneAreaNumbers(String serverIp, String catalogName) {
        return selectPhoneAreaNumbers(serverIp, catalogName);
    }

    /**
     * @param macNumber
     * @param phoneModel
     * @param mobileNumber
     * @param areaName
     * @param employeeName
     * @param userId
     * @param password
     * @param phoneAreaNumber
     * @return
     * @throws JdbcIOException
     * @throws JdbcProcedureException
     */
    public String appendAppUser(String macNumber, String phoneModel, String mobileNumber, String areaName, String employeeName, String userId, String password, String phoneAreaNumber) throws JdbcIOException, JdbcProcedureException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        //Map<String, String> param = new HashMap<String, String>();
        param.put("macNumber", macNumber);
        param.put("phoneModel", phoneModel);
        param.put("mobileNumber", mobileNumber);
        param.put("areaName", areaName);
        param.put("employeeName", employeeName);
        param.put("userId", userId);
        param.put("password", password);
        param.put("phoneAreaNumber", phoneAreaNumber);
        return insertAppUser(param);
    }

    /**
     * @param param
     * @return
     * @throws JdbcIOException
     * @throws JdbcProcedureException
     */
    public String insertAppUser(HashMap<String, Object> param) throws JdbcIOException, JdbcProcedureException {
        return JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).executeProcedure("GASMAX.AppUser.Insert", param, "outputMessage"); // 처리 결과 메시지를 반환
    }

    public int insertAppUser2(Map<String, String> param) throws JdbcIOException, JdbcProcedureException {
        return JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).insertQuery("GASMAX.AppUser.Insert2", param);
    }

    /**
     * 앱 사용자 코드 조회
     *
     * @param catalogName
     * @return AppUserMap 형식의 앱 사용자 코드 목록 반환
     */
    public AppUserMap selectAppUsers(String catalogName) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public AppUserMap selectAppUsers(String catalogName, String mobileNumber) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("uuid", mobileNumber);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public AppUserMap selectAppUsers_idPwd(String catalogName, String userId, String password) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("userId", userId);
            condition.put("password", password);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select_userIdPwd", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public AppUserMap selectAppUsers_idPwdUUid(String catalogName, String userId, String password, String uuid) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("userId", userId);
            condition.put("password", password);
            condition.put("uuid", uuid);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select_userIdPwdUUid", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public AppUserMap selectAppUsers_idPwdUUid_areaCode(String catalogName, String userId, String password, String uuid, String areaCode) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("userId", userId);
            condition.put("password", password);
            condition.put("uuid", uuid);
            condition.put("areaCode", areaCode);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select_userIdPwdUUid_areaCode", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue() + "_" + areaCode, appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println("Error in selectAppUsers_idPwdUUid_areaCode: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public AppUserMap selectAppUsers_idPwdUUid_hpSeq(String catalogName, String userId, String password, String uuid, String hpSeq) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("userId", userId);
            condition.put("password", password);
            condition.put("uuid", uuid);
            condition.put("hpSeq", hpSeq);
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select_userIdPwdUUid_hpSeq", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue() + "_" + hpSeq, appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println("Error in selectAppUsers_idPwdUUid_hpSeq: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public AppUser getAppUser_id_pwd_uuid_hpSeq(String catalogName, String id, String pwd, String uuid, String hpSeq) {
        AppUserMap appUsers = selectAppUsers_idPwdUUid_hpSeq(catalogName, id, pwd, uuid, hpSeq);
        AppUser appUser = appUsers.getAppUser(uuid.toLowerCase() + "_" + hpSeq);

        System.out.println("getAppUser_id_pwd_uuid_hpSeq called with hpSeq: " + hpSeq);
        System.out.println(appUsers);
        return appUser;
    }

    protected AppUserMap selectMultiAppUsers(String catalogName, String macNumber) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("macNumber", macNumber);

            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.MultiAppUser.Select", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getMultiKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * 사업장 목록 조회
     *
     * @param catalogName
     * @return
     */
    public LinkedHashMap<String, String> selectAreaCodes(String serverIp, String catalogName) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("catalogName", catalogName);

        @SuppressWarnings("rawtypes")
        List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_APP_USER_SELECT_AREA_CODE_ID, condition);
        for (HashMap<String, String> map : list) {
            result.put(map.get("areaCode"), map.get("areaName"));
        }
        return result;
    }

    /**
     * 전화 지역번호 목록 조회
     *
     * @param catalogName
     * @return
     */
    public LinkedHashMap<String, String> selectPhoneAreaNumbers(String serverIp, String catalogName) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("catalogName", catalogName);

        @SuppressWarnings("rawtypes")
        List<HashMap> list = JdbcUtil.getInstance(serverIp).selectQuery(GASMAX_APP_USER_SELECT_PHONE_AREA_NUMBER_ID, condition);
        for (HashMap<String, String> map : list) {
            result.put(map.get("phoneAreaNumber"), map.get("phoneAreaName"));
        }
        return result;
    }

    /**
     * @param condition
     * @return
     */
    protected int updateLatestLoginDate(HashMap<String, String> condition) {
        return JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).updateQuery("GASMAX.AppUser.UpdateLatestLoginDate", condition);
    }

    /**
     * @param condition
     * @return
     */
    protected int updateAppUser(HashMap<String, String> condition) {
        return JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).updateQuery("GASMAX.AppUser.Update", condition);
    }

    /**
     * HashMap을 AppUser으로 변환
     *
     * @param map
     * @return AppUser
     */

    protected static AppUser convertAppUser(HashMap<String, String> map) {
        AppUser appUser = new AppUser();

        appUser.setMacNumber(Optional.ofNullable(map.get("macNumber")).orElse(""));
        appUser.setAreaSeq(Optional.ofNullable(map.get("areaSeq")).orElse(""));
        appUser.setGrantState(Optional.ofNullable(map.get("grantState")).orElse(""));
        appUser.setPhoneModel(Optional.ofNullable(map.get("phoneModel")).orElse(""));
        appUser.setMobileNumber(Optional.ofNullable(map.get("mobileNumber")).orElse(""));
        appUser.setOnlyIpAddress(Optional.ofNullable(map.get("ipAddress")).orElse(""));
        appUser.setDbCatalogName(Optional.ofNullable(map.get("dbCatalogName")).orElse(""));
        appUser.setDbUserID(Optional.ofNullable(map.get("dbUserID")).orElse(""));
        appUser.setDbPassword(Optional.ofNullable(map.get("dbPassword")).orElse(""));
        appUser.setPort(Optional.ofNullable(map.get("port")).orElse(""));
        appUser.setAreaCode(Optional.ofNullable(map.get("areaCode")).orElse(""));
        appUser.setAreaName(Optional.ofNullable(map.get("areaName")).orElse(""));
        appUser.setUserId(Optional.ofNullable(map.get("userId")).orElse(""));
        appUser.setPassword(Optional.ofNullable(map.get("password")).orElse(""));
        appUser.setLastAreaCode(Optional.ofNullable(map.get("lastAreaCode")).orElse(""));
        appUser.setEmployeeCode(Optional.ofNullable(map.get("employeeCode")).orElse(""));
        appUser.setEmployeeName(Optional.ofNullable(map.get("employeeName")).orElse(""));
        appUser.setAreaAddress(Optional.ofNullable(map.get("areaAddress")).orElse(""));
        appUser.setPhoneAreaNumber(Optional.ofNullable(map.get("phoneAreaNumber")).orElse(""));
        appUser.setSignImagePath(Optional.ofNullable(map.get("signImagePath")).orElse(""));
        appUser.setLicenseDate(Optional.ofNullable(map.get("licenseDate")).orElse(""));
        appUser.setJoinDate(Optional.ofNullable(map.get("joinDate")).orElse(""));
        appUser.setLastLoginDate(Optional.ofNullable(map.get("lastLoginDate")).orElse(""));
        appUser.setExpiryDate(Optional.ofNullable(map.get("expiryDate")).orElse(""));
        appUser.setMenuPermission(Optional.ofNullable(map.get("menuPermission")).orElse(""));
        appUser.setIndividualNotice(Optional.ofNullable(map.get("individualNotice")).orElse(""));
        appUser.setRemark(Optional.ofNullable(map.get("remark")).orElse(""));
        // gasType은 trim()을 호출하므로 null 체크 후 처리
        String gasType = Optional.ofNullable(map.get("gasType")).orElse("").trim();
        appUser.setGasType(gasType);

        return appUser;
    }


    protected static HashMap<String, String> convertAppUser(AppUser appUser) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("macNumber", appUser.getMacNumber());
        map.put("areaSeq", appUser.getAreaSeq());
        map.put("grantState", appUser.getGrantState());
        map.put("phoneModel", appUser.getPhoneModel());
        map.put("mobileNumber", appUser.getMobileNumber());
        map.put("ipAddress", appUser.getOnlyIpAddress());
        map.put("dbCatalogName", appUser.getDbCatalogName());
        map.put("dbUserID", appUser.getDbUserID());
        map.put("dbPassword", appUser.getDbPassword());
        map.put("port", appUser.getPort());
        map.put("areaCode", appUser.getAreaCode());
        map.put("areaName", appUser.getAreaName());
        map.put("userId", appUser.getUserId());
        map.put("password", appUser.getPassword());
        map.put("lastAreaCode", appUser.getLastAreaCode());
        map.put("employeeCode", appUser.getEmployeeCode());
        map.put("employeeName", appUser.getEmployeeName());
        map.put("areaAddress", appUser.getAreaAddress());
        map.put("phoneAreaNumber", appUser.getPhoneAreaNumber());
        map.put("signImagePath", appUser.getSignImagePath());
        map.put("licenseDate", appUser.getLicenseDate());
        map.put("joinDate", appUser.getJoinDate());
        map.put("lastLoginDate", appUser.getLastLoginDate());
        map.put("expiryDate", appUser.getExpiryDate());
        map.put("menuPermission", appUser.getMenuPermission());
        map.put("individualNotice", appUser.getIndividualNotice());
        map.put("remark", appUser.getRemark());
        map.put("gasType", appUser.getGasType());

        return map;
    }


    /**
     * 비즈니스 로직 테스트용
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
//			BizAppUser bizAppUser = BizAppUser.getInstance();
//			System.out.println(bizAppUser.getAppUsers("GasMax_App").toXML());
//			String macNumber = "1";
//			String phoneModel = "Atrix";
//			String mobileNumber = "01043322115";
//			String areaName = "조아테크";
//			String employeeName = "백원태";
//			String userId = "1";
//			String password = "1";
//			String phoneAreaNumber = "02";
//			String message = bizAppUser.appendAppUser(macNumber, phoneModel, mobileNumber, areaName, employeeName, userId, password, phoneAreaNumber);
//			System.out.println(message);
//			AppUserMap appUsers = BizAppUser.getInstance().getAppUsers(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME);
//			System.out.println(appUsers.toXML());
//
//			AppUser appUser = BizAppUser.getInstance().getAppUser(BizAppUser.DEFAULT_APP_USER_CATATLOG_NAME,  "356455042867040");
//			System.out.println(appUser.toString());
//			System.out.println(BizAppUser.getInstance().getPhoneAreaNumbers("GM_TestHigh"));
        } catch (Exception e) {
            e.printStackTrace();
        }


//		JSONObject jsonObject = JSONObject.fromObject(new HashMap<String, String>);



        /* SELECT */
//		AppUserMap appUsers = BizAppUser.getInstance().getAppUsers();
//		System.out.println(appUsers.toXML());

        /* INSERT OR UPDATE*/
//		AppUser appUser = new AppUser();
//		appUser.setAppUserCode("TEST1");
//		appUser.setAppUserName("TEST AppUser1");
//		appUser.setUseYesNo("Y");
//		BizAppUser.getInstance().applyAppUser(appUser);

        /* DELETE */
//		BizAppUser.getInstance().deleteAppUser("TEST");

        /* DELETE LIST */
//		List<String> list = new java.util.ArrayList<String>();
//		list.add("TEST1");
//		list.add("TEST2");
//		BizAppUser.getInstance().deleteAppUsers(list);

        /* SELECT */
//		BizAppUser.getInstance().initCacheAppUsers();
//		System.out.println(cacheAppUsers.toXML());
//

//		System.out.println(cacheAppUsers.toXML());
    }
}
