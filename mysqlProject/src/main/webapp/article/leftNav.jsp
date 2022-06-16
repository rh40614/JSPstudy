<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/leftNav.css">
<%
session.setAttribute("saveUrl", null);
if(session.getAttribute("midx") != null){
	out.println("<style>#login-box{display:inherit;}</style>");
	out.println("<style>#logout-box{display:none;}</style>");
}else{
	out.println("<style>#login-box{display:none;}</style>");
	out.println("<style>#logout-box{display:inherit;}</style>");
}
%>
<div id="logout-box">
	<br>
	<input type="button" id="login"value="로그인" onclick="location.href='<%=request.getContextPath()%>/member/login.do'">
	<div id="logout-bottom">
		<span id="findId"><a href="<%=request.getContextPath()%>/member/find.do">아이디/비밀번호 찾기</a></span>
		<span id="join"><a href="<%=request.getContextPath()%>/member/join.do">회원가입</a></span>
	</div>
</div>
<div id="login-box">
	<input type="image" id="login-profile" alt="로딩중.." src="<%if(session.getAttribute("midx") == null){ }else{%>/profile/<%=session.getAttribute("midx")%>/<%=session.getAttribute("memberProfile")%><%}%>" onclick="location.href='<%=request.getContextPath()%>/member/info.do'">
	<p class="profile-right" id ="profileId"><%out.println(session.getAttribute("memberId"));%></p>
	<p class="profile-right" id ="profileNic"><%out.println(session.getAttribute("memberNicname"));%> 님</p>
	<div id="logout-bottom">
		<span id="join"><a href="<%=request.getContextPath()%>/member/logout.do">로그아웃</a></span>
	</div>
</div>


<div id="leftSubBox">
	<input type="button" value="글쓰기" id="write" onclick="location.href='<%=request.getContextPath()%>/board/write.do'">
	<input type="text" id = "searchText" placeholder="통합검색">
	<input type="button" id = "searchButton" value="🔍">	
</div>
<script>
	$("#searchButton").click(function(){
		let text = $("#searchText").val();
		window.location = '<%=request.getContextPath()%>/board/search.do?keyword='+text;
	});
</script>


<div id="list">
	<h3 style="margin-top: 0px; border-top: none;">커뮤니티</h3>
	<ul>				
		<li class="secList">
			<span><a href="<%=request.getContextPath()%>/board/list.do?category=1">공지사항</a></span>
			<span><a href="<%=request.getContextPath()%>/board/list.do?category=2">자유 게시판</a></span>
			<span><a href="<%=request.getContextPath()%>/board/list.do?category=3">질문 게시판</a></span>
		</li>
	</ul>
	<h3 class="mainList">정보 게시판</h3>
	<ul>	
		<li class="secList">
			<span><a href="<%=request.getContextPath()%>/board/list.do?category=4">게임소식</a></span>
			<span><a href="<%=request.getContextPath()%>/board/list.do?category=5">일상소식</a></span>
		</li>
	</ul>
	<h3 class="mainList">미디어 게시판</h3>
	<ul>	
		<li class="secList">
			<span><a href="<%=request.getContextPath()%>/board/gallery.do?category=6">갤러리</a></span>
		</li>
	</ul>
</div>