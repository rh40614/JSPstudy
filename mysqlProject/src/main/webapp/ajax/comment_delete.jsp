<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="mysqlProject.service.CommentDao"%>
<%@page import="org.json.simple.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("EUC-KR");
	
	String cidx = request.getParameter("cidx");
	int cidx_ = Integer.parseInt(cidx);
	CommentDao cd = new CommentDao();
	
	int value = cd.commentDelete(cidx_);
	
	out.print(value + "");
%>