<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
	<style>
		*{margin: 0; padding: 0;}
		body{
			width: 603px;
			color: white;
			font-size: 15px;
		}
		.box{
			width: 70%;
			margin: 0 auto;
			height: 300px;
			background-color: #595780;
			border: 1px solid white;
		}
		.box>label{
			width: 100px;
			height: 30px;
			cursor: pointer;
			text-align: center;
			display: block;
			margin: 15px auto;
			border: 1px solid gray;
			border-radius: 5px;
			background-color: #5cb85c;
			padding-top: 10px;
			
		}
		.box input[type="file"] {
			position: absolute;
			width: 1px;
			height: 1px;
			margin: -1px;
			overflow: hidden;
			clip:rect(0,0,0,0);
			border: 0;
		}
		.box-val{
			width: 100%;
			height: 100px;
		}	
		.box-val>img,#point{
			display: inline-block;
			float: left;
			width: 100px;
			height: 100px;
		}
		img{
			margin: 0 20px;
			background-color: white;
		}
		.box-val>#point{
			text-align: center;
			font-size: xx-large;
			margin: 0 20px;
			padding-top: 20px;
			height: 80px;
		}
		.box>input[type=button]{
			width: 100px;
			height: 30px;
			cursor: pointer;
			text-align: center;
			display: block;
			margin: 15px auto;
			border: 1px solid gray;
			border-radius: 5px;
		}
	</style>
	<script>
		function changeProfile(){
			var fm = document.frm;
			var check = confirm('위 사진으로 변경하시겠습니까?');
			if(check){
				fm.action = "<%=request.getContextPath()%>/member/infoProfile.do";
				fm.method = "post";
				fm.enctype="multipart/form-data";
				fm.submit();
			}else{
				return;
			}
		}
	</script>
</head>
<body>
	<br><br><br><br><br><br>
	<form name="frm">
	<div class="box">
		<label for="input_img">사진 업로드</label>
		<input type="file" accept="image/*" id="input_img" name="filename">
		<div class="box-val">
			<img id="login-profile" src="/profile/<%=session.getAttribute("midx")%>/<%=session.getAttribute("memberProfile")%>">
			<div id="point">→</div>
			<img name="newImg" id="newImg">
		</div>
		<script>
			$("#input_img").on("change", function(event) {

		    var file = event.target.files[0];

		    var reader = new FileReader(); 
		    reader.onload = function(e) {

		        $("#newImg").attr("src", e.target.result);
		    }

		    reader.readAsDataURL(file);
		});</script>
		<br><br><input type="button" onclick="changeProfile()" value="변경">
	</div>
	<input type="hidden" value="<%=session.getAttribute("midx")%>">
	</form>
</body>
</html>