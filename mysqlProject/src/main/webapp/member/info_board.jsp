<%@page import="mysqlProject.domain.BoardVo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="mysqlProject.domain.PageMaker"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
@SuppressWarnings("unchecked")
ArrayList<BoardVo> alist = (ArrayList<BoardVo>) request.getAttribute("alist");
PageMaker pm = (PageMaker) request.getAttribute("pm");
%>
<html>
<head>
<style>
	body{
		color: white;
	}
	a{
	cursor: pointer;
	}
	ul{
		list-style: none;
		padding: 0;
	}
	.list-bottom{
		border-bottom: 1px solid #d3db3b;
		padding: 5px 0;
		margin-bottom: 5px;
		font-size: 14px;
		color: #808080;
	}
	#numberTable{
		margin: 0 auto;
		text-align: center;
	}
	#numberTable a{
		text-decoration: none;
		color: inherit;		
	}
	#numberTable a:active {
		font-weight: bolder;
		color: blue;
		text-decoration: underline;
	}
</style>
<head>
	<meta charset="UTF-8">
	<title>타이틀</title>
	<script src="<%=request.getContextPath()%>/js/jquery-3.6.0.min.js"></script>
</head>
<br>
	<%for(BoardVo bv : alist){%>
	<ul>
		<li>
			<div class="list-title"><a onclick='window.parent.location.href="<%=request.getContextPath()%>/board/content.do?bidx=<%=bv.getBidx()%>"'><%=bv.getTitle()%> <span style="color: #ed8040">[<%=bv.getBoardComment()%>]</span></a></div>
			<div class="list-bottom">
				<span><%=bv.getBidx()%></span>
				<span style="color: #d3db3b;"><%
					if(bv.getCategory()==1){out.print("공지사항");}
					else if(bv.getCategory()==2){out.print("자유게시판");}
					else if(bv.getCategory()==3){out.print("질문게시판");}
					else if(bv.getCategory()==4){out.print("게임소식");}
					else if(bv.getCategory()==5){out.print("일상소식");}
					else if(bv.getCategory()==6){out.print("갤러리");}
				%></span>
				<span><%=bv.getWriteday()%></span>
			</div>
		</li>
	</ul>
	<%}%>
	<table id="numberTable">
			<tr>
			<td>
				<% 
				if(pm.isPrev() == true){out.println("<a href='"+request.getContextPath()+"/member/infoBoard.do?page="+(pm.getStartPage()-1)+"&midx="+session.getAttribute("midx")+"'>◀</a>");}
				%>
			</td>
			<td>
				<%
					for(int i=pm.getStartPage(); i<=pm.getEndPage();i++){
						out.println("<a href='"+request.getContextPath()+"/member/infoBoard.do?page="+i+"&midx="+session.getAttribute("midx")+"'>"+i+"</a>");
					}
				%>
			</td>
			<td>
				<% if(pm.isNext() && pm.getEndPage() >0){
					out.println("<a href='"+request.getContextPath()+"/member/infoBoard.do?page="+(pm.getEndPage()+1)+"&midx="+session.getAttribute("midx")+"'>▶</a>");
					}
				%>
			</td>
			</tr>
		</table>
<body>
</body>
</html>