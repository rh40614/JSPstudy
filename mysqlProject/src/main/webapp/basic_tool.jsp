<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>홈페이지</title>
	<link rel="stylesheet" href="./css/basic.css">
<%
	session.setAttribute("saveUrl", null);
	if(session.getAttribute("midx") != null){
		out.println("<style>#login-box{display:inherit;}</style>");
		out.println("<style>#logout-box{display:none;}</style>");
	}else{
		out.println("<style>#login-box{display:none;}</style>");
		out.println("<style>#logout-box{display:inherit;}</style>");
	}
%>
</head>
<body>
<article>
	<header>
		<a href="<%=request.getContextPath()%>/"><img alt="홈페이지" src="./img/logoImg.PNG"></a>
		<div class="header-outer">
			<iframe src="https://www.youtube.com/embed/jZwv83Stl60?autoplay=1&mute=1&loop=1"></iframe>
		</div>
	</header>
	
	<nav id="topNav">
		<ul class="topList">
			<li><a href="<%=request.getContextPath()%>/">홈</a></li>
			<li>
				<a href="#">커뮤니티</a>
				<ul class="topList-depth">
					<li><a>공지사항</a></li>
					<li><a>자유 게시판</a></li>
					<li><a>질문 게시판</a></li>
				</ul>
			</li>
			<li>
				<a href="#">정보 게시판</a>
				<ul class="topList-depth">
					<li><a>게임소식</a></li>
					<li><a>일상소식</a></li>
				</ul>
			</li>
			<li>
				<a href="#">갤러리</a>
			</li>			
		</ul>
	</nav>
	
	<nav id="leftNav">
		<div id="logout-box">
			<br>
			<input type="button" id="login"value="로그인" onclick="location.href='<%=request.getContextPath()%>/member/login.do'">
			<div id="logout-bottom">
				<span id="findId"><a href="<%=request.getContextPath()%>/member/find.do">아이디/비밀번호 찾기</a></span>
				<span id="join"><a href="<%=request.getContextPath()%>/member/join.do">회원가입</a></span>
			</div>
		</div>
		<div id="login-box">
			<input type="button" id="login-profile" onclick="location.href='<%=request.getContextPath()%>/member/myInfo.do'" value="프로필">
			<p class="profile-right" id ="profileId"><%out.println(session.getAttribute("memberId"));%></p>
			<p class="profile-right" id ="profileNic"><%out.println(session.getAttribute("memberNicname"));%> 님</p>
			<div id="logout-bottom">
				<span id="join"><a href="<%=request.getContextPath()%>/member/logout.do">로그아웃</a></span>
			</div>
		</div>
		
		<div id="leftSubBox">
			<input type="button" value="글쓰기" id="write" onclick="location.href='<%=request.getContextPath()%>/board/write.do'">
			<input type="text" id = "serchText" placeholder="통합검색">
			<input type="button" id = "serchButton" onclick="location.href='<%=request.getContextPath()%>/board/search.do'" value="🔍">	
		</div>
		
		
		<div id="list">
			<h3 style="margin-top: 0px; border-top: none;">커뮤니티</h3>
			<ul>				
				<li class="secList">
					<span><a>공지사항</a></span>
					<span><a>자유 게시판</a></span>
					<span><a>질문 게시판</a></span>
				</li>
			</ul>
			<h3 class="mainList">정보 게시판</h3>
			<ul>	
				<li class="secList">
					<span><a>게임소식</a></span>
					<span><a>일상소식</a></span>
				</li>
			</ul>
			<h3 class="mainList">미디어 게시판</h3>
			<ul>	
				<li class="secList">
					<span><a>갤러리</a></span>
				</li>
			</ul>
		</div>
	</nav>
	<section>
		<h4>홈페이지 입니다</h4>
		<hr>
		
	</section>
	
</article>
</body>
</html>