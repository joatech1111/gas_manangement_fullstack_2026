package com.gasmax.auth.service;

import com.gasmax.auth.model.AppUser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 회사별 DB에 동적으로 접속하는 서비스
 * 기존 JdbcUtil.getOrCreateDynamicSqlMap() 패턴을 Spring JdbcTemplate으로 재구현
 *
 * AppUser의 SVR_IP, SVR_Port, SVR_DBName, SVR_User, SVR_Pass 로
 * 회사 전용 DB에 연결한다 (GasMax_App 공용 DB와 별개).
 */
@Slf4j
@Service
public class DynamicJdbcService {

    /**
     * 기존 JdbcUtil의 sqlMapClientCache와 동일한 역할
     * key: "ip:port/dbName:user"
     */
    private final ConcurrentHashMap<String, JdbcTemplate> cache = new ConcurrentHashMap<>();

    /**
     * AppUser의 DB 접속 정보로 JdbcTemplate을 가져온다 (없으면 생성 후 캐시).
     */
    private JdbcTemplate getJdbcTemplate(AppUser user) {
        String key = user.getIpAddress() + ":" + user.getPort()
                + "/" + user.getDbCatalogName()
                + ":" + user.getDbUserId();

        return cache.computeIfAbsent(key, k -> {
            String url = "jdbc:jtds:sqlserver://"
                    + user.getIpAddress() + ":" + user.getPort()
                    + "/" + user.getDbCatalogName();

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user.getDbUserId());
            config.setPassword(user.getDbPassword());
            config.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
            config.setMaximumPoolSize(3);
            config.setConnectionTimeout(8000);
            config.setConnectionTestQuery("SELECT 1");
            config.setPoolName("dynamic-" + user.getDbCatalogName());

            log.info("[DynamicJdbc] 새 커넥션 풀 생성: {}", key);
            return new JdbcTemplate(new HikariDataSource(config));
        });
    }

    /**
     * 영업소 코드 목록
     * 기존: GASMAX.AppUser.Select.AreaCode / GASMAX.AreaCode.Select
     */
    public List<Map<String, Object>> findAreaCodes(AppUser user) {
        return getJdbcTemplate(user).queryForList(
                "SELECT AREA_CODE AS areaCode, AREA_NAME AS areaName FROM dbo.JNOTRY ORDER BY AREA_CODE"
        );
    }

    /**
     * 지역번호(DDD) 목록
     * 기존: GASMAX.AppUser.Select.PhoneAreaNumber
     */
    public List<Map<String, Object>> findPhoneAreaNumbers(AppUser user) {
        return getJdbcTemplate(user).queryForList(
                "SELECT Code AS phoneAreaNumber, CodeName AS phoneAreaName FROM dbo.BA_Code WHERE Gubun = 'DDD' ORDER BY Code"
        );
    }

    // =========================================================================
    // 거래처 품목 조회 (BizCustomerItem)
    // =========================================================================

    /**
     * 거래처 품목 조회 — gasType(세션)에 따라 HPG / LPG 쿼리 자동 분기
     * 기존: BizCustomerItem.getCustomerItemWeightHPGs / getCustomerItemWeightLPGs
     *
     * @param user         세션 사용자 (gasType, areaCode 사용)
     * @param customerCode 거래처 코드
     * @param saleType     0=가스, 1=용기  (LPG 전용; HPG 시 무시)
     * @param itemCode     단건 조회 시 품목 코드, 전체 조회 시 "" 또는 null
     */
    public List<Map<String, Object>> findCustomerItems(AppUser user, String customerCode, String saleType, String itemCode) {
        String gasType = user.getGasType() == null ? "" : user.getGasType().toUpperCase();
        if (gasType.contains("HIGH")) {
            return findCustomerItemsWeightHPG(user, customerCode, itemCode);
        } else {
            return findCustomerItemsWeightLPG(user, customerCode, saleType, itemCode);
        }
    }

    /**
     * 거래처 품목 — 일반 고압(HPG)
     * 기존: GASMAX.CustomerItem.SelectWeightHPG → dbo.fn_CUST_JP_Danga(areaCode, customerCode)
     */
    private List<Map<String, Object>> findCustomerItemsWeightHPG(AppUser user, String customerCode, String itemCode) {
        StringBuilder sql = new StringBuilder(
                "SELECT JP_Code AS itemCode" +
                "     , JP_Name AS itemName" +
                "     , ISNULL(JP_Spec,'') AS itemSpec" +
                "     , '' AS itemUnit, '0' AS capacity" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(CU_Danga_Code,0),1),'.00','') AS priceType" +
                "     , CASE CU_Danga_Code" +
                "         WHEN 0 THEN REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_Danga_0,0),1),'.00','')" +
                "         WHEN 1 THEN REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_Danga_1,0),1),'.00','')" +
                "         WHEN 2 THEN REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_Danga_2,0),1),'.00','')" +
                "       END AS salePrice" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_JAEGOCU,0),1),'.00','') AS itemBalance" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_LAST_MISU,0),1),'.00','') AS lastUnpaidAmount" +
                " FROM dbo.fn_CUST_JP_Danga(?,?)" +
                " WHERE JC_JP_STATE <> '2'"
        );
        if (itemCode != null && !itemCode.isBlank()) {
            sql.append(" AND JP_Code = ?");
            return getJdbcTemplate(user).queryForList(sql.append(" ORDER BY JP_Sort").toString(),
                    user.getAreaCode(), customerCode, itemCode);
        }
        return getJdbcTemplate(user).queryForList(sql.append(" ORDER BY JP_Sort").toString(),
                user.getAreaCode(), customerCode);
    }

    /**
     * 거래처 품목 — 일반 LPG
     * 기존: GASMAX.CustomerItem.SelectWeightLPG → dbo.fn_CUST_JP_Danga_LPG(areaCode, customerCode)
     *
     * saleType: 0=가스(단가 기준), 1=용기(통 기준)
     * 원본 iBATIS의 $saleType$ 문자열 치환 → CASE WHEN ? = 0 THEN ... 으로 파라미터화
     */
    private List<Map<String, Object>> findCustomerItemsWeightLPG(AppUser user, String customerCode, String saleType, String itemCode) {
        int st = "1".equals(saleType) ? 1 : 0;
        StringBuilder sql = new StringBuilder(
                "SELECT JP_Code AS itemCode" +
                "     , JP_Name AS itemName" +
                "     , ISNULL(JP_Spec,'') AS itemSpec" +
                "     , '' AS itemUnit, '0' AS capacity" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(CU_Danga_Code,0),1),'.00','') AS priceType" +
                "     , CASE WHEN ? = 0 THEN REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_Danga,0),1),'.000000','')" +
                "            WHEN ? = 1 THEN REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_OUTTONG,0),1),'.00','')" +
                "       END AS salePrice" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_JAEGOCU,0),1),'.00','') AS itemBalance" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_DC_Kum,0),1),'.00','') AS discountAmount" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_LAST_QTY,0),1),'.00','') AS lastSaleQuantity" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(JP_LAST_MISU,0),1),'.00','') AS lastUnpaidAmount" +
                " FROM dbo.fn_CUST_JP_Danga_LPG(?,?)" +
                " WHERE 1=1"
        );
        if (itemCode != null && !itemCode.isBlank()) {
            sql.append(" AND JP_Code = ?");
            return getJdbcTemplate(user).queryForList(
                    sql.append(" ORDER BY JP_LAST_DATE DESC, JP_Sort").toString(),
                    st, st, user.getAreaCode(), customerCode, itemCode);
        }
        return getJdbcTemplate(user).queryForList(
                sql.append(" ORDER BY JP_LAST_DATE DESC, JP_Sort").toString(),
                st, st, user.getAreaCode(), customerCode);
    }

    // =========================================================================
    // 거래처 검색 (BizCustomerSearch)
    // =========================================================================

    /**
     * 거래처 검색
     * 기존: GASMAX.CustomerSearch.Select → dbo.fn_CUST_FIND(areaCode, customerCode, keyword, type, employeeCode, grantCode)
     *
     * @param user           세션 사용자 (areaCode, employeeCode, menuPermission 사용)
     * @param keyword        검색 키워드 (이름, 주소, 전화번호 등)
     * @param customerCode   단건 조회 시 거래처 코드, 목록 조회 시 ""
     * @param searchType     0:중량, 1:체적검침, 2:체적공급, A:전체 (기본값 A)
     */
    public List<Map<String, Object>> searchCustomers(AppUser user, String keyword, String customerCode, String searchType) {
        String sql =
                "SELECT ISNULL(AREA_CODE,'') AS areaCode" +
                "     , ISNULL(CU_CODE,'') AS customerCode" +
                "     , ISNULL(CU_Type,'') AS customerType" +
                "     , ISNULL(CU_Type_Name,'') AS customerTypeName" +
                "     , ISNULL(CU_STae,'') AS customerStatusCode" +
                "     , ISNULL(CU_STae_Name,'') AS customerStatusName" +
                "     , ISNULL(CU_Name_View,'') AS customerName" +
                "     , ISNULL(CU_NAME,'') AS buildingName" +
                "     , ISNULL(CU_USERNAME,'') AS userName" +
                "     , ISNULL(CU_TEL,'') AS phoneNumber" +
                "     , ISNULL(CU_HP,'') AS mobileNumber" +
                "     , ISNULL(CU_ADDR1,'') AS address1" +
                "     , ISNULL(CU_ADDR2,'') AS address2" +
                "     , ISNULL(CU_SW_CODE,'') AS employeeCode" +
                "     , ISNULL(SW_NAME,'') AS employeeName" +
                "     , ISNULL(CU_CUTYPE,'') AS consumeTypeCode" +
                "     , ISNULL(CU_CUTYPE_Name,'') AS consumerTypeName" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(CU_JMisu,0)),'.00','') AS weightReceivable" +
                "     , REPLACE(CONVERT(VARCHAR(15),ISNULL(CU_CMisu,0)),'.00','') AS volumeReceivable" +
                "     , ISNULL(CU_MEMO,'') AS memo" +
                "     , ISNULL(CU_SukumType,'') AS paymentType" +
                "     , ISNULL(CU_SukumType_Name,'') AS paymentTypeName" +
                "     , ISNULL(CU_GumDate,'') AS readMeterDay" +
                "     , ISNULL(CU_HDate,'') AS latestSaftyCheckDate" +
                " FROM dbo.fn_CUST_FIND(?,?,?,?,?,?)" +
                " ORDER BY CU_Name_View";

        return getJdbcTemplate(user).queryForList(sql,
                user.getAreaCode(),
                customerCode == null ? "" : customerCode,
                keyword      == null ? "" : keyword,
                searchType   == null ? "A" : searchType,
                user.getEmployeeCode() == null ? "" : user.getEmployeeCode(),
                user.getMenuPermission() == null ? "" : user.getMenuPermission()
        );
    }
}
