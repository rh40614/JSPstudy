package jspstudy.dbconn;

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
				//접속정보를 활용해서 연결객체를 만든다. //드라이버 연동해서 오라클연결
				conn =DriverManager.getConnection(url,user,password);
				}catch(Exception e){
				e.printStackTrace();
			}
			return conn;
		
		}
}



// 이메서드를 사용하려면 객테 생성이후.getconnection을 호출하면도니다. 