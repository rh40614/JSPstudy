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
	.one-box{
		display: inline-block;
		width: 180px;
		margin: 10px 5px;
	}
	.onebox div{
		width: 100%;
	}
	.list-all{
		cursor: pointer;
	}
	.list-title{
		text-align: center;
	}
	.list-content {
		color: #b3bcc4;
		width: 100%;
		margin-top: 7px;
		font-size: 15px;
		overflow: hidden;
		display:-webkit-box;
		-webkit-line-clamp: 1; /* 라인수 */
		-webkit-box-orient: vertical;
		word-wrap:break-word;
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
		<h4>갤러리</h4>
		<hr>
		<div class="board-box">
			<%
			for(BoardVo bv : alist){
				if(bv.getCategory() != (int) session.getAttribute("category"))continue; %>
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
		<br>
		<script>
			function goList(obj){
				window.location.href="<%=request.getContextPath()%>/board/content.do?bidx="+obj;
			}
		</script>
		
		<table id="numberTable">
			<tr>
			<td>
				<% 
				if(pm.isPrev() == true){out.println("<a href='"+request.getContextPath()+"/board/gallery.do?page="+(pm.getStartPage()-1)+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");}
				%>
			</td>
			<td>
				<%
					for(int i=pm.getStartPage(); i<=pm.getEndPage();i++){
						out.println("<a href='"+request.getContextPath()+"/board/gallery.do?page="+i+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
					}
				%>
			</td>
			<td>
				<% if(pm.isNext() && pm.getEndPage() >0){
					out.println("<a href='"+request.getContextPath()+"/board/gallery.do?page="+(pm.getEndPage()+1)+"&category="+session.getAttribute("category")+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
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
				window.location = "<%=request.getContextPath()%>/board/gallery.do?category=<%=session.getAttribute("category")%>" + "&keyword="+keyword+"&searchType="+type;
			});
		</script>
	</section>
	
</article>
</body>
</html>