<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원가입 페이지</title>
<link rel="stylesheet" href="memberJoin.css">
<script>

function check(){
	var fm =document.frm; //form 태그 name으로접근해서 변수에 담기
	if(fm.memberId.value == ""){ //value 값은 입력받는값. 입력받는 값이 정해져있는 것이 아님으로 html태그에서 값을 정해주지 않는다.
		alert("아이디를 입력하세요");
		fm.memberId.focus();// 알림이 뜨고 난 후 알림이 뜬 이유의 위치로 포인터가 뜨도록 설정
		return;
	}else if(fm.memberpwd.value==""){ //value 값이 있는지 없는지 체크
		alert("비밀번호를 입력하세요");
		fm.memberpwd.focus();
		return;
	}else if(fm.memberpwdAgain.value==""){
		alert("비밀번호가 다릅니다.");
		fm.memberpwdAgain.focus();
		return;
	}else if(fm.memberName.value==""){
		alert("이름을 입력하세요");
		fm.memberName.focus();
		return;
	}else if(fm.memberPhone.value==""){
		alert("연락처를 입력하세요");
		fm.memberPhone.focus();
		return;
	}else if(fm.memberJumin.value==""){
		alert("주민등록번호를 입력하세요");
		fm.memberJumin.focus();
		return;
	}else if(fm.memberAdress.value==""){
		alert("주소를 입력하세요");
		fm.memberAdress.focus();
		return;
	}else if(fm.memberemail.value==""){
		alert("이메일을 입력하세요");
		fm.memberemail.focus();
		return;
	}else{	// 멤버가 취미를 하나도 선택하지 않았을경우
		var chkYN = false;
		for(var i = 0; i<fm.memberHobby.length;i++){
			if(fm.memberHobby[i].checked == true){ // 하나라도 체크가 되어있다면
				chkYN = true;
				break;
			}
		}
		if (chkYN == false){
			alert("취미를 한개이상 선택해주세요");
			return;
			}

	}
	
	//값을 처리하기 위해 넘기기 
	//가상경로 사용하기. 실제경로를 액션으로 지정하면 보안에 문제가 생길 수있다. 물리적인 경로를 알수없도록하기 
	//memberjoin 이라는 파일의 경로를 memberjoinAction.do 라는 가상경로를 만들어서 이 경로를 통해서 이동할 수 있도록하기 
	//fm.action ="./memberJoinOK.jsp";  -진짜경로
	
	fm.action ="<%=request.getContextPath()%>/member/memberJoinAction.do"; 	//서블릿이 가짜 경로를 받아서 서블릿에서 처리하도록하기
	fm.method ="post";
	fm.submit();
	
	return;
	}

</script>

</head>

<body>

<h1>회원가입</h1>
<form name= frm>
<!-- name에 객체를 넘겨 받아서 데이터 처리함으로 네임값 꼭 설정하기. 이왕이면 디비에있는 이름이랑 동일하게-->

<label ><span>아이디</span>
<input type="text" name="memberId" size="60"  value="" placeholder= " 아이디를 입력해주세요."></label>
<label ><span>비밀번호</span>
<input type="password" name="memberpwd" size="60" placeholder= " 비밀번호를 입력해주세요."></label>
<label ><span>비밀번호 확인</span>
<input type="password" name="memberpwdAgain" size="60" placeholder= " 비밀번호를 한번더 확인해주시길 바랍니다."></label>
<label><span>이름</span>
<input type="text" name="memberName" size="60" ></label>
<label><span>연락처</span>
<input type ="text" name ="memberPhone" size ="60" ></label>
<label><span>주민등록번호</span>
<input type = "text" name="memberJumin" size = "60" ></label>
<label><span>주소</span>
 <input type = "text" name = "memberAdress"size ="60"> </label>
<label><span>성별</span><!-- 라디오 버튼의 name 값이 같으면 중복 선택됨으로 같은 값으로 처리 -->
<input type="radio" name="memberGender" value="m" checked>남자
<input type="radio" name="memberGender" value="f">여자
</label>
<label><span>취미</span><!-- 중복선택이 가능한것은 name을 동일하게하고 jsp에서 배열로 받아야한다.  -->
<input type="checkbox" name="memberHobby" value = "soccer" checked>축구
<input type="checkbox" name="memberHobby" value = "basketball">농구
<input type="checkbox" name="memberHobby" value = "baseball">야구
</label><br>
<label><span>이메일</span>
<input type="email" name="memberemail" placeholder="id@email.com" size = "60px"></label><br>
<div class=wrapper><button id="bt" type="button" name= "button" value="submit"  onclick= "check();">회원가입</button></div>

</form>
</body>
</html>