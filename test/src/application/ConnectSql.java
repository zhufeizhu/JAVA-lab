package application;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;


public class ConnectSql {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/JAVA?useSSL=false";
 
    // ���ݿ���û��������룬��Ҫ�����Լ�������
    static final String USER = "root";
    static final String PASS = "123456";
	
    public Connection connect()
	{
    	  // ע�� JDBC ����        
		Connection con = null;
		try {
			Class.forName(JDBC_DRIVER);
			con =(Connection) DriverManager.getConnection(DB_URL,USER,PASS);
		}catch(SQLException se){
            // ���� JDBC ����
    	
            se.printStackTrace();
        }catch(Exception e){
            // ���� Class.forName ����
            e.printStackTrace();
        }
        return con;
	}	
}
