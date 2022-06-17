<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="mysqlProject.service.BoardDao"%>
<%@page import="mysqlProject.domain.BoardVo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
BoardDao bd = new BoardDao();
ArrayList<BoardVo> alist = bd.boardSelectAll();
ArrayList<BoardVo> alist2 = bd.boardGalleryIndexAll();
session.setAttribute("saveUrl", request.getRequestURI());
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>홈페이지d</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/basic.css">
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<style type="text/css">@import url("<%=request.getContextPath()%>/css/index.css");</style>
</head>
<body>
<article>
<!----------------------------------------------------------------------------------------------------------------------------->
	<header id="header"></header>
	<nav id="topNav"></nav>
	<nav id="leftNav"></nav>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#header").load("<%=request.getContextPath()%>/article/header.jsp");
			$("#topNav").load("<%=request.getContextPath()%>/article/topNav.jsp");
			$("#leftNav").load("<%=request.getContextPath()%>/article/leftNav.jsp");
		});
	</script> 
<!----------------------------------------------------------------------------------------------------------------------------->
	<section>
		<h4>홈페이지 입니다</h4>
		<hr>
		<div class="board-box">
			<div class="board-video">
				<iframe src="https://www.youtube.com/embed/jZwv83Stl60?autoplay=1&mute=1&loop=1"></iframe>
			</div>
			<div class="board-top-box">
				<span>중요 공지사항</span>
				<a class="board-view-all" href="<%=request.getContextPath()%>/board/list.do?category=1">전체 공지사항</a>
			</div>
			<div class="board-list">
				<% for(BoardVo bv : alist){if(bv.getNotice().equals("N"))continue;%>
				<ul>
					<li><%out.print("공지"); %></li>
					<li style="color: #ff644d; font-weight: bolder;"><%if(bv.getLevel_()==1)out.print(" ㄴ ");%><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getWriteday()%></li>
					<li><%=bv.getBoardViews()%></li>
				</ul>
				<%} %>
			</div>
		</div>
		<br>
		<div class="board-box">
			<div class="board-top-box"><span>전체 글</span><a class="board-view-all" href="<%=request.getContextPath()%>/board/list.do">전체 글 보기</a></div>
			<div class="board-list">
				<%int i=0;for(BoardVo bv : alist){if(i==30){break;}%>
				<ul>
					<li><%
							if(bv.getCategory() == 1){
								out.print("공지");
							}else out.print("▪");
						%>	
					</li>
					<li><%if(bv.getLevel_()==1)out.print(" <span style='font-size:13px'> ↳ </span> ");%><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getWriteday()%></li>
					<li><%=bv.getBoardViews()%></li>
				</ul>
				<%i++;} %>
			</div>
		</div>
		<br>
		<div class="board-box">
			<div class="board-top-box"><span>갤러리</span><a class="board-view-all" href="<%=request.getContextPath()%>/board/gallery.do?category=6">더 보기</a></div>
			<div class="board-list">
				<%for(BoardVo bv : alist2){%>
				<div class="one-box">
				<div class="list-all" onclick="goList(<%=bv.getBidx()%>)">
					<div class="list-title"><%=bv.getTitle()%><span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></div>
					<div class="list-content"><%=bv.getContent()%></div>
				</div>
				<div class="list-title" style="margin-top: 5px;"><%=bv.getWriter()%></div>
				</div>
				<script>$(".list-content>img").css("width","100%").css("height","160px");</script>
				<%} %>
			</div>
			<script>
			function goList(obj){
				window.location.href="<%=request.getContextPath()%>/board/content.do?bidx="+obj;
			}
		</script>
		</div>
		<br>
		<div class="half-box">
			<div class="board-top-box"><span>자유 게시판</span><a class="board-view-all" href="<%=request.getContextPath()%>/board/list.do?category=2">더 보기</a></div>
			<div class="half-list">
				<%int j=0; for(BoardVo bv : alist){if(j==7){break;}if(bv.getCategory() != 2)continue; %>
				<ul>
					<li><%=bv.getBidx()%></li>
					<li><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 11px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getBoardViews()%></li>
				</ul>
				<%j++;} %>
			</div>
		</div>
		<div class="half-box">
			<div class="board-top-box"><span>게임 소식</span><a class="board-view-all" href="<%=request.getContextPath()%>/board/list.do?category=4">더 보기</a></div>
			<div class="half-list">
				<%int k=0; for(BoardVo bv : alist){if(k==7){break;}if(bv.getCategory() != 4)continue; %>
				<ul>
					<li><%=bv.getBidx()%></li>
					<li><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 11px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getBoardViews()%></li>
				</ul>
				<%k++;} %>
			</div>
		</div>
		<br>
	</section>
</article>
</body>
</html>