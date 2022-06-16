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
	String bidx = request.getParameter("bidx");
	String nicName = request.getParameter("nicName");
	String reContent = request.getParameter("reContent");

	CommentDao cd = new CommentDao();
	
	String day = cd.reCommentWrite(cidx, bidx, nicName, reContent);
	JSONObject obj = new JSONObject();
	obj.put("day", day);
	
	out.print(obj.toJSONString());
%>