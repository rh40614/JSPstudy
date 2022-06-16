<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%
  String bidx = (String)request.getAttribute("bidx");
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
<title>Insert title here</title>
<link rel="stylesheet" href="boardDelete.css">
<script>
//함수를 만들어서 넘기는 방법은 다른사람들에게 보이지않게 묶어서 넘기는 방식으로 post이다. 

//function check(){
// bt.action ="<%//=request.getContextPath()%>/board/boardDeleteAction.do"
		//	bt.method ="post";
		//	bt.submit();
//}
</script>
</head>
<body>
<h1>삭제하시겠습니까?</h1>

<!-- location.href 는 get방식으로 주소창을 통해서 이동하는 방식이다.  -->
<div>
<button type="button" name= "bt" class="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardDeleteAction.do?bidx=<%=bidx%>'">확인</button>
<button type="button" name= "bt" class="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardContent.do?bidx=<%=bidx%>'">취소</button>
</div>
</body>
</html>