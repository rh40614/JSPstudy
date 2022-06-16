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
	height: 300px;
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
.btn-box{
	width: 70%;
	margin: 0 auto;
}
.btn-box>div,input{display: inline-block;}
input[type=button]{
	width: 100px;
	height: 30px;
	margin: 0 auto;
}
</style>
<%
	if(session.getAttribute("find").equals("id")){
		out.print("<style>.find-id{display:inherit;}</style>");
		out.print("<style>.find-pwd{display:none;}</style>");
	}else{
		out.print("<style>.find-id{display:none;}</style>");
		out.print("<style>.find-pwd{display:inherit;}</style>");
	}
%>
<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
<script>
	function findId(){
		let userName = $("#find-name").val();
		let userEmail = $("#find-email").val();
		$.ajax({
			url: '<%=request.getContextPath()%>/ajax/find_id.jsp',
			data: { userName: userName,userEmail: userEmail},
			success: function(value){
				if($("#find-name").val() == "" || $("#find-email").val() == ""){
					alert("입력사항을 모두 입력하세요");
				}else if(value == 1){
					alert("일치하는 정보가 없습니다.");
				}else{
					$("#frm-id").attr("action",'<%=request.getContextPath()%>/member/findResult.do').submit();
				}
			}					
		});
	}
	
	function findPwd(){
		let userId = $("#find-id").val();
		let userName = $("#find-name").val();
		let userEmail = $("#find-email").val();
		$.ajax({
			url: '<%=request.getContextPath()%>/ajax/find_pwd.jsp',
			data: { userId: userId, userName: userName, userEmail: userEmail},
			success: function(value){
				if($("#find-name").val() == "" || $("#find-email").val() == "" || $("#find-id").val() == ""){
					alert("입력사항을 모두 입력하세요");
				}else if(value == 1){
					alert("일치하는 정보가 없습니다.");
				}else{
					$("#frm-id").attr("action",'<%=request.getContextPath()%>/member/findResult.do').submit();
				}
			}					
		});
	}
</script>
</head>
<body>
<br><br>
<form name="frm" method="post" id="frm-id">
	<div class="bodyBox">
		<h3 class="find-id">아이디 찾기</h3>
		<h3 class="find-pwd">비밀번호 찾기</h3>
		<div class="box">
			<div class="find-pwd"><input type="text" placeholder="아이디" name="id" id="find-id"><br></div>
			<input type="text" placeholder="이름" name="name" id="find-name"><br>
			<input type="email" placeholder="이메일" name="email" id="find-email"><br>	
		</div>
		<div class="btn-box">
			<div>
				<input type="button" value="아이디 찾기" class="find-id" onclick="findId()">
				<input type="button" value="비밀번호 찾기" class="find-pwd" onclick="findPwd()">
			</div>
		<input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%>/member/find.do'">
		</div>
	</div>
</form>
</body>
</html>


