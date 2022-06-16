<%@page import="mysqlProject.domain.BoardVo"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	BoardVo bv = (BoardVo) request.getAttribute("bv");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<link rel="stylesheet" href="../css/basic.css">
<script src="/jQuery/js/jquery-3.6.0.min.js"></script>
<style type="text/css">
	/* boardWrite용 스타일 */		
	select,extarea{cursor:pointer;}
	form#write {width: 808px;}
	#write>select {
		width: 120px;
		height: 27px;
		margin: 15px 0 15px 10px;
		text-align: center;
	}
	#write input[type="button"]:not(#notice){
		width: 100px;
		height: 27px;
		float: right;
		margin: 15px 11px 15px 0;	
		border: none;
	}
	#write .outBox,textarea{
		font-size: 15px;
		font-family:'Malgun Gothic';
	}
	.areaHelpBox,.outBox{
		width: 786px;
		height: 30px;
		margin: 0 10px 0 10px;
		text-indent: 10px;
		border: 1px solid lightgray;
		display: block;
		padding: 0;
	}
	textarea{
		width: 804px;
		height: 700px;
	}
	#notice{
		width: 100px;
		height: 27px;
		margin: 15px 0 15px 11px;
		border: none;		
	}
</style>

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
<script type="text/javascript" src="<%=request.getContextPath()%>/opensource/se2/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>



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
		<h4>수정하기</h4>
	<hr>
	<form name="bwrite" id="write">
		<select name="category" class="board-category" id="cate">
			<option value="">게시판선택</option>
			<%
				if(session.getAttribute("oper").equals("Y")){
					out.println("<option value='1'>공지사항</option>");
				}
			%>
			<option value="2">자유 게시판</option>
			<option value="3">질문 게시판</option>
			<option value="">===============</option>
			<option value="4">게임소식</option>
			<option value="5">일상소식</option>
			<option value="6">갤러리</option>
		</select>
		<script>
			if(<%=bv.getCategory()%> == 1){
				$(".board-category").val("1").attr("selected","selected");
			}else if(<%=bv.getCategory()%> == 2){
				$(".board-category").val("2").attr("selected","selected");
			}else if(<%=bv.getCategory()%> == 3){
				$(".board-category").val("3").attr("selected","selected");
			}else if(<%=bv.getCategory()%> == 4){
				$(".board-category").val("4").attr("selected","selected");
			}else if(<%=bv.getCategory()%> == 5){
				$(".board-category").val("5").attr("selected","selected");
			}else if(<%=bv.getCategory()%> == 6){
				$(".board-category").val("6").attr("selected","selected");
			}
		</script>
		<input type="text" id="mainnotice" name = "mainnotice" value="N" style="display: none;">
		<%
			if(session.getAttribute("oper").equals("Y")){
				out.println("<input type='button' id='notice' value='공지사항(중요)'>");
				if(bv.getNotice().equals("Y")){
					out.println("<script>$('#notice').css('color','red');$('#cate').val('1');$('#mainnotice').val('Y');$('#cate option:gt(1)').attr('disabled',true);</script>");
				}
			}	
		%> 
		<input type="button" onclick="location.href='<%=request.getContextPath()%>/'" value="취소">
		<input type="button" onclick="check();" value="확인">
		<br><br>
		<input type="text" class="outBox" name="title" value="<%=bv.getTitle()%>">
		<br><br>
		<div id=smedit>
			<textarea rows="20" name="content" id="form-control" ><%=bv.getContent()%></textarea>
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
						}else if(bw.content.value == "" || bw.content.value == "<br>" || bw.content.value == "<p>&nbsp;</p>"){
							alert("내용을 입력하세요");
							bw.content.focus();
							return;
						}
						bw.action = "<%=request.getContextPath()%>/board/modifyAction.do?bidx=<%=bv.getBidx()%>";
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
