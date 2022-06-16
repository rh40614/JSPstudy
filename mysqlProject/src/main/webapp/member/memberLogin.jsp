<%@page import="mysqlProject.domain.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>로그인</title>
	<link rel="stylesheet" href="../css/login.css">
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<script>
		function loginCheck(){
			if($("#input-id").val()==""){
				$("#input-id").focus();
				$("#input-id").blur();
				$("#input-id").focus();
				return;
			}else if($("#input-pwd").val()==""){
				$("#input-pwd").focus();
				$("#input-pwd").blur();
				$("#input-pwd").focus();
				return;
			}			
			let userId = $("#input-id").val();
			let userPwd = $("#input-pwd").val();
			$.ajax({
				url: '<%=request.getContextPath()%>/ajax/login_check.jsp',
				data: { userId: userId,userPwd: userPwd},
				success: function(value){
					if(value == 1){
						alert("아이디 또는 비밀번호 오류입니다");
					}else{
						$("#loginForm").attr("action",'<%=request.getContextPath()%>/member/loginAction.do').submit();
					}
				}					
			});
		}
		
	</script>
</head>

<body>
<br><br>
<div id="bodyBox">
	<h3>로그인</h3>
	<form name="frm" id="loginForm" method="post">
	<div class="box">
		<h5>아이디</h5>
		<input type="text" name="memberId" id="input-id">
		<span></span>
		<h5>비밀번호</h5>
		<input type="password" name="memberPwd" id="input-pwd">	
		<span></span>	
		<br>
	</div>
	<div class="box" id="subBox">
	<span><a href="<%=request.getContextPath()%>/member/find.do">아이디/비밀번호 찾기</a></span>
	<span><a href="<%=request.getContextPath()%>/member/join.do">회원가입</a></span>
	</div>	
	<br>
	<div class="box" id="footer">
		<input type="button" value="로그인" onclick="loginCheck()"><br>
		<input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%>/'"><br>
	</div>	
	</form>
</div>
														
	
</body>
</html>


