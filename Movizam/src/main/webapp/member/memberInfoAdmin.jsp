<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "domain.MemberVo" %>  
<%@ page import= "java.util.*" %>

<%	
	MemberVo mv = (MemberVo)request.getAttribute("mv");		//회원정보
	
%>

   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>정보 관리</title>
<link rel="stylesheet" href="/Movizam/member/memberInfoAdmin.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>
</head>

<script>
//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			$(".menutab_content").slideToggle("fast");
		})

	});
</script>

</head>


<body>
<div id="wrap">
<!-- header -->
<header>
<a href="<%=request.getContextPath()%>/"><img src="../image/movizam.png" alt="배너 이미지" id="logo"></a>


<div id="menu">
<ul>
	<li class="menutab" ><a href="<%=request.getContextPath() %>/film/filmAll.do">영화</a>
		<% if(session.getAttribute("midx") != null && session.getAttribute("adminYN").equals("Y")){	%>			<!-- 문자열 비교임으로 .equals() -->
		<ul class="menutab_content">	
			<li><a href="<%=request.getContextPath() %>/film/filmInsert.do">영화 등록(관리자)</a></li>
			<li><a href="<%=request.getContextPath() %>/film/filmList.do">영화 수정/삭제(관리자)</a></li>
		</ul>	
		<% } %>
	</li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList_film.do">영화게시판</a></li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList.do">자유게시판</a></li>
</ul>
</div>

<!-- Login, Join -->

<% if(session.getAttribute("midx") == null){%>

<a href="<%=request.getContextPath()%>/member/memberLogin.do" id="login">Login</a>
<a href="<%=request.getContextPath()%>/member/memberJoin.do" id="join">Join</a>
<% } %>

<span id="loginSet">

<%if(session.getAttribute("midx") != null){ 		%>
	<a href="<%=request.getContextPath()%>/member/memberInfo.do" style="font-size: 16px; color: black;"><%=session.getAttribute("memberNickname")%> <span>님</span><br></a>
	<%=session.getAttribute("message")			%>
	
	
<a href="<%=request.getContextPath() %>/member/memberLogout.do" onclick="logout()">로그아웃</a>
<% } %>


</span>

<hr id="first_hr">



<!-- 검색창 -->
<div class="search">
<form name="frm" action="<%=request.getContextPath()%>/main/mainSearchAction.do" method="post">
	<input type="text" name="keyword" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn" onclick="location.href='<%=request.getContextPath()%>/main/mainSearchAction.do'"><img src="../image/search.png" alt="searchButton"></button>
</form>
</div>

<hr id="pink_hr">


</header>



<div id="navmainWrap"> 
<nav id="nav">
<ul class="tab_title">
	<li><a href="<%=request.getContextPath()%>/member/memberInfo.do">내 정보 보기</a></li>
	<li><a href="<%=request.getContextPath()%>/reply/manageReplyAdmin.do">댓글 관리(관리자)</a></li>
	<li class="on"><a href="<%=request.getContextPath()%>/member/memberList.do">회원 목록(관리자)</a></li>
</ul>
</nav>

<div id="separate"></div>


<!-- main -->
<main>

<div class="tab_content">
<span>| 회원 정보 관리 </span>
<table class="info">
<tbody>
<tr>
<td rowspan="4" class="imageBox">
<%if(mv.getMemberImage() != null){	%>
	<img src='<%=request.getContextPath() %>/image/profile/<%=mv.getMemberImage() %>' alt="회원프로필사진"></td>
<% 	}%>

<td>닉네임</td><td colspan="3"><%=mv.getMemberNickname() %></td>
</tr>
<tr>
<td>이름</td><td colspan="3"><%=mv.getMemberName() %></td>
</tr>
<tr>
<td>생년월일</td><td><%=mv.getBirth_yy() %>년</td><td><%=mv.getBirth_mm() %>월</td><td><%=mv.getBirth_dd() %>일</td>
</tr>
<tr>
<td>주소</td><td colspan="3"><%=mv.getMemberAdr() %></td>
</tr>
<tr>
<td rowspan="2" class="info_td1"></td><td>전화번호</td><td colspan="3"><%=mv.getMemberPhone()%></td>
</tr>
<tr>
<td>권한</td><td colspan="3"><%=mv.getAdminYN()%></td>
</tr>

</tbody>
</table>

<a href="<%=request.getContextPath()%>/member/memberEditAdmin.do?midx=<%=mv.getMidx() %>"><button type="button" id="editbtn" >수정하기</button></a>
</div>


<div id= memberDelete>
<a href="<%=request.getContextPath()%>/member/memberDelete.do">더 이상 서비스를 이용하고 싶지 않아요. 탈퇴하기</a>
</div>

</main>
</div>

<!-- footer -->
<footer>
<span id="footer_Content">
(54930)전라북도 전주시 덕진구 백제대로 572 5층 이젠 IT 컴퓨터학원(금암동)<br>
대표이사 김연희    사업자등록번호 104-32-32504<br>
호스팅사업자 이젠 IT 컴퓨터학원 <br>
</span>
</footer>
</div>
</body>
</html>