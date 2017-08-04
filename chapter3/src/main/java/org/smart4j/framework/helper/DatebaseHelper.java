package org.smart4j.framework.helper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.PropsUtil;

import com.mysql.jdbc.Connection;

/**
 * 数据库操作助手类
 * @author Minzhe Xu	2017年7月23日上午10:52:14
 *
 */
public class DatebaseHelper {
	 private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DatebaseHelper.class);
	    //用于存放当前线程的 sql 链接对象
	    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
	    private static final QueryRunner QUERY_RUNNER = new QueryRunner(); //查询更新 助手
	    private static final BasicDataSource BASIC_DATA_SOURCE;  //用于dbcp链接池的数据源
	    static {
	        Properties props = PropsUtil.loadProps("db.properties");
	        String driverclassname = PropsUtil.getString(props,"jdbc.driverClassName");
	        String url = PropsUtil.getString(props,"jdbc.url");
	        String username = PropsUtil.getString(props,"jdbc.username");
	        String password = PropsUtil.getString(props,"jdbc.password");

	        BASIC_DATA_SOURCE = new BasicDataSource();
	        BASIC_DATA_SOURCE.setDriverClassName(driverclassname);
	        BASIC_DATA_SOURCE.setUrl(url);
	        BASIC_DATA_SOURCE.setUsername(username);
	        BASIC_DATA_SOURCE.setPassword(password);
	    }
	    public static DataSource getDataSource(){
	        return BASIC_DATA_SOURCE;
	    }
	    public static Connection getConnection(){
	    	Connection connection = CONNECTION_HOLDER.get();
	    	if(connection==null){
	    		try {
	    			connection=(Connection) BASIC_DATA_SOURCE.getConnection();
				} catch (Exception e) {
					 LOGGER.error("get connection failure", e);
		             throw new RuntimeException(e);
				}finally {
					CONNECTION_HOLDER.set(connection);
				}
	    	}
			return connection;
	    }
	/**
	 * 开启事务
	 */
	public static void beginTransaction(){
		Connection connection = getConnection();
		try {
			if(connection!=null){
				connection.setAutoCommit(false);
			}
		} catch (Exception e) {
			LOGGER.error("begin transation failre",e);
			throw new RuntimeException(e);
		}finally{
			CONNECTION_HOLDER.set(connection);
		}
		
		
	}
	/**
	 * 提交事务
	 */
	public static void commitTransaction(){
		Connection connection = getConnection();
		if(connection!=null){
			try {
				connection.commit();
				connection.close();
			} catch (SQLException e) {
				LOGGER.error("commit transation failre",e);
				throw new RuntimeException(e);
			}finally{
				CONNECTION_HOLDER.remove();
			}
			
		}
	}
	/**
	 * 回滚事务
	 */
	public static void rollbackTransaction(){
		Connection conn=getConnection();
		if(conn!=null){
			try {
				conn.rollback();
				conn.close();
			} catch (Exception e) {
				LOGGER.error("rollback transation failre",e);
				throw new RuntimeException(e);
			}finally{
				CONNECTION_HOLDER.remove();
			}
		}
	}
	
	/**
	 * 查询列表
	 * @param entityClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        Connection conn = getConnection();
        List<T> result = null;
        try {
            result = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }
	
	public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
		T result=null;
		try {
			result=QUERY_RUNNER.query(getConnection(), sql,new  BeanHandler<T>(entityClass),params);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	/**
     * 执行多表链接查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executorQuery(String sql,Object... params){
        List<Map<String, Object>> result = null;
        try {
            result = QUERY_RUNNER.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("executor query failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }
    
    /**
     * 更新实体
     * @param sql
     * @param params
     * @return 返回影响的行数
     */
    public static int executeUpdate(String sql,Object... params){
        int rows = 0;
        try {
            rows = QUERY_RUNNER.update(getConnection(),sql,params);
        } catch (SQLException e) {
            LOGGER.error("executor update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }
    
    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap 实体属性
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }
    /**
     * 更新实体
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";

        StringBuilder columns = new StringBuilder();

        for(String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);
        Object[] params = paramsList.toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql,id) == 1;
    }

    //返回 表名.实体名称 全大写
    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName().toUpperCase();
    }

    public static <T> T queryColumn(String sql, Object... params) {
        T obj;
        try {
            obj = QUERY_RUNNER.query(getConnection(),sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static <T> Set<T> queryColumnSet(String sql, Object...params) {
        Set<T> result = null;
        try {
            List<T> query = QUERY_RUNNER.query(getConnection(), sql, new ColumnListHandler<T>(), params);
            result = new HashSet<T>();
            result.addAll(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
