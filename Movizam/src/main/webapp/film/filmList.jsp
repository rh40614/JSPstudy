<%@page import="domain.PageMaker"%>
<%@page import="java.util.ArrayList"%>
<%@page import="domain.FilmVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
ArrayList<FilmVo> alist = (ArrayList<FilmVo>)request.getAttribute("alist");
PageMaker pm =(PageMaker)request.getAttribute("pm");
%>

 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>영화목록관리</title>
<link rel="stylesheet" href="/Movizam/film/filmList.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>
</head>


<script>
//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			 $(".menutab_content").slideToggle("fast"); 
		
		})

	});

//로그아웃 성공//ajax로 바꾸기
	function logout(){
		alert("로그아웃되었습니다.")
	}

//레이어 팝업 //화면이 로딩되면 숨겨져있는 상태
	$(function(){
		$(".popup").css("display","none");
	})







//레이어 팝업 온(정말로 삭제 하시겠습니까? 예)	// 해당되는 영화의 fidx를 매개변수로 넘겨서 확인을 누르면 그 fidx 에 해당하는 영화를 처리할 수 있도록 하기
	function openFn(idx){
		$(".popup").css("display","block");
		
		//이미 함수에서 받았기때문에 여기서는 매개변수를 다시받지 않아도 된다. 여기서 받으면 버튼 자체의 매개변수로 지정이 되어서 object 타입이 되어버린다. 
		//클래스 .btn으로 잡게되면 아니요 버튼도 그 클래스는 가지고 있기때문에 아니요를 눌러도 삭제가 됨으로 아이디를 줘서 삭제한다. 
		if($("#btnY").click(function(){	
		 location.href='<%=request.getContextPath() %>/film/filmDelete.do?fidx='+idx;  
		})){
			
		}
	}  

//레이어팝업 닫기(아니요)
	function cancleFn(){
		$(".popup").css("display","none");
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

<script>
function logout(){
	alert("로그아웃되었습니다.")
}

</script>
</span>

<hr id="first_hr">



<!-- 검색창 -->
<div class="search">
<form name="frm" action="<%=request.getContextPath()%>/main/mainSearchAction.do" method="post">
	<input type="text" name="keyword" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn" onclick="location.href='<%=request.getContextPath()%>/main/mainSearchAction.do'"><img src="../image/search.png" alt="searchButton"></button>
</form>
</div>

<hr id="pink_hr">


</header>

<div id="navmainWrap">


<!-- nav1 -->
<nav id="nav">

<ul class="tab_title">
	<li ><a href="<%=request.getContextPath() %>/film/filmInsert.do">영화 등록(관리자)</a></li>
	<li class="on"><a href="<%=request.getContextPath() %>/film/filmList.do">영화 수정/삭제(관리자)</a></li>
</ul>

</nav>

<div id="separate"></div>

<!-- main -->
<main>

<div class="tab_content">
<!-- tab2 content -->
	<div>
		<span>| 영화 수정/삭제 </span>

		<table class="filmList">
		<thead>
			<tr>
				<th>NO.</th><th>카테고리</th><th>영화제목</th><th>개봉일자</th><th>상영상태</th><th>수정/삭제</th>
			</tr>
		</thead>
		<tbody>
		<% for(FilmVo fv: alist){%> 	
			<tr>
				<td><%=fv.getFidx() %></td>
				<td><%=fv.getFilmCategory() %></td>
				<td><%=fv.getFilmName() %></td>
				<td><%=fv.getFilmDate() %></td>
				<td><%=fv.getFilmStatus() %></td>
				<td>
					<button id="editbtn" onclick="location.href='<%=request.getContextPath() %>/film/filmView.do?fidx=<%=fv.getFidx()%>'">수정</button>	<!--해당되는 fidx 는 파라미터로 보내기!  -->
					<button id="deletebtn" onclick="openFn(<%=fv.getFidx()%>)">삭제</button>
				</td>
				<% } %>
			</tr>
		</tbody>
		</table>
	</div>
</div>

<!-- 페이징 -->
<table id="paging">
<tr>
<td>
<% 
//이전페이지가 있으면 이전페이지로. page는 sql코드로 1부터 넘어오지만 페이지는 0이 1페이지 이다. 
if(pm.isPrev() == true){	
	out.print("<a href='"+request.getContextPath()+"/film/filmList.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType() +"'>◀</a>");
}
%>
</td>
<td>
<% 
//out.println(pm.getStartPage());
//out.println(pm.getEndPage());

for(int i=pm.getStartPage(); i<= pm.getEndPage();i++){
	out.print("<a href='"+request.getContextPath()+"/film/filmList.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
}

%>
</td>
<td>
<% //다음장이 있고 마지막 페이지가 0보다크면 다음장을 가지고온다. 
if(pm.isNext() && pm.getEndPage() >0 ){ 
 
	out.print("<a href='"+request.getContextPath()+"/film/filmList.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
}%>
</td>
</tr>

</table>

<!-- 검색창 -->
<form name="frm" action="<%=request.getContextPath() %>/film/filmList.do"  method="post"> 
<table id="searchK"> 
<tr>
<td>
<select>
<option value="filmName">이름</option>

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




 <!-- 팝업  --> 
<div class="popup">
	<!-- 팝업창 -->
	<div class="popup_layer"> 
		<div class="text_area"><!-- 텍스트 영역 -->
			<strong class="title">삭제 하시겠습니까?</strong>
		</div>
		<div class="btn_area"><!-- 버튼 영역 -->
			<button type="button" name="button" class="btn" id="btnY" >예</button>  
			<button type="button" name="button" class="btn no"  onclick="cancleFn()">아니오</button>
		</div>
	</div>
	<!-- 반투명 배경 -->
	<div class="popup_dimmed"></div>
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
