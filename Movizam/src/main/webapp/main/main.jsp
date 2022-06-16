<%@page import="domain.*"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><!DOCTYPE html>
    
    <%		
   
    %>
    
     <% 
     ArrayList<FilmVo> alist1 = (ArrayList<FilmVo>)request.getAttribute("alist1"); 
     ArrayList<FilmVo> alist2 = (ArrayList<FilmVo>)request.getAttribute("alist2"); 
     ArrayList<FilmVo> alist3 = (ArrayList<FilmVo>)request.getAttribute("alist3");      
     FilmVo fv = (FilmVo)request.getAttribute("fv");
     
     
     ArrayList<BoardVo> alist4 = (ArrayList<BoardVo>)request.getAttribute("alist4");
     ArrayList<BoardVo> alist5 = (ArrayList<BoardVo>)request.getAttribute("alist5");
     
     
     %>
   
    
<html>
<head>
<meta charset="UTF-8">
<title>메인페이지</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/main/main.css">
<script src="<%=request.getContextPath() %>/js/jquery-3.6.0.min.js"></script>

<script>

//슬라이드 배너 
let interval;
let activeIndex = 1;

$(document).ready(function(){
 	interval = setInterval(changeActiveIndex,2500);
	$('.list-button-item').on('click',function(){
		
  	// list button의 색상 변경
    const index = $(this).index();
    activeIndex = index;
    changeActiveIndex();
    clearInterval(interval);
    // animation 재설정을 위해 animation을 잠시 제거한다.
    $('.banner').css('animation','none');
   	// animation 재설정
    $('.banner').animate({marginLeft:'${-100*index}%'},1,function(){
    	//1초의 시간 여유(해당 이미지로 이동하는 animation을 위한 시간)를 두고 다시 animation을 설정한다.
    	setTimeout(function(){
    		$('.banner').css('animation','animation${index+1} 10s infinite')
  
  		interval = setInterval(changeActiveIndex,2500);
      }, 1000)
    })
  })
})

//슬라이드 버튼 색변경(클래스 제거, 부여)
function changeActiveBtn(){
  $('.list-button-item').removeClass('active');
  $('.list-button span:eq(${activeIndex})').addClass('active');
}

//슬라이드 버튼 
function changeActiveIndex(){
	if(activeIndex>3) {
  	activeIndex%=4;
  }
  changeActiveBtn();
	activeIndex+=1;
}





/* 
$(document).ready(function(){
	$("#menutab ul.menutab_content").hide();
	$("#menutab ul li").click(function(){
		$("ul",this).slideToggle("fast");
	});
});

 */
 
 
 
 
//메뉴 탭(공통)
    $(function(){
		$("#menu").click(function(){
			 $(".menutab_content").slideToggle("fast"); 
		
		})

	});

 
 
//영화화면 처음에 로딩되면 상영예정, 상영종료는 숨기고 상영중만띄우기
$(function(){
	$(".movie_status_content > div").hide();
	$(".movie_status_content > div").eq(1).show();
});



//영화 메인 탭(상영예정, 상영중, 상영종료)
 	$(function(){
		$("ul.movie_status li").click(function(){
				var idx = $(this).index();
				$("ul.movie_status li").removeClass("on_tab");
				$(this).addClass("on_tab");
				$(".movie_status_content > div").hide();
				$(".movie_status_content > div").eq(idx).show();				
		});
	} );
	
	
	
//레이어 팝업 //화면이 로딩되면 숨겨져있는 상태
	$(function(){
		$(".popup").css("display","none");
		
	})
	
 //레이어 팝업 온(상세정보 보여주기)
	function showFn(idx){
		
	//ajax데이터 가지고 오기	
		$.ajax({
			type: "GET",
			//경로 대소문자 잘 보기
			url:"<%=request.getContextPath()%>/aJax/movieDetailAjax.jsp",	
			//매개변수값으로 가지고 온 데이터를 넘기기	// 응답페이지에서 파라미터로 받기
			data: "fidx="+idx,
			success : function(data){
				//데이터를 가져오는데 성공하면 팝업 띄우기
				$(".popup").css("display","block");
				
				console.log(data);
				var jData = JSON.parse(data.trim());
				
				var html = '';
				//하나의 데이터만 가지고 오기때문에 인덱스 0번째에 해당하는 것만 가지고 오면 된다. 
				html = "<tr><td colspan='2' rowspan ='3' id='L1'><img src='<%=request.getContextPath()%>/image/movie/"+jData[0].filmPoster+"' alt='영화포스터 입니다.' class='poster' style='width: 300px; height: 400px;'></td>"
				+"<td colspan='2' id='L2'>"+jData[0].filmName+"</td></tr>"
				+"<tr><td id='L3'>"+jData[0].filmStatus+"</td><td id='L4'>"+jData[0].filmDate+"</td></tr>"
				+"<tr><td colspan='2' id='L5'>"+jData[0].filmDetail+"</td></tr>";
				
				
				$("#movieDetails").html(html);
				
			}
		});
	
		
	}	
 
 
 
//예매 버튼 누르면  영화관으로 이동	
function goFn(){
	location.href='https://www.cgv.co.kr/';
}


//레이어팝업 닫기
function cancleFn(){
	$(".popup").css("display","none");
 }
 
</script>





</head>


<body>
<div id="wrap">
<!-- header -->
<header>
<a href="<%=request.getContextPath()%>/main/main.do"><img src="../image/movizam.png" alt="배너 이미지" id="logo"></a>


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

<script>
function logout(){
	alert("로그아웃되었습니다.")
}

</script>
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

<!-- main -->
<main>

<!-- 광고배너 슬라이드 -->
<div class="banner-container">
  <div class="banner">
    <div data-index=1><a href="https://www.jeonjufest.kr/"><img src="../image/slideBanner1.png" alt="banner_image" id="banner"></a></div>
    <div data-index=2><a href="https://www.16personalities.com/ko"><img src="../image/slideBanner2.png" alt="banner_image" id="banner"></a></div>
    <div data-index=3><a href="https://www.megabox.co.kr/"><img src="../image/slideBanner3.png" alt="banner_image" id="banner"></a></div>
    <div data-index=4><a href="https://www.cgv.co.kr/"><img src="../image/slideBanner4.png" alt="banner_image" id="banner"></a></div>
  </div>
</div>
<div class="list-button">
  <span class="list-button-item active"></span> 
  <span class="list-button-item"></span> 
  <span class="list-button-item"></span> 
  <span class="list-button-item"></span> 
</div>

	
<!-- 영화 -->	
	<ul class="movie_status">
		<li>상영예정</li>
		<li class ="on_tab">상영중</li>
		<li>상영종료</li>
	</ul>


	<div class="movie_status_content">
	<!--상영예정-->
		<div class="on_content" >
		<table> 
			<tr id="movieEach_tr">	
				<% 	
				for(FilmVo fv1 : alist1){		
				%>
				<td id="movieEach"><!--  td를 한칸씩 돌리기 (가로로배치됨) -->
					<table  onclick="showFn(<%=fv1.getFidx()%>)">
						<tr>
						<td><img src="<%=request.getContextPath()%>/image/movie/<%=fv1.getFilmPoster() %>" alt="사진 불러오기 실패" class="poster"></td>
						</tr>
						<tr>
						<td><%=fv1.getFilmName() %></td>
						</tr>
						<tr>
						<td id="movieEach"><%=fv1.getFilmDate().substring(0,10) %></td>
						</tr>
					</table>
				</td>
			<%} %>
			</tr>	<!-- 바깥 테이블의 tr -->
		
		</table>
		
		
		</div>
		
		<!--상영중-->
		<div class="on_content">
		<table> 
			<tr id="movieEach_tr">	
				<% 	
				for(FilmVo fv2 : alist2){		
				%>
				<td id="movieEach"><!--  td를 한칸씩 돌리기 (가로로배치됨) -->
					<table  onclick="showFn(<%=fv2.getFidx()%>)">
						<tr>
						<td><img src="<%=request.getContextPath()%>/image/movie/<%=fv2.getFilmPoster() %>" alt="사진 불러오기 실패" class="poster"  ></td>
						</tr>
						<tr>
						<td><%=fv2.getFilmName() %></td>
						</tr>
						<tr>
						<td id="movieEach"><%=fv2.getFilmDate().substring(0,10) %></td>
						</tr>
					</table>
				</td>
				<%	
				} %>
			</tr>	

		</table>
		</div>
		
		<!--상영종료-->
		<div class="on_content">
		
		<table> 
			<tr id="movieEach_tr">	
				<% 
				for(FilmVo fv3 : alist3){		
				%>
				<td id="movieEach"><!--  td를 한칸씩 돌리기 (가로로배치됨) -->
					<table  onclick="showFn(<%=fv3.getFidx()%>)">
						<tr>
						<td><img src="<%=request.getContextPath()%>/image/movie/<%=fv3.getFilmPoster() %>" alt="사진 불러오기 실패" class="poster"></td>
						</tr>
						<tr>
						<td><%=fv3.getFilmName() %></td>
						</tr>
						<tr>
						<td style="font-size: 16px;"><%=fv3.getFilmDate().substring(0,10) %></td>
						</tr>
					</table>
				</td>
				<% 
				} %>
			</tr>	<!-- 바깥 테이블의 tr -->
		</table>
		
		
		</div>
	</div>
	
	
<!-- 게시글 리스트 -->
	
	<div class="tab_content">
	<span>| 실시간 인기글</span>
	<table class="boardList">
	<thead>
		<tr>
		<th>NO.</th><th>제목</th><th>작성자</th><th>조회수</th><th>작성일</th>
		</tr>
	</thead>
	<tbody>
	<% for(BoardVo bv: alist4){%>
		<tr>
		<td><%=bv.getBidx() %></td>
		<td class="t1"><a href ="<%=request.getContextPath() %>/board/boardContent.do?bidx=<%=bv.getBidx() %>"><%=bv.getBoardTitle() %></a></td>
		<td style="width: 150px;"><%=bv.getBoardWriter() %></td>
		<td style="width: 100px;"><%=bv.getBoardHit() %></td>
		<td style="width: 200px;"><%=bv.getBoardWdate().substring(0,10) %></td>
		</tr>
	<%}%>	
	</tbody>

	</table>
	
	
	
	<span>| 실시간 추천글</span>
	<table class="boardList">
	<thead>
		<tr>
		<th>NO.</th><th>제목</th><th>작성자</th><th>추천수</th><th>작성일</th>
		</tr>
	</thead>
	<tbody>
	<% for(BoardVo bv: alist5){%>
		<tr>
		<td style="width: 80px;"><%=bv.getBidx() %></td>
		<td class="t1"><a href ="<%=request.getContextPath() %>/board/boardContent.do?bidx=<%=bv.getBidx() %>"><%=bv.getBoardTitle() %></a></td>
		<td style="width: 150px;"><%=bv.getBoardWriter() %></td>
		<td style="width: 100px;"><%=bv.getBoardLike() %></td>
		<td style="width: 200px;"><%=bv.getBoardWdate().substring(0,10) %></td>
		</tr>
	<%}%>	
	</tbody> 

	</table>
	</div>
	
	
	
	
	
	
</main>
</div>


<!-- 영화상세 팝업 --> 
<div class="popup">
	<!-- 팝업창 -->
	<div class="popup_layer"> 
		<table>
		<tbody id="movieDetails">
			<!-- aJax로 데이터 삽입하는 곳 -->
		</tbody>
		</table>
		<div class="btn_area"><!-- 버튼 영역 -->
			<button type="button" name="button" class="btn" onclick="goFn()" >예매하러가기</button>
			<button type="button" name="button" class="btn no" onclick="cancleFn()">아니오</button>
		</div>
	</div>
	<div class="popup_dimmed"></div><!-- 반투명 배경 -->
</div>
 

<!-- footer -->
<footer>
<span id="footer_Content">
(54930)전라북도 전주시 덕진구 백제대로 572 5층 이젠 IT 컴퓨터학원(금암동)<br>
대표이사 김연희    사업자등록번호 104-32-32504<br>
호스팅사업자 이젠 IT 컴퓨터학원 <br>
</span>
</footer>



</body>
</html>