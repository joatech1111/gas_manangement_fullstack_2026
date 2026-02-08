package com.joainfo.common.util.jdbc;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcUtil {
    /**
     * JdbcUtil의 인스턴스
     */
    private static JdbcUtil instance;

    /**
     * 공통으로 사용되는 디폴트 DB 설정
     */
    public static final String DEFAULT_SQL_CONFIG = "default_gasmax_app";
    public static final String DEFAULT_SQL_CONFIG_PATH = "/usr/local/tomcat/gasmax_db_config";
    public static String sqlConfigPath = "";

    /**
     * 키 값의 구분자
     */
    public static final String KEY_DELIMITER = "|";

    private SqlMapClient sqlMapClient;

    /**
     * 동적 생성된 SqlMapClient 캐시 (업체별 DB 접속 시 매번 XML 파싱을 방지)
     * key: "ip;port;dbName;user" 형식
     */
    private static final ConcurrentHashMap<String, SqlMapClient> sqlMapClientCache = new ConcurrentHashMap<>();

    /**
     * SQL Config Path를 반환하는 메서드
     */
    public String getSQLConfigPath() {
        if ("".equals(sqlConfigPath)) {
            Properties prop = new Properties();
            try {
                InputStream is = getClass().getResourceAsStream("/db_config.properties");
                if (is != null) {
                    prop.load(is);
                    is.close();
                    String path = (String) prop.get("prod");
                    if (path != null && !path.isEmpty()) {
                        java.io.File dir = new java.io.File(path);
                        if (dir.exists() && dir.isDirectory()) {
                            sqlConfigPath = path;
                            System.out.println("[JdbcUtil] DB config path (from properties): " + sqlConfigPath);
                            return sqlConfigPath;
                        } else {
                            System.out.println("[JdbcUtil] Properties path not found: " + path + " -> using default: " + DEFAULT_SQL_CONFIG_PATH);
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            sqlConfigPath = DEFAULT_SQL_CONFIG_PATH;
            System.out.println("[JdbcUtil] DB config path (default): " + sqlConfigPath);
            return sqlConfigPath;
        } else {
            return sqlConfigPath;
        }
    }

    /**
     * 디폴트 생성자
     *
     * @param serverIp 서버 아이피 또는 도메인이름.
     *                 - "default_gasmax_app" 같은 단순 이름이면 파일 기반으로 로드
     *                 - "ip;port;dbName;user" 형식이면 동적으로 XML을 생성하여 로드
     */
    private JdbcUtil(String serverIp) {
        sqlMapClient = this.getSqlMap(serverIp);
    }

    /**
     * Singleton으로 JdbcUtil 인스턴스 생성
     *
     * @param serverIp 서버 아이피 또는 도메인이름. "ip;port;dbName;user" 형식도 지원
     * @return JdbcUtil
     */
    public static JdbcUtil getInstance(String serverIp) {
        instance = new JdbcUtil(serverIp);
        return instance;
    }

    /**
     * @param serverIp 서버 아이피 또는 도메인이름
     * @return SqlMapClient
     * @throws JdbcIOException
     */
    private SqlMapClient getSqlMap(String serverIp) throws JdbcIOException {
        try {
            // "ip;port;dbName;user" 형식이면 → 동적 XML 생성 (파일 불필요)
            if (serverIp != null && serverIp.contains(";")) {
                return getOrCreateDynamicSqlMap(serverIp);
            }

            // "default_gasmax_app" 같은 단순 이름이면 → 기존 파일 기반
            Reader reader = openSqlMapConfigReader(serverIp);
            return SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JdbcIOException(e.getMessage() + "\n JDBC Connection Error.");
        }
    }

    // =========================================================================
    // 동적 XML 생성 방식 (업체별 DB - 파일 불필요)
    // =========================================================================

    /**
     * "ip;port;dbName;user" 문자열을 파싱하여 동적으로 iBatis SqlMapClient를 생성한다.
     * 동일한 접속정보에 대해서는 캐시된 SqlMapClient를 반환한다.
     *
     * @param connectionKey "ip;port;dbName;user" 형식
     * @return SqlMapClient
     */
    private SqlMapClient getOrCreateDynamicSqlMap(String connectionKey) throws JdbcIOException {
        // 캐시에 있으면 재사용 (매번 XML 파싱 방지)
        SqlMapClient cached = sqlMapClientCache.get(connectionKey);
        if (cached != null) {
            return cached;
        }

        // "ip;port;dbName;user;password" 파싱
        String[] parts = connectionKey.split(";");
        if (parts.length < 4) {
            throw new JdbcIOException("Invalid connection key format. Expected 'ip;port;dbName;user;password', got: " + connectionKey);
        }

        String ip = parts[0].trim();
        String port = parts[1].trim();
        String dbName = parts[2].trim();
        String user = parts[3].trim();
        String password = (parts.length >= 5) ? parts[4].trim() : (user + "_Pass");

        String xml = buildSqlMapConfigXml(ip, port, dbName, user, password);
        System.out.println("✅ [JdbcUtil] 동적 DB 설정 생성: " + ip + ":" + port + "/" + dbName + " (user=" + user + ")");

        try {
            SqlMapClient client = SqlMapClientBuilder.buildSqlMapClient(new StringReader(xml));
            sqlMapClientCache.put(connectionKey, client);
            return client;
        } catch (Exception e) {
            throw new JdbcIOException("동적 SqlMapClient 생성 실패: " + connectionKey + "\n" + e.getMessage());
        }
    }

    /**
     * iBatis 2 SqlMapConfig XML을 동적으로 생성한다.
     * default_gasmax_app.xml과 동일한 구조이며, JDBC 접속정보만 다르다.
     */
    private static String buildSqlMapConfigXml(String ip, String port, String dbName, String user, String password) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE sqlMapConfig\n");
        sb.append("    PUBLIC\n");
        sb.append("    \"-//ibatis.apache.org//DTD SQL Map Config 2.0 //EN\"\n");
        sb.append("    \"http://ibatis.apache.org/dtd/sql-map-config-2.dtd\">\n");
        sb.append("<sqlMapConfig>\n");
        sb.append("  <settings\n");
        sb.append("      useStatementNamespaces=\"false\"\n");
        sb.append("      cacheModelsEnabled=\"true\"\n");
        sb.append("      enhancementEnabled=\"true\"\n");
        sb.append("      lazyLoadingEnabled=\"true\"\n");
        sb.append("      statementCachingEnabled=\"true\"\n");
        sb.append("      classInfoCacheEnabled=\"true\"\n");
        sb.append("  />\n");
        sb.append("  <transactionManager type=\"JDBC\">\n");
        sb.append("    <dataSource type=\"SIMPLE\">\n");
        sb.append("      <property name=\"JDBC.Driver\" value=\"com.microsoft.sqlserver.jdbc.SQLServerDriver\"/>\n");
        sb.append("      <property name=\"JDBC.ConnectionURL\" value=\"jdbc:sqlserver://");
        sb.append(ip).append(":").append(port);
        sb.append(";DatabaseName=").append(dbName);
        sb.append(";SelectMethod=Cursor;\"/>\n");
        sb.append("      <property name=\"JDBC.Username\" value=\"").append(user).append("\"/>\n");
        sb.append("      <property name=\"JDBC.Password\" value=\"").append(password).append("\"/>\n");
        sb.append("    </dataSource>\n");
        sb.append("  </transactionManager>\n");
        // SQL Map 리소스 (default_gasmax_app.xml과 동일)
        sb.append("  <sqlMap resource=\"sql/common/COMMON_POSTAL_CODE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_APP_USER.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_AREA_CODE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CID_LIST.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_COLLECT_LIST.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_COLLECT_TYPE_CODE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CONSUME_TYPE_CODE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_ITEM.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_ITEM_BALANCE_HPG.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_ITEM_BALANCE_HPG_DETAIL_LIST.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_SAFTY_CHECK.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_SEARCH.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_TAX_INVOICE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_VOLUME_COLLECT.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_VOLUME_READ_METER.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_VOLUME_SALE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_WEIGHT_COLLECT.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_CUSTOMER_WEIGHT_SALE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_EMPLOYEE_CODE.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_READ_METER_LIST.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_SALE_LIST.xml\" />\n");
        sb.append("  <sqlMap resource=\"sql/gasmax/GASMAX_UNPAID_LIST.xml\" />\n");
        sb.append("</sqlMapConfig>\n");
        return sb.toString();
    }

    /**
     * 동적 SqlMapClient 캐시를 비운다 (필요시 호출)
     */
    public static void clearCache() {
        sqlMapClientCache.clear();
        System.out.println("✅ [JdbcUtil] SqlMapClient 캐시 초기화 완료");
    }

    // =========================================================================
    // 파일 기반 방식 (default_gasmax_app 전용 - 로그인/인증용)
    // =========================================================================

    private static final Pattern XML_ENCODING_PATTERN =
            Pattern.compile("encoding\\s*=\\s*['\\\"]([^'\\\"]+)['\\\"]", Pattern.CASE_INSENSITIVE);

    /**
     * 파일 기반 SqlMapConfig 로딩 (BOM 안전 처리)
     * default_gasmax_app.xml 등 파일 기반 설정에만 사용
     */
    private Reader openSqlMapConfigReader(String serverIp) throws IOException {
        String urlString = "file:///" + getSQLConfigPath() + "/" + serverIp + ".xml";
        URL url = new URL(urlString);

        byte[] bytes;
        try (InputStream is = url.openStream()) {
            bytes = readFully(is);
        }

        if (bytes == null) {
            throw new IOException("SQLMapConfig is null: " + urlString);
        }

        Charset charset = detectXmlCharset(bytes);
        bytes = stripBom(bytes);
        String xml = new String(bytes, charset);
        return new StringReader(xml);
    }

    private static byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = is.read(buf)) >= 0) {
            bos.write(buf, 0, n);
        }
        return bos.toByteArray();
    }

    private static Charset detectXmlCharset(byte[] bytes) {
        if (bytes.length >= 2) {
            if ((bytes[0] == (byte) 0xFE) && (bytes[1] == (byte) 0xFF)) return StandardCharsets.UTF_16BE;
            if ((bytes[0] == (byte) 0xFF) && (bytes[1] == (byte) 0xFE)) return StandardCharsets.UTF_16LE;
        }
        if (bytes.length >= 3) {
            if ((bytes[0] == (byte) 0xEF) && (bytes[1] == (byte) 0xBB) && (bytes[2] == (byte) 0xBF)) {
                return StandardCharsets.UTF_8;
            }
        }
        int len = Math.min(bytes.length, 256);
        String prefix = new String(bytes, 0, len, StandardCharsets.ISO_8859_1);
        Matcher m = XML_ENCODING_PATTERN.matcher(prefix);
        if (m.find()) {
            String enc = m.group(1);
            try {
                return Charset.forName(enc);
            } catch (Exception ignore) {}
        }
        return StandardCharsets.UTF_8;
    }

    private static byte[] stripBom(byte[] bytes) {
        if (bytes.length >= 3
                && (bytes[0] == (byte) 0xEF)
                && (bytes[1] == (byte) 0xBB)
                && (bytes[2] == (byte) 0xBF)) {
            byte[] out = new byte[bytes.length - 3];
            System.arraycopy(bytes, 3, out, 0, out.length);
            return out;
        }
        if (bytes.length >= 2
                && ((bytes[0] == (byte) 0xFE && bytes[1] == (byte) 0xFF)
                || (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xFE))) {
            byte[] out = new byte[bytes.length - 2];
            System.arraycopy(bytes, 2, out, 0, out.length);
            return out;
        }
        return bytes;
    }

    // =========================================================================
    // DB 쿼리 실행 메서드들 (기존과 동일)
    // =========================================================================

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<HashMap> selectQuery(String id, Map<String, String> condition) throws JdbcIOException, JdbcSelectException {
        List<HashMap> result;
        try {
            sqlMapClient.startTransaction();
            result = sqlMapClient.queryForList(id, condition);
            sqlMapClient.commitTransaction();
            return result;
        } catch (SQLException e) {
            throw new JdbcSelectException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Select Error. Select condition=[" + condition + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<HashMap> selectQuery(String id, List<String> condition) throws JdbcIOException, JdbcSelectException {
        List<HashMap> result;
        try {
            sqlMapClient.startTransaction();
            result = sqlMapClient.queryForList(id, condition);
            sqlMapClient.commitTransaction();
            return result;
        } catch (SQLException e) {
            throw new JdbcSelectException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Select Error. Select condition=[" + condition + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, String> selectOne(String id, Map<String, String> condition, String key) throws JdbcIOException, JdbcSelectException {
        Map result;
        try {
            sqlMapClient.startTransaction();
            result = sqlMapClient.queryForMap(id, condition, key);
            sqlMapClient.commitTransaction();
            return result;
        } catch (SQLException e) {
            throw new JdbcSelectException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Select Error. Select condition=[" + condition + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    public List<HashMap> selectQuery(String id) throws JdbcIOException, JdbcSelectException {
        return selectQuery(id, (Map<String, String>) null);
    }

    public int insertQuery(String id, Map<String, String> parameter) throws JdbcIOException, JdbcInsertException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.insert(id, parameter);
            sqlMapClient.commitTransaction();
            return 1;
        } catch (SQLException e) {
            throw new JdbcInsertException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Insert Error. Insert parameter=[" + parameter + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int updateQuery(String id, Map<String, String> parameter) throws JdbcIOException, JdbcUpdateException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.update(id, parameter);
            sqlMapClient.commitTransaction();
            return 1;
        } catch (SQLException e) {
            throw new JdbcUpdateException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Update Error. Update parameter=[" + parameter + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int deleteQuery(String id, String key) throws JdbcIOException, JdbcDeleteException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.delete(id, key);
            sqlMapClient.commitTransaction();
            return 1;
        } catch (SQLException e) {
            throw new JdbcDeleteException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Delete Error. Delete parameter=[" + key + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int deleteQuery(String id, HashMap<String, String> keys) throws JdbcIOException, JdbcDeleteException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.delete(id, keys);
            sqlMapClient.commitTransaction();
            return 1;
        } catch (SQLException e) {
            throw new JdbcDeleteException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Delete Error. Delete parameter=[" + keys + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int deleteQuery(String id, List<String> list) throws JdbcIOException, JdbcDeleteException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.delete(id, list);
            sqlMapClient.commitTransaction();
            return 1;
        } catch (SQLException e) {
            throw new JdbcDeleteException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] Delete Error. Delete parameter=[" + list + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int executeProcedure(String id, HashMap<String, Object> param) throws JdbcIOException, JdbcProcedureException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.queryForObject(id, param);
            sqlMapClient.getCurrentConnection().commit();
            return 1;
        } catch (SQLException e) {
            throw new JdbcProcedureException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] execute procedure Error. Execute procedure parameter=[" + param + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String executeProcedure(String id, HashMap<String, Object> param, String outputId) throws JdbcIOException, JdbcProcedureException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.queryForObject(id, param);
            sqlMapClient.getCurrentConnection().commit();
            String outputResult = (String) param.get(outputId);
            return outputResult;
        } catch (SQLException e) {
            throw new JdbcProcedureException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] execute procedure Error. Execute procedure parameter=[" + param + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String executeProcedureByIntegerReturn(String id, HashMap<String, Object> param, String outputId) throws JdbcIOException, JdbcProcedureException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.queryForObject(id, param);
            sqlMapClient.getCurrentConnection().commit();
            String outputResult = ((Integer) param.get(outputId)).toString();
            return outputResult;
        } catch (SQLException e) {
            throw new JdbcProcedureException(e.getMessage() + "\n SQLMapConfig id=[" + id + "] execute procedure Error. Execute procedure parameter=[" + param + ']');
        } finally {
            try {
                sqlMapClient.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * key1과 key2를 합친 값을 반환
     */
    public String getKeyValue(String key1, String key2) {
        return key1 + KEY_DELIMITER + key2;
    }
}
