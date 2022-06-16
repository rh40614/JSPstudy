<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>

else if(!idJ.test($("#memberId").val())){
		alert("아이디는 영어소문자, 숫자를 포함하여 4글자 이상 12글자 이내로 작성해주십시오.") 
		$("#memberId").val(""); 
		$("#memberId").focus(); 
		return; 
		
		//비밀번호 검사	
	}else if(fm.memberPwd.value==""){ 
		$("#pwd_check").html("비밀번호를 입력해 주십시오");
		$("#pwd_check").css("color","red");
		$("#pwd_check").css("font-size","16px");.
		fm.memberPwd.focus();
		return;
	}else if(!pwJ.test($("#memberPwd").val())){
		alert("비밀번호는 영어 대소문자, 숫자를 포함하여 4글자 이상 12글자 이내로 작성해주십시오.") 					
		$("#memberPwd").val(""); 
		$("#memberPwd").focus(); 
		return;
	}
		
		