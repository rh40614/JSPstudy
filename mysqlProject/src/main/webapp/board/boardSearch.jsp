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
	.board-list ul{
		list-style: none;
		padding: 5px 0;
		border-bottom: 1px solid #f5ecbc;
	}
	.board-list li{
		text-indent: 10px;
		font-size: 16px;
	}
	.list-content {
		color: #b3bcc4;
		width: 780px;
		margin-top: 7px;
		font-size: 15px;
		overflow: hidden;
		text-overflow: ellipsis;
		display:-webkit-box;
		-webkit-line-clamp: 3; /* 라인수 */
		-webkit-box-orient: vertical;
		word-wrap:break-word;
	}
	.list-bottom{
		font-size: 14px;
		margin-top: 7px;
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
		<h4><%=session.getAttribute("keyword")%>의 검색 결과</h4>
		<hr>
		<div class="board-box">
			
			<div class="board-list">
				<% for(BoardVo bv : alist){%>
				<ul>
					<li>
						<div class="list-title"><a href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"><%=bv.getTitle()%> <span style="color: #ed8040; font-size: 13px;">[<%=bv.getBoardComment()%>]</span></a></div>
						<div class="list-content"><%=bv.getContent()%></div>
						<div class="list-bottom">
							<span style="color: #d3db3b;"><%
								if(bv.getCategory()==1){out.print("공지사항");}
								else if(bv.getCategory()==2){out.print("자유게시판");}
								else if(bv.getCategory()==3){out.print("질문게시판");}
								else if(bv.getCategory()==4){out.print("게임소식");}
								else if(bv.getCategory()==5){out.print("일상소식");}
								else if(bv.getCategory()==6){out.print("갤러리");}
							%></span>
							<span><%=bv.getWriteday()%></span></div>
					</li>
					
				</ul>
				
				<%} %>
			</div>
		</div>
		<br>
		
		<table id="numberTable">
			<tr>
			<td>
				<% if(pm.isPrev() == true){
					out.println("<a href='"+request.getContextPath()+"/board/search.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>◀</a>");
					}
				%>
			</td>
			<td>
				<%
					for(int i=pm.getStartPage(); i<=pm.getEndPage();i++){
						out.println("<a href='"+request.getContextPath()+"/board/search.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
					}
				%>
			</td>
			<td>
				<% if(pm.isNext() && pm.getEndPage() >0){
					out.println("<a href='"+request.getContextPath()+"/board/search.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.getScri().getKeyword()+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
					}
				%>
			</td>
			</tr>	
		</table>
		
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
				window.location = "<%=request.getContextPath()%>/board/search.do?category=0" + "&keyword="+keyword+"&searchType="+type;
			});
		</script>
	</section>
	
</article>
</body>
</html>