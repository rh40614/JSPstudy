<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="jspstudy.domain.BoardVo" %>   
<%
   BoardVo bv = (BoardVo)request.getAttribute("bv");
%>
<%		/*로그인을 하지않고 글을 작성하려고 하는경우에 alert를 띄어주고 로그인페이지로 이동시켜준다.*/
 if(session.getAttribute("midx") == null){
 out.println("<script>alert('로그인 후 사용가능합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
 }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>답글</title>
<link rel="stylesheet" href="boardModify.css">

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
	
	
	fm.action ="<%=request.getContextPath()%>/board/boardModifyAction.do"; 	
	fm.method ="post";
	fm.submit();
	
	return;
}

</script>

</head>
<body>
<h1>수정하기</h1>
<form name="frm">
<table>
<tbody>
<tr>
<td><span>제목</span></td>
<td><input type="text" name="subject" placeholder="제목을 입력해주세요" value = "<%=bv.getSubject() %>"></td>
</tr>
<tr>
<td><span>작성자</span></td>
<td><input type="text" name="writer" value = "<%=bv.getWriter() %>"></td>
</tr>
<tr>
<td><span >내용</span></td>
<td colspan="3"><textarea cols="100" rows="30" name="content" ><%=bv.getContent() %></textarea></td>
</tr>
<tr>
<td><input type ="hidden" name="bidx" value="<%=bv.getBidx() %>"></td>	<!-- 클라에게는 보이지 않는 정보를 담기 위함 -->
</tr>
<tr>
<td colspan="3">
<div><button class= "bt" name="button" type="button" value="submit" onclick="check();">작성하기</button></div>
<div><button class= "bt"name="button" type="button" value="reset" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'">취소</button></div>

</td>
</tr>
</tbody>



</table>
</form>

</body>
</html>