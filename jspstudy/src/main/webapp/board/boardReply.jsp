<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="jspstudy.domain.BoardVo" %> 
<%BoardVo bv = (BoardVo)request.getAttribute("bv2"); %> 

	<%		/*로그인을 하지않고 글을 작성하려고 하는경우에 alert를 띄어주고 로그인페이지로 이동시켜준다.*/
    if(session.getAttribute("midx") == null){
    	out.println("<script>alert('로그인 후 사용가능합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");
    }
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 답글달기</title>
<link rel="stylesheet" href="boardReply.css">
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
	
	
	fm.action ="<%=request.getContextPath()%>/board/boardReplyAction.do"; 	
	fm.method ="post";
	fm.submit();
	
	return;
}



</script>





</head>
<body>
<h1>| 게시판 답글</h1>
<hr>
<form name="frm">

<input type=hidden name="bidx" value="<%=bv.getBidx()%>">
<input type=hidden name="originbidx" value="<%=bv.getOriginbidx()%>">
<input type=hidden name="depth" value="<%=bv.getDepth()%>">
<input type=hidden name="level_" value="<%=bv.getLevel_() %>">

<table>
<tbody>
<tr>
<td class="listHead"><span>제목</span></td>
<td><%=bv.getSubject() %></td>
</tr>
<tr>
<td class="listHead"><span>작성자</span></td>
<td><%=bv.getWriter() %></td>
</tr>
<tr>
<td class="listHead"><span>내용</span></td>
<td class="content"><%=bv.getContent() %></td>
</tr>
<tr>
<td><span>제목</span></td>
<td><input type="text" name="subject" placeholder="제목을 입력해주세요" id="subject"></td>
</tr>
<tr>
<td><span>작성자</span></td>
<td><input type="text" name="writer" class="input"></td>
</tr>
<tr>
<td><span >내용</span></td>

<td colspan="3"><textarea cols="100" rows="30" name="content" ></textarea></td>
</tr>
<tr>
<td colspan="3">
<div>
<button class= "bt" name="button" type="button" value="submit" onclick="check();">작성하기</button>
<button class= "bt"name="button" type="button" value="reset" onclick="location.href='<%=request.getContextPath()%>/board/boardContext.do?bidx=<%=bv.getBidx()%>>'">취소</button>
</div>

</td>
</tr>
</tbody>



</table>
</form>


</body>
</html>