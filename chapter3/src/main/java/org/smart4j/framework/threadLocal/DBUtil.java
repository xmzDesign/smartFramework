package org.smart4j.framework.threadLocal;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class DBUtil {
	private static final String driver="com.mysql.jdbc.Driver";
	private static final String url="jdbc:mysql://localhost:3306/demo";
	private static final String username="root";
	private static final String password="root";
	//定义一个数据库连接
	
	private static ThreadLocal<Connection> connContainer=new ThreadLocal<Connection>();
	
	//获取连接
	public static Connection getConnection(){
		Connection conn=connContainer.get();
		try {
			Class.forName(driver);
			conn=(Connection) DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connContainer.set(conn);
		}
		return conn;
	}
	
	//关闭连接
	public static void closeConnection(){
		Connection conn=connContainer.get();
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connContainer.remove();
		}
	}
	

}
