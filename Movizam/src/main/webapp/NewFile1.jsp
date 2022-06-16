<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>











	//닉네임 검사
		
	 if(fm.memberNickname.value==""){
		$("#nickname_check").html("닉네임을 입력해주십시오");
		$("#nickname_check").css("color","red");
		$("#nickname_check").css("font-size","16px");
		fm.memberName.focus();
		return;
	}else if(!nickJ.test($("#memberNickname").val())){
		alert("닉네임은 한글, 영문, 숫자만 가능하며 2-10자리 가능합니다.")
		$("#memberName").val(""); 
		$("#memberName").focus(); 
		return;
	}else{
		$("#nickname_check").removeAttr("style");
		$("#nickname_check").html('');
	}		
		
	
		
		
		
	//이름검사
	if(fm.memberName.value==""){
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
	if(fm.birth_yy.value==""){
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
	}else if(fm.birth_dd.value==""){
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
	if(fm.memberAdr.value==""){
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
	if(fm.memberPhone.value==""){
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
	if(fm.memberEmail.value==""){
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

			
		
















</body>
</html>