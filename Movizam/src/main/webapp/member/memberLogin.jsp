<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" href="/Movizam/member/memberLogin.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>
<script>

//메뉴 탭(공통)

    $(function(){
	$("#menu").click(function(){
		$(".menutab_content").slideToggle("fast");
	})

});
	

function logout(){
	alert("로그아웃되었습니다.")
}



function check(){	
	
	// 정규식
	var idJ = /^[a-z0-9]{4,12}$/;
	var pwJ = /^[A-Za-z0-9]{4,12}$/; 
	
	var fm =document.frm; 
	
	//아이디 검사
	var id= $("#memberId").val();
		
	if(id == ""){ 
		$("#id_check").html("아이디를 입력해 주십시오");
		$("#id_check").css("color","red");		
		$("#id_check").css("font-size","16px");
		fm.memberId.focus();		
		return;
	}else if(!idJ.test(id)){
		alert("아이디는 영어소문자, 숫자를 포함하여 4글자 이상 12글자 이내로 작성해주십시오.") 
		$("#memberId").val(""); 
		$("#memberId").focus(); 
		return;
	}else{		
		$("#id_check").removeAttr("style");
		$("#id_check").html('');
	}
	
	//비밀번호 검사
	var pwd= $("#memberPwd").val();
	
	if(pwd==""){ 
		$("#pwd_check").html("비밀번호를 입력해 주십시오");
		$("#pwd_check").css("color","red");
		$("#pwd_check").css("font-size","16px");
		fm.memberPwd.focus();
		return; 
	
	}else if(!pwJ.test(pwd)){
		alert("비밀번호는 영어 대소문자, 숫자를 포함하여 4글자 이상 12글자 이내로 작성해주십시오.") 					
		$("#memberPwd").val(""); 
		$("#memberPwd").focus(); 
		return; 
	
	}else{
		$("#pwd_check").removeAttr("style");
		$("#pwd_check").html('');
	}
	
	
	var fm =document.frm; 
		
	fm.action ="<%=request.getContextPath()%>/member/memberLoginAction.do"; 	
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


<!-- main -->
<main>
<div class="welcome">
<span id="welcome">어서오세요 :-)</span>
</div>

<form name=frm id="form">
<label><input type="text" name="memberId" id="memberId" placeholder="아이디를 입력해주세요" maxlength="20" size="50"></label>
<div class="check_css" id="id_check"></div>
<label><input type="password" name="memberPwd" id="memberPwd" placeholder="비밀번호를 입력해주세요" maxlength="15" size="50"></label>
<div class="check_css" id="pwd_check"></div>

<div class=wrapper><button id="bt" type="button" name= "button" id= "button"  value="submit"  onclick= "check();">로그인</button></div>

</form>

<div id="info">
<a href="<%=request.getContextPath()%>/member/memberJoin.do">아직회원이 아니신가요? 회원가입하러가기</a><br>
<a href="<%=request.getContextPath()%>/member/memberInfo_search.do">아이디, 비밀번호를 잊으셨나요? 찾으러가기</a>
</div>

</main>



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