<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/topNav.css">
<ul class="topList">
	<li><a href="<%=request.getContextPath()%>/">홈</a></li>
	<li>
		<a href="#">커뮤니티</a>
		<ul class="topList-depth">
			<li><a href="<%=request.getContextPath()%>/board/list.do?category=1">공지사항</a></li>
			<li><a href="<%=request.getContextPath()%>/board/list.do?category=2">자유 게시판</a></li>
			<li><a href="<%=request.getContextPath()%>/board/list.do?category=3">질문 게시판</a></li>
		</ul>
	</li>
	<li>
		<a href="#">정보 게시판</a>
		<ul class="topList-depth">
			<li><a href="<%=request.getContextPath()%>/board/list.do?category=4">게임소식</a></li>
			<li><a href="<%=request.getContextPath()%>/board/list.do?category=5">일상소식</a></li>
		</ul>
	</li>
	<li>
		<a href="<%=request.getContextPath()%>/board/gallery.do?category=6">갤러리</a>
	</li>			
</ul>