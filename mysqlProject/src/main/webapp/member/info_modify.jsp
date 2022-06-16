<%@page import="mysqlProject.domain.MemberVo"%>
<%@page import="mysqlProject.service.MemberDao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%response.setHeader("P3P", "CP='CAO PSA CONi OTR OUR DEM ONL'");%>
<!DOCTYPE html>
<%
MemberDao md = new MemberDao();
int midx_ = (int)session.getAttribute("midx");
MemberVo mv = md.infoIdx(midx_);
%>
<html>
<head>
	<head>
	<meta charset="UTF-8">
	<title>타이틀</title>
	<link rel="stylesheet" href="../css/join.css">	
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<script type="text/javascript">
		function endJoin(){
			$("#input-nic").focus();
			$("#input-nic").blur();
			let fm = document.frm;
			if($("#input-pwd").val()==""){
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
				fm.action = "<%=request.getContextPath()%>/member/idxModify.do";
				fm.method = "post";
				fm.submit();				
			}
		}
	</script>
	<style>
		h5{
			color: #f7f2f2;
		}
	</style>
</head>

<body>
<div id="bodyBox">
	<h3><%=session.getAttribute("memberNicname")%>님의 정보수정</h3>
	<br>
	<form name="frm">
	<div class="box">
		<h5>비밀번호</h5>
		<input type="password" name="memberPwd" id="input-pwd" value="<%=mv.getMemberpwd()%>">
		<span></span>
		<h5>비밀번호 확인</h5>
		<input type="password" name="memberPwd2" id="input-pwd2" value="<%=mv.getMemberpwd()%>">
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
		<input type="text" name="memberNic" id="input-nic" value="<%=mv.getMembernicname()%>">
		<span></span>
		<script>
			$("#input-nic").blur(function(){
				let userNic = $("#input-nic").val();
				$.ajax({
					url: '<%=request.getContextPath()%>/ajax/nic_list.jsp',
					data: { userNic: userNic},
					success: function(value){
						if('<%=mv.getMembernicname()%>'==$("#input-nic").val()){
							$("#input-nic").next("span").text(" ");
						}else if(value == 0){
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
		<input type="text" name="memberName" id="input-name" value="<%=mv.getMembername()%>">
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
		<input type="email" name="memberEmail" id="input-email" value="<%=mv.getMemberemail()%>">
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
		<script>
			let agency = '<%=mv.getMemberphoneagency()%>';
			if( agency == 'SKT'){
				$("#agency").val("SKT").attr("selected","selected");
			}else if(agency == 'KT'){
				$("#agency").val("KT").attr("selected","selected");
			}else if(agency == 'Altt'){
				$("#agency").val("Altt").attr("selected","selected");
			}else{
				$("#agency").val("LGU+").attr("selected","selected");
			}
		</script>
		<input type="tel" name="memberPhone" placeholder="숫자만 입력하세요" id="input-phone" onkeyup="this.value=this.value.replace(/[^0-9]/g,'');" value="<%=mv.getMemberphone()%>">
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
			<option value="">성별</option>
			<option value="M">남성</option>
			<option value="W">여성</option>
		</select>
		<script>
			let gender = '<%=mv.getMembergender()%>';
			if(gender == 'M'){
				$("#input-gender").val("M").attr("selected","selected");
			}else{
				$("#input-gender").val("W").attr("selected","selected");
			}
		</script>
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
		<input type="button" value="취소" onclick="window.parent.location.href='<%=request.getContextPath()%>/member/info.do'">
	</div>	
	</form>
</div>
</body>
</html>