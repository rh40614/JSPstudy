<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><!DOCTYPE html>
    
<html>
<head>
<meta charset="UTF-8">
<title>메인페이지</title>
<link rel="stylesheet" href="index.css">


<script>
<!--main으로 넘어가게 만들기   -->
/* 데이터를 가지고 jsp로 바로 이동 할 수없으므로 다른 가상 주소 만들기 분리 123435*/
location.href="<%=request.getContextPath()%>/main/main.do"
수정
</script>
</head>
<body>

</body>
</html> 