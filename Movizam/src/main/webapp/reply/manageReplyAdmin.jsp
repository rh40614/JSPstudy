<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "domain.*" %>  
<%@ page import= "java.util.*" %>

<%	
	ArrayList<ReplyVo> alist = (ArrayList<ReplyVo>)request.getAttribute("alist");
	PageMaker pm = (PageMaker)request.getAttribute("pm");
%>

   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>정보 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/reply/manageReply.css">
<script src="<%=request.getContextPath() %>/js/jquery-3.6.0.min.js"></script>


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
	<input type="text" name="searchBar" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn"><img src="../image/search.png" alt="searchButton"></button>
</div>

 <hr id="pink_hr">


</header>



<div id="navmainWrap"> 
<nav id="nav">
<ul class="tab_title">
	<li ><a href="<%=request.getContextPath()%>/member/memberInfo.do">내 정보 보기</a></li>
	<% if(session.getAttribute("adminYN").equals("Y")){	%>
	<li class="on"><a href="<%=request.getContextPath()%>/reply/manageReplyAdmin.do">댓글 관리(관리자)</a></li>
	<%	}else if(session.getAttribute("adminYN").equals("N")){%> 
	<li class="on"><a href="<%=request.getContextPath()%>/reply/manageReply.do">댓글 관리</a></li>
	<%	} %> 
	<% if(session.getAttribute("adminYN").equals("Y")){	%>
		<li><a href="<%=request.getContextPath()%>/member/memberList.do">회원 목록(관리자)</a></li>
	<%	} %> 
	
</ul>
</nav>

<div id="separate"></div>

<!-- main -->
<main>

<div class="tab_content">
<span>| 댓글 관리(관리자)</span>
<table class="memberList">
<thead>
<tr>
<th>회원번호</th><th>게시번호</th><th>댓글 내용</th><th>블라인드여부</th><th>블라인드/삭제</th>
</tr>
</thead>
<tbody>
<% for(ReplyVo rv : alist){%>
	
<tr>
	<td style="width: 50px;"><a href= "<%=request.getContextPath()%>/member/memberInfoAdmin.do?midx=<%=rv.getMidx() %>"><%=rv.getMidx() %></a></td>
	<td style="width: 50px;"><a href= "<%=request.getContextPath()%>/board/boardContent.do?bidx=<%=rv.getBidx() %>"><%=rv.getBidx() %></a></td>
	<td style="text-align: left; padding-left: 30px;"><a href= "<%=request.getContextPath()%>/board/boardContent.do?bidx=<%=rv.getBidx() %>"><%=rv.getReplyContent() %></a></td>
	<td style="width: 150px;"><%=rv.getBlindYN() %></td>
	<td style="width: 250px;">
	<button id="bilndbtn" onclick="location.href='<%=request.getContextPath() %>/reply/replyBlindOn.do?ridx=<%=rv.getRidx()%>'">블라인드</button>	
	<button id="bilndbtn" onclick="location.href='<%=request.getContextPath() %>/reply/replyBlindOff.do?ridx=<%=rv.getRidx()%>'">블라인드해제</button>	
	</td>


</tr>
<% } %>	
</tbody>

</table>
</div>
<!-- 페이징 -->
<table id="paging">
<tr>
<td>
<% 
//이전페이지가 있으면 이전페이지로. page는 sql코드로 1부터 넘어오지만 페이지는 0이 1페이지 이다. 
if(pm.isPrev() == true){	
	out.print("<a href='"+request.getContextPath()+"/reply/manageReply.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType() +"'>◀</a>");
}
%>
</td>
<td>
<% 
//out.println(pm.getStartPage());
//out.println(pm.getEndPage());

for(int i=pm.getStartPage(); i<= pm.getEndPage();i++){
	out.print("<a href='"+request.getContextPath()+"/reply/manageReply.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
}

%>
</td>
<td>
<% //다음장이 있고 마지막 페이지가 0보다크면 다음장을 가지고온다. 
if(pm.isNext() && pm.getEndPage() >0 ){ 
 
	out.print("<a href='"+request.getContextPath()+"/reply/manageReply.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
}%>
</td>
</tr>

</table>

<!-- 검색창 -->
<form name="frm" action="<%=request.getContextPath() %>/reply/manageReply.do"  method="post"> 
<table id="searchK"> 
<tr>
<td>
<select>
<option value="replyContent">내용</option>
<option value="replyWriter">작성자</option>

</select>
</td>
<td>
<input type="text" name ="keyword" size="30"> 
</td>
<td>
<input type="submit" name ="submit" value="검색"> 
</td>
</tr>

</table>
</form>
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