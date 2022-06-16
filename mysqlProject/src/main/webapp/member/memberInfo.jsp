<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="mysqlProject.service.BoardDao"%>
<%@page import="mysqlProject.domain.BoardVo"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	BoardDao bd = new BoardDao();
	ArrayList<BoardVo> alist = bd.boardSelectAll();
	session.setAttribute("saveUrl", request.getRequestURI());
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>홈페이지</title>
	<link rel="stylesheet" href="../css/basic.css">
	<script src="/perProject/js/jquery-3.6.0.min.js"></script>
<style type="text/css">
	.info{width: 810px;	height: 100%;}
	.info>div{display: inline-block; height: 100%;}
	.info-left{
		width: 139px;
		float: left;
		border-right: 1px solid gray;
	}
	.info-left>div{
		width: 100%;
		height: 30px;
		text-align: center;
		font-size: 18px;
		margin: 40px 0;
		cursor: pointer; 
	}
	.info-left>div:hover{text-decoration: underline;}
	
	.info-right{
		width: 670px;
		margin: auto;
	}
	.info-right>iframe {
		display: block;
		width: 90%;
		margin: auto;
		height: 100%;
		border: 0;
	}
</style>
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
		<h4>내정보</h4>
		<hr>
		<div class="info">
			<div class="info-left">
				<div onclick='info("<%=request.getContextPath()%>/member/info_modify.jsp")'>정보 수정</div>
				<div onclick='info("<%=request.getContextPath()%>/member/infoBoard.do")'>게시글 보기</div>
				<div onclick='info("<%=request.getContextPath()%>/member/info_img.jsp")'>이미지 수정</div>
				<div onclick='info("<%=request.getContextPath()%>/member/info_del.jsp")'>회원탈퇴</div>
				
			</div>
			<div class="info-right">
				<iframe id="iframe-info" src=""></iframe>
			</div>
		</div>
		<script>function info(url){$('#iframe-info').attr('src',url);}</script>
	</section>
	
</article>
</body>
</html>