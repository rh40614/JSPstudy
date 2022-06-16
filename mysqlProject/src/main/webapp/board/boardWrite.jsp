<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	if(session.getAttribute("midx") == null){
		session.setAttribute("saveUrl", request.getRequestURI());
		out.println("<script>alert('로그인 해주세요');location.href='"+request.getContextPath()+"/member/login.do'</script>");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/basic.css">
<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
<style type="text/css">@import url("<%=request.getContextPath()%>/css/boardWrite.css");</style>
<script type="text/javascript">
	$(function (){
		$("#notice").click(function (){
			if($('#cate option:gt(1)').is(":disabled")){
				$('#notice').css("color","black");
				$('#cate').val("0");
				$('#mainnotice').val("N");
				$('#cate option:gt(1)').attr("disabled",false);
			}
			else{
				$('#notice').css("color","red");
				$('#cate').val("1");
				$('#mainnotice').val("Y");
				$("#cate option:gt(1)").attr("disabled",true);
			}
		});
	})
</script>

<script>
	function save(){
		oEditors.getById["txtContent"].exec("UPDATE_CONTENTS_FIELD", []);  
	    		//스마트 에디터 값을 텍스트컨텐츠로 전달
		var content = document.getElementById("smartEditor").value;
		alert(document.getElementById("txtContent").value); 
	    		// 값을 불러올 땐 document.get으로 받아오기
		return; 
}
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/opensource/se2/js/HuskyEZCreator.js" charset="utf-8"></script>
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
		<h4>글쓰기</h4>
	<hr>
	<form name="bwrite" id="write">
		<select name="category" class="board-category" id="cate">
			<option value="">게시판선택</option>
			<%
				if(session.getAttribute("midx") != null){
					if(session.getAttribute("oper").equals("Y")){
						out.println("<option value='1'>공지사항</option>");
					}	
				}
			%>
			<option value="2">자유 게시판</option>
			<option value="3">질문 게시판</option>
			<option value="">===============</option>
			<option value="4">게임소식</option>
			<option value="5">일상소식</option>
			<option value="6">갤러리</option>
		</select>
		<%
			if(session.getAttribute("midx") != null){
				if(session.getAttribute("oper").equals("Y")){
					out.println("<input type='button' id='notice' value='공지사항(중요)'>");
				}	
			}
		%>
		<input type="text" id="mainnotice" name = "mainnotice" value="N" style="display: none;"> 
		<input type="button" onclick="location.href='<%=request.getContextPath()%>/'" value="취소">
		<input type="button" onclick="check();" value="확인">
		<br><br>
		<input type="text" class="outBox" name="title" placeholder="제목을 입력하세요">
		<br><br>
					<div id=smedit>
						<textarea rows="20" name="content" id="form-control"></textarea>
						<script type="text/javascript">
							var form = bwrite;
						
							var oEditors = [];
							
								nhn.husky.EZCreator.createInIFrame({
									oAppRef : oEditors,
									elPlaceHolder : "form-control",
									sSkinURI : "../js/SmartEditor2Skin.html",
									htParams : {
										// 툴바 사용 여부 (true:사용/ false:사용하지 않음)            
										bUseToolbar : true,                         
										// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)            
										bUseVerticalResizer : false,                 
										// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)           
										bUseModeChanger : true,         
										},
									fCreator: "createSEditor2"
								});
								
								//이미지삽입 - 업로드 완료페이지에서 호출됨.
								function insertIMG(fname){
								  var filepath = form.filepath.value;
								  var sHTML = "<img src='" + filepath + "/" + fname + "' style='cursor:hand;' border='0'>";  
								    // filepath 는 변수처리 혹은 직접 코딩해도 상관없음. 
								    // imageUpload.asp 에서 insertIMG 호출시 경로를 포함하여 넣어주는 방법을 추천.
								  oEditors.getById["form-control"].exec("PASTE_HTML", [sHTML]);
								}
								
								function pasteHTML(filepath){
								    var sHTML = '<img src="<%=request.getContextPath()%>/upload/'+filepath+'">';
								    oEditors.getById["form-control"].exec("PASTE_HTML", [sHTML]);
								}	
								
							function check(elClickedObj) {
								  // 에디터의 내용이 textarea에 적용된다.
								  oEditors.getById["form-control"].exec("UPDATE_CONTENTS_FIELD", [ ]);
								  // 에디터의 내용에 대한 값 검증은 이곳에서
								  // document.getElementById("textAreaContent").value를 이용해서 처리한다.

								  var bw = document.bwrite;
									if(bw.category.value ==""){
										alert("게시판 종류를 선택하세요");
										bw.category.focus();
										return;
									}
									else if(bw.title.value == ""){
										alert("제목을 입력하세요");
										bw.title.focus();
										return;
									}else if(bw.content.value == "<br>" || bw.content.value == ""){
										alert("내용을 입력하세요");
										bw.content.focus();
										return;
									}
									bw.action = "<%=request.getContextPath()%>/board/writeAction.do";
									bw.method = "post";
									bw.submit();
									
								  try {
								  elClickedObj.form.submit();
								  } catch(e) {

								  }
							  }
							
						</script>
					</div>
					</form>
	</section>
</article>
</body>
</html>
