<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<style>
		*{margin: 0; padding: 0;}
		body{
			width: 603px;
			color: white;
			font-size: 15px;
		}
		.box{
			width: 60%;
			margin: 0 auto;
			height: 200px;
			background-color: #595780;
			border: 1px solid white;
		}		
		.box-val{
			width: 100%;
		}
		.box>input[type=button]{
			width: 100px;
			height: 30px;
			cursor: pointer;
			text-align: center;
			display: block;
			margin: 15px auto;
			border: 1px solid gray;
			border-radius: 5px;
		}
		h3{
			text-align: center;
			margin: 20px 0;
		}
		input[type=password]{
			margin:auto;
			display: block;
			width: 250px;
			text-align: center;
			height: 30px;
			border-radius: 10px;
		}
	</style>
	<script>
		function memberDel(){
			let userPwd = $("#pwd").val(); 
			$.ajax({
				url: '<%=request.getContextPath()%>/ajax/del_check.jsp',
				data: { userPwd: userPwd},
				success: function(value){
					if(value == 1){
						alert("비밀번호가 일치하지 않습니다");
					}else{
						let del = confirm("정말로 삭제하시겠습니까?");
						if(del){
							window.parent.location.href='<%=request.getContextPath()%>/member/delAction.do';	
						}else return;
						
					}
				}					
			});
		}
	</script>
</head>
<body>
	<br><br><br><br><br><br>
	<div class="box">
		<div><h3>회원탈퇴</h3></div>
		<br>
		<div class="box-val">
			<input type="password" id="pwd" placeholder="비밀번호 입력">
		</div>
		<br><input type="button" onclick="memberDel()" value="탈퇴하기">
	</div>
</body>
</html>