<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>아이디/비밀번호 찾기</title>
<style>
	body{
	position: relative;
	width: 100%;
	height: 100%;
	margin: 0;
}		
.bodyBox{
	width: 300px;
	height: 220px;
	border: 1px solid black;
	margin: 0 auto;
	display: block;
	margin-top: 50px;
}
h3{text-align: center; color: deepskyblue;}
input{cursor:pointer;}
.bodyBox input{
	display: block;
	width: 80%;
	height: 35px;
	margin-top: 7px;
	margin: 0 auto;
}
</style>
</head>
<body>
<br><br>
<div class="bodyBox">
	<h3>찾으시려는 정보를 클릭하세요</h3>
	<div class="box">
		<input type="button" value="아이디" onclick="location.href='<%=request.getContextPath()%>/member/findIdAction.do'"><br>
		<input type="button" value="비밀번호" onclick="location.href='<%=request.getContextPath()%>/member/findPwdAction.do'"><br>	
	</div>
	<input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%>/'"><br>
</div>
														
	
</body>
</html>


