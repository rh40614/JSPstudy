<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" %>
<%
	//input 객체의 이름을 담은 파라미터를 호출하면 그 객체의 값을 리턴한다.
	
	request.setCharacterEncoding("utf-8");
	String memberId = request.getParameter("memberId");	
	String memberPwd = request.getParameter("memberPwd");
	String memberName = request.getParameter("memberName");
	String memberMail = request.getParameter("memberMail");
	String memberPhone = request.getParameter("memberPhone");
	String memberJumin = request.getParameter("memberJumin");
	String memberAddr = request.getParameter("memberAddr");
	String memberGender = request.getParameter("memberGender");
	String[] memberHobby = request.getParameterValues("memberHobby");
	
	
	out.println(memberId+"<br>");
	out.println(memberPwd+"<br>");
	out.println(memberName+"<br>");
	out.println(memberMail+"<br>");
	out.println(memberPhone+"<br>");
	out.println(memberJumin+"<br>");
	out.println(memberAddr+"<br>");
	out.println(memberGender+"<br>");
	for(int i=0;i<memberHobby.length;i++){
		out.println(memberHobby[i]+" ");	
	}
	
%>
 --%>
