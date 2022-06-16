<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>로그인 페이지</title>
<link rel="stylesheet" href="memberLogin.css">
<script>

function check(){
	var fm =document.frm; //form 태그 name으로접근해서 변수에 담기
	if(fm.memberId.value == ""){ //value 값은 입력받는값. 입력받는 값이 정해져있는 것이 아님으로 html태그에서 값을 정해주지 않는다.
		alert("아이디를 입력하세요");
		fm.memberId.focus();// 알림이 뜨고 난 후 알림이 뜬 이유의 위치로 포인터가 뜨도록 설정
		return;
	}else if(fm.memberPwd.value==""){ //value 값이 있는지 없는지 체크
		alert("비밀번호를 입력하세요");
		fm.memberPwd.focus();
		return;
	}
	
	
	fm.action ="<%=request.getContextPath()%>/member/memberLoginAction.do"; 	//서블릿이 가짜 경로를 받아서 서블릿에서 처리하도록하기
	fm.method ="post";
	fm.submit();
	
	return;
	}

</script>

</head>

<body>

<h1>로그인</h1>
<form name= frm>
<!-- name에 객체를 넘겨 받아서 데이터 처리함으로 네임값 꼭 설정하기. 이왕이면 디비에있는 이름이랑 동일하게-->

<label ><span>아이디</span>
<input type="text" name="memberId" size="60"  value="" placeholder= " 아이디를 입력해주세요."></label>
<label ><span>비밀번호</span>
<input type="password" name="memberPwd" size="60" placeholder= " 비밀번호를 입력해주세요."></label>

<div class=wrapper><button id="bt" type="button" name= "button" value="submit"  onclick= "check();">로그인</button></div>

</form>
</body>
</html>