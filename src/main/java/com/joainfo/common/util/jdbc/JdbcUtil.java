package com.joainfo.common.util.jdbc;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JdbcUtil {
    /**
     * JdbcUtil의 인스턴스
     */
    private static JdbcUtil instance;

    /**
     * 공통으로 사용되는 디폴트 DB 설정
     */
    //public static final String DEFAULT_SQL_CONFIG = "joatech.dyndns.org;2521;GasMax_Sample;GasMax_Sample";
//	public static final String DEFAULT_SQL_CONFIG_DEV = "default_gasmax_app";
//	public static final String DEFAULT_SQL_CONFIG_PROD = "default_gasmax_prod";

    /**
     * * todo:공통으로 사용되는 디폴트 DB 설정
     * * todo:공통으로 사용되는 디폴트 DB 설정
     * * todo:공통으로 사용되는 디폴트 DB 설정
     */
    public static final String DEFAULT_SQL_CONFIG = "default_gasmax_app";
    //public static final String DEFAULT_SQL_CONFIG_PATH = "C:/gasmax_db_config";
    public static final String DEFAULT_SQL_CONFIG_PATH = "./gasmax_db_config";
    public static String sqlConfigPath = "";

    /**
     * 키 값의 구분자
     */
    public static final String KEY_DELIMITER = "|";

    private SqlMapClient sqlMapClient;

    private static String getEnv() {
        String env = System.getenv("env"); // OS 환경 변수 가져오기
        return (env != null && !env.isEmpty()) ? env : "dev"; // 기본값: dev
    }

    /**
     * SQL Config Path를 반환하는 메서드
     */
//    public String getSQLConfigPath() {
//        if ("".equals(sqlConfigPath)) {
//            Properties prop = new Properties();
//            try {
//                InputStream is = getClass().getResourceAsStream("/db_config.properties");
//                if (is != null) {
//                    prop.load(is);
//                    is.close();
//                    String env = getEnv();
//                    if (env.equals("prod")) {
//                        sqlConfigPath = (String) prop.get("prod");
//                    } else {
//                        sqlConfigPath = (String) prop.get("dev");
//                    }
//                    return sqlConfigPath;
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            return DEFAULT_SQL_CONFIG_PATH;
//        } else {
//            return sqlConfigPath;
//        }
//    }



    public String getSQLConfigPath() {
        if ("".equals(sqlConfigPath)) {
            Properties prop = new Properties();
            try {
                InputStream is = getClass().getResourceAsStream("/db_config.properties");
                if (is != null) {
                    prop.load(is);
                    is.close();
                    // 무조건 prod 설정만 사용
                    sqlConfigPath = (String) prop.get("prod");
                    return sqlConfigPath;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return DEFAULT_SQL_CONFIG_PATH;
        } else {
            return sqlConfigPath;
        }
    }

    /**
     * 디폴트 생성자
     *
     * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
     */
    private JdbcUtil(String serverIp) {
        sqlMapClient = this.getSqlMap(serverIp);
    }

    /**
     * Singleton으로 JdbcUtil 인스턴스 생성
     *
     * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
     * @return InfraLogUpdator
     */
    public static JdbcUtil getInstance(String serverIp) {
        instance = new JdbcUtil(serverIp);
        return instance;
    }

    /**
     * @param serverIp 서버 아이피 또는 도메인이름. 동일한 이름으로 DB 설정 파일이 존재해야 한다.
     * @return SqlMapClient
     * @throws JdbcIOException
     */
    private SqlMapClient getSqlMap(String serverIp) throws JdbcIOException {
        Reader reader;
        try {
//			reader = Resources.getResourceAsReader("file:///C:/sql/" + serverIp + ".xml");
            reader = Resources.getUrlAsReader("file:///" + getSQLConfigPath() + "/" + serverIp + ".xml");
        } catch (IOException e) {
            e.printStackTrace();
            throw new JdbcIOException(e.getMessage() + "\n JDBC Connection Error.");
        }
        return SqlMapClientBuilder.buildSqlMapClient(reader);
    }

    /**
     * Select 쿼리 수행
     *
     * @param id
     * @param condition 검색 조건
     * @return List<HashMap>
     * @throws JdbcIOException
     * @throws JdbcSelectException
     */
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

    /**
     * Select 쿼리 수행
     *
     * @param id
     * @param condition
     * @return List<HashMap>
     * @throws JdbcIOException
     * @throws JdbcSelectException
     */
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

    /**
     * Select 쿼리 수행
     *
     * @param id
     * @param condition 검색 조건
     * @param key       가져올키
     * @return Map
     * @throws JdbcIOException
     * @throws JdbcSelectException
     */
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

    /**
     * 조건 없는 Select 쿼리 수행
     *
     * @param id
     * @return List<HashMap>
     * @throws JdbcIOException
     * @throws JdbcSelectException
     */
    @SuppressWarnings({"rawtypes"})
    public List<HashMap> selectQuery(String id) throws JdbcIOException, JdbcSelectException {
        return selectQuery(id, (Map<String, String>) null);
    }

    /**
     * Insert
     *
     * @param id
     * @param parameter
     * @return 처리 건수
     * @throws JdbcIOException
     * @throws JdbcInsertException
     * @throws SQLException
     */
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

    /**
     * Update
     *
     * @param id
     * @param parameter
     * @return 처리 건수
     * @throws JdbcIOException
     * @throws JdbcUpdateException
     */
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

    /**
     * Delete
     *
     * @param id
     * @param key
     * @return 처리 건수
     * @throws JdbcIOException
     * @throws JdbcDeleteException
     */
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

    /**
     * Delete
     *
     * @param id
     * @param keys
     * @return 처리 건수
     * @throws JdbcIOException
     * @throws JdbcDeleteException
     */
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

    /**
     * Delete List
     *
     * @param id
     * @param list
     * @return 처리 건수
     * @throws JdbcIOException
     * @throws JdbcDeleteException
     */
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
//			sqlMapClient.commitTransaction(); // <-- 프로시저에서는 이렇게 커밋이 안되고 롤백됨.
            sqlMapClient.getCurrentConnection().commit(); // 프로시저 수행시 커밋이 안되는 오류 수정함.
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

    /**
     * 프로시저 실행 후 출력 값 받아오기
     *
     * @param id
     * @param param
     * @param outputId
     * @return
     * @throws JdbcIOException
     * @throws JdbcProcedureException
     */
    public String executeProcedure(String id, HashMap<String, Object> param, String outputId) throws JdbcIOException, JdbcProcedureException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.queryForObject(id, param);
//			sqlMapClient.commitTransaction(); // <-- 프로시저에서는 이렇게 커밋이 안되고 롤백됨.
            sqlMapClient.getCurrentConnection().commit(); // 프로시저 수행시 커밋이 안되는 오류 수정함.
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

    /**
     * 프로시저 실행 후 출력 값 받아오기
     *
     * @param id
     * @param param
     * @param outputId
     * @return
     * @throws JdbcIOException
     * @throws JdbcProcedureException
     */
    public String executeProcedureByIntegerReturn(String id, HashMap<String, Object> param, String outputId) throws JdbcIOException, JdbcProcedureException {
        try {
            sqlMapClient.startTransaction();
            sqlMapClient.queryForObject(id, param);
//			sqlMapClient.commitTransaction(); // <-- 프로시저에서는 이렇게 커밋이 안되고 롤백됨.
            sqlMapClient.getCurrentConnection().commit(); // 프로시저 수행시 커밋이 안되는 오류 수정함.
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

    public static void main(String[] args) {
        try {
//			Reader reader            = Resources.getResourceAsReader("sql/SQLMapConfig.xml");
//			SqlMapClient sqlMap      = SqlMapClientBuilder.buildSqlMapClient(reader);
//			Properties prop = Resources.getResourceAsProperties("sql/SQLMapConfig.xml");
//			System.out.println(prop.get("<properties"));
//			prop.put("<properties", "resource=\"sql/db.properties\"/>");
//			System.out.println(prop.get("<properties"));
//			SqlMapClient sqlMap2      = SqlMapClientBuilder.buildSqlMapClient(reader);

//			System.out.println(JdbcUtil.getInstance().getSqlMap().getDataSource().getConnection().getClientInfo());
//			System.out.println(Resources.getResourceURL("sql/SQLMapConfig.xml"));
//			System.out.println(Resources.getResourceAsProperties("sql/SQLMapConfig.xml"));
//			System.out.println(Resources.getResourceAsProperties("sql/SQLMapConfig.xml").getProperty("maxRequests"));
//			Enumeration e = Resources.getResourceAsProperties("sql/SQLMapConfig.xml").propertyNames();
//			while(e.hasMoreElements()){
//				System.out.println(e.nextElement());
//			}
//			SqlMapClient sqlMap      = SqlMapClientBuilder.buildSqlMapClient(reader);
//			Resources.getResourceAsProperties("sql/SQLMapConfig.xml").put("", "");
//			Map<String, String> condition = new HashMap<String, String>();
//			condition.put("USE_YN", "Y");
//
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			List<HashMap> queryForList = (List<HashMap>) sqlMap.queryForList("RMCC.ResourceGroup.Select", condition);
//			for( HashMap<String, String> data :  queryForList) {
//			       System.out.println(data.get("GRP_CD"));
//			}
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("catalogName", "GM_TestHigh");
            param.put("areaCode", "01");
            param.put("customerCode", "");
            param.put("customerType", "0");
            param.put("customerName", "테스트");
            param.put("userName", "테스트_USER");
            param.put("phoneNumber", "2090-6987");
            param.put("phoneNumberFind", "20906987");
            param.put("mobileNumber", "20-90");
            param.put("mobileNumberFind", "2090");
            param.put("address1", "서울 광진");
            param.put("address2", "584-3");
            param.put("remark1", "비고1");
            param.put("remark2", "비고2");
            param.put("employeeCode", "01");
            param.put("employeeName", "하");
            param.put("consumeTypeCode", "16"); // 프로시저에 아직 등록안된 파라미터
            param.put("appUserMobileNumber", "010-2090-6987");

//			String result = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).executeProcedure("GASMAX.CustomerSearch.Insert", param, "outputMessage");
            String result = JdbcUtil.getInstance(JdbcUtil.DEFAULT_SQL_CONFIG).executeProcedure("GASMAX.CustomerSearch.Insert", param, "outputCustomerCode");
            System.out.println("거래처 코드=>" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


    /**
     * key1과 key2를 합친 값을 반환
     *
     * @param key1
     * @param key2
     * @return
     */
    public String getKeyValue(String key1, String key2) {
        return key1 + KEY_DELIMITER + key2;
    }
}
