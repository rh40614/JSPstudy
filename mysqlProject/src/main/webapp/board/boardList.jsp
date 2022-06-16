<%@page import="mysqlProject.domain.PageMaker"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="mysqlProject.service.BoardDao"%>
<%@page import="mysqlProject.domain.BoardVo"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	@SuppressWarnings("unchecked")
	ArrayList<BoardVo> alist = (ArrayList<BoardVo>) request.getAttribute("alist");
	BoardDao bd = new BoardDao();
	ArrayList<BoardVo> alist2 = bd.boardSelectAll();
	PageMaker pm = (PageMaker) request.getAttribute("pm");

	
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>홈페이지</title>
	<link rel="stylesheet" href="../css/basic.css">
<style type="text/css">
	section a{
		text-decoration: none;
		color: inherit;
	}
	.board-box{
		width: 780px;
		height: auto;
		display: block;
		margin: 0 auto;
		border: 0;
	}
	.half-box{
		width: 378px;
		height: auto;
		float: left;
		display: inline-block;
		margin-left: 14px;
		border: 0;
	}
	.board-top-box{
		height: 45px;
		border-bottom: 1px solid #edcc11; 
	}
	.board-top-box >span{
		float: left;
		font-size: 17px;
		font-weight: bolder;
		color: #6df7eb;
		margin: 11px 0 11px 8px;
	}
	.board-top-box >a{
		text-decoration: none;
		float: right;
		font-size: 12px;
		color: inherit;
		margin: 19px 5px 11px 0;
	}
	
	.board-list ul{
		list-style: none;
		padding: 5px 0;
		border-bottom: 1px solid #f5ecbc;
	}
	.board-list li{
		display: inline-block;
		text-align: center;
		font-size: 15px;
	}
	.board-list ul>li:first-child {width: 50px;}
	.board-list ul>li:nth-child(2) {width: 465px; text-align: inherit; text-indent: 10px;}
	.board-list ul>li:nth-child(3) {width: 100px;}
	.board-list ul>li:nth-child(4) {width: 70px;}
	.board-list ul>li:last-child {width: 70px;}
	
	#numberTable{
		margin: 0 auto;
		text-align: center;
	}
	#numberTable a{
		text-decoration: none;
		color: inherit;		
	}
	#numberTable a:active {
		font-weight: bolder;
		color: blue;
		text-decoration: underline;
	}
</style>

<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
</head>
<body>
<article>
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
		<h4><%
			if(session.getAttribute("category").equals(0)){out.print("전체글보기");}	
			else if(session.getAttribute("category").equals(1)){out.print("공지사항");}
			else if(session.getAttribute("category").equals(2)){out.print("자유게시판");}
			else if(session.getAttribute("category").equals(3)){out.print("질문게시판");}
			else if(session.getAttribute("category").equals(4)){out.print("게임소식");}
			else if(session.getAttribute("category").equals(5)){out.print("일상소식");}%></h4>
		<hr>
		<div class="board-box">
			
			<div class="board-list">
				<% for(BoardVo bv : alist2){if(bv.getNotice().equals("N"))continue;%>
				<ul>
					<li><%out.print("공지"); %></li>
					<li style="color: #ff644d; font-weight: bolder;"><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getWriteday()%></li>
					<li><%=bv.getBoardViews()%></li>
				</ul>
				<%} %>
			</div>
		</div>
		<div class="board-box">
			<div class="board-list">
				<% 
				if(session.getAttribute("category").equals(0)){
					for(BoardVo bv : alist){%>
					<ul>
					<li><%=bv.getBidx()%></li>
					<li><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></li>
					<li><%=bv.getWriter()%></li>
					<li><%=bv.getWriteday()%></li>
					<li><%=bv.getBoardViews()%></li>
					</ul>
				<% }} else{
					for(BoardVo bv : alist){
						if(bv.getCategory() != (int) session.getAttribute("category"))continue; %>
					<ul>
						<li><%=bv.getBidx()%></li>
						<li><%if(bv.getLevel_()==1)out.print(" ↳ ");%><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></li>
						<li><%=bv.getWriter()%></li>
						<li><%=bv.getWriteday()%></li>
						<li><%=bv.getBoardViews()%></li>
					</ul>
				<%}} %>
			</div>
		</div>
		<br>
		
		<table id="numberTable">
			<tr>
			<td>
				<% 
				if(pm.isPrev() == true){out.println("<a href='"+request.getContextPath()+"/board/list.do?page="+(pm.getStartPage()-1)+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");}
				%>
			</td>
			<td>
				<%
					for(int i=pm.getStartPage(); i<=pm.getEndPage();i++){
						out.println("<a href='"+request.getContextPath()+"/board/list.do?page="+i+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
					}
				%>
			</td>
			<td>
				<% if(pm.isNext() && pm.getEndPage() >0){
					out.println("<a href='"+request.getContextPath()+"/board/list.do?page="+(pm.getEndPage()+1)+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
					}
				%>
			</td>
			</tr>
		</table>
		
		<br>
		<div class="search-box" style="width: 390px; height: 30px; margin: auto;">
			<select class="search-select" name="searchType" style="height: 30px;">
				<option value="title">제목</option>
				<option value="content">내용</option>
				<option value="writer">작성자</option>
				<option value="title-content">제목 + 내용</option>
			</select>
			<input type="text" name = "keyword" class="search-keyword" placeholder="검색" style="width: 250px; height: 26px;">
			<input type="button" class = "search-btn" value="🔍" style="width: 30px; height: 30px;">
		</div>
		<script>
			$(".search-btn").click(function(){
				let type = $(".search-select").val();
				let keyword = $(".search-keyword").val();
				window.location = "<%=request.getContextPath()%>/board/list.do?category=<%=session.getAttribute("category")%>" + "&keyword="+keyword+"&searchType="+type;
			});
		</script>
	</section>
	
</article>
</body>
</html>