<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import ="jspstudy.domain.*" %>
<%@ page import ="java.util.*" %>

<%
//select 쿼리를사용하기 위해서 function에서 메서드를 만든다. 
//memebrSelectAll  메서드를 호출한다. 

	//MemberDao md = new MemberDao(); 
	//ArrayList<MemberVo> alist =md.memberSelectAll();

	//MemberDao md = new MemberDao();
	//ArrayList<MemberVo> alist = md.memberSelectAll();
	//out.println(alist.get(0).getMembername()+"<br>");
	
	
//객체 생성을 여기서 하지않고 컨트롤러로 넘김. 값만 여기로가져와서 꺼내기
	ArrayList<MemberVo> alist =(ArrayList<MemberVo>)request.getAttribute("alist");

%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록</title>
<link rel="stylesheet" href="memberList.css">

</head>
<body>
<%
if(session.getAttribute("midx") !=null){ 
	out.println("회원아이디 :"+ session.getAttribute("midx")+ "<br>");
	out.println("회원이름 :"+session.getAttribute("memberName"));
	
	out.println("<a href='"+request.getContextPath() +"/member/memberLogout.do'>로그아웃</a>");
}

%>


<button type= "button" name="bt" class="bt" onclick="location.href='<%=request.getContextPath()%>/'">홈</button> 		
<h1>회원목록 리스트</h1>
<div>
<table >
<thead>
<tr id="listHead">
<th class="th">번호</th>
<th class="th">회원이름</th>
<th class="th">회원연락처</th>
<th>작성일</th>
</tr>
</thead>
<tbody>
<!-- 향상된 반복문으로 표내부의 줄을 데이터의 값을 가져와서 출력. %= 하면 out.println의 역할을 한다. -->
<%
for(MemberVo mv :alist) {
%>
<tr>
<td class="th"><%=mv.getMidx() %></td>
<td class="th"><%=mv.getMembername() %></td>
<td class="th"><%=mv.getMemberphone() %></td>
<td><%=mv.getWriteday() %></td>
</tr>

<% }%>


</tbody>

</table>
</div>

</body>
</html>