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
        if (appUsers == null) return null;
        // getKeyValue()가 "_uuid_areaSeq" 형태이므로 uuid만으로는 못 찾음 → 폴백 처리
        AppUser appUser = appUsers.getAppUser(uuid.toLowerCase());
        if (appUser == null && appUsers.getAppUsers() != null) {
            // uuid를 포함하는 키를 찾거나, 1건이면 바로 반환
            if (appUsers.getAppUsers().size() == 1) {
                appUser = appUsers.getAppUsers().values().iterator().next();
            } else {
                for (java.util.Map.Entry<String, AppUser> entry : appUsers.getAppUsers().entrySet()) {
                    if (entry.getKey().contains(uuid.toLowerCase())) {
                        appUser = entry.getValue();
                        break;
                    }
                }
            }
        }
        return appUser;
    }

    /**
     * uuid + hpSeq 조합으로 특정 업체의 AppUser를 정확하게 반환
     * @param catalogName DB 카탈로그명
     * @param uuid 디바이스 UUID (HP_Number)
     * @param hpSeq 업체 순번 (HP_SEQ)
     * @return 해당 업체의 AppUser, 없으면 uuid 단독으로 폴백
     */
    public AppUser getAppUserByHpSeq(String catalogName, String uuid, String hpSeq) {
        AppUserMap appUsers = selectAppUsersByHpSeq(catalogName, uuid, hpSeq);
        if (appUsers == null) return null;

        // uuid + hpSeq 조합키로 조회 (getKeyValue()가 "_uuid_hpSeq" 형태)
        String compositeKey = "_" + (uuid == null ? "" : uuid.toLowerCase()) + "_" + (hpSeq == null ? "" : hpSeq);
        AppUser appUser = appUsers.getAppUser(compositeKey);

        // 조합키로 못 찾으면 결과가 1건일 때 첫번째 반환 (폴백)
        if (appUser == null && "1".equals(appUsers.getTotalRowCount())) {
            try {
                appUser = appUsers.getAppUsers().values().iterator().next();
            } catch (Exception ignore) {}
        }

        System.out.println("getAppUserByHpSeq - uuid: " + uuid + ", hpSeq: " + hpSeq + ", found: " + (appUser != null));
        return appUser;
    }

    public AppUser getAppUser_id_pwd(String catalogName, String id, String pwd) {
        AppUserMap appUsers = getAppUsers_idPwd(catalogName, id, pwd);
        AppUser appUser = appUsers.getAppUser("test".toLowerCase());
        return appUser;
    }

    public AppUser getAppUser_id_pwd_uuid(String catalogName, String id, String pwd, String uuid) {
        AppUserMap appUsers = selectAppUsers_idPwdUUid(catalogName, id, pwd, uuid);
        if (appUsers == null) return null;
        // getKeyValue()가 "_uuid_areaSeq" 형태이므로 uuid만으로는 못 찾음 → 결과가 1건이면 바로 반환
        AppUser appUser = null;
        if (appUsers.getAppUsers() != null && appUsers.getAppUsers().size() == 1) {
            appUser = appUsers.getAppUsers().values().iterator().next();
        } else {
            // 여러 건이면 uuid로 시작하는 키를 찾아봄
            if (appUsers.getAppUsers() != null) {
                for (java.util.Map.Entry<String, AppUser> entry : appUsers.getAppUsers().entrySet()) {
                    if (entry.getKey().contains(uuid.toLowerCase())) {
                        appUser = entry.getValue();
                        break;
                    }
                }
            }
        }
        System.out.println("getAppUser_id_pwd_uuid - uuid: " + uuid + ", found: " + (appUser != null));
        return appUser;
    }

    public AppUser getAppUser_id_pwd_uuid_areaCode(String catalogName, String id, String pwd, String uuid, String areaCode) {
        AppUserMap appUsers = selectAppUsers_idPwdUUid_areaCode(catalogName, id, pwd, uuid, areaCode);
        if (appUsers == null) return null;
        // 결과가 1건이면 바로 반환 (키 형식 변경으로 인한 안전 처리)
        AppUser appUser = null;
        if (appUsers.getAppUsers() != null && appUsers.getAppUsers().size() == 1) {
            appUser = appUsers.getAppUsers().values().iterator().next();
        } else {
            // 기존 키 형태로 시도
            appUser = appUsers.getAppUser(uuid.toLowerCase() + "_" + areaCode);
        }
        System.out.println("getAppUser_id_pwd_uuid_areaCode - areaCode: " + areaCode + ", found: " + (appUser != null));
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
    /**
     * @deprecated areaSeq 없이 호출하면 UPDATE가 실행되지 않음 (HP_SEQ 필수 조건).
     *             setAppUser(macNumber, areaSeq, areaCode, ...) 버전을 사용하세요.
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
        System.out.println("[WARNING] setAppUser called WITHOUT areaSeq! macNumber=" + macNumber + " - UPDATE will not match any row.");
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put("macNumber", macNumber);
        condition.put("uuid", macNumber);
        condition.put("areaSeq", ""); // HP_SEQ 필수이므로 빈값이면 0건 업데이트됨
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

    /**
     * uuid + hpSeq로 특정 업체의 AppUser를 조회
     * SQL에 areaSeq 조건이 추가되어 정확히 해당 업체만 반환
     */
    public AppUserMap selectAppUsersByHpSeq(String catalogName, String uuid, String hpSeq) {
        try {
            AppUserMap appUsers = new AppUserMap();
            HashMap<String, String> condition = new HashMap<String, String>();
            condition.put("catalogName", catalogName);
            condition.put("uuid", uuid);
            if (hpSeq != null && !hpSeq.isEmpty() && !"0".equals(hpSeq)) {
                condition.put("areaSeq", hpSeq);
            }
            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select", condition);
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                appUsers.setAppUser(appUser.getKeyValue(), appUser);
            }
            return appUsers;
        } catch (Exception e) {
            System.out.println("Error in selectAppUsersByHpSeq: " + e.getMessage());
            e.printStackTrace();
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

            // ★ 실행 SQL 콘솔 출력
            StringBuilder debugSql = new StringBuilder();
            debugSql.append("\n========================================\n");
            debugSql.append("[selectAppUsers_idPwdUUid_hpSeq] 실행 쿼리:\n");
            debugSql.append("SELECT ... FROM ").append(catalogName).append(".dbo.AppUser\n");
            debugSql.append("WHERE 1=1\n");
            if (userId != null && !userId.isEmpty()) debugSql.append("  AND Login_User = '").append(userId).append("'\n");
            if (password != null && !password.isEmpty()) debugSql.append("  AND Login_Pass = '").append(password).append("'\n");
            if (uuid != null && !uuid.isEmpty()) debugSql.append("  AND HP_Number = '").append(uuid).append("'\n");
            if (hpSeq != null && !hpSeq.isEmpty()) debugSql.append("  AND HP_SEQ = '").append(hpSeq).append("'\n");
            debugSql.append("조건 파라미터: {catalogName=").append(catalogName)
                    .append(", userId=").append(userId)
                    .append(", password=").append(password)
                    .append(", uuid=").append(uuid)
                    .append(", hpSeq=").append(hpSeq).append("}\n");
            debugSql.append("========================================");
            System.out.println(debugSql.toString());

            @SuppressWarnings("rawtypes")
            List<HashMap> list = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).selectQuery("GASMAX.AppUser.Select_userIdPwdUUid_hpSeq", condition);

            System.out.println("[selectAppUsers_idPwdUUid_hpSeq] 조회 결과: " + (list == null ? 0 : list.size()) + "건");
            for (HashMap<String, String> map : list) {
                AppUser appUser = convertAppUser(map);
                String key = appUser.getKeyValue() + "_" + hpSeq;
                appUsers.setAppUser(key, appUser);
                System.out.println("[selectAppUsers_idPwdUUid_hpSeq] 결과 행 - key: " + key
                        + ", areaSeq(HP_SEQ): " + appUser.getAreaSeq()
                        + ", areaCode: " + appUser.getAreaCode()
                        + ", areaName: " + appUser.getAreaName()
                        + ", dbCatalogName: " + appUser.getDbCatalogName()
                        + ", serverIp: " + appUser.getOnlyIpAddress());
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
        if (appUsers == null) return null;

        // getKeyValue()가 "_uuid_hpSeq" 형태이므로, 저장 키도 같은 형태 + "_" + hpSeq → 중복 방지
        // 저장 키: appUser.getKeyValue() + "_" + hpSeq = "_uuid_areaSeq_hpSeq"
        // 조회 키도 동일하게 맞춤
        String lookupKey = "_" + (uuid == null ? "" : uuid.toLowerCase()) + "_" + (hpSeq == null ? "" : hpSeq) + "_" + hpSeq;
        AppUser appUser = appUsers.getAppUser(lookupKey);

        // 못 찾으면 결과가 1건일 때 첫번째 반환 (폴백)
        if (appUser == null && appUsers.getAppUsers() != null && appUsers.getAppUsers().size() == 1) {
            appUser = appUsers.getAppUsers().values().iterator().next();
        }

        System.out.println("getAppUser_id_pwd_uuid_hpSeq - lookupKey: " + lookupKey + ", found: " + (appUser != null));
        System.out.println("getAppUser_id_pwd_uuid_hpSeq - map keys: " + (appUsers.getAppUsers() != null ? appUsers.getAppUsers().keySet() : "null"));
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
