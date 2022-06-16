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

<%
	if(session.getAttribute("midx") != null){
		out.println("<style>#login-box{display:inherit;}</style>");
		out.println("<style>#logout-box{display:none;}</style>");
	}else{
		out.println("<style>#login-box{display:none;}</style>");
		out.println("<style>#logout-box{display:inherit;}</style>");
	}
%>

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
	<header>
		<a href="<%=request.getContextPath()%>/"><img alt="홈페이지" src="../img/logoImg.PNG"></a>
		<div class="header-slide">
			<div class="header-slide-container">
				<div class="header-slides fade">
					<img alt="valtan" src="<%=request.getContextPath()%>/img/baltan.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=30"'>
				</div>
				<div class="header-slides fade">
					<img alt="biackiss" src="<%=request.getContextPath()%>/img/biackiss.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=31"'>
				</div>
				<div class="header-slides fade">
					<img alt="koukusaton" src="<%=request.getContextPath()%>/img/koukusaton.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=32"'>
				</div>
				<a class="prev" onclick="plusSlides(-1)">&#10094;</a>
				<a class="next" onclick="plusSlides(1)">&#10095;</a>
				<div class="header-slide-dot" style="text-align: center;">
					<span class="dot" onclick="currentSlide(1)"></span>
					<span class="dot" onclick="currentSlide(2)"></span>
					<span class="dot" onclick="currentSlide(3)"></span>
				</div>
			</div>
			<script>
				var slideIndex = 0;
				showSlides();
				function plusSlides(n){
					showSlides_(slideIndex += n);
				}
				function currentSlide(n){
					showSlides_(slideIndex = n);
				}
				function showSlides_(n) {
					  var i;
					  var slides = document.getElementsByClassName("header-slides");
					  var dots = document.getElementsByClassName("dot");
					  if (n > slides.length) {slideIndex = 1}    
					  if (n < 1) {slideIndex = slides.length}
					  for (i = 0; i < slides.length; i++) {
					      slides[i].style.display = "none";  
					  }
					  for (i = 0; i < dots.length; i++) {
					      dots[i].className = dots[i].className.replace(" active", "");
					  }
					  slides[slideIndex-1].style.display = "block";  
					  dots[slideIndex-1].className += " active";
					}
				
				function showSlides() {
				    var i;
				    var slides = document.getElementsByClassName("header-slides");
				    var dots = document.getElementsByClassName("dot");
				    for (i = 0; i < slides.length; i++) {
				       slides[i].style.display = "none";  
				    }
				    slideIndex++;
				    if (slideIndex > slides.length) {slideIndex = 1}    
				    for (i = 0; i < dots.length; i++) {
				        dots[i].className = dots[i].className.replace(" active", "");
				    }
				    slides[slideIndex-1].style.display = "block";  
				    dots[slideIndex-1].className += " active";
				    setTimeout(showSlides, 5000); // Change image every 2 seconds
				}
			</script>
		</div>
	</header>
	
	<nav id="topNav">
		<ul class="topList">
			<li><a href="<%=request.getContextPath()%>/">홈</a></li>
			<li>
				<a href="#">커뮤니티</a>
				<ul class="topList-depth">
					<li><a href="<%=request.getContextPath()%>/board/list.do?category=1">공지사항</a></li>
					<li><a href="<%=request.getContextPath()%>/board/list.do?category=2">자유 게시판</a></li>
					<li><a href="<%=request.getContextPath()%>/board/list.do?category=3">질문 게시판</a></li>
				</ul>
			</li>
			<li>
				<a href="#">정보 게시판</a>
				<ul class="topList-depth">
					<li><a href="<%=request.getContextPath()%>/board/list.do?category=4">게임소식</a></li>
					<li><a href="<%=request.getContextPath()%>/board/list.do?category=5">일상소식</a></li>
				</ul>
			</li>
			<li>
				<a href="#">갤러리</a>
			</li>			
		</ul>
	</nav>
	
	<nav id="leftNav">
		<div id="logout-box">
			<br>
			<input type="button" id="login"value="로그인" onclick="location.href='<%=request.getContextPath()%>/member/login.do'">
			<div id="logout-bottom">
				<span id="findId"><a href="<%=request.getContextPath()%>/member/find.do">아이디/비밀번호 찾기</a></span>
				<span id="join"><a href="<%=request.getContextPath()%>/member/join.do">회원가입</a></span>
			</div>
		</div>
		<div id="login-box">
			<input type="image" id="login-profile" alt="로딩중.." src="<%if(session.getAttribute("midx") == null){ }else{%><%=request.getContextPath()%>/img/profile/<%=session.getAttribute("midx")%>/<%=session.getAttribute("profile")%><%}%>" onclick="location.href='<%=request.getContextPath()%>/member/info.do'">
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
					<span><a>갤러리</a></span>
				</li>
			</ul>
		</div>
	</nav>
	<section>
		<h4>글쓰기</h4>
	<hr>
	<form name="frm" id="write">
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
		<textarea id="txtContent" cols="60" name="content" style="width:800px; height:600px;" placeholder="내용을 입력하세요"></textarea>

	<script type="text/javascript">
		var form = frm;
		
		var oEditors = [];
		nhn.husky.EZCreator.createInIFrame({
		    oAppRef: oEditors,
		    elPlaceHolder: "txtContent",
		    sSkinURI: "<%=request.getContextPath()%>/opensource/se2/SmartEditor2Skin.html",
		 	// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
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
		
		function insertIMG(fname){
			  var filepath = form.filepath.value;
			  var sHTML = "<img src='" + filepath + "/" + fname + "' style='cursor:hand;' border='0'>";
			  oEditors.getById["txtContent"].exec("PASTE_HTML", [sHTML]);
			}
		
		function pasteHTML(filepath){
			var sHTML = '<img src="<%=request.getContextPath()%>/upload/'+filepath+'">';
			oEditors.getById["txtContent"].exec("PASTE_HTML",[sHTML]);
		}
		
		//‘저장’ 버튼을 누르는 등 저장을 위한 액션을 했을 때 submitContents가 호출된다고 가정한다.
		function check(elClickedObj) {
		    // 에디터의 내용이 textarea에 적용된다.
		    oEditors.getById["txtContent"].exec("UPDATE_CONTENTS_FIELD", [ ]);
		    // 에디터의 내용에 대한 값 검증은 이곳에서
		    // document.getElementById("textAreaContent").value를 이용해서 처리한다.
		    var fm = document.frm;
			if(fm.category.value ==""){
				alert("게시판 종류를 선택하세요");
				fm.category.focus();
				return;
			}
			else if(fm.title.value == ""){
				alert("제목을 입력하세요");
				document.frm.title.focus();
				return;
			}else if(fm.content.value == "<br>" || fm.content.value == ""){
				alert("내용을 입력하세요");
				document.frm.content.focus();
				return;
			}
			fm.action = "<%=request.getContextPath()%>/board/writeAction.do";
			fm.method = "post";
			fm.submit();
		    
		    /* 
		    try {
		        elClickedObj.form.submit();
		    } catch(e) {
		     
		    } */
		}
		 
		// textArea에 이미지 첨부
		function pasteHTML(filepath){
		    var sHTML = '<img src="<%=request.getContextPath()%>/upload/'+filepath+'">';
		    oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]);
		}
		
	</script>
		<br>
		<div id="se2_sample" style="margin:10px 0;">
			<input type="button" onclick="save();" value="본문 내용 가져오기">
		</div>
	</form>
	</section>
</article>
</body>
</html>
