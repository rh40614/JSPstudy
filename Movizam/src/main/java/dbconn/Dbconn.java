package dbconn;

import java.sql.Connection;
import java.sql.DriverManager;


public class Dbconn {
	
		
		String url="jdbc:mysql://@127.0.0.1:3306/mysql?serverTimezone=UTC&characterEncoding=UTF-8";	

		
		String user ="root";
		String password="1234";
		
		
		public Connection getConnection() {
			Connection conn =null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver"); //드라이버를 메모리에 로딩
				conn =DriverManager.getConnection(url,user,password);
				}catch(Exception e){
				e.printStackTrace();
			}
			return conn;
		
		}
}

