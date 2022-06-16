<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import ="java.util.ArrayList" %>
<%@page import ="jspstudy.domain.*"%>
    
  <%
 ArrayList<BoardVo> alist =(ArrayList<BoardVo>)request.getAttribute("alist");
   %>
  <%
  PageMaker pm = (PageMaker)request.getAttribute("pm");
  %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 목록</title>
<link rel="stylesheet" href="boardList.css">

</head>
<body>
<h1>| 게시물 목록</h1>
<hr>
<form name="frm" action="<%=request.getContextPath() %>/board/boardList.do"  method="post"> <!-- 이 폼태그가 경로로 이동할때 keyword와 서치타입을 같이 가지고 간다.  -->

<table id="search">
<tr>
<td id="search_td">
<select name="searchType">
<option value="subject">제목</option>
<option value="writer">작성자</option>
</select> 
</td>
<td>
<input type="text" name="keyword" size="30">
</td>
<td>
<input type="submit" name="submit" value="검색">
</td>
</tr>
</table>
</form>

<div>
<!-- index는 서버에서 인식하는 jsp파일이기 때문에 기본적으로 web-xml파일에 정의되어 적어주지않아도 된다. 적으면 오히려 보안에 위협이된다. 위치를 지정해야하기때문에 / 하나는 쳐야한다.-->
<button type= "button" name="bt" class="bt" onclick="location.href='<%=request.getContextPath()%>/'">홈</button> 		
<button type= "button" name="bt" class="bt" onclick="location.href='<%=request.getContextPath()%>/board/boardWrite.do'">게시글 작성</button> 	
</div>

<table id="board_table">
<thead>
<tr id="listHead">
<th class="th">번 호</th>
<th class="th">제 목</th>
<th class="th">작 성 자</th>
<th>작 성 일</th>
</tr>
</thead>
<tbody>
<!-- 향상된 반복문으로 표내부의 줄을 데이터의 값을 가져와서 출력. %= 하면 out.println의 역할을 한다. -->
<% for(BoardVo bv : alist){ %>
<tr id="trList">
<td class="th"><%=bv.getBidx() %></td>
<td class="th_content">
<% 
for(int i=1;i<=bv.getLevel_();i++){ //level 이 1이면  답변글이면 띄우고 ㄴ 표시를 앞에 나타내기
	out.println("&nbsp;&nbsp;"); //&nbsp; 스페이스바 처럼 이동하는것
	if(i == bv.getLevel_()){
		out.println("ㄴ");
	}
}
%>
<!-- 제목을 누르면 content로 연결할 수 있도록 a 태그로 감싸기. ?앞은 경로 뒤는 파라미터-->
<a href="<%=request.getContextPath()%>/board/boardContent.do?bidx=<%=bv.getBidx()%>">
<%=bv.getSubject() %></a></td>

<td class="th"><%=bv.getWriter() %></td>
<td class="th"><%=bv.getWriteday() %></td>
</tr>
<% } %>

</tbody>
</table>

<table id="paging">
<tr>
<td>
<% 
if (pm.isPrev() == true){ //이전페이지가 있으면 이전페이지로. page는 sql코드로 1부터 넘어오지만 페이지는 0이 1페이지 이다. 
	
	out.print("<a href='"+request.getContextPath()+"/board/boardList.do?page="+(pm.getStartPage()-1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType() +"'>◀</a>");
}
%>

</td>
<td>
<%
//out.println(pm.getStartPage());
//out.println(pm.getEndPage());
for(int i=pm.getStartPage(); i<= pm.getEndPage();i++){
	
	out.print("<a href='"+request.getContextPath()+"/board/boardList.do?page="+i+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>"+i+"</a>");
}

%>
</td>
<td>
<% //다음장이 있고 마지막 페이지가 0보다크면 다음장을 가지고온다. 
if(pm.isNext() && pm.getEndPage() >0 ){ 
 
	out.print("<a href='"+request.getContextPath()+"/board/boardList.do?page="+(pm.getEndPage()+1)+"&keyword="+pm.encoding(pm.getScri().getKeyword())+"&searchType="+pm.getScri().getSearchType()+"'>▶</a>");
}%>
</td>
</tr>
</table>














</body>
</html>