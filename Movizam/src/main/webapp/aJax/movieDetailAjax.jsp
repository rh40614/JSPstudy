<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="domain.FilmVo"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="dbconn.Dbconn" %>	 

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

   <%
   Connection conn = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;

	Dbconn dbconn = new Dbconn();		// Dbconn에 만들어 놓았음으로 import 해주어야함
	conn = dbconn.getConnection();
	
	//data로 넘어온 해당되는 fidx 값 찾기 
	String fidx = request.getParameter("fidx");
	int fidx_ = Integer.parseInt(fidx);
	
	String sql= "select * from film where fidx=?";
	
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, fidx_ );
	
	rs= pstmt.executeQuery();
	
	
	/* FilmVo fv = null; */
	/* if(rs.next()){
		 fv = new FilmVo(); 
		
		fv.setFidx(rs.getInt("fidx"));
		fv.setFilmCategory(rs.getString("filmCategory"));
		fv.setFilmDate(rs.getString("filmDate"));
		fv.setFilmName(rs.getString("filmName"));
		fv.setFilmStatus(rs.getString("filmStatus"));
		fv.setFilmDetail(rs.getString("filmDetail"));
		fv.setFilmPoster(rs.getString("filmPoster"));
		
	} */
	
	
	
	//JsonSimple.jar 넣어주기. JSON 배열에 담고 JSON 오브젝트에 담아서 넘긴다. 
	JSONArray array = new JSONArray();	//[]
		
	if(rs.next()){
		//{}
		JSONObject obj = new JSONObject();
		obj.put("filmPoster", rs.getString("filmPoster"));
		obj.put("filmName", rs.getString("filmName"));
		obj.put("filmStatus", rs.getString("filmStatus"));
		obj.put("filmDate", rs.getString("filmDate"));
		obj.put("filmDetail", rs.getString("filmDetail"));
		
		array.add(obj);		//[]에 insert 객체한것
	}
	//Json형식으로 만들어서 반환
	out.print(array.toJSONString());
	
	
   
   %>
   
   
       