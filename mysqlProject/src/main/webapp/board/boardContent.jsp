<%@page import="mysqlProject.domain.PageMaker"%>
<%@page import="mysqlProject.domain.CommentVo"%>
<%@page import="mysqlProject.service.CommentDao"%>
<%@page import="org.w3c.dom.CDATASection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
<%@page import="mysqlProject.domain.BoardVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	@SuppressWarnings("unchecked")
	ArrayList<CommentVo> clist = (ArrayList<CommentVo>) request.getAttribute("clist"); 
	BoardVo bv = (BoardVo) request.getAttribute("bv");
	PageMaker pm = (PageMaker) request.getAttribute("pm");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글보기</title>
<link rel="stylesheet" href="../css/basic.css">
<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
<style type="text/css">@import url("<%=request.getContextPath()%>/css/boardContent.css");
</style>

<script>
	//댓글달기
	function comment(){
		if(<%=session.getAttribute("midx")%>==null){
			alert("로그인 후 이용가능합니다");
			return;
		}
		else if($("#commContent").val()==""){
			alert("댓글을 입력하세요");
			return;
		}
		
		let bidx = <%=bv.getBidx()%>;
		let nicName = '<%=session.getAttribute("memberNicname")%>';
		let commContent = $("#commContent").val();
		$("#commContent").val("");
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/ajax/comment_list.jsp',
			data: { bidx: bidx, nicName: nicName, commContent: commContent},
			success: function(data){
				let json = JSON.parse(data.trim());
				window.location.reload();
			},
			error: function(){
				alert("error");
			}
		});
	}
	//댓글 삭제
	function commentDelete(cidx){
		let delComm = confirm('정말로 삭제하시겠습니까?');
		if(delComm){
			$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/ajax/comment_delete.jsp',
				data: {cidx: cidx},
				success: function(data){
					alert("삭제 완료");
					window.location.reload();
				}
			});
		}else return;
		
	}
	//대댓달기
	function reComment(obj){
		let cidx = $(obj).parent().parent().parent().prev().find("input").val();
		let bidx = <%=bv.getBidx()%>;
		let nicName = '<%=session.getAttribute("memberNicname")%>';
		let reContent = $("#reContent").val();
		$("#reContent").val("");
		$.ajax({
			type: "POST",
			url: '<%=request.getContextPath()%>/ajax/reComment.jsp',
			data: { cidx: cidx, bidx: bidx, nicName: nicName, reContent: reContent},
			success: function(data){
				let json = JSON.parse(data.trim());
				window.location.reload();
			},
			error: function(){
				alert("error");
			}
		});
	}
</script>
	
<script>
$(document).ready(function () {
	$(".comment-recom").click(function () {
		if(<%=session.getAttribute("midx")%> == null){
			alert("로그인 후 이용가능합니다");
			return;
		}
		let idx = `
		  	<div class='reComment'>
			<div class='reComment-box'>
			<div class='board-comment-write'>
			<div class='board-comment-write-nic'><%=session.getAttribute("memberNicname")%></div>
			<textarea name='commContent' id='reContent' onkeydown='resize(this)' onkeyup='resize(this)' placeholder='댓글을 입력하세요'></textarea>
			<input type='button' class='board-comment-write-btn' style='margin-left: 10px;' value='취소' onclick='reCommdel();'>
			<input type='button' class='board-comment-write-btn' value='등록' onclick='reComment(this);'>
			<input type='hidden' name='bidx' value="<%=bv.getBidx()%>">
			</div></div></div>
			`;
		if($("#reContent").val() != "" && $(".reComment").length){
			let sw = confirm("확인을 누르시면 작성중이던 답글은 사라집니다");
			if(sw){}
			else return;			
		}
		$(".reComment").remove();
		$(this).parent().parent().after(idx);
	});
});
function reCommdel(){
	$(".reComment").remove();
}
function resize(obj){
	obj.style.height = '17px';
	obj.style.height = (obj.scrollHeight) + 'px';
}
</script>
<script>
	//게시글 삭제
	function boardDelete(){
		let sw = confirm("정말로 삭제하시겠습니까?");
		if(sw){
			$(location).attr("href",'<%=request.getContextPath()%>/board/delete.do?bidx=<%=bv.getBidx()%>');	
		}
		else return;
	}
	function boardModify(){
		$(location).attr("href",'<%=request.getContextPath()%>/board/modify.do?bidx=<%=bv.getBidx()%>');
	}
</script>
</head>
<body>
<article>
	<header id="header"></header>
	<nav id="topNav"></nav>
	<nav id="leftNav"></nav>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#header").load("<%=request.getContextPath()%>/article/header.jsp");
			$("#topNav").load("<%=request.getContextPath()%>/article/topNav.jsp");
			$("#leftNav").load("<%=request.getContextPath()%>/article/leftNav.jsp");
		});
	</script> 
<!----------------------------------------------------------------------------------------------------------------------------->
	<section>
		<h4></h4>
	<hr>
	<div class="board">
		<div class="board-header">
			<div class="board-header-cont"><%
				if(bv.getCategory()==1){out.print("공지사항");}
				else if(bv.getCategory()==2){out.print("자유게시판");}
				else if(bv.getCategory()==3){out.print("질문게시판");}
				else if(bv.getCategory()==4){out.print("게임소식");}
				else if(bv.getCategory()==5){out.print("일상소식");}
				else if(bv.getCategory()==6){out.print("갤러리");}
			%></div>
			
			
			<%
			if(session.getAttribute("midx") != null){
				if(session.getAttribute("midx").equals(bv.getMidx()) || session.getAttribute("oper").equals("Y")){
					out.println("<input type='button' class='board-header-cont' onclick='boardDelete()' value='삭제'>");
					out.println("<input type='button' class='board-header-cont' onclick='boardModify()' value='수정'>");
				}
			}%>
			<div class="board-header-cont"><%=bv.getTitle()%></div>
		</div>
		<div class="board-nav">
			<ul>
				<li class="board-nav-cont"><%=bv.getWriter()%></li>
				<li class="board-nav-cont"><%=bv.getWriteday()%></li>
				<li class="board-nav-cont">[<%=bv.getBoardComment()%>]</li>
				<li class="board-nav-cont">조회수</li>
			</ul>
		</div>
		<br>
		<div class="board-content"><%=bv.getContent()%></div>
		<br>
		<div class="board-member-idx">
			<a href="#"><img src="/profile/<%=bv.getMidx()%>/<%=bv.getProfile()%>"><span><%=bv.getWriter()%></span>님의 게시글 검색</a>
		</div>
		<div class="board-bottom-nav">
			<div class="board-bottom-nav-cont">댓글💬</div>
			<input type="button" class="board-bottom-nav-cont" value="목록" onclick="location.href='<%=request.getContextPath()%>/board/list.do?category=<%=bv.getCategory()%>'">
			<input type="button" class="board-bottom-nav-cont" value="답글" onclick="location.href='<%=request.getContextPath()%>/board/reply.do?bidx=<%=bv.getOriginbidx()%>&category=<%=bv.getCategory()%>&depth=<%=bv.getDepth()%>'">
		</div>
		
		<div class="board-comment-list">
			<ul class="comment-list-ul">
				<%for(CommentVo cv : clist){
					if(cv.getBidx()==bv.getBidx()){
						if(cv.getDelyn().equals("Y") && cv.getLevel_()==0){%>
							<li class="comment-list-li">
								<div><span><%=cv.getWriter()%></span><span><%=cv.getDay()%></span></div>
								<div style="font-size: 15px; color: gray;">삭제된 댓글입니다.</div>
							</li>
						<%}else if(cv.getDelyn().equals("Y") && cv.getLevel_()==1){%>
							<li class="comment-list-li recomm-li">
								<div><span><%=cv.getWriter()%></span><span><%=cv.getDay()%></span></div>
								<div style="font-size: 15px; color: gray;">삭제된 댓글입니다.</div>
							</li>
						<!-- delyn이 N일 경우 -->
						<%}else{
							if(cv.getLevel_() == 0){%>
								<li class="comment-list-li">
							<%}
							else{%>
								<li class="comment-list-li recomm-li">
							<%}%>
								<div>
									<span><%=cv.getWriter()%></span><span><%=cv.getDay()%></span>
									<input type="hidden" value="<%=cv.getCidx()%>">	
									<%if(session.getAttribute("midx") != null){
										if(session.getAttribute("memberNicname").equals(cv.getWriter()) || session.getAttribute("oper").equals("Y")){
											out.println("<a onclick='commentDelete("+cv.getCidx()+");'>삭제</a>");
										}
									}%>
								</div>
								<div><%=cv.getContent()%></div>
								<%if(cv.getLevel_() == 0){%>
								<div><a class="comment-recom">답글</a></div>
								<%}%>
							</li>
						<%}%>
					<%}%>
				<%}%>
			</ul>
		</div>
		<table id="numberTable">
			<tr>
			<td>
				<% 
				if(pm.isPrev() == true){out.println("<a href='"+request.getContextPath()+"/board/content.do?page="+(pm.getStartPage()-1)+"&bidx="+bv.getBidx()+"'>◀</a>");}
				%>
			</td>
			<td>
				<%
					for(int i=pm.getStartPage(); i<=pm.getEndPage();i++){
						out.println("<a href='"+request.getContextPath()+"/board/content.do?page="+i+"&bidx="+bv.getBidx()+"'>"+i+"</a>");
					}
				%>
			</td>
			<td>
				<% if(pm.isNext() && pm.getEndPage() >0){
					out.println("<a href='"+request.getContextPath()+"/board/list.do?page="+(pm.getEndPage()+1)+"&bidx="+bv.getBidx()+"'>▶</a>");
					}
				%>
			</td>
			</tr>
		</table><br>
		<div class="board-comment">
			<div class="board-comment-box">
				<div class="board-comment-write">
						<div class="board-comment-write-nic"><%if(session.getAttribute("midx")==null){out.print("로그인하세요");}else{%><%=session.getAttribute("memberNicname")%><%}%></div>
						<textarea name="commContent" id="commContent" onkeydown="resize(this)" onkeyup="resize(this)" placeholder="댓글을 입력하세요"></textarea>
						<script>
							function resize(obj){
								obj.style.height = '17px';
								obj.style.height = (obj.scrollHeight) + 'px';
							}
						</script>
						<input type="button" class="board-comment-write-btn" value="등록" onclick="comment();">
				</div>
			</div>
		</div>
		<div class="board-all-list"></div>
	</div>
	</section>
</article>
</body>
</html>
