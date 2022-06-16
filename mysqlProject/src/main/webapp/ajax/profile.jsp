<%@page import="mysqlProject.service.MemberDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String userMidx = request.getParameter("userMidx");
	
	MemberDao md = new MemberDao();
	String value = md.ajaxProfile(userMidx);
	String result = request.getContextPath() + "/img/profile/"+userMidx+"/"+ value; 
	
	out.println(result + "");
%>