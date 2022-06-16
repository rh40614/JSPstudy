<%@page import="domain.FilmVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% FilmVo fv = (FilmVo)request.getAttribute("fv"); %>

 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>영화수정</title>
<link rel="stylesheet" href="/Movizam/film/filmView.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>
</head>

<body>
<script>


//로그아웃 성공//ajax로 바꾸기
function logout(){
	alert("로그아웃되었습니다.")
}

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

<script>
function logout(){
	alert("로그아웃되었습니다.")
}

</script>
</span>

<hr id="first_hr">



<!-- 검색창 -->
<div class="search">
	<input type="text" name="searchBar" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn"><img src="../image/search.png" alt="searchButton"></button>
</div>

 <hr id="pink_hr">


</header>

<div id="navmainWrap">


<!-- nav1 -->
<nav id="nav">

<ul class="tab_title">
	<li class="on"><a href="<%=request.getContextPath() %>/film/filmInsert.do">영화 등록(관리자)</a></li>
	<li><a href="<%=request.getContextPath() %>/film/filmList.do">영화 수정/삭제(관리자)</a></li>
</ul>
</nav>

<div id="separate"></div>

<!-- main -->
<main>

<div class="tab_content">
<!-- tab1 content -->
<div>
<span>| 영화 정보확인 </span> 

<form> 

<label>
	<span>카테고리</span>
	<input type="text" name="filmCategory" value="<%=fv.getFilmCategory()%>" readonly></label>
<label>
	<span>제목</span>
	<input type="text" name="filmName" value="<%=fv.getFilmName()%>" readonly></label>
<label>
	<span>개봉일자</span>
	<input type="text" id="filmDate" name="filmDate" value="<%=fv.getFilmDate() %>" readonly></label>
<label>
	<span>상영상태</span>
	<input type="text" id="filmState" name="filmState" value="<%=fv.getFilmStatus() %>" readonly>
</label>
<label style="width: 900px;">
	<span>상세정보</span><br>
	<textarea name="filmDetail" readonly><%=fv.getFilmDetail() %></textarea>
</label>
<!--영화 포스터 미리보기-->
<label id="poster">
<% if(fv.getFilmPoster() != null){		%>
	<img src="<%=request.getContextPath()%>/image/movie/<%=fv.getFilmPoster() %>" alt="사진 불러오기 실패">
<%  } %>
</label>

<button type="button"  id="editbtn" onclick="location.href='<%=request.getContextPath()%>/film/filmEdit.do?fidx=<%=fv.getFidx()%>'">수정하러가기</button>
</form>									
</div>
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
