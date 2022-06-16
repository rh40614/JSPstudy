<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
  
<%
//jdbc: java database connectivity. 데이터 베이스를 자바를 이용하여 조작하기 위한 자바 라이브러리 
	String url="jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String user ="system";
	String password="1234";
	

	//해당패키지에 있는 클래스를 가져온다. //드라이버를 메모리에 로딩
	Class.forName("oracle.jdbc.driver.OracleDriver");
	
	//자바에 sql에 드라이브 매니져의 메서드 이용하여 url,user,password 을 가져옴
	//연결정보 담기 //import로 java.sql.* 로 모든 파일 연동해서 코드 간단하게 하기
	//java.sql.Connection conn = java.sql.DriverManager.getConnection(url,user,password);
	//접속정보를 활용해서 연결객체를 만든다. //드라이버 연동해서 오라클연결
	Connection conn =DriverManager.getConnection(url,user,password);


%>@