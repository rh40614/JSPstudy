<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="mysqlProject.service.CommentDao"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("EUC-KR");
	
	String bidx = request.getParameter("bidx");
	String nicName = request.getParameter("nicName");
	String commContent = request.getParameter("commContent");

	CommentDao cd = new CommentDao();
	
	String day = cd.commentWrite(bidx, nicName, commContent);
	JSONObject obj = new JSONObject();
	obj.put("day", day);
	
	out.print(obj.toJSONString());
%>