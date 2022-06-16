<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css">
<a href="<%=request.getContextPath()%>/"><img alt="홈페이지" src="<%=request.getContextPath()%>/img/logoImg.PNG"></a>
<div class="header-slide">
	<div class="header-slide-container">
		<div class="header-slides fade">
			<img alt="valtan" src="<%=request.getContextPath()%>/img/back1.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=30"'>
		</div>
		<div class="header-slides fade">
			<img alt="biackiss" src="<%=request.getContextPath()%>/img/back2.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=31"'>
		</div>
		<div class="header-slides fade">
			<img alt="koukusaton" src="<%=request.getContextPath()%>/img/back3.jpg" onclick='location.href="<%=request.getContextPath()%>/board/content.do?bidx=32"'>
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
		    setTimeout(showSlides, 5000);
		}
	</script>
</div>