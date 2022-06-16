<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import ="jspstudy.dbconn.Dbconn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>main page</title>
<link rel="stylesheet" href="index.css">
</head>
<body>
<%
Dbconn dbconn = new Dbconn();
out.print(dbconn);
%>


<header>
<h1>메인 페이지 입니다.</h1>
</header>
<!-- 클라이언트가 클릭을하면 주소에 따라서 서버로 이동한다. 그런데 우리는 web.xml에 확장자가 .do 이면 FrontController로 넘기라고 전달해놓음.
그리고 프론트 컨트롤러로 들어왔을때 두번째 단어가 멤버이면 멤버 컨트롤러, 보드이면 보드 컨트롤러로 넘어가도록함-->
<nav>
<h2><a href="<%=request.getContextPath()%>/member/memberJoin.do">회원가입하기</a></h2><!-- 확장자가 .do 이므로 FrontController로 넘어간다. web.xml에 지정해놓음 -->
<h2><a href="<%=request.getContextPath()%>/member/memberLogin.do">로그인</a></h2>	
<h2><a href="<%=request.getContextPath()%>/member/memberList.do">회원목록 가기</a></h2>
<h2><a href="<%=request.getContextPath()%>/board/boardWrite.do">게시글 작성하기</a></h2>
<h2><a href="<%=request.getContextPath()%>/board/boardList.do">게시글 목록</a></h2>
</nav>
</body>
</html>