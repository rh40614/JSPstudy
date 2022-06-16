<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import= "domain.*" %> 
<%@ page import= "java.util.*" %>

<% 
	BoardVo bv = (BoardVo)request.getAttribute("bv"); 
	HashMap<String, Integer> map =(HashMap<String, Integer>)request.getAttribute("map");
	ArrayList<ReplyVo> alist = (ArrayList<ReplyVo>)request.getAttribute("alist");
	PageMaker pm = (PageMaker)request.getAttribute("pm");
%>

   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Movizam | <%=bv.getBoardTitle() %></title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/board/boardContent.css">
<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>


<script>


		
		
//메뉴 탭(공통)
	   $(function(){
		$("#menu").click(function(){
			 $(".menutab_content").slideToggle("fast"); 
			
		})

	});
	
//로그아웃	
function logout(){
	alert("로그아웃되었습니다.")
}


//게시물 삭제
	function deleteFn(idx) {
		var check =confirm("삭제하시겠습니까?");
    	if (check == true) {
    		location.href='<%=request.getContextPath()%>/board/boardDeleteAction.do?bidx='+idx;
    	     alert("삭제되었습니다.");
  	  }else{
 	   	alert("취소되었습니다.");
 	  }
	}


//댓글 작성
	function checkFn(idx){
		var fm = document.frm;
	
		if($("#replyContent") ==""){
			alert("댓글 내용을 작성해 주십시오");
		}
	
	
		
		$("#frm").attr("method","POST");
		$("#frm").attr("action","<%=request.getContextPath()%>/reply/replyWriteAction.do?bidx="+idx+"").submit();
	}


//댓글 삭제
	function C_deleteFn(idx1, idx2){
		
		var check =confirm("삭제하시겠습니까?");
    	
		if (check == true) {
    		location.href='<%=request.getContextPath()%>/reply/replyDelete.do?ridx='+idx1+'&bidx='+idx2; 
    	    alert("삭제되었습니다.");
  	 	 }else{
 	 	  	alert("취소되었습니다.");
 		 }
		
	}


	

//댓글수정 누르면 table 숨기고 폼꺼내기
	function C_editFn(idx1, idx2, obj){
	
		//this를 매개변수로 넘겨서 obj로 받고, 버튼을 기준으로 해당되는 요소를 찾아서 비우기 
		//$(obj).parent().parent().next().empty();
		// 비운곳에 변수로 설정한것을 넘겨서 대체하기(tr안에 넣는 것)
		var html ="";
			html += "<form name = 'frm2' id='frm2'>";
			html += "<td><textarea name='replyContent_E' id='replyContent_E'>"+$("#content2"+idx1).val()+"</textarea></td>";
			html += "<td colspan='3'><button type='button' class='btn3' id='editbtn' >수정</button></td>";
			html += "<td><button type='button' class='btn3' id='canclebtn' >닫기</button></td>";
			html += "</form>";
			
		//$(obj).parent().parent().next().html(html); 
		//div 만들어서 아이디 자체를 변수랑 연결해서 댓글마다 수정창 나타날 수있게 부여
		$("#editbox"+idx1).html(html);
		$("#editbox"+idx1).next().css('display','none');
		//alert($("#content2"+idx1).val());
		//$("#content2"+idx1)
		
		//취소 버튼 클릭
		$("#canclebtn").click(function(){
			$("#editbox"+idx1).empty();	//display none으로 하면 수정버튼을 다시누르면 html이 쌓여서 아예 지우기
			$("#editbox"+idx1).next().css('display','block');
		})
		
		//수정 버튼클릭
		$("#editbtn").click(function(){
		//	alert("클릭");
			
			if($("#replyContent_E") ==""){
				alert("댓글 내용을 작성해 주십시오");
			}
			
			
		//전송	
		$("#frm2").attr("method","POST");
		$("#frm2").attr("action","<%=request.getContextPath()%>/reply/replyEdit.do?ridx="+idx1+"&bidx="+idx2+"").submit();
			
			
		return;
			
		});
		
		
		
	}
	
	
	//대댓글 작성
	function C_replyFn(idx1, idx2, idx3, idx4, idx5, idx6, idx7 ){
		
	var html="";
	html += "<form name = 'frm3' id='frm3'>";
	html += "<textarea name='replyContent_R' id='replyContent_R' style='width: 900px;''></textarea>";
	html += "<button type='button' class='btn3' id='canclebtn2'>닫기</button>";
	html += "<button type='button' class='btn3' id='replybtn'>작성</button>";
	html += "<input type='hidden' name='originRidx' value='"+idx3+"'>";
	html += "<input type='hidden' name='depth' value='"+idx4+"'>";
	html += "<input type='hidden' name='level_' value='"+idx5+"'>";
	html += "<input type='hidden' name='replyWriter' value='"+idx6+"'>"; 
	html += "<input type='hidden' name='replyWdate' value='"+idx7+"'>";
	html += "</form>";
	
	
	
	
	$("#C_replyBox"+idx1).html(html);


	//댓글 칸 열기
	$("#member"+idx1).css('display','block');
	$("#C_replyBox"+idx1).css('display','block');
	
	//취소
	$("#canclebtn2").click(function(){
		$("#C_replyBox"+idx1).empty();
		$("#member"+idx1).css('display','none');
	})
	
	//작성 버튼클릭
	 $("#replybtn").click(function(){
			//alert("왘");
		if($("#replyContent_R") ==""){
			alert("댓글 내용을 작성해 주십시오");
		}
		
		$("#frm3").attr("method","POST");
		$("#frm3").attr("action","<%=request.getContextPath()%>/reply/replyComment.do?ridx="+idx1+"&bidx="+idx2+"&originRidx="+idx3+"&depth="+idx4+"&level_="+idx5+"&replyWriter="+idx6+"&replyWdate="+idx7+"").submit();
		
			
		return;

	});
	 
	
	}
	


</script>

</head>

<body>
<div id="wrap">
<!-- header -->
<header>
<a href="<%=request.getContextPath()%>/"><img src="../image/movizam.png" alt="배너 이미지" id="logo"></a>


<div id="menu">
<ul>
	<li class="menutab" ><a href="<%=request.getContextPath() %>/film/filmAll.do">영화</a>
		<% if(session.getAttribute("midx") != null && session.getAttribute("adminYN").equals("Y")){	%>			<!-- 문자열 비교임으로 .equals() -->
		<ul class="menutab_content">	
			<li><a href="<%=request.getContextPath() %>/film/filmInsert.do">영화 등록(관리자)</a></li>
			<li><a href="<%=request.getContextPath() %>/film/filmList.do">영화 수정/삭제(관리자)</a></li>
		</ul>	
		<% } %>
	</li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList_film.do">영화게시판</a></li>
	<li class="menutab"><a href="<%=request.getContextPath() %>/board/boardList.do">자유게시판</a></li>
</ul>
</div>

<!-- Login, Join -->

<% if(session.getAttribute("midx") == null){%>

<a href="<%=request.getContextPath()%>/member/memberLogin.do" id="login">Login</a>
<a href="<%=request.getContextPath()%>/member/memberJoin.do" id="join">Join</a>
<% } %>

<span id="loginSet">

<%if(session.getAttribute("midx") != null){ 		%>
	<a href="<%=request.getContextPath()%>/member/memberInfo.do" style="font-size: 16px; color: black;"><%=session.getAttribute("memberNickname")%> <span>님</span><br></a>
	<%=session.getAttribute("message")			%>
	
	
<a href="<%=request.getContextPath() %>/member/memberLogout.do" onclick="logout()">로그아웃</a>
<% } %>




</span>

<hr id="first_hr">



<!-- 검색창 -->
<div class="search">
<form name="frm" action="<%=request.getContextPath()%>/main/mainSearchAction.do" method="post">
	<input type="text" name="keyword" id="searchBar">
	<button type="button" name="searchButton" id="searchbtn" onclick="location.href='<%=request.getContextPath()%>/main/mainSearchAction.do'"><img src="../image/search.png" alt="searchButton"></button>
</form>
</div>

<hr id="pink_hr">


</header>

<div id="navmainWrap">
<!-- nav1 -->
<nav id="nav">

<ul class="tab_title">
	<li><a href="<%=request.getContextPath()%>/board/hitList.do">인기게시판</a></li>
	<li><a href="<%=request.getContextPath()%>/board/likeList.do">추천게시판</a></li>
	<li><a href="<%=request.getContextPath()%>/board/boardList.do">자유게시판</a></li>
	<li><a href="<%=request.getContextPath() %>/board/boardList_film.do">영화게시판</a></li>
</ul>



</nav>

<div id="separate"></div>

<!-------------------------------- main ------------------------------------>
<main>

<div class="tab_content">

<span>| <%=bv.getBoardTitle() %></span>


<table class="boardContent">
	<tr class="t1">
		<td>작성자</td><td><%=bv.getBoardWriter() %></td>
		<td>조회수</td><td><%=bv.getBoardHit() %></td>
		<td>추천수</td><td><%=bv.getBoardLike() %></td>
		<td>작성일</td> <td><%=bv.getBoardWdate().substring(0, 10)%> </td>
	
				
	</tr>
	<!-- 사진있으면 보여주기 -->
	<% if(bv.getBoardFilename() != null){	%>
	<tr>
		<td colspan="8">
		<img src="<%=request.getContextPath()%>/image/boardImg/<%=bv.getBoardFilename() %>" alt="<%=bv.getBoardFilename() %>" style="width: 500px; height: 600px; margin: 50px;">
		</td>
	</tr>	
	<% } %>
	<tr>
		<td class="t2" colspan="8"><%=bv.getBoardContent() %></td>
	</tr>
	
</table>
<!-- 세션의 회원번호랑 게시물의 midx 비교해서 본인만 보이게  -->
<% 
if(session.getAttribute("midx") != null) {	//로그인한 회원은 버튼 안보이고
	if(session.getAttribute("midx") == (Object)(bv.getMidx())){		//로그인하고 게시글이랑 midx 동일하면 보이기
 %>
<button type="button" class="btn1" onclick="location.href='<%=request.getContextPath()%>/board/boardEdit.do?bidx=<%=bv.getBidx()%>'">수정</button>
<button type="button" class="btn1" onclick="deleteFn(<%=bv.getBidx()%>)">삭제</button>

<% } }%>
<button type="button" class="btn2" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'">목록</button>
<button type="button" class="btn2" onclick="location.href='<%=request.getContextPath()%>/board/contentRecommend.do?bidx=<%=bv.getBidx()%>&flag=1'">추천</button>

</div>


<!-- 이전글, 다음글 -->
<div> 
<table id="preNext">
<% if(map.get("pre") == -1){%>
 	<tr>
		<td></td>
	</tr>
<%}else{%>
	<tr>
		<td onclick="location.href='<%=request.getContextPath() %>/board/boardContent.do?bidx=<%=map.get("pre")%>'">이전글 | </td>
	</tr>
<% } %>

<% if(map.get("next") == null){%>
 	<tr>
		<td></td>
	</tr>
<%}else{%>
	<tr>
		<td onclick="location.href='<%=request.getContextPath() %>/board/boardContent.do?bidx=<%=map.get("next")%>'">다음글 | </td>
	</tr>
<% } %>
</table>
</div>



<!--------------------------- 댓글영역 (main의 flex 안에서 하단으로 정렬)----------------------------->


<div id="reply_area">

<!-- 댓글 출력리스트 -->
<table id="reply_list">
<% 
for(ReplyVo rpd : alist){	
%>
<tr>
<td>
<% 
	for(int i=1;i<=rpd.getLevel_();i++){ //level 이 1이면  답변글이면 띄우고 ㄴ 표시를 앞에 나타내기
		out.println("&nbsp;&nbsp;"); 
		if(i == rpd.getLevel_()){
			out.println("ㄴ");
		}
	}	
%>
<%=rpd.getReplyWriter() %>
</td> 
<td>
<% 	if(session.getAttribute("midx") != null) {	//로그인만한 회원은 댓글버튼만 보이고  %>
	<!-- 날짜도 문자열이니까 따옴표로 묶어주기. 대댓글을 다는 회원은 현재 로그인한 사람임으로 session 값을 writer에 넘기기 -->
	<button type="button" class="btn4" id="C_replybtn" onclick="C_replyFn(<%=rpd.getRidx()%>, <%=rpd.getBidx()%>, <%=rpd.getOriginRidx()%>, <%=rpd.getDepth()%>, <%=rpd.getLevel_()%>, '<%=session.getAttribute("memberNickname")%>', '<%=rpd.getReplyWdate()%>' )" >댓글</button>  	
	<% if(session.getAttribute("midx") == (Object)(bv.getMidx())){		//로그인하고 게시글이랑 midx 동일하면 삭제, 수정까지 보이기%>
<button type="button" class="btn4" onclick="C_deleteFn(<%=rpd.getRidx()%>, <%=rpd.getBidx()%>)">삭제</button>
<button type="button" class="btn4" id="C_editbtn" onclick="C_editFn(<%=rpd.getRidx()%>, <%=rpd.getBidx()%>)">수정</button>
 <% } } %>

</td>
</tr>
<tr>
<td>
<!--댓글 내용, 수정 영역-->
<div id="editbox<%=rpd.getRidx()%>" style="width: 450px;"></div>
<div class ="replyContent">
<% 
	for(int i=1;i<=rpd.getLevel_();i++){ //level 이 1이면  답변글이면 띄우고 ㄴ 표시를 앞에 나타내기
		out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
	}	

if(rpd.getBlindYN().equals("Y")){
	out.print("관리자에 의해 블라인드처리된 댓글입니다.");
}else{%>
	<%=rpd.getReplyContent()%>
<% 	}	%>

</div>
<textarea name ="content2" id ="content2<%=rpd.getRidx()%>" style="display : none;"><%=rpd.getReplyContent() %></textarea>
</td>
</tr>

<!-- 대댓글 -->

<tr>

	<%if (session.getAttribute("midx") != null){  %>
	<td id ="member<%=rpd.getRidx()%>" style="display: none"><%=session.getAttribute("memberNickname")%>님</td>
	<% } %>
</tr>
<tr>
	<td> 
		<div id="C_replyBox<%=rpd.getRidx()%>" style="width:800px;"></div>
	</td> 
	
</tr>
<% } %><!-- 댓글 반복문 끝나는 지점 -->



<%-- <% } %> --%>
</table>

<!-- 댓글 작성칸 -->
<form name ="frm" id="frm">
	<table> 
		<tr>
		<%if (session.getAttribute("midx") != null){  %>
		<td><%=session.getAttribute("memberNickname")%>님</td>
		<% } %>
		</tr>
		<tr>
		<td>
		<%if (session.getAttribute("midx") != null){  %>
		<textarea name="replyContent" id="replyContent" placeholder="소중한 댓글을 부탁드려요!"></textarea>
		<% }else if(session.getAttribute("midx") == null) {%>
		<textarea name="replyContent" id="replyContent" readonly>댓글은 로그인 후 작성가능합니다.</textarea>
		 <% } %>
		
		</td>
		</tr>
	</table>
	
	<%if (session.getAttribute("midx") != null){  %>
		<button type="button" class="btn3" onclick="checkFn(<%=bv.getBidx()%>)">등록</button>
	<% } %>
</form>


</div>

<hr id="reply_hr">


</main>
</div>

<!-- footer -->
<footer>
<span id="footer_Content">
(54930)전라북도 전주시 덕진구 백제대로 572 5층 이젠 IT 컴퓨터학원(금암동)<br>
대표이사 김연희    사업자등록번호 104-32-32504<br>
호스팅사업자 이젠 IT 컴퓨터학원 <br>
</span>
</footer>
</div>
</body>
</html>