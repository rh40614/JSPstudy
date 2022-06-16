<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "domain.*" %> 
<%@ page import= "java.util.*" %>

<%	

	/*로그인을 하지않고 글을 작성하려고 하는경우에 alert를 띄어주고 로그인페이지로 이동시켜준다.*/
	if(session.getAttribute("midx") == null){
	//게시물을 작성하려했으나 로그인을하고 돌아오는 경우
	session.setAttribute("saveURI",request.getRequestURI());
	
	out.println("<script>alert('로그인 후 사용가능합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
}
%>


   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Movizam - 게시글 작성</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/board/boardWrite.css">
<script src="<%=request.getContextPath() %>/js/jquery-3.6.0.min.js"></script>

<script>


//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			 $(".menutab_content").slideToggle("fast"); 
		
		})

	});
 
 
 
 function checkFn(){
	 
	 var fm = document.frm;
	 
	 
	 if($("#title").val()==""){
			alert("제목을 입력해주세요");
			$("#title").focus();
			return;	//return 안하면 다른 내용도 다지워짐!
		}else if($("#textarea").val()==""){
			alert("내용을 작성해주세요");
			$("#textarea").focus();
			return;
		}else if($("#boardType > option:selected").val()==""){
			//selectbox는 optiondml select로 비교
			alert("게시판을 선택해주세요");
			$("#boardType").focus();
			return;
		}
		
	
		fm.action ="<%=request.getContextPath()%>/board/boardWriteAction.do"; 	
		fm.method ="post";
		fm.submit();
			
		return;
		 
 }
 
 
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
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList.do">자유게시판</a></li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList_film.do">영화게시판</a></li>
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
	<li><a href="<%=request.getContextPath()%>/board/boardHit.do">인기게시판</a></li>
	<li><a href="<%=request.getContextPath()%>/board/boardLike.do">추천게시판</a></li>
	<li><a href="<%=request.getContextPath()%>/board/boardList.do">자유게시판</a></li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList_film.do">영화게시판</a>
</ul>



</nav>

<div id="separate"></div>


<!---------------------- main ----------------------------->
<main>

<div class="tab_content">
<span>| 게시글 작성</span>

<form name="frm" enctype="multipart/form-data" method="post">
<table class="boardList">
<tr>
<td>
<select name="boardType" id="boardType" required> 
	<option value="" disabled selected>게시판옵션</option>
	<option value="free">자유게시판</option>
	<option value="film">영화게시판</option>
</select>
</td>
</tr>
<tr>
<td class="t1">제목</td>
<td>
<input type="text" name="title" id="title" >
</td>
</tr>
<tr>
<td class="t1">내용</td>
<td >
<textarea name="textarea" id="textarea"></textarea>
</td>
</tr>
<tr>
<td colspan="2" style="text-align: right;">
<input type="file" name="fileName" id="fileName">
</td>
</tr>

</table>
<button type="button" id="btn" onclick="checkFn()">작성하기</button>
</form>


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