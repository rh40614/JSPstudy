<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  	<%		
  	/*로그인을 하지않고 글을 작성하려고 하는경우에 alert를 띄어주고 로그인페이지로 이동시켜준다.*/
    if(session.getAttribute("midx") == null){
    	//게시물을 작성하려했으나 로그인을하고 돌아오는 경우
    	session.setAttribute("saveURI",request.getRequestURI());
    	
    	out.println("<script>alert('로그인 후 사용가능합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
    }
    %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="boardWrite.css">
<script>

function check(){

	var fm = document.frm;
	
	if(fm.subject.value == ""){
		alert("제목을 입력해주세요");
		fm.subject.focus();
		return;
	}else if(fm.writer.value == ""){
		alert("게시글 작성자를 입력하세요")
		fm.writer.focus();
		return;
	}else if(fm.content.value ==""){
		alert("내용을 작성해주십시오")
		fm.content.focus();
		return;
	}
	
	
	fm.action ="<%=request.getContextPath()%>/board/boardWriteAction.do"; 	
	fm.method ="post";
	fm.enctype ="multipart/form-data"; //파일 전송
	fm.submit();
	
	return;
}



</script>





</head>
<body>
<h1>| 게시판</h1>
<hr>
<form name="frm">
<table>
<tbody>
<tr>
<td><span>제목</span></td>
<td><input type="text" name="subject" placeholder="제목을 입력해주세요" id="subject"></td>
</tr>
<tr>
<td><span>작성자</span></td> <!-- 작성자 값을 로그인한 사람의 이름을 session값꺼내서 등록하고 readonly를 이용하여 바꾸지 못하도록 설정 -->
<td><input type="text" name="writer" class="input" value ="<%=session.getAttribute("memberName") %>" readonly="readonly"></td>
</tr>
<tr>
<td><span >내용</span></td>

<td colspan="3"><textarea cols="100" rows="30" name="content" ></textarea></td>
</tr>

<tr>
<td><span>파일</span></td> 
<td><input type="file" name="filename" class="input"  ></td>
</tr>

<tr>
<td colspan="3">
<div>
<button class= "bt" name="button" type="button" value="submit" onclick="check();">작성하기</button>
<button class= "bt"name="button" type="button" value="reset" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'">취소</button>
</div>

</td>
</tr>
</tbody>



</table>
</form>


</body>
</html>