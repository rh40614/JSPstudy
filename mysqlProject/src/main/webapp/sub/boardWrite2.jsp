<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
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
		width: 776px;
		height: 700px;
		margin: 0 10px;
		border: 1px solid lightgray;
		resize: none;
		padding: 10px 0 0 10px;
	}
	#notice{
		width: 100px;
		height: 27px;
		margin: 15px 0 15px 11px;
		border: none;		
	}
</style>

<script type="text/javascript" src="../js/js2/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/opensource/se2/photo_uploader/plugin/hp_SE2M_AttachQuickPhoto.js" charset="utf-8"></script>



</head>
<body>
	<form name="bwrite">
						<select name="bcode" class="bcode" style="width:200px; height:30px; text-align:left;
						 padding-left:10px; background-color:#f4f4f4; border:none; border-radius:3px;">
							<option>게시판선택</option>
								<option value="자유게시판">자유게시판</option>
								<option value="리뷰게시판">리뷰게시판</option>
								<option value="질의응답"></option>
								<option value="공지사항" id="option">공지사항</option>
						</select>
						
						<input type="text" name="subject" id="bsubject" placeholder="제목">
					<!-- 스마트에디터 구현 -->
					<div id=smedit>
						<textarea rows="20" name="content" id="form-control" style="width:850px; height:600px;"></textarea>
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
								
							function write1(elClickedObj) {
								  // 에디터의 내용이 textarea에 적용된다.
								  oEditors.getById["form-control"].exec("UPDATE_CONTENTS_FIELD", [ ]);
								  // 에디터의 내용에 대한 값 검증은 이곳에서
								  // document.getElementById("textAreaContent").value를 이용해서 처리한다.

								  var bw = document.bwrite;
									if(bw.subject.value == ""){
										alert("제목을 입력하세요");
										document.bwrite.subject.focus();
										return;
									}else if(bw.content.value == ""){
										alert("내용을 입력하세요");
										document.bwrite.content.focus();
										return;
									}
									bw.action="<%=request.getContextPath()%>/board/boardWriteAction.do";
									bw.method = "post";
									bw.submit();	
									return;
								  
								  try {
								  elClickedObj.form.submit();
								  } catch(e) {

								  }
							  }
							
						</script>
					</div>
					</form>
</body>
</html>
