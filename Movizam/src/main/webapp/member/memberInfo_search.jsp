<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import= "domain.MemberVo" %>  
   <%
   MemberVo mv = (MemberVo)request.getAttribute("mv");
    
   %>
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 찾기</title>
<link rel="stylesheet" href="/Movizam/member/memberInfo_search.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>


<script>

//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			$(".menutab_content").slideToggle("fast");
		})

	});



function check(){
	
	// 정규식
	//이름 정규식
	var nameJ = /^[가-힣]{2,6}$/;
	var phoneJ = /^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/;
	
	var fm =document.frm; 
		//이름검사
	if(fm.memberName.value==""){
		$("#name_check").html("이름을 입력해주십시오");
		$("#name_check").css("color","red");
		$("#name_check").css("font-size","16px");
		fm.memberName.focus();
		return;
	}else if(!nameJ.test($("#memberName").val())){
		alert("이름은 두글자 이상 여섯글자 이내로 작성해주십시오") 
		$("#memberName").val(""); 
		$("#memberName").focus(); 
		return; 
	
		//생년월일 검사
	}else if(fm.birth_yy.value==""){
		$("#birth_check").html("출생연도를 입력해주십시오");
		$("#birth_check").css("color","red");
		$("#birth_check").css("font-size","16px");
		fm.birth_yy.focus();
		return;	
	}else if ($("#mm > option:selected").val()==""){
		$("#birth_check").html("출생월을 선택해주십시오");		
		$("#birth_check").css("color","red");
		$("#birth_check").css("font-size","16px");
		fm.birth_mm.focus();
		return;
	}else if(fm.birth_dd.value==""){
		$("#birth_check").html("출생일을 입력해주십시오");
		$("#birth_check").css("color","red");
		$("#birth_check").css("font-size","16px");
		fm.birth_dd.focus();
		return;
		
		//전화번호 검사
	}else if(fm.memberPhone.value==""){
		$("#phone_check").html("전화번호를 입력해주십시오");
		$("#phone_check").css("color","red");
		$("#phone_check").css("font-size","16px");
		fm.memberPhone.focus();
		return;	
	}else if(!phoneJ.test($("#memberPhone").val())){
		alert("전화번호는 '-'를 제외하고 입력해주시길 바랍니다.") 
		$("#memberPhone").val(""); 
		$("#memberPhone").focus(); 
		return; 
	}
		
		
	fm.action ="<%=request.getContextPath()%>/member/memberInfo_searchAction.do"; 	//서블릿이 가짜 경로를 받아서 서블릿에서 처리하도록하기
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
<form name="frm" action="<%=request.getContextPath()%>/main/mainSearchAction.do" method="post">
	<input type="text" name="keyword" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn" onclick="location.href='<%=request.getContextPath()%>/main/mainSearchAction.do'"><img src="../image/search.png" alt="searchButton"></button>
</form>
</div>

<hr id="pink_hr">


</header>





<!----------------- main ---------------------->
<div id="flexOut">
<main>
<div id="flex1">
<!-- 정보찾기 -->
	<span id="title1">아이디, 비밀번호 찾기</span>

	<form name=frm id="form">
		<label><span>이름</span><input type="text" name="memberName" id="memberName" placeholder="이름" size="30"></label>
		<div class="check_css" id="name_check"></div>

	<label><span>생년월일</span>     
	<!-- BIRTH_YY -->
	<input type="text" name="birth_yy" id="yy" class="int" maxlength="4" placeholder="년(4자)"  size="10">
	<!-- BIRTH_MM -->
	<select id="mm" class="int" name="birth_mm">                                 
		<option value="01">1</option>
		<option value="02">2</option>
		<option value="03">3</option>
		<option value="04">4</option>
		<option value="05">5</option>
		<option value="06">6</option>
		<option value="07">7</option>
		<option value="08">8</option>
		<option value="09">9</option>
		<option value="10">10</option>
		<option value="11">11</option>
		<option value="12">12</option>                               
	</select>
	<!-- BIRTH_DD -->
	<input type="text" name="birth_dd" id="dd" class="int" maxlength="2" placeholder="일" size="8">  
	</label> <div class="check_css" id="birth_check"></div>
	<!-- 전화번호 -->
	<label><span>전화번호</span>
		<input type="text" name="memberPhone"  id="memberPhone" placeholder="'-' 없이 작성해주세요" size="30"></label>
	<div class="check_css" id="Phone_check"></div>

	<div class=wrapper>
	<input type="button" id="bt" name= "button" id= "button" onclick= "check();" value="조회하기">
	</div>
</form>
</div>

<div id="separate"></div>

<div id="flex2">
<!-- 고객정보반환 -->
<span id="title2">조회된 고객님의 정보입니다.</span>

<div id="return">
<div id="memberIdReturn">
<%if(mv!=null){%><!-- 처음 화면에 들어올때 는 널값이므로 조회 후 널값이 아닐때 띄어주기 -->
<%=mv.getMemberId() %>	
<% }%>
</div>
<div id="memberPwdReturn">
<%if(mv!=null){%>
<%=mv.getMemberPwd() %>	
<% }%>
</div>
</div>


<div id="info">
<a href="<%=request.getContextPath()%>/member/memberJoin.do">아직회원이 아니신가요? 회원가입하러가기</a><br>
</div>

<div id="info2">
<a href="<%=request.getContextPath()%>/member/memberLogin.do">정보를 찾으셧나요? 로그인하러가기</a>
</div>
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