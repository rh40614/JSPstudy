<%@page import="mysqlProject.domain.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	MemberVo mv = (MemberVo) request.getAttribute("mv");
%>
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
	height: 200px;
	border: 1px solid black;
	margin: 0 auto;
	display: block;
	margin-top: 50px;
}
h3{text-align: center; color: deepskyblue;}
input{cursor:pointer;}
.box input{
	display: block;
	width: 80%;
	height: 35px;
	margin-top: 7px;
	margin: 0 auto;
}
.find-id>span{
	display: block;
	width: 100%;
	text-align: center;
	font-size: 18px;
}
.btn-box{
	width: 100%;
	text-align: center;
}
.btn-box>div,input{display: inline-block;}
input[type=button]{
	width: 100px;
	height: 30px;
	margin: 0 auto;
}

h5{
	font-size: 15px;
	margin: 10px 0 0 0;
}
div.find-pwd{
	width: 260px;
	margin: 0 auto;
}
div.find-pwd span{
	font-size: 13px;
	display: block;
}
div.find-pwd input{
	width: 100%;
	height: 30px;
	padding: 0;
	margin-top: 7px;
	text-indent: 10px;
	font-size: 14px;	
}

</style>
 <%
	if(session.getAttribute("find").equals("id")){
		out.print("<style>.find-id{display:inherit;}</style>");
		out.print("<style>.find-pwd{display:none;}</style>");
	}else{
		out.print("<style>.find-id{display:none;}</style>");
		out.print("<style>.find-pwd{display:inherit;}</style>");
		out.print("<style>.bodyBox{height:320px;}</style>");
	}
%>
<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
<script>
	function changePwd(){
		if($("#input-pwd").val() == ""){
			$("#input-pwd").focus();
			$("#input-pwd").blur();
			$("#input-pwd").focus();
			return;
		}else if($("#input-pwd").val() != $("#input-pwd2").val()){
			$("#input-pwd").focus();
			$("#input-pwd").blur();
			$("#input-pwd").focus();
			return;
		}
		$("#frm").attr("action",'<%=request.getContextPath()%>/member/changePwd.do').submit();
	}
</script>
</head>
<body>
<br><br>
<form name="frm" method="post" id="frm">
	<div class="bodyBox">
		<h3 class="find-id"><%=mv.getMembername()%>님의 아이디</h3>
		<h3 class="find-pwd">비밀번호 재설정</h3>
		<div class="box">
			<div class="find-id"><span><%=mv.getMemberid()%></span></div>
			<div class="find-pwd">
				<h5>비밀번호</h5>
				<input type="password" name="memberPwd" id="input-pwd">
				<span></span>
				<h5>비밀번호 확인</h5>
				<input type="password" name="memberPwd2" id="input-pwd2">
			</div>
<script>
	$("#input-pwd").blur(function(){
		if($("#input-pwd").val() == ""){
			$("#input-pwd").next("span").text("비밀번호를 입력하세요");
			$("#input-pwd").next("span").css("color","red");
		}else if($("#input-pwd").val() != $("#input-pwd2").val()){
			$("#input-pwd").next("span").text("비밀번호 확인필요");
			$("#input-pwd").next("span").css("color","red");			
		}else{
			$("#input-pwd").next("span").text("비밀번호 일치");
			$("#input-pwd").next("span").css("color","green");
		}
	});
	$("#input-pwd2").blur(function(){
		if($("#input-pwd").val() == ""){
			$("#input-pwd").next("span").text("비밀번호를 입력하세요");
			$("#input-pwd").next("span").css("color","red");
		}else if($("#input-pwd2").val() == ""){
			$("#input-pwd").next("span").text("비밀번호확인을 입력하세요");
			$("#input-pwd").next("span").css("color","red");
		}else if($("#input-pwd").val() != $("#input-pwd2").val()){
			$("#input-pwd").next("span").text("비밀번호 확인필요");
			$("#input-pwd").next("span").css("color","red");			
		}else{
			$("#input-pwd").next("span").text("비밀번호 일치");
			$("#input-pwd").next("span").css("color","green");
		}
	});
</script>
		</div>
		<br><br>
		<div class="btn-box">
			<div>
				<input type="button" value="비밀번호 찾기" class="find-id" onclick="location.href='<%=request.getContextPath()%>/member/findPwdAction.do'">
				<input type="button" value="비밀번호 변경" class="find-pwd" onclick="changePwd()">
			</div>
		<input type="button" value="로그인" onclick="location.href='<%=request.getContextPath()%>/member/login.do'">
		
		</div>
	</div>
	<input type="hidden" name="id" value="<%=mv.getMemberid()%>">
</form>
</body>
</html>


