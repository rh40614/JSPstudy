<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>영화등록</title>
<link rel="stylesheet" href="/Movizam/film/filmInsert.css">
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

//영화 등록  //예 버튼을 누르면 영화등록 처리
function checkFn(){
	var fm = document.frm;
	
	
	if($("#filmCategory > option:selected").val()==""){
		alert("장르를 선택해주세요");
		$("#filmCategory").focus();
	}else if($("#filmName").val()==""){
		alert("영화 제목을 입력해주세요");
		$("#filmName").focus();
	}else if($("#filmDate").val()==""){
		alert("개봉일자를 입력해주세요");
		$("#filmDate").focus();
	}else if($("#filmStatus > option:selected").val()==""){
		alert("상영상태를 선택해주세요");
		$("#filmStatus").focus();
	}else if($("#filmDetail").val()==""){
		alert("영화 상세정보를 입력해주십시오.");
		$("#filmDetail").focus();
	}
	
	alert("정상 처리 되었습니다.");
	
	fm.action ="<%=request.getContextPath()%>/film/filmInsertAction.do"; 	
	fm.method ="post";
	fm.submit();
	
	
	
	return;
	
}





//레이어 팝업 //화면이 로딩되면 숨겨져있는 상태
	$(function(){
		$(".popup").css("display","none");
		$(".popup2").css("display","none");
	})
	
//레이아 팝업 온(등록하시겠습니까?)
	function enrollFn(){
		$(".popup").css("display","block");
	} 
 
//레이어팝업 닫기
	function cancleFn(){
		$(".popup").css("display","none");
}
	
/* 
 //영화등록에 성공하면 성공했다는 스트립트 올리기
 if(value == 1){
	$(".popup2").css("display","block");
	
}	

 */

 





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
	<input type="text" name="searchBar" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn"><img src="../image/search.png" alt="searchButton"></button>
</div>

 <hr id="pink_hr">


</header>

<div id="navmainWrap">

<!-- nav1 -->
<nav id="nav">

<ul class="tab_title">
	<li class="on"><a href="<%=request.getContextPath() %>/film/filmInsert.do">영화 등록(관리자)</a></li>
	<li><a href="<%=request.getContextPath() %>/film/filmList.do">영화 수정/삭제(관리자)</a></li>
</ul>

</nav>

<div id="separate"></div>

<!-- main -->
<main>

<div class="tab_content">
<!-- tab1 content -->
<div>
<span>| 영화 등록 </span>

<form name="frm" enctype="multipart/form-data" method="post"> 
<label><span>카테고리</span>
<select name="filmCategory" id="filmCategory" required>
	<!--태그 값을 선택하지않으면 넘어가지 않게 required 넣어주고 장르옵션은 disabled랑 selected로 제어하기  -->
	<option value="" disabled selected>장르</option>
	<option value="액션">액션</option>
	<option value="코미디">코미디</option>
	<option value="드라마">드라마</option>
	<option value="멜로&로맨스">멜로 &#38;로맨스</option>
	<option value="범죄,수사,스릴러">범죄,수사,스릴러</option>
	<option value="SF&판타지">SF,판타지</option>
	<option value="사극&시대극">사극&#38;시대극</option>
	<option value="실험&예술영화">실험&#38;예술영화</option>
	<option value="애니메이션">애니메이션</option>
	<option value="성인&에로">성인&#38;에로</option>
</select>
</label>

<label><span>제목</span><input type="text" name="filmName" id="filmName"></label>
<label><span>개봉일자</span><input type="text" name="filmDate" id="filmDate"></label>

<label><span>상영상태</span>
<select name="filmStatus" id="filmStatus" required>
	<option value="" disabled selected>상영상태</option>
	<option value="상영예정">상영예정</option>
	<option value="상영중">상영중</option>
	<option value="상영종료">상영종료</option>
</select>
</label>

<label style="width: 900px;"><span>상세정보</span><br>
<textarea name="filmDetail" id="filmDetail"></textarea></label>


<label  id="poster">
<input type= "file" name="filmPoster" id="filmPoster">
</label>

<button type="button"  id="enrollbtn" onclick="enrollFn()" >등록하기</button>
</form>
</div>
</div>



</main>
</div>

 <!-- 팝업  --> 
<div class="popup">
	<!-- 팝업창 -->
	<div class="popup_layer"> 
		<div class="text_area"><!-- 텍스트 영역 -->
			<strong class="title">등록하시겠습니까?</strong>
			<!-- <p class="text">등록 후 수정은 수정페이지를 이용해주십시오</p> -->
		</div>
		<div class="btn_area"><!-- 버튼 영역 -->
			<button type="button" name="button" class="btn" onclick="checkFn()" >예</button>
			<button type="button" name="button" class="btn no" onclick="cancleFn()">아니오</button>
		</div>
	</div>
	<div class="popup_dimmed"></div><!-- 반투명 배경 -->
</div>
 
<!-- 팝업2 - 정상처리 
<div class="popup2">
	 팝업창 
	<div class="popup_layer"> 
		<div class="text_area"><!-- 텍스트 영역 
			<strong class="title">정상 처리되었습니다.</strong>
		</div>
		<div class="btn_area"><!-- 버튼 영역 
			<button type="button" name="button" class="btn2" onclick="checkFn2()" >예</button>
			
		</div>
	</div>
	<div class="popup_dimmed"></div><!-- 반투명 배경 
</div> 
  --> 



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
