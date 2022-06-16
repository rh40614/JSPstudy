<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="jspstudy.domain.BoardVo" %>
    <%
    BoardVo bv = (BoardVo)request.getAttribute("bv");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="boardContent.css">
<script>



</script>
</head>
<body>
<table>
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
<td class="listHead"><span>파일 다운로드</span></td>
<% if(bv.getFileName()!=null){ %>
<td><img src="<%=request.getContextPath()%>/images/<%=bv.getFileName()%>" alt="사진 불러오기 실패">
<!-- 사진다운로드 링크 연결하기 -->
<a href="<%=request.getContextPath()%>/board/fileDownload.do?fileName=<%=bv.getFileName()%>"><%=bv.getFileName()%></a>
</td>
<%  } %>
</tr>
<tr>
<td colspan="2" class= listHead> <!-- 답변하기 기능의 경우 원글에 대해서 새로운 글을 아래에 적는것이므로 여러개의 값을 가져와야함.연결문자인 & 을 사용하여 파라미터들을 연결해준다.  -->
<div>
<button type="button" name="bt" value="reset" id="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardModify.do?bidx=<%=bv.getBidx() %>'">수정</button>
<button type="button" name="bt" value="" id="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardReply.do?bidx=<%=bv.getBidx()%>&originbidx=<%=bv.getOriginbidx()%>&depth=<%=bv.getDepth()%>&level_=<%=bv.getLevel_()%>'">답변</button> 
<button type="button" name="bt" value="" id="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardDelete.do?bidx=<%=bv.getBidx()%>'">삭제</button>
<button type="button" name="bt" value="" id="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'">목록</button>
</div>		<!-- 가상주소 뒤의 ?는 여기서부터는 sql을 언급하는 것이고 그뒤로 연결할때는 &를 쓴다. -->
</td>
</tr>
</table>
</body>
</html>