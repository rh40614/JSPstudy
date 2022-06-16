<%@page import="mysqlProject.service.MemberDao"%>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("EUC-KR");
	
	String userId = request.getParameter("userId");
	String userName = request.getParameter("userName");
	String userEmail = request.getParameter("userEmail");
	
	MemberDao md = new MemberDao();
	
	int value = md.findPwd(userId, userName, userEmail);
	
	out.write(value + "");
%>