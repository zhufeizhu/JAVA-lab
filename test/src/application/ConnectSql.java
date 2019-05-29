package application;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class ConnectSql {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/JAVA?useSSL=false";
 
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";
	
    public Connection connect()
	{
    	  // 注册 JDBC 驱动        
		Connection con = null;
		try {
			Class.forName(JDBC_DRIVER);
			con =(Connection) DriverManager.getConnection(DB_URL,USER,PASS);
		}catch(SQLException se){
            // 处理 JDBC 错误
    	
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return con;
	}	
}
