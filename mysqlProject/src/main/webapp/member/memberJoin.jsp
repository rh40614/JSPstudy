<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>타이틀</title>
	<link rel="stylesheet" href="../css/join.css">	
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<script type="text/javascript">
		function endJoin(){
			let fm = document.frm;
			if($("#input-id").val()=="" || $("#input-id").next("span").css("color")=="rgb(255, 0, 0)"){
				$("#input-id").focus();
				$("#input-id").blur();
				$("#input-id").focus();
				return;
			}else if($("#input-pwd").val()==""){
				$("#input-pwd").focus();
				$("#input-pwd").blur();
				$("#input-pwd").focus();
				return;
			}else if($("#input-pwd2").val()=="" || $("#input-pwd").val()!=$("#input-pwd2").val()){
				$("#input-pwd2").focus();
				$("#input-pwd2").blur();
				$("#input-pwd2").focus();
				return;
			}else if($("#input-nic").val()=="" || $("#input-nic").next("span").css("color")=="rgb(255, 0, 0)"){
				$("#input-nic").focus();
				$("#input-nic").blur();
				$("#input-nic").focus();
				return;
			}else if($("#input-name").val()==""){
				$("#input-name").focus();
				$("#input-name").blur();
				$("#input-name").focus();
				return;
			}else if($("#input-email").val()==""){
				$("#input-email").focus();
				$("#input-email").blur();
				$("#input-email").focus();
				return;
			}else if($("#input-phone").val()==""){
				$("#input-phone").focus();
				$("#input-phone").blur();
				$("#input-phone").focus();
				return;
			}else if($("#input-gender").val()==""){
				$("#input-gender").focus();
				$("#input-gender").blur();
				$("#input-gender").focus();
				return;
			}else{
				fm.action = "<%=request.getContextPath()%>/member/joinAction.do";
				fm.method = "post";
				fm.submit();				
			}
		}
	</script>
	<style></style>
</head>

<body>
<div id="bodyBox">
	<h3>회원가입 페이지</h3>
	<br>
	<form name="frm">
	<div class="box">
		<h5>아이디</h5>
		<input type="text" name="memberId" id="input-id">
		<span></span>
		<script>
			$("#input-id").blur(function(){
				let userId = $("#input-id").val();
				$.ajax({
					url: '<%=request.getContextPath()%>/ajax/id_list.jsp',
					data: { userId: userId},
					success: function(value){
						if(value == 0){
							$("#input-id").next("span").text("중복된 아이디입니다");
							$("#input-id").next("span").css("color","red");
						}else if($("#input-id").val() == ""){
							$("#input-id").next("span").text("아이디를 입력하세요");
							$("#input-id").next("span").css("color","red");
						}else if($("#input-id").val() != ""){
							$("#input-id").next("span").text("사용가능 아이디입니다");
							$("#input-id").next("span").css("color","green");			
						}
					}					
				});
			});
		</script>
		<h5>비밀번호</h5>
		<input type="password" name="memberPwd" id="input-pwd">
		<span></span>
		<h5>비밀번호 확인</h5>
		<input type="password" name="memberPwd2" id="input-pwd2">
		<span></span>
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
				if($("#input-pwd2").val() == ""){
					$("#input-pwd2").next("span").text("비밀번호 확인을 입력하세요");
					$("#input-pwd2").next("span").css("color","red");
				}else if($("#input-pwd").val() != $("#input-pwd2").val()){
					$("#input-pwd").next("span").text("비밀번호 확인필요");
					$("#input-pwd").next("span").css("color","red");
					$("#input-pwd2").next("span").text("");
				}else if($("#input-pwd").val() == $("#input-pwd2").val()){
					$("#input-pwd").next("span").text("비밀번호 일치");
					$("#input-pwd").next("span").css("color","green");
					$("#input-pwd2").next("span").text("");
				}	
			});
		</script>	
	</div>
	<br>	 
	<div class="box">
		<h5>닉네임</h5>
		<input type="text" name="memberNic" id="input-nic">
		<span></span>
		<script>
			$("#input-nic").blur(function(){
				let userNic = $("#input-nic").val();
				$.ajax({
					url: '<%=request.getContextPath()%>/ajax/nic_list.jsp',
					data: { userNic: userNic},
					success: function(value){
						if(value == 0){
							$("#input-nic").next("span").text("중복된 닉네임입니다");
							$("#input-nic").next("span").css("color","red");
						}else if($("#input-nic").val() == ""){
							$("#input-nic").next("span").text("닉네임을 입력하세요");
							$("#input-nic").next("span").css("color","red");
						}else if($("#input-nic").val() != ""){
							$("#input-nic").next("span").text("사용가능한 닉네임입니다");
							$("#input-nic").next("span").css("color","green");			
						}
					}					
				});
			});
		</script>
		<h5>이름</h5>
		<input type="text" name="memberName" id="input-name">
		<span></span>
		<script>
			$("#input-name").blur(function(){
				if($("#input-name").val() == ""){
					$("#input-name").next("span").text("이름을 입력하세요");
					$("#input-name").next("span").css("color","red");
				}else{
					$("#input-name").next("span").text("이름 입력완료");
					$("#input-name").next("span").css("color","green");
				}
			});
		</script>
		<h5>이메일</h5>
		<input type="email" name="memberEmail" id="input-email">
		<span></span>
		<script>
			$("#input-email").blur(function(){
				if($("#input-email").val() == ""){
					$("#input-email").next("span").text("이메일을 입력하세요");
					$("#input-email").next("span").css("color","red");
				}else{
					$("#input-email").next("span").text("이메일 입력완료");
					$("#input-email").next("span").css("color","green");
				}
			});
		</script>
		<h5>연락처</h5>
		<select name="memberPhoneAgency" id="agency">
			<option value="SKT">SKT</option>
			<option value="KT">KT</option>
			<option value="LGU+">LGU+</option>
			<option value="Altt">알뜰폰</option>
		</select>
		<input type="tel" name="memberPhone" placeholder="숫자만 입력하세요" id="input-phone" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');">
		<span></span>
		<script>
			$("#input-phone").blur(function(){
				if($("#input-phone").val() == ""){
					$("#input-phone").next("span").text("번호를 입력하세요");
					$("#input-phone").next("span").css("color","red");
				}else{
					$("#input-phone").next("span").text("번호 입력완료");
					$("#input-phone").next("span").css("color","green");
				}
			});
		</script>
		<h5>성별</h5>
		<select name="memberGender" id="input-gender">
			<option value="" >성별</option>
			<option value="M">남성</option>
			<option value="W">여성</option>
		</select>
		<span></span>
		<script>
			$("#input-gender").click(function(){
				if($("#input-gender").val() == ""){
					$("#input-gender").next("span").text("성별을 선택하세요");
					$("#input-gender").next("span").css("color","red");
				}else{
					$("#input-gender").next("span").text("선택완료");
					$("#input-gender").next("span").css("color","green");
				}
			});
		</script>					
	</div>
	<br>
	<div class="box" id="footer">
		<input type="button" value="완료" onclick="endJoin();"><br>
		<input type="button" value="취소" onclick="location.href='<%=request.getContextPath()%>/'">
	</div>	
	</form>
</div>
														
	
</body>
</html>


