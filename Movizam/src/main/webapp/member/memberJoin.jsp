<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="/Movizam/member/memberJoin.css">
<script src="/Movizam/js/jquery-3.6.0.min.js"></script>


<script>

//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			$(".menutab_content").slideToggle("fast");
		})

	});



//모든 공백 체크 정규식
var empJ = /\s/g;
//아이디 정규식
var idJ = /^[a-z0-9]{4,12}$/;
//비밀번호 정규식
var pwJ = /^[A-Za-z0-9]{4,12}$/; 
//이름 정규식
var nameJ = /^[가-힣]{2,6}$/;
//이메일 검사 정규식
var mailJ = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
//휴대폰 번호 정규식
var phoneJ = /^01([0|1|6|7|8|9]?)?([0-9]{3,4})?([0-9]{4})$/;
//닉네임
var nickJ =/^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$/;




function check(){
	var fm= document.frm;

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
		
	}else if($("#memberPwd2").val() != pwd){				
		$("#pwd2_check").html("비밀번호가 다릅니다.");
		$("#pwd2_check").css("color","red");
		$("#pwd2_check").css("font-size","16px");
		$("#memberPwd2").val(""); 
		$("#memberPwd2").focus(); 
		return;
	}else{
		$("#pwd_check").removeAttr("style");
		$("#pwd_check").html('');
		$("#pwd2_check").removeAttr("style");
		$("#pwd2_check").html('');
	}
	
	
		//닉네임 검사
		
	 if($("memberNickname").val ==''){
		$("#nickname_check").html("닉네임을 입력해주십시오");
		$("#nickname_check").css("color","red");
		$("#nickname_check").css("font-size","16px");
		$("#memberNickname").focus(); 
		return;
	}else if(!nickJ.test($("#memberNickname").val())){
		alert("닉네임은 한글, 영문, 숫자만 가능하며 2-10자리 가능합니다.")
		$("#memberNickname").val(""); 
		$("#memberNickname").focus(); 
		return;
	}else{
		$("#nickname_check").removeAttr("style");
		$("#nickname_check").html('');
	}		
		
	
		
		
		
	//이름검사
	if($("memberName").val ==""){
		$("#name_check").html("이름을 입력해주십시오");
		$("#name_check").css("color","red");
		$("#name_check").css("font-size","16px");
		fm.memberName.focus();
		return;
	}else if(!nameJ.test($("#memberName").val())){
		alert("이름은 두글자 이상 여섯글자 이내로 작성해주십시오"); 
		$("#memberName").val(""); 
		$("#memberName").focus(); 
		return; 
	}else{
		$("#name_check").removeAttr("style");
		$("#name_check").html('');
	}
			
		
	//생년월일 검사
	if($("birth_yy").val ==""){
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
	}else if($("birth_dd").val ==""){
		$("#birth_check").html("출생일을 입력해주십시오");
		$("#birth_check").css("color","red");
		$("#birth_check").css("font-size","16px");
		fm.birth_dd.focus();
		return;
	}else{
		$("#birth_check").removeAttr("style");
		$("#birth_check").html('');
	}
			
			
			
	//주소 검사
	if($("memberAdr").val ==""){
		$("#adr_check").html("주소를 입력해주십시오");
		$("#adr_check").css("color","red");
		$("#adr_check").css("font-size","16px");
		fm.memberAdr.focus();
		return;
	}else{
		$("#adr_check").removeAttr("style");
		$("#adr_check").html('');
	}
			
	//전화번호검사
	if($("memberPhone").value==""){
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
	}else{
		$("#phone_check").removeAttr("style");
		$("#phone_check").html('');
	}
	
	
	
			
	//이메일 검사
	if($("memberEmail").value==""){
		$("#email_check").html("이메일을 입력해주십시오");
		$("#email_check").css("color","red");
		$("#email_check").css("font-size","16px");
		fm.memberEmail.focus();
		return;
	}else if(!mailJ.test($("#memberEmail").val())){
		alert("이메일형식에 맞게 입력해주세요") 
		$("#memberEmail").val(""); 
		$("#memberEmail").focus(); 
		return;
		
	}else{
		$("#email_check").removeAttr("style");
		$("#email_check").html('');
	}

			
	$("#form").attr("method","POST");
	$("#form").attr("action","<%=request.getContextPath()%>/member/memberJoinAction.do").submit();
		
	
		
	<%-- fm.action ="<%=request.getContextPath()%>/member/memberJoinAction.do"; 	
	fm.method ="post";
	fm.submit(); --%>
	
	alert("회원가입이 완료되었습니다. 로그인 후 이용바랍니다.");
	
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


<!-- main -->
<main>
<div class="welcome">
<span id="welcome">환영합니다!<br>회원가입에 필요한 정보를<br>입력해주시기 바랍니다.</span>
<br>
<br>
<a href="<%=request.getContextPath()%>/member/memberInfo_search.do"><span id="lookingfor">아이디, 비밀번호를 잊으셨나요? 찾으러가기</span></a>
</div>

<form name=frm id="form">
<label><span>아이디</span><input type="text" name="memberId" id="memberId" placeholder="아이디" maxlength="20" size="30"></label>
<div class="check_css" id="id_check"></div>
<label><span>비밀번호</span><input type="password" name="memberPwd" id="memberPwd" placeholder="비밀번호" maxlength="15" size="30"></label>
<div class="check_css" id="pwd_check"></div>
<label><span>비밀번호</span><input type="password" name="memberPwd2" id="memberPwd2" placeholder="비밀번호를 확인해주세요" maxlength="15" size="30"></label>
<div class="check_css" id="pwd2_check"></div>
<label><span>닉네임</span><input type="text" name="memberNickname" id="memberNickname" placeholder="사용할 닉네임을 정해주십시오" size="30"></label>
<div class="check_css" id="nickname_check"></div>
<label><span>이름</span><input type="text" name="memberName" id="memberName" placeholder="이름" size="30"></label>
<div class="check_css" id="name_check"></div>


<label><span>생년월일</span>     
<!-- BIRTH_YY -->
<input type="text" name="birth_yy" id="yy" class="int" maxlength="4" placeholder="년(4자)"  size="8">
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
<input type="text" name="birth_dd" id="dd" class="int" maxlength="2" placeholder="일" size="7">
</label>
<div class="check_css" id="birth_check"></div>      
       
<label><span>주소</span><input type="text" name="memberAdr" id="memberAdr" placeholder="도시단위부터 작성해주십시오" size="45"></label>
<div class="check_css" id="adr_check"></div>
<label><span>전화번호</span><input type="text" name="memberPhone"  id="memberPhone" placeholder="'-' 없이 작성해주세요" size="30"></label>
<div class="check_css" id="phone_check"></div>
<label><span>이메일</span><input type="email" name="memberEmail" id="memberEmail" placeholder="id@email.com" size = "30px"></label>
<div class="check_css" id="email_check"></div>

<div class=wrapper><button id="bt" type="button" name= "button" id= "button"  value="submit"  onclick= "check();">회원가입</button></div>

</form>





</main>


<!-- nav2-->
<nav id="nav2">

</nav>
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